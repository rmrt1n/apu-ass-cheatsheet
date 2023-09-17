package assignment.view.user;

import java.awt.CardLayout;
import javax.swing.JPanel;

public class User extends JPanel {
  private NavigationPane nav;
  private CardLayout card;
  private ProfileManagement profile;
  private AppointmentManagement appt;
  private VacStatus status;

  private static final String PROFILE = "profile";
  private static final String APPOINTMENT = "appt";
  private static final String STATUS = "status";

  public User() {
    nav = new NavigationPane();
    card = new CardLayout();

    profile = new ProfileManagement();
    appt = new AppointmentManagement();
    status = new VacStatus();

    nav.getContentPanel().setLayout(card);
    nav.getContentPanel().add(profile, PROFILE);
    nav.getContentPanel().add(appt, APPOINTMENT);
    nav.getContentPanel().add(status, STATUS);

    setLayout(new CardLayout());
    add(nav);
  }

  public NavigationPane getNavigationPane() {
    return nav;
  }

  public ProfileManagement getProfileManagement() {
    return profile;
  }

  public AppointmentManagement getAppointmentManagement() {
    return appt;
  }

  public VacStatus getVacStatus() {
    return status;
  }

  public void setViewProfile() {
    card.show(nav.getContentPanel(), PROFILE);
  }

  public void setViewAppointment() {
    card.show(nav.getContentPanel(), APPOINTMENT);
  }

  public void setViewVacStatus() {
    card.show(nav.getContentPanel(), STATUS);
  }
}
