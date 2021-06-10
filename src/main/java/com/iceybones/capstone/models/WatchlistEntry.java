package com.iceybones.capstone.models;

public class WatchlistEntry implements Comparable<WatchlistEntry>{
  String symbol;
  String rating;
  double value;
  double change;
  double percent;

  public WatchlistEntry(String symbol, String rating) {
    this.symbol = symbol;
    this.rating = rating;

  }


  public String getSymbol() {
    return symbol;
  }

  public String getRating() {
    switch (rating) {
      case "buyStrong":
        return "\u2605\u2605";
      case "buy":
        return "\u2605\u2606";
      case "sell":
          return "\u2606\u2606";
    }
    return rating;
  }

  public String getRatingRaw() {
    return rating;
  }

  public double getValue() {
    return value;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public double getChange() {
    return change;
  }

  public void setChange(double change) {
    this.change = change;
  }

  public double getPercent() {

    return percent;
  }

  public void setPercent(double percent) {
    this.percent = percent;
  }

  @Override
  public int compareTo(WatchlistEntry o) {
    return (int) ((value * 1000) - (o.getValue() * 1000));
  }
}
