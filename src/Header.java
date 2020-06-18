import javax.print.Doc;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

class Header extends JPanel {
    private static Header header;
    private JTextField resultTextField;

    public JTextField getResultTextField() {
        return resultTextField;
    }

    public JTextField getProcessTextField() {
        return processTextField;
    }

    private JTextField processTextField;
    private JPopupMenu rightResultPopupMenu;
    private JMenuItem jmitCopy;
    private JMenuItem jmitPaste;
    private JMenuItem jmitSeleteAll;
    public static Header getInstance(){
        if(header==null){
            header = new Header();
        }
        return header;
    }
    private Header() {
        //显示算式的文本框
        processTextField = new JTextField();
        processTextField.setHorizontalAlignment(JTextField.RIGHT);
        processTextField.setEditable(false);
        processTextField.setFocusable(true);
        processTextField.setFont(new BasicFont(Font.PLAIN, 12));
        processTextField.setForeground(Color.gray);
        processTextField.setText("8+5");
        processTextField.setBorder(new EmptyBorder(15, 5, 0, 5));
        //显示当前按下数字和计算结果的文本框
        resultTextField = new JTextField();
        resultTextField.setHorizontalAlignment(JTextField.RIGHT);
        resultTextField.setEditable(false);
        resultTextField.setFocusable(true);
        resultTextField.setFont(new BasicFont(Font.BOLD, 36));
        resultTextField.setBorder(new EmptyBorder(0, 5, 0, 5));
        //定义这个文本框的右键弹出菜单
        rightResultPopupMenu = new JPopupMenu();
        jmitCopy = new JMenuItem("复制");
        jmitPaste = new JMenuItem("粘贴");
        jmitSeleteAll = new JMenuItem("全选");
        jmitSeleteAll.addActionListener(e -> {
            if (e.getSource() == jmitSeleteAll) {
                System.out.println("press select all");
                Header.this.resultTextField.requestFocus();
                Header.this.resultTextField.selectAll();
            }
        });
        rightResultPopupMenu.add(jmitCopy);
        rightResultPopupMenu.add(jmitPaste);
        rightResultPopupMenu.add(jmitSeleteAll);
        //构造鼠标右键监听器
        resultTextField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //Button3就是鼠标右键
                if (e.getButton() == MouseEvent.BUTTON3) {
                    rightResultPopupMenu.show(Header.this, e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        //构造一个文本监听器,当文本长度长于窗口宽度时调整字体大小
        resultTextField.getDocument().addDocumentListener(new DocumentListener() {
            private void filter(DocumentEvent e) {
                ChangeFont cf;
                String content;
                try {
                    content = e.getDocument().getText(0, e.getDocument().getLength());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    content = "";
                }
                //这里是用来暴力计算结果文本框的字符串对应字体长度从而改变字号的
                //使用Font的getStringBounds的getWidth计算,避免人工暴力设定值,
                //考虑小数点,逗号,符号,数字的宽度不同
                Font font = new BasicFont(Font.BOLD,48);
                for(int i = 36;font.getStringBounds(content,
                        new FontRenderContext(new AffineTransform(), true, true)).getWidth()
                        +40>=MainGridBagLayoutWindows.windowWidth;i--){
                    font = new BasicFont(Font.BOLD,i);
                }
                cf = new ChangeFont(font);
                //稍后执行changeFont操作
                //这里这样调用是因为使用JTextField进入了Document部分,
                //JTextField被加锁
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

        //排版两个文本框的GridBagLayout
        GridBagLayout textFieldGridBagLayout = new GridBagLayout();
        setLayout(textFieldGridBagLayout);
        GridBagConstraints c = new GridBagConstraints();
        //垂直占比10分之一,水平扩展100%
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 0.1;
        c.gridx = 0;
        c.gridy = 0;
        textFieldGridBagLayout.setConstraints(processTextField, c);
        //垂直占比10分之9,水平扩展100%
        c.weightx = 1;
        c.weighty = 0.9;
        c.gridx = 0;
        c.gridy = 1;
        textFieldGridBagLayout.setConstraints(resultTextField, c);
        //添加两个文本框
        add(processTextField);
        add(resultTextField);
    }

    //为上面的文本监听器服务,稍后执行的Runable接口实现
    //用来修改resultTextField的文字大小
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
