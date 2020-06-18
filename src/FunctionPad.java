import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 这个Function 写的是除了加减乘除等之外的按键
 * 具体见字符串数组
 */
class FunctionPad extends JPanel {
    ButtonClickHandler buttonClickHandler;
    static String[] FButtonStrings = {"%", "CE", "C"};
    static String[] FButtonStringsWithX = {"1/x", "x²", "√x"};

    public static final String MOD = "%";
    public static final String CE = "CE";
    public static final String C = "C";
    public static final String ONE_DEVIDE_X = "1/x";
    public static final String X_SQURE = "x²";
    public static final String SQRT = "√x";
    private static FunctionPad functionPad;
    public  static  FunctionPad getInstance(){
        if(functionPad == null)
            functionPad = new FunctionPad();
        return functionPad;
    }
    private FunctionPad() {
        this.buttonClickHandler = new FunctionButtonClickHandler();
        setLayout(new GridLayout(2, 3));
        //普通字,使用默认字体
        for (String s : FButtonStrings) {
            add(new FunctionButton(s, buttonClickHandler));
        }
        //含x字,使用公式字体
        for (String s : FButtonStringsWithX) {
            add(new FunctionButton(s, buttonClickHandler, new Font("Times New Roman", Font.ITALIC, 16)));
        }
    }

    private class FunctionButton extends JButton {
        FunctionButton(String text, ButtonClickHandler handler) {
            super(text);
            setFocusable(false);
            setFont(new BasicFont(Font.PLAIN, 16));
            this.addActionListener(handler);
        }
        FunctionButton(String text, ButtonClickHandler handler, Font font) {
            super(text);
            setFocusable(false);
            setFont(font);
            this.addActionListener(handler);
        }
    }
    /**
     * 在这里编写功能按钮点击的事件处理
     * 需要重写方法:
     * public void actionPerformed(ActionEvent e);
     */
    private class FunctionButtonClickHandler extends ButtonClickHandler {

        FunctionButtonClickHandler() {
            super();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton jb = (JButton) (e.getSource());
            String text = "you pressed" + jb.getText();
            System.out.println(text);
            if (jb.getText().equals(FunctionPad.CE) || jb.getText().equals(FunctionPad.C)) {
                text = "000";
            }
            MainWindow.resultTextField.setText(text);
        }
    }
}

