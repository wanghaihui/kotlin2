package com.imagine.peace.utils;

import android.text.TextUtils;
import android.util.Log;

import com.imagine.peace.bean.Lrc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by conquer on 2018/4/8.
 *
 */

public class LrcParser {
    private static final LrcParser instance = new LrcParser();

    public static final LrcParser getInstance(){
        return instance;
    }
    private LrcParser() {
    }

    public synchronized List<Lrc> getLrcList(File file) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Lrc> lrcList = new ArrayList<>();
        String lrcLine;

        if (br != null) {
            try {
                while ((lrcLine = br.readLine()) != null) {
                    List<Lrc> rows = createLrc(lrcLine);
                    if (rows != null && rows.size() > 0) {
                        lrcList.addAll(rows);
                    }
                }
                Collections.sort(lrcList);

                for (int i = 0; i < lrcList.size() - 1; i++) {
                    lrcList.get(i).setTotalTime(lrcList.get(i + 1).getTime() - lrcList.get(i).getTime());
                }
                if (lrcList.size() > 1) {
                    lrcList.get(lrcList.size() - 1).setTotalTime(5000);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return lrcList;
    }

    private  List<Lrc> createLrc(String lrcLine){
        if(!lrcLine.startsWith("[")) {
            return null;
        }
        // 最后一个"]"
        int lastIndexOfRightBracket = lrcLine.lastIndexOf("]");
        // 歌词内容
        String content = lrcLine.substring(lastIndexOfRightBracket + 1, lrcLine.length());
        // 截取出歌词时间，并将"[" 和"]" 替换为"-"   [offset:0]
        Log.d("LrcParser", "lrcLine= " + lrcLine);
        // -03:33.02--00:36.37-
        String times = lrcLine.substring(0, lastIndexOfRightBracket + 1).replace("[", "-").replace("]", "-");
        String[] timesArray = times.split("-");
        List<Lrc> lrcRows = new ArrayList<>();
        for (String tem : timesArray) {
            if(TextUtils.isEmpty(tem.trim())){
                continue;
            }

            try{
                Lrc lrcRow = new Lrc(tem, formatTime(tem), content);
                lrcRows.add(lrcRow);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return lrcRows;
    }

    private int formatTime(String timeStr) {
        timeStr = timeStr.replace('.', ':');
        String[] times = timeStr.split(":");

        return Integer.parseInt(times[0])*60*1000
                + Integer.parseInt(times[1])*1000
                + Integer.parseInt(times[2]);
    }
}
