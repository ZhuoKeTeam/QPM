package com.jm.android.gt.utils;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 比较耗时，请在子线程中进行操作
 */
public class ManifestParser {

    private static XMLParser manifestInfo;

    public static synchronized XMLParser parseManifestInfoByRecursion(Context context) {
        if (context == null) {
            return null;
        }
        if (manifestInfo != null) {
            return manifestInfo;
        }
        XMLParser manifestInfo = new XMLParser();
        manifestInfo.register("manifest#uses-sdk");
        manifestInfo.register("manifest#instrumentation");
        manifestInfo.register("manifest#uses-permission");
        manifestInfo.register("manifest#supports-screens");

        manifestInfo.register("manifest#application#uses-library");
        manifestInfo.register("manifest#application#meta-data");

        manifestInfo.register("manifest#application#activity#intent-filter#data");
        manifestInfo.register("manifest#application#activity#intent-filter#action");
        manifestInfo.register("manifest#application#activity#intent-filter#category");

        manifestInfo.register("manifest#application#receiver#intent-filter#data");
        manifestInfo.register("manifest#application#receiver#intent-filter#action");
        manifestInfo.register("manifest#application#receiver#intent-filter#category");

        manifestInfo.register("manifest#application#provider#intent-filter#data");
        manifestInfo.register("manifest#application#provider#intent-filter#action");
        manifestInfo.register("manifest#application#provider#intent-filter#category");

        manifestInfo.register("manifest#application#service#intent-filter#data");
        manifestInfo.register("manifest#application#service#intent-filter#action");
        manifestInfo.register("manifest#application#service#intent-filter#category");
        try {
            XMLParser.parseBinary(context, manifestInfo, context.getApplicationInfo().sourceDir, "AndroidManifest.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ManifestParser.manifestInfo = manifestInfo;
        return manifestInfo;
    }

    public static synchronized List<XMLParser> getModule(Context context, String moduleName) {
        List<XMLParser> modules = new ArrayList<>();
        if (TextUtils.isEmpty(moduleName)) {
            return modules;
        }
        XMLParser manifest = parseManifestInfoByRecursion(context);
        if (!manifest.getSonTagMap().containsKey("application")) {
            return modules;
        }
        List<XMLParser> applications = manifest.getSonTagMap().get("application");
        if (applications.size() != 1) {
            return modules;
        }
        XMLParser application = applications.get(0);
        if (!application.getSonTagMap().containsKey(moduleName)) {
            return modules;
        }
        modules.addAll(application.getSonTagMap().get(moduleName));
        return modules;
    }
}

