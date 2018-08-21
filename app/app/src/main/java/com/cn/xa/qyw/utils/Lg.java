package com.cn.xa.qyw.utils;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by cn on 15-6-1.
 */
public class Lg {

    public static final String NAME = "chengnan";

    public static final boolean isDebug = true;

    public static void e(String str) {

        if (isDebug) {
            Log.e(NAME, str);
        }

    }

    public static void e(Exception e){
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        Log.e(NAME,writer.toString());
    }
}
