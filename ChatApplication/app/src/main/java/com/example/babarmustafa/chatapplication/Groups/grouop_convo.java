package com.example.babarmustafa.chatapplication.Groups;

/**
 * Created by BabarMustafa on 2/8/2017.
 */

public class grouop_convo {
    String msg ;
    String time;
    String sendr_id;

    public grouop_convo() {
    }

    public grouop_convo(String msg, String time, String sendr_id) {
        this.msg = msg;
        this.time = time;
        this.sendr_id = sendr_id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSendr_id() {
        return sendr_id;
    }

    public void setSendr_id(String sendr_id) {
        this.sendr_id = sendr_id;
    }
}
