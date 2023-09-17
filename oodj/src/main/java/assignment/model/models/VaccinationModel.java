package assignment.model.models;

import assignment.model.types.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class VaccinationModel extends BaseModel {
  private static ArrayList<Vaccination> vaccinations = new ArrayList<Vaccination>();
  private static final String filename = "./src/main/resources/data/vaccination.txt";
  private static final String header = "vcn_id,vcn_vac_id,vcn_vcc_id,vcn_quantity,vcn_start_dt,vcn_end_dt";

  public VaccinationModel() {
    read();
    getLastId(vaccinations);
  }

  public String[][] toArray() {
    String[][] a = new String[vaccinations.size()][6];
    for (int i = 0; i < vaccinations.size(); i++) {
      Vaccination vn = vaccinations.get(i);
      a[i] = vn.toString().split(",");
    }
    return a;
  }

  private static Vaccine getVaccine(int id) {
    for (Vaccine v : VaccineModel.getVaccines()) {
      if (id == v.getId()) {
        return v;
      }
    }
    return null;
  }

  private static VaccineCentre getVaccineCentre(int id) {
    for (VaccineCentre c : VaccineCentreModel.getVacCentres()) {
      if (id == c.getId()) {
        return c;
      }
    }
    return null;
  }

  public void addVaccination(int vacId, int vccId, int qty, String start, String end) {
    Vaccination v = new Vaccination(newId(), getVaccine(vacId), getVaccineCentre(vccId), qty, start, end);
    vaccinations.add(v);
  }

  public static ArrayList<Vaccination> getVaccinations() {
    return vaccinations;
  }

  public Vaccination getVaccination(int id) {
    id = id < 0 ? vaccinations.size() + id + 1 : id;
    if (id < 0 || id > vaccinations.size()) return null;
    return vaccinations.get(id - 1);
  }

  public void modifyVaccination(int id, int vacId, int vccId, int qty, String startdate, String enddate) {
    for (int i = 0; i < vaccinations.size(); i++) {
      Vaccination vn = vaccinations.get(i);
      Vaccine v = vn.getVaccine();
      VaccineCentre vc = vn.getVacCentre();

      if (vn.getId() != id) continue;
      if (v.getId() != vacId) vn.setVaccine(getVaccine(vacId));
      if (vc.getId() != vccId) vn.setVacCentre(getVaccineCentre(vccId));
      if (vn.getQuantity() != qty) vn.setQuantity(qty);
      if (!(vn.getStartDate().toString().equals(startdate))) vn.setStartDate(startdate);
      if (!(vn.getEndDate().toString().equals(enddate))) vn.setEndDate(enddate);
    }
  }
  
  public void removeVaccination(int id) {
    for (int i = 0; i < vaccinations.size(); i++) {
      Vaccination v = vaccinations.get(i);
      if (v.getId() == id) vaccinations.remove(v);
    }
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
      System.err.println("Error when creating vaccination.txt");
    }

    try {
      Scanner s = new Scanner(new File(filename));
      s.nextLine();
      // s.useDelimiter("[,\\n]");

      while (s.hasNext()) {
        String[] l = s.nextLine().split(",");
        Vaccination v = new Vaccination(
            Integer.parseInt(l[0]),
            getVaccine(Integer.parseInt(l[1])),
            getVaccineCentre(Integer.parseInt(l[2])),
            Integer.parseInt(l[3]),
            l[4],
            l[5]);
        vaccinations.add(v);
      }
    } catch (Exception e) {
      System.err.println("Error when reading vaccination.txt");
      System.err.println(e.getMessage());
    }
  }

  public static void write() {
    try {
      PrintWriter p = new PrintWriter(filename);
      p.println(header);
      for (int i = 0; i < vaccinations.size(); i++) {
        Vaccination v = vaccinations.get(i);
        p.println(v.toString());
      }
      p.close();
    } catch (Exception e) {
      System.err.println("Error when writing vaccination.txt");
      System.err.println(e.getMessage());
    }
  }
}
