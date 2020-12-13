package model;

public class DivideByZeroException extends CalculationException{
    @Override
    public String getMessage() {
        return "除数不能为零";
    }
}
