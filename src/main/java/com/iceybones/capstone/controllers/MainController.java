package com.iceybones.capstone.controllers;

<<<<<<< HEAD
import com.iceybones.capstone.app.CleanNews;
import com.iceybones.capstone.app.ScrapeNews;
import com.iceybones.capstone.dl4j.PrepareNews;
import com.iceybones.capstone.dl4j.TestNews;
import com.iceybones.capstone.dl4j.TrainNews;
import com.iceybones.capstone.models.AlpacaWrapper;
=======
import com.iceybones.capstone.CleanNews;
import com.iceybones.capstone.ScrapeNews;
import com.iceybones.capstone.dl4j.PrepareNews;
import com.iceybones.capstone.dl4j.TestNews;
import com.iceybones.capstone.dl4j.TrainNews;
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
import com.iceybones.capstone.models.WatchlistEntry;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
<<<<<<< HEAD
import java.nio.file.StandardOpenOption;
=======
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
<<<<<<< HEAD
import java.util.Collections;
=======
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;
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
<<<<<<< HEAD
import javafx.scene.control.ChoiceBox;
=======
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
<<<<<<< HEAD
import javafx.scene.control.RadioButton;
=======
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
import javafx.scene.control.Slider;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;
<<<<<<< HEAD
import net.jacobpeterson.abstracts.enums.SortDirection;
import net.jacobpeterson.abstracts.websocket.exception.WebsocketException;
=======
import javax.tools.Tool;
import net.jacobpeterson.abstracts.enums.SortDirection;
import net.jacobpeterson.abstracts.websocket.exception.WebsocketException;
import net.jacobpeterson.alpaca.AlpacaAPI;
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
import net.jacobpeterson.alpaca.enums.asset.AssetStatus;
import net.jacobpeterson.alpaca.enums.marketdata.BarsTimeFrame;
import net.jacobpeterson.alpaca.enums.order.OrderSide;
import net.jacobpeterson.alpaca.enums.order.OrderStatus;
import net.jacobpeterson.alpaca.enums.order.OrderTimeInForce;
import net.jacobpeterson.alpaca.rest.exception.AlpacaAPIRequestException;
import net.jacobpeterson.alpaca.websocket.broker.listener.AlpacaStreamListener;
import net.jacobpeterson.alpaca.websocket.broker.listener.AlpacaStreamListenerAdapter;
import net.jacobpeterson.alpaca.websocket.broker.message.AlpacaStreamMessageType;
import net.jacobpeterson.alpaca.websocket.marketdata.listener.MarketDataListener;
import net.jacobpeterson.alpaca.websocket.marketdata.listener.MarketDataListenerAdapter;
import net.jacobpeterson.alpaca.websocket.marketdata.message.MarketDataMessageType;
import net.jacobpeterson.domain.alpaca.account.Account;
import net.jacobpeterson.domain.alpaca.asset.Asset;
import net.jacobpeterson.domain.alpaca.marketdata.historical.bar.BarsResponse;
import net.jacobpeterson.domain.alpaca.marketdata.historical.snapshot.Snapshot;
import net.jacobpeterson.domain.alpaca.marketdata.historical.trade.Trade;
<<<<<<< HEAD
=======
import net.jacobpeterson.domain.alpaca.marketdata.historical.trade.TradesResponse;
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
import net.jacobpeterson.domain.alpaca.marketdata.realtime.MarketDataMessage;
import net.jacobpeterson.domain.alpaca.marketdata.realtime.bar.BarMessage;
import net.jacobpeterson.domain.alpaca.order.Order;
import net.jacobpeterson.domain.alpaca.position.Position;
import net.jacobpeterson.domain.alpaca.streaming.AlpacaStreamMessage;
import net.jacobpeterson.domain.alpaca.streaming.account.AccountUpdateMessage;
import net.jacobpeterson.domain.alpaca.streaming.trade.TradeUpdateMessage;

public class MainController implements Initializable {

<<<<<<< HEAD
  public enum BuyType {
    ALL_BUYS("All Buys"), STRONG_ONLY("Strong Only");
    String txt;

    BuyType(String txt) {
      this.txt = txt;
    }
  }

=======
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
  ExecutorService service = Executors.newSingleThreadExecutor();
  ObservableList<String> scanResults;
  ObservableList<Position> positions = FXCollections.observableArrayList();
  ObservableList<Order> orders = FXCollections.observableArrayList();
  ObservableList<WatchlistEntry> watchlist = FXCollections.observableArrayList();
  String watchlistId;
  BarsResponse currentBarResponse = new BarsResponse();
  Snapshot currentSnapshot = new Snapshot();
<<<<<<< HEAD
  //  AlpacaAPI alpacaAPI = new AlpacaAPI();
  public static HashMap<String, Asset> usEquities;
  Map<String, Integer> buyMap = new HashMap<>();
  ScrapeNews scraper = new ScrapeNews();
  CleanNews cleanNews = new CleanNews();
  TestNews testNews = new TestNews();
  int lineChartLimit = 50;
=======
  AlpacaAPI alpacaAPI = new AlpacaAPI();
  public static HashMap<String, Asset> usEquities;
  ScrapeNews scraper = new ScrapeNews();
  CleanNews cleanNews = new CleanNews();
  TestNews testNews = new TestNews();
  int lineChartLimit = 100;
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
  double currentStockPrice = 0.0;

