package controller;

import model.InvalidExpressionException;
import model.UnaryCalculator;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ExpressionUnaryFilterTest {

    ExpressionUnaryFilter filter = new ExpressionUnaryFilter("5+3+()+((5+2)*3)+(2)+(2.2)+abs(-9)+5%");

    @Test
    public void removeUnnecessaryBrackets() {
        filter.restoreOrigin();
        filter.removeUnnecessaryBrackets();
        System.out.println(filter.getResult());
        assertEquals("5+3+0+((5+2)*3)+2+2.2+abs-9+5%", filter.getResult());
    }

    @Test
    public void replaceRegex() throws InvalidExpressionException {
        filter.replaceRegex("√\\((\\d(\\.\\d)*)+\\)", 1,
                (substring) -> UnaryCalculator.calcUnary(new BigDecimal(substring),
                        UnaryCalculator.UnaryOperator.SQRT).toString());
        filter.replaceRegex("abs\\((-*\\d(\\.\\d)*)+\\)", 1,
                (substring) -> UnaryCalculator.calcUnary(new BigDecimal(substring),
                        UnaryCalculator.UnaryOperator.ABS).toString());
        filter.replaceRegex("(\\d(\\.\\d)*)+%", 1,
                (substring) -> UnaryCalculator.calcUnary(new BigDecimal(substring),
                        UnaryCalculator.UnaryOperator.PERCENT).toString());
        System.out.println(filter.getResult());
        filter.removeUnnecessaryBrackets();
        System.out.println(filter.getResult());
        assertEquals("5+3+0+((5+2)*3)+2+2.2+9+0.05", filter.getResult());
    }
}