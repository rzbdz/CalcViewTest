
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 一个转换中缀表达式为后缀表达式的测试类
 */
public class TestCalcStack {
    public static void main(String[] args) {
        Stack<Item> stack = new Stack<>();

        //构建表达式:1+2*3+2
        stack.push(new Num("2"));
        stack.push(new Option(Item.opt.PLUS));
        stack.push(new Num("3"));
        stack.push(new Option(Item.opt.TIMES));
        stack.push(new Num("2"));
        stack.push(new Option(Item.opt.PLUS));
        stack.push(new Num("1"));
        //调用中缀转后缀并进行打印
        infixToSuffix(stack);

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
     * 需要进一步处理:(1)优化代码(嵌套if else杀人)
     * (2)写括号
     * <p>
     * 返回还没写
     *
     * @param Infix 传入一个Stack,内容是Item
     * @return
     */
    public static Stack<Item> infixToSuffix(Stack<Item> Infix) {
        Stack<Item> Output = new Stack<>();
        MyStack<Item> Operators = new MyStack<>();

        //2020.7.2
        Item item;
        while (!Infix.empty()) {
            item = Infix.pop();
            if (item instanceof Num) {
                Output.push(item);
            } else {
                if (!Operators.isPrior(item) || Infix.isEmpty()) {
                    while (!Operators.isEmpty()) {
                        Output.push(Operators.pop());
                    }
                    Operators.push(item);
                } else {
                    Operators.push(item);
                }
            }
        }
        while (!Operators.isEmpty()) {
            Output.push(Operators.pop());
        }
        while (!Output.empty())
            System.out.println(Output.pop().toString());

        //
        return Output;
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

    /**
     * 用枚举初始化
     * @param opti
     */
    Option(opt opti) {
        this.option = opti;
    }

    /**
     * 用char初始化
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
        }
    }

    /**
     * 获取优先级
     * @return
     */
    int getPriority() {
        return map.get(this.option);
    }

    /**
     * 是不是左括号
     * @return
     */
    boolean isLeftBracket() {
        return this.option == opt.LEFTBRA;
    }

    /**
     * 是不是右括号
     * @return
     */
    boolean isRightBracket() {
        return this.option == opt.RIGHTBRA;
    }

    /**
     * 是不是括号
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
}

/**
 * 这个是用来给Operator的临时栈用的
 * 添加一个isPrior方法判断优先级
 * 主要是为了处理Stack为空的情况
 * @param <E>
 */
class MyStack<E> extends Stack<E> {
    public boolean isPrior(Object o) {
        boolean result = false;
        Option item = (Option) o;
        if (!this.isEmpty() && item.getPriority() > ((Option) (this.peek())).getPriority()) {
            result = true;
        }
        return result;
    }
}