package assignment.controller.user;

import java.util.ArrayList;

import assignment.model.Model;
import assignment.view.View;
import assignment.view.user.VacStatus;
import assignment.model.types.*;

public class UVacStatus {
  private Model model;
  private View view;
  private User user;
  private VacStatus status;

  public UVacStatus(Model m, View v, User u) {
    model = m;
    view = v;
    user = u;
    status = view.getUserPanel().getVacStatus();

    status.setUser(u.getName(), u.getIC());
    setTitle();
    setupCert();
  }

  // set vaccination status
  private void setTitle() {
    String s= user.getVacStatus();
    String str;

    switch (s) {
      case User.VAC_PARTIAL: str = "Partially Vaccinated"; break;
      case User.VAC_FULL: str = "Fully Vaccinated"; break;
      case User.VAC_UNVAC:
      default:
       str = "Not Vaccinated";
    }

    view.getUserPanel().getVacStatus().setVacStatus(str);
  }

  private int countDose(ArrayList<Appointment> a) {
    int count = 0;
    for (int i = 0; i < a.size(); i++) {
      if (a.get(i).getStatus().equals(Appointment.APPT_FINISHED)) count++;
    }
    return count;
  }

  // set vaccination history
  private void setupCert() {
    ArrayList<Appointment> a = model.getAppointmentModel().getAppointmentsByUser(user);
    int doses = countDose(a);

    // set null
    if (!(doses == 1 || doses == 2)) {
      status.setUnvac(0);
      return;
    } 

    // set vaccinated
    int count = 0;
    for (int i = 0; i < a.size(); i++) {
      Appointment appt = a.get(i);
      if (appt.getStatus().equals(Appointment.APPT_FINISHED)) {
        count++;
        Vaccination vn = appt.getVaccination();
        Vaccine v = vn.getVaccine();
        VaccineCentre vc = vn.getVacCentre();

        // set dose 1
        if (count == 1) {
          status.setDose1(vn.getStartDate(), vn.getEndDate(),
                          vc.getName(),vc.getLocation(), v.getName());
          status.setUnvac(1);
        } 
        // set dose 2
        if (count == 2) {
          status.setDose2(vn.getStartDate(), vn.getEndDate(),
                          vc.getName(),vc.getLocation(), v.getName());
        }
      }
    }
  }
}
