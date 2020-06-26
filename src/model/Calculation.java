package model;

import java.math.BigDecimal;

public class Calculation {
    public String expression;
    public String lastOperation;
    public BigDecimal currentDigit;

    @Override
    public String toString() {
        return expression;
    }
}
