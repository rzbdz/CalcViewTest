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
    public static final String BACKSPACE = "←";
    public static final String DEVIDE = "÷";
    public static final String TIMES = "×";
    public static final String PLUS = "＋";
    public static final String MINUS = "－";
    public static final String EQUALS = "＝";
    private static BasicOperationPad basicOperationPad;
    public static BasicOperationPad getInstance(){
        if(basicOperationPad==null){
            basicOperationPad = new BasicOperationPad();
        }
        return basicOperationPad;
    }
    private BasicOperationPad() {
        this.buttonClickHandler = new BasicOperationButtonClickHandler();
        setLayout(new GridLayout(6, 1));
        for (String s : FButtonStrings) {
            add(new BasicOperationButton(s, buttonClickHandler));
        }
    }

    private class BasicOperationButton extends JButton {
        BasicOperationButton(String text, ButtonClickHandler handler) {
            super(text);
            setFocusable(false);
            setFont(new BasicFont(Font.PLAIN, 16));
            this.addActionListener(handler);
        }
    }

    /**
     * 在这里编写基本按钮点击的事件处理
     * 需要重写方法:
     * public void actionPerformed(ActionEvent e);
     */
    private class BasicOperationButtonClickHandler extends ButtonClickHandler {
        BasicOperationButtonClickHandler() {
            super();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton jb = (JButton) (e.getSource());
            String text = MainWindow.resultTextField.getText();
            if (jb.getText().equals(BasicOperationPad.BACKSPACE)) {
                if (text.length() == 1) {
                    text = "000";
                } else {
                    text = text.substring(0, text.length() - 1);
                }

            } else {
                text = "you pressed99999999999999 " + jb.getText();
                System.out.println(text);
            }
            MainWindow.resultTextField.setText(text);
        }
    }
}

