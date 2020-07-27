package model;

import java.math.BigDecimal;

/**
 * 定义Item类作为Operator和Number(Digital)的基类
 */
public abstract class Item {
    /**
     * 定义Operator的枚举
     */
    public enum opt {
        PLUS, MINUS, TIMES, DIVIDES, LEFTBRA, RIGHTBRA;
    }

    ;
    /**
     * 供子类Operator用
     */
    public opt option;
    /**
     * 供子类Num用
     */
    public BigDecimal bigDecimal;

    /**
     * 重写toString
     * 根据子类类型输出字符串
     *
     * @return String
     */
    @Override
    public String toString() {
        if (this instanceof Operation) {
            return this.option.toString();
        } else {
            return this.bigDecimal.toString();
        }
    }
}
