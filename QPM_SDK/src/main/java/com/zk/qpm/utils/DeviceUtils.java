/*
 * Tencent is pleased to support the open source community by making
 * Tencent GT (Version 2.4 and subsequent versions) available.
 *
 * Notwithstanding anything to the contrary herein, any previous version
 * of Tencent GT shall not be subject to the license hereunder.
 * All right, title, and interest, including all intellectual property rights,
 * in and to the previous version of Tencent GT (including any and all copies thereof)
 * shall be owned and retained by Tencent and subject to the license under the
 * Tencent GT End User License Agreement (http://gt.qq.com/wp-content/EULA_EN.html).
 * 
 * Copyright (C) 2015 THL A29 Limited, a Tencent company. All rights reserved.
 * 
 * Licensed under the MIT License (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 * 
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.zk.qpm.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 设备信息工具类。
 */
public class DeviceUtils {

	/**
	 * 获取设备型号
	 * 
	 * @return 设备型号
	 */
	public static String getDevModel() {
		return Build.MODEL;
	}

	public static String getHardware() {
		return Build.HARDWARE;
	}

	/**
	 * 获取SDK版本
	 * 
	 * @return SDK版本
	 */
	public static String getSDKVersion() {
		String version = "";
		version = Build.VERSION.RELEASE;
		return version;
	}

	/**
	 * 获取设备dip(device independent pixels)
	 *
	 * @param mactivity
	 *            应用程序最前端的activity对象
	 * @return 设备dip
	 */
	public static int getDevDensityDpi(Activity mactivity) {
		DisplayMetrics metric = new DisplayMetrics();
		mactivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int densityDpi = metric.densityDpi;
		return densityDpi;
	}

	/**
	 * 获取设备density
	 *
	 * @param mactivity
	 *            应用程序最前端的activity对象
	 * @return 设备density
	 */
	public static float getDevDensity(Activity mactivity) {
		DisplayMetrics metric = new DisplayMetrics();
		mactivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		float density = metric.density;
		return density;
	}

	/**
	 * 获取设备比例因子
	 *
	 * @param mactivity
	 *            应用程序最前端的activity对象
	 * @return 设备比例因子
	 */
	public static float getDevScaledDensity(Activity mactivity) {
		DisplayMetrics metric = new DisplayMetrics();
		mactivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		float scaledDensity = metric.scaledDensity;
		return scaledDensity;
	}

	/**
	 * 获取设备屏幕宽度
	 *
	 * @param mactivity
	 *            应用程序最前端的activity对象
	 * @return 设备屏幕宽度
	 */
	public static int getDisplayWidth(Activity mactivity) {
		return mactivity.getWindowManager().getDefaultDisplay().getWidth();
	}

	/**
	 * 获取设备屏幕高度
	 *
	 * @param mactivity
	 *            应用程序最前端的activity对象
	 * @return 设备屏幕高度
	 */
	public static int getDisplayHeight(Activity mactivity) {
		return mactivity.getWindowManager().getDefaultDisplay().getHeight();
	}

	/**
	 * 获取状态栏高度
	 *
	 * @param context
	 *            应用程序的上下文环境
	 * @return 状态栏高度
	 */
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0;
		int sbar = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sbar;
	}

	/**
	 * 获取手机本地IP
	 */
	public static void getLocalIP() {
		String ipaddress = "";
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						ipaddress = ipaddress + ";"
								+ inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
		}
	}

	public static String getABI() {
		String CPU_ABI = Build.CPU_ABI;
		String CPU_ABI2 = Build.CPU_ABI2;
		
		

		return CPU_ABI + " " + CPU_ABI2;
	}
}
