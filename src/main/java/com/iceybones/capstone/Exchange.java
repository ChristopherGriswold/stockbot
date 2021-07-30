package com.iceybones.capstone;

public enum Exchange {
    NYSE("stocks/nyse/"), NASDAQ("stocks/nasdaq/");
    String path;
    Exchange(String s) {
        this.path = s;
    }
}
