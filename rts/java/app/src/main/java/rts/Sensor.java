package rts;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.lang.InterruptedException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeoutException;

public class Sensor implements Runnable {
  private ConnectionFactory factory;
  private State state;
  private String name;
  private DataGenerator generator;

  public Sensor(String name, State state, ConnectionFactory factory,
      DataGenerator generator) {
    this.name = name;
    this.state = state;
    this.factory = factory;
    this.generator = generator;
  }

  @Override
  public void run() {
    try (Connection con = factory.newConnection()) {
      Channel ch = con.createChannel();
      ch.queueDeclare(name, false, false, false, null);

      try {
        while (state.getDistance() > 0) {
          int data = generator.generate();
          state.set(name, data);
          ch.basicPublish("", name, null, String.valueOf(data).getBytes());
          System.out.printf(
              "%s127.0.0.1 sensor %s [%s] \"measured value of %d%s\"%s\n",
              Main.ANSI_GREEN, rfill(name), getISODateTime(), data, getUnit(),
              Main.ANSI_RESET);
          Thread.sleep(2000);
        }
      } catch (InterruptedException e) {
        System.err.println(e.getMessage());
      }
      System.out.printf("127.0.0.1 sensor %s [%s] \"closing connection\"\n",
          name, getISODateTime());
    } catch (TimeoutException | IOException e) {
      System.err.println(e.getMessage());
    }
  }

  private String getISODateTime() {
    LocalDateTime now = LocalDateTime.now();
    String dt = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    return dt.substring(0, dt.lastIndexOf('.'));
  }

  private String rfill(String s) {
    return s + " ".repeat(8 - s.length());
  }

  private String getUnit() {
    switch (name) {
      case Main.QUEUE_GPS:
        return " km";
      case Main.QUEUE_ALTITUDE:
        return " m";
      case Main.QUEUE_SPEED:
        return " km/h";
      case Main.QUEUE_PRESSURE:
        return " pa";
      case Main.QUEUE_WEATHER:
        return "";
    }
    return "";
  }
}
