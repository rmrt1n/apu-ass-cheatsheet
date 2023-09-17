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
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;

import assignment.model.Model;
import assignment.model.types.Vaccination;
import assignment.model.models.VaccinationModel;
import assignment.view.View;
import assignment.view.admin.VaccinationManagement;

public class TVaccination implements TableController {
  private Model model;
  private View view;

  private VaccinationManagement vnm;
  private JTable table;
  private DefaultTableModel tableModel;
  private VaccinationModel vaccinationModel;

  public TVaccination(Model m, View v) {
    model = m;
    view = v;

    vnm = view.getAdminPanel().getVaccinationManagement();
    vaccinationModel = model.getVaccinationModel();

    setTable();
    vnm.addInventoryTableMouseListener(handleMouseClick());
    vnm.addTableAddListener(handleTableAdd());
    vnm.addTableEditListener(handleTableModify());
    vnm.addTableDeleteListener(handleTableDelete());
    vnm.addSearchListener(handleSearch());
    vnm.addSearchDocumentListener(handleSearchReset());
  }

  private boolean validateInput(JTextField[] textFields, DatePicker[] dates, TimePicker[] times, boolean emptyId) {
    int id, vacId, vccId, qty;
    String start, end;
    ArrayList<Vaccination> vaccinations = VaccinationModel.getVaccinations();

    try {
      // check types
      id = emptyId ? 0 : Integer.parseInt(textFields[0].getText());
      vacId = Integer.parseInt(textFields[1].getText());
      vccId = Integer.parseInt(textFields[2].getText());
      qty = Integer.parseInt(textFields[3].getText());
      start = LocalDateTime.of(dates[0].getDate(), times[0].getTime()).toString();
      end = LocalDateTime.of(dates[1].getDate(), times[1].getTime()).toString();

      if(!validateDateTimeInput(dates, times)) return false;

      // check duplicate
      if(emptyId){
        for(int i = 0; i < vaccinations.size(); i++){
          if(vaccinations.get(i).getVaccine().getId() == vacId &&
             vaccinations.get(i).getVacCentre().getId() == vccId &&
             vaccinations.get(i).getStartDate().toString().equals(start) &&
             vaccinations.get(i).getEndDate().toString().equals(end)
             ) {
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
        table = vnm.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        JTextField[] textFields = vnm.getTextField();
        DatePicker[] dates = vnm.getDatePicker();
        TimePicker[] times = vnm.getTimePicker();

        // Set text fields
        for (int i = 0; i < textFields.length; i++) {
          String data = tableModel.getValueAt(table.getSelectedRow(), i).toString();          
          textFields[i].setText(data);
        }
        
        // Set start and end date & time
        LocalDateTime start = LocalDateTime.parse(
            tableModel.getValueAt(table.getSelectedRow(), 4).toString());
        LocalDateTime end = LocalDateTime.parse(
            tableModel.getValueAt(table.getSelectedRow(), 5).toString());

        dates[0].setDate(start.toLocalDate());
        dates[1].setDate(end.toLocalDate());

        times[0].setTime(start.toLocalTime());
        times[1].setTime(end.toLocalTime());
      }
    };
  }

  private boolean validateDateTimeInput(DatePicker[] dates, TimePicker[] times) {
    for (int i = 0; i < dates.length; i++) {
      if (dates[i].getDate() == null) {
        JOptionPane.showMessageDialog(null,
            "Please enter a valid input", "Invalid Date", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    for (int i = 0; i < times.length; i++) {
      if (times[i].getTime() == null) {
        JOptionPane.showMessageDialog(null,
            "Please enter a valid input", "Invalid Time", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    return true;
  }

  public ActionListener handleTableAdd() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        table = vnm.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        JTextField[] textFields = vnm.getTextField();
        DatePicker[] dates = vnm.getDatePicker();
        TimePicker[] times = vnm.getTimePicker();

        // validate input
        if(!validateInput(textFields, dates, times, true)) return;

        // get values
        String start = LocalDateTime.of(dates[0].getDate(), times[0].getTime()).toString(),
               end = LocalDateTime.of(dates[1].getDate(), times[1].getTime()).toString();

        // check if input vaccine or vaccine centre doesn't exist
        if (model.getVaccineModel().getVaccine(Integer.parseInt(textFields[1].getText())) == null ||
            model.getVaccineCentreModel().getVacCentre(Integer.parseInt(textFields[2].getText())) == null) {
          JOptionPane.showMessageDialog(
            null,"Vaccine or vaccine centre doesn't exist!",
            "Invalid Input", JOptionPane.ERROR_MESSAGE);
          return;
        }

        vaccinationModel.addVaccination(Integer.parseInt(textFields[1].getText()), 
                                        Integer.parseInt(textFields[2].getText()), 
                                        Integer.parseInt(textFields[3].getText()), 
                                        start, end);

        Vaccination vn = vaccinationModel.getVaccination(-1);
        String[] newRow = vn.toString().split(",");
        tableModel.addRow(newRow);
      }
    };
  }

  public ActionListener handleTableDelete() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        table = vnm.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        
        int row = table.getSelectedRow();
        if (row < 0) return;

        JTextField[] textFields = vnm.getTextField();
        vaccinationModel.removeVaccination(Integer.parseInt(textFields[0].getText()));
        tableModel.removeRow(row);
      }
    };
  }

  public ActionListener handleTableModify() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        table = vnm.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        JTextField[] textFields = vnm.getTextField();
        DatePicker[] dates = vnm.getDatePicker();
        TimePicker[] times = vnm.getTimePicker();

        // validate input
        if (!validateInput(textFields, dates, times, false)) return;

        // get values
        String start = LocalDateTime.of(
            dates[0].getDate(), times[0].getTime()).toString();
        String end = LocalDateTime.of(
            dates[1].getDate(), times[1].getTime()).toString();

        // check if vaccine or vaccine centre doesn't exist
        if (model.getVaccineModel().getVaccine(Integer.parseInt(textFields[1].getText())) == null ||
            model.getVaccineCentreModel().getVacCentre(Integer.parseInt(textFields[2].getText())) == null) {
          JOptionPane.showMessageDialog(
            null,"Vaccine or vaccine centre doesn't exist!",
            "Invalid Input", JOptionPane.ERROR_MESSAGE);
          return;
        }

        vaccinationModel.modifyVaccination(
          Integer.parseInt(textFields[0].getText()), 
          Integer.parseInt(textFields[1].getText()), 
          Integer.parseInt(textFields[2].getText()), 
          Integer.parseInt(textFields[3].getText()), 
          start, end);

        // set table
        for(int i = 0; i < textFields.length; i++) {
          tableModel.setValueAt(textFields[i].getText(),table.getSelectedRow(),i);
        }
        tableModel.setValueAt(start,table.getSelectedRow(),4);
        tableModel.setValueAt(end,table.getSelectedRow(),5);
      }
    };
  }

  private ActionListener handleSearch() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        try {
          String query = vnm.getSearchField();
          if (query.trim().isEmpty()) return;

          ArrayList<String[]> results = new ArrayList<String[]>();
          ArrayList<Vaccination> vaccinations = VaccinationModel.getVaccinations();

          for (int i = 0; i < vaccinations.size(); i++) {
            Vaccination vn = vaccinations.get(i);
            if (vn.toString().matches(String.format(".*%s.*", query))) {
              results.add(vn.toString().split(","));
            }
          }

          vnm.setTable(new DefaultTableModel(
            results.toArray(new String[0][0]),
            new String[] {
              "Vaccination ID", "Vaccine ID", "Centre ID", "Quantity",
              "Start Date", "End Date"
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
    vnm.setTable(new DefaultTableModel(
      vaccinationModel.toArray(),
      new String[] {
        "Vaccination ID", "Vaccine ID", "Centre ID", "Quantity", "Start Date", "End Date"
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
        if (vnm.getSearchField().isEmpty()) setTable();
      }
    };
  }
}
