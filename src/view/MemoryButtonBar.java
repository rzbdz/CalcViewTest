package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Bar 顾名思义就是程序文本框下面的那一排M+M-MR 按键.
 * <p>这个类是 M 键所在的JPanel,M键的解释是:<br>
 * 计算器里面有一个内存,M就是Memory<br>
 * M+：记忆当前数字，累加数字当中。du<br>
 * M-：记忆当前数字，以负zhi数的形式累加数字当中。即：把总数扣dao除当前数字。<br>
 * MC：Memory Clea，也就是将目前记忆的数字「归零」。<br>
 * MR：Memory Recall，将当前计算出来的数字呈现出来。<br>
 * MS：无视目前记忆多少数字，直接以当前数字取代记忆中的数字。<br>
 * 具体可以参照微软计算器的内存栏和M键</p>
 * <br>
 * 和JFrame相同,这里也是使用单例模式,没有DCL锁,编写事件时通过<br>
 * {@code MemoryButtonBar.getInstance()}来获取一个实例.<br>
 * 注:对MemoryButtonBar可能出现的外部访问的需求如:<br>
 * 出现除零错误时需要禁止所有按键({@code setEnable(false)})<br>
 * <p>作为JPanel, 程序约定所有JPanel内的Button的字符<br>
 * 串都以静态成员的形式表示出来(当成是枚举或常量使用)</p>
 * 这里含有的静态常量是:<br>
 * {@link MemoryButtonBar#MemoryClear}<br>
 * {@link MemoryButtonBar#MemoryRead}<br>
 * {@link MemoryButtonBar#MemoryPlus}<br>
 * {@link MemoryButtonBar#MemoryMinus}<br>
 * {@link MemoryButtonBar#MemorySave}<br>
 *  事件的编写在{@link MButtonClickHandler},
 *  这里可以考虑只搞一个Memory,那就写一个static的BigDecimal变量就好了
 *
 */
class MemoryButtonBar extends JPanel {
    /**
     * 这个是事件监听的实例,不用管她
     */
    private ButtonClickHandler buttonClickHandler;
    public static final String MemoryClear = "MC";
    public static final String MemoryRead = "MR";
    public static final String MemoryPlus = "M+";
    public static final String MemoryMinus = "M-";
    public static final String MemorySave = "MS";
    /**
     * 这个数组是供For循环创建这些按钮
     */
    static String[] MButtonStrings =
            {MemoryClear, MemoryRead, MemoryPlus, MemoryMinus, MemorySave};
    /**
     * 具体Button的实例
     */
    private MButton MCButton;
    private MButton MRButton;
    private MButton MPlusButton;
    private MButton MMinusButton;
    private MButton MSaveButton;
    /**
     * 单例模式的单例
     */
    private static MemoryButtonBar mButtonBar;

    /**
     * 单例模式获取实例成员对象的方法
     * @return 返回一个Bar的实例
     */
    public static MemoryButtonBar getInstance() {
        if (mButtonBar == null)
            mButtonBar = new MemoryButtonBar();
        return mButtonBar;
    }

    private MemoryButtonBar() {
        this.buttonClickHandler = new MButtonClickHandler();
        setLayout(new GridLayout(1, 5));
        this.add(MCButton = new MButton(MButtonStrings[0], buttonClickHandler));
        this.add(MRButton = new MButton(MButtonStrings[1], buttonClickHandler));
        this.add(MPlusButton = new MButton(MButtonStrings[2], buttonClickHandler));
        this.add(MMinusButton = new MButton(MButtonStrings[3], buttonClickHandler));
        this.add(MSaveButton = new MButton(MButtonStrings[4], buttonClickHandler));
        this.MCButton.setEnabled(false);
        this.MRButton.setEnabled(false);
    }

    private class MButton extends JButton {
        MButton(String text, ButtonClickHandler handler) {
            super(text);
            setFocusable(false);
            setFont(new BasicFont(Font.BOLD, 12));
            this.addActionListener(handler);
        }
    }

    /**
     * 在这里编写M键按钮点击的事件处理
     * 需要重写方法:
     * public void actionPerformed(ActionEvent e);
     */
    private class MButtonClickHandler extends ButtonClickHandler {
        MButtonClickHandler() {
            super();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton jb = (JButton) (e.getSource());
            String text = "you pressed" + jb.getText();
            System.out.println(text);
            //CalculatorWindow.resultTextField.setText(text);
        }
    }
}

