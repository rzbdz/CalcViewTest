import view.CalculatorFrame;
import view.MemoryToolFrame;

import javax.swing.*;

/**
 * 程序的入口
 * 处理程序的启动流程(与程序逻辑,交互无关的操作)
 * 这里只用来创建了一个计算器JFrame出口, 并显示出来
 */
public class App {
    public static void main(String[] args) {
        setSystemLooksAndFeels();
        CalculatorFrame calculatorFrame = CalculatorFrame.getInstance();
        calculatorFrame.setVisible(true);
    }

    /**
     * 设置程序界面LooksAndFeels为操作系统默认
     */
    private static void setSystemLooksAndFeels() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
