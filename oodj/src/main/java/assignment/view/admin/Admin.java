package assignment.view.admin;

import java.awt.CardLayout;

import javax.swing.JPanel;

public class Admin extends JPanel {
  private NavigationPane nav;
  private CardLayout card;

  private UserManagement user;
  private AdminManagement admin;
  private VaccineManagement vaccine;
  private VaccineCentreManagement vacCentre;
  private VaccinationManagement vaccination;
  private AppointmentManagement appt;
  private Report report;

  private static final String USER = "user";
  private static final String ADMIN = "admin";
  private static final String VACCINE = "vaccine";
  private static final String VAC_CENTRE = "vacCentre";
  private static final String VACCINATION = "vaccination";
  private static final String APPOINTMENT = "appointment";
  private static final String REPORT = "report";

  public Admin() {
    // Card Layout
    // https://stackoverflow.com/questions/34113406/implementing-cardlayout-within-a-jframe-and-switching-cards-based-on-specific-bu
    nav = new NavigationPane();
    card = new CardLayout();

    user = new UserManagement();
    admin = new AdminManagement();
    vaccine = new VaccineManagement();
    vacCentre= new VaccineCentreManagement();
    vaccination = new VaccinationManagement();
    appt = new AppointmentManagement();
    report = new Report();

    nav.getContentPanel().setLayout(card);
    nav.getContentPanel().add(user, USER);
    nav.getContentPanel().add(admin, ADMIN);
    nav.getContentPanel().add(vaccine, VACCINE);
    nav.getContentPanel().add(vacCentre, VAC_CENTRE);
    nav.getContentPanel().add(vaccination, VACCINATION);
    nav.getContentPanel().add(appt, APPOINTMENT);
    nav.getContentPanel().add(report, REPORT);

    setLayout(new CardLayout());
    add(nav);
  }

  public void setViewUser() {
    card.show(nav.getContentPanel(), USER);
  }

  public void setViewAdmin() {
    card.show(nav.getContentPanel(), ADMIN);
  }

  public void setViewVaccine() {
    card.show(nav.getContentPanel(),VACCINE);
  }

  public void setViewVacCentre() {
    card.show(nav.getContentPanel(), VAC_CENTRE);
  }

  public void setViewVaccination() {
    card.show(nav.getContentPanel(), VACCINATION);
  }
  
  public void setViewAppointment() {
    card.show(nav.getContentPanel(), APPOINTMENT);
  }

  public void setViewReport() {
    card.show(nav.getContentPanel(), REPORT);
  }
  
  public UserManagement getUserManagement() {
    return user;
  }

  public AdminManagement getAdminManagement() {
    return admin;
  }

  public VaccineManagement getVaccineManagement() {
    return vaccine;
  }

  public VaccineCentreManagement getVaccineCentreManagement() {
    return vacCentre;
  }

  public VaccinationManagement getVaccinationManagement() {
    return vaccination;
  }

  public AppointmentManagement getAppointmentManagement() {
    return appt;
  }

  public Report getReport() {
    return report;
  }

  public NavigationPane getNavigationPane() {
    return nav;
  }
}
