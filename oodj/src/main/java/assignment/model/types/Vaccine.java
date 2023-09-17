package assignment.model.types;

public class Vaccine implements Type {
  private int id;
  private String name;
  private int dose;
  private int doseInterval;
  private int isDeleted;

  public Vaccine(int id, String name, int dose, int doseInterval, int isDeleted) {
    this.id = id;
    this.name = name;
    this.dose = dose;
    this.doseInterval = doseInterval;
    this.isDeleted = isDeleted;
  }

  public String toString() {
    return String.format("%d,%s,%d,%d,%d", id, name, dose, doseInterval, isDeleted);
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDose(int dose) {
    this.dose = dose;
  }

  public void setDoseInterval(int doseInterval) {
    this.doseInterval = doseInterval;
  }

  public void setIsDeleted(int d) {
    this.isDeleted = d;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getDose() {
    return dose;
  }

  public int getDoseInterval() {
    return doseInterval;
  }

  public int getIsDeleted() {
    return isDeleted;
  }
}
