import javax.swing.*;
import java.awt.*;


/**
 * 使用GridBagLayout作为布局的JFrame之类
 * 构造函数会创建一个完整的计算器窗口
 */
class MainGridBagLayoutWindows extends JFrame {
    //计算器窗口顶部标题栏的标题
    static String windowsTitle = "标准计算器";
    //计算器窗口内header框框的默认字
    static String headerDefaultText = "000";
    MainGridBagLayoutWindows() {
        //设置JFrame属性
        super(windowsTitle);
        //setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(340,500));
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screensize.width/2-170,screensize.height/2-250);
        setSize(340,500);
        //定义计算器窗口内组件
        Header hd = new Header();
        MButtonBar mbb = new MButtonBar();
        FunctionPad fp = new FunctionPad();
        NumberPad nbp = new NumberPad();
        BasicOperationPad bop = new BasicOperationPad();
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
        c.weighty = 0.3;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 5;
        MainWindow.resultTextField = hd.jTextField;
        hd.jTextField.setText(headerDefaultText);
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
