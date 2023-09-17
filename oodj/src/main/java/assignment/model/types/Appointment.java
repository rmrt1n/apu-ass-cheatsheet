package assignment.model.types;

public class Appointment implements Type {
  public static final String APPT_ONGOING = "APPT_ONGOING";
  public static final String APPT_CANCELLED = "APPT_CANCELLED";
  public static final String APPT_FINISHED = "APPT_FINISHED";

  private int id;
  private User user;
  private Vaccination vaccination;
  private String status;

  public Appointment(int id, User user, Vaccination vaccination, String status) {
    this.id = id;
    this.user = user;
    this.vaccination = vaccination;
    this.status = status;
  }

  public String toString() {
    return String.format("%d,%d,%d,%s", id, user.getId(), vaccination.getId(), status.toString());
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void setVaccination(Vaccination vaccination) {
    this.vaccination = vaccination;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public int getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public Vaccination getVaccination() {
    return vaccination;
  }

  public String getStatus() {
    return status;
  }
}
