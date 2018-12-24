package com.zk.qpm.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用XML解析器
 */
public class XMLParser {

    private static final String FLAG_SEPARATOR = "#";
    private static final String FLAG_READ_TEXT = "@";
    private static final String ENCODING = "UTF-8";
    private static final String REFLECT_METHOD = "addAssetPath";

    // 当前解析路径
    private static final StringBuilder parsePath = new StringBuilder();
    // 是否解析namespace
    private static final boolean needParseNameSpace = false;
    // 是否需要读text
    private boolean needReadText = false;

    // 自己的标签名
    private String tagName;
    // 自己的内容
    private String text;
    // 是否已经解析完毕
    private boolean isParseComplete;
    // 路径
    private String path;
    // 层级
    private int level = 1;
    // 属性键值对
    private Map<String, String> attributeMap = new HashMap<>();
    // 子节点键值对
    private Map<String, List<XMLParser>> sonTagMap = new HashMap<>();

    /**
     * 通用解析XML方法，可以解析所有的xml结构，需要在方法调用之前正确设置{@link #register(String)}
     *
     * @param context
     * @param xmlParser 在外界注册好后传进来
     * @param in        xml的输入流
     * @return 返回已经解析完的结果
     * @throws XmlPullParserException
     * @throws IOException
     */
    public static synchronized XMLParser parse(Context context, XMLParser xmlParser, InputStream in)
            throws XmlPullParserException, IOException {
        if (context == null || xmlParser == null || in == null) {
            return null;
        }

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(in, ENCODING);
        return parse(context, xmlParser, parser);
    }

    /**
     * 解析Android的二进制xml文件，比如本APK中的AndroidManifest.xml，需要在方法调用之前正确设置{@link #register(String)}
     *
     * @param context
     * @param xmlParser         在外界注册好后传进来
     * @param binaryFilePath    二进制xml所在的目录或者压缩包的目录
     * @param binaryXmlFileName 需要解析的二进制xml文件名，比如 AndroidManifest.xml
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     * @throws ReflectiveOperationException
     */
    public static synchronized XMLParser parseBinary(Context context, XMLParser xmlParser,
                                                     String binaryFilePath, String binaryXmlFileName)
            throws XmlPullParserException, IOException, ReflectiveOperationException {
        if (context == null || xmlParser == null) {
            return null;
        }
        XmlResourceParser parser = getBinaryXmlParser(context, binaryFilePath, binaryXmlFileName);
        return parse(context, xmlParser, parser);
    }

