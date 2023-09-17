package assignment.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowListener;
import java.awt.Toolkit;

import javax.swing.GroupLayout;
import javax.swing.JFrame;

import assignment.view.admin.Admin;
import assignment.view.user.User;
import assignment.view.auth.Auth;
import assignment.view.user.NavigationPane;

public class View extends JFrame {
  private Auth authPanel = new Auth();
  private Admin adminPanel;
  private User userPanel;
  
  private GroupLayout userDashboardLayout;
  private NavigationPane userDashboardContainer = new NavigationPane();

  public View() {
    // Set app attributes
    setTitle("Vaccination Registration System");
    setBackground(Color.decode("#e5e5e5"));
    setSize(1280, 720);
    setPreferredSize(new Dimension(1280, 720));
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/assets/app64.png")));

    userDashboardLayout = new GroupLayout(getContentPane());
    userDashboardLayout.setHorizontalGroup(
        userDashboardLayout
            .createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(
                userDashboardLayout
                    .createSequentialGroup()
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(
                        userDashboardContainer,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    userDashboardLayout.setVerticalGroup(
        userDashboardLayout
            .createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(
                userDashboardLayout
                    .createSequentialGroup()
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(
                        userDashboardContainer,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

    getContentPane().add(authPanel);
  }

  public Auth getAuthPanel() {
    return authPanel;
  }

  public Admin getAdminPanel() {
    return adminPanel;
  }

  public User getUserPanel() {
    return userPanel;
  }

  public void setViewAdmin() {
    adminPanel = new Admin();
    getContentPane().remove(authPanel);
    getContentPane().add(adminPanel);
    revalidate();
    repaint();
  }

  public void setViewUser() {
    userPanel = new User();
    getContentPane().remove(authPanel);
    getContentPane().add(userPanel);
    revalidate();
    repaint();
  }

  public void logoutUser() {
    getContentPane().remove(userPanel);
    getContentPane().add(authPanel);
    revalidate();
    repaint();
  }

  public void logoutAdmin() {
    getContentPane().remove(adminPanel);
    getContentPane().add(authPanel);
    revalidate();
    repaint();
  }

  public void addNewWindowListener(WindowListener l) {
    addWindowListener(l);
  }

}
