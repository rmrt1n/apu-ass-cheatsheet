package assignment.model.models;

import assignment.model.types.Vaccine;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class VaccineModel extends BaseModel {
  private static ArrayList<Vaccine> vaccines = new ArrayList<Vaccine>();
  private static final String filename = "./src/main/resources/data/vaccine.txt";
  private static final String header = "vac_id,vac_name,vac_dose,vac_dose_interval,vac_deleted";

  public VaccineModel() {
    read();
    getLastId(vaccines);
  }

  private int countNotDeleted() {
    int count = 0;
    for (int i = 0; i < vaccines.size(); i++) {
      if (vaccines.get(i).getIsDeleted() == 1) continue;
      count++;
    }
    return count;
  }

  public String[][] toArray() {
    int size = countNotDeleted();
    String[][] a = new String[size][4];
    int j = 0;

    for (int i = 0; i < vaccines.size(); i++) {
      Vaccine v = vaccines.get(i);
      if (v.getIsDeleted() == 1) continue;
      a[j++] = new String[] { 
        Integer.toString(v.getId()),
        v.getName(),
        Integer.toString(v.getDose()),
        Integer.toString(v.getDoseInterval())
      };
    }
    return a;
  }

  public void addVaccine(String name, int dose, int interval) {
    Vaccine v = new Vaccine(newId(), name, dose, interval, 0);
    vaccines.add(v);
  }

  public Vaccine getVaccine(int id) {
    id = id < 0 ? vaccines.size() + id + 1 : id;
    if (id < 0 || id > vaccines.size()) return null;
    return vaccines.get(id - 1);
  }

  public void removeVaccine(int id) {
    for (int i = 0; i < vaccines.size(); i++) {
      Vaccine v = vaccines.get(i);
      if (v.getId() == id) v.setIsDeleted(1);
    }
  }

  public void modifyVaccine(int id, String name, int dose, int interval) {
    for (int i = 0; i < vaccines.size(); i++) {
      Vaccine v = vaccines.get(i);
      if (v.getId() != id) continue;
      if (!v.getName().equals(name)) v.setName(name);
      if (v.getDose() != dose) v.setDose(dose);
      if (v.getDoseInterval() != interval) v.setDoseInterval(interval);
    }
  }

  public static ArrayList<Vaccine> getVaccines() {
    return vaccines;
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
      System.err.println("Error when creating vaccine.txt");
    }
     
    try {
      Scanner s = new Scanner(new File(filename));
      s.nextLine();

      while (s.hasNext()) {
        String[] l = s.nextLine().split(",");
        Vaccine v = new Vaccine(
            Integer.parseInt(l[0]),
            l[1],
            Integer.parseInt(l[2]),
            Integer.parseInt(l[3]),
            Integer.parseInt(l[4]));
        vaccines.add(v);
      }
    } catch (IOException e) {
      System.err.println("Error when reading vaccine.txt");
      System.err.println(e.getMessage());
    }
  }

  public static void write() {
    try {
      PrintWriter p = new PrintWriter(filename);
      p.println(header);
      for (int i = 0; i < vaccines.size(); i++) {
        Vaccine v = vaccines.get(i);
        p.println(v.toString());
      }
      p.close();
    } catch (Exception e) {
      System.err.println("Error when writing vaccine.txt");
      System.err.println(e.getMessage());
    }
  }
}
