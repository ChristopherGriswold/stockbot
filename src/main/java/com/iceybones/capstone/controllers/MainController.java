package com.iceybones.capstone.controllers;

import com.iceybones.capstone.CleanNews;
import com.iceybones.capstone.ScrapeNews;
import com.iceybones.capstone.dl4j.TestNews;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;
import net.jacobpeterson.abstracts.enums.SortDirection;
import net.jacobpeterson.abstracts.websocket.exception.WebsocketException;
import net.jacobpeterson.alpaca.AlpacaAPI;
import net.jacobpeterson.alpaca.enums.asset.AssetStatus;
import net.jacobpeterson.alpaca.enums.marketdata.BarsTimeFrame;
import net.jacobpeterson.alpaca.enums.order.OrderSide;
import net.jacobpeterson.alpaca.enums.order.OrderStatus;
import net.jacobpeterson.alpaca.enums.order.OrderTimeInForce;
import net.jacobpeterson.alpaca.rest.exception.AlpacaAPIRequestException;
import net.jacobpeterson.alpaca.websocket.marketdata.listener.MarketDataListener;
import net.jacobpeterson.alpaca.websocket.marketdata.listener.MarketDataListenerAdapter;
import net.jacobpeterson.alpaca.websocket.marketdata.message.MarketDataMessageType;
import net.jacobpeterson.domain.alpaca.account.Account;
import net.jacobpeterson.domain.alpaca.asset.Asset;
import net.jacobpeterson.domain.alpaca.marketdata.historical.bar.BarsResponse;
import net.jacobpeterson.domain.alpaca.marketdata.historical.quote.LatestQuoteResponse;
import net.jacobpeterson.domain.alpaca.marketdata.historical.trade.Trade;
import net.jacobpeterson.domain.alpaca.marketdata.realtime.MarketDataMessage;
import net.jacobpeterson.domain.alpaca.marketdata.realtime.bar.BarMessage;
import net.jacobpeterson.domain.alpaca.marketdata.realtime.quote.QuoteMessage;
import net.jacobpeterson.domain.alpaca.marketdata.realtime.trade.TradeMessage;
import net.jacobpeterson.domain.alpaca.order.Order;
import net.jacobpeterson.domain.alpaca.position.Position;
import net.jacobpeterson.domain.alpaca.watchlist.Watchlist;

public class MainController implements Initializable {

  ExecutorService service = Executors.newSingleThreadExecutor();
  ObservableList<String> scanResults;
  ObservableList<Position> positions = FXCollections.observableArrayList();
  ObservableList<Order> orders = FXCollections.observableArrayList();
  ObservableList<Asset> watchlist = FXCollections.observableArrayList();
  String watchlistId;
  AlpacaAPI alpacaAPI = new AlpacaAPI();
  List<Asset> usEquities;
  ScrapeNews scraper = new ScrapeNews();
  CleanNews cleanNews = new CleanNews();
  TestNews testNews = new TestNews();

  {
    try {
      usEquities = alpacaAPI.getAssets(AssetStatus.ACTIVE, "us_equity");
      usEquities.sort(Comparator.comparing(Asset::getSymbol));
    } catch (AlpacaAPIRequestException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    tickerSymbolCbox.setOnHiding(e -> {
      onActionTicker();
    });
    runClock();
    updateCash();
    setupPortfolioTable();
    setupOrdersTable();
    setupWatchlistTable();
//    setupLineChart("AAPL");
    tickerSymbolCbox.setCellFactory(new Callback<ListView<Asset>, ListCell<Asset>>() {
      @Override
      public ListCell<Asset> call(ListView<Asset> param) {
        ListCell cell = new ListCell<Asset>() {
          @Override
          public void updateItem(Asset item, boolean empty) {
            super.updateItem(item, empty);

            getListView().setPrefWidth(600);
            if (!empty) {
              setText(item.getSymbol() + " - " + item.getName() + " - " + item.getExchange());
            } else {
              setText(null);
            }
          }
        };
        return cell;
      }
    });
    tickerSymbolCbox.setConverter(new StringConverter<Asset>() {

      @Override
      public String toString(Asset asset) {
        if (asset == null) {
          return null;
        }
        return asset.getSymbol();
      }

      @Override
      public Asset fromString(String string) {
        Asset out = new Asset();
        for (var asset : usEquities) {
          if (asset.getSymbol().equals(string)) {
            out = asset;
          }
        }
        return out;
      }
    });
    DecimalFormat format = new DecimalFormat("#.0");

    quantityField.setTextFormatter(new TextFormatter<>(c ->
    {
      if (c.getControlNewText().isEmpty()) {
        return c;
      }

      ParsePosition parsePosition = new ParsePosition(0);
      Object object = format.parse(c.getControlNewText(), parsePosition);

      if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
        return null;
      } else {
        return c;
      }
    }));
  }

