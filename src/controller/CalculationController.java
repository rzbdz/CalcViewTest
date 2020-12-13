
package controller;

import model.*;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;


/**
 * 控制器,
 * 这里写法是单例模式,
 * view视图里通过{@code getInstance()}获取一个控制器实例,
 * 控制器实现{@code updateModel(参数)}方法<br>
 * 和{@code Object updateView()}方法<br>
 * 视图通过调用上述两个方法完成视图更新;<br>
 * <br>
 * 使用举例<br>
 * ...<br>
 * CalcController.getInstance().updateModel("1+1");<br>
 * BigDecimal toBeShow = CalcController.getInstance().updateView();<br>
 * System.out.println((new DecimalFormat(",###.####")).format(toBeShow));<br>
 * ...<br>
 *
 * @see Controller
 */
public class CalculationController implements Controller {
    BinaryCalculation model;

    static volatile CalculationController controller = new CalculationController();

    /**
     * 单例模式获取实例的成员方法,详细见参阅,
     * 考虑按键可能多次创建controller,
     * 所以用了线程锁(不加时遇到bug)
     *
     * @return 一个controller实例
     * @see CalculationController
     */
    public static CalculationController getInstance() {
        if (controller == null) {
            synchronized (CalculationController.class) {
                if (controller == null) {
                    controller = new CalculationController();
                }
            }
        }
        return controller;
    }

    /**
     * 构造器,创建model
     * 单例模式所以是private
     */
    private CalculationController() {
        this.model = new BinaryCalculation();
    }

    /**
     * 更新数据模型
     * 传一个string来更新model
     *
     * @param expression 应当是一个String类,为UI读到的表达式
     */
    @Override
    public void updateModel(Object expression) throws DivideByZeroException, InvalidExpressionException {
        this.model.doCalculation(stringToInfixStack((String) expression));
    }

    /**
     * 更新视图
     *
     * @return 这里是返回一个BigDecimal给视图用.
     * <p>
     * 因为视图需要格式化显示(e.g. 9,999,999.999),
     * 所以不宜返回String.
     */
    @Override
    public BigDecimal updateView() {
        return this.model.getResult();
    }

    /**
     * 2020.7.20
     * cast string to a infix expression stack<br>
     * string to be cast is expected to contain no spaces<br>
     * 写在controller是遵守
     * 考虑用户输入不合适,如一个(3),
     * 代码就报错了,有两种解决方案,一个是把(3)替换成(0+3)来计算,第二个
     * 解决方案是在这里用正则把括号给去掉,这个正则应该是
     * "\\([0-9]*(\\.[0-9]*)*\\)".<br>
     * 说到正则,下面说的考虑过滤非法字符串也可以写一个正则来验证
     * 一个表达式是不是合法的<br>
     * 考虑小数点的情况,多位的情况,没有过滤非法字符串(如"2..9","2.9.9.9")<br>
     * 可以写一个Exception匹配一下这个:<br>
     * java.lang.NumberFormatException: Character array contains more than one decimal point<br>
     * 最后的实现是:再输入层当有小数点时禁止用户输入小数点.
     * 其他考虑还有括号的数量是否合法(最后用了一个括号补全),像windows的计算器,
     * 它会统计表达式一共有多少个left,然后给在表达式末尾
     * 添上等同数量的右括号但是对于诸多情况,<br>
     * 一个一很好很方便的方法是出现exception就显示:非法输入 就好了<br>
     *
     * @param exp the string to be cast , which is expected to contain no space
     * @return a stack of item (with the first number on the top)
     */
    public static Deque<Item> stringToInfixStack(String exp) throws InvalidExpressionException {
        //exp = exp.replace(" ","");//建议这个在View的时候就搞掉把
        exp = new ExpressionUnaryFilter(exp).filterAll().getResult();
        System.out.println("过滤器过滤后的表达式: "+exp);
        Deque<Item> stack = new ArrayDeque<>();
        try{
            for (int i = 0; i < exp.length(); ) {
                if (exp.charAt(i) >= '0' && exp.charAt(i) <= '9') {
                    int j = i + 1;
                    while (j < exp.length() && (exp.charAt(j) >= '0' && exp.charAt(j) <= '9' || exp.charAt(j) == '.')) {
                        j++;
                    }
                    stack.addLast(new Digit(exp.substring(i, j)));
                    //System.out.println(exp.substring(i,j));
                    i = j;
                } else {
                    //System.out.println("not num"+exp.charAt(i));
                    stack.addLast(new Operator(exp.charAt(i)));
                    i += 1;
                }
            }
        }catch (Exception ex){
            throw new InvalidExpressionException();
        }

        return stack;
    }

}

