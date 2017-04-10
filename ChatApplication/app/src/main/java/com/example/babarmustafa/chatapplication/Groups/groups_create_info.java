package com.example.babarmustafa.chatapplication.Groups;

/**
 * Created by BabarMustafa on 1/31/2017.
 */

public class groups_create_info {
    String admin_name;
    String group_name;
    String g_i_url;

    public groups_create_info() {
    }

    public groups_create_info(String admin_name, String group_name, String g_i_url) {
        this.admin_name = admin_name;
        this.group_name = group_name;
        this.g_i_url = g_i_url;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getG_i_url() {
        return g_i_url;
    }

    public void setG_i_url(String g_i_url) {
        this.g_i_url = g_i_url;
    }
}
