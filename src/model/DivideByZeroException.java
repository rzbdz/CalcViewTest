package model;

public class DivideByZeroException extends java.lang.ArithmeticException{
    @Override
    public String getMessage() {
        return "除数不能为零";
    }
}
