package view;

import controller.CalcController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
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
class OperationPad extends JPanel implements CanTurnErrorState {
    ButtonClickHandler buttonClickHandler;
    public static final String BACKSPACE = "←";
    public static final String MOD = "%";
    public static final String DIVIDES = "÷";
    public static final String TIMES = "×";
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String EQUALS = "=";
    static String[] FButtonStrings = {BACKSPACE, MOD, DIVIDES, TIMES, MINUS, PLUS, EQUALS};
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

    @Override
    public void setErrorState(boolean bool) {

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
            String text = TextHeader.getResultText();
            if (jb.getText().equals(OperationPad.BACKSPACE)) {
                if (text.length() <= 1) {
                    text = "0";
                } else {
                    var d = TextHeader.getResultTextDecimal();
                    text = d.toString().substring(0,d.toString().length()-1);
                }
                TextHeader.setResultText(new BigDecimal(text));
            } else if (jb.getText().equals(OperationPad.EQUALS)) {
                if(TextHeader.getExpressionText()!=null&&! TextHeader.getExpressionText().equals("")){
                    CalcController c = CalcController.getInstance();
                    try{
                        c.updateModel(TextHeader.getExpressionText());
                        TextHeader.setExpressionText(TextHeader.getExpressionText()+'=');
                        TextHeader.setResultText(c.updateView());
                    }catch (Exception controllerException){
                        TextHeader.setResultText(controllerException.getMessage());
                        CalculatorFrame.getInstance().setErrorState(true);
                    }
                }

            } else {
                text = "you pressed99999999999999 " + jb.getText();
                System.out.println(text);
                TextHeader.setExpressionText(TextHeader.getExpressionText()+jb.getText());
            }
        }
    }
}

