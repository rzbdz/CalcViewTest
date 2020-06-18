import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main
 */

public class MainWindow {
    static JTextField resultTextField;
    static JTextField processTextField;
    static long memoryNumber;
    static MainGridBagLayoutWindows m;
    public static void main(String[] args) {
        setSystemLNF();
        m = new MainGridBagLayoutWindows();
        resultTextField.addKeyListener(new KeyPressedHandler());
    }

    //获取设置系统样式
    private static void setSystemLNF() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}

class BasicFont extends Font {
    BasicFont(int fontStyle, int size) {
        super("微软雅黑", fontStyle, size);
    }
}

