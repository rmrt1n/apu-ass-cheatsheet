package assignment.controller;

import java.awt.event.*;
import assignment.model.Model;
import assignment.view.View;

public class Controller {
  private Model model;
  private View view;

  public enum AuthActivityType {
    A_SIGN_UP,
    A_SIGN_IN
  };

  private AuthActivityType authActivity = AuthActivityType.A_SIGN_IN;
  private boolean isLoggedIn = false;
  private AuthController authController;

  public Controller(Model m, View v) {
    this.model = m;
    this.view = v;

    this.authController = new AuthController(this.model, this.view);

    // write to file when app is closed
    this.view.addNewWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        model.writeFile();
      }
    });
  }
}
