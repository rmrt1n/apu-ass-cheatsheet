package assignment.view.auth;

import java.awt.CardLayout;
import javax.swing.JPanel;

public class Auth extends JPanel {
  private LoginForm loginForm;
  private SignupForm signupForm;
  private CardLayout card;

  public Auth() {
    this.loginForm = new LoginForm();
    this.signupForm = new SignupForm();
    this.card = new CardLayout();

    this.setLayout(this.card);
    this.add(loginForm);
    this.add(signupForm);
  }

  public LoginForm getLoginForm() {
    return loginForm;
  }

  public SignupForm getSignupForm() {
    return signupForm;
  }

  public void next() {
    this.card.next(this);
  }
}
