package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.*;

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
 * 事件的编写在{@link MButtonClickHandler},
 * //如果不够时间这里可以考虑只搞一个Memory,那就写一个static的BigDecimal变量就好了
 * 实际上最后搞了一个List,实现了列表功能
 */
class MemoryButtonBar extends JPanel implements CanTurnErrorState {
    /**
     * 这个是事件监听的实例,不用管她
     */
    private ButtonClickHandler buttonClickHandler;
    private final MyArrayList<BigDecimal> decimalList = new MyArrayList<>();

    public ArrayList<BigDecimal> getDecimalList() {
        return this.decimalList;
    }
    public boolean hasMemory(){
        return this.getDecimalList().size()>0;
    }

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
    private MemoryButton MCButton;
    private MemoryButton MRButton;
    private MemoryButton MPlusButton;
    private MemoryButton MMinusButton;
    private MemoryButton MSaveButton;
    /**
     * 单例模式的单例
     */
    private static MemoryButtonBar mButtonBar;

    /**
     * 单例模式获取实例成员对象的方法
     *
     * @return 返回一个Bar的实例
     */
    public static MemoryButtonBar getInstance() {
        if (mButtonBar == null)
            mButtonBar = new MemoryButtonBar();
        return mButtonBar;
    }

    private MemoryButtonBar() {
        this.buttonClickHandler = new MButtonClickHandler();
        setLayout(new GridLayout(1, 5, 1, 1));
        this.add(MCButton = new MemoryButton(MButtonStrings[0], buttonClickHandler));
        this.add(MRButton = new MemoryButton(MButtonStrings[1], buttonClickHandler));
        this.add(MPlusButton = new MemoryButton(MButtonStrings[2], buttonClickHandler));
        this.add(MMinusButton = new MemoryButton(MButtonStrings[3], buttonClickHandler));
        this.add(MSaveButton = new MemoryButton(MButtonStrings[4], buttonClickHandler));
        this.MCButton.setEnabled(false);
        this.MRButton.setEnabled(false);
    }

    /**
     * 如果没有内存的时候,按钮要进入无内存状态
     * @param bool true时进入无内存状态,false进入有内存状态
     */
    public void setNoMemoryState(boolean bool) {
        //获取全部的components
        for (Component component : this.getComponents()) {
            //对于是按钮的组件
            if (component instanceof MemoryButton) {
                //这里就排除一些不变灰的按钮,让能变灰的都变灰
                //这里主要是参考windows的标准计算器的样子来搞的
                if (!((MemoryButton) component).getText().contains("MC") &&
                        !((MemoryButton) component).getText().contains("MR")) {
                } else component.setEnabled(!bool);
            }
            //Debug code
            //System.out.println(component.getClass().toString());
        }
    }

    private boolean isError = false;

    @Override
    public boolean isErrorState() {
        return isError;
    }

    @Override
    /**
     * 除零等错误禁止m+m-等
     */
    public void setErrorState(boolean bool) {
        //获取全部的components
        for (Component component : this.getComponents()) {
            //对于是按钮的组件
            if (component instanceof MemoryButton) {
               component.setEnabled(!bool);
            }
            //Debug code
            //System.out.println(component.getClass().toString());
        }
        if(decimalList.isEmpty()||decimalList.size()<1){
            setNoMemoryState(true);
        }
        isError = bool;
    }

    private class MemoryButton extends JButton {
        MemoryButton(String text, ButtonClickHandler handler) {
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

        BigDecimal latestDigit;
        boolean aBoolean = false;

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton jb = (JButton) (e.getSource());

            String temp = jb.getText();
            String text = "Memory Button, You have Clicked: " + temp;
            System.out.println(text);
            //CalculatorWindow.resultTextField.setText(text);

            switch (temp) {
                case MemoryPlus:
                    if (decimalList.size() == 0) {
                        decimalList.add(new BigDecimal("0"));
                    }
                    latestDigit = TextHeader.getLatestDigit();
                    decimalList.push(decimalList.pop().add(latestDigit));
                    break;
                case MemoryMinus:
                    if (decimalList.size() == 0) {
                        decimalList.add(new BigDecimal("0"));
                    }
                    latestDigit = TextHeader.getLatestDigit();
                    decimalList.push(decimalList.pop().subtract(latestDigit));
                    break;
                case MemoryRead:
                    TextHeader.getInstance().setLatestDigit(decimalList.peek());
                    break;
                case MemoryClear:
                    decimalList.clear();
                    break;
                case MemorySave:
                    decimalList.push(TextHeader.getLatestDigit());
            }
            if (CalculatorFrame.getInstance().memoryToolFrame != null)
                MemoryToolFrame.memoryJList.
                        setListData(MemoryButtonBar.this.getDecimalList().toArray(new BigDecimal[0]));
            aBoolean = decimalList.size() == 0;
            setNoMemoryState(aBoolean);
        }
    }
}
class MyArrayList<E> extends ArrayList<E>{
    MyArrayList(){
        super();
    }
    public E pop(){
        E re = this.get(0);
        remove(0);
        return re;
    }
    public void push(E element){
        add(0,element);
    }
    public E peek(){
        return this.get(0);
    }
}

