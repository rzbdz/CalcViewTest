package model;

import controller.CalculationController;

public class InvalidExpressionException extends CalculationException {
    @Override
    public String getMessage() {
        return "无效输入";
    }
}