  {
    try {
<<<<<<< HEAD
      usEquities = (HashMap<String, Asset>) AlpacaWrapper.getAPI()
          .getAssets(AssetStatus.ACTIVE, "us_equity")
=======
      usEquities = (HashMap<String, Asset>) alpacaAPI.getAssets(AssetStatus.ACTIVE, "us_equity")
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
          .stream()
          .collect(Collectors.toMap((Asset a) -> a.getSymbol(), (Asset b) -> b));
//      usEquities.sort(Comparator.comparing(Asset::getSymbol));
    } catch (AlpacaAPIRequestException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
<<<<<<< HEAD
    int counter = 0;
    var tryServ = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    tickerSymbolCbox.setOnHiding(e -> {
      onActionTicker();
    });
    autoTypeChoice.getItems().addAll(BuyType.values());
    autoTypeChoice.getSelectionModel().select(0);
    autoTypeChoice.setConverter(new StringConverter<BuyType>() {
      @Override
      public String toString(BuyType o) {
        return o != null ? o.txt : "";
      }

      @Override
      public BuyType fromString(String s) {
        switch (s) {
          case "Buy All":
            return BuyType.ALL_BUYS;
          case "Strong Only":
            return BuyType.STRONG_ONLY;
        }
        return null;
      }
    });
    typeTestChoice.getItems().addAll(BuyType.values());
    typeTestChoice.getSelectionModel().select(0);
    typeTestChoice.setConverter(new StringConverter<BuyType>() {
      @Override
      public String toString(BuyType o) {
        return o != null ? o.txt : "";
      }

      @Override
      public BuyType fromString(String s) {
        switch (s) {
          case "Buy All":
            return BuyType.ALL_BUYS;
          case "Strong Only":
            return BuyType.STRONG_ONLY;
        }
        return null;
      }
    });
=======
    tickerSymbolCbox.setOnHiding(e -> {
      onActionTicker();
    });
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
    runClock();
    updateCash();
    setupPortfolioTable();
    setupOrdersTable();
    setupWatchlistTable();
<<<<<<< HEAD
    newsParent.getItems()
        .addAll("========== Directions ==========",
            "Step 1: Press the SCAN button to scrape today's news headlines.",
            "- Wait for it to finish or press the SCAN button again to stop scanning and save results.",
            "Step 2: Choose a buy type and press EVALUATE to evaluate the saved news headlines.",
            "- News articles will be classified as either SELL, BUY or BUY STRONG indicators.",
            "- Select a buy type of ALL BUYS to create a buy plan that includes both BUY and BUY STRONG indicators.",
            "- Otherwise, select STRONG ONLY to only consider BUY STRONG indicators in the creation of the buy plan.",
            "Step 3: Once a buy plan has been created, enter a BUY LIMIT % and press CONFIRM BUYS to execute the buy plan.",
            "- The BUY LIMIT % is the max percentage that you are willing to spend over the bid price for each order.",
            "- This step may only be peformed before the market opens.",
            "- Once submitted, all orders will attempt to be filled at market open.",
            "- Any orders that are not able to be filled at market open will be cancelled.",
            "Step 4: Enter a TRAILING STOP % and press SELL ALL to submit trailing stop sell orders for all open positions.",
            "- This step may be performed at any time.",
            "- The TRAILING STOP % defines the percentage loss below the HIGH WATER MARK that will trigger an immediate sell order.",
            "- The submitted sell orders will stay active until cancelled.",
            "Tips: Shortly before market open perform a complete news scan, evaluation and confirm the buy plan.",
            "Tips: Once the market opens and all buy orders have either been filled or cancelled, submit the sell all order.",
            "=============================================================");
    testNewsParent.getItems()
        .addAll("========== Directions ==========",
            "If no classification is on file, enter a Buy % and a Buy Strong % and click Classify.",
=======
    testNewsParent.getItems()
        .addAll("=============================================================",
            "Directions: If no classification is on file, enter a Buy % and a Buy Strong % and click Classify.",
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
            "Buy % is a measure of the lowest projected increase to a stock that will cause it to be considered a good buy.",
            "Likewise, Buy Strong % is a determination as to what projected increase would be considered a great buy.",
            "Example: Buy % = 5 and Buy Strong % = 10",
            "=============================================================");
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
        for (var asset : usEquities.keySet()) {
          if (usEquities.get(asset).getSymbol().equals(string)) {
            out = usEquities.get(asset);
          }
        }
        return out;
      }
    });
    var format = new DecimalFormat("#.0");
    UnaryOperator<TextFormatter.Change> decimalOnly = (TextFormatter.Change c) ->
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
    };
    testStartDatePicker.setDayCellFactory(picker -> new DateCell() {
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
<<<<<<< HEAD
        setDisable(empty || date.compareTo(LocalDate.of(2009, 2, 11)) < 0
            || date.compareTo(LocalDate.of(2020, 6, 9)) > 0
            || date.getDayOfWeek() == DayOfWeek.SUNDAY
            || date.getDayOfWeek() == DayOfWeek.SATURDAY);
=======
        setDisable(empty || date.compareTo(LocalDate.of(2015, 6, 10)) < 0
            || date.compareTo(LocalDate.of(2020, 6, 9)) > 0);
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
      }
    });
    testStartDatePicker.setValue(LocalDate.of(2020, 6, 1));
    testEndDatePicker.setValue(LocalDate.of(2020, 6, 5));
    testEndDatePicker.setDayCellFactory(picker -> new DateCell() {
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
<<<<<<< HEAD
        setDisable(empty || date.compareTo(LocalDate.of(2009, 2, 11)) < 0
            || date.compareTo(LocalDate.of(2020, 6, 9)) > 0
            || date.getDayOfWeek() == DayOfWeek.SUNDAY
            || date.getDayOfWeek() == DayOfWeek.SATURDAY);
=======
        setDisable(empty || date.compareTo(LocalDate.of(2015, 6, 10)) < 0
            || date.compareTo(LocalDate.of(2020, 6, 9)) > 0);
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
      }
    });

    TableColumn<WatchlistEntry, String> nameCol = new TableColumn<WatchlistEntry, String>();

    nameCol.setCellFactory
        (
            column ->
            {
              return new TableCell<WatchlistEntry, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {

                }
              };
            });

    quantityField.setTextFormatter(new TextFormatter<>(decimalOnly));
    buyPercentTxt.setTextFormatter(new TextFormatter<>(decimalOnly));
    buyStrongPercentTxt.setTextFormatter(new TextFormatter<>(decimalOnly));
<<<<<<< HEAD
    investmentTestTxt.setTextFormatter(new TextFormatter<>(decimalOnly));
    trailingSLTestTxt.setTextFormatter(new TextFormatter<>(decimalOnly));
    buyLimitPercentTxt.setTextFormatter(new TextFormatter<>(decimalOnly));
    investmentOpTxt.setTextFormatter(new TextFormatter<>(decimalOnly));
    trailingSLOpTxt.setTextFormatter(new TextFormatter<>(decimalOnly));
=======
    sellPercentTestTxt.setTextFormatter(new TextFormatter<>(decimalOnly));
    sellStrongPercentTestTxt.setTextFormatter(new TextFormatter<>(decimalOnly));
    maxDailyTestTxt.setTextFormatter(new TextFormatter<>(decimalOnly));
    maxBuyTestTxt.setTextFormatter(new TextFormatter<>(decimalOnly));
    maxBuyTxt.setTextFormatter(new TextFormatter<>(decimalOnly));
    maxDailyTxt.setTextFormatter(new TextFormatter<>(decimalOnly));
    sellPercentTxt.setTextFormatter(new TextFormatter<>(decimalOnly));
    sellStrongPercentTxt.setTextFormatter(new TextFormatter<>(decimalOnly));
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
    minibatchTxt.setTextFormatter(new TextFormatter<>(decimalOnly));
    epochTxt.setTextFormatter(new TextFormatter<>(decimalOnly));
    testSizeTxt.setTextFormatter(new TextFormatter<>(decimalOnly));

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
<<<<<<< HEAD
        Account account = AlpacaWrapper.getAPI().getAccount();
