package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

/**
 * 这个Function 写的是除了加减乘除等之外的按键
 * 具体见字符串数组
 */
class FunctionPad extends JPanel {
    ButtonClickHandler buttonClickHandler;
    public static final String X_Y = "xʸ";
    public static final String CE = "CE";
    public static final String C = "C";
    public static final String ONE_DIVIDES_X = "1/x";
    public static final String X_SQUARE = "x²";
    public static final String SQRT = "√x";
    public static final String LEFT_BRACKET = "(";
    public static final String RIGHT_BRACKET = ")";
    public static final String ABS = "|x|";
    static String[] FButtonStrings = {CE, C};
    static String[] FButtonStringsWithX = {X_Y, X_SQUARE, SQRT};

    private static FunctionPad functionPad;

    public static FunctionPad getInstance() {
        if (functionPad == null)
            functionPad = new FunctionPad();
        return functionPad;
    }

    private FunctionPad() {
        this.buttonClickHandler = new FunctionButtonClickHandler();
        setLayout(new GridLayout(3, 3));
        add(new FunctionButton(ONE_DIVIDES_X, buttonClickHandler, new Font("Times New Roman", Font.ITALIC, 16)));
        //普通字,使用默认字体
        for (String s : FButtonStrings) {
            add(new FunctionButton(s, buttonClickHandler));
        }
        //含x字,使用公式字体
        for (String s : FButtonStringsWithX) {
            add(new FunctionButton(s, buttonClickHandler, new Font("Times New Roman", Font.ITALIC, 16)));
        }
        add(new FunctionButton(LEFT_BRACKET, buttonClickHandler));
        add(new FunctionButton(RIGHT_BRACKET, buttonClickHandler));
        add(new FunctionButton(ABS, buttonClickHandler, new Font("Times New Roman", Font.ITALIC, 16)));
    }

    private static class FunctionButton extends JButton {
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
    private static class FunctionButtonClickHandler extends ButtonClickHandler {

        FunctionButtonClickHandler() {
            super();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton jb = (JButton) (e.getSource());
            String text = "you pressed" + jb.getText();
            System.out.println(text);
            if (jb.getText().equals(FunctionPad.CE)) {
                //CE
                TextHeader.setResultText(new BigDecimal("0"));
            } else if (jb.getText().equals(FunctionPad.C)) {
                //C
                TextHeader.setResultText(new BigDecimal("0"));
                TextHeader.setExpressionText("");
            }
        }
    }
}

