package cn.gowild.robotlife.eve;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mars.xlog.Xlog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class Logger {

	public static final int MESSAGE_MAX_LENGHT = 1024;
	public static final int LEVEL_ALL = 0;
	public static final int LEVEL_VERBOSE = 0;
	public static final int LEVEL_DEBUG = 1;
	public static final int LEVEL_INFO = 2;
	public static final int LEVEL_WARNING = 3;
	public static final int LEVEL_ERROR = 4;
	public static final int LEVEL_FATAL = 5;
	public static final int LEVEL_NONE = 6;
	private static int LOWEST_LOG_LEVEL = LEVEL_ALL;
	private static int LOGGER_LEVEL = LOWEST_LOG_LEVEL;

	private static boolean          bSaveLog           = false;
	private static String LOG_FILE_PATH      = "/log/com.gowild.mobileclient/";
	private static SimpleDateFormat dateFormatFileName = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
	private static SimpleDateFormat dateFormat         = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
	private static StringBuffer mSpecialTags = new StringBuffer();
	private static Context mContext;

	static {
		dateFormatFileName.applyPattern("yyyy-MM-dd");
		dateFormat.applyPattern("yyyy-MM-dd  HH:mm:ss.SSS");
	}

	private static Context sContext;
	public static void setSaveLog(boolean save, Context context) {
		bSaveLog = save;
		sContext = context;
	}

	public static File checkFileExist() {
		String environment = Environment.getExternalStorageDirectory().getAbsolutePath();
		Calendar calendar = Calendar.getInstance();
		String filePath  = environment + LOG_FILE_PATH + dateFormatFileName.format(calendar.getTime()) + "/";
		File file = new File(filePath);
		if (!file.exists() || !file.isDirectory()) {
			file.delete();
			file.mkdirs();
		}
		String fileName = "log.txt";
		file = new File(filePath + fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public static void i(String tag, String message) {
		if(message==null){
			return;
		}
		if (message.length() > MESSAGE_MAX_LENGHT) {
			while (message.length() > MESSAGE_MAX_LENGHT) {
				final String printMsg = message.substring(0, MESSAGE_MAX_LENGHT);
				StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
				com.tencent.mars.xlog.Log.i(tag,rebuildMsg(stackTraceElement,printMsg));
				message = message.substring(MESSAGE_MAX_LENGHT, message.length());
			}
		}

		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
		com.tencent.mars.xlog.Log.i(tag,rebuildMsg(stackTraceElement,message));
	}

	public static void e(String tag, String message) {
		if(message==null){
			return;
		}

		if (message.length() > MESSAGE_MAX_LENGHT) {
			while (message.length() > MESSAGE_MAX_LENGHT) {
				final String printMsg = message.substring(0, MESSAGE_MAX_LENGHT);
				StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
				com.tencent.mars.xlog.Log.e(tag,rebuildMsg(stackTraceElement,printMsg));
				message = message.substring(MESSAGE_MAX_LENGHT, message.length());
			}
		}
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
		com.tencent.mars.xlog.Log.e(tag,rebuildMsg(stackTraceElement,message));
	}

	public static void d(String tag, String message) {
		if(message==null){
			return;
		}

		if (message.length() > MESSAGE_MAX_LENGHT) {
			while (message.length() > MESSAGE_MAX_LENGHT) {
				final String printMsg = message.substring(0, MESSAGE_MAX_LENGHT);
				StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
				com.tencent.mars.xlog.Log.d(tag,rebuildMsg(stackTraceElement,printMsg));
				message = message.substring(MESSAGE_MAX_LENGHT, message.length());
			}
		}

		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
		com.tencent.mars.xlog.Log.d(tag,rebuildMsg(stackTraceElement,message));
	}

	public static void w(String tag, String message) {
		if(message==null){
			return;
		}

		if (message.length() > MESSAGE_MAX_LENGHT) {
			while (message.length() > MESSAGE_MAX_LENGHT) {
				final String printMsg = message.substring(0, MESSAGE_MAX_LENGHT);
				StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
				com.tencent.mars.xlog.Log.w(tag,rebuildMsg(stackTraceElement,printMsg));
				message = message.substring(MESSAGE_MAX_LENGHT, message.length());
			}
		}
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
		com.tencent.mars.xlog.Log.w(tag,rebuildMsg(stackTraceElement,message));
//		}
	}


	public static void v(String tag, String message) {
		if(message==null){
			return;
		}

		if (message.length() > MESSAGE_MAX_LENGHT) {
			while (message.length() > MESSAGE_MAX_LENGHT) {
				final String printMsg = message.substring(0, MESSAGE_MAX_LENGHT);
				StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
				com.tencent.mars.xlog.Log.v(tag,rebuildMsg(stackTraceElement,printMsg));
				message = message.substring(MESSAGE_MAX_LENGHT, message.length());
			}
		}
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
		com.tencent.mars.xlog.Log.v(tag,rebuildMsg(stackTraceElement,message));
	}

	private static void print(int priority, String tag, String msg) {
		Log.println(priority, tag, msg);
		StringBuffer buffer = new StringBuffer();
		buffer.append("\t").append(getPriorityString(priority)).append("\t").append(tag).append("\t").append(msg);
	}

	private static String getPriorityString(int priority) {
		switch (priority) {
			case Logger.LEVEL_ALL:
				return "A";
			case Logger.LEVEL_INFO:
				return "I";
			case Logger.LEVEL_WARNING:
				return "W";
			case Logger.LEVEL_DEBUG:
				return "D";
			case Logger.LEVEL_ERROR:
				return "E";
			default:
				return "V";
		}
	}

	public static void setLoggerEnable(boolean enable) {
		LOGGER_LEVEL = enable ? LEVEL_ALL : LEVEL_NONE;
	}

	public static void addSpecialTag(String tag) {
		if (TextUtils.isEmpty(tag)) {
			return;
		}
		mSpecialTags.append(tag);
	}


	public static void init(Context context, String dir,String suffix) {
		mContext = context;
		Xlog.setConsoleLogOpen(true);
		Xlog.appenderOpen(Xlog.LEVEL_VERBOSE, Xlog.AppednerModeAsync, "", dir, suffix);
		com.tencent.mars.xlog.Log.setLogImp(new Xlog());
		com.tencent.mars.xlog.Log.appenderFlush(true);
	}

	public static void close() {
		com.tencent.mars.xlog.Log.appenderClose();
	}

	static {
		System.loadLibrary("stlport_shared");
		System.loadLibrary("marsxlog");
	}

	public static void v(String tag, int userId, int seriesNum, String module, String message) {
		reportLog(tag, userId, seriesNum, LEVEL_VERBOSE, module, message);
	}

	public static void d(String tag, int userId, int seriesNum, String module, String message) {
		reportLog(tag, userId, seriesNum, LEVEL_DEBUG, module, message);
	}

	public static void i(String tag, int userId, int seriesNum, String module, String message) {
		reportLog(tag, userId, seriesNum, LEVEL_INFO, module, message);
	}

	public static void w(String tag, int userId, int seriesNum, String module, String message) {
		reportLog(tag, userId, seriesNum, LEVEL_WARNING, module, message);
	}

	public static void e(String tag, int userId, int seriesNum, String module, String message) {
		reportLog(tag, userId, seriesNum, LEVEL_ERROR, module, message);
	}

	/**
	 * 上报日志
	 * 上报格式：[用户Id][SeriesNumber][时间戳][日志级别][日志来源/模块][日志类型/子模块][当前版本][日志正文]
	 * [18682128689][123456][2018-01-02 21:30:52.223][D][iOS][Normal][V_2.1.5][{...}]
	 */
	public static void reportLog(String tag, int userId, int seriesNum, int level, String module, String message) {
		if (message.length() > MESSAGE_MAX_LENGHT) {
			while (message.length() > MESSAGE_MAX_LENGHT) {
				final String printMsg = message.substring(0, MESSAGE_MAX_LENGHT);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String log = String.format("[%d][%d][%s][%s][Android][%s][%s]\n[%s]", userId, seriesNum, format.format(new Date()), getPriorityString(level), module, getVerisonName(), printMsg);
				Xlog.logWrite2(level, tag, "", module, 0, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
				message = message.substring(MESSAGE_MAX_LENGHT, message.length());
			}
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String log = String.format("[%d][%d][%s][%s][Android][%s][%s][%s]", userId, seriesNum, format.format(new Date()), getPriorityString(level), module, getVerisonName(), message);
		Xlog.logWrite2(level, tag, "", module, 0, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), log);
	}

	private static String getVerisonName() {
		try {
			if(mContext != null) {
				return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Rebuild Log Msg
	 *
	 * @param msg msg
	 * @return String
	 */
	private static String rebuildMsg(StackTraceElement stackTraceElement, String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append("Thread:")
				.append(Thread.currentThread().getName())
				.append(" ")
				.append(getSimpleClassName(stackTraceElement.getClassName()))
				.append(".")
				.append(stackTraceElement.getMethodName())
				.append(" (")
				.append(stackTraceElement.getFileName())
				.append(":")
				.append(stackTraceElement.getLineNumber())
				.append(") ")
				.append(msg);
		return sb.toString();
	}

	/**
	 * Get SimpleClass Name
	 *
	 * @param name name
	 * @return String
	 */
	private static String getSimpleClassName(String name) {
		int lastIndex = name.lastIndexOf(".");
		return name.substring(lastIndex + 1);
	}

}
