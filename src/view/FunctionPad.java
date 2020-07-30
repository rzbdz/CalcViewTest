package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

/**
 * 这个Function 写的是除了加减乘除等之外的按键<br>
 * 具体见字符串数组<br>
 * 单例模式说明{@link CalculatorFrame}<br>
 * <br>
 * 重要成员是内部类{@code FunctionButtonClickHandler{}},
 * 负责处理按钮的事件监听.<br>
 * 说明:对于这些Panel,不用处理UI的部分了,只需要写那个Handler就好了.<br>
 * 重要成员二是方法{@code setErrorState(boolean)},这个见参考
 * 实现的接口.<br>
 * @see CanTurnErrorState
 */
class FunctionPad extends JPanel implements CanTurnErrorState {
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

    /**
     * 单例模式获取实例
     * @return 返回FunctionPad的一个实例
     * @see CalculatorFrame
     */
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
        add(new FunctionButton(ABS, buttonClickHandler,
                new Font("Times New Roman", Font.ITALIC, 16)));
    }

    @Override
    /**
     * <b>重要成员</b>
     * 实现{@link CanTurnErrorState}接口的方法
     * @see CanTurnErrorState
     */
    public void setErrorState(boolean bool) {
        //获取全部的components
        for (Component component : this.getComponents()) {
            //对于是按钮的组件
            if (component instanceof FunctionButton) {
                //这里就排除一些不变灰的按钮,让能变灰的都变灰
                //这里主要是参考windows的标准计算器的样子来搞的
                if (((FunctionButton) component).getText().contains(C) ||
                        ((FunctionButton) component).getText().contains(CE)) {
                } else component.setEnabled(!bool);
            }
            //Debug code
            //System.out.println(component.getClass().toString());

        }
    }

    /**
     * 内部样式类,
     * 主要继承JButton和修改一些样式
     */
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
     * <b>重要成员</b>
     * 在这里编写功能按钮点击的事件处理
     * 需要重写方法:
     * public void actionPerformed(ActionEvent e);
     */
    private static class FunctionButtonClickHandler extends ButtonClickHandler implements CanTurnErrorState{

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
                FunctionPad.getInstance().setErrorState(false);
            } else if (jb.getText().equals(FunctionPad.C)) {
                //C
                TextHeader.setResultText(new BigDecimal("0"));
                TextHeader.setExpressionText("");
                CalculatorFrame.getInstance().setErrorState(false);
            } else if (jb.getText().equals(FunctionPad.LEFT_BRACKET)) {
            }
        }

        @Override
        public void setErrorState(boolean bool) {

        }
    }
}

