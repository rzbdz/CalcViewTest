package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

class KeyPressedHandler implements KeyListener {

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

}

