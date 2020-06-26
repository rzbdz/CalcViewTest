package controller;

import model.Calculation;

public class CalcController {
    Calculation model = new Calculation();
    public static enum BINARY_SYMBOl {TIMES,PLUS,DIVIDES,MINUS,EQUALS,MOD};
    private  BINARY_SYMBOl binary_symbol;
    public static enum UNARY_SYMBOl {ONE_DIVIDES_X,X_SQUARE,SQRT};
    private UNARY_SYMBOl unary_symbol;
    public static enum CURRENT_STATE{};
    private CURRENT_STATE current_state;
}
