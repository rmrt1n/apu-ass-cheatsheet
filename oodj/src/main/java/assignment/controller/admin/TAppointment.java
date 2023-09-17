
package assignment.controller.admin;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

import assignment.model.Model;
import assignment.model.types.*;
import assignment.model.models.AppointmentModel;
import assignment.view.View;
import assignment.view.admin.AppointmentManagement;
import javax.swing.JComboBox;

public class TAppointment implements TableController {
  private Model model;
  private View view;
  
  private AppointmentManagement apm;
  private JTable table;
  private DefaultTableModel tableModel;
  private AppointmentModel apptModel;

  public TAppointment(Model m, View v) {
    model = m;
    view = v;

    apm = view.getAdminPanel().getAppointmentManagement();
    apptModel = model.getAppointmentModel();

    setTable();
    apm.addAppointmentTableMouseListener(handleMouseClick());
    apm.addTableAddListener(handleTableAdd());
    apm.addTableEditListener(handleTableModify());
    apm.addTableDeleteListener(handleTableDelete());
    apm.addSearchListener(handleSearch());
    apm.addSearchDocumentListener(handleSearchReset());
  }

  private boolean validateInput(JTextField[] textFields, boolean emptyId) {
    int id, userId, vcnId;
    ArrayList<Appointment> appointments = AppointmentModel.getAppointments();

    try {
      // check types
      id = emptyId ? 0 : Integer.parseInt(textFields[0].getText());
      userId = Integer.parseInt(textFields[1].getText());
      vcnId = Integer.parseInt(textFields[2].getText());

      // check duplicate
      if(emptyId){
        for(int i = 0; i < appointments.size(); i++){
          if(appointments.get(i).getUser().getId() == userId && 
             appointments.get(i).getVaccination().getId() == vcnId) {
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
        table = apm.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        JTextField[] textFields = apm.getTextField();
        JComboBox comboBox = apm.getComboBox();
        int i;
        String data;
        //Get data
        for(i = 0; i < (table.getColumnCount()-1); i++){
          data = tableModel.getValueAt(table.getSelectedRow(), i).toString();
          //Set textfields
          textFields[i].setText(data);
        }
        data = tableModel.getValueAt(table.getSelectedRow(), i).toString();
        
        //Set combo box
        switch(data){
          case Appointment.APPT_ONGOING:
            comboBox.setSelectedItem("Ongoing");
            break;
          case Appointment.APPT_FINISHED:
            comboBox.setSelectedItem("Finished");
            break;
          case Appointment.APPT_CANCELLED:
            comboBox.setSelectedItem("Cancelled");
            break;
        }
      }
    };
  }

  //check if user still has ongoing appointment
  private boolean hasAppointment(User u) {
    ArrayList<Appointment> appt = model.getAppointmentModel().getAppointmentsByUser(u);
    for (int i = 0; i < appt.size(); i++) {
      Appointment a = appt.get(i);
      if (a.getStatus().equals(Appointment.APPT_ONGOING)) return true;
    }
    return false;
  }

  public ActionListener handleTableAdd() {
      return new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
          table = apm.getTable();
          tableModel = (DefaultTableModel) table.getModel();
          JTextField[] textFields = apm.getTextField();
          JComboBox comboBox = apm.getComboBox();

          // validate input
          if(!validateInput(textFields, true)) return;

          int userId = Integer.parseInt(textFields[1].getText()), vcnId = Integer.parseInt(textFields[2].getText());
          String status = "";

          User u = model.getUserModel().getUser(userId);
          
          // check if input user or centre doesn't exist
          if (u == null || model.getVaccinationModel().getVaccination(vcnId) == null) {
            JOptionPane.showMessageDialog(
            null,"User or vaccination centre doesn't exist!",
            "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
          }

          // check if user has existing appointment
          if (hasAppointment(u)) {
            JOptionPane.showMessageDialog(
            null,"User already has ongoing appointment",
            "User has appointment", JOptionPane.ERROR_MESSAGE);
            return;
          }

          // check if user is fully vaccinated
          if (u.getVacStatus().equals(User.VAC_FULL)) {
            JOptionPane.showMessageDialog(
            null,"User is fully vaccinated",
            "User fully vaccinated", JOptionPane.ERROR_MESSAGE);
            return;
          }

          // get appointment status
          switch(comboBox.getSelectedItem().toString()){
            case "Ongoing":
              status = Appointment.APPT_ONGOING;
              break;
            case "Finished":
              status = Appointment.APPT_FINISHED;
              break;
            case "Cancelled":
              status = Appointment.APPT_CANCELLED;
              break;
          }

          apptModel.addAppointment(userId, vcnId, status);

          Appointment a = apptModel.getAppointment(-1);
          String[] newRow = a.toString().split(",");
          tableModel.addRow(newRow);
      }
    };
  }

  public ActionListener handleTableDelete() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        table = apm.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        
        int row = table.getSelectedRow();
        if (row < 0) return;

        JTextField[] textFields = apm.getTextField();
        apptModel.removeAppointment(Integer.parseInt(textFields[0].getText()));
        tableModel.removeRow(row);
      }
    };
  }

  public ActionListener handleTableModify() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        table = apm.getTable();
        tableModel = (DefaultTableModel) table.getModel();
        JTextField[] textFields = apm.getTextField();
        JComboBox comboBox = apm.getComboBox();

        // validate input
        if(!validateInput(textFields, false)) return;

        // get values
        int id = Integer.parseInt(textFields[0].getText()), 
          userId = Integer.parseInt(textFields[1].getText()), 
          vcnId = Integer.parseInt(textFields[2].getText());
        String status = comboBox.getSelectedItem().toString();

        switch(status){
          case "Ongoing":
            status = Appointment.APPT_ONGOING;
            break;
          case "Finished":
            status = Appointment.APPT_FINISHED;
            break;
          case "Cancelled":
            status = Appointment.APPT_CANCELLED;
            break;
        }

        // check if same values
        Appointment a = apptModel.getAppointment(id);
        if (a == null) return;
         
        // check if id is null
        if (model.getUserModel().getUser(userId) == null ||
            model.getVaccinationModel().getVaccination(vcnId) == null) {
          JOptionPane.showMessageDialog(
            null,"User or vaccination centre doesn't exist!",
            "Invalid Input", JOptionPane.ERROR_MESSAGE);
          return;
        }

        apptModel.modifyAppointment(id, userId, vcnId, status);
        
        // set table
        int i;
        for(i = 0; i < textFields.length; i++) {
          tableModel.setValueAt(textFields[i].getText(),table.getSelectedRow(),i);
        }
        
        tableModel.setValueAt(status,table.getSelectedRow(),i);
      }
    };
  }

  private ActionListener handleSearch() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        try {
          String query = apm.getSearchField();
          if (query.trim().isEmpty()) return;

          ArrayList<String[]> results = new ArrayList<>();
          ArrayList<Appointment> appts = AppointmentModel.getAppointments();

          for (int i = 0; i < appts.size(); i++) {
            Appointment a = appts.get(i);
            if (a.toString().matches(String.format(".*%s.*", query))) {
              results.add(a.toString().split(","));
            }
          }

          apm.setTable(new DefaultTableModel(
            results.toArray(new String[0][0]),
            new String[] {
              "Appointment ID", "User ID", "Vaccination ID", "Appointment Status"
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
    apm.setTable(new DefaultTableModel(
      apptModel.toArray(),
      new String[] {
        "Appointment ID", "User ID", "Vaccination ID", "Appointment Status"
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
        if (apm.getSearchField().isEmpty()) setTable();
      }
    };
  }
}
