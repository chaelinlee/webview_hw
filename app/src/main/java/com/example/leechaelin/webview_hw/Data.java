package com.example.leechaelin.webview_hw;

/**
 * Created by leechaelin on 2017. 5. 10..
 */

public class Data {
    private String name;
    private String url;

    public Data(String name, String url){
        this.name=name;
        this.url=url;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return name+"   "+url;
    }
}
