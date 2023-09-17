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
import assignment.model.types.VaccineCentre;
import assignment.model.models.VaccineCentreModel;
import assignment.view.View;
import assignment.view.admin.VaccineCentreManagement;

public class TVacCentre implements TableController {
  private Model model;
  private View view;

  private VaccineCentreManagement vcm;
  private JTable table;
  private DefaultTableModel tableModel;
  private VaccineCentreModel vacCentreModel;

  public TVacCentre(Model m, View v) {
    model = m;
    view = v;

    vcm = view.getAdminPanel().getVaccineCentreManagement();
    vacCentreModel = model.getVaccineCentreModel();

    setTable();
    vcm.addInventoryTableMouseListener(handleMouseClick());
    vcm.addTableAddListener(handleTableAdd());
    vcm.addTableDeleteListener(handleTableDelete());
    vcm.addTableEditListener(handleTableModify());
    vcm.addSearchListener(handleSearch());
    vcm.addSearchDocumentListener(handleSearchReset());
  }

  private boolean validateInput(JTextField[] textFields, boolean emptyId) {
    int id, qty;
    String name, location;
    ArrayList<VaccineCentre> vacCentre = VaccineCentreModel.getVacCentres();

    try {
      // check types
      id = emptyId ? 0 : Integer.parseInt(textFields[0].getText());
      name = textFields[1].getText();
      location = textFields[2].getText();
      qty = Integer.parseInt(textFields[3].getText());

      // check strings
      if (name.trim().isEmpty() || location.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Input cannot be empty!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        return false;
      }

      if (name.contains(",") || location.contains(",")) {
        JOptionPane.showMessageDialog(null, "Input cannot contain ','!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        return false;
      }

      // check duplicates
      if(emptyId){
        for(int i = 0; i < vacCentre.size(); i++){
          if(vacCentre.get(i).getName().equals(name) &&
             vacCentre.get(i).getLocation().equals(location)) {
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

  public MouseAdapter handleMouseClick() {
    return new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        table = vcm.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        JTextField[] textFields = vcm.getTextField();

        //Get data
        for(int i = 0; i < table.getColumnCount(); i++){
          String data = tableModel.getValueAt(table.getSelectedRow(), i).toString();
          //Set text fields
          textFields[i].setText(data);
        }
      }
    };
  }

  public ActionListener handleTableAdd() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        table = vcm.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        JTextField[] textFields = vcm.getTextField();

        // validate input
        if(!validateInput(textFields, true)) return;

        // get values
        String name = textFields[1].getText(), location = textFields[2].getText();
        int qty = Integer.parseInt(textFields[3].getText());

        vacCentreModel.addVacCentre(name, location, qty);

        VaccineCentre vc = vacCentreModel.getVacCentre(-1);
        String[] newRow = vc.toString().split(",");
        tableModel.addRow(newRow);
      }
    };
  }

  public ActionListener handleTableDelete() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        table = vcm.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        
        int row = table.getSelectedRow();
        if (row < 0) return;

        JTextField[] textFields = vcm.getTextField();
        vacCentreModel.removeVacCentre(Integer.parseInt(textFields[0].getText()));
        tableModel.removeRow(row);
      }
    };
  }

  public ActionListener handleTableModify() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        table = vcm.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        JTextField[] textFields = vcm.getTextField();

        // validate input
        if(!validateInput(textFields, false)) return;

        // get values
        int id = Integer.parseInt(textFields[0].getText()), qty = Integer.parseInt(textFields[3].getText());
        String name = textFields[1].getText(), location = textFields[2].getText();

        // check if same values
        VaccineCentre vc = vacCentreModel.getVacCentre(id);
        if (vc == null) return;

        vacCentreModel.modifyVacCentre(id, name, location, qty);

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
          String query = vcm.getSearchField();
          if (query.trim().isEmpty()) return;

          ArrayList<String[]> results = new ArrayList<String[]>();
          ArrayList<VaccineCentre> vacCentres = VaccineCentreModel.getVacCentres();

          for (int i = 0; i < vacCentres.size(); i++) {
            VaccineCentre vc = vacCentres.get(i);
            if (vc.getIsDeleted() == 1) continue;
            if (vc.toString().matches(String.format(".*%s.*", query))) {
              results.add(vc.toString().split(","));
            }
          }

          vcm.setTable(new DefaultTableModel(
            results.toArray(new String[0][0]),
            new String[] {
              "Centre Id", "Centre Name", "Centre Location", "Centre Capacity"
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
    vcm.setTable(new DefaultTableModel(
      vacCentreModel.toArray(),
      new String[] {
        "Centre Id", "Centre Name", "Centre Location", "Centre Capacity"
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
        if (vcm.getSearchField().isEmpty()) setTable();
      }
    };
  }
}
