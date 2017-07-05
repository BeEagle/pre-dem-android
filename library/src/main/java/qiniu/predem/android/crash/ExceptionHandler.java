package qiniu.predem.android.crash;

import android.annotation.SuppressLint;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.UUID;

import qiniu.predem.android.bean.AppBean;
import qiniu.predem.android.bean.CrashBean;
import qiniu.predem.android.config.FileConfig;
import qiniu.predem.android.util.FileUtil;

/**
 * Created by Misty on 17/6/15.
 */

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "ExceptionHandler";
    @SuppressLint("StaticFieldLeak")
    private static FileUtil fileLogManager;
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;
    private boolean mIgnoreDefaultHandler = false;

    public ExceptionHandler(Thread.UncaughtExceptionHandler defaultExceptionHandler, boolean ignoreDefaultHandler) {
        mDefaultExceptionHandler = defaultExceptionHandler;
        mIgnoreDefaultHandler = ignoreDefaultHandler;
        fileLogManager = FileUtil.getInstance();
    }

    public static void saveException(Throwable exception, Thread thread) {
        final Date now = new Date();
        final Date startDate = new Date(CrashManager.getInitializeTimestamp());
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        BufferedWriter writer = null;
        exception.printStackTrace(printWriter);

        String filename = UUID.randomUUID().toString();

        CrashBean crashBean = new CrashBean(filename, exception);
        crashBean.setAppPackage(AppBean.APP_PACKAGE);
        crashBean.setAppVersionCode(AppBean.APP_VERSION);
        crashBean.setAppVersionName(AppBean.APP_VERSION_NAME);
        crashBean.setAppStartDate(startDate);
        crashBean.setAppCrashDate(now);

        //device data
        crashBean.setOsVersion(AppBean.ANDROID_VERSION);
        crashBean.setOsBuild(AppBean.ANDROID_BUILD);
        crashBean.setDeviceManufacturer(AppBean.PHONE_MANUFACTURER);
        crashBean.setDeviceModel(AppBean.PHONE_MODEL);

        //thread data
        crashBean.setThreadName(thread.getName() + "-" + thread.getId());

        if (AppBean.CRASH_IDENTIFIER != null) {
            crashBean.setReporterKey(AppBean.CRASH_IDENTIFIER);
        }


        // TODO: 17/6/15 将crash信息写入文件
//        crashBean.writeCrashReport();
        fileLogManager.writeCrashReport(crashBean);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (FileConfig.FILES_PATH == null) {
            // If the files path is null, the exception can't be stored
            // Always call the default handler instead
            mDefaultExceptionHandler.uncaughtException(thread, ex);
        } else {
            saveException(ex, thread);

            if (!mIgnoreDefaultHandler) {
                mDefaultExceptionHandler.uncaughtException(thread, ex);
            } else {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(10);
            }
        }
    }
}