import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Semaphore;

public class Airport {
  private static final int N_RUNWAYS = 1;
  private static final int N_GATES = 2;
  private static final int N_FUEL_TRUCKS = 1;
  private static final int N_PLANES = 6;

  public static void main(String[] args) {
    BlockingQueue<Runway> runways = new ArrayBlockingQueue<Runway>(N_RUNWAYS);
    BlockingQueue<Gate> gates = new ArrayBlockingQueue<Gate>(N_GATES);
    Semaphore fuelTruck = new Semaphore(N_FUEL_TRUCKS);

    ExecutorService runwayThreadpool = Executors.newFixedThreadPool(N_RUNWAYS + 1);

    Statistics stats = new Statistics(N_PLANES);

    System.out.println("[main thread] airport simulation started...\n");
    try {
      runways.put(new Runway());
      for (int i = 0; i < 2; i++) gates.put(new Gate(i + 1));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    for (int i = 0; i < 6; i++) {
      runwayThreadpool.submit(new Plane(i + 1, runways, gates, fuelTruck, stats));
    }

    runwayThreadpool.shutdown();

    try {
      if (runwayThreadpool.awaitTermination(500, TimeUnit.SECONDS)) {
        System.out.println("[main thread] airport simulation ended...\n");
        System.out.println("simulation statistics: ");
        System.out.println(
          String.format("longest waiting time: %.2f seconds [plane %d]",
            (double)(stats.getMaxWaitingTime() / 1000),
            stats.getMaxWaitingPlane()));
        System.out.println(
          String.format("shortest waiting time: %.2f seconds [plane %d]",
            (double)stats.getMinWaitingTime() / 1000,
            stats.getMinWaitingPlane()));
        System.out.println(
          String.format("average waiting time: %.2f seconds",
            (double)stats.getAvgWaitingTime() / 1000));
        System.out.println(String.format("planes served: %d", N_PLANES));
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
