package com.example.model.shas_masechtot;

import com.google.gson.annotations.SerializedName;

public class Masechet {
    @SerializedName("pages")
    private int pages;

    @SerializedName("masechet")
    private String name;


    public Masechet(int pages, String name) {
        this.pages = pages;
        this.name = name;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
