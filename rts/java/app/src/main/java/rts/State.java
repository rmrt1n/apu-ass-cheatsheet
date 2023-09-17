package rts;

public class State {
  // average values
  private int distance = 1000; // distance to destination
  private int altitude = 11000;
  private int pressure = 540;
  private int speed = 50;
  private int weather = 10; // 0-10, bad to good
  private boolean isLanding = false;

  public State() {}

  public synchronized int getAltitude() {
    return altitude;
  }

  public synchronized int getPressure() {
    return pressure;
  }

  public synchronized int getSpeed() {
    return speed;
  }

  public synchronized int getWeather() {
    return weather;
  }

  public synchronized int getDistance() {
    return distance;
  }

  public synchronized boolean getIsLanding() {
    return isLanding;
  }

  public synchronized void set(String name, int data) {
    switch (name) {
      case Main.QUEUE_GPS:
        distance = data;
        break;
      case Main.QUEUE_ALTITUDE:
        altitude = data;
        break;
      case Main.QUEUE_SPEED:
        speed = data;
        break;
      case Main.QUEUE_PRESSURE:
        pressure = data;
        break;
      case Main.QUEUE_WEATHER:
        weather = data;
        break;
    }
  }

  public synchronized void setAltitude(int altitude) {
    this.altitude = altitude;
  }

  public synchronized void setPressure(int pressure) {
    this.pressure = pressure;
  }

  public synchronized void setSpeed(int speed) {
    this.speed = speed;
  }

  public synchronized void setWeatherCondition(int weather) {
    this.weather = weather;
  }

  public synchronized void setDistance(int distance) {
    this.distance = distance;
  }

  public synchronized void setIsLanding(boolean isLanding) {
    this.isLanding = isLanding;
  }
}
