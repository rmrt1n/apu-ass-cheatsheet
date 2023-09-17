package assignment.model.types;

import java.time.LocalDate;

public class User extends Human implements Type {
  public static final String USER_LOCAL = "USER_LOCAL";
  public static final String USER_FOREIGN = "USER_FOREIGN";

  public static final String VAC_UNVAC = "VAC_UNVAC";
  public static final String VAC_PARTIAL = "VAC_PARTIAL";
  public static final String VAC_FULL = "VAC_FULL";

  public static final String M = "M";
  public static final String F = "F";

  private int id;
  private String name;
  private String email;
  private String password;
  private String phone;
  private LocalDate birthdate;
  private String gender;
  private String local;
  private String ic;
  private String vacStatus;
  private int isDeleted;

  public User(int userId, String userName, String userEmail, String userPassword,
              String userPhone, String birthdate, String userGender, String userLocal, 
              String userIC, String userVacStatus, int isDeleted) {
    this.id = userId;
    this.name = userName;
    this.email = userEmail;
    this.password = userPassword;
    this.phone = userPhone;
    this.birthdate = LocalDate.parse(birthdate);
    this.gender = userGender;
    this.local = userLocal;
    this.ic = userIC;
    this.vacStatus = userVacStatus;
    this.isDeleted = isDeleted;
  }

  public String toString() {
    return String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%d",
      id, name, email, password, phone, birthdate.toString(), gender, local, ic, vacStatus, isDeleted);
  }

  public void setId(int userId) {
    this.id = userId;
  }

  public void setName(String userName) {
    this.name = userName;
  }

  public void setEmail(String userEmail) {
    this.email = userEmail;
  }

  public void setPassword(String userPassword) {
    this.password = md5sum(userPassword);
  }

  public void setPhone(String userPhone) {
    this.phone = userPhone;
  }

  public void setBirthdate(String birthdate) {
    this.birthdate = LocalDate.parse(birthdate);
  }

  public void setGender(String userGender) {
    this.gender = userGender;
  }

  public void setLocal(String userLocal) {
    this.local = userLocal;
  }

  public void setIC(String userIC) {
    this.ic = userIC;
  }

  public void setVacStatus(String userVacStatus) {
    this.vacStatus = userVacStatus;
  }

  public void setIsDeleted(int isDeleted) {
    this.isDeleted = isDeleted;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getPhone() {
    return phone;
  }

  public LocalDate getBirthdate() {
    return birthdate;
  }

  public String getGender() {
    return gender;
  }

  public String getLocal() {
    return local;
  }

  public String getIC() {
    return ic;
  }

  public String getVacStatus() {
    return vacStatus;
  }

  public int getIsDeleted() {
    return isDeleted;
  }
}
