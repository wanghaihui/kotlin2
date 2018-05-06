package com.imagine.ffmpeg;

/**
 * Created by conquer on 2018/4/10.
 *
 */

public class FFmpegNative {

    static {
        System.loadLibrary("ffmpeg-lib");
    }

    public static int runCommand(String[] command){
        int ret;
        synchronized (FFmpegNative.class){
            // 不允许多线程访问
            ret = innerRunCommand(command);
        }
        return ret;
    }

    public static native void setDebug(boolean debug);

    /**
     * 执行指令
     * @param command
     * @return 命令返回结果
     */
    private static native int innerRunCommand(String[] command);
}
