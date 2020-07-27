package model;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Calculation Model
 * initialized with a Deque of Item
 * e.g. 1+2+3 , 1 is on the top of the deque.
 * use doCalculation to update result
 * use getResult to get result;
 */
public class Calculation {

    Deque<Item> infixStack;

    public BigDecimal getResult() {
        return result;
    }

    BigDecimal result;
    public Calculation(){
        this.infixStack = null;
        this.result = null;
    }
    public Calculation(Deque<Item> infixStack) {
        this.infixStack = infixStack;
        this.result = null;
    }
    public void doCalculation(Deque<Item> infixStack){
        this.infixStack = infixStack;
        doCalculation();
    }
    public void doCalculation(){
            this.result = suffixToResult(infixToSuffix(this.infixStack));
    }

    /**
     * 中缀转后缀(只支持+-*除)
     * <p>
     * 传入一个Stack,内容是Item
     * <p>
     * 格式应该是:peek() 获得的是表达式的最开始一个
     * 如:1+2*3+2,stack的peek到bottom依次是:1,+,2,*,3,+,2
     * peek()应该是1而不是2.
     * <p>
     * 进行的处理是:
     * 转换成一个Output,后缀表达式,peek端是后缀表达式的最后一个
     * 如:1+2*3+2,转换成 1,2,3,*,+,2,+
     * 返回的就是stack:{peek:+,{2,+,*,3,2},bottom:1}
     * <p>
     * 括号没写
     * <p>
     * 返回还没写
     *
     * @param Infix 传入一个Stack,内容是Item,使用中缀表达式的stack
     * @return
     */
    private Deque<Item> infixToSuffix(Deque<Item> Infix) {
        Deque<Item> Output = new ArrayDeque<>();
        //用来处理运算符
        MyStack<Item> Operators = new MyStack<>();

        //2020.7.2
        Item item;
        while (!Infix.isEmpty()) {
            //中缀里,第一个元素是表达式的第一个.
            // 如果非空就pop一个元素
            item = Infix.pop();
            if (item instanceof Digit) {
                //如果是数字,push进Output 栈里
                Output.push(item);
            } else if (((Operation) item).isRightBracket()) {
                while (!Operators.isEmpty() && !(((Operation) Operators.peek()).isLeftBracket())) {
                    Output.push(Operators.pop());
                }
                Operators.pop();

            } else if (!Operators.isInBracket() && !Operators.isPrior(item) || Infix.isEmpty()) {
                //如果是当前元素不是优先运算符或者是结尾,如+-,
                //说明前一段的优先运算结束了
                while (!Operators.isEmpty()) {
                    //pop前一段运算
                    Output.push(Operators.pop());
                }
                Operators.push(item);
            } else {
                //如果不是优先运算符,要先进Operators栈
                //直到遇到下一个优先运算符
                Operators.push(item);
            }

        }
        //如果Infix结束了, 清空Operator
        while (!Operators.isEmpty()) {
            Output.push(Operators.pop());
        }
        //如果Ouput非空输出
        return Output;
    }

    private BigDecimal suffixToResult(Deque<Item> suffixStack) {
        Item i = suffixStack.pop();
        if (suffixStack.isEmpty()) {
            return ((Digit) i).getDecimal();
        } else if (i instanceof Operation) {
            return calcBinary(suffixToResult(suffixStack), suffixToResult(suffixStack), (Operation) i);
        } else if (i instanceof Digit) {
            return ((Digit) i).getDecimal();
        } else {
            return ((Digit) i).getDecimal();
        }
    }

    private BigDecimal calcBinary(BigDecimal b1, BigDecimal b2, Operation operation) {
        switch (operation.option) {
            case PLUS:
                return b2.add(b1);
            case TIMES:
                return b2.multiply(b1);
            case MINUS:
                return b2.subtract(b1);
            case DIVIDES:
                return b2.divide(b1, 9, BigDecimal.ROUND_HALF_UP);
            default:
                return b2;
        }
    }

}

/**
 * 这个是用来给Operator的临时栈用的
 * 添加一个isPrior方法判断优先级
 * 主要是为了处理Stack为空的情况
 *
 * @param <E>
 */
class MyStack<E> extends ArrayDeque<E> {
    public boolean isPrior(Object o) {
        boolean result = false;
        Operation item = (Operation) o;
        if (!this.isEmpty() && item.getPriority() >= ((Operation) (this.peek())).getPriority()) {
            result = true;
        }
        return result;
    }

    public boolean isInBracket() {
        return (!this.isEmpty()) && ((Operation) this.peek()).isLeftBracket();
    }
}


