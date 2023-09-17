package assignment.controller.admin;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import assignment.model.Model;
import assignment.view.View;
import assignment.controller.ReportController;

public class AdminController {
  private Model model;
  private View view;

  private TUser um;
  private TAdmin am;
  private TVaccine vm;
  private TVacCentre vcm;
  private TVaccination vnm;
  private TAppointment apm;
  private ReportController report;

  public AdminController(Model m, View v) {
    model = m;
    view = v;

    um = new TUser(model, view);
    am = new TAdmin(model, view);
    vm = new TVaccine(model, view);
    vcm = new TVacCentre(model, view);
    vnm = new TVaccination(model, view);
    apm = new TAppointment(model, view);
    report = new ReportController(model, view);

    view.getAdminPanel().getNavigationPane().addTabListeners(handleTabEvents());
  }

  private MouseAdapter handleTabEvents() {
    return new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        switch (evt.getComponent().getName()) {
          case "manageUserTab":
            view.getAdminPanel().setViewUser();
            break;
          case "manageAdminTab":
            view.getAdminPanel().setViewAdmin();
            break;
          case "manageVaccineTab":
            view.getAdminPanel().setViewVaccine();
            break;
          case "manageVaccineCentreTab":
            view.getAdminPanel().setViewVacCentre();
            break;
          case "manageVaccinationTab":
            view.getAdminPanel().setViewVaccination();
            break;
          case "manageAppointmentTab":
            view.getAdminPanel().setViewAppointment();
            break;
          case "viewReportTab":
            report.regen();
            view.getAdminPanel().setViewReport();
            break;
          case "logoutTab":
            view.logoutAdmin();
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
}
