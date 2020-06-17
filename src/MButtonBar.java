import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 这个类是 M 键,M键的解释是:
 * 计算器里面有一个内存,M就是Memory
 * M+：记忆当前数字，累加数bai字当中。du
 * M-：记忆当前数字，以负zhi数的形式累加数字当中。即：把总数扣dao除当前数字。
 * MC：Memory Clea，也就是将目前记忆的数字「归零」。
 * MR：Memory Recall，将当前计算出来的数字呈现出来。
 * MS：无视目前记忆多少数字，直接以当前数字取代记忆中的数字。
 * 具体可以参照微软计算器的内存栏和M键
 */
class MButtonBar extends JPanel {
    ButtonClickHandler buttonClickHandler;
    static String[] MButtonStrings = {"MC", "MR", "M+", "M-", "MS"};

    MButtonBar() {
        this.buttonClickHandler = new MButtonClickHandler();
        setLayout(new GridLayout(1, 5));
        for (String s : MButtonStrings) {
            add(new MButton(s, buttonClickHandler));
        }
    }

    private class MButton extends JButton {
        MButton(String text, ButtonClickHandler handler) {
            super(text);
            setFocusable(false);
            setFont(new BasicFont(Font.BOLD, 12));
            this.addActionListener(handler);
        }
    }
}

/**
 * 在这里编写M键按钮点击的事件处理
 * 需要重写方法:
 * public void actionPerformed(ActionEvent e);
 */
class MButtonClickHandler extends ButtonClickHandler{
    MButtonClickHandler(){
        super();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton jb = (JButton) (e.getSource());
        String text = "you pressed" + jb.getText();
        System.out.println(text);
        MainWindow.resultTextField.setText(text);
    }
}