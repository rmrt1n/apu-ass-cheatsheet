package assignment.model.models;

import java.util.ArrayList;
import assignment.model.types.Type;

public abstract class BaseModel {
  private int lastId = 0;

  protected void getLastId(ArrayList<? extends Type> a) {
    int size = a.size();
    if (size > 0) lastId = ((Type)(a.get(size - 1))).getId();
  }

  protected int newId() {
    return ++lastId;
  }
   
  public static void read() {};
  public static void write() {};
}
