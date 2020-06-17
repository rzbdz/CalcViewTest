import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 这里只有 0-9 , +/- , . 按键,具体描述在下面
 */
class NumberPad extends JPanel {
    ButtonClickHandler buttonClickHandler;

    public static final String TURN_POSITIVE_OR_NEGATIVE = "+/-";
    public static final String DOT = ".";

    NumberPad() {
        buttonClickHandler = new NumberButtonClickHandler();
        setLayout(new GridLayout(4, 3));
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
}

/**
 * 在这里编写数字按钮点击的事件处理
 * 需要重写方法:
 * public void actionPerformed(ActionEvent e);
 */
class NumberButtonClickHandler extends ButtonClickHandler{
    NumberButtonClickHandler(){
        super();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton jb = (JButton) (e.getSource());
        String text = "you pressed" + jb.getText();
        System.out.println(text);
        if(jb.getText().equals(NumberPad.TURN_POSITIVE_OR_NEGATIVE)){
            if(MainWindow.resultTextField.getText().charAt(0)=='-'){
                MainWindow.resultTextField.setText(MainWindow.resultTextField.getText().substring(1,MainWindow.resultTextField.getText().length()));
            }else{
                MainWindow.resultTextField.setText("-"+MainWindow.resultTextField.getText());
            }

        }else{
            MainWindow.resultTextField.setText(MainWindow.resultTextField.getText()+jb.getText());
        }

    }


}
