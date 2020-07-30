
package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * <p>
 * 仍然是单例模式,仍然是JPanel<br>
 * 更多资讯查看see also</p>
 * <p>
 * 这里有两个文本框一个数据类型,<br>
 * 一个是用来搞表达式的(UI上面的那行8+5);<br>
 * 一个是用来搞结果和当前input 数字的(UI大粗体那行9999);<br>
 * 一个数据类型是{@link TextHeader#decimal}<br></p>
 * <p>
 * 要关注的的是几个Java bean(主要是给controller来调用的):<br>
 * <p>
 * 一个是<b>resultText</b>和<b>decimal</b><br>
 * {@link TextHeader#setResultText(BigDecimal)}<br>
 * {@link TextHeader#getResultText()}<br>
 * {@link TextHeader#getResultTextDecimal()}<br>
 * 这里搞一个decimal是因为,text是需要进行格式化的<br>
 * e.g. 9,999,999,000.123 对应的decimal是9999999000123<br>
 * 实际运算里,是运算的decimal,text只是用来展示,和退格处理,
 * 所以说退格处理里面要同时修改decimal(使用{@code setResultText(BigDecimal)}就很安全)<br></p><p>
 * 然后是<b>expressionText</b><br>
 * {@link TextHeader#getExpressionText()}<br>
 * {@link TextHeader#setExpressionText(String)}<br>
 * 这里是用getter和setter来修改文本的</p><p>
 * 主要的ui事件都写好了,重点还是在别的地方({@link NumberPad},
 * {@link OperationPad},{@link FunctionPad}等)
 * 调用获得instance,然后<b>传入updateView给上面说的几个java bean方法</b><br>
 * </p>
 * <p>
 * 事件的话这里没有什么UI事件需要继续编写的了.
 * 这里的有些匿名类,主要实现了两个事件监听,<br><p>
 * (1)响应更改text时事件的自动调节文本大小(通过测量gui的宽度和每个单字在相应字体中的宽度之和来进行调节,保持美观)
 * </p><p>
 * (2)响应右键弹出菜单事件(右键复制粘贴全选复制等式). </p>
 *
 * MVC的资讯查看controller:
 * @see controller.Controller
 * @see controller.CalcController
 * @see CalculatorFrame
 * @see FunctionPad
 * @see NumberPad
 */
class TextHeader extends JPanel {

    private volatile static TextHeader header;
    /**
     * 这里可以写一下初始状态的BigDecimal来debug(gui)
     */
    private static BigDecimal decimal = new BigDecimal("999999999999");

    public static TextHeader getInstance() {
        if (header == null) {
            synchronized (TextHeader.class) {
                if (header == null) header = new TextHeader();
            }

        }
        return header;
    }

    /**
     * 通过指定text来设置结果文本框
     * 已过时,debug可用
     * @param text 直接设置text的text
     */
    @Deprecated
    public static void setResultText(String text) {
        TextHeader.getInstance().resultTextField.setText(text);
    }

    /**
     * 根据格式设置ResultText
     * @param bigDecimal 要格式化输出的参数
     */
    public static void setResultText(BigDecimal bigDecimal) {
        TextHeader.decimal = bigDecimal;
        TextHeader.getInstance().resultTextField.setText(new DecimalFormat(",###." + "#".repeat(20)).format(decimal));
    }

    /**
     * 设置表达式,这里就没有格式化了,直接set
     * @param text 要设置的表达式
     */
    public static void setExpressionText(String text) {
        TextHeader.getInstance().expressionTextField.setText(text);
    }

    /**
     * 获取ResultText(注意是String)
     * @return String
     */
    public static String getResultText() {
        return TextHeader.getInstance().resultTextField.getText();
    }

    /**
     * 获取Decimal,这个比较常用
     * @return 可能可以把这个decimal认为是一个ViewModel,视图自己的数据模型
     */
    public static BigDecimal getResultTextDecimal() {
        return TextHeader.decimal;
    }

    /**
     * 获取expression,这里的写法是,如果有等号,将会删除等号.
     * @return 返回一个expression String,无等号
     */
    public static String getExpressionText() {
        String str = TextHeader.getInstance().expressionTextField.getText();
        if (str.length() > 0 && str.charAt(str.length() - 1) == '=') return str.substring(0, str.length() - 1);
        else
            return TextHeader.getInstance().expressionTextField.getText();
    }


    private final JTextField resultTextField;
    private final JTextField expressionTextField;

    private TextHeader() {
        //显示算式的文本框
        expressionTextField = new JTextField();
        expressionTextField.setHorizontalAlignment(JTextField.RIGHT);
        expressionTextField.setEditable(false);
        expressionTextField.setFocusable(false);
        expressionTextField.setFont(new BasicFont(Font.PLAIN, 12));
        expressionTextField.setForeground(Color.gray);
        expressionTextField.setText("8+5");
        expressionTextField.setBorder(new EmptyBorder(15, 5, 0, 5));
        //显示当前按下数字和计算结果的文本框
        resultTextField = new JTextField();
        resultTextField.setHorizontalAlignment(JTextField.RIGHT);
        resultTextField.setEditable(false);
        resultTextField.setFocusable(true);
        resultTextField.setFont(new BasicFont(Font.BOLD, 36));
        resultTextField.setBorder(new EmptyBorder(0, 5, 0, 5));
        //这里是用来切换鼠标样式的(框选文本)
        resultTextField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.TEXT_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        //定义这个文本框的右键弹出菜单
        JPopupMenu rightResultPopupMenu = new JPopupMenu();
        JMenuItem jMenuItemCopy = new JMenuItem("复制");
        JMenuItem jMenuItemPaste = new JMenuItem("粘贴");
        JMenuItem jMenuItemSeleteAll = new JMenuItem("全选");
        JMenuItem jMenuItemCopyEquation = new JMenuItem("复制等式");
        //实现右键的复制粘贴
        jMenuItemSeleteAll.addActionListener(e -> {
            if (e.getSource() == jMenuItemSeleteAll) {
                System.out.println("press select all");
                TextHeader.this.resultTextField.requestFocus();
                TextHeader.this.resultTextField.selectAll();
            }
        });
        jMenuItemCopy.addActionListener(e -> {
            if (e.getSource() == jMenuItemCopy) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(TextHeader.this.resultTextField.getSelectedText()), null);
            }
        });
        jMenuItemCopyEquation.addActionListener(e -> {
            if (e.getSource() == jMenuItemCopyEquation) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(TextHeader.this.expressionTextField.getText() + TextHeader.this.resultTextField.getText()), null);
            }
        });
        rightResultPopupMenu.add(jMenuItemCopy);
        rightResultPopupMenu.add(jMenuItemPaste);
        rightResultPopupMenu.add(jMenuItemSeleteAll);
        rightResultPopupMenu.add(jMenuItemCopyEquation);
        //构造鼠标右键监听器,把浮动菜单显示在右键点击的位置
        resultTextField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //Button3就是鼠标右键
                if (e.getButton() == MouseEvent.BUTTON3) {
                    rightResultPopupMenu.show(TextHeader.this, e.getX(), e.getY());
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
                } finally {
                    if (content == null) {
                        content = "";
                    }
                }
                //这里是用来暴力计算结果文本框的字符串对应字体长度从而改变字号的
                //使用Font的getStringBounds的getWidth计算,避免人工暴力设定值,
                //考虑小数点,逗号,符号,数字的宽度不同
                if (!content.equals("")) {
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
        textFieldGridBagLayout.setConstraints(expressionTextField, gridBagConstraints);
        //垂直占比10分之9,水平扩展100%
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0.9;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        textFieldGridBagLayout.setConstraints(resultTextField, gridBagConstraints);
        //添加两个文本框
        add(expressionTextField);
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
            TextHeader.this.resultTextField.setFont(ft);
        }
    }


}



