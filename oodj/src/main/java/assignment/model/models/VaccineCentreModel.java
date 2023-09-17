package assignment.model.models;

import assignment.model.types.VaccineCentre;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class VaccineCentreModel extends BaseModel {
  private static ArrayList<VaccineCentre> vacCentres = new ArrayList<VaccineCentre>();
  private static final String filename = "./src/main/resources/data/vaccine_centre.txt";
  private static final String header = "vcc_id,vcc_name,vcc_location,vcc_capacity,vcc_deleted";

  public VaccineCentreModel() {
    read();
    getLastId(vacCentres);
  }

  private int countNotDeleted() {
    int count = 0;
    for (int i = 0; i < vacCentres.size(); i++) {
      if (vacCentres.get(i).getIsDeleted() == 1) continue;
      count++;
    }
    return count;
  }

  public String[][] toArray() {
    int size = countNotDeleted();
    String[][] a = new String[size][4];
    int j = 0;

    for (int i = 0; i < vacCentres.size(); i++) {
      VaccineCentre vc = vacCentres.get(i);
      if (vc.getIsDeleted() == 1) continue;
      a[j++] = new String[] { 
        Integer.toString(vc.getId()),
        vc.getName(),
        vc.getLocation(),
        Integer.toString(vc.getCapacity()),
      };
    }
    return a;
  }

  public void addVacCentre(String vccName, String vccLocation, int vccCapacity) {
    VaccineCentre v = new VaccineCentre(newId(), vccName, vccLocation, vccCapacity, 0);
    vacCentres.add(v);
  }

  public VaccineCentre getVacCentre(int id) {
    id = id < 0 ? vacCentres.size() + id + 1 : id;
    if (id < 0 || id > vacCentres.size()) return null;
    return vacCentres.get(id - 1);
  }

  public void removeVacCentre(int id) {
    for (int i = 0; i < vacCentres.size(); i++) {
      VaccineCentre vc = vacCentres.get(i);
      if (vc.getId() == id) vc.setIsDeleted(1);
    }
  }

  public void modifyVacCentre(int id, String name, String location, int capacity) {
    for (int i = 0; i < vacCentres.size(); i++) {
      VaccineCentre vc = vacCentres.get(i);
      if (vc.getId() != id) continue;
      if (!vc.getName().equals(name)) vc.setName(name);
      if (!vc.getLocation().equals(location)) vc.setLocation(location);
      if (vc.getCapacity() != capacity) vc.setCapacity(capacity);
    }
  }

  public static ArrayList<VaccineCentre> getVacCentres() {
    return vacCentres;
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
      System.err.println("Error when creating vaccine_centre.txt");
    }

    try {
      Scanner s = new Scanner(new File(filename));
      s.nextLine();

      while (s.hasNext()) {
        String[] l = s.nextLine().split(",");
        VaccineCentre v = new VaccineCentre(
            Integer.parseInt(l[0]),
            l[1],
            l[2], 
            Integer.parseInt(l[3]),
            Integer.parseInt(l[4]));

        vacCentres.add(v);
      }
    } catch(Exception e) {
      System.err.println("Error when reading vaccine_centre.txt");
      System.err.println(e.getMessage());
    }
  }

  public static void write() {
    try {
      PrintWriter p = new PrintWriter(filename);
      p.println(header);
      for (int i = 0; i < vacCentres.size(); i++) {
        VaccineCentre c = vacCentres.get(i);
        p.println(c.toString());
      }
      p.close();
    } catch(Exception e) {
        System.err.println("Error when writing vaccine_centre.txt");
        System.err.println(e.getMessage());
    }
  }
}
