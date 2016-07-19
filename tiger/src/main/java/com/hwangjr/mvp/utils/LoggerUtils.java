package com.hwangjr.mvp.utils;

import android.os.Environment;

import com.hwangjr.mvp.MVPApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

public final class LoggerUtils {
    private static final String CRASH_FILE_NAME = "Crash.log";
    private static final String DEBUG_FILE_NAME = "Debug.log";
    private static final String LEAK_FILE_NAME = "Leak.log";

    private static void writeLogToFile(String fileName, String logContent) {
        if (SDCardUtils.hasSDCard()) {
            File root = new File(new File(Environment.getExternalStorageDirectory(), MVPApplication.getInstance().getPackageName()), "log");
            if (!root.exists()) {
                root.mkdirs();
            }

            Date nowtime = new Date();
            String nowtimeFormat = DateUtils.format(DateUtils.SDF_YYYYMMDD, nowtime);
            String logFileName = nowtimeFormat + fileName;

            FileWriter filerWriter = null;
            BufferedWriter bufWriter = null;
            try {
                File file = new File(root, logFileName);
                filerWriter = new FileWriter(file, true);
                bufWriter = new BufferedWriter(filerWriter);
                bufWriter.write("=================================================");
                bufWriter.newLine();
                bufWriter.write(
                        DateUtils.format(DateUtils.SDF_YMDHHMMSS, nowtime));
                bufWriter.newLine();
                bufWriter.write(logContent);
                bufWriter.newLine();


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bufWriter != null) {
                    try {
                        bufWriter.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public static void writeCrashLog(String crashLog) {
        writeLogToFile(CRASH_FILE_NAME, crashLog);
    }

    public static void writeDebugLog(String debugLog) {
        writeLogToFile(DEBUG_FILE_NAME, debugLog);
    }

    public static void writeLeakLog(String leakLog) {
        writeLogToFile(LEAK_FILE_NAME, leakLog);
    }
}
