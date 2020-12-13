package model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * 一元计算器静态类
 */
public interface UnaryCalculator {
    /**
     * 支持的一元运算分别是%,abs和sqrt
     */
    static enum UnaryOperator {PERCENT, ABS, SQRT}

    static BigDecimal calcUnary(BigDecimal decimal, UnaryOperator operator) throws InvalidExpressionException {
        BigDecimal resultDecimal;
        //System.out.println(decimal.toString());
        switch (operator) {
            case PERCENT:
                resultDecimal = decimal.divide(BigDecimal.valueOf(100), 9, RoundingMode.HALF_UP).stripTrailingZeros();
                break;
            case ABS:
                resultDecimal = decimal.abs();
                break;
            case SQRT:
                if (decimal.compareTo(new BigDecimal("0")) < 0) {
                    throw  new InvalidExpressionException();
                }
                resultDecimal = decimal.sqrt(MathContext.DECIMAL64);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + operator);
        }
        return resultDecimal;
    }
}
