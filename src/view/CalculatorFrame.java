package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

import static java.awt.Toolkit.getDefaultToolkit;
import static java.lang.Thread.sleep;


/**
 * 程序中计算器主界面窗口(extends JFrame)<br>
 * //不再使用GridBagLayout作为布局<br>
 * //由于gbl的weight是分配extra空间,导致无法符合要的效果,改为null layout<br>
 * //程序内动态计算组件的Location和Size,然后动态更新UI.<br>
 * <br>
 * 通过调用{@code getInstance()}方法获取一个窗口的实例,<br>
 * 窗口会自动完成界面组件的创建和显示,将会获取另外的几个组件的实例并显示:<br>
 * 具有以下几个JPanel组件:<br>
 * {@link TextHeader}:2行JPanel(窗口上方), 包含一个显示表达式的小文本框,一个显示结果的大文本框<br>
 * {@link MemoryButtonBar}:1行5个按钮(文本框下方),是内存按键的按钮MC,MR,M+<br>
 * {@link FunctionPad}:3*3的网格JPanel组件(窗口中间),包含括号,CE/C,以及与x有关的一元/二元运算<br>
 * {@link NumberPad}:4*3的数字按钮(窗口左下),小数点,以及正负号转变按钮<br>
 * {@link OperationPad}:7*1的运算按钮(窗口最右边),包括退格键到等号的按钮<br>
 * 上述所有组件也都是采用单例模式,
 * 编写事件时可以通过{@code JPanel类名.getInstance()}
 * 编写事件时可以通过{@code JPanel类名.getInstance()}
 * 来获得组件实例进行操作
 * <br><br>
 * 具有一个内部类 {@code private static class ResizeListener}:
 * <p>成员静态类,用于监听窗口大小改变的事件</p>
 * <p>禁止小于窗口最小大小</p>
 * <p>防止用户乱拉窗口大小</p>
 */
public class CalculatorFrame extends JFrame implements CanTurnErrorState {

    /**
     * 单例模式实例成员
     * 用了DCL
     */
    private static volatile CalculatorFrame instance;

    /**
     * 单例模式获取实例成员对象的方法
     *
     * @return 返回一个计算器窗口的实例
     */
    public static CalculatorFrame getInstance() {
        if (instance == null) {
            synchronized (CalculatorFrame.class) {
                if (instance == null) {
                    instance = new CalculatorFrame();
                }
            }
        }
        return instance;
    }

    /**
     * 定义窗口的最小宽度,也是窗口初始化时的窗口宽度
     */
    static int windowMinWidth = 344;
    /**
     * 定义窗口的最小高度,也是窗口初始化时的窗口高度
     */
    static int windowMinHeight = 504;

    /**
     * 私有成员
     *
     * @see CalculatorFrame
     * @see TextHeader
     */
    private TextHeader textHeader;
    /**
     * 私有成员
     *
     * @see CalculatorFrame
     * @see MemoryButtonBar
     */
    private MemoryButtonBar menuButtonBar;
    /**
     * 私有成员
     *
     * @see CalculatorFrame
     * @see FunctionPad
     */
    private FunctionPad functionPad;
    /**
     * 私有成员
     *
     * @see CalculatorFrame
     * @see FunctionPad
     */
    private NumberPad numberPad;
    /**
     * 私有成员
     *
     * @see CalculatorFrame
     * @see FunctionPad
     */
    private OperationPad basicOperationPad;

    /**
     * 链接一个内存窗口(程序顶部的内存工具栏)
     * 私有成员
     *
     * @see CalculatorFrame
     * @see MemoryToolFrame
     */
    public MemoryToolFrame memoryToolFrame = null;


