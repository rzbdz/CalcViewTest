package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


/**
 * 使用GridBagLayout作为布局的JFrame之类
 * 构造函数会创建一个完整的计算器窗口
 */
public class CalculatorFrame extends JFrame {

    static private CalculatorFrame instance;

    //单例
    public static CalculatorFrame getInstance() {
        if (instance == null)
            instance = new CalculatorFrame();
        return instance;
    }
    //窗口最小宽度
    static int windowMinWidth = 340;
    //窗口最小高度
    static int windowMinHeight = 500;

    private TextHeaderControl textHeader;
    private MenuButtonBarControl menuButtonBar;
    private FunctionPadControl functionPad;
    private NumberPadControl numberPad;
    private OperationPadControl basicOperationPad;

    void setResult(String s) {
        this.textHeader.setResultText(s);
    }

    void setProcess(String s) {
        this.textHeader.setProcessText(s);
    }
    //构造函数
    private CalculatorFrame() {
        super("标准计算器");

        //设置JFrame属性
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(windowMinWidth, windowMinHeight));
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screensize.width / 2 - (windowMinWidth / 2), screensize.height / 2 - (windowMinHeight / 2));
        setSize(windowMinWidth, windowMinHeight);
        this.addComponentListener(new ResizeListener());
        //初始化控件们
        this.InitializeComponents();
    }

    private void InitializeComponents() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu memory = new JMenu("内存");
        memory.add(new JMenuItem("读取内存"));
        memory.add(new JMenuItem("查看记录"));
        JMenu help = new JMenu("帮助");
        help.add(new JMenuItem("使用方法"));
        help.add(new JMenuItem("关于"));
        JSeparator jSeparator = new JSeparator(JSeparator.HORIZONTAL);
        jSeparator.setBackground(Color.WHITE);
        jSeparator.setForeground(Color.WHITE);
        jSeparator.setBorder(new EmptyBorder(0, 0, 0, 0));
        JRadioButton jRadioButton = new JRadioButton("置顶");
        jRadioButton.addActionListener(e -> {
            this.setAlwaysOnTop(((JRadioButton) (e.getSource())).isSelected());
        });
        jRadioButton.setFont(new BasicFont(Font.PLAIN, memory.getFont().getSize()));
        jRadioButton.setBackground(Color.WHITE);
        jRadioButton.setFocusPainted(false);
        jMenuBar.add(memory);
        jMenuBar.add(help);
        jMenuBar.add(jSeparator);
        jMenuBar.add(jRadioButton);
        setJMenuBar(jMenuBar);
        //定义计算器窗口内组件
        textHeader = TextHeaderControl.getInstance();
        menuButtonBar = MenuButtonBarControl.getInstance();
        functionPad = FunctionPadControl.getInstance();
        numberPad = NumberPadControl.getInstance();
        basicOperationPad = OperationPadControl.getInstance();
        //定义Layout
        GridBagLayout gridBagLayout = new GridBagLayout();
        //设置layout
        this.setLayout(gridBagLayout);
        //这个玩意是用来搞GridBag 属性布局的
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        //设置header的GridBag属性
        //下面全是magic number,尽量不要改
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0.35;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 5;
        gridBagLayout.setConstraints(textHeader, gridBagConstraints);
        //设置计算器M Button Menu Bar 的GridBag属性
        //下面全是magic number,尽量不要改
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0.05;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 1;
        gridBagLayout.setConstraints(menuButtonBar, gridBagConstraints);
        //设置功能键的GridBag属性
        //下面全是magic number,尽量不要改
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 14;
        gridBagConstraints.gridheight = 4;
        gridBagLayout.setConstraints(functionPad, gridBagConstraints);
        //设置Number Pad的GridBag属性
        //下面全是magic number,尽量不要改
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 0.4;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 14;
        gridBagConstraints.gridheight = 8;
        gridBagLayout.setConstraints(numberPad, gridBagConstraints);
        //设置Basic Operation Button的GridBag属性
        //下面全是magic number,尽量不要改
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.6;
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 12;
        gridBagLayout.setConstraints(basicOperationPad, gridBagConstraints);
        //究极 add
        add(textHeader);
        add(menuButtonBar);
        add(functionPad);
        add(numberPad);
        add(basicOperationPad);
        this.setVisible(true);
        textHeader.setResultText("9,999");
    }
}

class ResizeListener extends ComponentAdapter {

    @Override
    public void componentResized(ComponentEvent e) {
        Dimension d = (e.getComponent()).getSize();
        if(d.height< CalculatorFrame.windowMinHeight||d.width< CalculatorFrame.windowMinWidth){
            ((JFrame)(e.getComponent())).setResizable(false);
            (e.getComponent()).setSize(CalculatorFrame.windowMinWidth, CalculatorFrame.windowMinHeight);
            ((JFrame)(e.getComponent())).setResizable(true);
        }
    }
}

class BasicFont extends Font {
    BasicFont(int fontStyle, int size) {
        super("微软雅黑", fontStyle, size);
    }
}
