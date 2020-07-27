package model;

import java.math.BigDecimal;

/**
 * 数字子类
 */
public class Digit extends Item {
    public Digit(String str) {
        this.bigDecimal = new BigDecimal(str);
    }

    public BigDecimal getDecimal() {
        return super.bigDecimal;
    }
}
