import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Plane implements Runnable {
	private int planeNumber;
	private BlockingQueue<Runway> runways;
  private BlockingQueue<Gate> gates;
	private Runway runway;
	private Gate gate;
	private Semaphore truck;
	private Statistics stats;
	private long waitingTime;

  public Plane(int planeNumber,
               BlockingQueue<Runway> runways,
               BlockingQueue<Gate> gates,
							 Semaphore fuelTruck,
							 Statistics stats) {
		this.planeNumber = planeNumber;
    this.runways = runways;
		this.truck = fuelTruck;
    this.gates = gates;
		this.stats = stats;

		arriveAndRequestForLanding();
  }

	@Override
	public void run() {
		landAndHeadToGate();
		passengersDisembark();
		refillAndCleanPlane();
		refuel();
		passengersEmbark();
		requestForTakeOffAndTakeOff();
		// send stats to the Statistics class
		synchronized (stats) {
			stats.addWaitingTime(planeNumber, waitingTime);
		}
	}

	private void arriveAndRequestForLanding() {
		Random random = new Random();
		try {
			Thread.sleep(random.nextInt(3000));
			System.out.println(
				String.format("[plane %d] arrives at airport & requests for landing",
											planeNumber));
			// get time when arrive at airport
			waitingTime = System.currentTimeMillis();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void landAndHeadToGate() {
		try {
			// get runway & gate
			runway = runways.take();
			gate = gates.take();
			runways.put(runway);

			// get time difference of now and when first arrived
			waitingTime = System.currentTimeMillis() - waitingTime;

			System.out.println(
				String.format("[plane %d] lands on runway & docks at gate %d",
											planeNumber,
											gate.getGateNumber()));
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void passengersDisembark() {
		try {
			System.out.println(
				String.format("[plane %d] passengers gets off plane to gate %d",
											planeNumber,
											gate.getGateNumber()));
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void refillAndCleanPlane() {
		try {
			System.out.println(
				String.format("[plane %d] staff cleans plane & refills supplies",
											planeNumber));
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void refuel() {
		try {
			// get semaphore here
			truck.acquire();
			System.out.println(
				String.format("[plane %d] refuel truck comes & refuels plane",
											planeNumber));
			Thread.sleep(5000);
			System.out.println(
				String.format("[plane %d] refuel truck finishes refuelling plane",
											planeNumber));
			truck.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void passengersEmbark() {
		try {
			System.out.println(
				String.format("[plane %d] passengers gets on plane from gate %d",
											planeNumber,
											gate.getGateNumber()));
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void requestForTakeOffAndTakeOff() {
		try {
			System.out.println(
				String.format("[plane %d] requests for take-off",
											planeNumber));
			Thread.sleep(3000);
			// same thing as before
			runway = runways.take();
			gates.put(gate);
			runways.put(runway);

			System.out.println(
				String.format("[plane %d] takes-off from the runway",
											planeNumber));
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
