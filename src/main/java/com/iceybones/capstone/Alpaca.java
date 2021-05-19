package com.iceybones.capstone;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.jacobpeterson.alpaca.AlpacaAPI;
import net.jacobpeterson.alpaca.enums.asset.AssetStatus;
import net.jacobpeterson.alpaca.rest.exception.AlpacaAPIRequestException;
import net.jacobpeterson.domain.alpaca.asset.Asset;
import net.jacobpeterson.domain.alpaca.marketdata.historical.quote.QuotesResponse;


public class Alpaca {
    public static AlpacaAPI alpacaAPI = new AlpacaAPI();
    public static List<Asset> usEquities;

    static {
        try {
            usEquities = alpacaAPI.getAssets(AssetStatus.ACTIVE, "us_equity");
            usEquities.sort(Comparator.comparing(Asset::getSymbol));
        } catch (AlpacaAPIRequestException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }

    public List<Asset> getSymbols(String in) throws AlpacaAPIRequestException {
        List<Asset> out = new ArrayList<>(usEquities);
        for (var asset : usEquities) {
            if (!asset.getSymbol().toLowerCase().contains(in.toLowerCase())) {
                out.remove(asset);
            }
        }
        return out;
    }
    public void printQuote(String symbol) {
        try {
            // Get first 100 quotes of AAPL of first minute on 3/1/2021 and print them out
            QuotesResponse appleQuotesResponse = alpacaAPI.getQuotes(
                symbol,
                ZonedDateTime.of(2021, 3, 1, 9, 30, 0, 0, ZoneId.of("America/New_York")),
                ZonedDateTime.of(2021, 3, 1, 9, 31, 0, 0, ZoneId.of("America/New_York")),
                100,
                null);
            appleQuotesResponse.getQuotes().forEach(System.out::println);
        } catch (AlpacaAPIRequestException e) {
            e.printStackTrace();
        }
    }
    public BigDecimal getEquity() {
        BigDecimal out = null;
        try {
            out = BigDecimal.valueOf(Double.parseDouble(alpacaAPI.getAccount().getEquity()));
            out = out.setScale(2, RoundingMode.FLOOR);
        } catch (AlpacaAPIRequestException e) {
            e.printStackTrace();
        }
        return out;
    }
    public BigDecimal getBuyingPower() {
        BigDecimal out = null;
        try {
            out = BigDecimal.valueOf(Double.parseDouble(alpacaAPI.getAccount().getBuyingPower()));
            out = out.setScale(2, RoundingMode.FLOOR);
        } catch (AlpacaAPIRequestException e) {
            e.printStackTrace();
        }
        return out;
    }
}
