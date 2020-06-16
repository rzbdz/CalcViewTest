import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 这个类就是ButtonClickHandler
 */
class ButtonClickHandler implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton jb = (JButton) (e.getSource());
        System.out.println("youp ress" + jb.getText());
    }
}
