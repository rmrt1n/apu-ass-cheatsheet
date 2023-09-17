package assignment.controller.user;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.time.LocalDate;

import assignment.model.Model;
import assignment.model.models.VaccinationModel;
import assignment.model.types.*;
import assignment.view.View;

public class UserController {
  private Model model;
  private View view;

  public UserController(Model m, View v) {
    model = m;
    view = v;

    view.getUserPanel().getNavigationPane().addTabListeners(handleTabEvents());
    addVaccinations();
  }

  private MouseAdapter handleTabEvents() {
    return new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        switch (evt.getComponent().getName()) {
          case "manageProfileTab":
            view.getUserPanel().setViewProfile();
            break;
          case "manageAppointmentTab":
            view.getUserPanel().setViewAppointment();
            break;
          case "vaccinationStatusTab":
            view.getUserPanel().setViewVacStatus();
            break;
          case "logoutTab":
            view.logoutUser();
            break;
        }
      }

      //Hover effects
      public void mouseEntered(MouseEvent evt) {
        JPanel panel = (JPanel) evt.getSource();
        panel.setBackground(new Color(235, 235, 235));
      }

      public void mouseExited(MouseEvent evt) {
        JPanel panel = (JPanel) evt.getSource();
        panel.setBackground(new Color(245, 245, 245));
      }
    };
  }

  // Populate appointments page
  private void addVaccinations() {
    model.getVaccinationModel();
    ArrayList<Vaccination> vcn = VaccinationModel.getVaccinations();
    LocalDate today = LocalDate.now();

    for (int i = 0; i < vcn.size(); i++) {
      Vaccination vn = vcn.get(i);
      if (vn.getEndDate().toLocalDate().isBefore(today)) continue;

      VaccineCentre vc = vn.getVacCentre();
      Vaccine v = vn.getVaccine();
      view.getUserPanel()
          .getAppointmentManagement()
          .addVaccination(vn.getId(), vc.getName(), vc.getLocation(), vn.getStartDate(),
                          vn.getEndDate(), v.getName(), vn.getQuantity());
    }
  }
}
