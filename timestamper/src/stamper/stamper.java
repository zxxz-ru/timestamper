/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stamper;

/**
 *
 * @author zxxz
 */
public class stamper {

  public static void main(String[] argd) {
    MainFrame frame = new MainFrame();
    java.awt.EventQueue.invokeLater(new Runnable() {

      @Override
      public void run() {
        new MainFrame().setVisible(true);
      }
    });
  }//main
}
