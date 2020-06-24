package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

class TextHeaderControl extends JPanel {
    private static TextHeaderControl header;
    private JTextField resultTextField;

    public void setResultText(String text) {
        this.resultTextField.setText(text);
    }

    public void setProcessText(String text) {
        this.processTextField.setText(text);
    }

    private JTextField processTextField;
    private JPopupMenu rightResultPopupMenu;
    private JMenuItem jMenuItemCopy;
    private JMenuItem jMenuItemPaste;
    private JMenuItem jMenuItemSeleteAll;

    public static TextHeaderControl getInstance() {
        if (header == null) {
            header = new TextHeaderControl();
        }
        return header;
    }

    private TextHeaderControl() {
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
        jMenuItemCopy = new JMenuItem("复制");
        jMenuItemPaste = new JMenuItem("粘贴");
        jMenuItemSeleteAll = new JMenuItem("全选");
        jMenuItemSeleteAll.addActionListener(e -> {
            if (e.getSource() == jMenuItemSeleteAll) {
                System.out.println("press select all");
                TextHeaderControl.this.resultTextField.requestFocus();
                TextHeaderControl.this.resultTextField.selectAll();
            }
        });
        rightResultPopupMenu.add(jMenuItemCopy);
        rightResultPopupMenu.add(jMenuItemPaste);
        rightResultPopupMenu.add(jMenuItemSeleteAll);
        //构造鼠标右键监听器
        resultTextField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //Button3就是鼠标右键
                if (e.getButton() == MouseEvent.BUTTON3) {
                    rightResultPopupMenu.show(TextHeaderControl.this, e.getX(), e.getY());
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
                String content = null;
                try {
                    content = e.getDocument().getText(0, e.getDocument().getLength());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                //这里是用来暴力计算结果文本框的字符串对应字体长度从而改变字号的
                //使用Font的getStringBounds的getWidth计算,避免人工暴力设定值,
                //考虑小数点,逗号,符号,数字的宽度不同
                if (content!=""&&content!=null) {
                    Font font = new BasicFont(Font.BOLD, 48);
                    for (int i = 36; font.getStringBounds(content,
                            new FontRenderContext(new AffineTransform(), true, true)).getWidth()
                            + 40 >= resultTextField.getParent().getSize().width; i--) {
                        font = new BasicFont(Font.BOLD, i);
                    }
                    cf = new ChangeFont(font);
                    //稍后执行changeFont操作
                    //这里这样调用是因为使用JTextField进入了Document部分,
                    //JTextField被加锁
                    EventQueue.invokeLater(cf);
                }
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
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        //垂直占比10分之一,水平扩展100%
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        textFieldGridBagLayout.setConstraints(processTextField, gridBagConstraints);
        //垂直占比10分之9,水平扩展100%
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0.9;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        textFieldGridBagLayout.setConstraints(resultTextField, gridBagConstraints);
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
            TextHeaderControl.this.resultTextField.setFont(ft);
        }
    }


}
