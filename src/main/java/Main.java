import javax.swing.*;
import renderer.Display;

public class Main {
    public static void openWindow(){
        Display display = new Display();
        display.frame.setTitle(display.title);
        display.frame.add(display);
        display.frame.pack();
        display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.frame.setLocationRelativeTo(null);
        display.frame.setResizable(false);
        display.frame.setVisible(true);
        display.start();
    }

    public static void main(String[] args){
        openWindow();

    }
}
