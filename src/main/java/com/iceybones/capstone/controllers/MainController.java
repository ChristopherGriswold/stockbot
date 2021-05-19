package com.iceybones.capstone.controllers;

import com.iceybones.capstone.CleanNews;
import com.iceybones.capstone.ScrapeNews;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import net.jacobpeterson.abstracts.enums.SortDirection;
import net.jacobpeterson.abstracts.websocket.exception.WebsocketException;
import net.jacobpeterson.alpaca.AlpacaAPI;
import net.jacobpeterson.alpaca.enums.asset.AssetStatus;
import net.jacobpeterson.alpaca.enums.order.OrderSide;
import net.jacobpeterson.alpaca.enums.order.OrderStatus;
import net.jacobpeterson.alpaca.enums.order.OrderTimeInForce;
import net.jacobpeterson.alpaca.enums.order.OrderType;
import net.jacobpeterson.alpaca.rest.exception.AlpacaAPIRequestException;
import net.jacobpeterson.alpaca.websocket.broker.listener.AlpacaStreamListener;
import net.jacobpeterson.alpaca.websocket.broker.listener.AlpacaStreamListenerAdapter;
import net.jacobpeterson.alpaca.websocket.broker.message.AlpacaStreamMessageType;
import net.jacobpeterson.domain.alpaca.account.Account;
import net.jacobpeterson.domain.alpaca.asset.Asset;
import net.jacobpeterson.domain.alpaca.order.Order;
import net.jacobpeterson.domain.alpaca.position.Position;
import net.jacobpeterson.domain.alpaca.streaming.AlpacaStreamMessage;
import net.jacobpeterson.domain.alpaca.streaming.account.AccountUpdateMessage;
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
    runClock();
    updateCash();
    setupPortfolioTable();
    setupOrdersTable();
    setupWatchlistTable();
    tickerSymbolCbox.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
      @Override
      public ListCell<String> call(ListView<String> param) {
        ListCell cell = new ListCell<String>() {
          @Override
          public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            getListView().setPrefWidth(600);
            if (!empty) {
              setText(item);
            } else {
              setText(null);
            }
          }
        };
        return cell;
      }
    });
    try {
      // List to account updates and trade updates from Alpaca and print their messages out
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
              updateOrdersTable();
              break;
          }
        }
      };

      // Add the 'AlpacaStreamListener'
      // Note that when the first 'AlpacaStreamListener' is added, the Websocket
      // connection is created.
      alpacaAPI.addAlpacaStreamListener(alpacaStreamListener);

    } catch (WebsocketException exception) {
      exception.printStackTrace();
    }
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
      if (!cellData.getValue().getFilledAvgPrice().equals("null")) {
        return "$" + cellData.getValue().getFilledAvgPrice();
      } else {
        return "";
      }
    }));
    ordersNotionalCol.setCellValueFactory(cellData -> Bindings.createStringBinding(() ->
    {
      if (!cellData.getValue().getNotional().isEmpty()) {
        return "$" + cellData.getValue().getNotional();
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
    watchlistTable.setItems(watchlist);
    updateWatchlistTable();
  }

  private void updateWatchlistTable() {
    try {
      watchlistId = alpacaAPI.getWatchlists().get(0).getId();
    } catch (AlpacaAPIRequestException e) {
      e.printStackTrace();
    }
    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
      try {
        Watchlist wach = alpacaAPI.getWatchlist(watchlistId);
        watchlist.setAll(wach.getAssets());
      } catch (AlpacaAPIRequestException e) {
        e.printStackTrace();
      }
    }, 0, 1, TimeUnit.SECONDS);
  }

  private void setLineChart() {
//    alpaca.
//    XYChart.Series<LocalDateTime, Double> series = new Series<>();
//    series.getData().add(new XYChart.Data<>("Jan", jan));
//    series.getData().add(new XYChart.Data<>("Feb", feb));
//    series.getData().add(new XYChart.Data<>("Mar", mar));
//    series.getData().add(new XYChart.Data<>("Apr", apr));
//    series.getData().add(new XYChart.Data<>("May", may));
//    series.getData().add(new XYChart.Data<>("Jun", jun));
//    series.getData().add(new XYChart.Data<>("Jul", jul));
//    series.getData().add(new XYChart.Data<>("Aug", aug));
//    series.getData().add(new XYChart.Data<>("Sep", sep));
//    series.getData().add(new XYChart.Data<>("Oct", oct));
//    series.getData().add(new XYChart.Data<>("Nov", nov));
//    series.getData().add(new XYChart.Data<>("Dec", dec));
//    series.setName(type);
//    monthTypeChart.getData().add(series);
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
  void onActionAllowFractionalCheck(ActionEvent event) {

  }

  @FXML
  void onActionAutoAddCheck(ActionEvent event) {

  }

  @FXML
  void onActionAutoSellCheck(ActionEvent event) {

  }

  @FXML
  void onActionBuyBtn(ActionEvent event) {

  }

  @FXML
  void onActionMaxBidTxt(ActionEvent event) {

  }

  @FXML
  void onActionOnActionBuyCheck(ActionEvent event) {

  }

  @FXML
  void onActionOrderTypeCbox(ActionEvent event) {

  }

  @FXML
  void onActionQuantityCbox(ActionEvent event) {

  }

  @FXML
  void onActionSellBtn(ActionEvent event) {

  }

  @FXML
  void onActionSellLagTxt(ActionEvent event) {

  }

  @FXML
  void onActionSubmitBtn(ActionEvent event) {
    OrderSide orderSide = OrderSide.BUY;
    if (sellBtn.isSelected()) {
      orderSide = OrderSide.SELL;
    }
    OrderType orderType = orderTypeCbox.getValue();
    OrderTimeInForce orderTimeInForce = timeInForceCbox.getValue();
    switch (orderType) {
      case LIMIT:
        break;
      case MARKET:
        break;
      case STOP:
        break;
      case STOP_LIMIT:
        break;
      case TRAILING_STOP:
        break;
    }
  }

  @FXML
  void onActionTestBtn(ActionEvent event) {

  }

  @FXML
  void onActionTickerSymbolCbox(ActionEvent event) {
    Platform.runLater(() -> {
      String selected = tickerSymbolCbox.getSelectionModel().getSelectedItem();
      if (selected != null) {
        tickerSymbolCbox.getEditor().setText(selected.split(" - ")[0]);
      }
    });
  }

  @FXML
  void onKeyReleasedTickerSymbolCbox() {
    if (!tickerSymbolCbox.getSelectionModel().isEmpty()) {
      tickerSymbolCbox.show();
      return;
    }
    List<String> symbols = usEquities.stream()
        .filter((a) -> a.getSymbol().toLowerCase()
            .contains(tickerSymbolCbox.getEditor().getText().toLowerCase()) || a.getName()
            .toLowerCase().contains(tickerSymbolCbox.getEditor().getText().toLowerCase()))
        .map((a) -> a.getSymbol() + " - " + a.getName() + " - " + a.getExchange())
        .collect(Collectors.toList());
    Platform.runLater(() -> {
      tickerSymbolCbox.getItems().clear();
      tickerSymbolCbox.getItems().addAll(symbols);
      if (tickerSymbolCbox.getItems().size() > 0) {
        tickerSymbolCbox.show();
      }
    });
  }

  @FXML
  void onActionTimeInForceCbox(ActionEvent event) {

  }

  @FXML
  private TableView<Position> portfolioTable;

  @FXML
  private TableColumn<?, ?> portfolioSymbolCol;

  @FXML
  private TableColumn<?, ?> portfolioPositionCol;

  @FXML
  private TableColumn<Position, String> portfolioValueCol;

  @FXML
  private TableColumn<Position, String> portfolioPandLCol;

  @FXML
  private TableColumn<Position, String> portfolioPercentCol;

  @FXML
  private TableView<Asset> watchlistTable;

  @FXML
  private TableColumn<?, ?> watchlistSymbolCol;

  @FXML
  private TableColumn<?, ?> watchlistRatingCol;

  @FXML
  private TableColumn<?, ?> watchlistValueCol;

  @FXML
  private TableColumn<?, ?> watchlistChangeCol;

  @FXML
  private TableColumn<?, ?> watchlistPercentCol;

  @FXML
  private ComboBox<String> tickerSymbolCbox;

  @FXML
  private LineChart<?, ?> lineChart;

  @FXML
  private ToggleButton buyBtn;

  @FXML
  private ToggleGroup buySellGroup;

  @FXML
  private ToggleButton sellBtn;

  @FXML
  private ComboBox<Double> quantityCbox;

  @FXML
  private ComboBox<OrderType> orderTypeCbox;

  @FXML
  private ComboBox<OrderTimeInForce> timeInForceCbox;

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
  private TableColumn<Order, String> ordersNotionalCol;

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

}
