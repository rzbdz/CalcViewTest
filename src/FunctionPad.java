import javax.swing.*;
import java.awt.*;

/**
 * 这个Function 写的是除了加减乘除等之外的按键
 * 具体见字符串数组
 */
class FunctionPad extends JPanel {
    ButtonClickHandler buttonClickHandler;
    static String[] FButtonStrings = {"%", "CE", "C", "1/x", "x^2", "√x"};

    FunctionPad() {
        this.buttonClickHandler = new FunctionButtonClickHandler();
        setLayout(new GridLayout(2, 3));
        for (String s : FButtonStrings) {
            add(new FunctionButton(s, buttonClickHandler));
        }
    }

    private class FunctionButton extends JButton {
        FunctionButton(String text, ButtonClickHandler handler) {
            super(text);
            setFont(new BasicFont(Font.PLAIN, 18));
            this.addActionListener(handler);
        }
    }
}

/**
 * 在这里编写功能按钮点击的事件处理
 * 需要重写方法:
 * public void actionPerformed(ActionEvent e);
 */
class FunctionButtonClickHandler extends ButtonClickHandler{
    FunctionButtonClickHandler(){
        super();
    }
}