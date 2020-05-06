import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class MainWindow {
    static JTextArea jTextArea = null;
    static class Acl implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            jTextArea.replaceRange("你按下了"+((JButton)e.getSource()).getText(),0,jTextArea.getText().length());
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("计算器");
        frame.setSize(320, 390);
        JPanel jp = new JPanel();
        JButton[] fkButtons = new JButton[14];
        jTextArea = new JTextArea("你好,我是弱智计算器");
        jp.add(jTextArea);
        int i = 1;
        Acl acl = new Acl();
        for (; i <= 10; i++) {
            fkButtons[i - 1] = new JButton(String.valueOf(i));
            fkButtons[i-1].addActionListener(acl);
            jp.add(fkButtons[i - 1]);
        }
        char[] chset = {'+', '-', '×', '÷'};
        i = 10;
        for (char ch : chset) {
            fkButtons[i] = new JButton(String.valueOf(ch));
            jp.add(fkButtons[i]);
        }




        jp.setLayout(new FlowLayout(FlowLayout.LEADING, 3, 3));
        jp.setBackground(new Color(212, 212, 212));
        frame.add(jp);
        frame.setBounds(500, 500, 330, 400);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}


