package com.education.officertopline.log;

import com.education.officertopline.app.AppConfigFile;
import com.socks.library.KLog;

/**
 * 日志工具类 simple introduction
 *
 * <p>
 * detailed comment
 * 
 * @author CWI-APST Email:lei.zhang@symboltech.com 2015年10月19日
 * @see
 * @since 1.0
 */
public class LogUtil {
	public static void v(String tag, String msg) {
			KLog.v(tag, msg);
	}

	public static void v(String msg) {
			KLog.v(msg);
	}

	public static void d(String tag, String msg) {
			KLog.d(tag, msg);
	}

	public static void d(String msg) {
		KLog.d(msg);
	}

	public static void i(String tag, String msg) {
		KLog.i(tag, msg);
	}

	public static void i(String msg) {
		KLog.i(msg);
	}

	public static void w(String tag, String msg) {
			KLog.w(tag, msg);
	}

	public static void w(String msg) {
			KLog.w(msg);
	}

	public static void e(String tag, String msg) {
		KLog.e(tag, msg);
	}

	public static void e(String msg) {
		KLog.e(msg);
	}
}
