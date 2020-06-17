import com.sun.tools.javac.Main;

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
        MainWindow.resultTextField.setText("you press" + e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

}

class ResizeListener extends ComponentAdapter{

    @Override
    public void componentResized(ComponentEvent e) {
        Dimension d = (e.getComponent()).getSize();
        if(d.height<MainGridBagLayoutWindows.windowHeight||d.width<MainGridBagLayoutWindows.windowWidth){
            ((JFrame)(e.getComponent())).setResizable(false);
            (e.getComponent()).setSize(MainGridBagLayoutWindows.windowWidth,MainGridBagLayoutWindows.windowHeight);
            ((JFrame)(e.getComponent())).setResizable(true);
        }
    }
}