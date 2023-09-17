package rts;

import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class Main {
  // queue names
  public static final String QUEUE_GPS = "gps";
  public static final String QUEUE_ALTITUDE = "altitude";
  public static final String QUEUE_SPEED = "speed";
  public static final String QUEUE_PRESSURE = "pressure";
  public static final String QUEUE_WEATHER = "weather";
  // for colored terminal output
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_RESET = "\u001B[0m";

  public static void main(String[] args) {
    State state = new State();
    ConnectionFactory factory = new ConnectionFactory();
    Random rand = new Random();

    Sensor g = new Sensor(QUEUE_GPS, state, factory, () -> {
      // stable 50 km every hour (2 seconds)
      return state.getDistance() - 50;
    });
    Sensor a = new Sensor(QUEUE_ALTITUDE, state, factory, () -> {
      // fluctuations in altitude
      return state.getAltitude()
          + rand.nextInt(200) * (rand.nextBoolean() ? 1 : -1);
    });
    Sensor s = new Sensor(QUEUE_SPEED, state, factory, () -> {
      // harmless tiny fluctuations in speed
      return state.getSpeed() + rand.nextInt(5) * (rand.nextBoolean() ? 1 : -1);
    });
    Sensor p = new Sensor(QUEUE_PRESSURE, state, factory, () -> {
      // simulate sudden pressure drop
      if (state.getDistance() == 700)
        return 300;
      // harmless tiny fluctuations in cabin pressure
      return state.getPressure()
          + rand.nextInt(10) * (rand.nextBoolean() ? 1 : -1);
    });
    Sensor w = new Sensor(QUEUE_WEATHER, state, factory, () -> {
      // random weather condition
      return rand.nextInt(10);
    });

    ExecutorService executor = Executors.newFixedThreadPool(5);

    executor.execute(g);
    executor.execute(a);
    executor.execute(s);
    executor.execute(p);
    executor.execute(w);

    System.out.println("simulation started");
    FC fc = new FC(state, factory);
    try {
      fc.run();
    } catch (TimeoutException | IOException e) {
      System.err.println(e.getMessage());
    }

    executor.shutdown();
    while (!executor.isTerminated()) {
    }
    System.out.println("simulation completed");
  }
}
