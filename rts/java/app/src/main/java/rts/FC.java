package rts;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class FC {
  private State state;
  private ConnectionFactory factory;

  public FC(State state, ConnectionFactory factory) {
    this.state = state;
    this.factory = factory;
  }

  public void run() throws IOException, TimeoutException {
    Connection con = factory.newConnection();

    Channel chGPS = con.createChannel();
    Channel chAltitude = con.createChannel();
    Channel chSpeed = con.createChannel();
    Channel chPressure = con.createChannel();
    Channel chWeather = con.createChannel();

    chGPS.queueDeclare(Main.QUEUE_GPS, false, false, false, null);
    chAltitude.queueDeclare(Main.QUEUE_ALTITUDE, false, false, false, null);
    chSpeed.queueDeclare(Main.QUEUE_SPEED, false, false, false, null);
    chPressure.queueDeclare(Main.QUEUE_PRESSURE, false, false, false, null);
    chWeather.queueDeclare(Main.QUEUE_WEATHER, false, false, false, null);

    ExecutorService executor = Executors.newFixedThreadPool(5);

    executor.execute(() -> handle(chGPS, Main.QUEUE_GPS));
    executor.execute(() -> handle(chAltitude, Main.QUEUE_ALTITUDE));
    executor.execute(() -> handle(chSpeed, Main.QUEUE_SPEED));
    executor.execute(() -> handle(chPressure, Main.QUEUE_PRESSURE));
    executor.execute(() -> handle(chWeather, Main.QUEUE_WEATHER));

    executor.shutdown();
    while (!executor.isTerminated()) {
    }

    chAltitude.close();
    chGPS.close();
    chSpeed.close();
    chPressure.close();
    chWeather.close();
    con.close();
  }

  private void handle(Channel ch, String queue) {
    try {
      while (state.getDistance() > 0) {
        DeliverCallback callback = null;
        switch (queue) {
          case Main.QUEUE_GPS:
            callback = handleGPS;
            break;
          case Main.QUEUE_ALTITUDE:
            callback = handleAltitude;
            break;
          case Main.QUEUE_SPEED:
            callback = handleSpeed;
            break;
          case Main.QUEUE_PRESSURE:
            callback = handlePressure;
            break;
          case Main.QUEUE_WEATHER:
            callback = handleWeather;
            break;
        }
        if (callback != null) {
          ch.basicConsume(queue, true, callback, consumerTag -> {});
        }
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  private String getISODateTime() {
    LocalDateTime now = LocalDateTime.now();
    String dt = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    return dt.substring(0, dt.lastIndexOf('.'));
  }

  private DeliverCallback handleGPS = new DeliverCallback() {
    @Override
    public void handle(String consumerTag, Delivery message)
        throws IOException {
      String msg = new String(message.getBody(), "UTF-8");
      int distance = Integer.parseInt(msg);
      // arbitrary distance value to start landing
      if (distance <= 250 && distance > 0 && !state.getIsLanding()) {
        System.out.printf(
            "%s127.0.0.1 fc landing-gear [%s] \"initiating aircraft landing\"%s\n",
            Main.ANSI_BLUE, getISODateTime(), Main.ANSI_RESET);
        state.setIsLanding(true);
      }
      if (distance == 0) {
        System.out.printf(
            "%s127.0.0.1 fc landing-gear [%s] \"aircraft has successfully landed\"%s\n",
            Main.ANSI_BLUE, getISODateTime(), Main.ANSI_RESET);
      }
    }
  };

  private DeliverCallback handleAltitude = new DeliverCallback() {
    @Override
    public void handle(String consumerTag, Delivery message)
        throws IOException {
      String msg = new String(message.getBody(), "UTF-8");
      int altitude = Integer.parseInt(msg);
      // arbitrary values for too high/too low altitude
      if (altitude > 11100) {
        System.out.printf(
            "%s127.0.0.1 fc wing-flaps   [%s] \"altitude too high, lowering wing flaps\"%s\n",
            Main.ANSI_BLUE, getISODateTime(), Main.ANSI_RESET);
        state.setAltitude(altitude - 100);
      }
      // if not going to land and altitude too low
      if (state.getDistance() > 250 && altitude < 10900) {
        System.out.printf(
            "%s127.0.0.1 fc wing-flaps   [%s] \"altitude too low, raising wing flaps\"%s\n",
            Main.ANSI_BLUE, getISODateTime(), Main.ANSI_RESET);
        state.setAltitude(altitude + 100);
      }
      // lower altitude for landing
      if (state.getIsLanding()) {
        System.out.printf(
            "%s127.0.0.1 fc wing-flaps   [%s] \"preparing for landing, lowering wing flaps\"%s\n",
            Main.ANSI_BLUE, getISODateTime(), Main.ANSI_RESET);
        state.setAltitude(state.getDistance() == 50 ? 0 : altitude / 2);
      }
    }
  };

  private DeliverCallback handleSpeed = new DeliverCallback() {
    @Override
    public void handle(String consumerTag, Delivery message)
        throws IOException {
      String msg = new String(message.getBody(), "UTF-8");
      int speed = Integer.parseInt(msg);
      // lower speed for landing
      if (state.getIsLanding()) {
        System.out.printf(
            "%s127.0.0.1 fc main-engine  [%s] \"preparing for landing, reducing speed\"%s\n",
            Main.ANSI_BLUE, getISODateTime(), Main.ANSI_RESET);
        state.setSpeed(state.getDistance() == 50 ? 0 : speed / 2);
      }
    }
  };

  private DeliverCallback handlePressure = new DeliverCallback() {
    @Override
    public void handle(String consumerTag, Delivery message)
        throws IOException {
      String msg = new String(message.getBody(), "UTF-8");
      int pressure = Integer.parseInt(msg);
      if (pressure < 500) {
        System.out.printf(
            "%s127.0.0.1 fc oxygen-mask  [%s] \"dangerously low cabin pressure, deploying oxygen masks\"%s\n",
            Main.ANSI_BLUE, getISODateTime(), Main.ANSI_RESET);
        state.setPressure(pressure + 75);
      }
    }
  };

  private DeliverCallback handleWeather = new DeliverCallback() {
    @Override
    public void handle(String consumerTag, Delivery message)
        throws IOException {
      String msg = new String(message.getBody(), "UTF-8");
      int weather = Integer.parseInt(msg);
      if (weather < 3) {
        System.out.printf(
            "%s127.0.0.1 fc main-engine  [%s] \"bad weather conditions, reducing speed\"%s\n",
            Main.ANSI_BLUE, getISODateTime(), Main.ANSI_RESET);
        state.setSpeed(state.getSpeed() - 5);
      }
    }
  };
}
