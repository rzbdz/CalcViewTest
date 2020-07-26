import java.math.BigDecimal;
import java.util.*;

/**
 * 一个转换中缀表达式为后缀表达式的测试类
 */
public class TestCalcStack {

    public static BigDecimal calc(BigDecimal b1, BigDecimal b2, Option o) {
        switch (o.option) {
            case PLUS:
                return b2.add(b1);
            case TIMES:
                return b2.multiply(b1);
            case MINUS:
                return b2.subtract(b1);
            case DIVIDES:
                return b2.divide(b1,9,BigDecimal.ROUND_HALF_UP);
            default:
                return b2;
        }
    }

    /**
     * cast string to a infix expression stack
     * @param exp  the string to be cast , which is expected to contain no space
     * @return a stack of item (with the first number on the top)
     */
    public static Deque<Item> stringToStack(String exp){
        Deque<Item> stack = new ArrayDeque<>();
        for (int i = 0; i < exp.length(); ) {
            if (exp.charAt(i) >= '0' && exp.charAt(i) <= '9') {
                int j = i + 1;
                while (exp.charAt(j) >= '0' && exp.charAt(j) <= '9') {
                    j++;
                }
                System.out.println(exp.substring(i,j));
                stack.addLast(new Num(exp.substring(i, j)));
                i = j;
            } else {
                System.out.println("not num"+exp.charAt(i));
                stack.addLast(new Option(exp.charAt(i)));
                i += 1;
            }
        }
        return stack;
    }

    public static void main(String[] args) {
        //官方不推荐使用java.util.Stack(继承Vector)类作为栈的实现
        //改成ArrayDeque代替
        //Stack<Item> stack = new Stack<>();

        String exp = "35-6*4/2+5*(1+1)";
        Deque<Item> stack = stringToStack(exp);
        for(var i : stack) System.out.println(i.toString());
        System.out.println(SuffixToResult(infixToSuffix(stack)).toString());

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
    public static Deque<Item> infixToSuffix(Deque<Item> Infix) {
        Deque<Item> Output = new ArrayDeque<>();
        //用来处理运算符
        MyStack<Item> Operators = new MyStack<>();

        //2020.7.2
        Item item;
        while (!Infix.isEmpty()) {
            //中缀里,第一个元素是表达式的第一个.
            // 如果非空就pop一个元素
            item = Infix.pop();
            if (item instanceof Num) {
                //如果是数字,push进Output 栈里
                Output.push(item);
            } else if (((Option) item).isRightBracket()) {
                while (!Operators.isEmpty() && !(((Option) Operators.peek()).isLeftBracket())) {
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

    public static BigDecimal SuffixToResult(Deque<Item> suffixStack) {
        Item i = suffixStack.pop();
        if (suffixStack.isEmpty()) {
            return ((Num) i).getDecimal();
        } else if (i instanceof Option) {
            return calc(SuffixToResult(suffixStack), SuffixToResult(suffixStack), (Option) i);
        } else if (i instanceof Num) {
            return ((Num) i).getDecimal();
        } else {
            return ((Num) i).getDecimal();
        }
    }

}


/**
 * 定义Item类作为Operator和Number(Digital)的基类
 */
abstract class Item {
    /**
     * 定义Operator的枚举
     */
    public enum opt {
        PLUS, MINUS, TIMES, DIVIDES, LEFTBRA, RIGHTBRA;
    }

    ;
    /**
     * 供子类Operator用
     */
    public opt option;
    /**
     * 供子类Num用
     */
    public BigDecimal bigDecimal;

    /**
     * 重写toString
     * 根据子类类型输出字符串
     *
     * @return String
     */
    @Override
    public String toString() {
        if (this instanceof Option) {
            return this.option.toString();
        } else {
            return this.bigDecimal.toString();
        }
    }
}

/**
 * Item的子类,存运算符
 * 理想的是用一个Union
 * 这里 伪
 */
class Option extends Item {
    /**
     * 多对一Map优先级
     */
    static Map<opt, Integer> map = new HashMap<>();
    static int BRACKET_PRIORITY = 2;

    /**
     * 静态代码块
     */
    static {
        map.put(opt.DIVIDES, 1);
        map.put(opt.PLUS, 0);
        map.put(opt.MINUS, 0);
        map.put(opt.TIMES, 1);
        map.put(opt.LEFTBRA, 2);
        map.put(opt.RIGHTBRA, 2);
    }

    public Item.opt getOption() {
        return super.option;
    }

    /**
     * 用枚举初始化
     *
     * @param opti
     */
    Option(opt opti) {
        this.option = opti;
    }

    /**
     * 用char初始化
     *
     * @param o
     */
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
        }else{
            this.option = null;
        }
    }

    /**
     * 获取优先级
     *
     * @return
     */
    int getPriority() {
        return map.get(this.option);
    }

    /**
     * 是不是左括号
     *
     * @return
     */
    boolean isLeftBracket() {
        return this.option == opt.LEFTBRA;
    }

    /**
     * 是不是右括号
     *
     * @return
     */
    boolean isRightBracket() {
        return this.option == opt.RIGHTBRA;
    }

    /**
     * 是不是括号
     *
     * @return
     */
    boolean isBracker() {
        return this.option == opt.LEFTBRA || this.option == opt.RIGHTBRA;
    }
}

/**
 * 数字子类
 */
class Num extends Item {
    Num(String str) {
        this.bigDecimal = new BigDecimal(str);
    }

    public BigDecimal getDecimal() {
        return super.bigDecimal;
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
        Option item = (Option) o;
        if (!this.isEmpty() && item.getPriority() >= ((Option) (this.peek())).getPriority()) {
            result = true;
        }
        return result;
    }

    public boolean isInBracket() {
        return (!this.isEmpty()) && ((Option) this.peek()).isLeftBracket();
    }
}