    /**
     * xml解析模板方法
     */
    public static synchronized XMLParser parse(Context context, XMLParser xmlParser, XmlPullParser parser)
            throws XmlPullParserException, IOException {
        if (context == null || xmlParser == null || parser == null) {
            return null;
        }
        int eventType = XmlPullParser.START_DOCUMENT;
        do {
            eventType = parser.next();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    xmlParser.startParse(parser);
                    break;
                case XmlPullParser.END_TAG:
                    xmlParser.stopParse(parser);
                    break;
                default:
                    break;
            }
        } while (eventType != (XmlPullParser.END_DOCUMENT));
        return xmlParser;
    }

    /**
     * 通过AssetManager的机制来获取解析二进制xml的解析器
     *
     * @param context
     * @param binaryFilePath    二进制xml所在的目录或者压缩包的目录
     * @param binaryXmlFileName 二进制xml的文件名
     * @return 二进制xml解析器
     * @throws ReflectiveOperationException
     * @throws IOException
     */
    private static XmlResourceParser getBinaryXmlParser(Context context, String binaryFilePath, String binaryXmlFileName)
            throws ReflectiveOperationException, IOException {
        if (TextUtils.isEmpty(binaryFilePath) || TextUtils.isEmpty(binaryXmlFileName)) {
            return null;
        }
        AssetManager assetManager = context.getAssets();
        Method addAssetPath = assetManager.getClass().getMethod(REFLECT_METHOD, String.class);
        addAssetPath.setAccessible(true);
        int cookie = (int) addAssetPath.invoke(assetManager, binaryFilePath);
        return assetManager.openXmlResourceParser(cookie, binaryXmlFileName);
    }

    /**
     * 在解析之前，需要先注册需要解析的标签，注册采用链式注册的方式，
     * 用{@link #FLAG_SEPARATOR} 来隔开父与子的标签，
     * 用{@link #FLAG_READ_TEXT} 来作为标签结尾来设置该标签是否需要读取text数据
     *
     * 比如解析AndroidManifest时：
     * XMLParser manifestInfo = new XMLParser();
     * manifestInfo.register("manifest#uses-sdk");
     * manifestInfo.register("manifest#instrumentation");
     * manifestInfo.register("manifest#uses-permission");
     * manifestInfo.register("manifest#supports-screens");
     * manifestInfo.register("manifest#application#uses-library");
     * manifestInfo.register("manifest#application#meta-data");
     * manifestInfo.register("manifest#application#activity#intent-filter#data");
     * manifestInfo.register("manifest#application#activity#intent-filter#action");
     * manifestInfo.register("manifest#application#activity#intent-filter#category");
     * manifestInfo.register("manifest#application#receiver#intent-filter#data");
     * manifestInfo.register("manifest#application#receiver#intent-filter#action");
     * manifestInfo.register("manifest#application#receiver#intent-filter#category");
     * manifestInfo.register("manifest#application#provider#intent-filter#data");
     * manifestInfo.register("manifest#application#provider#intent-filter#action");
     * manifestInfo.register("manifest#application#provider#intent-filter#category");
     * manifestInfo.register("manifest#application#service#intent-filter#data");
     * manifestInfo.register("manifest#application#service#intent-filter#action");
     * manifestInfo.register("manifest#application#service#intent-filter#category");
     *
     * 比如解析SharedPreferences时：
     * XMLParser parser = new XMLParser();
     * parser.register("map#boolean");
     * parser.register("map#long");
     * parser.register("map#float");
     * parser.register("map#string@");
     * parser.register("map#int");
     * parser.register("map#set#string@");
     */
    public void register(String action) {
        if (TextUtils.isEmpty(action)) {
            return;
        }
        // 没有分隔符，只是赋值自己的tagName
        if (!action.contains(FLAG_SEPARATOR)) {
            path = action;
            tagName = action;
            return;
        }
        String[] tagNames = action.split(FLAG_SEPARATOR);
        // 依然没有分隔符
        if (tagNames.length < 2) {
            path = tagNames[0];
            tagName = tagNames[0];
            return;
        }
        // 赋值tagName和path
        tagName = tagNames[level - 1];
        StringBuilder pathBuilder = new StringBuilder();
        for (int i = 0; i < level; i++) {
            pathBuilder.append(tagNames[i]).append(FLAG_SEPARATOR);
        }
        if (pathBuilder.length() != 0) {
            pathBuilder.setLength(pathBuilder.length() - FLAG_SEPARATOR.length());
        }
        path = pathBuilder.toString();
        // 判断是否需要读取文本，因为这里pull有BUG，需要特殊处理
        if (tagName.endsWith(FLAG_READ_TEXT)){
            needReadText = true;
            tagName = tagName.substring(0, tagName.lastIndexOf(FLAG_READ_TEXT));
        }
        if (path.endsWith(FLAG_READ_TEXT)){
            path = path.substring(0, path.lastIndexOf(FLAG_READ_TEXT));
        }
        // 如果没有子节点，就返回
        if (level >= tagNames.length) {
            return;
        }
        if (tagNames[level].endsWith(FLAG_READ_TEXT)){
            tagNames[level] = tagNames[level].substring(0, tagNames[level].lastIndexOf(FLAG_READ_TEXT));
        }
        // 添加子节点，并预置一个解析对象，递归调用本方法注册子标签
        if (!sonTagMap.containsKey(tagNames[level])) {
            List<XMLParser> sonTags = new ArrayList<>();
            sonTagMap.put(tagNames[level], sonTags);
            XMLParser son = new XMLParser();
            son.level = level + 1;
            son.register(action);
            sonTags.add(son);
        } else {
            List<XMLParser> sonTags = sonTagMap.get(tagNames[level]);
            XMLParser son = sonTags.get(0);
            son.register(action);
        }

        if (parsePath.length() > 0) {
            parsePath.setLength(0);
        }
    }

    /**
     * 递归解析开始标签，内部完成attribute属性的解析和子标签的解析
     */
    private void startParse(XmlPullParser parser) throws XmlPullParserException {
        String parseTagName = parser.getName();
        if (TextUtils.isEmpty(tagName) || TextUtils.isEmpty(parseTagName)) {
            throw new XmlPullParserException("tagName is Empty");
        }
        // 设置当前解析路径，用于找到具体的解析器解析
        if (parsePath.length() == 0) {
            parsePath.append(parseTagName);
        } else if (!parsePath.toString().endsWith(parseTagName)) {
            parsePath.append(FLAG_SEPARATOR).append(parseTagName);
        }

        // 首先解析自己的键值对
        if (tagName.equals(parseTagName)) {
            parseAttribute(parser);
            text = safeNextText(parser);
        } else {
            parseSonTag(parser, true);
        }
    }

    /**
     * 解析自己的attribute属性
     */
    private void parseAttribute(XmlPullParser parser) throws XmlPullParserException {
        int attributeCount = parser.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            String attributeNamespace = parser.getAttributeNamespace(i);
            String attributeName = parser.getAttributeName(i);
            String attributeValue = parser.getAttributeValue(i);
            if (TextUtils.isEmpty(attributeName)) {
                throw new XmlPullParserException("attributeName is null");
            }
            if (TextUtils.isEmpty(attributeValue)) {
                continue;
            }
            if (needParseNameSpace) {
                String key = TextUtils.isEmpty(attributeNamespace)
                        ? attributeName
                        : attributeNamespace + ":" + attributeName;
                attributeMap.put(key, attributeValue);
            } else {
                attributeMap.put(attributeName, attributeValue);
            }
        }
    }

    private String safeNextText(XmlPullParser parser) {
        try {
            if (needReadText) {
                String result = parser.nextText();
                // 解决PULL解析的BUG，在调用了nextText方法后，会默认跳过该END_TAG，需要手动触发
                if (parser.getEventType() == XmlPullParser.END_TAG) {
                    stopParse(parser);
                }
                return result;
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析子标签
     */
    private void parseSonTag(XmlPullParser parser, boolean isStartTag) throws XmlPullParserException {
        // 首先匹配当前tagName
        String[] parseTags = parsePath.toString().split(FLAG_SEPARATOR);
        if (parseTags.length < level) {
            return;
        }
        // 当前tag是否与路径匹配
        if (!tagName.equals(parseTags[level - 1])) {
            return;
        }
        // 查看子类
        if (parseTags.length < level + 1) {
            return;
        }
        String sonTag = parseTags[level];
        List<XMLParser> sonTags = null;
        if (!sonTagMap.containsKey(sonTag)) {
            sonTags = new ArrayList<>();
            sonTagMap.put(sonTag, sonTags);
        } else {
            sonTags = sonTagMap.get(sonTag);
        }
        if (sonTags.isEmpty()) {
            if (isStartTag) {
                XMLParser son = new XMLParser();
                son.level = level + 1;
                son.register(path + FLAG_SEPARATOR + sonTag);
                son.startParse(parser);
                sonTags.add(son);
            }
            return;
        }
        // 取出上一个未完成的
        XMLParser lastSon = sonTags.get(sonTags.size() - 1);
        if (lastSon.isParseComplete) {
            if (isStartTag) {
                //没有未完成的，则新建
                XMLParser son = new XMLParser();
                son.level = level + 1;
                String action = path + FLAG_SEPARATOR + sonTag;
                // 继承上一个结点的needReadText属性
                if (lastSon.needReadText){
                    action += FLAG_READ_TEXT;
                }
                son.register(action);
                son.startParse(parser);
                sonTags.add(son);
            }
        } else {
            if (isStartTag) {
                lastSon.startParse(parser);
            } else {
                lastSon.stopParse(parser);
            }
        }
    }

    /**
     * 递归解析结束标签
     */
    private void stopParse(XmlPullParser parser) throws XmlPullParserException {
        String parseTagName = parser.getName();
        if (TextUtils.isEmpty(tagName) || TextUtils.isEmpty(parseTagName)) {
            throw new XmlPullParserException("tagName is Empty");
        }
        if (tagName.equals(parseTagName)) {
            isParseComplete = true;
        } else {
            parseSonTag(parser, false);
        }
        // 设置解析路径
        if (parsePath.toString().endsWith(parseTagName)) {
            if (parsePath.lastIndexOf(FLAG_SEPARATOR) != -1) {
                parsePath.setLength(parsePath.lastIndexOf(FLAG_SEPARATOR));
            } else {
                parsePath.setLength(0);
            }
        }
    }


    @Override
    public String toString() {
        // 先输出自己的键值对
        StringBuilder sb = new StringBuilder();
        sb.append(tagName).append(":");
        for (Map.Entry<String, String> entry : attributeMap.entrySet()) {
            if (!TextUtils.isEmpty(entry.getKey()) && !TextUtils.isEmpty(entry.getValue())) {
                sb.append("[").append(entry.getKey()).append("=").append(entry.getValue()).append("]").append(",");
            }
        }
        sb.setLength(sb.length() - 1);
        sb.append("\n");
        for (Map.Entry<String, List<XMLParser>> sonTags : sonTagMap.entrySet()) {
            for (XMLParser son : sonTags.getValue()) {
                String sonString = son.toString();
                if (!TextUtils.isEmpty(sonString)) {
                    for (int i = 0; i < level; i++) {
                        sb.append("\t");
                    }
                    sb.append(sonString);
                }
            }
        }
        if (sb.length() == tagName.length() + "\n".length()) {
            return "";
        }
        return sb.toString();
    }

    public String getTagName() {
        return tagName;
    }

    public String getText() {
        return text;
    }

    public Map<String, String> getAttributeMap() {
        return attributeMap;
    }

    public Map<String, List<XMLParser>> getSonTagMap() {
        return sonTagMap;
    }
}
