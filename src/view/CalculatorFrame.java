package view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

import static java.lang.Thread.sleep;


/**
 * 程序中计算器主界面窗口(extends JFrame)<br>
 * //不再使用GridBagLayout作为布局<br>
 * //由于gbl的weight是分配extra空间,导致无法符合要的效果,改为null layout
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
    private MemoryToolFrame memoryToolFrame = null;


    /**
     * 私有构造函数
     * 约定程序中UI组件使用单例模式,
     * 均使用{@code ComponentClass.getInstance()}<br>
     * 获取单例实例
     */
    private CalculatorFrame() {
        super("计算器");
        setIconImage(new ImageIcon("src/res/icon.png").getImage());
        //设置JFrame属性
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(windowMinWidth, windowMinHeight));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width / 2 - (windowMinWidth / 2), screenSize.height / 2 - (windowMinHeight / 2));
        setSize(windowMinWidth, windowMinHeight);
        this.addComponentListener(new ResizeListener());
        this.addKeyListener(new KeyPressedHandler());
        //初始化控件们
        this.InitializeComponents();
    }

    /**
     * 约定所有JFrame都把绘制UI的代码封到
     * {@code InitializeComponents()}方法中,
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
        memory.add(readMemory);
        memory.add(viewMemory);
        JMenu history = new JMenu("历史");
        JMenuItem readHistory = history.add(new JMenuItem("读取历史"));
        JMenuItem viewHistory = history.add(new JMenuItem("查看记录"));
        JMenu help = new JMenu("帮助");
        help.add(new JMenuItem("使用方法"));
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
                JLabel icon = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().
                        getImage(CalculatorFrame.class.getResource("../res/icon.png")).
                        getScaledInstance(40, 40, Image.SCALE_DEFAULT)));
                aboutFrame.add(icon);
                aboutFrame.setSize(400, 400);
                aboutFrame.add(new JTextArea(" * ****开源组件声明:***********************************\n" +
                        " * ****(1)com.formdev.flatlaf(Apache-2.0 License)*****\n" +
                        " * **************https://www.formdev.com/flatlaf/*****\n" +
                        " * ***************************************************\n" +
                        "更新日志:\n0.1.1支持主题和异常处理\n0.1.0支持加减乘除和括号"));
                aboutFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                aboutFrame.setLocation(this.getX(), this.getY());
                aboutFrame.setIconImage(new ImageIcon("src/res/icon.png").getImage());
                aboutFrame.setVisible(true);
            }
        });
        help.add(about);
        JMenu theme = new JMenu("主题");
        ButtonGroup themeBtnGroup = new ButtonGroup();
        JMenuItem darkTheme = new JRadioButtonMenuItem("暗黑主题");
        JMenuItem lightTheme = new JRadioButtonMenuItem("光明主题");
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
            }
        });
        lightTheme.addActionListener(e -> {
            if (((JRadioButtonMenuItem) e.getSource()).isSelected()) {
                Theme.setLightLooksAndFeels(CalculatorFrame.getInstance());
            }
        });
        systemTheme.addActionListener(e -> {
            if (((JRadioButtonMenuItem) e.getSource()).isSelected()) {
                Theme.setSystemLooksAndFeels(CalculatorFrame.getInstance());
            }
        });
        //JSeparator jSeparator = new JSeparator(JSeparator.HORIZONTAL);
        JRadioButton jRadioButton = new JRadioButton("置顶");
        jRadioButton.setMargin(new Insets(0,0,0,5));
        jRadioButton.addActionListener(e -> {
            this.setAlwaysOnTop(((JRadioButton) (e.getSource())).isSelected());
        });
        jRadioButton.setFont(new BasicFont(Font.PLAIN, memory.getFont().getSize()));
        jRadioButton.setBackground(jMenuBar.getBackground());
        jRadioButton.setFocusPainted(false);
        jMenuBar.add(memory);
        jMenuBar.add(history);
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
        System.out.println(getContentPane().getHeight());
        System.out.println(getContentPane().getWidth());
        System.out.println(windowMinHeight);
        System.out.println(windowMinWidth);
        setComponentsNullLayoutBounds(0, getContentPane().getHeight(), getContentPane().getWidth());
        TextHeader.setResultText(TextHeader.getResultTextDecimal());
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
        functionPad.setBounds(2, menuButtonBar.getY() + menuButtonBar.getHeight()+1,
                (int) (0.75D * (double) windowWidth), (int) (0.3D * (double) windowHeight)-1);
        numberPad.setBounds(2, functionPad.getY() + functionPad.getHeight(),
                functionPad.getWidth(), (int) (0.4D * (double) windowHeight));
        basicOperationPad.setBounds(numberPad.getX() + numberPad.getWidth(), menuButtonBar.getY() + menuButtonBar.getHeight(),
                (int) (0.25D * (double) windowWidth), (int) (0.7D * (double) windowHeight));
        if (instance != null){
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
        System.out.println("youp ress" + jb.getText());
    }
}

/**
 * 处理整个程序键盘事件的监听器
 * 注释:这个程序里约定实现接口的监听器都写成Handler结尾命名
 */
class KeyPressedHandler implements KeyListener {

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}

/**
 * 一个使用微软雅黑字体的继承自Font类的字体类
 * 构造函数为:<br>
 * {@code BasicFont(int fontStyle, int size);}
 */
class BasicFont extends Font {
    BasicFont(int fontStyle, int size) {
        super("微软雅黑", fontStyle, size);
    }
}
