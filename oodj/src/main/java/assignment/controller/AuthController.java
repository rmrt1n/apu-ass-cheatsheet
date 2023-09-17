package assignment.controller;

import assignment.model.*;
import assignment.model.types.*;
import assignment.model.models.*;
import assignment.view.*;
import assignment.view.auth.*;
import assignment.view.auth.Auth;
import assignment.controller.user.*;
import assignment.controller.admin.AdminController;

import java.awt.Component;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.time.LocalDate;

import javax.swing.JOptionPane;

public class AuthController {
  public enum UserType {
    U_USER,
    U_ADMIN
  };

  private Model model;
  private View view;
  private UserType userType = UserType.U_USER;
  private Human loggedInPerson;

  private Auth authPanel;
  private SignupForm signup;
  private LoginForm login;

  private UVacStatus sm;
  private UProfileManagement pm;
  private UAppointmentManagement am;

  public AuthController(Model m, View v) {
    model = m;
    view = v;

    authPanel = view.getAuthPanel();
    signup = authPanel.getSignupForm();
    login = authPanel.getLoginForm();

    login.addRegisterHereMouseListener(handleRegisterLoginLink());
    signup.addLoginHereMouseListener(handleRegisterLoginLink());

    login.addCommitteeHereMouseListener(handleCommitteeLink());
    signup.addCommitteeHereMouseListener(handleCommitteeLink());

    login.addSubmitButtonEvent(handleLogin());
    signup.addSubmitButtonEvent(handleRegister());
  }

  public UserType getUserType() {
    return userType;
  }

  private MouseAdapter handleRegisterLoginLink() {
    return new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        authPanel.next();
      }

      // hover effects
      public void mouseEntered(MouseEvent evt) {
        JLabel l = (JLabel)evt.getComponent();
        l.setText(String.format("<html><a href='/'>%s</a></html>", l.getText()));
      }

