package controller;

import model.Calculation;
import model.Digit;
import model.Item;
import model.Operation;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;

public class CalcController {
    Calculation model;
    static CalcController controller=new CalcController();
    public static CalcController getInstance(){
        return controller;
    }
    private CalcController(){
        this.model = new Calculation();
    }
    public void updateModel(String exp){
        this.model.doCalculation(stringToStack(exp));
    }
    public BigDecimal updateView(){
        return this.model.getResult();
    }
    /**
     * cast string to a infix expression stack
     * @param exp  the string to be cast , which is expected to contain no space
     * @return a stack of item (with the first number on the top)
     */
    private static Deque<Item> stringToStack(String exp){
        Deque<Item> stack = new ArrayDeque<>();
        for (int i = 0; i < exp.length(); ) {
            if (exp.charAt(i) >= '0' && exp.charAt(i) <= '9') {
                int j = i + 1;
                while (j<exp.length()&&(exp.charAt(j) >= '0' && exp.charAt(j) <= '9'||exp.charAt(j)=='.')) {
                    j++;
                }
                stack.addLast(new Digit(exp.substring(i, j)));
                System.out.println(exp.substring(i,j));
                i = j;
            } else {
                System.out.println("not num"+exp.charAt(i));
                stack.addLast(new Operation(exp.charAt(i)));
                i += 1;
            }
        }
        return stack;
    }
}
