import javax.swing.*;
import java.awt.*;

class Header extends JPanel {
    JTextField jTextField;

    Header() {
        jTextField = new JTextField();
        jTextField.setHorizontalAlignment(JTextField.RIGHT);
        jTextField.setEditable(false);
        jTextField.setFocusable(true);
        jTextField.setFont(new BasicFont(Font.BOLD, 36));
        setLayout(new BorderLayout());
        add(jTextField);
    }
}
