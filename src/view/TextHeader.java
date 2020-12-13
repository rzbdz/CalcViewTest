
package view;

import controller.CalculationController;
import model.CalculationException;
import model.DivideByZeroException;
import model.InvalidExpressionException;

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
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * <p>
 * 文本框控件,继承JPanel<br>
 * 更多资讯查看see also</p>
 * <p>
 * 这里有两个文本框一个数据类型,<br>
 * 一个是用来搞表达式的(UI上面的那行8+5);<br>
 * 一个是用来搞结果和当前input 数字的(UI大粗体那行9999);<br>
 * 一个数据类型是{@link TextHeader#resultDecimal}<br></p>
 * <p>
 * <p>
 * 一个是<b>resultText</b>和<b>decimal</b><br>
 * {@link TextHeader#setResultDecimal(BigDecimal)}<br>
 * {@link TextHeader#getResultText()}<br>
 * {@link TextHeader#getResultTextDecimal()}<br>
 * 这里搞一个decimal是因为,text是需要进行格式化的<br>
 * e.g. 9,999,999,000.123 对应的decimal是9999999000123<br>
 * 实际运算里,是运算的decimal,text只是用来展示,和退格处理,
 * 退格处理里面要同时修改decimal(使用{@code setResultText(BigDecimal)}就很安全)<br></p><p>
 * 然后是<b>expressionText</b><br>
 * {@link TextHeader#getExpressionText()}<br>
 * {@link TextHeader#setExpressionText(String)}<br>
 * 这里是用getter和setter来修改文本的</p><p>
 * 主要的ui事件都写好了,重点还是在别的地方({@link NumberPad},
 * {@link OperationPad},{@link FunctionPad}等)
 * 调用获得instance,然后<b>传入updateView给上面说的几个java bean方法</b><br>
 * </p>
 * <p>
 * 这里的有些匿名类,主要实现了两个事件监听,<br><p>
 * (1)响应更改text时事件的自动调节文本大小(通过测量gui的宽度和每个单字在相应字体中的宽度之和来进行调节,保持美观)
 * </p><p>
 * (2)响应右键弹出菜单事件(右键复制粘贴全选复制等式). </p>
 * <p>
 * 后来这里实现了很多成员方法,主要是方便输入的控件调用,
 * 避免在输入的地方频繁操作TextField.
 * 理论上可以抽出来放到Controller里,时间有限.
 *
 * @see controller.Controller
 * @see CalculationController
 * @see CalculatorFrame
 * @see FunctionPad
 * @see NumberPad
 */
class TextHeader extends JPanel implements CanTurnErrorState {

    private volatile static TextHeader header;
    /**
     * 这里可以写一下初始状态的数字
     */
    private static BigDecimal resultDecimal = new BigDecimal("0");
    private static String latestSimpleCalc;
    /**
     * 判断一轮的大数字连续输入
     */
    private boolean isInNewDigit = false;
    /**
     * 判断当前大数字有没有小数点,如果有了就不能再输入小数点
     */
    private boolean hasOneDot = true;
    /**
     * 判断是不是新一轮的计算(请考虑按下等号后,再按数字就会清空表达式开始新一轮计算的过程)
     */
    private boolean isNewCalculation = true;

    public static TextHeader getInstance() {
        if (header == null) {
            synchronized (TextHeader.class) {
                if (header == null) header = new TextHeader();
            }

        }
        return header;
    }


    /**
     * 更新输入.
     *
     * @param jbText jButton 的传入text
     */
    public void updateDigit(String jbText) {
        if (isErrorState()) {
            setErrorState(false);
        }
        if (isNewCalculation) {
            setExpressionText("");
            isNewCalculation = false;
        }
        if (!jbText.equals(".")) {
            if (!isInNewDigit) {
                setResultDecimal(new BigDecimal(jbText));
                isInNewDigit = true;
                hasOneDot = false;
            } else {
                String pre = getResultPlainText();
                setResultDecimal(new BigDecimal(pre + jbText));
            }
        } else if (!hasOneDot && isInNewDigit) {
            String pre = getResultText();
            setResultText(pre + ".");
            hasOneDot = true;
        }
    }

    /**
     * 获取一个没有逗号的字符串
     *
     * @return String
     */
    public String getResultPlainText() {
        return getResultText().replace(",", "");
    }

    /**
     * 更新后加运算符
     *
     * @param jbText 传入的运算符或子表达式
     */
    public void updatePostOperator(String jbText) {
        if (isNewCalculation) {
            setExpressionText("");
            isNewCalculation = false;
            isInNewDigit = true;
        }
        if (jbText.equals("(")) {
            if (!isInNewDigit)
                setExpressionText(getExpressionText() + jbText);
            return;
        } else if (isInNewDigit) {
            //System.out.println(BigDecimal.ZERO.toString());
            if (resultDecimal.compareTo(new BigDecimal("0")) < 0) {
                setExpressionText(getExpressionText() + "0" + resultDecimal.stripTrailingZeros().toString() + jbText);
            } else
                setExpressionText(getExpressionText() + resultDecimal.stripTrailingZeros().toString() + jbText);
        } else {
            setExpressionText(getExpressionTextWithoutLast() + jbText);
        }
        isInNewDigit = false;
        try {
            setResultDecimal(getCalcResult());
        } catch (CalculationException e) {
            System.out.println("Exception Msg: " + e.getMessage());
            setErrorState(true);
            setResultText(e.getMessage());
        }
        hasOneDot = getResultText().contains(".");
    }

    /**
     * 获取整个表达式的结果,
     *
     * @return 结果
     */
    public BigDecimal getCalcResult() throws DivideByZeroException, InvalidExpressionException {
        var c = CalculationController.getInstance();
        c.updateModel(TextHeader.getExpressionTextWithoutLast());
        return c.updateView();
    }

    /**
     * 获取子表达式的结果,
     * 如:5+6+sqrt(9)后应该显示的是3而不是整个表达式的结果
     *
     * @param subString 子表达式
     * @return 结果
     */
    public BigDecimal getCalcResult(String subString) throws DivideByZeroException, InvalidExpressionException {
        var c = CalculationController.getInstance();
        c.updateModel(subString);
        return c.updateView();
    }

    /**
     * 更新一个一元运算,本质是进行了数字a的替换为abs(a),etc...
     *
     * @param unaryString 更新的一元附加表达式
     */
    public void updateUnaryOperator(String unaryString) {
        if (isNewCalculation) {
            setExpressionText("");
            isNewCalculation = false;
            isInNewDigit = true;
        }
        setExpressionText(getExpressionText() + unaryString);
        isInNewDigit = false;
        try {
            setResultDecimal(getCalcResult(unaryString));
        } catch (CalculationException e) {
            System.out.println("Exception Msg: " + e.getMessage());
            setErrorState(true);
            setResultText(e.getMessage());
        }
        hasOneDot = getResultText().contains(".");
    }

    /**
     * 声明进入了相反的符号
     */
    public void turnOppositeSign() {
        setResultDecimal(resultDecimal = new BigDecimal("0").subtract(resultDecimal));
    }

    /**
     * 声明进入一个按下等号的状态
     */
    public void turnEqualState() {
        System.out.println("Is Inputing a New Digit: " + isInNewDigit);
        if (isInNewDigit || isNewCalculation) {
            setExpressionText(getExpressionText() + resultDecimal.stripTrailingZeros().toPlainString() + "=");
        } else {
            setExpressionText(getExpressionTextWithoutLast() + "=");
        }
        isInNewDigit = false;
        hasOneDot = false;
        try {
            setResultDecimal(getCalcResult());
        } catch (CalculationException e) {
            System.out.println("Exception Msg: " + e.getMessage());
            setErrorState(true);
            setResultText(e.getMessage());
        }
        isNewCalculation = true;
    }

    /**
     * 进行一个退格操作
     */
    public void backSpace() {
        if (getResultTextDecimal().stripTrailingZeros().toPlainString().length() <= 1) {
            setCState();
            return;
        }
        if (getResultText().contains(".")) {
            setResultText(getResultText()
                    .substring(0, getResultText().length() - 1));
            if (getResultText().charAt(getResultText().length() - 1) != '.') {
                setResultDecimal(new BigDecimal(getResultText()));
            }

        } else
            setResultDecimal(new BigDecimal(getResultPlainText()
                    .substring(0, getResultPlainText().length() - 1)));
        this.hasOneDot = getResultText().contains(".");
    }


    @Override
    public void setErrorState(boolean bool) {
        isInNewDigit = false;
        hasOneDot = false;
        isNewCalculation = true;
        isError = bool;
        CalculatorFrame.getInstance().setErrorState(bool);
    }

    private boolean isError = false;

    @Override
    public boolean isErrorState() {
        return isError;
    }


    /**
     * 声明进入CE状态
     * 全部清零
     */
    public void setCEState() {
        setExpressionText("");
        setResultDecimal(new BigDecimal("0"));
        setErrorState(false);
    }

    /**
     * 声明进入C状态
     * 清零当前数字
     */
    public void setCState() {
        setResultDecimal(new BigDecimal("0"));
        isInNewDigit = false;
        hasOneDot = false;
    }

    /**
     * 返回一个最新的数字(给Memory)
     *
     * @return 最新的数字给他以便于存入内存
     */
    public static BigDecimal getLatestDigit() {
        return getResultTextDecimal();
    }

    /**
     * 设置一个最新的数字(给部分需求用)
     *
     * @param memoryRead 从程序中更新一个数字(如内存)
     */
    public void setLatestDigit(BigDecimal memoryRead) {
        setResultDecimal(memoryRead);
        this.isInNewDigit = true;
        if (memoryRead.compareTo(new BigDecimal(memoryRead.intValue())) == 0)
            this.hasOneDot = false;
    }

    /**
     * 设置新计算状态
     *
     * @param bool 布尔
     */
    private void setNewCalculationState(boolean bool) {

    }

    /**
     * 通过指定text来设置结果文本框
     * 异常或者进行退格操作要用
     *
     * @param text 直接设置text的text
     */
    public static void setResultText(String text) {
        TextHeader.getInstance().resultTextField.setText(text);
    }

    /**
     * 根据格式设置ResultText
     *
     * @param bigDecimal 要格式化输出的参数
     */
    private static void setResultDecimal(BigDecimal bigDecimal) {
        TextHeader.resultDecimal = bigDecimal;
        TextHeader.getInstance().resultTextField.setText(new DecimalFormat(",###." + "#".repeat(20)).format(resultDecimal));
    }

    /**
     * 设置表达式,这里就没有格式化了,直接set
     *
     * @param text 要设置的表达式
     */
    public static void setExpressionText(String text) {
        TextHeader.getInstance().expressionTextField.setText(text);
    }

    /**
     * 获取ResultText(注意是String)
     *
     * @return String
     */
    public static String getResultText() {
        return TextHeader.getInstance().resultTextField.getText();
    }

    /**
     * 获取Decimal,这个比较常用
     *
     * @return 可能可以把这个decimal认为是一个ViewModel, 视图自己的数据模型
     */
    public static BigDecimal getResultTextDecimal() {
        return TextHeader.resultDecimal.stripTrailingZeros();
    }

    /**
     * 获取expression,这里的写法是,如果有等号,将会删除.
     *
     * @return 返回一个expression String,无末尾=
     */
    public static String getExpressionText() {
        if (getInstance().expressionTextField.getText().length() == 0) return "";
        String str = TextHeader.getInstance().expressionTextField.getText();
        if (str.length() > 0 && str.charAt(str.length() - 1) == '=') return str.substring(0, str.length() - 1);
        else
            return TextHeader.getInstance().expressionTextField.getText();
    }

    /**
     * 获取expression,这里的写法是,如果有号,将会删除号.
     *
     * @return 返回一个expression String,无末尾號
     */
    public static String getExpressionTextWithoutLast() {
        if (getExpressionText().equals("")) return "";
        String str = TextHeader.getInstance().expressionTextField.getText();
        boolean symbol = false;
        String s = str.substring(str.length() - 1);
        if (s.equals(OperationPad.EQUALS) || s.equals(OperationPad.TIMES)
                || s.equals(OperationPad.DIVIDES) || s.equals(OperationPad.PLUS)
                || s.equals(OperationPad.MINUS) || s.equals("^")) {
            symbol = true;
        }
        return symbol ? str.substring(0, str.length() - 1) : str;

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
        expressionTextField.setText("");
        expressionTextField.setBorder(new EmptyBorder(15, 5, 0, 5));
        //显示当前按下数字和计算结果的文本框
        resultTextField = new JTextField();
        resultTextField.setHorizontalAlignment(JTextField.RIGHT);
        resultTextField.setEditable(false);
        resultTextField.setFocusable(true);
        resultTextField.setText(new DecimalFormat(",###." + "#".repeat(20)).format(resultDecimal));
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
                System.out.println("JMenu: Selected All");
                TextHeader.this.resultTextField.requestFocus();
                TextHeader.this.resultTextField.selectAll();
                rightResultPopupMenu.show(TextHeader.this, this.resultTextField.getX() + this.resultTextField.getWidth(),
                        this.resultTextField.getY() + this.resultTextField.getHeight() / 2);
                Theme.updateCurrentLooksAndFeels(rightResultPopupMenu);
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
                    Theme.updateCurrentLooksAndFeels(rightResultPopupMenu);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (TextHeader.getInstance().resultTextField.getSelectedText() != null) {
                        rightResultPopupMenu.show(TextHeader.this, e.getX(), e.getY() + 40);
                        Theme.updateCurrentLooksAndFeels(rightResultPopupMenu);
                    }
                }
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
                UpdateResultTextFontSize(content);
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

    public void UpdateResultTextFontSize(String content) {
        ChangeFont cf;
        if (!content.equals("")) {
            int max = 56;
            Font font = new BasicFont(Font.BOLD, max);
            for (int i = max - 1; font.getStringBounds(content,
                    new FontRenderContext(new AffineTransform(), true, true)).getWidth()
                    + 40 >= resultTextField.getParent().getSize().width; i--) {
                font = new BasicFont(Font.BOLD, i);
            }
            cf = new ChangeFont(font);
            //稍后执行changeFont操作
            //这里这样调用是因为响应document事件时使用JTextField进入了Document部分,
            //JTextField被加锁
            EventQueue.invokeLater(cf);
        }
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



