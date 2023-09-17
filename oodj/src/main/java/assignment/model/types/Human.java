package assignment.model.types;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class Human {
  public abstract void setId(int id);
  public abstract void setName(String name);
  public abstract void setEmail(String email);
  public abstract void setPassword(String password);
  public abstract void setPhone(String phone);
  public abstract String getName();
  public abstract String getEmail();
  public abstract String getPassword();
  public abstract String getPhone();

  public static String md5sum(String str) {
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("error: unable to compute md5sum", e);
    }
    md.update(str.getBytes());
    BigInteger digest = new BigInteger(1, md.digest());
    String hash = digest.toString(16);
    while (hash.length() < 32) hash = "0" + hash;
    return hash;
  }
}
