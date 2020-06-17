import javax.swing.*;
import java.awt.*;


/**
 * 使用GridBagLayout作为布局的JFrame之类
 * 构造函数会创建一个完整的计算器窗口
 */
class MainGridBagLayoutWindows extends JFrame {
    //计算器窗口顶部标题栏的标题
    static final String windowTitle = "标准计算器";
    //计算器窗口内header框框的默认字
    static final String headerDefaultText = "9,999,999,999,999,999";
    //窗口最小宽度
    static final int windowWidth = 340;
    //窗口最小高度
    static final int windowHeight = 500;
    Header hd ;
    MButtonBar mbb ;
    FunctionPad fp ;
    NumberPad nbp ;
    BasicOperationPad bop;
    MainGridBagLayoutWindows() {
        //设置JFrame属性
        super(windowTitle);
        this.addComponentListener(new ResizeListener());
        //setResizable(false);
        JMenuBar jmb = new JMenuBar();
        JMenu memory = new JMenu("内存");
        memory.add(new JMenuItem("读取内存"));
        memory.add(new JMenuItem("查看记录"));
        JMenu help = new JMenu("帮助");
        help.add(new JMenuItem("使用方法"));
        help.add(new JMenuItem("关于"));
        jmb.add(memory);
        jmb.add(help);
        setJMenuBar(jmb);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(windowWidth, windowHeight));
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screensize.width/2-(windowWidth/2),screensize.height/2-(windowHeight/2));
        setSize(windowWidth,windowHeight);
        //定义计算器窗口内组件
        hd = new Header();
        mbb = new MButtonBar();
        fp = new FunctionPad();
        nbp = new NumberPad();
        bop = new BasicOperationPad();
        //定义Layout
        GridBagLayout gridBagLayout = new GridBagLayout();
        //设置layout
        this.setLayout(gridBagLayout);
        //这个玩意是用来搞GridBag 属性布局的
        GridBagConstraints c = new GridBagConstraints();
        //设置header的GridBag属性
        //下面全是magic number,尽量不要改
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 0.45;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 5;
        MainWindow.resultTextField = hd.resultTextField;
        MainWindow.processTextField = hd.processTextField;
        hd.resultTextField.setText(headerDefaultText);
        gridBagLayout.setConstraints(hd, c);
        //设置计算器M Button Menu Bar 的GridBag属性
        //下面全是magic number,尽量不要改
        c.weightx = 1;
        c.weighty = 0.05;
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        gridBagLayout.setConstraints(mbb, c);
        //设置功能键的GridBag属性
        //下面全是magic number,尽量不要改
        c.weightx = 0.7;
        c.weighty = 0.2;
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 14;
        c.gridheight = 4;
        gridBagLayout.setConstraints(fp, c);
        //设置Number Pad的GridBag属性
        //下面全是magic number,尽量不要改
        c.weightx = 0.7;
        c.weighty = 0.4;
        c.gridx = 0;
        c.gridy = 10;
        c.gridwidth = 14;
        c.gridheight = 8;
        gridBagLayout.setConstraints(nbp, c);
        //设置Basic Operation Button的GridBag属性
        //下面全是magic number,尽量不要改
        c.weightx = 0.3;
        c.weighty = 0.6;
        c.gridx = 15;
        c.gridy = 6;
        c.gridwidth = 6;
        c.gridheight = 12;
        gridBagLayout.setConstraints(bop, c);
        //究极无敌 add
        add(hd);
        add(mbb);
        add(fp);
        add(nbp);
        add(bop);
        this.setVisible(true);
    }
}
