package assignment.model.models;

import assignment.model.types.User;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class UserModel extends BaseModel {
  private static ArrayList<User> users = new ArrayList<User>();
  private static final String filename = "./src/main/resources/data/user.txt";
  private static final String header = "user_id,user_name,user_email,user_password,user_phone,user_birthdate,user_gender,user_local,user_ic,user_vac_status,user_deleted";

  public UserModel() {
    read();
    getLastId(users);
  }

  private int countNotDeleted() {
    int count = 0;
    for (int i = 0; i < users.size(); i++) {
      if (users.get(i).getIsDeleted() == 1) continue;
      count++;
    }
    return count;
  }

  public String[][] toArray() {
    int size = countNotDeleted();
    String[][] a = new String[size][8];
    int j = 0;

    for (int i = 0; i < users.size(); i++) {
      User u = users.get(i);
      if (u.getIsDeleted() == 1) continue;
      a[j++] = new String[] { 
        Integer.toString(u.getId()),
        u.getName(),
        u.getEmail(),
        u.getPassword(),
        u.getPhone(),
        u.getBirthdate().toString(),
        u.getGender(),
        u.getLocal(),
        u.getIC(),
        u.getVacStatus(),
      };
    }
    return a;
  }

  public static User isRegisteredUser(String email) {
    for (int i = 0; i < users.size(); i++) {
      User u = users.get(i);
      if (u.getEmail().equals(email)) return u;
    }
    return null;
  }

  public User verifyUser(String email, String password) {
    User u = isRegisteredUser(email);
    if (u == null) return null;
    if (!u.getPassword().equals(User.md5sum(password))) return null;
    return u;
  }

  public void addUser(String name, String email, String password, String phone, String birthdate,
                      String gender, String local, String ic, String vacStatus) {
    User u = new User(newId(), name, email, User.md5sum(password), phone,
                      birthdate, gender, local, ic, vacStatus, 0);
    users.add(u);
  }

  public User getUser(int id) {
    id = id < 0 ? users.size() + id + 1 : id;
    if (id < 0 || id > users.size()) return null;
    return users.get(id - 1);
  }

  public void removeUser(int id) {
    for (int i = 0; i < users.size(); i++) {
      User u = users.get(i);
      if (u.getId() == id) u.setIsDeleted(1);
    }
  }

  public void modifyUser(
      int id, String name, String email, String password, String phone, 
      String birthdate, String gender, String local, String ic, String vacStatus) {
    for (int i = 0; i < users.size(); i++) {
      User u = users.get(i);
      String hashedPass = User.md5sum(password);
      if (u.getId() != id) continue;
      if (!u.getName().equals(name)) u.setName(name);
      if (!u.getEmail().equals(email)) u.setEmail(email);
      if (!password.equals("") && !u.getPassword().equals(hashedPass)) u.setPassword(password);
      if (!u.getPhone().equals(phone)) u.setPhone(phone);
      if (!u.getBirthdate().toString().equals(birthdate)) u.setBirthdate(birthdate);
      if (!u.getGender().toString().equals(gender)) u.setGender(gender);
      if (!u.getLocal().toString().equals(local)) u.setLocal(local);
      if (!u.getIC().equals(ic)) u.setIC(ic);
      if (!u.getVacStatus().toString().equals(vacStatus)) u.setVacStatus(vacStatus);
    }
  }

  public static ArrayList<User> getUsers() {
    return users;
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
      System.err.println("Error when creating user.txt");
    }

    try {
      Scanner s = new Scanner(new File(filename));
      s.nextLine();

      while (s.hasNext()) {
        String[] w = s.nextLine().split(",");
        User u = new User(
          Integer.parseInt(w[0]), w[1], w[2], w[3], w[4], w[5], w[6], w[7], w[8], w[9], Integer.parseInt(w[10]));
        users.add(u);
      }
    } catch (Exception e) {
      System.err.println("Error when reading user.txt");
      System.err.println(e.getMessage());
    }
  }

  public static void write() {
    try {
      PrintWriter p = new PrintWriter(filename);
      p.println(header);
      for (int i = 0; i < users.size(); i++) {
        User u = users.get(i);
        p.println(u.toString());
      }
      p.close();
    } catch (Exception e) {
      System.err.println("Error when writing user.txt");
      System.err.println(e.getMessage());
    }
  }
}
