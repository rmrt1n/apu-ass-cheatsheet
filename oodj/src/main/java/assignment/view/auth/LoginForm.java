/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment.view.auth;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

/** @author rmrt1n */
public class LoginForm extends JPanel {

  /** Creates new form NewJPanel */
  public LoginForm() {
    initComponents();
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT
   * modify this code. The content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        formContainer = new javax.swing.JPanel();
        formCard = new javax.swing.JPanel();
        inputPassword = new javax.swing.JPasswordField();
        title = new javax.swing.JLabel();
        inputEmail = new javax.swing.JTextField();
        submitButton = new javax.swing.JButton();
        emailAddress = new javax.swing.JLabel();
        password = new javax.swing.JLabel();
        registerHere = new javax.swing.JLabel();
        committeeHere = new javax.swing.JLabel();

        jMenu1.setText("jMenu1");

        setPreferredSize(new java.awt.Dimension(1280, 720));

        formContainer.setBackground(new java.awt.Color(255, 255, 255));
        formContainer.setPreferredSize(new java.awt.Dimension(375, 575));

        formCard.setOpaque(false);

        inputPassword.setPreferredSize(new java.awt.Dimension(95, 25));

        title.setFont(new java.awt.Font("Cantarell", 1, 24)); // NOI18N
        title.setForeground(new java.awt.Color(79, 79, 79));
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("Log in");

        submitButton.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        submitButton.setForeground(new java.awt.Color(79, 79, 79));
        submitButton.setText("Log in");
        submitButton.setToolTipText("");
        submitButton.setMinimumSize(new java.awt.Dimension(23, 25));
        submitButton.setPreferredSize(new java.awt.Dimension(95, 25));

        emailAddress.setForeground(new java.awt.Color(79, 79, 79));
        emailAddress.setText("Email address");

        password.setForeground(new java.awt.Color(79, 79, 79));
        password.setText("Password");

        registerHere.setForeground(new java.awt.Color(51, 51, 255));
        registerHere.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        registerHere.setText("Haven't registered yet? Sign up here");

        committeeHere.setForeground(new java.awt.Color(0, 51, 255));
        committeeHere.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        committeeHere.setText("Committee");

        javax.swing.GroupLayout formCardLayout = new javax.swing.GroupLayout(formCard);
        formCard.setLayout(formCardLayout);
        formCardLayout.setHorizontalGroup(
            formCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formCardLayout.createSequentialGroup()
                .addGroup(formCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(title, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inputPassword, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputEmail, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(emailAddress, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(password, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(submitButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(registerHere, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(committeeHere, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        formCardLayout.setVerticalGroup(
            formCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formCardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title)
                .addGap(30, 30, 30)
                .addComponent(emailAddress)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(password)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(submitButton, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(registerHere)
                .addGap(112, 112, 112)
                .addComponent(committeeHere)
                .addContainerGap())
        );

        javax.swing.GroupLayout formContainerLayout = new javax.swing.GroupLayout(formContainer);
        formContainer.setLayout(formContainerLayout);
        formContainerLayout.setHorizontalGroup(
            formContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formContainerLayout.createSequentialGroup()
                .addContainerGap(65, Short.MAX_VALUE)
                .addComponent(formCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71))
        );
        formContainerLayout.setVerticalGroup(
            formContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formContainerLayout.createSequentialGroup()
                .addContainerGap(62, Short.MAX_VALUE)
                .addComponent(formCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(450, Short.MAX_VALUE)
                .addComponent(formContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(455, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(65, Short.MAX_VALUE)
                .addComponent(formContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

  // controller actions here
  public void addRegisterHereMouseListener(MouseListener l) {
    registerHere.addMouseListener(l);
  }

  public void addCommitteeHereMouseListener(MouseListener l) {
    committeeHere.addMouseListener(l);
  }

  public void addSubmitButtonEvent(java.awt.event.ActionListener l) {
    // remove existing listeners first
    ActionListener[] listeners = submitButton.getActionListeners();
    for (ActionListener ls : listeners) submitButton.removeActionListener(ls);
    submitButton.addActionListener(l);
  }

  // getter setter here
  public void setTitle(String newTitle) {
    this.title.setText(newTitle);
  }

  public void setCommitteeUserHere(String newTitle) {
    this.committeeHere.setText(newTitle);
  }

  public String getEmail() {
    return this.inputEmail.getText();
  }

  public String getPassword() {
    return String.valueOf(this.inputPassword.getPassword());
  }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel committeeHere;
    private javax.swing.JLabel emailAddress;
    private javax.swing.JPanel formCard;
    private javax.swing.JPanel formContainer;
    private javax.swing.JTextField inputEmail;
    private javax.swing.JPasswordField inputPassword;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JLabel password;
    private javax.swing.JLabel registerHere;
    private javax.swing.JButton submitButton;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
