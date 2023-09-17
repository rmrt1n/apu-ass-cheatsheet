package assignment.controller.admin;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import java.util.ArrayList;

import assignment.model.Model;
import assignment.model.types.Vaccine;
import assignment.model.models.VaccineModel;
import assignment.view.View;
import assignment.view.admin.VaccineManagement;

public class TVaccine implements TableController {
  private Model model;
  private View view;

  private VaccineManagement vm;
  private JTable table;
  private DefaultTableModel tableModel;
  private VaccineModel vaccineModel;

  public TVaccine(Model m, View v) {
    model = m;
    view = v;

    vm = view.getAdminPanel().getVaccineManagement();
    vaccineModel = model.getVaccineModel();

    setTable();
    vm.addInventoryTableMouseListener(handleMouseClick());
    vm.addTableAddListener(handleTableAdd());
    vm.addTableDeleteListener(handleTableDelete());
    vm.addTableEditListener(handleTableModify());
    vm.addSearchListener(handleSearch());
    vm.addSearchDocumentListener(handleSearchReset());
  }

  private boolean validateInput(JTextField[] textFields, boolean emptyId) {
    int id, dose, doseInterval;
    String name;
    ArrayList<Vaccine> vaccines = VaccineModel.getVaccines();

    try {
      // check types
      id = emptyId ? 0 : Integer.parseInt(textFields[0].getText());
      name = textFields[1].getText();
      dose = Integer.parseInt(textFields[2].getText());
      doseInterval = Integer.parseInt(textFields[3].getText());

      // check strings
      if (name.trim().isEmpty())  {
        JOptionPane.showMessageDialog(null, "Input cannot be empty!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        return false;
      }

      if (name.contains(",")) {
        JOptionPane.showMessageDialog(null, "Input cannot contain ','!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      
      // check dose
      if(dose == 1 && doseInterval != 0){
        JOptionPane.showMessageDialog(null, "Vaccines with one dose should have 0 dose interval", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      
      if(dose > 5 || dose < 1){
        JOptionPane.showMessageDialog(null, "Invalid vaccine dose", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        return false;
      }

      // check duplicates
      if(emptyId){
        for(int i = 0; i < vaccines.size(); i++){
          if(vaccines.get(i).getName().equals(name)) {
            JOptionPane.showMessageDialog(
            null, "Adding duplicate record", "Invalid input",
            JOptionPane.ERROR_MESSAGE);
            return false;
          }
        }
      }

    } catch(NumberFormatException e) {
      JOptionPane.showMessageDialog(null, "Please enter a valid input", "Invalid Input", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  public MouseAdapter handleMouseClick(){
    return new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        table = vm.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        JTextField[] textFields = vm.getTextField();

        //Get data
        for(int i = 0; i < table.getColumnCount(); i++){
          String data = tableModel.getValueAt(table.getSelectedRow(), i).toString();
          //Set data
          textFields[i].setText(data);
        }
      }
    };
  }

  public ActionListener handleTableAdd(){
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        table = vm.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        JTextField[] textFields = vm.getTextField();

        // validate input
        if(!validateInput(textFields, true)) return;

        // get values
        int dose = Integer.parseInt(textFields[2].getText()), doseInterval = Integer.parseInt(textFields[3].getText());
        String name = textFields[1].getText();

        vaccineModel.addVaccine(name, dose, doseInterval);

        Vaccine v = vaccineModel.getVaccine(-1);
        String[] newRow = v.toString().split(",");
        tableModel.addRow(newRow);
      }
    };
  }

  public ActionListener handleTableDelete(){
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        table = vm.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        
        int row = table.getSelectedRow();
        if (row < 0) return;

        JTextField[] textFields = vm.getTextField();
        vaccineModel.removeVaccine(Integer.parseInt(textFields[0].getText()));
        tableModel.removeRow(row);
      }
    };
  }

  public ActionListener handleTableModify(){
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        table = vm.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        JTextField[] textFields = vm.getTextField();

        // get values
        if(!validateInput(textFields, false)) return;

        int id = Integer.parseInt(textFields[0].getText()), 
            dose = Integer.parseInt(textFields[2].getText()), 
            doseInterval = Integer.parseInt(textFields[3].getText());
        String name = textFields[1].getText();
        
        vaccineModel.modifyVaccine(id, name, dose, doseInterval);

        for(int i = 0; i < textFields.length; i++) {
          tableModel.setValueAt(textFields[i].getText(),table.getSelectedRow(),i);
        }
      }
    };
  }

  private ActionListener handleSearch() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        try {
          String query = vm.getSearchField();
          if (query.trim().isEmpty()) return;

          ArrayList<String[]> results = new ArrayList<String[]>();
          ArrayList<Vaccine> vaccines = VaccineModel.getVaccines();

          for (int i = 0; i < vaccines.size(); i++) {
            Vaccine v = vaccines.get(i);
            if (v.getIsDeleted() == 1) continue;
            if (v.toString().matches(String.format(".*%s.*", query))) {
              results.add(v.toString().split(","));
            }
          }

          vm.setTable(new DefaultTableModel(
            results.toArray(new String[0][0]),
            new String[] {
              "Vaccine Id", "Vaccine Name", "Vaccine Dose", "Vaccine Dose Interval"
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
    vm.setTable(new DefaultTableModel(
      vaccineModel.toArray(),
      new String[] {
        "Vaccine Id", "Vaccine Name", "Vaccine Dose", "Vaccine Dose Interval"
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
        if (vm.getSearchField().isEmpty()) setTable();
      }
    };
  }
}