    /**
     * 私有构造函数
     * 约定程序中UI组件使用单例模式,
     * 均使用{@code ComponentClass.getInstance()}<br>
     * 获取单例实例
     */
    private CalculatorFrame() {
        super("计算器");
        setFocusable(true);
        setIconImage(getDefaultToolkit().
                getImage(CalculatorFrame.class.getClassLoader().getResource("res/icon.png")));
        //设置JFrame属性
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(windowMinWidth, windowMinHeight));
        Dimension screenSize = getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width / 2 - (windowMinWidth / 2), screenSize.height / 2 - (windowMinHeight / 2));
        setSize(windowMinWidth, windowMinHeight);
        this.addComponentListener(new ResizeListener());

        //初始化控件们
        this.InitializeComponents();
        requestFocus();
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                requestFocus();
            }
        });
        GlobalKeyBoardListen();

    }

    /**
     * 通过AWTToolkit获取一个全局监听事件,
     * 监听键盘操作.
     * 注意:使用时一定要在英语输入法下,
     * (目前体会到Swing对中文输入法的键盘监听有问题)
     */
    public void GlobalKeyBoardListen(){
        Toolkit.getDefaultToolkit().addAWTEventListener((AWTEventListener) event -> {
            if (((KeyEvent) event).getID() == KeyEvent.KEY_PRESSED) {
                CanSimulateKeyboard simulator = null;
                String operationEnum = "\n\b+-*/=%";
                char ch = ((KeyEvent) event).getKeyChar();
                String find = String.valueOf(ch);
                if (ch >= '0' && ch <= '9' || ch == '.') {
                    simulator = NumberPad.getInstance();
                } else if (ch == '^' || ch == '(' || ch == ')' || ch == 'c') {
                    if (ch == '^')
                        find = FunctionPad.X_Y;
                    if (ch == 'c')
                        find = FunctionPad.C;
                    simulator = FunctionPad.getInstance();
                } else if (operationEnum.indexOf(((KeyEvent) event).getKeyChar()) != -1) {
                    if (ch == '\b')
                        find = OperationPad.BACKSPACE;
                    if (ch == '\n')
                        find = OperationPad.EQUALS;
                    if (ch == '*')
                        find = OperationPad.TIMES;
                    if (ch == '/')
                        find = OperationPad.DIVIDES;
                    simulator = OperationPad.getInstance();
                } else {
                    System.out.println("You Pressed: " + ((KeyEvent) event).getKeyChar() + " (该键无模拟输入)");
                }
                if (simulator != null)
                    simulator.simulatePressed(find);
            }
        }, AWTEvent.KEY_EVENT_MASK);
    }

    /**
     * 约定所有JFrame都把绘制UI的代码封到
     * {@code InitializeComponents()}方法中,
     * (参考Windows GUI写法)
     * 此方法只在程序JFrame子类构造函数中调用
     */
    private void InitializeComponents() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu memory = new JMenu("内存");

        JMenuItem readMemory = new JMenuItem("读取内存");
        JMenuItem viewMemory = new JMenuItem("查看记录");
        viewMemory.addActionListener(e -> {
            if (e.getSource() == viewMemory) {
                if (this.memoryToolFrame == null) {
                    memoryToolFrame = new MemoryToolFrame(this.getX() + this.getWidth(), this.getY());
                } else {
                    memoryToolFrame.setVisible(true);
                }
            }
        });
        readMemory.addActionListener(e -> {
            if(e.getSource()==readMemory){
                System.out.println("Read Memory From MenuBar: ");
                if(MemoryButtonBar.getInstance().hasMemory()) {
                    System.out.println((BigDecimal) ((MyArrayList) MemoryButtonBar.getInstance().getDecimalList()).peek());
                    TextHeader.getInstance().setLatestDigit((BigDecimal) ((MyArrayList) MemoryButtonBar.getInstance().getDecimalList()).peek());
                }
            }
        });
        memory.add(readMemory);
        memory.add(viewMemory);
        JMenu history = new JMenu("历史");
        JMenuItem readHistory = history.add(new JMenuItem("读取历史"));
        JMenuItem viewHistory = history.add(new JMenuItem("查看记录"));
        JMenu help = new JMenu("帮助");
        JMenuItem usage =  new JMenuItem("使用方法");
        JMenuItem about = new JMenuItem("关于");
        about.addActionListener(e -> {
            if (e.getSource() == about) {
                JFrame aboutFrame = new JFrame("关于这个计算器");
                aboutFrame.setLayout(new FlowLayout());
                JTextField comp = new JTextField("半标准半科学计算器 v0.1.1@pjz,wyx");
                comp.setEditable(false);
                comp.setBackground(jMenuBar.getBackground());
                comp.setForeground(readHistory.getForeground());
                aboutFrame.add(comp);
                JLabel icon = new JLabel(new ImageIcon(getDefaultToolkit().
                        getImage(CalculatorFrame.class.getClassLoader().getResource("res/icon.png")).
                        getScaledInstance(40, 40, Image.SCALE_DEFAULT)));
                aboutFrame.add(icon);
                aboutFrame.setSize(400, 400);
                aboutFrame.add(new JTextArea(" *****主题声明:***********************************\n" +
                        " * ****(1)com.formdev.flatlaf(Apache-2.0 License)*****\n" +
                        " * **************https://www.formdev.com/flatlaf/*****\n" +
                        " * ***************************************************\n" +
                        "更新日志:\n0.1.2支持所有帮助中声明的功能,修复bug\n0.1.1支持主题和异常处理\n0.1.0支持加减乘除和括号"));
                aboutFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                aboutFrame.setLocation(this.getX(), this.getY());
                aboutFrame.setIconImage(getDefaultToolkit().
                        getImage(CalculatorFrame.class.getClassLoader().getResource("res/icon.png")));
                aboutFrame.setVisible(true);
            }
        });
        usage.addActionListener(e->{
            if(e.getSource()==usage){
                JFrame helpframe = new JFrame("计算器帮助");
                helpframe.setLayout(new FlowLayout());
                JTextField comp = new JTextField("半标准半科学计算器 交作业版@pjz,wyx");
                comp.setEditable(false);
                comp.setBackground(jMenuBar.getBackground());
                comp.setForeground(readHistory.getForeground());
                helpframe.add(comp);
                JLabel icon = new JLabel(new ImageIcon(getDefaultToolkit().
                        getImage(CalculatorFrame.class.getClassLoader().getResource("res/icon.png")).
                        getScaledInstance(40, 40, Image.SCALE_DEFAULT)));
                helpframe.add(icon);
                helpframe.setSize(800, 400);
                helpframe.add(new JTextArea("v0.1.3交作业版，帮助。\n" +
                        "1.支持使用键盘(英文输入法)，鼠标输入(文本框的粘贴输入没有实现),支持复制表达式或复制数字到剪切板;\n" +
                        "5.支持模拟计算器内存，保存多个数字,点击菜单栏查看\n" +
                        "2.具有不太完善的异常处理，最常见按的除零提醒，以及非法输入（使用正则表达式校验）提醒.\n" +
                        "3.具有一定的鲁棒性，处理正负号,括号缺失等情况大概与Window计算器一致。\n" +
                        "2.支持半个科学计算器，半个标准计算器的运算，运算遵循运算优先度;\n" +
                        "3.支持切换主题;\n" +
                        "4.需要复制表达式或者选中数字请在文本框区域框选并按下鼠标标右键，接着鼠标单机左键选择你要进行的操作;\n" +
                        "6.历史记录功能没有具体实现\n" +
                        "6.请老师手下留情.\n" +
                        "8.TextHeader文本框中响应输入的成员方法较多,主要是为了方便其他输入控件调用,避免直接操作JTextField\n"));
                helpframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                helpframe.setLocation(this.getX(), this.getY());
                helpframe.setIconImage(new ImageIcon(getDefaultToolkit().
                        getImage(CalculatorFrame.class.getClassLoader().getResource("res/icon.png"))).getImage());
                helpframe.setVisible(true);
            }
        });
        help.add(usage);
        help.add(about);
        JMenu theme = new JMenu("主题");
        ButtonGroup themeBtnGroup = new ButtonGroup();
        JMenuItem darkTheme = new JRadioButtonMenuItem("黑主题");
        JMenuItem lightTheme = new JRadioButtonMenuItem("明主题");
        JMenuItem systemTheme = new JRadioButtonMenuItem("系统主题");
        themeBtnGroup.add(darkTheme);
        themeBtnGroup.add(lightTheme);
        themeBtnGroup.add(systemTheme);
        theme.add(darkTheme);
        theme.add(lightTheme);
        theme.add(systemTheme);
        darkTheme.addActionListener(e -> {
            if (((JRadioButtonMenuItem) e.getSource()).isSelected()) {
                Theme.setDarkLooksAndFeels(CalculatorFrame.getInstance());
                Theme.updateCurrentLooksAndFeels(this.memoryToolFrame);
            }
        });
        lightTheme.addActionListener(e -> {
            if (((JRadioButtonMenuItem) e.getSource()).isSelected()) {
                Theme.setLightLooksAndFeels(CalculatorFrame.getInstance());
                Theme.updateCurrentLooksAndFeels(this.memoryToolFrame);
            }
        });
        systemTheme.addActionListener(e -> {
            if (((JRadioButtonMenuItem) e.getSource()).isSelected()) {
                Theme.setSystemLooksAndFeels(CalculatorFrame.getInstance());
                Theme.updateCurrentLooksAndFeels(this.memoryToolFrame);
            }
        });
        //JSeparator jSeparator = new JSeparator(JSeparator.HORIZONTAL);
        JRadioButton jRadioButton = new JRadioButton("置顶");
        jRadioButton.setMargin(new Insets(0, 0, 0, 5));
        jRadioButton.addActionListener(e -> {
            this.setAlwaysOnTop(((JRadioButton) (e.getSource())).isSelected());
            if (this.memoryToolFrame != null)
                this.memoryToolFrame.setAlwaysOnTop(((JRadioButton) (e.getSource())).isSelected());
        });
        jRadioButton.setFont(new BasicFont(Font.PLAIN, memory.getFont().getSize()));
        jRadioButton.setBackground(jMenuBar.getBackground());
        jRadioButton.setFocusPainted(false);
        jMenuBar.add(memory);
        //历史菜单栏菜单按钮注释(不做这个了)
        //jMenuBar.add(history);
        jMenuBar.add(theme);
        jMenuBar.add(help);
        //jMenuBar.add(jSeparator);
        jMenuBar.add(Box.createHorizontalGlue());
        jMenuBar.add(jRadioButton);
        setJMenuBar(jMenuBar);
        //定义计算器窗口内组件
        textHeader = TextHeader.getInstance();
        menuButtonBar = MemoryButtonBar.getInstance();
        functionPad = FunctionPad.getInstance();
        numberPad = NumberPad.getInstance();
        basicOperationPad = OperationPad.getInstance();
        setLayout(null);
        //究极 add
        add(textHeader);
        add(menuButtonBar);
        add(functionPad);
        add(numberPad);
        add(basicOperationPad);
        //null layout的设置部分
        this.setVisible(true);
        //System.out.println(getContentPane().getHeight());
        //System.out.println(getContentPane().getWidth());
        //System.out.println(windowMinHeight);
        //System.out.println(windowMinWidth);
        setComponentsNullLayoutBounds(0, getContentPane().getHeight(), getContentPane().getWidth());
        //TextHeader.setResultText(TextHeader.getResultTextDecimal());
        /**
         * 最后的一些初始化工作
         */
        darkTheme.setSelected(true);
    }

    public void setComponentsNullLayoutBounds(int offsetHeight, int windowHeight, int windowWidth) {
        windowWidth -= 4;
        textHeader.setBounds(2, offsetHeight, windowWidth, (int) (0.25D * (double) windowHeight));
        menuButtonBar.setBounds(1, textHeader.getY() + textHeader.getHeight(),
                windowWidth, (int) (0.05D * (double) windowHeight));
        functionPad.setBounds(2, menuButtonBar.getY() + menuButtonBar.getHeight() + 1,
                (int) (0.75D * (double) windowWidth), (int) (0.3D * (double) windowHeight) - 1);
        numberPad.setBounds(2, functionPad.getY() + functionPad.getHeight(),
                functionPad.getWidth(), (int) (0.4D * (double) windowHeight));
        basicOperationPad.setBounds(numberPad.getX() + numberPad.getWidth(), menuButtonBar.getY() + menuButtonBar.getHeight(),
                (int) (0.25D * (double) windowWidth), (int) (0.7D * (double) windowHeight));
        if (instance != null) {
            getInstance().repaint();
            SwingUtilities.updateComponentTreeUI(instance);
        }

    }

    @Override
    public void setErrorState(boolean bool) {
        basicOperationPad.setErrorState(bool);
        functionPad.setErrorState(bool);
        numberPad.setErrorState(bool);
        menuButtonBar.setErrorState(bool);
        isError = bool;
    }

    private  boolean isError = false;
    @Override
    public boolean isErrorState() {
        return isError;
    }

    /**
     * 成员静态类,用于监听窗口大小改变的事件
     * 禁止小于窗口最小大小
     * 防止用户乱拉窗口大小
     * (构造时已经设置了min w和h,主要是系统高dpi缩放的时候还是可以乱拖拉的)
     */
    private static class ResizeListener extends ComponentAdapter {

        @Override
        public void componentResized(ComponentEvent e) {
            Dimension d = (e.getComponent()).getSize();
            if (d.height < CalculatorFrame.windowMinHeight || d.width < CalculatorFrame.windowMinWidth) {
                ((JFrame) (e.getComponent())).setResizable(false);
                (e.getComponent()).setSize(CalculatorFrame.windowMinWidth, CalculatorFrame.windowMinHeight);
                try {
                    sleep(100);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                ((JFrame) (e.getComponent())).setResizable(true);
                try {
                    sleep(100);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
            CalculatorFrame.getInstance().
                    setComponentsNullLayoutBounds(0,
                            CalculatorFrame.getInstance().getContentPane().getHeight(), ((JFrame) (e.getComponent())).getContentPane().getWidth());
            TextHeader.getInstance().UpdateResultTextFontSize(TextHeader.getResultText());
        }

        @Override
        public void componentMoved(ComponentEvent e) {
            int x = e.getComponent().getX() + e.getComponent().getWidth();
            int y = e.getComponent().getY();
            if (((CalculatorFrame) (e.getComponent())).memoryToolFrame != null) {
                ((CalculatorFrame) (e.getComponent())).memoryToolFrame.setLocation(x, y);
            }
        }
    }

}

/**
 * 处理鼠标点击按钮事件的监听器父类
 * 创建这个类的目的是方便实现统一的Debug操作和加强可读性
 * 然后子类都继承这个类实现具体的事件处理
 * 注释:这个程序里约定实现接口的监听器都写成Handler结尾命名
 */
class ButtonClickHandler implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton jb = (JButton) (e.getSource());
        System.out.println("ButtonClickHandler: You have Clicked: " + jb.getText());
    }
}

