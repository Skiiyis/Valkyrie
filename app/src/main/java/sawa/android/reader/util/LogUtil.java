package sawa.android.reader.util;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by mc100 on 2017/4/13.
 */

public class LogUtil {

    public static void w(String msg) {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stacks[5];
        String format = "[%s,%s,%d]";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        String position = String.format(format, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        Log.w(position, msg);
    }

    public static void w(Throwable tr) {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stacks[5];
        String format = "[%s,%s,%d]";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        String position = String.format(format, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        Log.w(position, sw.toString());
    }

    public static void e(String msg) {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stacks[5];
        String format = "[%s,%s,%d]";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        String position = String.format(format, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        Log.e(position, msg);
    }

    public static void e(Throwable tr) {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stacks[5];
        String format = "[%s,%s,%d]";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        String position = String.format(format, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        Log.e(position, sw.toString());
    }
}
