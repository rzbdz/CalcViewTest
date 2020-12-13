package controller;

import model.DivideByZeroException;
import model.InvalidExpressionException;
import org.junit.Test;
import view.CalculatorFrame;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class CalculationControllerTest {

    @Test
    public void stringToInfixStack() throws DivideByZeroException, InvalidExpressionException {
        CalculationController ctl = CalculationController.getInstance();
       ctl.updateModel("5+3+()+((5+2)*3)+(2)+(2.2)-abs(-6)+√(9)+5%");
       System.out.println(ctl.updateView());
       assertEquals(ctl.updateView(),new BigDecimal(30.25));
    }
}