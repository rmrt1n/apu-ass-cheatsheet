public class Statistics {
  private long[] waitingTimes;
  
  public Statistics(int nPlanes) {
    waitingTimes = new long[nPlanes];
  }

  public void addWaitingTime(int planeNumber, long waitingTime) {
    waitingTimes[planeNumber - 1] = waitingTime;
  }

  public long getMaxWaitingTime() {
    long max = waitingTimes[0];
    for (int i = 1; i < waitingTimes.length; i++) {
      max = waitingTimes[i] > max ? waitingTimes[i] : max;
    }
    return max;
  }

  public int getMaxWaitingPlane() {
    long max = waitingTimes[0];
    int id = 0;
    for (int i = 1; i < waitingTimes.length; i++) {
      if (waitingTimes[i] > max) {
        max = waitingTimes[i];
        id = i;
      }
    }
    return id;
  }

  public long getMinWaitingTime() {
    long min = waitingTimes[0];
    for (int i = 1; i < waitingTimes.length; i++) {
      min = waitingTimes[i] < min ? waitingTimes[i] : min;
    }
    return min;
  }

  public int getMinWaitingPlane() {
    long min = waitingTimes[0];
    int id = 0;
    for (int i = 1; i < waitingTimes.length; i++) {
      if (waitingTimes[i] < min) {
        min = waitingTimes[i];
        id = i;
      }
    }
    return id;
  }

  public long getAvgWaitingTime() {
    long avg = 0;
    for (int i = 0; i < waitingTimes.length; i++) avg += waitingTimes[i];
    return avg / waitingTimes.length;
  }
}
