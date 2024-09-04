package com.zk.qpm.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.StringDef;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  SP文件工具
 */
public class PrefsParser {

    private static final String SPLIT = ",";
    private static final String SUFFIX_XML = ".xml";

    public static final PrefXMLParser PARSER_XML = new PrefXMLParser();
    public static final PrefSPParser PARSER_SP = new PrefSPParser();

    /**
     * 得到所有的SP文件
     */
    private static File[] getAllPrefFiles(Context context) {
        File filesDir = context.getFilesDir();
        if (filesDir == null || filesDir.getParentFile() == null) {
            return new File[]{};
        }
        File dataDir = filesDir.getParentFile();
        File[] sharedPrefDirs = dataDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return TextUtils.equals(name, "shared_prefs");
            }
        });
        if (sharedPrefDirs.length == 0) {
            return new File[]{};
        }
        File[] sharedPrefFiles = sharedPrefDirs[0].listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return !TextUtils.isEmpty(name) && name.endsWith(SUFFIX_XML);
            }
        });
        return sharedPrefFiles;
    }

    /**
     * 得到所有的SP文件转换的实体类映射表
     */
    public static Map<File, List<PrefItem>> getAllPrefs(Context context) {
        return getAllPrefs(context, PARSER_SP);
    }

    /**
     * 得到所有的SP文件转换的实体类映射表
     */
    public static Map<File, List<PrefItem>> getAllPrefs(Context context, IPrefParser parser) {
        File[] allPrefFiles = getAllPrefFiles(context);
        Map<File, List<PrefItem>> maps = new HashMap<>();
        for (File file : allPrefFiles) {
            List<PrefItem> items = getPrefs(context, file, parser);
            if (!items.isEmpty()) {
                maps.put(file, items);
            }
        }
        return maps;
    }

    /**
     * 得到指定SP文件转换的实体类列表
     */
    public static List<PrefItem> getPrefs(Context context, String prefName, IPrefParser parser) {
        List<PrefItem> items = new ArrayList<>();
        if (TextUtils.isEmpty(prefName)) {
            return items;
        }
        if (!prefName.endsWith(SUFFIX_XML)) {
            prefName = prefName + SUFFIX_XML;
        }
        Map<File, List<PrefItem>> allPrefs = getAllPrefs(context, parser);
        for (File file : allPrefs.keySet()) {
            if (!TextUtils.equals(file.getName(), prefName)) {
                continue;
            }
            return getPrefs(context, file, parser);
        }
        return items;
    }

    /**
     * 得到指定SP文件转换的实体类列表
     */
    public static List<PrefItem> getPrefs(Context context, File file, IPrefParser parser) {
        return parser.parse(context, file);
    }

    /**
     *  将SP实体类更新或者写入文件
     * @return true：写入成功；false：写入失败，可能是格式转换异常
     */
    public static boolean writePrefs(Context context, String prefName, PrefItem item) {
        if (context == null || TextUtils.isEmpty(prefName) || item == null
                || TextUtils.isEmpty(item.key) || TextUtils.isEmpty(item.value)
                || TextUtils.isEmpty(item.type)) {
            return false;
        }
        if (prefName.endsWith(SUFFIX_XML)){
            prefName = prefName.substring(0, prefName.lastIndexOf(SUFFIX_XML));
        }
        SharedPreferences sp = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        if (sp == null) {
            return false;
        }
        SharedPreferences.Editor editor = sp.edit();
        switch (item.type){
            case IPrefTypeDefine.TYPE_BOOLEAN:
                if (!TextUtils.equals(item.value, "true")
                        && !TextUtils.equals(item.value, "false")){
                    return false;
                }
                editor.putBoolean(item.key, Boolean.parseBoolean(item.value));
                break;
            case IPrefTypeDefine.TYPE_LONG:
                try {
                    editor.putLong(item.key, Long.parseLong(item.value));
                } catch (NumberFormatException e){
                    return false;
                }
                break;
            case IPrefTypeDefine.TYPE_FLOAT:
                try {
                    editor.putFloat(item.key, Float.parseFloat(item.value));
                }catch (NumberFormatException e){
                    return false;
                }
                break;
            case IPrefTypeDefine.TYPE_STRING:
                editor.putString(item.key, item.value);
                break;
            case IPrefTypeDefine.TYPE_INT:
                try{
                    editor.putInt(item.key, Integer.parseInt(item.value));
                }catch (NumberFormatException e){
                    return false;
                }
                break;
            case IPrefTypeDefine.TYPE_SET:
                try{
                    String[] sons = item.value.split(SPLIT);
                    if (sons.length == 0){
                        return false;
                    }
                    Set<String> set = new HashSet<>();
                    for(String son : sons){
                        set.add(son);
                    }
                    editor.putStringSet(item.key, set);
                }catch (Exception e){
                    return false;
                }
                break;
            default:
                return false;
        }
        return editor.commit();
    }

    /**
     * 通过 {@link android.content.SharedPreferences} 进行解析
     */
    public static class PrefSPParser implements IPrefParser {

        @Override
        public List<PrefItem> parse(Context context, File file) {
            List<PrefItem> items = new ArrayList<>();
            if (context == null || file == null || !file.exists()) {
                return items;
            }
            String name = file.getName();
            if (name.endsWith(SUFFIX_XML)) {
                name = name.substring(0, name.lastIndexOf(SUFFIX_XML));
            }
            SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
            if (sp == null) {
                return items;
            }
            Map<String, ?> attributeMap = sp.getAll();
            for (Map.Entry<String, ?> entry : attributeMap.entrySet()) {
                String key = entry.getKey();
                if (TextUtils.isEmpty(key)) {
                    continue;
                }
                PrefItem item = new PrefItem();
                item.key = key;
                Object value = entry.getValue();
                if (value instanceof Boolean) {
                    item.type = IPrefTypeDefine.TYPE_BOOLEAN;
                    item.value = value.toString();
                } else if (value instanceof Long) {
                    item.type = IPrefTypeDefine.TYPE_LONG;
                    item.value = value.toString();
                } else if (value instanceof Float) {
                    item.type = IPrefTypeDefine.TYPE_FLOAT;
                    item.value = value.toString();
                } else if (value instanceof String) {
                    item.type = IPrefTypeDefine.TYPE_STRING;
                    item.value = value.toString();
                } else if (value instanceof Integer) {
                    item.type = IPrefTypeDefine.TYPE_INT;
                    item.value = value.toString();
                } else if (value instanceof Set) {
                    item.type = IPrefTypeDefine.TYPE_SET;
                    Set valueSet = (Set) value;
                    StringBuilder valueSb = new StringBuilder();
                    for (Object object : valueSet) {
                        valueSb.append(object.toString()).append(SPLIT);
                    }
                    if (valueSb.length() > 0) {
                        valueSb.setLength(valueSb.length() - SPLIT.length());
                    }
                    item.value = valueSb.toString();
                }
                items.add(item);
            }
            return items;
        }
    }

    /**
     * 通过 {@link XMLParser} 进行解析XML
     */
    public static class PrefXMLParser implements IPrefParser {

        @Override
        public List<PrefItem> parse(Context context, File file) {
            List<PrefItem> items = new ArrayList<>();
            XMLParser parser = parsePrefFile(context, file);
            if (parser != null) {
                items.addAll(transferParser2Item(parser));
            }
            return items;
        }

        private XMLParser parsePrefFile(Context context, File file) {
            if (context == null || file == null || !file.exists()) {
                return null;
            }
            XMLParser parser = new XMLParser();
            parser.register("map#boolean");
            parser.register("map#long");
            parser.register("map#float");
            parser.register("map#string@");
            parser.register("map#int");
            parser.register("map#set#string@");
            try {
                return XMLParser.parse(context, parser, new FileInputStream(file));
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        private List<PrefItem> transferParser2Item(XMLParser parser) {
            List<PrefItem> items = new ArrayList<>();
            if (parser == null) {
                return items;
            }
            List<PrefItem> booleanItem = transferBaiscType(parser, IPrefTypeDefine.TYPE_BOOLEAN);
            if (booleanItem != null) {
                items.addAll(booleanItem);
            }
            List<PrefItem> longItem = transferBaiscType(parser, IPrefTypeDefine.TYPE_LONG);
            if (longItem != null) {
                items.addAll(longItem);
            }
            List<PrefItem> floatItem = transferBaiscType(parser, IPrefTypeDefine.TYPE_FLOAT);
            if (floatItem != null) {
                items.addAll(floatItem);
            }
            List<PrefItem> intItem = transferBaiscType(parser, IPrefTypeDefine.TYPE_INT);
            if (intItem != null) {
                items.addAll(intItem);
            }
            List<PrefItem> stringItem = transferStringType(parser, IPrefTypeDefine.TYPE_STRING);
            if (stringItem != null) {
                items.addAll(stringItem);
            }
            List<PrefItem> setItem = transferSetType(parser, IPrefTypeDefine.TYPE_SET);
            if (setItem != null) {
                items.addAll(setItem);
            }
            return items;
        }

        private List<PrefItem> transferBaiscType(XMLParser parser, String typeStr) {
            List<PrefItem> items = new ArrayList<>();
            if (parser.getSonTagMap().containsKey(typeStr)) {
                List<XMLParser> sons = parser.getSonTagMap().get(typeStr);
                for (XMLParser son : sons) {
                    Map<String, String> attributeMap = son.getAttributeMap();
                    String key = attributeMap.get("name");
                    String value = attributeMap.get("value");
                    if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                        PrefItem item = new PrefItem();
                        item.key = key;
                        item.value = value;
                        item.type = typeStr;
                        items.add(item);
                    }
                }
            }
            return items;
        }

        private List<PrefItem> transferStringType(XMLParser parser, String typeStr) {
            List<PrefItem> items = new ArrayList<>();
            if (parser.getSonTagMap().containsKey(typeStr)) {
                List<XMLParser> sons = parser.getSonTagMap().get(typeStr);
                for (XMLParser son : sons) {
                    Map<String, String> attributeMap = son.getAttributeMap();
                    String key = attributeMap.get("name");
                    if (TextUtils.isEmpty(key)) {
                        continue;
                    }
                    String value = son.getText();
                    if (TextUtils.isEmpty(value)) {
                        continue;
                    }
                    PrefItem item = new PrefItem();
                    item.key = key;
                    item.value = value;
                    item.type = typeStr;
                    items.add(item);
                }
            }
            return items;
        }

        private List<PrefItem> transferSetType(XMLParser parser, String typeStr) {
            List<PrefItem> items = new ArrayList<>();
            if (parser.getSonTagMap().containsKey(typeStr)) {
                List<XMLParser> sons = parser.getSonTagMap().get(typeStr);
                for (XMLParser son : sons) {
                    Map<String, String> attributeMap = son.getAttributeMap();
                    String key = attributeMap.get("name");
                    if (TextUtils.isEmpty(key)) {
                        continue;
                    }
                    if (!son.getSonTagMap().containsKey(IPrefTypeDefine.TYPE_STRING)) {
                        continue;
                    }
                    List<XMLParser> grandSons = son.getSonTagMap().get(IPrefTypeDefine.TYPE_STRING);
                    StringBuilder value = new StringBuilder();
                    for (XMLParser grandSon : grandSons) {
                        if (TextUtils.isEmpty(grandSon.getText())) {
                            continue;
                        }
                        value.append(grandSon.getText()).append(SPLIT);
                    }
                    if (value.length() > 0) {
                        value.setLength(value.length() - SPLIT.length());
                    }
                    if (value.length() == 0) {
                        continue;
                    }
                    PrefItem item = new PrefItem();
                    item.key = key;
                    item.value = value.toString();
                    item.type = typeStr;
                    items.add(item);
                }
            }
            return items;
        }
    }

    public interface IPrefParser {

        List<PrefItem> parse(Context context, File file);

    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({IPrefTypeDefine.TYPE_BOOLEAN, IPrefTypeDefine.TYPE_LONG,
            IPrefTypeDefine.TYPE_FLOAT, IPrefTypeDefine.TYPE_STRING,
            IPrefTypeDefine.TYPE_INT, IPrefTypeDefine.TYPE_SET})
    public @interface IPrefTypeDefine{
        String TYPE_BOOLEAN = "boolean";
        String TYPE_LONG = "long";
        String TYPE_FLOAT = "float";
        String TYPE_STRING = "string";
        String TYPE_INT = "int";
        String TYPE_SET = "set";
    }

    public static class PrefItem {

        public @IPrefTypeDefine String type;
        public String key;
        public String value;

        public PrefItem() {
        }

        public PrefItem(String type, String key, String value) {
            this.type = type;
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "PrefItem{" +
                    "type='" + type + '\'' +
                    ", key='" + key + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
