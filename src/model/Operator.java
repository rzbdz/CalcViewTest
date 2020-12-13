package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Item的子类,存运算符
 * 理想的是用一个Union
 * 这里 伪Union
 */
public class Operator extends Item {
    /**
     * 多对一Map优先级
     */
    static Map<OPERATOR, Integer> priorityMap = new HashMap<>();
    static int BRACKET_PRIORITY = 3;
    static int POW_PRIORITY = 2;
    static int TIMES_DIVIDES_PRIORITY = 1;
    static int PLUS_MINUS_PRIORITY = 0;

    /**
     * 静态代码块
     */
    static {
        priorityMap.put(OPERATOR.DIVIDES, TIMES_DIVIDES_PRIORITY);
        priorityMap.put(OPERATOR.PLUS, PLUS_MINUS_PRIORITY);
        priorityMap.put(OPERATOR.MINUS, PLUS_MINUS_PRIORITY);
        priorityMap.put(OPERATOR.TIMES, TIMES_DIVIDES_PRIORITY);
        priorityMap.put(OPERATOR.LEFT_BRACKET, BRACKET_PRIORITY);
        priorityMap.put(OPERATOR.RIGHT_BRACKET, BRACKET_PRIORITY);
        priorityMap.put(OPERATOR.POW, POW_PRIORITY);
    }

    public OPERATOR getOperator() {
        return super.operator;
    }

    /**
     * 用枚举初始化
     *
     * @param operator Item.OPERATOR 枚举来赋值
     */
    public Operator(OPERATOR operator) {
        this.operator = operator;
    }

    /**
     * 用char初始化<br>
     * 考虑了+和＋(两个不同的符号),
     * /和÷(一个是Debug和Test用,一个是UI传入的÷)等等<br>
     * 要添加符号,就要来这里同步修改
     *
     * @param operationCharacter 一个合法的char运算符,考虑多对一
     */
    public Operator(char operationCharacter) {
        if (operationCharacter == '+' || operationCharacter == '＋') {
            this.operator = OPERATOR.PLUS;
        } else if (operationCharacter == '-') {
            this.operator = OPERATOR.MINUS;
        } else if (operationCharacter == '×' || operationCharacter == '*') {
            this.operator = OPERATOR.TIMES;
        } else if (operationCharacter == '÷' || operationCharacter == '/') {
            this.operator = OPERATOR.DIVIDES;
        } else if (operationCharacter == '(' || operationCharacter == '（') {
            this.operator = OPERATOR.LEFT_BRACKET;
        } else if (operationCharacter == ')' || operationCharacter == '）') {
            this.operator = OPERATOR.RIGHT_BRACKET;
        } else if (operationCharacter=='^') {
            this.operator = OPERATOR.POW;
        } else {
            this.operator = null;
        }
    }

    /**
     * 获取优先级
     *
     * @return
     */
    int getPriority() {
        return priorityMap.get(this.operator);
    }

    /**
     * 是不是左括号
     *
     * @return
     */
    boolean isLeftBracket() {
        return this.operator == OPERATOR.LEFT_BRACKET;
    }

    /**
     * 是不是右括号
     *
     * @return
     */
    boolean isRightBracket() {
        return this.operator == OPERATOR.RIGHT_BRACKET;
    }

    /**
     * 是不是括号
     *
     * @return
     */
    boolean isBracker() {
        return this.operator == OPERATOR.LEFT_BRACKET || this.operator == OPERATOR.RIGHT_BRACKET;
    }
}
