package assignment.controller.admin;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.util.regex.*;
import java.util.ArrayList;

import assignment.model.Model;
import assignment.model.types.Admin;
import assignment.model.models.AdminModel;
import assignment.view.View;
import assignment.view.admin.AdminManagement;

public class TAdmin implements TableController {
  private Model model;
  private View view;

  private AdminManagement am;
  private JTable table;
  private DefaultTableModel tableModel;
  private AdminModel adminModel;

  public TAdmin(Model m, View v) {
    model = m;
    view = v;

    am = view.getAdminPanel().getAdminManagement();
    adminModel = model.getAdminModel();

    setTable();
    am.addInventoryTableMouseListener(handleMouseClick());
    am.addTableAddListener(handleTableAdd());
    am.addTableDeleteListener(handleTableDelete());
    am.addTableEditListener(handleTableModify());
    am.addSearchListener(handleSearch());
    am.addSearchDocumentListener(handleSearchReset());
  }

  private boolean validateInput(JTextField[] textFields, boolean emptyId) {
    int id;
    String name, email, password, phone;
    ArrayList<Admin> admins = AdminModel.getAdmins();

    try {
      // check types
      id = emptyId ? 0 : Integer.parseInt(textFields[0].getText());
      name = textFields[1].getText();
      email = textFields[2].getText();
      password = textFields[3].getText();
      phone = textFields[4].getText();

      // check strings
      if (name.trim().isEmpty()     ||
          email.trim().isEmpty()    ||
          phone.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Input cannot be empty!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        return false;
      }


      if (name.contains(",") || email.contains(",") || phone.contains(",")) {
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

      // check duplicate
      if(emptyId){
        for(int i = 0; i < admins.size(); i++){
          if(admins.get(i).getEmail().equals(email)) {
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
        table = am.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        JTextField[] textFields = am.getTextField();

        //Get data
        for(int i = 0; i < table.getColumnCount(); i++){
          //Skip password textfield
          if (i == 3) {
            continue;
          }
          String data = tableModel.getValueAt(table.getSelectedRow(), i).toString();

          //Set data
          textFields[i].setText(data);
        }
      }
    };
  }

  public ActionListener handleTableAdd() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        table = am.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        JTextField[] textFields = am.getTextField();

        // validate input
        if(!validateInput(textFields, true)) return;
        String name = textFields[1].getText(), email = textFields[2].getText(),
               password = textFields[3].getText(), phone = textFields[4].getText();

        adminModel.addAdmin(name, email, password, phone);

        Admin a = adminModel.getAdmin(-1);
        String[] newRow = a.toString().split(",");
        tableModel.addRow(newRow);
      }
    };
  }

  public ActionListener handleTableDelete() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        table = am.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        
        int row = table.getSelectedRow();
        if (row < 0) return;

        JTextField[] textFields = am.getTextField();
        adminModel.removeAdmin(Integer.parseInt(textFields[0].getText()));
        tableModel.removeRow(row);
      }
    };
  }

  public ActionListener handleTableModify() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        table = am.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        JTextField[] textFields = am.getTextField();
        
        // validate input
        if(!validateInput(textFields, false)) return;
        // get values
        int id = Integer.parseInt(textFields[0].getText());
        String name = textFields[1].getText(), email = textFields[2].getText(),
              password = textFields[3].getText(), phone = textFields[4].getText();

        // check if same values
        Admin a = adminModel.getAdmin(id);
        if (a == null) return;

        adminModel.modifyAdmin(id, name, email, password, phone);

        String[] newInput = { String.valueOf(id), name, email, a.getPassword(), phone };
        
        // handle password edit
        if (!password.isEmpty()){
          newInput[3] = Admin.md5sum(password);
        }
        
        for(int i = 0; i < textFields.length; i++) {
          tableModel.setValueAt(newInput[i],table.getSelectedRow(),i);
        }
      }
    };
  }

  private ActionListener handleSearch() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        try {
          String query = am.getSearchField();
          if (query.trim().isEmpty()) return;

          ArrayList<String[]> results = new ArrayList<>();
          ArrayList<Admin> admins = AdminModel.getAdmins();

          for (int i = 0; i < admins.size(); i++) {
            Admin a = admins.get(i);
            if (a.getIsDeleted() == 1) continue;
            if (a.toString().matches(String.format(".*%s.*", query))) {
              results.add(a.toString().split(","));
            }
          }

          am.setTable(new DefaultTableModel(
            results.toArray(new String[0][0]),
            new String[] {
              "Admin ID", "Admin Name", "Admin Email", "Admin Password", "Admin Phone"
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
    am.setTable(new DefaultTableModel(
      adminModel.toArray(),
      new String[] {
        "Admin ID", "Admin Name", "Admin Email", "Admin Password", "Admin Phone"
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
        if (am.getSearchField().isEmpty()) setTable();
      }
    };
  }
}
