package com.zjp.tuling;


public class ListData {

    public static final int SEND = 1;
    public static final int RECEVE = 2;
    public static final int CODE_TEXT = 100000;
    public static final int CODE_LINK = 200000;
    public static final int CODE_NEW = 302000;
    public static final int CODE_TRAIN = 305000;
    public static final int CODE_AIRPLANE = 306000;
    public static final int CODE_MENU = 306000;

    public int flag;
    public int code;
    private String content;
    private String time;
    private String url;
    private int who;
    //文本类对象
    public ListData(String content,int flag,int code,String time,int who){
        this.content = content;
        this.flag = flag;
        this.code = code;
        this.time = time;
        this.who = who;
    }

    //查询列车对象



    //查询航班对象

    //网址类对象
    public ListData(String content,String url,int flag,int code,String time,int who){
        this.content = content;
        this.url = url;
        this.flag = flag;
        this.time = time;
        this.who = who;
    }

    //菜谱、视频、小说类对象

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static int getSend() {
        return SEND;
    }

    public static int getReceve() {
        return RECEVE;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getWho() {
        return who;
    }

    public void setWho(int who) {
        this.who = who;
    }
}
