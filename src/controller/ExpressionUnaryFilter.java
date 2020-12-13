package controller;

import model.InvalidExpressionException;
import model.UnaryCalculator;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一元过滤控制器,
 * 对view传入的字符串进行合法性检验和修复部分非法表达,
 * 然后对view传入的字符串进行一元运算匹配(过程调用一个静态接口),
 * 过滤后再传给infixtosuffix进行二元运算匹配
 */
class ExpressionUnaryFilter {
    private final String origin;
    private String result;


    public String getOrigin() {
        return origin;
    }

    public String getResult() {
        return result;
    }

    public void restoreOrigin() {
        this.result = origin;
    }

    public ExpressionUnaryFilter(String string) {
        this.origin = string;
        this.result = string;
    }

    /**
     * 进行所有过滤
     *
     * @return 返回一个过滤器
     */
    public ExpressionUnaryFilter filterAll() throws InvalidExpressionException {
        fixDuplicate();
        fixUnClosingBracket();
        executeUnaryCalculation();
        removeUnnecessaryBrackets();
        return this;
    }

    /**
     * 修复未关闭的括号
     */
    public void fixUnClosingBracket() {
        var ar = result.toCharArray();
        int left = 0;
        int right = 0;
        for (var i : ar) {
            if (i == '(') left++;
            if (i == ')') right++;
        }
        if (left > right) {
            result += ")".repeat(left - right);
        }
        if (right > left) {
            result = "(".repeat(right - left) + result;
        }
    }

    /**
     * 去重复符号,供二元中缀转后缀计算用
     */
    public void fixDuplicate() {
        result = result.replaceAll("\\+-", "-");
        result = result.replaceAll("-\\+", "-");
        result = result.replaceAll("\\(-", "(0-");
        result = result.replaceAll("\\(+", "(");
        result = result.replaceAll("--", "+");
    }

    /**
     * 去掉一元括号多余括号
     */
    public void removeUnnecessaryBrackets() {
        String regex = "\\((\\d+(\\.\\d)*)*\\)";
        Matcher m = getRegexMatcher(regex);
        while (m.find()) {
            //System.out.println("bracket g0:" + m.group());//entire (10.15)
            //System.out.println("        g1:" + m.group(1));//10.15
            //System.out.println("        g2:" + m.group(2));//.15
            String replace;
            if (m.group(1) != null) {
                replace = m.group(1);
            } else {
                replace = "0";
            }
            int start = m.start(0);
            int end = m.end(0);
            //System.out.println(start);
            //System.out.println(end);
            StringBuilder sb = new StringBuilder(result);
            result = sb.replace(start, end, replace).toString();
            m = getRegexMatcher(regex);
        }
    }

    /**
     * 进行一元运算
     */
    public void executeUnaryCalculation() throws InvalidExpressionException {
        replaceRegex("√\\((\\d+(\\.\\d+)*)\\)", 1,
                (substring) -> UnaryCalculator.calcUnary(new BigDecimal(substring),
                        UnaryCalculator.UnaryOperator.SQRT).toString());
        replaceRegex("abs\\((-*\\d+(\\.\\d+)*)+\\)", 1,
                (substring) -> UnaryCalculator.calcUnary(new BigDecimal(substring),
                        UnaryCalculator.UnaryOperator.ABS).toString());
        replaceRegex("(\\d+(\\.\\d+)*)+%", 1,
                (substring) -> UnaryCalculator.calcUnary(new BigDecimal(substring),
                        UnaryCalculator.UnaryOperator.PERCENT).toString());


        System.out.println("Execute Unary Calculation - Result: "+getResult());
    }

    /**
     * 调用接口对正则匹配的进行一个替换
     * 如5+abs(-5),正则abs\(-*\d\)
     * 如果group number是1
     * 传给doer的是-5
     * 替换的是"abs(-5)"
     *
     * @param regex       正则
     * @param groupNumber 需要传给doer的group序号
     * @param doer        实现do something的接口
     */
    public void replaceRegex(String regex, int groupNumber, DoSomethingToString doer) throws InvalidExpressionException {
        var m = getRegexMatcher(regex);
        while (m.find()) {
            //System.out.println("group0:" + m.group());
            //System.out.println("group1:" + m.group(1));
            //System.out.println("group2:" + m.group(2));
            int start = m.start(0);
            int end = m.end(0);
            StringBuilder sb = new StringBuilder(result);
            result = sb.replace(start, end, doer.doSomething(m.group(groupNumber))).toString();
            m = getRegexMatcher(regex);
        }
    }

    /**
     * 获取对成员result字符串的一个正则matcher
     *
     * @param regex 正则
     * @return 返回一个matcher
     */
    private Matcher getRegexMatcher(String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(result);
    }
}
