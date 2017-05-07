package sawa.android.common.util;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by mc100 on 2017/4/13.
 */

public class LogUtil {

    private static final String content = "Class:%s\nMethod:%s\nLineNumber:%s\nTime:%s\nMsg:%s";


    public static void e(String msg) {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stacks[3];
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        msg = String.format(content, callerClazzName, caller.getMethodName(), caller.getLineNumber(), System.currentTimeMillis(), msg);
        Log.e("[Reader]", msg);
    }

    public static void e(Throwable tr) {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stacks[3];
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        String msg = String.format(content, callerClazzName, caller.getMethodName(), caller.getLineNumber(), System.currentTimeMillis(), sw.toString());
        Log.e("[Reader]", msg);
    }
}
