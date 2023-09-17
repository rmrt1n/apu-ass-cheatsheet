package assignment.model.types;

public class VaccineCentre implements Type {
  private int id;
  private String name;
  private String location;
  private int capacity;
  private int isDeleted;

  public VaccineCentre(int id, String name, String location, int capacity, int isDeleted) {
    this.id = id;
    this.name = name;
    this.location = location;
    this.capacity = capacity;
    this.isDeleted = isDeleted;
  }

  public String toString() {
    return String.format("%d,%s,%s,%d,%d", id, name, location, capacity, isDeleted);
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public void setIsDeleted(int isDeleted) {
    this.isDeleted = isDeleted;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getLocation() {
    return location;
  }

  public int getCapacity() {
    return capacity;
  }

  public int getIsDeleted() {
    return isDeleted;
  }
}
