package assignment.controller.user;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

import assignment.model.Model;
import assignment.model.types.User;
import assignment.view.View;
import assignment.view.user.ProfileManagement;

public class UProfileManagement {
  private Model model;
  private View view;
  private User user;
  private ProfileManagement pm;

  public UProfileManagement(Model m, View v, User u) {
    model = m;
    view = v;
    user = u;
    pm = view.getUserPanel().getProfileManagement();

    setDetails();
    pm.addBtnListener(handleModify());
  }

  // set user profile textfields
  private void setDetails() {
    pm.setWelcome(user.getName());
    pm.setDetails(
      user.getName(),
      user.getEmail(),
      user.getPhone(),
      user.getBirthdate().toString(),
      user.getGender().equals(User.M) ? "Male" : "Female",
      user.getIC()
    );
  }

  private String[] validateInput(JTextField[] textFields, boolean emptyId) {
    // check types
    String email = textFields[0].getText();
    String password = textFields[1].getText();
    String phone = textFields[2].getText();

      // check strings
      if (email.trim().isEmpty() || phone.trim().isEmpty())  {
        JOptionPane.showMessageDialog(null, "Input cannot be empty!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        return null;
      }

      if (email.contains(",") || phone.contains(",")) {
        JOptionPane.showMessageDialog(null, "Input cannot contain ','!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        return null;
      }

    return new String[]{ email, password, phone };
  }

  private ActionListener handleModify() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        JTextField[] textFields = pm.getTextField();

        // validate input
        String[] s = validateInput(textFields, false);
        if (s == null) return;

        // get values
        model.getUserModel().modifyUser(
          user.getId(), user.getName(), s[0], s[1], s[2], user.getBirthdate().toString(),
          user.getGender(), user.getLocal().toString(), user.getIC(),
          user.getVacStatus().toString());

        // modify text fields
        for(int i = 0; i < textFields.length; i++) {
          textFields[i].setText(s[i]);
        }
        JOptionPane.showMessageDialog(null, "Successfully updated details");
      }
    };
  }

}
