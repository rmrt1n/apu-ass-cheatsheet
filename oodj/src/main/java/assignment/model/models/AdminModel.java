package assignment.model.models;

import assignment.model.types.Admin;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminModel extends BaseModel {
  private static ArrayList<Admin> admins = new ArrayList<Admin>();
  private static final String filename = "./src/main/resources/data/admin.txt";
  private static final String header = "admin_id,admin_name,admin_email,admin_password,admin_phone,admin_deleted";

  public AdminModel() {
    read();
    getLastId(admins);
  }

  private int countNotDeleted() {
    int count = 0;
    for (int i = 0; i < admins.size(); i++) {
      if (admins.get(i).getIsDeleted() == 1) continue;
      count++;
    }
    return count;
  }

  public String[][] toArray() {
    int size = countNotDeleted();
    String[][] a = new String[size][5];
    int j = 0;

    for (int i = 0; i < admins.size(); i++) {
      Admin u = admins.get(i);
      if (u.getIsDeleted() == 1) continue;
      a[j++] = new String[] { 
        Integer.toString(u.getId()),
        u.getName(),
        u.getEmail(),
        u.getPassword(),
        u.getPhone(),
      };
    }
    return a;
  }

  public static Admin isRegisteredAdmin(String email) {
    for (int i = 0; i < admins.size(); i++) {
      Admin a = admins.get(i);
      if (a.getEmail().equals(email)) return a;
    }
    return null;
  }

  public Admin verifyAdmin(String email, String password) {
    Admin a = isRegisteredAdmin(email);
    if(a!=null) {
      if (!a.getPassword().equals(Admin.md5sum(password))) return null;
      return a;
    }
    return null;
  }

  public void addAdmin(String name, String email, String password, String phone) {
      Admin a = new Admin(newId(), name, email, Admin.md5sum(password), phone, 0);
      admins.add(a);
  }

  public Admin getAdmin(int id) {
    id = id < 0 ? admins.size() + id + 1 : id;
    if (id < 0 || id > admins.size()) return null;
    return admins.get(id - 1);
  }

  public void removeAdmin(int id) {
    for (int i = 0; i < admins.size(); i++) {
      Admin a = admins.get(i);
      if (a.getId() == id) a.setIsDeleted(1);
    }
  }

  public void modifyAdmin(int id, String name, String email, String password, String phone) {
    for (int i = 0; i < admins.size(); i++) {
      Admin a = admins.get(i);
      String hashedPass = Admin.md5sum(password);
      if (a.getId() != id) continue;
      if (!a.getName().equals(name)) a.setName(name);
      if (!a.getEmail().equals(email)) a.setEmail(email);
      if (!password.equals("") && !a.getPassword().equals(hashedPass)) a.setPassword(password);
      if (!a.getPhone().equals(phone)) a.setPhone(phone);
    }
  }

  public static ArrayList<Admin> getAdmins() {
    return admins;
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
    } catch (IOException e) {
      System.err.println("Error when creating admin.txt");
    }

    try {
      Scanner s = new Scanner(new File(filename));
      s.nextLine();

      while (s.hasNext()) {
        String[] l = s.nextLine().split(",");
        Admin a = new Admin(Integer.parseInt(l[0]), l[1], l[2], l[3], l[4], Integer.parseInt(l[5]));
        admins.add(a);
      }
    } catch (Exception e) {
      System.err.println("Error when reading admin.txt");
      System.err.println(e.getMessage());
    }
  }

  public static void write() {
    try {
      PrintWriter p = new PrintWriter(filename);
      p.println(header);
      for (int i = 0; i < admins.size(); i++) {
        Admin a = admins.get(i);
        p.println(a.toString());
      }
      p.close();
    } catch (Exception e) {
      System.err.println("Error when writing to admin.txt");
      System.err.println(e.getMessage());
    }
  }
}
