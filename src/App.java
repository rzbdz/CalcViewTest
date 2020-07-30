import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import view.CalculatorFrame;

import javax.swing.*;

import static view.Theme.setDarkLooksAndFeels;
import static view.Theme.setLightLooksAndFeels;

/**
 * 程序的入口
 * 处理程序的启动流程(与程序逻辑,交互无关的操作)
 * 这里只用来创建了一个计算器JFrame出口, 并显示出来
 *
 * ****开源组件声明:***********************************
 * ****(1)com.formdev.flatlaf(Apache-2.0 License)*****
 * **************https://www.formdev.com/flatlaf/*****
 * ***************************************************
 */
public class App {
    public static void main(String[] args) {
        setDarkLooksAndFeels();
        CalculatorFrame calculatorFrame = CalculatorFrame.getInstance();
        calculatorFrame.setVisible(true);
    }

}
