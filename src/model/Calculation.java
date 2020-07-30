package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Calculation Model<br>
 * 这个的实际意义是定义一个 "运算"类,它有一个表达式,和一个"运算"结果,它自己能进行"运算".<br>
 * 表现到类里就是有一个中缀栈{@code Deque<> infixStack}(表达式),
 * 一个结果{@code BigDecimal result}(结果)<br>
 * 然后一个方法{@code void doCalculation(Parameter)}.<br>
 * use {@code doCalculation()} to update result<br>
 * use {@code getResult()} to get result;<br><br>
 * 如果要添加运算种类,<br>
 * 要修改,能修改的部分:<br>
 * {@link Calculation#calcBinary(BigDecimal, BigDecimal, Operator)},
 * 具体是处理Operator的switch case<br>
 * 同时需要同步修改:<br>
 * {@link Operator#priorityMap} 运算符优先级map<br>
 * {@link Operator#operator} operator种类的枚举类型(+-*÷)<br>
 * {@link Operator#Operator(char)} 根据字符构造Operator的switch case<br>
 * <br>
 * 使用举例:<br>
 *     ...<br>
 *     var infixStack = ...//代码构建一个中缀表达式栈<br>
 *     ...<br>
 *     Calculation model = new Calculation();//建立一个运算对象<br>
 *     model.doCalculation(infixStack);//让它做运算<br>
 *     var result = model.getResult();<br>
 *     ...
 * @see controller.CalcController
 */
public class Calculation {

    /**
     * infix 表达式栈,<br>
     * <p>用这个存是因为可以方便backspace的后期优化,
     * 你可以直接给把后面的元素给remove或者是修改</p>
     * <p>
     * 但是其实现在的controller的写法是每次update都
     * 是一个新的string构建一个新的表达式了,
     * 要实现删除元素(removeBack之类),
     * 那controller得搞一个别的updateModel的方法,
     * 这里也要写写方法给controller用的(比如modifyLastItem(Item item)).</p>
     */
    Deque<Item> infixStack;

    /**
     * 获取计算结果,如果没有在之前调用
     * {@code doCalculation(stack)},那么
     * 很有可能返回一个null.可以在视图里处理这个null,
     * 或者给搞一个{@code Exception}来throw.<br>
     *
     * @return a  BigDecimal as result.
     */
    public BigDecimal getResult() {
        return result;
    }

    BigDecimal result;

    /**
     * 初始化Calculation,成员将会是null
     */
    public Calculation() {
        this.infixStack = null;
        this.result = null;
    }

    /**
     * 需要传入一个合规的中缀表达Deque<br>
     * e.g. 1+2+3 , 1 is on the top of the deque.<br>
     * 提示:遍历表达式通过{@code addLast(element)}来创建中缀栈
     *
     * @param infixStack 中缀表达式的栈,e.g. a+b, a is the first.
     */
    public void doCalculation(Deque<Item> infixStack) {
        this.infixStack = infixStack;
        this.result = suffixToResult(infixToSuffix(this.infixStack));
        System.out.println(result.toString());
    }

    /**
     * 2020.7.29
     *
     * 中缀转后缀(只支持+-*除和括号)
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
     *
     * @param Infix 传入一个Stack,内容是Item,使用中缀表达式的stack
     * @return 返回一个deque
     * @see test.calculation.CalculationForStringTestPro
     */
    private Deque<Item> infixToSuffix(Deque<Item> Infix) {
        Deque<Item> output = new ArrayDeque<>();
        //用来处理运算符
        OperatorStack<Item> operators = new OperatorStack<>();

        Item item;
        //2020.7.2 failed
        //2020.7.29 modified from test class(test.calculation.CalculationForStringTestPro@Wang)
        //下面的注释是给自己debug看的,提交时记得抹去
        while (!Infix.isEmpty()) {
            //前缀非空pop前缀
            item = Infix.pop();
            if (item instanceof Digit) {
                //是数字就进后缀栈
                output.push(item);
            } else if (item instanceof Operator) {
                //是操作符的情况,要分别考虑:(1)左括号(2)右括号(3)其他运算符
                if (((Operator) item).isLeftBracket()) {
                    //如果是左括号,进栈就退出
                    operators.push(item);
                    continue;
                }
                if (!operators.isEmpty()) {
                    //考虑不是左括号的情况,(1)右括号(2)其他运算符
                    if (((Operator) item).isRightBracket()) {
                        //考虑右括号,必然有一个匹配的左括号
                        while (!operators.isEmpty() && !(((Operator) operators.peek()).isLeftBracket())) {
                            //push(pop)直到遇到左括号
                            output.push(operators.pop());
                        }
                        //不能忘记pop掉左括号(不用push因为后缀表达式不保留左括号)
                        operators.pop();
                    } else {
                        //考虑非右括号情况,如果当前遍历到的运算符优先级是低级或同级,就要从operators弹出之前的运算符
                        //特别要注意括号的情况,括号优先级虽然是最高的,但是不能弹出左括号
                        //例子:1+(2+3)+4,遍历到1+(2+的时候,当前item为+,就只能弹到左括号之后
                        //ForString的Test写法里面的priority设置巧妙,让()<+-<*/,巧妙规避了判断括号
                        //复制CalculationForStringTestPro函数逻辑之后,这里我暂时保留+-<*/<()优先级的写法.
                        while (!operators.isEmpty() && operators.isNotPrior(item) && !operators.isPeekBracket()) {
                            output.push(operators.pop());
                        }
                    }
                }
                //这里仍然在非左括号的分支下,
                //对于左括号,刚刚已经push进operators里了
                //对于其他符号(右括号和+-*/)
                //要把不是右括号的加进入(因为遍历到右括号就右括号不能加入)
                if (!((Operator) item).isRightBracket()) {
                    operators.push(item);
                }
            }
        }
        while (!operators.isEmpty()) {
            output.push(operators.pop());
        }
        //如果Ouput非空输出
        return output;
    }

    /**
     * 计算后缀表达式<br>
     * 使用递归,它内部是通过调用一个函数来计算每个子表达式的,
     * 详见see also<br>
     * @param suffixStack 一个合规的后缀表达式栈
     * @return 返回一个BigDecimal
     * @throws DivideByZeroException 更多exception敬请期待(或者砍了exception)
     * @see Calculation#calcBinary
     */
    private BigDecimal suffixToResult(Deque<Item> suffixStack) throws DivideByZeroException {
        Item i = suffixStack.pop();
        if (suffixStack.isEmpty()) {
            return ((Digit) i).getDecimal();
        } else if (i instanceof Operator) {
            return calcBinary(suffixToResult(suffixStack), suffixToResult(suffixStack), (Operator) i);
        } else if (i instanceof Digit) {
            return ((Digit) i).getDecimal();
        } else {
            return ((Digit) i).getDecimal();
        }
    }

    /**
     *
     * 计算二元表达式的方法
     * 私有,供suffixToResult递归调用
     * 传入参数要符合顺序,见下面的参数注释:
     * @param decimalToCalc 运算数,如除数(10÷5中的5),减数(a-b中的a),就是表达式中后面的数,suffix栈中前面的数
     * @param decimalToBeCalc 被运算数,如被除数(10÷5中的10),就是表达式中后面的数,suffix栈中前面的数
     * @param operation 操作符号,{@link Operator}
     * @return 返回一个BigDecimal结果
     * @throws DivideByZeroException 除零
     */
    private BigDecimal calcBinary(BigDecimal decimalToCalc, BigDecimal decimalToBeCalc, Operator operation) throws DivideByZeroException {
        switch (operation.operator) {
            case PLUS:
                return decimalToBeCalc.add(decimalToCalc);
            case TIMES:
                return decimalToBeCalc.multiply(decimalToCalc);
            case MINUS:
                return decimalToBeCalc.subtract(decimalToCalc);
            case DIVIDES:
                if (decimalToCalc.compareTo(BigDecimal.ZERO) == 0) {
                    throw new DivideByZeroException();
                }
                //这里的除法是写死了保留9位小数,可以添加设置让用户决定这个位数
                return decimalToBeCalc.divide(decimalToCalc, 9, RoundingMode.HALF_UP);
            default:
                //未考虑情况暂时返回被运算数
                return decimalToBeCalc;
        }
    }

}

/**
 *
 * 这个是用来给Operator的临时栈用的
 * 主要是为了处理Stack为空的情况,实现了
 * {@code isNotPrior(Object)}方法来判断参数与栈顶相比的优先级,
 * {@code isPeekBracket()}方法来判断栈顶是不是括号(主要是在运算中判断有没有到达左括号)
 * @param <E>栈的项目类型
 */
class OperatorStack<E> extends ArrayDeque<E> {

    public boolean isNotPrior(Object o) {
        boolean result = false;
        Operator item = (Operator) o;
        if (this.isEmpty()) return false;
        else {
            assert this.peek() != null;
            if (item.getPriority() <= ((Operator) (this.peek())).getPriority()) {
                result = true;
            }
        }

        return result;
    }


    public boolean isPeekBracket() {
        if (isEmpty()) return false;
        else return ((Operator) this.peek()).isBracker();
    }

}


