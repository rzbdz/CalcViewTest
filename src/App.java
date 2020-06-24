import view.CalculatorFrame;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        setSystemLooksAndFeels();
        CalculatorFrame calculatorFrame = CalculatorFrame.getInstance();
    }

    //获取设置系统样式
    private static void setSystemLooksAndFeels() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
