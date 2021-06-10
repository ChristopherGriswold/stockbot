package com.iceybones.capstone.models;

import com.iceybones.capstone.controllers.LoginController;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.jacobpeterson.alpaca.AlpacaAPI;
import net.jacobpeterson.alpaca.enums.api.DataAPIType;

public class AlpacaWrapper {

  public static AlpacaAPI alpacaAPI;
  private static final AtomicInteger rateCounter = new AtomicInteger(0);
  private static final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

  static {
    alpacaAPI = new AlpacaAPI(LoginController.key, LoginController.secret, LoginController.ep_type, DataAPIType.SIP);
  }
  public static AlpacaAPI getAPI() {
    while (rateCounter.intValue() > 0) {

      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    rateCounter.incrementAndGet();
    service.schedule(() -> {
      if (rateCounter.intValue() > 0) {
        rateCounter.getAndDecrement();
      }
    }, 300, TimeUnit.MILLISECONDS);
//    System.out.println("ReturnAPI");
    return alpacaAPI;
  }
}
