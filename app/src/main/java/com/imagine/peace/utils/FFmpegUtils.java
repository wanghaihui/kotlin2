package com.imagine.peace.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.imagine.ffmpeg.FFmpegNative;
import com.imagine.peace.R;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

/**
 * Created by conquer on 2018/4/13.
 *
 */

public class FFmpegUtils {
    private static final String TAG = FFmpegUtils.class.getSimpleName();

    public static final String FFMPEG_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ffmpeg";

    public static int generateGifVideo(String path) {
        String[] commands = new String[6];
        commands[0] = "ffmpeg";
        commands[1] = "-f";
        commands[2] = "gif";
        commands[3] = "-i";
        commands[4] = path;
        commands[5] = FFMPEG_PATH + "/outputGif.mp4";
        return FFmpegNative.runCommand(commands);
    }

    public static int generateTenSecondVideo() {
        try {
            File concatFile = new File(FFMPEG_PATH, "concat.txt");
            FileOutputStream fos = new FileOutputStream(concatFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter writer = new BufferedWriter(osw);
            for (int i = 0; i < 9; i++){
                writer.write("file " + "\'" + FFMPEG_PATH + "/outputGif.mp4" + "\'" + "\r\n");
            }
            writer.close();

            String[] commands = new String[10];
            commands[0] = "ffmpeg";
            commands[1] = "-f";
            commands[2] = "concat";
            commands[3] = "-i";
            commands[4] = concatFile.getAbsolutePath();
            commands[5] = "-c";
            commands[6] = "copy";
            commands[7] = "-t";
            commands[8] = "11";
            commands[9] = FFMPEG_PATH + "/outputBase.mp4";
            return FFmpegNative.runCommand(commands);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static int generateTenSecondAudio(String path, String startTime) {
        String[] commands = new String[10];
        commands[0] = "ffmpeg";
        commands[1] = "-i";
        commands[2] = path;
        commands[3] = "-ss";
        commands[4] = startTime;
        commands[5] = "-t";
        commands[6] = "10";
        commands[7] = "-acodec";
        commands[8] = "copy";
        commands[9] = FFMPEG_PATH + "/output.mp3";
        return FFmpegNative.runCommand(commands);
    }

    public static String generateVideoAudio() {
        String outputPath = FFMPEG_PATH + "/" + "LQ" + System.currentTimeMillis() + ".mp4";
        String[] commands = new String[10];
        commands[0] = "ffmpeg";
        commands[1] = "-i";
        commands[2] = FFMPEG_PATH + "/output.mp3";
        commands[3] = "-i";
        commands[4] = FFMPEG_PATH + "/outputBase.mp4";
        commands[5] = "-acodec";
        commands[6] = "copy";
        commands[7] = "-vcodec";
        commands[8] = "copy";
        commands[9] = outputPath;
        if (FFmpegNative.runCommand(commands) == 0) {
            return outputPath;
        } else {
            return null;
        }
    }

    public static void moveFontToSDCard(Context context, String name) {
        File file = new File(FFMPEG_PATH, name);
        if (file.exists()) {
            return;
        }

        try {
            InputStream inStream = context.getResources().openRawResource(R.raw.lyrics_cn_bold);

            FileOutputStream fileOutputStream = new FileOutputStream(file);//存入SDCard
            byte[] buffer = new byte[10];
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            byte[] bs = outStream.toByteArray();
            fileOutputStream.write(bs);
            outStream.close();
            inStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void moveLogoToSDCard(Context context, String name) {
        File file = new File(FFMPEG_PATH, name);
        if (file.exists()) {
            return;
        }

        try {
            InputStream inStream = context.getResources().openRawResource(R.raw.ic_logo);

            FileOutputStream fileOutputStream = new FileOutputStream(file);//存入SDCard
            byte[] buffer = new byte[10];
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            byte[] bs = outStream.toByteArray();
            fileOutputStream.write(bs);
            outStream.close();
            inStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setFontConfigEnv() {
        String[] commands = {"sh","-c","export FONTCONFIG_FILE=/sdcard/fonts/fonts.conf;echo $FONTCONFIG_FILE"};
        runSystemCommand(commands, null);
    }

    private static void runSystemCommand(String[] command, File file) {
        if (command != null && command.length > 0) {
            try {
                Process ps;
                if (file != null)
                    ps = Runtime.getRuntime().exec(command, null, file);
                else
                    ps = Runtime.getRuntime().exec(command);
                String message = loadStream(ps.getInputStream());
                String errorMeg = loadStream(ps.getErrorStream());
                Log.d(TAG, message);
                Log.d(TAG, "---------------------------");
                Log.d(TAG, errorMeg);
                try {
                    ps.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String loadStream(InputStream in) throws IOException {
        int ptr;
        in = new BufferedInputStream(in);
        StringBuffer buffer = new StringBuffer();
        while ((ptr = in.read()) != -1) {
            buffer.append((char) ptr);
        }
        return new String(buffer.toString().getBytes("ISO-8859-1"), "GBK");
    }
}
