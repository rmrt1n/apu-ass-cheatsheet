package assignment.controller.admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.util.regex.*;
import java.util.ArrayList;

import assignment.model.Model;
import assignment.model.types.User;
import assignment.model.models.UserModel;
import assignment.view.View;
import assignment.view.admin.UserManagement;

public class TUser implements TableController {
  private Model model;
  private View view;

  private UserManagement um;
  private JTable table;
  private DefaultTableModel tableModel;
  private UserModel userModel;

  public TUser(Model m, View v) {
    model = m;
    view = v;

    um = view.getAdminPanel().getUserManagement();
    userModel = model.getUserModel();

    setTable();
    um.addUserTableMouseListener(handleMouseClick());
    um.addTableAddListener(handleTableAdd());
    um.addTableDeleteListener(handleTableDelete());
    um.addTableEditListener(handleTableModify());
    um.addSearchListener(handleSearch());
    um.addSearchDocumentListener(handleSearchReset());
  }

  private boolean validateInput(JTextField[] textFields, boolean emptyId) {
    int id;
    String name, email, password, phone, birthdate, ic;
    ArrayList<User> users = UserModel.getUsers();

    try {
      // check types
      id = emptyId ? 0 : Integer.parseInt(textFields[0].getText());
      name = textFields[1].getText();
      email = textFields[2].getText();
      password = textFields[3].getText();
      phone = textFields[4].getText();
      birthdate = textFields[5].getText();
      ic = textFields[6].getText();

      // check strings
      if (name.trim().isEmpty()       ||
          email.trim().isEmpty()      ||
          phone.trim().isEmpty()      ||
          birthdate.trim().isEmpty()  ||
          ic.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Input cannot be empty!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        return false;
      }

      if (name.contains(",")      ||
          email.contains(",")     ||
          phone.contains(",")     ||
          birthdate.contains(",") ||
          ic.contains(",")){
        JOptionPane.showMessageDialog(null, "Input cannot contain ','!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        return false;
      }

      Pattern p = Pattern.compile("^(.+)@(.+)$");
      Matcher m = p.matcher(email);
      if (!m.matches()) {
        JOptionPane.showMessageDialog(
            null, "Please input a valid email", "Invalid email",
            JOptionPane.ERROR_MESSAGE);
        return false;
      }

      p = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");
      m = p.matcher(phone); 
      if (!m.find() || !m.group(0).equals(phone)) {
        JOptionPane.showMessageDialog(
            null, "Please input a valid international phone number", "Invalid phone number",
            JOptionPane.ERROR_MESSAGE);
        return false;
      }

      // check duplicates
      if(emptyId){
        for(int i = 0; i < users.size(); i++){
          if(users.get(i).getEmail().equals(email) ||
             users.get(i).getIC().equals(ic)) {
            JOptionPane.showMessageDialog(
            null, "Adding duplicate record", "Invalid input",
            JOptionPane.ERROR_MESSAGE);
            return false;
          }
        }
        
        if(password.isEmpty()){
          JOptionPane.showMessageDialog(
            null, "Password cannot be empty", "Invalid input",
            JOptionPane.ERROR_MESSAGE);
            return false;
        }      
      }

    } catch(NumberFormatException e) {
      JOptionPane.showMessageDialog(null, "Please enter a valid input", "Invalid Input", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  public MouseAdapter handleMouseClick() {
    return new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        table = um.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        Object[] fields = um.getFields();
        JTextField[] textFields = um.getTextField();
        JRadioButton[] radioButtons = um.getButtons();

        int tfCount = 0;
        //Get data
        for(int i = 0; i < (table.getColumnCount()); i++){
          //Skip password textfield
          if (i == 3) {
            tfCount++;
            continue;
          }
          String data = tableModel.getValueAt(table.getSelectedRow(), i).toString();
          //Set data
          if (fields[i] instanceof JTextField) {
            textFields[tfCount].setText(data);
            tfCount++;
          } else {
              switch(data){
                case User.M:
                  radioButtons[0].setSelected(true);
                  break;
                case User.F:
                  radioButtons[1].setSelected(true);
                  break;
                case User.USER_LOCAL:
                  radioButtons[2].setSelected(true);
                  break;
                case User.USER_FOREIGN:
                  radioButtons[3].setSelected(true);
                  break;
                case User.VAC_UNVAC:
                  radioButtons[4].setSelected(true);
                  break;
                case User.VAC_PARTIAL:
                  radioButtons[5].setSelected(true);
                  break;
                case User.VAC_FULL:
                  radioButtons[6].setSelected(true);
                  break;
              }
          }
        }
      }
    };
  }

  public ActionListener handleTableAdd() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        table = um.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        JTextField[] textFields = um.getTextField();
        JRadioButton[] radioButtons = um.getButtons();

        // validate input
        if(!validateInput(textFields, true)) return;
        
        // get values
        String name = textFields[1].getText(), email = textFields[2].getText(),
               password = textFields[3].getText(), phone = textFields[4].getText(),
               birthdate = textFields[5].getText(), ic = textFields[6].getText();
        String gender = radioButtons[0].isSelected() ? User.M : User.F;
        String local = radioButtons[2].isSelected() ? User.USER_LOCAL : User.USER_FOREIGN;
        String vacStatus = radioButtons[4].isSelected() ? User.VAC_UNVAC : 
                           radioButtons[5].isSelected() ? User.VAC_PARTIAL : User.VAC_FULL;

        userModel.addUser(name, email, password, phone,
                          birthdate, gender, local, ic, vacStatus);

        User u = userModel.getUser(-1);
        String[] newRow = u.toString().split(",");
        tableModel.addRow(newRow);
      }
    };
  }

  public ActionListener handleTableDelete() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        table = um.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        
        int row = table.getSelectedRow();
        if (row < 0) return;

        JTextField[] textFields = um.getTextField();
        userModel.removeUser(Integer.parseInt(textFields[0].getText()));
        tableModel.removeRow(row);
      }
    };
  }

  public ActionListener handleTableModify() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        table = um.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        JTextField[] textFields = um.getTextField();
        JRadioButton[] radioButtons = um.getButtons();
        Object[] fields = um.getFields();

        // validate input
        if(!validateInput(textFields, false)) return;
        // get values
        int id = Integer.parseInt(textFields[0].getText());
        String name = textFields[1].getText(), email = textFields[2].getText(),
              password = textFields[3].getText(), phone = textFields[4].getText(),
              birthdate = textFields[5].getText(), ic = textFields[6].getText();
        String gender = radioButtons[0].isSelected() ? User.M : User.F;
        String local = radioButtons[2].isSelected() ? User.USER_LOCAL : User.USER_FOREIGN;
        String vacStatus = radioButtons[4].isSelected() ? User.VAC_UNVAC : 
                           radioButtons[5].isSelected() ? User.VAC_PARTIAL : User.VAC_FULL;
        
        // check if same values
        User u = userModel.getUser(id);
        if (u == null) return;

        userModel.modifyUser(id, name, email, password, phone, birthdate, 
                                      gender, local, ic, vacStatus);
        
        String[] newInput = { String.valueOf(id), name, email, u.getPassword(), phone, birthdate, gender, local, ic, vacStatus };
        
        // handle password edit
        if (!password.isEmpty()){
          newInput[3] = User.md5sum(password);
        }
        
        for(int i = 0; i < fields.length; i++) {
          tableModel.setValueAt(newInput[i],table.getSelectedRow(),i);
        } 
      }
    };
  }

  private ActionListener handleSearch() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        try {
          String query = um.getSearchField();
          if (query.trim().isEmpty()) return;

          ArrayList<String[]> results = new ArrayList<String[]>();
          ArrayList<User> users = UserModel.getUsers();

          for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            if (u.getIsDeleted() == 1) continue;
            if (u.toString().matches(String.format(".*%s.*", query))) {
              results.add(u.toString().split(","));
            }
          }

          um.setTable(new DefaultTableModel(
            results.toArray(new String[0][0]),
            new String[] {
              "User ID", "Name", "Email",
              "Password", "Phone", "Birth Date",
              "Gender", "Locality", "IC",
              "Vaccination Status"
            }
          ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
          });

        } catch(NumberFormatException e) {
          JOptionPane.showMessageDialog(null,
              "Please enter a valid input", "Invalid Input", JOptionPane.ERROR_MESSAGE);
          return;
        }
      }
    };
  }

  private void setTable() {
    um.setTable(new DefaultTableModel(
      userModel.toArray(),
      new String[] {
        "User ID", "Name", "Email",
        "Password", "Phone", "Birth Date",
        "Gender", "Locality", "IC",
        "Vaccination Status"
      }
    ) {
      public boolean isCellEditable(int row, int column) {
          return false;
      }
    });
  }

  private DocumentListener handleSearchReset() {
    return new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        updateTable();
      }
      public void removeUpdate(DocumentEvent e) {
        updateTable();
      }
      public void insertUpdate(DocumentEvent e) {
        updateTable();
      }

      private void updateTable() {
        if (um.getSearchField().isEmpty()) setTable();
      }
    };
  }
}
