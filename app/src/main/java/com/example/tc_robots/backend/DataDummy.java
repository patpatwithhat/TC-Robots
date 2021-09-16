package com.example.tc_robots.backend;

import com.example.tc_robots.R;

import java.util.ArrayList;

public class DataDummy {
    private ArrayList<Article> articles = new ArrayList<>();

    public DataDummy() {
        int cubeId = R.drawable.cube;
        int cylinderId = R.drawable.cylinder;
        articles.add(new Article("cube", cubeId, 6));
        articles.add(new Article("cylinder", cylinderId, 8));
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }
}
