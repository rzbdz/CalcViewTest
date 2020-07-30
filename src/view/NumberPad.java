package view;

import controller.CalcController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

/**
 * 这里只有 0-9 , +/- , . 按键,具体描述在下面<br>
 * 单例模式说明{@link CalculatorFrame}<br>
 *<br>
 * 重要成员是内部类{@code NumberButtonClickHandler{}}
 * 负责处理按钮的事件监听<br>
 * 说明:对于这些Panel,不用处理UI的部分了,只需要写那个Handler就好了<br>
 * {@link NumberButtonClickHandler}
 * @see CanTurnErrorState
 */
class NumberPad extends JPanel implements CanTurnErrorState{
    ButtonClickHandler buttonClickHandler;

    public static final String TURN_POSITIVE_OR_NEGATIVE = "+/-";
    public static final String DOT = ".";
    private static NumberPad numberPad;
    public static NumberPad getInstance(){
        if(numberPad==null){
            numberPad = new NumberPad();
        }
        return numberPad;
    }
    private NumberPad() {
        buttonClickHandler = new NumberButtonClickHandler();
        setLayout(new GridLayout(4, 3,1,1));
        for (int i = 7; i >= 1; i -= 3) {
            for (int j = i; j < i + 3; j++)
                add(new NumberButton(String.valueOf(j), buttonClickHandler));
        }
        //这个按钮是用来修改已经按下的数字的正负号的
        add(new NumberButton("+/-", buttonClickHandler));
        //0
        add(new NumberButton("0", buttonClickHandler));
        //小数点
        add(new NumberButton(".", buttonClickHandler));
    }

    @Override
    public void setErrorState(boolean bool) {
        for (Component co : this.getComponents()) {
            if (co instanceof NumberButton) {
                if (((NumberButton) co).getText().contains(DOT)) {
                } else co.setEnabled(!bool);
            }
            //System.out.println(co.getClass().toString());  //得到co的类型

        }
    }
    /**
     * 内部Button类,定义一些属性,尽量不修改
     */
    private class NumberButton extends JButton {
        NumberButton(String text, ButtonClickHandler handler) {
            super(text);
            setFocusable(false);
            setFont(new BasicFont(Font.BOLD, 16));
            this.addActionListener(handler);
        }
    }
    /**
     * 在这里编写数字按钮点击的事件处理
     * 需要重写方法:
     * public void actionPerformed(ActionEvent e);
     */
    private class NumberButtonClickHandler extends ButtonClickHandler{
        NumberButtonClickHandler(){
            super();
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton jb = (JButton) (e.getSource());
            String text = "you pressed" + jb.getText();
            System.out.println(text);
            CalculatorFrame.getInstance().setErrorState(false);
            if (TextHeader.getResultText().equals("0")) {
                TextHeader.setResultText(new BigDecimal(jb.getText()));
            } else
                TextHeader.setResultText(new BigDecimal(TextHeader.getResultText() + jb.getText()));

        }
    }

}

