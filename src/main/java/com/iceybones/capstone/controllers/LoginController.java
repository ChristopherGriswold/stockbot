package com.iceybones.capstone.controllers;

import com.iceybones.capstone.app.ApplicationManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import net.jacobpeterson.alpaca.enums.api.EndpointAPIType;

public class LoginController implements Initializable {
  public static String key;
  public static String secret;
  public static EndpointAPIType ep_type;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

  }

  @FXML
  private TextField apiKeyTxt;

  @FXML
  private TextField secretKeyTxt;

  @FXML
  private Button loginBtn;

  @FXML
  private TextField usernameTxt;
  @FXML
  private PasswordField passwordTxt;
  @FXML
  private ProgressBar loginProgress;
  @FXML
  private CheckBox paperAccountCheck;

  @FXML
  void onActionLogin(ActionEvent event) {
    loginProgress.setVisible(true);
    loginBtn.setDisable(true);
    key = apiKeyTxt.getText();
    secret = secretKeyTxt.getText();
    if (paperAccountCheck.isSelected()) {
      ep_type = EndpointAPIType.PAPER;
    } else {
      ep_type = EndpointAPIType.LIVE;
    }
//    AlpacaAPI api = new AlpacaAPI(key, secret, ep_type, DataAPIType.SIP);
//    try {
//      api.getAccount();
//    } catch (AlpacaAPIRequestException e) {
//      loginProgress.setVisible(false);
//      loginBtn.setDisable(false);
//      return;
//    }
    ApplicationManager.login();
  }

}
