package com.jm.android.gt.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 模糊匹配字符串工具，支持忽略大小写
 */
public class FuzzyMatchUtil {

    public interface IMatchResultAdapter<T> {

        /**
         * 从元数据提取字符串进行比较
         */
        String extractStringFromSource(T t);

        /**
         * 将匹配结果注入回原数据
         */
        void fillResultToSource(T t, List<Integer> matchPosition);
    }

    /**
     *  传入指定对象，返回指定对象
     */
    public static<T> List<T> match(List<T> source, String key, boolean ignoreCase, IMatchResultAdapter<T> adapter) {
        if (TextUtils.isEmpty(key) || adapter == null) {
            return source;
        }
        List<T> result = new ArrayList<>();
        for (T each : source) {
            List<Integer> matchIndex = findMatchIndex(adapter.extractStringFromSource(each), key, ignoreCase);
            if (matchIndex.size() == key.length()) {
                // 匹配成功
                adapter.fillResultToSource(each, matchIndex);
                result.add(each);
            }
        }
        return result;
    }

    /**
     *  只返回字符串
     */
    public static List<String> matchJustReturnString(List<String> source, String key, boolean ignoreCase) {
        if (TextUtils.isEmpty(key)) {
            return new ArrayList<>(source);
        }
        List<String> result = new ArrayList<>();
        for (String each : source) {
            List<Integer> matchIndex = findMatchIndex(each, key, ignoreCase);
            if (matchIndex.size() == key.length()) {
                result.add(each);
            }
        }
        return result;
    }

    /**
     * 查找满足条件的字符串下标，如果返回的下标集合的个数和 key 值的个数不同，则认为匹配失败
     */
    private static List<Integer> findMatchIndex(String source, String key, boolean ignoreCase) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < key.length(); i++) {
            String ch = String.valueOf(key.charAt(i));
            String upper = ch.toUpperCase();
            String lower = ch.toLowerCase();

            int index = 0;
            if (result.size() != 0) {
                // 找到上一个查询位置的下一个起始坐标
                index = result.get(result.size() - 1) + 1;
                if (index >= source.length()) {
                    // 上一次搜索已经到达末尾了
                    continue;
                }
            }

            int currentMinIndex = -1;

            if (ignoreCase) {
                // 首先查找大写的索引
                currentMinIndex = source.indexOf(upper, index);

                if (!upper.equals(lower)) {
                    // 有大小写区别再查找小写的索引
                    int temp = source.indexOf(lower, index);
                    if (temp != -1) {
                        if (currentMinIndex == -1) {
                            currentMinIndex = temp;
                        } else if (temp < currentMinIndex) {
                            currentMinIndex = temp;
                        }
                    }
                }
            } else {
                currentMinIndex = source.indexOf(ch, index);
            }

            if (currentMinIndex == -1) {
                continue;
            }

            result.add(currentMinIndex);
        }
        return result;
    }

}
