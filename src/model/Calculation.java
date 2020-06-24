package model;

public class Calculation {
    public enum SUF_SYMBOl {TIMES,PLUS,DIVIDES};
    private SUF_SYMBOl sufSymbol;
    public enum PRE_SYMBOl {TIMES,PLUS,DIVIDES};
    private PRE_SYMBOl preSymbol;

    public enum CURRENT_STATE{};
    private CURRENT_STATE current_state;
}
