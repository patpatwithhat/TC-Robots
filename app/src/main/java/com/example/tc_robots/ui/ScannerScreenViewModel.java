package com.example.tc_robots.ui;

import android.os.CountDownTimer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tc_robots.backend.Article;
import com.example.tc_robots.backend.DataDummy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScannerScreenViewModel extends ViewModel {

    private static final String TAG = "ScannerScreenViewModel";
    private final MutableLiveData<List<Article>> orderedArticles = new MutableLiveData<>();
    private final List<Article> articlesInStock = new ArrayList<>();
    private final MutableLiveData<Boolean> isScannerBlocked = new MutableLiveData<>();

    //TODO add disposables https://www.youtube.com/watch?v=Th0bab1muPU&list=PLgCYzUzKIBE-8wE9Sv3yzYZlo70PBmFPz&index=4

    public ScannerScreenViewModel() {

    }

    public LiveData<List<Article>> getOrderedArticles() {
        return orderedArticles;
    }

    public List<Article> getArticlesInStock() {
        return articlesInStock;
    }

    public void addArticle(String name) {
        Article articleToSearchFor = new Article(name);
        if(!isArticleInStock(articleToSearchFor)) return;
        Article articleInStock = getArticleFromStock(articleToSearchFor);
        if (orderedArticles.getValue() == null) {
            List<Article> list = new ArrayList<>();
            list.add(articleInStock);
            orderedArticles.setValue(list);
        } else if (!isDuplicateArticle(articleInStock)) {
            List<Article> list = orderedArticles.getValue();
            list.add(articleInStock);
            orderedArticles.setValue(list);
        }
    }

    private boolean isArticleInStock(Article article) {
        return articlesInStock.contains(article);
    }

    private Article getArticleFromStock(Article articleToSearchFor) {
        return articlesInStock.get( articlesInStock.indexOf(articleToSearchFor));
    }

    private boolean isDuplicateArticle(Article article) {
        List<Article> list = orderedArticles.getValue();
        return Objects.requireNonNull(list).contains(article);
    }

    public void fetchArticlesInStock() {
        DataDummy data = new DataDummy();
        articlesInStock.addAll(data.getArticles());
    }

    private void blockScanner() {
        isScannerBlocked.setValue(true);
    }

    private void activateScanner() {
        isScannerBlocked.setValue(false);
    }

    public void refreshScanner() {
        blockScanner();
        CountDownTimer timer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                activateScanner();
            }
        };
        timer.start();
    }

    public LiveData<Boolean> isScannerBlocked() {
        return isScannerBlocked;
    }
}
