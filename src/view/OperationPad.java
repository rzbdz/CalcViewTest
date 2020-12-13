package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

/**
 * 这里就是普通的退格加减乘除等<br>
 * 值得注意的是:<br>
 * 这里用了数学符号,和直接的=+-不一样,
 * 比较时要用常量,常量举例:{@link OperationPad#BACKSPACE},
 * 单例模式说明{@link CalculatorFrame}<br><br>
 * 重要成员是内部类{@code BasicOperationButtonClickHandler{}}
 * 负责处理按钮的事件监听<br>
 * 说明:对于这些Panel,不用处理UI的部分了,只需要写那个Handler就好了
 * {@link BasicOperationButtonClickHandler}
 */
class OperationPad extends JPanel implements CanTurnErrorState,CanSimulateKeyboard {
    ButtonClickHandler buttonClickHandler;
    public static final String BACKSPACE = "←";
    public static final String PERCENT = "%";
    public static final String DIVIDES = "÷";
    public static final String TIMES = "×";
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String EQUALS = "=";
    static String[] FButtonStrings = {BACKSPACE, PERCENT, DIVIDES, TIMES, MINUS, PLUS, EQUALS};
    private static OperationPad basicOperationPad;

    public static OperationPad getInstance() {
        if (basicOperationPad == null) {
            basicOperationPad = new OperationPad();
        }
        return basicOperationPad;
    }

    private OperationPad() {
        this.buttonClickHandler = new BasicOperationButtonClickHandler();
        setLayout(new GridLayout(7, 1, 1, 1));
        for (String s : FButtonStrings) {
            add(new BasicOperationButton(s, buttonClickHandler));
        }

    }
    private boolean isError = false;

    @Override
    public boolean isErrorState() {
        return isError;
    }

    @Override
    public void setErrorState(boolean bool) {
        //获取全部的components
        for (Component component : this.getComponents()) {
            //对于是按钮的组件
            if (component instanceof BasicOperationButton) {
                //这里主要是参考windows的标准计算器的样子来搞的
                component.setEnabled(!bool);
            }
            //Debug code
            //System.out.println(component.getClass().toString());
        }
    }

    @Override
    public void simulatePressed(String text) {
        for(var c : this.getComponents()){
            if(c instanceof OperationPad.BasicOperationButton){
                if(((BasicOperationButton) c).getText().contains(text)){
                    ((BasicOperationButton) c).doClick();
                    System.out.println("Operation Simulated: "+((BasicOperationButton) c).getText());
                }
            }
        }
    }

    private static class BasicOperationButton extends JButton {
        BasicOperationButton(String text, ButtonClickHandler handler) {
            super(text);
            setFont(new BasicFont(Font.PLAIN, 16));
            if (Objects.equals(text, OperationPad.EQUALS)) {
                setBackground(new Color(97, 159, 211));
                setForeground(Color.BLACK);
            }
            setFocusable(false);
            this.addActionListener(handler);
        }
    }

    /**
     * 在这里编写基本按钮点击的事件处理
     * 需要重写方法:
     * public void actionPerformed(ActionEvent e);
     */
    private static class BasicOperationButtonClickHandler extends ButtonClickHandler {
        BasicOperationButtonClickHandler() {
            super();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton jb = (JButton) (e.getSource());
            switch (jb.getText()) {
                case OperationPad.EQUALS:
                    TextHeader.getInstance().turnEqualState();
                    break;
                case OperationPad.BACKSPACE:
                    TextHeader.getInstance().backSpace();
                    break;
                case OperationPad.DIVIDES:
                case OperationPad.MINUS:
                case OperationPad.PLUS:
                case OperationPad.TIMES:
                case OperationPad.PERCENT:
                    TextHeader.getInstance().updatePostOperator(jb.getText());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + jb.getText());
            }
        }
    }
}