      public void mouseExited(MouseEvent evt) {
        JLabel l = (JLabel)evt.getComponent();
        String old = l.getText();
        Pattern p = Pattern.compile("<html><a href='/'>(.*)</a></html>");
        Matcher m = p.matcher(old);
        m.find();
        l.setText(m.group(1));
      }
    };
  }

  private MouseAdapter handleCommitteeLink() {
    return new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        // change button listener when changing user to admin, vice versa

        // admin access
        if(userType == UserType.U_USER) {
          String password = JOptionPane.showInputDialog(evt.getComponent(), "Input Committee Password: ");
          if(password != null) {
            if(password.equals("a")) {
              authorise();
            } else {
              JOptionPane.showMessageDialog(evt.getComponent(), "Authorised personnel only");
            }
          }
        } else {
          authorise();
        }
      }

      // hover effects
      public void mouseEntered(MouseEvent evt) {
        JLabel l = (JLabel)evt.getComponent();
        l.setText(String.format("<html><a href='/'>%s</a></html>", l.getText()));
      }

      public void mouseExited(MouseEvent evt) {
        JLabel l = (JLabel)evt.getComponent();
        l.setText(userType == UserType.U_USER ? "Committee" : "User");
      }
    };
  }
  
  private void authorise() {
    userType = userType == UserType.U_USER ? UserType.U_ADMIN : UserType.U_USER;
    login.addSubmitButtonEvent(handleLogin());
    signup.addSubmitButtonEvent(handleRegister());
    
    // set title
    authPanel
        .getLoginForm()
        .setTitle(userType == UserType.U_USER ? "Login" : "Committee Login");
    authPanel
        .getSignupForm()
        .setTitle(userType == UserType.U_USER ? "Sign Up" : "Committee Sign Up");
    
    // set form
    authPanel
        .getSignupForm()
        .toggleCommittee(userType == UserType.U_USER);
  }

  private ActionListener handleLogin() {
    return userType == UserType.U_USER ? handleUserLogin() : handleAdminLogin();
  }

  private ActionListener handleRegister() {
    return userType == UserType.U_USER ? handleUserRegister() : handleAdminRegister();
  }

  private ActionListener handleUserLogin() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        String email = login.getEmail();
        String password = login.getPassword();

        User u = model.getUserModel().verifyUser(email, password);

        if (u != null) {
          loggedInPerson = (Human)u;

          view.setViewUser();
          model.updateModel();
          new UserController(model, view);

          sm = new UVacStatus(model, view, (User)loggedInPerson);
          pm = new UProfileManagement(model, view, (User)loggedInPerson);
          am = new UAppointmentManagement(model, view, (User)loggedInPerson);
        } else {
          JOptionPane.showMessageDialog(
              (Component) evt.getSource(), "Invalid credentials");
        }
      }
    };
  }

  private ActionListener handleAdminLogin() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        String email = login.getEmail();
        String password = login.getPassword();

        Admin a = model.getAdminModel().verifyAdmin(email, password);

        if (a != null) {
          loggedInPerson = (Human)a;
          model.updateModel();
          view.setViewAdmin();
          new AdminController(model, view);
        } else {
          JOptionPane.showMessageDialog((Component) evt.getSource(), "Invalid credentials");
        }
      }
    };
  }

  private boolean verifyUserRegister(String name, String email, String password,
                                  String phone, String birthdate, String gender, 
                                  String local, String ic) {
    // check if input is empty
    if (name.trim().isEmpty()     || email.trim().isEmpty()     ||
        password.trim().isEmpty() || phone.trim().isEmpty()     ||
        birthdate == null         || gender.trim().isEmpty()    ||
        local.trim().isEmpty()    || ic.trim().isEmpty()) {
      JOptionPane.showMessageDialog(
          null, "Input cannot be empty", "Invalid input",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }

    // check invalid "," input
    if (name.contains(",")      || email.contains(",")  || phone.contains(",") ||
        birthdate.contains(",") || gender.contains(",") || local.contains(",") ||
        ic.contains(",")) {
      JOptionPane.showMessageDialog(
          null, "Input cannot contain ','", "Invalid input",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    
    // validate email (https://www.educba.com/java-email-validation/)
    Pattern p = Pattern.compile("^(.+)@(.+)$");
    Matcher m = p.matcher(email);
    if (!m.matches()) {
      JOptionPane.showMessageDialog(
          null, "Please input a valid email", "Invalid email",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }

    // validate phone number (https://java2blog.com/validate-phone-number-java/)
    p = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");
    m = p.matcher(phone); 
    if (!m.find() || !m.group(0).equals(phone)) {
      JOptionPane.showMessageDialog(
          null, "Please input a valid international phone number", "Invalid phone number",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }

    if (LocalDate.parse(birthdate).isAfter(LocalDate.now())) {
      JOptionPane.showMessageDialog(
          null, "Please input a valid birth date", "Invalid date",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }

    return true;
  }

  private ActionListener handleUserRegister() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        String name = signup.getName();
        String email = signup.getEmail();
        String password = signup.getPassword();
        String phone = signup.getPhone();
        String birthdate = signup.getBirthdate();
        String gender = signup.getGender();
        String local = signup.getLocal();
        String ic = signup.getIC();

        // validate input
        if (!verifyUserRegister(name, email, password, phone, birthdate, gender, local, ic)) return;

        User u = UserModel.isRegisteredUser(email);
        if (u != null) {
          JOptionPane.showMessageDialog((Component) evt.getSource(), "User exists");
          return;
        }
        
        // register user
        model.getUserModel().addUser(
            name, email, password, phone, birthdate, gender, local, ic, User.VAC_UNVAC);
        JOptionPane.showMessageDialog((Component) evt.getSource(), "Registration successful!");
      }
    };
  }

  private boolean verifyAdminRegister(String name, String email, String password, String phone) {
        // check if input is empty
    if (name.trim().isEmpty()     ||
        email.trim().isEmpty()    ||
        password.trim().isEmpty() || 
        phone.trim().isEmpty()) {
      JOptionPane.showMessageDialog(
          null, "Input cannot be empty", "Invalid input",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }

    // check invalid "," input
    if (name.contains(",") || email.contains(",") || phone.contains(",")) {
      JOptionPane.showMessageDialog(
          null, "Input cannot contain ','", "Invalid input",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    
    // validate email
    Pattern p = Pattern.compile("^(.+)@(.+)$");
    Matcher m = p.matcher(email);
    if (!m.matches()) {
      JOptionPane.showMessageDialog(
          null, "Please input a valid email", "Invalid email",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }

    // validate phone
    p = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");
    m = p.matcher(phone); 
    m.find();
    if (!m.group().equals(phone)) {
      JOptionPane.showMessageDialog(
          null, "Please input a valid phone number", "Invalid phone number",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }

    return true;
  }

  private ActionListener handleAdminRegister() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        String name = signup.getName();
        String email = signup.getEmail();
        String password = signup.getPassword();
        String phone = signup.getPhone();

        // validate input
        if (!verifyAdminRegister(name, email, password, phone)) return;

        Admin a = AdminModel.isRegisteredAdmin(email);
        if (a != null) {
          JOptionPane.showMessageDialog((Component) evt.getSource(), "Admin exists");
          return;
        }

        // register admin
        model.getAdminModel().addAdmin(name, email, password, phone);
        JOptionPane.showMessageDialog((Component) evt.getSource(), "Admin added");
      }
    };
  }
}
