package assignment.model;

import java.util.ArrayList;
import java.time.LocalDateTime;
import assignment.model.models.*;
import assignment.model.types.*;

public class Model {
  private AdminModel adminModel;
  private UserModel userModel;
  private VaccineModel vaccineModel;
  private VaccineCentreModel vacCentreModel;
  private VaccinationModel vaccinationModel;
  private AppointmentModel appointmentModel;

  public Model() {
    adminModel = new AdminModel();
    userModel = new UserModel();
    vaccineModel = new VaccineModel();
    vacCentreModel = new VaccineCentreModel();
    vaccinationModel = new VaccinationModel();
    appointmentModel = new AppointmentModel();

    updateModel();
  }

  public void writeFile() {
    AdminModel.write();
    UserModel.write();
    VaccineModel.write();
    VaccineCentreModel.write();
    AppointmentModel.write();
    VaccinationModel.write();
  }

  public AdminModel getAdminModel() {
    return this.adminModel;
  }

  public UserModel getUserModel() {
    return this.userModel;
  }

  public VaccineModel getVaccineModel() {
    return this.vaccineModel;
  }

  public VaccineCentreModel getVaccineCentreModel() {
    return this.vacCentreModel;
  }

  public VaccinationModel getVaccinationModel() {
    return this.vaccinationModel;
  }

  public AppointmentModel getAppointmentModel() {
    return this.appointmentModel;
  }

  public void updateModel() {
    ArrayList<Appointment> appts = AppointmentModel.getAppointments();
    for (int i = 0; i < appts.size(); i++) {
      Appointment a = appts.get(i);
      Vaccination vn = a.getVaccination();
      if (LocalDateTime.now().isAfter(vn.getEndDate())) {
        if (a.getStatus().equals(Appointment.APPT_ONGOING)) {
          a.setStatus(Appointment.APPT_FINISHED);
          User u = a.getUser();
          Vaccine v = a.getVaccination().getVaccine();
          if (v.getDose() == 1) {
            u.setVacStatus(User.VAC_FULL);
            continue;
          }
          String status = u.getVacStatus();
          if (status.equals(User.VAC_UNVAC)) u.setVacStatus(User.VAC_PARTIAL);
          if (status.equals(User.VAC_PARTIAL)) u.setVacStatus(User.VAC_FULL);
        }
      }
    }
  }
}
