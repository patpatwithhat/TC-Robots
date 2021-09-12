package com.example.tc_robots.ui;

import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tc_robots.backend.Article;

import java.util.List;
import java.util.Objects;

public class ScannerScreenViewModel extends ViewModel {

    private static final String TAG = "ScannerScreenViewModel";
    private final MutableLiveData<List<Article>> articles = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isScannerBlocked = new MutableLiveData<>();

    //TODO add disposables https://www.youtube.com/watch?v=Th0bab1muPU&list=PLgCYzUzKIBE-8wE9Sv3yzYZlo70PBmFPz&index=4

    public ScannerScreenViewModel() {

    }

    public LiveData<List<Article>> getArticles() {
        return articles;
    }

    public void addArticle(String name) {
        articles.getValue().add(new Article(name));
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
