package assignment.controller.user;

import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

import assignment.model.Model;
import assignment.model.types.*;
import assignment.view.View;
import assignment.view.user.AppointmentManagement;
import assignment.view.user.VaccinationComponent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UAppointmentManagement {
  private Model model;
  private View view;
  private User user;
  private AppointmentManagement am;
  
  public UAppointmentManagement(Model m, View v, User u) {
    model = m;
    view = v;
    user = u;
    am = view.getUserPanel().getAppointmentManagement();

    editVaccinations();
  }

  private void editVaccinations() {
    ArrayList<VaccinationComponent> cards = am.getVaccinations();
    ArrayList<Appointment> appts = model.getAppointmentModel().getAppointmentsByUser(user);

    if(cards.isEmpty()){
      // no appointments available
      am.addVaccination(0, "", "", LocalDateTime.MAX, LocalDateTime.MAX, "", 0);
      setNull(cards.get(0));
    } else {
      // populate page with appointments
      for (int i = 0; i < cards.size(); i++) {
        VaccinationComponent c = cards.get(i);
        Vaccination vcn = model.getVaccinationModel().getVaccination(c.getVcnId());

        Appointment latest = null;
        for (int j = 0; j < appts.size(); j++) {
          Appointment a = appts.get(j);
          if (a.getVaccination().equals(vcn)) latest = a;
        }

        // set status
        if (latest != null) {
          switch (latest.getStatus()) {
            case Appointment.APPT_ONGOING:
              setBooked(c, vcn, latest);
              break;
            case Appointment.APPT_CANCELLED:
              setNotBooked(c, vcn, latest);
              break;
            case Appointment.APPT_FINISHED:
              setFinished(c);
              break;
          }
        } else {
            setNotBooked(c, vcn, latest);
        }
      }
    }
  }

  private void setNull(VaccinationComponent c) {
      c.removeBookBtnListeners();
      c.removeElements();
      c.setVacCentreName("No vaccination appointments available right now");
  }
  
  private void setBooked(VaccinationComponent c, Vaccination vcn, Appointment a) {
    c.removeBookBtnListeners();
    c.setBookStatus("Booked");
    c.setBookBtnStatus("Cancel");
    c.addBookBtnListener(handleCancel(c, vcn, a));
  }

  private void setNotBooked(VaccinationComponent c, Vaccination vcn, Appointment a) {
    c.removeBookBtnListeners();
    c.setBookStatus("Not Booked");
    c.setBookBtnStatus("Book");
    c.addBookBtnListener(handleBook(c, vcn, a));
  }

  private void setFinished(VaccinationComponent c) {
    c.removeBookBtnListeners();
    c.setBookStatus("Finished");
    c.setBookBtnStatus("Finished");
  }

  private boolean hasAppointment() {
    ArrayList<Appointment> appt = model.getAppointmentModel().getAppointmentsByUser(user);
    for (int i = 0; i < appt.size(); i++) {
      Appointment a = appt.get(i);
      if (a.getStatus().equals(Appointment.APPT_ONGOING)) return true;
    }
    return false;
  }

  private Appointment getLastAppointment() {
    ArrayList<Appointment> appt = model.getAppointmentModel().getAppointmentsByUser(user);
    for (int i = 0; i < appt.size(); i++) {
      Appointment a = appt.get(i);
      if (a.getStatus().equals(Appointment.APPT_FINISHED)) return a;
    }
    return null;
  }

  private ActionListener handleBook(VaccinationComponent c, Vaccination vcn, Appointment a) {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        // if user has already booked an appointment
        if (hasAppointment()) {
          JOptionPane.showMessageDialog(
              null, "You already have an appointment!",
              "Too many appointments", JOptionPane.ERROR_MESSAGE);
          return;
        }

        // if user is fully vaccinated
        if (user.getVacStatus().equals(User.VAC_FULL)) {
          JOptionPane.showMessageDialog(
              null, "You are Fully Vaccinated",
              "Fully Vaccinated", JOptionPane.ERROR_MESSAGE);
          return;
        }

        // if user has 1st dose and not after dose interval
        if (user.getVacStatus().equals(User.VAC_PARTIAL)) {
          Appointment last = getLastAppointment();
          if (last == null) {
          JOptionPane.showMessageDialog(
              null, "You have no past appointments",
              "Unvaccinated", JOptionPane.ERROR_MESSAGE);
          return;
          }
          Vaccination vn = last.getVaccination();
          Vaccine v = vn.getVaccine();
          if (LocalDateTime.now().isBefore(vcn.getEndDate().plusDays(v.getDoseInterval()))) {
          JOptionPane.showMessageDialog(
              null, String.format("You need to wait %d days after your last appointment", v.getDoseInterval()),
              "Not past dose interval", JOptionPane.ERROR_MESSAGE);
          return;
          }
        }

        // if vaccine is out of stock
        if (vcn.getQuantity() == 0) {
          JOptionPane.showMessageDialog(
              null, "Vaccine out of stock", "Vaccine out of stock", JOptionPane.ERROR_MESSAGE);
          return;
        }
        
        // confirmation
        // https://stackoverflow.com/questions/15853112/joptionpane-yes-no-option
        String message = "Are you sure you want to submit this vaccination appointment?\n" +
                "\nTime:\n" + 
                vcn.getStartDate().format(DateTimeFormatter.ofPattern("EEEE, d MMMM uuuu HH:mm")) + " - " +
                vcn.getEndDate().format(DateTimeFormatter.ofPattern("EEEE, d MMMM uuuu HH:mm")) +
                "\nVenue:\n" + vcn.getVacCentre().getName() + ", " + 
                vcn.getVacCentre().getLocation();

        if(JOptionPane.showConfirmDialog(null, message, "Confirm Appointment", 
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            model.getAppointmentModel().addAppointment(user.getId(), vcn.getId(), Appointment.APPT_ONGOING);
            Appointment a = model.getAppointmentModel().getAppointment(-1);
            Vaccination vn = a.getVaccination();
            vn.setQuantity(vn.getQuantity() - 1);
            c.setVacNameQty(vn.getVaccine().getName(), vn.getQuantity());
            setBooked(c, vcn, a);
        };
        
      }
    };
  }

  private ActionListener handleCancel(VaccinationComponent c, Vaccination vcn, Appointment a) {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        // confirmation
        String message = "Are you sure you want to cancel this vaccination appointment?\n" +
             "\nTime:\n" + 
             vcn.getStartDate().format(DateTimeFormatter.ofPattern("EEEE, d MMMM uuuu HH:mm")) + " - " +
             vcn.getEndDate().format(DateTimeFormatter.ofPattern("EEEE, d MMMM uuuu HH:mm")) +
             "\nVenue:\n" + vcn.getVacCentre().getName() + ", " + 
             vcn.getVacCentre().getLocation();
        if(JOptionPane.showConfirmDialog(null, message, "Confirm Cancellation", 
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            a.setStatus(Appointment.APPT_CANCELLED);
            Vaccination vn = a.getVaccination();
            vn.setQuantity(vn.getQuantity() + 1);
            c.setVacNameQty(vn.getVaccine().getName(), vn.getQuantity());
            setNotBooked(c, vcn, a);
        }
      }
    };
  }
}
