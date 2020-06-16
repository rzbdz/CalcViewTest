import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 这里就是普通的退格加减乘除等
 * 值得注意的是:
 * 这里用了数学符号,和直接的=+-不一样,
 * 比较时要复制字符串数组的符号,或者改成直接的-=+
 */
class BasicOperationPad extends JPanel {
    ButtonClickHandler buttonClickHandler;
    static String[] FButtonStrings = {"←", "÷", "×", "－", "＋", "＝"};

    BasicOperationPad() {
        this.buttonClickHandler = new BasicOperationButtonClickHandler();
        setLayout(new GridLayout(6, 1));
        for (String s : FButtonStrings) {
            add(new BasicOperationButton(s, buttonClickHandler));
        }
    }

    private class BasicOperationButton extends JButton {
        BasicOperationButton(String text, ButtonClickHandler handler) {
            super(text);
            setFont(new BasicFont(Font.PLAIN, 18));
            this.addActionListener(handler);
        }
    }
}

/**
 * 在这里编写基本按钮点击的事件处理
 * 需要重写方法:
 * public void actionPerformed(ActionEvent e);
 */
class BasicOperationButtonClickHandler extends ButtonClickHandler{
    BasicOperationButtonClickHandler(){
        super();
    }
}