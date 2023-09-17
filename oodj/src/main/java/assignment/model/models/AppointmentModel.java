package assignment.model.models;

import assignment.model.types.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class AppointmentModel extends BaseModel {
  private static ArrayList<Appointment> appointments = new ArrayList<Appointment>();
  private static final String filename = "./src/main/resources/data/appointment.txt";
  private static final String header = "appt_id,appt_user_id,appt_vcn_id,appt_status";

  public AppointmentModel() {
    read();
    getLastId(appointments);
  }
  
  public static ArrayList<Appointment> getAppointments() {
    return appointments;
  }

  public String[][] toArray() {
    String[][] arr = new String[appointments.size()][4];
    for (int i = 0; i < appointments.size(); i++) {
      Appointment a = appointments.get(i);
      arr[i] = a.toString().split(",");
    }
    return arr;
  }
  
  private static User getUser(int id) {
    for (User u : UserModel.getUsers()) {
      if (id == u.getId()) {
        return u;
      }
    }
    return null;
  }

  private static Vaccination getVaccination(int id) {
    for (Vaccination v : VaccinationModel.getVaccinations()) {
      if (v.getId() == id) {
        return v;
      }
    }
    return null;
  }

  public Appointment getAppointment(int id) {
    id = id < 0 ? appointments.size() + id + 1 : id;
    if (id < 0 || id > appointments.size()) return null;
    return appointments.get(id - 1);
  }
  
  public void addAppointment(int userId, int vcnId, String status) {
    Appointment a = new Appointment(
      newId(), getUser(userId), getVaccination(vcnId), status);
    appointments.add(a);
  }
  
  public void modifyAppointment(int id, int userId, int vcnId, String status) {
    for (int i = 0; i < appointments.size(); i++) {
      Appointment a = appointments.get(i);
      User u = a.getUser();
      Vaccination vcn = a.getVaccination();

      if (a.getId() != id) continue;
      if (u.getId() != userId) a.setUser(getUser(userId));
      if (vcn.getId() != vcnId) a.setVaccination(getVaccination(vcnId));
      if (!a.getStatus().equals(status)) a.setStatus(status);
    }
  }
  
  public void removeAppointment(int id) {
    for (int i = 0; i < appointments.size(); i++) {
      Appointment a = appointments.get(i);
      if (a.getId() == id) appointments.remove(a);
    }
  }

  public ArrayList<Appointment> getAppointmentsByUser(User user) {
    ArrayList<Appointment> a = new ArrayList<Appointment>();
    for (int i = 0; i < appointments.size(); i++) {
      Appointment appt = appointments.get(i);
      if (appt.getUser().toString().equals(user.toString())) {
        a.add(appt);
      }
    }
    return a;
  }

  public static void read() {
    try {
      File f = new File(filename);
      if (!f.exists()) {
        f.createNewFile();
        PrintWriter p = new PrintWriter(filename);
        p.println(header);
        p.close();
      }
    } catch (Exception e) {
      System.err.println("Error when creating appointment.txt");
    }

    try {
      Scanner s = new Scanner(new File(filename));
      s.nextLine();

      while (s.hasNext()) {
        String[] l = s.nextLine().split(",");
        Appointment a = new Appointment(
            Integer.parseInt(l[0]),
            getUser(Integer.parseInt(l[1])),
            getVaccination(Integer.parseInt(l[2])),
            l[3]);
        appointments.add(a);
      }
    } catch (Exception e) {
      System.err.println("Error when reading appointment.txt");
      System.err.println(e.getMessage());
    }
  }

  public static void write() {
    try {
      PrintWriter p = new PrintWriter(filename);
      p.println(header);
      for (int i = 0; i < appointments.size(); i++) {
        Appointment a = appointments.get(i);
        p.println(a.toString());
      }
      p.close();
    } catch (Exception e) {
      System.err.println("Error when writing appointment.txt");
      System.err.println(e.getMessage());
    }
  }
}
