package model;

import java.math.BigDecimal;

/**
 * 数字子类<br>
 * 用BigDecimal的原因是,无论是int还是Double,都是不准的,
 * Double无法精确表示int,int表示不了小数.
 * 最安全的方法是使用BigDecimal,还有一个要点是
 * 创建BigDecimal是不能用Double/Integer的,
 * 必须用String来保证精准.
 * 对一元运算的想法:<br>
 * <p>View读到了如之前是1+1+3,
 * 这是resultText应该是3
 * (decimal也是3),然后来一个abs
 * 然后,搞一个专门应付一元的类和controller,
 * 或者view自己就把一元计算给搞了,
 * 但是它拼接的expressionText就有一元的符号了,
 * 比如1+1+3+abs(3),或者还有sqrt(3),etc...
 * 解决方案有两种,一种是view搞两套字符串,
 * 一个是expressionTextForBinaryCalc,一个
 * 是expressionTextForView.然后它给controller传的是
 * Binary的,里面是没有sqrt的,自己保留的那个还是有的.
 * 还有8天这个可以搞一下.
 * </p>
 */
public class Digit extends Item {
    public Digit(String str) {
        this.digit = new BigDecimal(str);
    }

    /**
     * 封装了访问Decimal,不能访问Item的Operator.
     * @return decimal
     */
    public BigDecimal getDecimal() {
        return super.digit;
    }
}