  private void runClock() {
    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> Platform
            .runLater(() -> clockLbl.setText(LocalTime.now().truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString()))
        , 0, 1, TimeUnit.SECONDS);
  }

  private void updateCash() {
    var decimalFormat = new DecimalFormat("$###,###,##0.00");
    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
      try {
        Account account = alpacaAPI.getAccount();
        Platform.runLater(() -> equityLbl
            .setText(decimalFormat.format(Double.parseDouble(account.getEquity()))));
        Platform.runLater(() -> buyingPowerLbl.setText(
            decimalFormat.format(Double.parseDouble(account.getBuyingPower()))));
        double dailyProfit = Double.parseDouble(account.getEquity()) - Double
            .parseDouble(account.getLastEquity());
        if (dailyProfit < 0) {
          Platform.runLater(() -> dailyProfitLbl.setTextFill(Color.RED));
        } else {
          Platform.runLater(() -> dailyProfitLbl.setTextFill(Color.GREEN));
        }
        Platform.runLater(() -> dailyProfitLbl.setText(decimalFormat.format(dailyProfit)));
      } catch (AlpacaAPIRequestException e) {
        e.printStackTrace();
      }
    }, 0, 1, TimeUnit.SECONDS);
  }

  private void setupPortfolioTable() {
    portfolioSymbolCol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
    portfolioPositionCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
    portfolioValueCol.setCellValueFactory(cellData -> Bindings.createStringBinding(() ->
        "$" + cellData.getValue().getMarketValue()));
    portfolioPandLCol.setCellValueFactory(new PropertyValueFactory<>("unrealizedPl"));
    portfolioPercentCol.setCellValueFactory(new PropertyValueFactory<>("unrealizedPlpc"));
    portfolioPandLCol.setCellFactory(
        new Callback<TableColumn<Position, String>, TableCell<Position, String>>() {
          @Override
          public TableCell<Position, String> call(
              TableColumn<Position, String> positionStringTableColumn) {
            return new TableCell<Position, String>() {
              @Override
              protected void updateItem(String item, boolean empty) {
                if (empty || item == null) {
                  setText(null);
                  return;
                }
                if (item.contains("-")) {
                  setTextFill(Color.RED);
                  item = "-$" + item.substring(1);
                } else {
                  setTextFill(Color.GREEN);
                  item = "$" + item;
                }
                setText(item);
              }
            };
          }

          ;
        });
    portfolioPercentCol.setCellFactory(
        new Callback<TableColumn<Position, String>, TableCell<Position, String>>() {
          @Override
          public TableCell<Position, String> call(
              TableColumn<Position, String> positionStringTableColumn) {
            return new TableCell<Position, String>() {
              @Override
              protected void updateItem(String item, boolean empty) {
                if (item == null) {
                  setText(null);
                  return;
                }
                if (item.contains("-")) {
                  setTextFill(Color.RED);
                  item = (item.length() > 6) ? item.substring(0, 5) : item;
                } else {
                  setTextFill(Color.GREEN);
                  item = (item.length() > 5) ? item.substring(0, 5) : item;
                }
                setText(item);
              }
            };
          }

          ;
        });
    updatePortfolioTable();
    portfolioTable.setItems(positions);
  }

  private void updatePortfolioTable() {
    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
      try {
        positions.setAll(alpacaAPI.getOpenPositions());
      } catch (AlpacaAPIRequestException e) {
        e.printStackTrace();
      }
    }, 0, 1, TimeUnit.SECONDS);
  }

  private void setupOrdersTable() {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");
    ordersSymbolCol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
    ordersOrderCol.setCellValueFactory(cellData -> Bindings.createStringBinding(
        () -> cellData.getValue().getOrderType().substring(0, 1).toUpperCase() + cellData.getValue()
            .getOrderType().substring(1) + " " + cellData.getValue().getSide().toUpperCase() + "\n"
            + cellData.getValue().getCreatedAt().withZoneSameInstant(ZoneId.systemDefault())
            .format(dtf)));
    ordersSharesCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
    ordersPricePerShareCol.setCellValueFactory(cellData -> Bindings.createStringBinding(() ->
    {
      if (cellData.getValue().getFilledAvgPrice() != null) {
        return "$" + cellData.getValue().getFilledAvgPrice();
      } else {
        return "";
      }
    }));
    ordersAmountCol.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> {
      if (cellData.getValue() == null || !cellData.getValue().getStatus().equals("filled")) {
        return null;
      } else {
        return "$" + String.valueOf(Double.parseDouble(cellData.getValue().getQty()) * Double
            .parseDouble(cellData.getValue().getFilledAvgPrice()));
      }
    }));

    ordersStatusCol.setCellValueFactory(
        cellData -> Bindings.createStringBinding(() -> cellData.getValue().getStatus()));
    ordersStatusCol.setCellFactory(
        new Callback<TableColumn<Order, String>, TableCell<Order, String>>() {
          @Override
          public TableCell<Order, String> call(TableColumn<Order, String> orderStringTableColumn) {
            return new TableCell<Order, String>() {
              @Override
              protected void updateItem(String item, boolean empty) {
                if (item == null || empty) {
                  setText(null);
                  return;
                }
                if (item.equals("filled")) {
                  setTextFill(Color.GREEN);
                } else {
                  setTextFill(Color.BLACK);
                }
                setText(item.substring(0, 1).toUpperCase() + item.substring(1));
              }
            };
          }

          ;
        });
    updateOrdersTable();
    ordersTable.setItems(orders);
  }

  private void updateOrdersTable() {
    try {
      orders.setAll(alpacaAPI
          .getOrders(OrderStatus.ALL, 50, ZonedDateTime.now().minusDays(30), ZonedDateTime.now(),
              SortDirection.DESCENDING, false, null
          ));
    } catch (AlpacaAPIRequestException e) {
      e.printStackTrace();
    }
  }

  private void setupWatchlistTable() {
    watchlistSymbolCol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
    watchlistValueCol.setCellValueFactory(cellData -> Bindings.createStringBinding(() ->
        {
          return "$" + cellData.getValue().getSymbol();
        }
    ));
//    watchlistValueCol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
//    watchlistChangeCol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
//    watchlistPercentCol.setCellValueFactory(new PropertyValueFactory<>("symbol"));

    watchlistTable.setItems(watchlist);
    updateWatchlistTable();
  }

  private void watchStock(String symbol) {

  }

  private void updateWatchlistTable() {
    try {
      watchlistId = alpacaAPI.getWatchlists().get(0).getId();
    } catch (AlpacaAPIRequestException e) {
      e.printStackTrace();
    }
    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
      try {
        Watchlist watch = alpacaAPI.getWatchlist(watchlistId);
        watchlist.setAll(watch.getAssets());
      } catch (AlpacaAPIRequestException e) {
        e.printStackTrace();
      }
    }, 0, 1, TimeUnit.SECONDS);
  }

  private void setupLineChart(String symbol) {
    XYChart.Series<Number, Number> series = new Series<>();
    int numDays = 1;
    DayOfWeek today = ZonedDateTime.now().getDayOfWeek();
    switch (today) {
      case MONDAY:
        numDays++;
      case SUNDAY:
        numDays++;
      case SATURDAY:
        numDays++;
    }
    try {
      BarsResponse barsResponse = alpacaAPI.getBars(
          symbol,
          ZonedDateTime.now().minusDays(numDays),
          ZonedDateTime.now().plusHours(3).plusMinutes(45),
          null,
          null,
          BarsTimeFrame.MINUTE);
      barsResponse.setBars(new ArrayList<>(barsResponse.getBars().stream()
          .sorted((a, b) -> (int) (b.getT().toEpochSecond() - a.getT().toEpochSecond())).limit(500)
          .collect(Collectors.toList())));
      for (var bar : barsResponse.getBars()) {
        series.getData().add(new XYChart.Data<>(bar.getT().toEpochSecond(), bar.getC()));
      }
      updateSeries(series, symbol);
//      Trade latest = alpacaAPI.getLatestTrade(symbol).getTrade();
//      series.getData().add(new XYChart.Data<>(latest.getT().toEpochSecond(), latest.getP()));
      Platform.runLater(() -> quotePrice.setText("$" + barsResponse.getBars().get(0).getC()));
    } catch (AlpacaAPIRequestException e) {
      e.printStackTrace();
    }

    try {
      // Listen to TSLA quotes, trades, and minute bars and print their messages out
      MarketDataListener listener = new MarketDataListenerAdapter(
          symbol,
          MarketDataMessageType.TRADE,
          MarketDataMessageType.QUOTE,
          MarketDataMessageType.BAR) {
        @Override
        public void onStreamUpdate(MarketDataMessageType streamMessageType,
            MarketDataMessage streamMessage) {
          System.out.println("UPDATE");
          switch (streamMessageType) {
            case TRADE:
              TradeMessage tradeMessage = (TradeMessage) streamMessage;
              System.out.printf("Trade: Price=%.2f Size=%d Time=%s\n",
                  tradeMessage.getPrice(), tradeMessage.getSize(), tradeMessage.getTimestamp());
              break;
            case QUOTE:
              QuoteMessage quoteMessage = (QuoteMessage) streamMessage;
              quotePrice.setText("$" + quoteMessage.getBidPrice().toString());
              break;
            case BAR:
              BarMessage barMessage = (BarMessage) streamMessage;
              series.getData().add(new XYChart.Data<>(barMessage.getTimestamp().toEpochSecond(),
                  barMessage.getClose()));
              quotePrice.setText("$" + barMessage.getClose());
              break;
          }
        }
      };

      // Add the 'MarketDataListener'
      // Note that when the first 'MarketDataListener' is added, the Websocket
      // connection is created.
//      alpacaAPI.removeMarketDataStreamListener(listener);
      alpacaAPI.addMarketDataStreamListener(listener);
    } catch (WebsocketException exception) {
      exception.printStackTrace();
    }
    areaChart.getData().clear();
    areaChart.getData().add(series);
    ((NumberAxis) areaChart.getXAxis()).setAutoRanging(true);
    ((NumberAxis) areaChart.getYAxis()).setAutoRanging(true);
    ((NumberAxis) areaChart.getXAxis()).setForceZeroInRange(false);
    ((NumberAxis) areaChart.getYAxis()).setForceZeroInRange(false);
    ((NumberAxis) areaChart.getYAxis()).setTickLabelFormatter(new StringConverter<Number>() {
      @Override
      public String toString(Number number) {
        return "$" + number;
      }

      @Override
      public Number fromString(String s) {
        return null;
      }
    });
    ((NumberAxis) areaChart.getXAxis()).setTickLabelFormatter(new StringConverter<Number>() {
      @Override
      public String toString(Number number) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mma\nMM/dd/yy");

        var i = Instant.ofEpochSecond(number.longValue());
        var z = ZonedDateTime.ofInstant(i, ZoneId.of("America/New_York"));
        return z.format(dtf);
      }

      @Override
      public Number fromString(String s) {
        return null;
      }
    });
    areaChart.autosize();
  }

  ScheduledExecutorService seriesUpdater = Executors.newSingleThreadScheduledExecutor();

  private void updateSeries(Series series, String symbol) {
    seriesUpdater.scheduleAtFixedRate(() -> {
      Trade latest = null;
      try {
        latest = alpacaAPI.getLatestTrade(symbol).getTrade();
      } catch (AlpacaAPIRequestException e) {
        e.printStackTrace();
      }
      Trade finalLatest = latest;
      Platform.runLater(() -> series.getData()
          .add(new XYChart.Data<>(finalLatest.getT().toEpochSecond(), finalLatest
              .getP())));

    }, 0, 3, TimeUnit.SECONDS);
  }

  private void backTest() {

    try (var out = Files.newBufferedWriter(Path.of("bought_checked.csv"));
        var in = Files.newBufferedReader(Path.of("bought.csv"))) {
      String line = null;
      while ((line = in.readLine()) != null) {
        var lineSplit = line.split(",");
        var dailyBar = alpacaAPI.getSnapshot(lineSplit[1]).getDailyBar();
        if (dailyBar != null) {
          out.write(dailyBar.getO() + "," + dailyBar.getH() + ","
              + dailyBar.getH().doubleValue() / dailyBar.getO()
              .doubleValue() + "," + line);
        } else {
          out.write(line);
        }
        out.newLine();
      }
    } catch (IOException | AlpacaAPIRequestException e) {
      e.printStackTrace();
    }
    ;
  }

  @FXML
  void onActionScanBtn(ActionEvent event) throws IOException, InterruptedException {
    if (scanBtn.isSelected()) {
      scanResults = scraper.scan();
      newsParent.setItems(scanResults);
    } else {
      scraper.cancelScan();
    }
  }

  @FXML
  void onActionAutoAddCheck(ActionEvent event) {

  }

  @FXML
  void onActionAutoSellCheck(ActionEvent event) {

  }

  @FXML
  void onActionBuyBtn(ActionEvent event) {
    checkSubmit();
  }

  @FXML
  void onActionMaxBidTxt(ActionEvent event) {

  }

  @FXML
  void onActionOnActionBuyCheck(ActionEvent event) {

  }


  @FXML
  void onActionSellBtn(ActionEvent event) {
    checkSubmit();
  }

  @FXML
  void onActionSellLagTxt(ActionEvent event) {

  }

  @FXML
  void onActionSubmitBtn(ActionEvent event) {
    OrderSide orderSide = OrderSide.BUY;
    Order order = null;
    if (sellBtn.isSelected()) {
      orderSide = OrderSide.SELL;
    }
    try {
      if (quantityField.getText().contains(".")) {
        order = alpacaAPI.requestNewFractionalMarketOrder(tickerSymbolCbox.getValue().getSymbol(),
            Double.parseDouble(quantityField.getText()), orderSide);
      } else {
        order = alpacaAPI.requestNewMarketOrder(tickerSymbolCbox.getValue().getSymbol(),
            Integer.parseInt(quantityField.getText()), orderSide,
            OrderTimeInForce.IOC);
      }
      estimatedPriceLbl.setText("Estimated Price: $0.00");
    } catch (AlpacaAPIRequestException e) {
      estimatedPriceLbl.setText("Order Failed");
    } finally {
      buyBtn.setSelected(false);
      sellBtn.setSelected(false);
      quantityField.clear();
      submitBtn.setDisable(true);
      updateOrdersTable();
    }
  }

  @FXML
  void onActionTestBtn(ActionEvent event) {
    testBtn.setDisable(true);
    Future done = service.submit(() -> {
      try {
        TestNews.main();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    service.submit(() -> {
      while (!done.isDone()) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      Platform.runLater(() -> testBtn.setDisable(false));
    });
  }

  @FXML
  void onActionTickerSymbolCbox(ActionEvent event) {
//    buyBtn.setSelected(false);
//    sellBtn.setSelected(false);
//    submitBtn.setDisable(true);
//    quantityField.clear();
//    estimatedPriceLbl.setText("Estimated Price: $0.00");
//    if (tickerSymbolCbox.getSelectionModel().getSelectedItem() != null) {
//      String selected = tickerSymbolCbox.getSelectionModel().getSelectedItem().getSymbol();
//      if (selected != null) {
//        tickerSymbolCbox.getEditor().setText(selected);
//        setupLineChart(selected);
//      }
//    }
  }

  @FXML
  void onActionTicker() {
    buyBtn.setSelected(false);
    sellBtn.setSelected(false);
    submitBtn.setDisable(true);
    quantityField.clear();
    estimatedPriceLbl.setText("Estimated Price: $0.00");
    if (tickerSymbolCbox.getSelectionModel().getSelectedItem() != null) {
      String selected = tickerSymbolCbox.getSelectionModel().getSelectedItem().getSymbol();
      if (selected != null) {
        tickerSymbolCbox.getEditor().setText(selected);
        setupLineChart(selected);
      }
    }
  }

  @FXML
  void onKeyReleasedTickerSymbolCbox(KeyEvent event) {
    if (!tickerSymbolCbox.getSelectionModel().isEmpty()) {
      tickerSymbolCbox.show();
//      return;
    }
    if (event.getCode().isArrowKey()) {
      return;
    }
    if (event.getCode() == KeyCode.ENTER) {
      onActionTicker();
    }
    tickerSymbolCbox.setVisibleRowCount(10);
    List<Asset> filtered = usEquities.stream()
        .filter((a) -> a.getSymbol().toLowerCase()
            .contains(tickerSymbolCbox.getEditor().getText().toLowerCase()) || a.getName()
            .toLowerCase().contains(tickerSymbolCbox.getEditor().getText().toLowerCase()))
        .sorted((a, b) -> {
          String aString = a.getSymbol().toLowerCase() + a.getName().toLowerCase();
          String bString = b.getSymbol().toLowerCase() + b.getName().toLowerCase();
          return aString.indexOf(tickerSymbolCbox.getEditor().getText().toLowerCase())
              - bString.indexOf(tickerSymbolCbox.getEditor().getText().toLowerCase());
        })
        .collect(Collectors.toList());

    Platform.runLater(() -> {
      tickerSymbolCbox.getItems().clear();
      tickerSymbolCbox.getItems().addAll(filtered);
      if (tickerSymbolCbox.getItems().size() > 0) {
        tickerSymbolCbox.show();
      }
    });
  }

  @FXML
  void onKeyTypedQuantityField(KeyEvent event) {
    checkSubmit();
    if (tickerSymbolCbox.getValue() != null) {
      try {
        Double input = null;
        try {
          input = Double.parseDouble(quantityField.getText());
        } catch (NumberFormatException n) {
          n.printStackTrace();
        }
        input = (input == null) ? 0 : input;
        Double price = 0.0;
        if (alpacaAPI.getClock().getIsOpen()) {
          LatestQuoteResponse latestQuote = alpacaAPI
              .getLatestQuote(tickerSymbolCbox.getEditor().getText());
          price =
              buyBtn.isSelected() ? latestQuote.getQuote().getAp() : latestQuote.getQuote().getBp();
        } else {
          var latestTrade = alpacaAPI.getLatestTrade(tickerSymbolCbox.getEditor().getText());
          price = latestTrade.getTrade().getP();
        }
        estimatedPriceLbl.setText("Estimated Price: $" + price * input);
      } catch (AlpacaAPIRequestException e) {
        e.printStackTrace();
      }
    }
  }

  private void checkSubmit() {
    submitBtn.setDisable(
        (!buyBtn.isSelected() && !sellBtn.isSelected()) || quantityField.getText().isEmpty()
            || tickerSymbolCbox.getSelectionModel().getSelectedItem() == null);
  }

  @FXML
  private TableView<Position> portfolioTable;

  @FXML
  private TableColumn<Position, String> portfolioSymbolCol;

  @FXML
  private TableColumn<Position, String> portfolioPositionCol;

  @FXML
  private TableColumn<Position, String> portfolioValueCol;

  @FXML
  private TableColumn<Position, String> portfolioPandLCol;

  @FXML
  private TableColumn<Position, String> portfolioPercentCol;

  @FXML
  private TableView<Asset> watchlistTable;

  @FXML
  private TableColumn<Asset, String> watchlistSymbolCol;

  @FXML
  private TableColumn<Asset, String> watchlistRatingCol;

  @FXML
  private TableColumn<Asset, String> watchlistValueCol;

  @FXML
  private TableColumn<Asset, String> watchlistChangeCol;

  @FXML
  private TableColumn<Asset, String> watchlistPercentCol;

  @FXML
  private ComboBox<Asset> tickerSymbolCbox;

  @FXML
  private AreaChart<Number, Number> areaChart;

  @FXML
  private ToggleButton buyBtn;

  @FXML
  private ToggleGroup buySellGroup;

  @FXML
  private ToggleButton sellBtn;


  @FXML
  private Button submitBtn;

  @FXML
  private TableView<Order> ordersTable;

  @FXML
  private TableColumn<Order, String> ordersSymbolCol;

  @FXML
  private TableColumn<Order, String> ordersOrderCol;

  @FXML
  private TableColumn<Order, String> ordersSharesCol;

  @FXML
  private TableColumn<Order, String> ordersPricePerShareCol;

  @FXML
  private TableColumn<Order, String> ordersAmountCol;

  @FXML
  private TableColumn<Order, String> ordersStatusCol;

  @FXML
  private ToggleButton scanBtn;

  @FXML
  private Button testBtn;

  @FXML
  private CheckBox autoAddCheck;

  @FXML
  private CheckBox autoBuyCheck;

  @FXML
  private CheckBox autoSellCheck;

  @FXML
  private CheckBox allowFractionalCheck;

  @FXML
  private TextField maxBidTxt;

  @FXML
  private TextField sellLagTxt;

  @FXML
  private ListView<String> newsParent;

  @FXML
  private Label equityLbl;

  @FXML
  private Label dailyProfitLbl;

  @FXML
  private Label buyingPowerLbl;

  @FXML
  private Label clockLbl;

  @FXML
  private Label quotePrice;

  @FXML
  private Label quoteChange;

  @FXML
  private TextField quantityField;

  @FXML
  private Label estimatedPriceLbl;

}
