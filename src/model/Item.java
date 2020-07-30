package model;

import java.math.BigDecimal;

/**
 * 定义Item类作为Operator和Number(Digital)的基类<br>
 * 我想使用Union的写法来匹配一个表达式Item,<br>
 * 目前是这样定义的<br>
 * Item:
 * <ul>
 *     <li>BigDecimal digit</li>
 *     <li>Item.OPERATOR operator</li>
 * </ul>
 * 然后子类Digit和Operator分别对自己所属的类型对
 * 上面两个protected成员进行访问,封装起来,对外只能访问一种.
 */
public abstract class Item {
    /**
     * 定义Operator的枚举
     */
    public enum OPERATOR {
        PLUS, MINUS, TIMES, DIVIDES, LEFT_BRACKET, RIGHT_BRACKET;
    }

    ;
    /**
     * 供子类Operator用
     */
    protected OPERATOR operator;
    /**
     * 供子类Num用
     */
    protected BigDecimal digit;

    /**
     * 重写toString
     * 根据子类类型输出字符串
     *
     * @return String
     */
    @Override
    public String toString() {
        if (this instanceof Operator) {
            return this.operator.toString();
        } else {
            return this.digit.toString();
        }
    }
}
