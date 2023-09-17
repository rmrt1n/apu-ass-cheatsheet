package assignment.controller.admin;

import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;

public interface TableController {
  public MouseAdapter handleMouseClick();
  public ActionListener handleTableAdd();
  public ActionListener handleTableModify();
  public ActionListener handleTableDelete();
}