package view;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public interface Theme {
    enum ThemeEnum {SYSTEM, DARK, LIGHT}


    /**
     * 警告,不支持程序内动态更换主题,如果要动态更换,请使用
     * 参数版
     *
     * @link Theme#setDarkLooksAndFeels(Component)}
     */
    public static void setSystemLooksAndFeels() {
        setLNF(null, ThemeEnum.SYSTEM);
    }

    /**
     * 警告,不支持程序内动态更换主题,如果要动态更换,请使用
     * 参数版
     *
     * @link Theme#setDarkLooksAndFeels(Component)}
     */
    public static void setLightLooksAndFeels() {
        setLNF(null, ThemeEnum.LIGHT);

    }

    /**
     * 警告,不支持程序内动态更换主题,如果要动态更换,请使用
     * 参数版
     *
     * @link Theme#setDarkLooksAndFeels(Component)}
     */
    public static void setDarkLooksAndFeels() {
        setLNF(null, ThemeEnum.DARK);

    }

    /**
     * 支持程序内动态更换主题
     */
    public static void setSystemLooksAndFeels(Component component) {
        setLNF(CalculatorFrame.getInstance(), ThemeEnum.SYSTEM);

    }

    /**
     * 支持程序内动态更换主题
     */
    public static void setLightLooksAndFeels(Component component) {
        setLNF(CalculatorFrame.getInstance(), ThemeEnum.LIGHT);

    }

    /**
     * 支持程序内动态更换主题
     */
    public static void setDarkLooksAndFeels(Component component) {
        setLNF(CalculatorFrame.getInstance(), ThemeEnum.DARK);

    }

    private static void setLNF(Component rootComponent, ThemeEnum themeEnum) {
        LookAndFeel lnf;
        try {
            switch (themeEnum) {
                case SYSTEM:
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                case DARK:
                    UIManager.setLookAndFeel(new FlatDarculaLaf());
                    break;
                case LIGHT:
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    break;
            }
        } catch (Exception ex) {
            System.err.println("主题应用失败");
        }
        if (rootComponent != null){
            SwingUtilities.updateComponentTreeUI(rootComponent);
            rootComponent.repaint();
        }
    }
}
