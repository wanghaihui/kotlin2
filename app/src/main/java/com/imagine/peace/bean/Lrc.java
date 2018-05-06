package com.imagine.peace.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by conquer on 2018/4/8.
 *
 */

public class Lrc implements Comparable<Lrc>, Serializable {
    // 开始时间
    private String timeStr;
    // 开始时间--毫秒数
    private int time;
    // 歌词内容
    private String content;
    // 该行歌词显示的总时间
    private int totalTime;

    public Lrc(String timeStr, int time, String content) {
        super();
        this.timeStr = timeStr;
        this.time = time;
        this.content = content;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public String getTimeStr() {
        return this.timeStr;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return this.time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getTotalTime() {
        return this.totalTime;
    }

    @Override
    public int compareTo(@NonNull Lrc anotherLrc) {
        return this.time - anotherLrc.time;
    }
}