=======
        Account account = alpacaAPI.getAccount();
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
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
    }, 0, 5, TimeUnit.SECONDS);
  }

  private void setupPortfolioTable() {
    portfolioSymbolCol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
    portfolioPositionCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
    portfolioValueCol.setCellValueFactory(cellData -> Bindings.createDoubleBinding(
        () -> Double.parseDouble(cellData.getValue().getMarketValue())));
    portfolioPandLCol.setCellValueFactory(cellData -> Bindings.createDoubleBinding(
        () -> Double.parseDouble(cellData.getValue().getUnrealizedPl())));
    portfolioPercentCol.setCellValueFactory(cellData -> Bindings.createDoubleBinding(
        () -> Double.parseDouble(cellData.getValue().getUnrealizedPlpc())));
    portfolioPandLCol.setCellFactory(
        new Callback<TableColumn<Position, Number>, TableCell<Position, Number>>() {
          @Override
          public TableCell<Position, Number> call(
              TableColumn<Position, Number> positionStringTableColumn) {
            return new TableCell<Position, Number>() {
              @Override
              protected void updateItem(Number item, boolean empty) {
                if (empty || item == null) {
                  setText(null);
                  return;
                }
                if (item.doubleValue() < 0) {
                  setTextFill(Color.RED);
                } else {
                  setTextFill(Color.GREEN);
                }
                DecimalFormat df = new DecimalFormat("###,##0.00");
                setText("$" + df.format(item));
              }
            };
          }

          ;
        });
    portfolioPercentCol.setCellFactory(
        new Callback<TableColumn<Position, Number>, TableCell<Position, Number>>() {
          @Override
          public TableCell<Position, Number> call(
              TableColumn<Position, Number> positionStringTableColumn) {
            return new TableCell<Position, Number>() {
              @Override
              protected void updateItem(Number item, boolean empty) {
                if (item == null) {
                  setText(null);
                  return;
                }
                if (item.doubleValue() < 0) {
                  setTextFill(Color.RED);
                } else {
                  setTextFill(Color.GREEN);
                }
                DecimalFormat df = new DecimalFormat("###,##0.00");
                setText(df.format(item) + "%");
              }
            };
          }

          ;
        });
    portfolioValueCol.setCellFactory(
        new Callback<TableColumn<Position, Number>, TableCell<Position, Number>>() {
          @Override
          public TableCell<Position, Number> call(
              TableColumn<Position, Number> positionStringTableColumn) {
            return new TableCell<Position, Number>() {
              @Override
              protected void updateItem(Number item, boolean empty) {
                if (empty || item == null) {
                  setText(null);
                  return;
                }
                DecimalFormat df = new DecimalFormat("###,##0.00");
                setText("$" + df.format(item));
              }
            };
          }

          ;
        });
<<<<<<< HEAD
=======
    watchlistValueCol.setCellFactory(
        new Callback<TableColumn<WatchlistEntry, Double>, TableCell<WatchlistEntry, Double>>() {
          @Override
          public TableCell<WatchlistEntry, Double> call(
              TableColumn<WatchlistEntry, Double> watchlistEntryStringTableColumn) {
            return new TableCell<WatchlistEntry, Double>() {
              @Override
              protected void updateItem(Double item, boolean empty) {
                if (item == null) {
                  setText(null);
                  return;
                }
                DecimalFormat df = new DecimalFormat("###,##0.00");
                setText("$" + df.format(item));
              }
            };
          }

          ;
        });
    watchlistChangeCol.setCellFactory(
        new Callback<TableColumn<WatchlistEntry, Double>, TableCell<WatchlistEntry, Double>>() {
          @Override
          public TableCell<WatchlistEntry, Double> call(
              TableColumn<WatchlistEntry, Double> watchlistEntryStringTableColumn) {
            return new TableCell<WatchlistEntry, Double>() {
              @Override
              protected void updateItem(Double item, boolean empty) {
                if (item == null) {
                  setText(null);
                  return;
                }
                if (item < 0) {
                  setTextFill(Color.RED);
                } else {
                  setTextFill(Color.GREEN);
                }
                DecimalFormat df = new DecimalFormat("###,##0.00");
                setText("$" + df.format(item));
              }
            };
          }

          ;
        });
    watchlistPercentCol.setCellFactory(
        new Callback<TableColumn<WatchlistEntry, Double>, TableCell<WatchlistEntry, Double>>() {
          @Override
          public TableCell<WatchlistEntry, Double> call(
              TableColumn<WatchlistEntry, Double> watchlistEntryStringTableColumn) {
            return new TableCell<WatchlistEntry, Double>() {
              @Override
              protected void updateItem(Double item, boolean empty) {
                if (item == null) {
                  setText(null);
                  return;
                }
                if (item < 0) {
                  setTextFill(Color.RED);
                } else {
                  setTextFill(Color.GREEN);
                }
                DecimalFormat df = new DecimalFormat("###,##0.00");
                setText(df.format(item) + "%");
              }
            };
          }

          ;
        });
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
    portfolioTable.setItems(positions);
    updatePortfolioTable();
  }

  private void updatePortfolioTable() {
    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
      try {
<<<<<<< HEAD
        var openPositions = AlpacaWrapper.getAPI().getOpenPositions().stream()
=======
        var openPositions = alpacaAPI.getOpenPositions().stream()
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
            .collect(Collectors.toMap((a) -> a.getSymbol(), (b) -> b));
        for (int i = 0; i < positions.size(); i++) {
          if (openPositions.containsKey(positions.get(i).getSymbol())) {
            int finalI = i;
            positions.set(finalI, openPositions.get(positions.get(finalI).getSymbol()));
          } else {
            positions.remove(i--);
          }
        }
        for (var key : openPositions.keySet()) {
<<<<<<< HEAD
          if (!positions.contains(openPositions.get(key))) {
=======
          if(!positions.contains(openPositions.get(key))) {
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
            positions.add(openPositions.get(key));
          }
        }
//        positions.setAll();
      } catch (AlpacaAPIRequestException e) {
        e.printStackTrace();
      }
    }, 0, 3, TimeUnit.SECONDS);
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
<<<<<<< HEAD
    ordersTable.setItems(orders);
    updateOrdersTable();
    try {
=======
    updateOrdersTable();
    ordersTable.setItems(orders);
    try {
      // List to account updates and trade updates from Alpaca and print their messages out
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
      AlpacaStreamListener alpacaStreamListener = new AlpacaStreamListenerAdapter(
          AlpacaStreamMessageType.ACCOUNT_UPDATES,
          AlpacaStreamMessageType.TRADE_UPDATES) {
        @Override
        public void onStreamUpdate(AlpacaStreamMessageType streamMessageType,
            AlpacaStreamMessage streamMessage) {
          switch (streamMessageType) {
            case ACCOUNT_UPDATES:
              System.out.println((AccountUpdateMessage) streamMessage);
              break;
            case TRADE_UPDATES:
              if (((TradeUpdateMessage) streamMessage).getData().getEvent().getAPIName()
                  .equals("fill")) {
                Toolkit.getDefaultToolkit().beep();
              }
              updateOrdersTable();
              break;
          }
        }
      };
<<<<<<< HEAD
      AlpacaWrapper.getAPI().addAlpacaStreamListener(alpacaStreamListener);
=======
      alpacaAPI.addAlpacaStreamListener(alpacaStreamListener);
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4

    } catch (WebsocketException exception) {
      exception.printStackTrace();
    }
  }

  private void updateOrdersTable() {
    try {
<<<<<<< HEAD
      var alpacaOrders = AlpacaWrapper.getAPI()
          .getOrders(OrderStatus.ALL, 500, ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")).minusDays(30),
              ZonedDateTime.now().plusMinutes(2),
              SortDirection.DESCENDING, false, null);
      orders.setAll(alpacaOrders);
=======
      orders.setAll(alpacaAPI
          .getOrders(OrderStatus.ALL, 50, ZonedDateTime.now().minusDays(30), ZonedDateTime.now(),
              SortDirection.DESCENDING, false, null
          ));
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
    } catch (AlpacaAPIRequestException e) {
      e.printStackTrace();
    }

  }

  private void setupWatchlistTable() {
    watchlistSymbolCol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
    watchlistRatingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
    watchlistChangeCol.setCellValueFactory(new PropertyValueFactory<>("change"));
    watchlistPercentCol.setCellValueFactory(new PropertyValueFactory<>("percent"));
    watchlistValueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
<<<<<<< HEAD
    watchlistValueCol.setCellFactory(
        new Callback<TableColumn<WatchlistEntry, Double>, TableCell<WatchlistEntry, Double>>() {
          @Override
          public TableCell<WatchlistEntry, Double> call(
              TableColumn<WatchlistEntry, Double> watchlistEntryStringTableColumn) {
            return new TableCell<WatchlistEntry, Double>() {
              @Override
              protected void updateItem(Double item, boolean empty) {
                if (item == null) {
                  setText(null);
                  return;
                }
                DecimalFormat df = new DecimalFormat("###,##0.00");
                setText("$" + df.format(item));
              }
            };
          }

          ;
        });
    watchlistChangeCol.setCellFactory(
        new Callback<TableColumn<WatchlistEntry, Double>, TableCell<WatchlistEntry, Double>>() {
          @Override
          public TableCell<WatchlistEntry, Double> call(
              TableColumn<WatchlistEntry, Double> watchlistEntryStringTableColumn) {
            return new TableCell<WatchlistEntry, Double>() {
              @Override
              protected void updateItem(Double item, boolean empty) {
                if (item == null) {
                  setText(null);
                  return;
                }
                if (item < 0) {
                  setTextFill(Color.RED);
                } else {
                  setTextFill(Color.GREEN);
                }
                DecimalFormat df = new DecimalFormat("###,##0.00");
                setText("$" + df.format(item));
              }
            };
          }

          ;
        });
    watchlistPercentCol.setCellFactory(
        new Callback<TableColumn<WatchlistEntry, Double>, TableCell<WatchlistEntry, Double>>() {
          @Override
          public TableCell<WatchlistEntry, Double> call(
              TableColumn<WatchlistEntry, Double> watchlistEntryStringTableColumn) {
            return new TableCell<WatchlistEntry, Double>() {
              @Override
              protected void updateItem(Double item, boolean empty) {
                if (item == null) {
                  setText(null);
                  return;
                }
                if (item < 0) {
                  setTextFill(Color.RED);
                } else {
                  setTextFill(Color.GREEN);
                }
                DecimalFormat df = new DecimalFormat("###,##0.00");
                setText(df.format(item) + "%");
              }
            };
          }

          ;
        });
    watchlistTable.setItems(watchlist);
  }

  private void priceWatchlist() {
    try {
      var symbols = watchlist.stream().map(a -> a.getSymbol())
          .collect(Collectors.toList());
      Map<String, Snapshot> snapshots = AlpacaWrapper.getAPI().getSnapshots(symbols);
      for (var element : watchlist) {
        element.setValue(snapshots.get(element.getSymbol()).getLatestTrade().getP());
        if (snapshots.get(element.getSymbol()).getPrevDailyBar() == null
            || snapshots.get(element.getSymbol()).getDailyBar() == null) {
          continue;
        }
        element.setChange(snapshots.get(element.getSymbol()).getDailyBar().getC() - snapshots
            .get(element.getSymbol()).getPrevDailyBar().getC());
        element
            .setPercent(100 * (snapshots.get(element.getSymbol()).getDailyBar().getC() / snapshots
                .get(element.getSymbol()).getPrevDailyBar().getC() - 1));
      }
    } catch (AlpacaAPIRequestException e) {
      e.printStackTrace();
    }
  }

  private void updateWatchlistTable() {
    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate((() -> {
      priceWatchlist();
=======
    watchlistTable.setItems(watchlist);
  }

  private void updateWatchlistTable() {
    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate((() -> {
      try {
        var symbols = watchlist.stream().map(a -> a.getSymbol())
            .collect(Collectors.toList());

        Map<String, Snapshot> snapshots = alpacaAPI.getSnapshots(symbols);
        for (var element : watchlist) {
          element.setValue(snapshots.get(element.getSymbol()).getLatestTrade().getP());
          if (snapshots.get(element.getSymbol()).getPrevDailyBar() == null
              || snapshots.get(element.getSymbol()).getDailyBar() == null) {
            continue;
          }
          element.setChange(snapshots.get(element.getSymbol()).getDailyBar().getC() - snapshots
              .get(element.getSymbol()).getPrevDailyBar().getC());
          element
              .setPercent(100 * (snapshots.get(element.getSymbol()).getDailyBar().getC() / snapshots
                  .get(element.getSymbol()).getPrevDailyBar().getC() - 1));
        }
      } catch (AlpacaAPIRequestException e) {
        e.printStackTrace();
      }
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
      watchlistTable.refresh();
    }), 0, 10, TimeUnit.SECONDS);
  }

  private void getBars(String symbol) {
    try {
<<<<<<< HEAD
      currentBarResponse = AlpacaWrapper.getAPI().getBars(
          symbol,
          ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")).minusDays(6),
          ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")).plusHours(3).plusMinutes(45),
=======
      currentBarResponse = alpacaAPI.getBars(
          symbol,
          ZonedDateTime.now().minusDays(6),
          ZonedDateTime.now().plusHours(3).plusMinutes(45),
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
          10000,
          null,
          BarsTimeFrame.MINUTE);
    } catch (AlpacaAPIRequestException e) {
      e.printStackTrace();
    }
  }

  private void setupAreaChart(String symbol) {
    XYChart.Series<Number, Number> series = new Series<>();

    double change = 0.0;
    double changePct = 0.0;
    if (currentSnapshot.getPrevDailyBar() != null && currentSnapshot.getDailyBar() != null) {
      change = currentSnapshot.getDailyBar().getC() - currentSnapshot.getPrevDailyBar().getC();
      changePct =
<<<<<<< HEAD
          100 * (currentSnapshot.getDailyBar().getC() / currentSnapshot.getPrevDailyBar().getC()
              - 1);
=======
          100 * (currentSnapshot.getDailyBar().getC() / currentSnapshot.getPrevDailyBar().getC() - 1);
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
    }
    DecimalFormat df = new DecimalFormat("###,##0.00");
    if (change < 0) {
      quoteChange.setTextFill(Color.RED);
    } else {
      quoteChange.setTextFill(Color.GREEN);
    }
    quoteChange.setText("$" + df.format(change) + " (" + df.format(changePct) + "%)");
<<<<<<< HEAD
    if (currentBarResponse.getSymbol() == null || !currentBarResponse.getSymbol().equals(symbol)) {
      getBars(symbol);
      updateSeries(series, symbol);
=======
    if(currentBarResponse.getSymbol() == null || !currentBarResponse.getSymbol().equals(symbol)) {
      getBars(symbol);
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
    }
    var bars = (new ArrayList<>(currentBarResponse.getBars().stream()
        .sorted((a, b) -> (int) (b.getT().toEpochSecond() - a.getT().toEpochSecond()))
        .limit(lineChartLimit)
        .collect(Collectors.toList())));
    for (var bar : bars) {
      series.getData().add(new XYChart.Data<>(bar.getT().toEpochSecond(), bar.getC()));
    }
<<<<<<< HEAD
//      Trade latest = AlpacaWrapper.borrowAPI().getLatestTrade(symbol).getTrade();
=======
    updateSeries(series, symbol);
//      Trade latest = alpacaAPI.getLatestTrade(symbol).getTrade();
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
//      series.getData().add(new XYChart.Data<>(latest.getT().toEpochSecond(), latest.getP()));
    quotePrice.setText("$" + bars.get(0).getC());

    try {
      MarketDataListener listener = new MarketDataListenerAdapter(
          symbol,
          MarketDataMessageType.TRADE,
          MarketDataMessageType.QUOTE,
          MarketDataMessageType.BAR) {
        @Override
        public void onStreamUpdate(MarketDataMessageType streamMessageType,
            MarketDataMessage streamMessage) {
          switch (streamMessageType) {
            case TRADE:
              break;
            case QUOTE:
              break;
            case BAR:
              BarMessage barMessage = (BarMessage) streamMessage;
              series.getData().add(new XYChart.Data<>(barMessage.getTimestamp().toEpochSecond(),
                  barMessage.getClose()));
              quotePrice.setText("$" + barMessage.getClose());
              try {
<<<<<<< HEAD
                var snapshot = AlpacaWrapper.getAPI().getSnapshot(symbol);
=======
                var snapshot = alpacaAPI.getSnapshot(symbol);
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
                quoteChange.setText(
                    Double.toString(snapshot.getDailyBar().getC() - snapshot.getDailyBar().getO()));
              } catch (AlpacaAPIRequestException e) {
                e.printStackTrace();
              }
              break;
          }
        }
      };
<<<<<<< HEAD
      AlpacaWrapper.getAPI().addMarketDataStreamListener(listener);
=======
      alpacaAPI.addMarketDataStreamListener(listener);
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
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
        DecimalFormat df = new DecimalFormat("#.00");
        return "$" + df.format(number);
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
<<<<<<< HEAD
        var z = ZonedDateTime.ofInstant(i, ZoneId.systemDefault());
=======
        var z = ZonedDateTime.ofInstant(i, ZoneId.of("America/New_York"));
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
        return z.format(dtf);
      }

      @Override
      public Number fromString(String s) {
        return null;
      }
    });
    areaChart.autosize();
  }

<<<<<<< HEAD
  private void buyAll(BuyType buyType, Double maxDaily) {
    var buyList = watchlist.stream().filter((a) -> a.getRatingRaw().equals("buy")).collect(
        Collectors.toList());
    var buyStrongList = watchlist.stream().filter((a) -> a.getRatingRaw().equals("buyStrong"))
        .collect(
            Collectors.toList());
    if (buyType == BuyType.ALL_BUYS) {
      buyStrongList.addAll(buyList);
    }
    buyStrongList.sort((a, b) -> (int) ((a.getValue() * 1000) - (b.getValue() * 1000)));
    Map<String, Integer> orderList = new HashMap<>();
    double runningTotal = 0.0;
    boolean done = false;
    while (!done) {
      double iterationTotal = 0.0;
      for (int i = 0; i < buyStrongList.size() - 1; i++) {
        if (buyStrongList.get(i + 1).getValue() < iterationTotal) {

        }
      }
    }
  }

  ScheduledExecutorService seriesUpdater = Executors.newSingleThreadScheduledExecutor();

  private void updateSeries(Series series, String symbol) {
    seriesUpdater.shutdownNow();
    seriesUpdater = Executors.newSingleThreadScheduledExecutor();
    seriesUpdater.scheduleAtFixedRate(() -> {
      Trade latest = null;
      try {
        latest = AlpacaWrapper.getAPI().getLatestTrade(symbol).getTrade();
=======
  ScheduledExecutorService seriesUpdater = Executors.newSingleThreadScheduledExecutor();

  private void updateSeries(Series series, String symbol) {
    seriesUpdater.scheduleAtFixedRate(() -> {
      Trade latest = null;
      try {
        latest = alpacaAPI.getLatestTrade(symbol).getTrade();
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
      } catch (AlpacaAPIRequestException e) {
        e.printStackTrace();
      }
      Trade finalLatest = latest;
      Platform.runLater(() -> series.getData()
          .add(new XYChart.Data<>(finalLatest.getT().toEpochSecond(), finalLatest
              .getP())));

    }, 0, 5, TimeUnit.SECONDS);
  }

  private void backTest() {
<<<<<<< HEAD
    automationIndicator.setVisible(true);
=======

>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
    try (var out = Files.newBufferedWriter(Path.of("bought_checked.csv"));
        var in = Files.newBufferedReader(Path.of("bought.csv"))) {
      String line = null;
      while ((line = in.readLine()) != null) {
        var lineSplit = line.split(",");
<<<<<<< HEAD
        var dailyBar = AlpacaWrapper.getAPI().getSnapshot(lineSplit[1]).getDailyBar();
=======
        var dailyBar = alpacaAPI.getSnapshot(lineSplit[1]).getDailyBar();
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
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
<<<<<<< HEAD
    automationIndicator.setVisible(false);
  }

  @FXML
  void onActionGetDataBtn(ActionEvent event) {
    DecimalFormat df = new DecimalFormat("##0.0###");
    try {
      var lines = Files.newBufferedReader(Path.of("data/^RUA.csv"))
          .lines().skip(1).collect(Collectors.toList());
      Collections.reverse(lines);
      var out = Files.newBufferedWriter(Path.of("data/^RUA_trimmed.csv"),
          StandardOpenOption.CREATE);
      out.write("Date,Performance");
      out.newLine();
      lines.stream().map((a) -> {
       var linesplit = a.split(",");
       return linesplit[0] + "," + (a.contains("null") ? "1.0"
           : df.format(Double.parseDouble(linesplit[5]) / Double.parseDouble(linesplit[1])));
      }).forEach((a) -> {
        try {
          System.out.println(a);
          out.write(a);
          out.newLine();
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
=======
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
  }

  @FXML
  void onActionScanBtn(ActionEvent event) throws IOException, InterruptedException {
<<<<<<< HEAD
    investmentOpTxt.clear();
    evaluateBtn.setDisable(true);
    if (evaluateBtn.isSelected()) {
      scanBtn.setSelected(false);
      return;
    }
    automationIndicator.setVisible(true);
    if (scanBtn.isSelected()) {
=======
    automationIndicator.setVisible(true);
    if (scanBtn.isSelected()) {
//      service.submit(() -> {
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
      try {
        scanResults = scraper
            .scan(newsParent, usEquities.keySet().stream().sorted().toList(), this);
      } catch (IOException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
<<<<<<< HEAD
    } else {
      scanBtn.setSelected(true);
      newsParent.getItems().add("Saving scan results. Please wait.");
      ScrapeNews.cancelScan();
=======
//        Platform.runLater(() -> automationIndicator.setVisible(false));
//      });
      var serv = Executors.newSingleThreadScheduledExecutor();
      serv.scheduleAtFixedRate(() -> {
        if (ScrapeNews.service.isTerminated()) {
          finishScanTest();
          serv.shutdownNow();

        }
      }, 0, 1, TimeUnit.SECONDS);
    } else {
      scraper.cancelScan();
      finishScanTest();
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
    }
  }

  public void finishScanTest() {
    automationIndicator.setVisible(false);
    scanBtn.setSelected(false);
<<<<<<< HEAD
    investmentOpTxt.clear();
    newsParent.getItems().add("=========== Scan Finished ===========");
    Toolkit.getDefaultToolkit().beep();
  }

  public void finishEvaluation() {
    automationIndicator.setVisible(false);
    evaluateBtn.setSelected(false);
    Toolkit.getDefaultToolkit().beep();
  }


  public void finishBackTest() {
    automationIndicator.setVisible(false);
    backtestBtn.setSelected(false);
=======
    testBtn.setDisable(false);
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
    Toolkit.getDefaultToolkit().beep();
  }

  @FXML
<<<<<<< HEAD
  void onActionAllowFractional(ActionEvent event) {
=======
  void onActionAutoAddCheck(ActionEvent event) {
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4

  }

  @FXML
  void onActionAutoSellCheck(ActionEvent event) {

  }

  @FXML
  void onActionBuyBtn(ActionEvent event) {
    checkSubmit();
  }


  @FXML
  void onActionOnActionBuyCheck(ActionEvent event) {

  }


  @FXML
  void onActionSellBtn(ActionEvent event) {
    checkSubmit();
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
<<<<<<< HEAD
        order = AlpacaWrapper.getAPI()
            .requestNewFractionalMarketOrder(tickerSymbolCbox.getValue().getSymbol(),
                Double.parseDouble(quantityField.getText()), orderSide);
      } else {
        order = AlpacaWrapper.getAPI()
            .requestNewMarketOrder(tickerSymbolCbox.getValue().getSymbol(),
                Integer.parseInt(quantityField.getText()), orderSide,
                OrderTimeInForce.IOC);
=======
        order = alpacaAPI.requestNewFractionalMarketOrder(tickerSymbolCbox.getValue().getSymbol(),
            Double.parseDouble(quantityField.getText()), orderSide);
      } else {
        order = alpacaAPI.requestNewMarketOrder(tickerSymbolCbox.getValue().getSymbol(),
            Integer.parseInt(quantityField.getText()), orderSide,
            OrderTimeInForce.IOC);
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
      }
      estimatedPriceLbl.setText("Estimated Price: $0.00");
    } catch (AlpacaAPIRequestException e) {
      estimatedPriceLbl.setText("Order Failed");
    } finally {
      buyBtn.setSelected(false);
      sellBtn.setSelected(false);
      quantityField.clear();
      submitBtn.setDisable(true);
<<<<<<< HEAD
//      updateOrdersTable();
    }
  }

  @FXML
  void onKeyReleasedInvestment(KeyEvent event) {
    if (scanBtn.isSelected()) {
      evaluateBtn.setDisable(true);
      return;
    }
    evaluateBtn.setDisable(investmentOpTxt.getText().isEmpty());
  }

  @FXML
  void onActionEvaluateBtn(ActionEvent event) {
    if (!Files.exists(Path.of("data/news/news_model.net"))) {
      newsParent.getItems().add("You must have a trained model to use this function. Go to Automation/Testing to train a model.");
      evaluateBtn.setSelected(false);
      return;
    }
    autoConfirmBtn.setDisable(true);
    buyLimitPercentTxt.setDisable(false);
    if (!evaluateBtn.isSelected()) {
      evaluateBtn.setSelected(true);
      return;
    }
    if (investmentOpTxt.getText() == null) {
      return;
    }
    if (scanBtn.isSelected()) {
      ScrapeNews.cancelScan();
    }
    automationIndicator.setVisible(true);
=======
      updateOrdersTable();
    }
  }

  @FXML
  void onActionTestBtn(ActionEvent event) {
    automationIndicator.setVisible(true);
    testBtn.setDisable(true);
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
    service.submit(() -> {
      try {
        var list = TestNews.main(newsParent, this).stream()
            .filter((a) -> usEquities.keySet().contains(a.getSymbol()))
            .collect(Collectors.toList());
<<<<<<< HEAD
//        Platform.runLater(() -> {
        watchlist.clear();
        watchlist.addAll(list);
        finishEvaluation();
//        });
        priceWatchlist();
        buyMap = TestNews.calculateBuys(newsParent, list,
            new double[]{Double.parseDouble(investmentOpTxt.getText())}, autoTypeChoice.getValue());
        updateWatchlistTable();
        if (buyMap.size() > 0) {
//          autoConfirmBtn.setDisable(false);
          buyLimitPercentTxt.setDisable(false);
        }

=======
        Platform.runLater(() -> {
          watchlist.clear();
          watchlist.addAll(list);
          testBtn.setDisable(false);
        });
        updateWatchlistTable();
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  @FXML
  void onActionTickerSymbolCbox(ActionEvent event) {

  }

  @FXML
  void onActionTicker() {
    if (tickerSymbolCbox.getValue() == null) {
      return;
    }
    try {
      currentSnapshot = null;
<<<<<<< HEAD
      currentSnapshot = AlpacaWrapper.getAPI().getSnapshot(tickerSymbolCbox.getValue().getSymbol());
=======
      currentSnapshot = alpacaAPI.getSnapshot(tickerSymbolCbox.getValue().getSymbol());
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
      currentStockPrice = currentSnapshot.getLatestTrade().getP();
    } catch (AlpacaAPIRequestException e) {
      e.printStackTrace();
    }
    if (tickerSymbolCbox.getSelectionModel().getSelectedItem() != null) {
      String selected = tickerSymbolCbox.getSelectionModel().getSelectedItem().getSymbol();
      if (selected != null) {
        tickerSymbolCbox.getEditor().setText(selected);
        setupAreaChart(selected);
      }
    }
    buyBtn.setSelected(false);
    buyBtn.setDisable(false);
    sellBtn.setSelected(false);
    sellBtn.setDisable(false);
    submitBtn.setDisable(true);
    quantityField.clear();
    quantityField.setDisable(false);
    estimatedPriceLbl.setText("Estimated Price: $0.00");
  }

  @FXML
  void onKeyReleasedTickerSymbolCbox(KeyEvent event) {
    if (!tickerSymbolCbox.getSelectionModel().isEmpty()) {
      tickerSymbolCbox.show();
    }
    if (event.getCode().isArrowKey()) {
      return;
    }
    if (event.getCode() == KeyCode.ENTER) {
      onActionTicker();
    }
    List<Asset> filtered = usEquities.values().stream()
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

    tickerSymbolCbox.getItems().clear();
    tickerSymbolCbox.getItems().addAll(filtered);
    if (tickerSymbolCbox.getItems().size() > 0) {
      tickerSymbolCbox.show();
    }
  }

  @FXML
<<<<<<< HEAD
  void onActionSellAllBtn(ActionEvent event) {
    sellAll();
  }

  @FXML
=======
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
  void onKeyTypedQuantityField(KeyEvent event) {
    checkSubmit();
    if (quantityField.getText().isEmpty()) {
      estimatedPriceLbl.setText("Estimated Price: $0.00");
      return;
    }
    if (tickerSymbolCbox.getValue() != null) {
      Double input = Double.parseDouble(quantityField.getText());
      DecimalFormat df = new DecimalFormat("###,##0.00");
      estimatedPriceLbl.setText("Estimated Price: $" + df.format(currentStockPrice * input));
    }
  }

  private void checkSubmit() {
    submitBtn.setDisable(
        (!buyBtn.isSelected() && !sellBtn.isSelected()) || quantityField.getText().isEmpty()
            || tickerSymbolCbox.getSelectionModel().getSelectedItem() == null);
  }

  private void checkClassify() {
    classifyBtn
        .setDisable(buyPercentTxt.getText().isEmpty() || buyStrongPercentTxt.getText().isEmpty());
  }

  private void checkTrain() {
    trainBtn.setDisable(
        minibatchTxt.getText().isEmpty() || epochTxt.getText().isEmpty() || testSizeTxt.getText()
            .isEmpty());
  }

  private void checkBacktest() {
    backtestBtn
<<<<<<< HEAD
        .setDisable(investmentTestTxt.getText().isEmpty()
            || testStartDatePicker.getValue() == null || testEndDatePicker.getValue() == null
            || testEndDatePicker.getValue().isBefore(testStartDatePicker.getValue()));
=======
        .setDisable(maxDailyTestTxt.getText().isEmpty() || maxDailyTestTxt.getText().isEmpty()
            || sellPercentTestTxt.getText().isEmpty() || sellStrongPercentTestTxt.getText()
            .isEmpty()
            || testStartDatePicker.getValue() == null || testEndDatePicker.getValue() == null);
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
  }

  @FXML
  void onKeyReleasedClassify() {
    checkClassify();
  }

  @FXML
  void onKeyReleasedTrain() {
    checkTrain();
  }

  @FXML
<<<<<<< HEAD
  void onActionDatePickerBacktest(ActionEvent event) {
    checkBacktest();
  }

  void sellAll() {
    newsParent.getItems().add("Submitting SELL orders. Please wait.");
    automationIndicator.setVisible(true);
    service.submit(() -> {
      for (var pos : positions) {
        try {
          AlpacaWrapper.getAPI()
              .requestNewTrailingStopPercentOrder(pos.getSymbol(), Integer.parseInt(pos.getQty()), OrderSide.SELL,
                  OrderTimeInForce.GTC, Double.parseDouble(trailingSLOpTxt.getText()), false);
        } catch (AlpacaAPIRequestException e) {
          e.printStackTrace();
        }
      }
      Platform.runLater(() -> {
        automationIndicator.setVisible(false);
        newsParent.getItems().add("Finished submitting orders.");
        updateOrdersTable();
        Toolkit.getDefaultToolkit().beep();
      });
    });
=======
  void onKeyReleasedBacktest() {
    checkBacktest();
  }

  @FXML
  void onActionDatePickerBacktest(ActionEvent event) {
    checkBacktest();
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
  }


  @FXML
<<<<<<< HEAD
  void onActionAutoConfirmBtn(ActionEvent event) {
    automationIndicator.setVisible(true);
    autoConfirmBtn.setDisable(true);
    newsParent.getItems().addAll("========== Submitting Orders ==========",
        "Please Wait...");
    service.submit(() -> {
      int counter = 0;
      for (var buy : buyMap.keySet()) {
        try {
          var price = AlpacaWrapper.getAPI().getLatestTrade(buy).getTrade().getP()
              * (1 + Double.parseDouble(buyLimitPercentTxt.getText()) / 100);
          AlpacaWrapper.getAPI()
              .requestNewLimitOrder(buy, buyMap.get(buy), OrderSide.BUY, OrderTimeInForce.OPG,
                  price,
                  false);
//          if (counter++ % (buyMap.size() / 50) == 1) {
//            Platform.runLater(
//                () -> {
//                  String current = newsParent.getItems().get(newsParent.getItems().size() - 1)
//                      .replaceFirst("[.]", "|");
//                  newsParent.getItems().set(newsParent.getItems().size() - 1, current);
////                  newsParent.getItems().set(newsParent.getItems().size() - 1,
////                      "Evaluating " + symbol + ": " + finalCounter + "/" + dailyStocks.get(date).size());
//                });
//          }
        } catch (AlpacaAPIRequestException e) {
          e.printStackTrace();
        }
      }
      Platform.runLater(() -> {
        newsParent.getItems().addAll("========== All Orders Submitted ==========");
        automationIndicator.setVisible(false);
      });
      updateOrdersTable();
    });
  }

  @FXML
  void onActionBacktestBtn(ActionEvent event) {
    if (!Files.exists(Path.of("data/news/news_model.net"))) {
      testNewsParent.getItems().add("You must have a trained model to use this function. Go to Automation/Testing to train a model.");
      backtestBtn.setSelected(false);
      return;
    }
    if (!backtestBtn.isSelected()) {
      backtestBtn.setSelected(false);
      return;
    }
    automationIndicator.setVisible(true);
    service.submit(() -> {
      if (fullRadioBtn.isSelected()) {
        TestNews.backTest(testNewsParent, this,
            testStartDatePicker.getValue(), testEndDatePicker.getValue(),
            Double.parseDouble(investmentTestTxt.getText()),
            Double.parseDouble(trailingSLTestTxt.getText()),
            typeTestChoice.getValue());
      } else {
        TestNews.backTest(testNewsParent, this,
            testStartDatePicker.getValue(), testEndDatePicker.getValue(),
            Double.parseDouble(investmentTestTxt.getText()),
            typeTestChoice.getValue());
      }
      Platform.runLater(() -> {
        automationIndicator.setVisible(false);
        backtestBtn.setSelected(false);
      });
    });
  }

  @FXML
  void onActionSellCheck(ActionEvent event) {
=======
  void OnActionBuyCheck(ActionEvent event) {

  }

  @FXML
  void onActionBacktestBtn(ActionEvent event) {
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4

  }

  @FXML
<<<<<<< HEAD
  void onKeyReleasedTrailingSL(KeyEvent event) {
    sellAllBtn.setDisable(trailingSLOpTxt.getText().isEmpty());
  }

  @FXML
  void onKeyReleasedBuyLimitTxt(KeyEvent event) {
    autoConfirmBtn.setDisable(buyLimitPercentTxt.getText().isEmpty());
=======
  void onActionSellCheck(ActionEvent event) {

>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
  }

  @FXML
  void onActionClassifyBtn(ActionEvent event) {
<<<<<<< HEAD
    testNewsParent.getItems().add("========== Starting Classification ==========");
    classifyBtn.setDisable(true);
    automationIndicator.setVisible(true);
    Executors.newSingleThreadExecutor().submit(() -> {
      int minLines = PrepareNews.classify(Double.parseDouble(buyPercentTxt.getText()),
          Double.parseDouble(buyStrongPercentTxt.getText()));
      Platform.runLater(() -> {
        testNewsParent.getItems()
            .addAll("========== Classification Complete ==========",
=======
    testNewsParent.getItems().add("Performing Classification");
    classifyBtn.setDisable(true);
    automationIndicator.setVisible(true);
    Executors.newSingleThreadExecutor().submit(() -> {
      int minLines = PrepareNews.classify(Double.parseDouble(buyPercentTxt.getText()) / 100 + 1,
          Double.parseDouble(buyStrongPercentTxt.getText()) / 100 + 1);
      Platform.runLater(() -> {
        testNewsParent.getItems()
            .addAll("----- Classification complete -----",
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
                "Buy @ " + buyPercentTxt.getText() + "%",
                "Buy Strong @ " + buyStrongPercentTxt.getText() + "%",
                "Max test size: " + minLines,
                "=============================================================",
<<<<<<< HEAD
                "========== Training ==========",
                "You must now train your model on the classified data.",
=======
                "Training: You must now train your model on the classified data.",
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
                "Enter a Minibatch size, an Epoch number and a Test Size.",
                "Minibatch size is the number of data elements that will be analyzed during each iteration.",
                "A minibatch size of 50 is a good place to start.",
                "If your test size is very large increasing this number can speed up the training process.",
                "Using a minibatch size that is too small or too large will adversely affect training performance.",
                "The number of Epochs is the amount of full learning cycles that will be performed on the entire data set.",
                "It is recommended to keep this in the range of 3-10 to avoid under or overfitting the data set.",
                "The bigger the Test Size the better but larger Test Sizes will require more time to compute.",
                "=============================================================");
        testSizeTxt.setText(Integer.toString(minLines));
        buyPercentTxt.clear();
        buyStrongPercentTxt.clear();
        automationIndicator.setVisible(false);
      });
    });
  }

  @FXML
  void onActionTrainBtn(ActionEvent event) {
<<<<<<< HEAD
    if (!trainBtn.isSelected()) {
      trainBtn.setSelected(false);
      return;
    }
=======
    trainBtn.setDisable(true);
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
    automationIndicator.setVisible(true);
    Executors.newSingleThreadExecutor().submit(() -> {
      TrainNews.train((int) Double.parseDouble(minibatchTxt.getText()),
          (int) Double.parseDouble(epochTxt.getText()),
<<<<<<< HEAD
          (int) Double.parseDouble(testSizeTxt.getText()), testNewsParent, this);
      Platform.runLater(() -> {
        automationIndicator.setVisible(false);
        trainBtn.setSelected(false);
      });
    });
=======
          (int) Double.parseDouble(testSizeTxt.getText()), testNewsParent);
      Platform.runLater(() -> {
        automationIndicator.setVisible(false);
        testNewsParent.getItems().add(
            "You now have a trained model on file and are free to backtest and use operational functions.");
      });
    });
//    minibatchTxt.clear();
//    epochTxt.clear();
//    testSizeTxt.clear();
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
  }

  @FXML
  void onActionWatchCheck(ActionEvent event) {

  }

  @FXML
<<<<<<< HEAD
  void onActionCancelAllBtn(ActionEvent event) {
    try {
      AlpacaWrapper.getAPI().cancelAllOrders();
      updateOrdersTable();
      Toolkit.getDefaultToolkit().beep();
=======
  void onActionWatchBtn(ActionEvent event) {
    try {
      alpacaAPI.addWatchlistAsset(alpacaAPI.getWatchlists().get(0).getId(),
          tickerSymbolCbox.getSelectionModel().getSelectedItem().getSymbol());
      updateWatchlistTable();
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
    } catch (AlpacaAPIRequestException e) {
      e.printStackTrace();
    }
  }


  @FXML
  void onMouseReleasedChartScale(MouseEvent event) {
    if (tickerSymbolCbox.getValue() == null) {
      return;
    }
    lineChartLimit = (int) chartScale.getValue();
    setupAreaChart(tickerSymbolCbox.getValue().getSymbol());
  }

  @FXML
  void onKeyReleasedBacktest(KeyEvent event) {
<<<<<<< HEAD
    checkBacktest();
=======

>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
  }

  @FXML
  void onKeyReleasedClassify(KeyEvent event) {
    checkClassify();
  }

  @FXML
  void onKeyReleasedTrain(KeyEvent event) {
    checkTrain();
  }

<<<<<<< HEAD
  public ToggleButton getTrainBtn() {
    return trainBtn;
  }

  public ToggleButton getBackTestBtm() {
    return backtestBtn;
  }

=======
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
  @FXML
  private TableView<Position> portfolioTable;

  @FXML
  private TableColumn<Position, String> portfolioSymbolCol;

  @FXML
  private TableColumn<Position, Double> portfolioPositionCol;

  @FXML
  private TableColumn<Position, Number> portfolioValueCol;

  @FXML
  private TableColumn<Position, Number> portfolioPandLCol;

  @FXML
  private TableColumn<Position, Number> portfolioPercentCol;

  @FXML
  private TableView<WatchlistEntry> watchlistTable;

  @FXML
  private TableColumn<WatchlistEntry, String> watchlistSymbolCol;

  @FXML
  private TableColumn<WatchlistEntry, String> watchlistRatingCol;

  @FXML
  private TableColumn<WatchlistEntry, Double> watchlistValueCol;

  @FXML
  private TableColumn<WatchlistEntry, String> watchListNewsCol;

  @FXML
  private TableColumn<WatchlistEntry, Double> watchlistChangeCol;

  @FXML
  private TableColumn<WatchlistEntry, Double> watchlistPercentCol;


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

<<<<<<< HEAD
=======

>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4
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
<<<<<<< HEAD
  private ToggleButton evaluateBtn;

  @FXML
  private ChoiceBox<BuyType> autoTypeChoice;
=======
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
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4

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

  @FXML
<<<<<<< HEAD
  private TextField investmentTestTxt;

  @FXML
  private ChoiceBox<BuyType> typeTestChoice;

  @FXML
  private TextField trailingSLOpTxt;

  @FXML
  private Button sellAllBtn;

  @FXML
  private TextField trailingSLTestTxt;
=======
  private TextField maxBuyTxt;

  @FXML
  private TextField maxDailyTxt;

  @FXML
  private TextField sellPercentTxt;

  @FXML
  private TextField sellStrongPercentTxt;
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4

  @FXML
  private CheckBox autoWatchCheck;

  @FXML
  private TextField buyPercentTxt;

  @FXML
  private TextField buyStrongPercentTxt;

  @FXML
  private Button classifyBtn;

  @FXML
  private TextField minibatchTxt;

  @FXML
  private TextField epochTxt;

  @FXML
  private TextField testSizeTxt;

  @FXML
<<<<<<< HEAD
  private ToggleButton trainBtn;

  @FXML
  private TextField maxBuyTxt;

  @FXML
  private TextField investmentOpTxt;

  @FXML
  private TextField buyLimitPercentTxt;
=======
  private Button trainBtn;

  @FXML
  private TextField maxBuyTestTxt;

  @FXML
  private TextField maxDailyTestTxt;
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4

  @FXML
  private TextField sellPercentTestTxt;

  @FXML
  private TextField sellStrongPercentTestTxt;

  @FXML
  private DatePicker testStartDatePicker;

  @FXML
  private DatePicker testEndDatePicker;

  @FXML
<<<<<<< HEAD
  private ToggleButton backtestBtn;

  @FXML
  private RadioButton quickRadioBtn;

  @FXML
  private RadioButton fullRadioBtn;
=======
  private Button backtestBtn;
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4

  @FXML
  private ListView<String> testNewsParent;

  @FXML
  private ProgressIndicator automationIndicator;

  @FXML
<<<<<<< HEAD
  private Button autoConfirmBtn;
=======
  private Button watchBtn;
>>>>>>> 9ef15b5c9753d2e1f4a7087e8214b12a6689fba4

  @FXML
  private Slider chartScale;

  @FXML
  private ProgressIndicator backTestIndicator;

}
