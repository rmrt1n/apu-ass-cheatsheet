package assignment;

//import javax.swing.UIManager;
import assignment.controller.Controller;
import assignment.model.Model;
import assignment.view.View;

public class App {
  public static View appgui = new View();
  public static Model model = new Model();
  public static Controller controller = new Controller(model, appgui);

  public static void main(String[] args) {
    /* Create and display app */
    java.awt.EventQueue.invokeLater(
      new Runnable() {
        public void run() {
          appgui.setVisible(true);
        }
      }
    );
  }
}
