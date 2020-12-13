package view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * 这个是内存工具的窗口,
 * 就是那个程序顶部菜单栏的一个弹出窗口.
 * 实现对绑定List数据模型的实时更新,
 * 支持单个项目m+m-等功能操作
 */
public class MemoryToolFrame extends JFrame {
    public static final int minWidth = 320;
    public static final int minHeight = 400;

    public static JList<BigDecimal> memoryJList = new JList<BigDecimal>();

    public MemoryToolFrame() {
        super();
        setIconImage(Toolkit.getDefaultToolkit().
                getImage(CalculatorFrame.class.getClassLoader().getResource("res/icon.png")));
        setTitle("内存记录");
        initializeComponents();
    }

    public MemoryToolFrame(int x, int y) {
        super();
        setIconImage(Toolkit.getDefaultToolkit().
                getImage(CalculatorFrame.class.getClassLoader().getResource("res/icon.png")));
        setTitle("内存记录");
        initializeComponents(x, y);
    }

    private void initializeComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void initializeComponents(int x, int y) {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocation(x, y);
        setSize(minWidth, minHeight);
        memoryJList.setFont(new BasicFont(BasicFont.PLAIN, 24));
        memoryJList.setListData((BigDecimal[]) MemoryButtonBar.getInstance().getDecimalList().toArray(new BigDecimal[0]));
        memoryJList.setAlignmentX(Component.RIGHT_ALIGNMENT);
        memoryJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenuItem read = jPopupMenu.add(new JMenuItem("MR"));
        read.addActionListener(e -> TextHeader.getInstance().setLatestDigit(memoryJList.getSelectedValue()));
        JMenuItem plus = jPopupMenu.add(new JMenuItem("M+"));
        plus.addActionListener((e) -> {
            int index = memoryJList.getSelectedIndex();
            var decimal = memoryJList.getSelectedValue();
            MemoryButtonBar.getInstance().getDecimalList().set(index, decimal.add(TextHeader.getLatestDigit()));
            memoryJList.setListData(MemoryButtonBar.getInstance().getDecimalList().toArray(new BigDecimal[0]));
        });
        JMenuItem minus = jPopupMenu.add(new JMenuItem("M-"));
        minus.addActionListener((e) -> {
            int index = memoryJList.getSelectedIndex();
            var decimal = memoryJList.getSelectedValue();
            MemoryButtonBar.getInstance().getDecimalList().set(index, decimal.subtract(TextHeader.getLatestDigit()));
            memoryJList.setListData(MemoryButtonBar.getInstance().getDecimalList().toArray(new BigDecimal[0]));
        });
        JMenuItem mc = jPopupMenu.add(new JMenuItem("MC"));
        mc.addActionListener(e -> {
            MemoryButtonBar.getInstance().getDecimalList().clear();
            MemoryButtonBar.getInstance().setNoMemoryState(true);
            memoryJList.setListData(MemoryButtonBar.getInstance().getDecimalList().toArray(new BigDecimal[0]));
        });
        JMenuItem copy = jPopupMenu.add(new JMenuItem("复制"));
        copy.addActionListener((e) -> {
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            cb.setContents(new StringSelection(memoryJList.getSelectedValue().toString()), null);
        });
        memoryJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseReleased(e);
                if (e.getButton() == 3 && memoryJList.locationToIndex(e.getPoint()) != -1) {
                    jPopupMenu.setVisible(true);
                    memoryJList.setSelectedIndex(memoryJList.locationToIndex(e.getPoint()));
                    jPopupMenu.show(memoryJList, e.getX(), e.getY());
                }else{
                    jPopupMenu.setVisible(false);
                }

            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }
        });
        JScrollPane js = new JScrollPane(memoryJList);
        js.setBorder(BorderFactory.createEmptyBorder());
        setLayout(new BorderLayout());
        JLabel title = new JLabel("内存列表", SwingConstants.CENTER);
        title.setFont(new BasicFont(BasicFont.PLAIN, 24));
        this.add(title, BorderLayout.NORTH);
        this.add(js);
        this.add(new JLabel("右键显示操作菜单!", SwingConstants.CENTER), BorderLayout.SOUTH);
        this.setVisible(true);
    }
}
