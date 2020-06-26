package model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

//一个转换中缀表达式为后缀表达式的测试类
public class TestCalcStack {
    public static void main(String[] args) {
        String expression = "2*（3+5）+7/1-4";
        Stack<Item> stack = new Stack<>();
        Stack<Item> optCache = new Stack<>();
        int lastIndex = 0;
        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (ch == '*' || ch == '+' || ch == '-' || ch == '/' || ch == '(' || ch == ')' || i == expression.length() - 1) {
                if (i - 1 - lastIndex > 0)
                    stack.push(new Num(expression.substring(lastIndex, i - 1)));
                lastIndex = i + 1;
                Option currentOpt = new Option(expression.charAt(i));
                if(currentOpt.getPriority()>((Option)optCache.peek()).getPriority()){
                    optCache.push(currentOpt);
                }

            }

        }
    }

}

abstract class Item {
    public enum opt {PLUS, MINUS, TIMES, DIVIDES, LEFTBRA, RIGHTBRA}

    ;
    public opt option;
    public BigDecimal bigDecimal;
}

class Option extends Item {
    static Map<opt, Integer> map = new HashMap<>();

    static {
        map.put(opt.DIVIDES, 1);
        map.put(opt.PLUS, 0);
        map.put(opt.MINUS, 0);
        map.put(opt.TIMES, 1);
        map.put(opt.LEFTBRA, 2);
        map.put(opt.RIGHTBRA, 2);
    }

    Option(opt opti) {
        this.option = opti;
    }

    Option(char o) {
        if (o == '+') {
            this.option = opt.PLUS;
        } else if (o == '-') {
            this.option = opt.MINUS;
        } else if (o == '*') {
            this.option = opt.TIMES;
        } else if (o == '/') {
            this.option = opt.DIVIDES;
        } else if (o == '(') {
            this.option = opt.LEFTBRA;
        } else if (o == ')') {
            this.option = opt.RIGHTBRA;
        }
    }

    int getPriority() {
        return map.get(this.option);
    }
    boolean isLeftBracket(){
        return this.option==opt.LEFTBRA;
    }
    boolean isRightBracket(){
        return this.option==opt.RIGHTBRA;
    }
    boolean isBracker(){
        return this.option==opt.LEFTBRA||this.option==opt.RIGHTBRA;
    }
}

class Num extends Item {
    Num(String str) {
        this.bigDecimal = new BigDecimal(str);
    }
}
