package assignment.model.types;

import java.time.LocalDateTime;

public class Vaccination implements Type {
  private int id;
  private Vaccine vaccine;
  private VaccineCentre vacCentre;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private int quantity;

  public Vaccination(
      int id,
      Vaccine vaccine,
      VaccineCentre vacCentre,
      int quantity,
      String startDate,
      String endDate
      ) {
    this.id = id;
    this.vaccine = vaccine;
    this.vacCentre = vacCentre;
    this.quantity = quantity;
    this.startDate = LocalDateTime.parse(startDate);
    this.endDate = LocalDateTime.parse(endDate);
  }

  public String toString() {
    return String.format("%d,%d,%d,%d,%s,%s",
      id, vaccine.getId(), vacCentre.getId(), quantity, startDate.toString(), endDate.toString());
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setVaccine(Vaccine vaccine) {
    this.vaccine = vaccine;
  }

  public void setVacCentre(VaccineCentre vacCentre) {
    this.vacCentre = vacCentre;
  }

  public void setStartDate(String startDate) {
    this.startDate = LocalDateTime.parse(startDate);
  }

  public void setEndDate(String endDate) {
    this.endDate = LocalDateTime.parse(endDate);
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public int getId() {
    return id;
  }

  public Vaccine getVaccine() {
    return vaccine;
  }

  public VaccineCentre getVacCentre() {
    return vacCentre;
  }

  public LocalDateTime getStartDate() {
    return startDate;
  }

  public LocalDateTime getEndDate() {
    return endDate;
  }

  public int getQuantity() {
    return quantity;
  }
}
