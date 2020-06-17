import javax.print.Doc;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

class Header extends JPanel {
    JTextField resultTextField;
    JTextField processTextField;

    Header() {
        resultTextField = new JTextField();
        resultTextField.setHorizontalAlignment(JTextField.RIGHT);
        resultTextField.setEditable(false);
        resultTextField.setFocusable(true);
        resultTextField.setFont(new BasicFont(Font.BOLD, 36));
        resultTextField.setBorder(new EmptyBorder(0, 5, 0, 5));
        //构造一个文本监听器,当文本长度长于窗口宽度时调整字体大小
        resultTextField.getDocument().addDocumentListener(new DocumentListener() {
            private void filter(DocumentEvent e) {
                System.out.println(e.getDocument().getLength());
                ChangeFont cf;
                if (e.getDocument().getLength() >= 17) {
                    System.out.println("locked header");
                    cf = new ChangeFont(new BasicFont(Font.BOLD, 24));
                } else {
                    System.out.println("locked header");
                    cf= new ChangeFont(new BasicFont(Font.BOLD, 36));
                }
                EventQueue.invokeLater(cf);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filter(e);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                filter(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filter(e);
            }

        });

        processTextField = new JTextField();
        processTextField.setHorizontalAlignment(JTextField.RIGHT);
        processTextField.setEditable(false);
        processTextField.setFocusable(true);
        processTextField.setFont(new BasicFont(Font.PLAIN, 12));
        processTextField.setForeground(Color.darkGray);
        processTextField.setText("8+5");
        processTextField.setBorder(new EmptyBorder(15, 5, 0, 5));

        GridBagLayout textFieldGridBagLayout = new GridBagLayout();
        setLayout(textFieldGridBagLayout);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 0.1;
        c.gridx = 0;
        c.gridy = 0;
        textFieldGridBagLayout.setConstraints(processTextField, c);
        c.weightx = 1;
        c.weighty = 0.9;
        c.gridx = 0;
        c.gridy = 1;
        textFieldGridBagLayout.setConstraints(resultTextField, c);
        add(processTextField);
        add(resultTextField);
    }

    //为上面的文本监听器服务
    private class ChangeFont implements Runnable {
        Font ft;

        ChangeFont(Font ft) {
            this.ft = ft;
        }

        @Override
        public void run() {
            Header.this.resultTextField.setFont(ft);
        }
    }
}
