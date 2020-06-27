package controller;

import model.Calculation;

import java.math.BigDecimal;

public class CalcController {
    Calculation model = new Calculation();
    static CalcController controller=new CalcController();
    public static CalcController getInstance(){
        return controller;
    }
    private CalcController(){

    }
    String expression="900+900";
    char operator='=';
    BigDecimal result=new BigDecimal("1800");

    public char getOperator() {
        return operator;
    }

    public String getExpression() {
        return expression;
    }

    public BigDecimal getResult() {
        return result;
    }


    public static enum BINARY_SYMBOl {TIMES, PLUS, DIVIDES, MINUS, EQUALS, MOD}

    ;
    private BINARY_SYMBOl binary_symbol;

    public static enum UNARY_SYMBOl {ONE_DIVIDES_X, X_SQUARE, SQRT}

    ;
    private UNARY_SYMBOl unary_symbol;

    public static enum CURRENT_STATE {}

    ;
    private CURRENT_STATE current_state;
}
