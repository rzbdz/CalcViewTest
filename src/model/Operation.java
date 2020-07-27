package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Item的子类,存运算符
 * 理想的是用一个Union
 * 这里 伪
 */
public class Operation extends Item {
    /**
     * 多对一Map优先级
     */
    static Map<opt, Integer> map = new HashMap<>();
    static int BRACKET_PRIORITY = 2;

    /**
     * 静态代码块
     */
    static {
        map.put(opt.DIVIDES, 1);
        map.put(opt.PLUS, 0);
        map.put(opt.MINUS, 0);
        map.put(opt.TIMES, 1);
        map.put(opt.LEFTBRA, 2);
        map.put(opt.RIGHTBRA, 2);
    }

    public opt getOption() {
        return super.option;
    }

    /**
     * 用枚举初始化
     *
     * @param opti
     */
    public Operation(opt opti) {
        this.option = opti;
    }

    /**
     * 用char初始化
     *
     * @param o
     */
    public Operation(char o) {
        if (o == '+'||o=='＋') {
            this.option = opt.PLUS;
        } else if (o == '-') {
            this.option = opt.MINUS;
        } else if (o == '×'||o=='*') {
            this.option = opt.TIMES;
        } else if (o == '÷'||o=='/') {
            this.option = opt.DIVIDES;
        } else if (o == '('||o=='（') {
            this.option = opt.LEFTBRA;
        } else if (o == ')'||o=='）') {
            this.option = opt.RIGHTBRA;
        } else {
            this.option = null;
        }
    }

    /**
     * 获取优先级
     *
     * @return
     */
    int getPriority() {
        return map.get(this.option);
    }

    /**
     * 是不是左括号
     *
     * @return
     */
    boolean isLeftBracket() {
        return this.option == opt.LEFTBRA;
    }

    /**
     * 是不是右括号
     *
     * @return
     */
    boolean isRightBracket() {
        return this.option == opt.RIGHTBRA;
    }

    /**
     * 是不是括号
     *
     * @return
     */
    boolean isBracker() {
        return this.option == opt.LEFTBRA || this.option == opt.RIGHTBRA;
    }
}
