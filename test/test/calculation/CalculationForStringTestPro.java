package test.calculation;

import org.junit.Assert;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Xuan Wang
 */
public class CalculationForStringTestPro {

    /*
     * 中缀转后缀
     * */
    public static String InfixToSuffix(String str) {

        List<String> finalQueue = new ArrayList<String>();
        List<Character> operatorStack = new ArrayList<Character>();

        char[] charArray = str.trim().toCharArray();
        String operator = "*/+-()";
        char temp = 0;
        int len = 0;

        for (int i = 0; i < charArray.length; i++) {
            temp = charArray[i];
            if (Character.isDigit(temp)) {
                len++;
            } else if (Character.isLetter(temp)) {
                len++;
            } else if (temp == '.') {
                len++;
            } else if (operator.indexOf(temp) != -1) {
                if (len > 0) {
                    finalQueue.add(String.valueOf(Arrays.copyOfRange(charArray, i - len, i)));
                    len = 0;
                }
                if (temp == '(') {
                    operatorStack.add(temp);
                    continue;
                }
                if (!operatorStack.isEmpty()) {
                    int size = operatorStack.size() - 1;
                    //这个flag的作用我考虑为判断有没有走过括号轮
                    boolean flag = false;
                    while (size >= 0 && temp == ')' && operatorStack.get(size) != '(') {
                        finalQueue.add(String.valueOf(operatorStack.remove(size)));
                        size--;
                        flag = true;
                    }
                    //这里的priority设置巧妙,让()<+-<*/,巧妙规避了弹出括号的情况
                    while (size >= 0 && !flag && PRIORITY.get(operatorStack.get(size)) >= PRIORITY.get(temp)) {
                        finalQueue.add(String.valueOf(operatorStack.remove(size)));
                        size--;
                    }
                }
                if (temp != ')') {
                    operatorStack.add(temp);
                } else {
                    operatorStack.remove(operatorStack.size() - 1);
                }
            }
            if (i == charArray.length - 1) {
                if (len > 0) {
                    finalQueue.add(String.valueOf(Arrays.copyOfRange(charArray, i - len + 1, i + 1)));
                }
                int size = operatorStack.size() - 1;
                while (size >= 0) {
                    finalQueue.add(String.valueOf(operatorStack.remove(size)));
                    size--;
                }
            }

        }
        return String.join(",", finalQueue);
    }

    /*
     * 计算结果
     * */
    public static String getResult(String str) {

        String[] arr = str.split(",");
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < arr.length; i++) {
            int size = list.size();
            switch (arr[i]) {
                case "+":
                    double a = Double.parseDouble(list.remove(size - 2)) + Double.parseDouble(list.remove(size - 2));
                    list.add(String.valueOf(a));
                    break;
                case "-":
                    double b = Double.parseDouble(list.remove(size - 2)) - Double.parseDouble(list.remove(size - 2));
                    list.add(String.valueOf(b));
                    break;
                case "*":
                    double c = Double.parseDouble(list.remove(size - 2)) * Double.parseDouble(list.remove(size - 2));
                    list.add(String.valueOf(c));
                    break;
                case "/":
                    double d = Double.parseDouble(list.remove(size - 2)) / Double.parseDouble(list.remove(size - 2));
                    list.add(String.valueOf(d));
                    break;
                default:
                    list.add(arr[i]);
                    break;
            }
        }
        if (list.size() != 1)
            return "error";
        else
            return list.get(0);
    }

    /*
     * 设置优先级，0为最高
     * */
    private static final Map<Character, Integer> PRIORITY = new HashMap<Character, Integer>();

    static {
        PRIORITY.put('-', 1);
        PRIORITY.put('+', 1);
        PRIORITY.put('*', 2);
        PRIORITY.put('/', 2);
        PRIORITY.put('(', 0);
    }

    @Test
    public void test() {
        String a = InfixToSuffix("(5/3+1)+(3)");
        System.out.println(a);
        System.out.println(getResult(a));
        char[] ar = {'+', '-', '*', '/'};
        Assert.assertTrue(generateExpressionsAndCalc(ar));
    }

    private static boolean generateExpressionsAndCalc(char[] arr) {
        boolean returnB = true;
        Random r = new Random(Calendar.getInstance().getTimeInMillis());
        for (int i = 0; i < 1000; i++) {
            StringBuilder s = new StringBuilder(r.nextInt(50) + 1 + "");
            int b = r.nextInt(9) + 2;
            for (int j = 0; j < b; j++) {
                if (r.nextBoolean()) {
                    int cu = r.nextInt(4);
                    s.append(String.valueOf(arr[cu]));
                    s.append('(');
                    int kb = r.nextInt(4) + 2;
                    boolean first = true;
                    for (int k = 0; k < kb; k++) {
                        cu = r.nextInt(4);
                        if (!first) {
                            s.append(arr[cu]);
                        } else {
                            first = false;
                        }
                        s.append(r.nextInt(50) + 1);
                    }
                    s.append(')');
                    s.append(String.valueOf(arr[cu])).append(r.nextInt(50) + 1);
                }
                int cu = r.nextInt(4);
                s.append(String.valueOf(arr[cu])).append(r.nextInt(50) + 1);
            }
            System.out.println(s);
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("javascript");
            try {
                String result = (new BigDecimal(String.valueOf(engine.eval(s.toString())))).toPlainString();
                System.out.print(result + "      ");
                String a;
                try {
                    a = getResult(InfixToSuffix(s.toString()));
                    BigDecimal decimal1 = new BigDecimal(result);
                    BigDecimal decimal2 = new BigDecimal(a);
                    returnB = decimal2.subtract(decimal1).compareTo(new BigDecimal(0.1)) == -1;
                } catch (Exception e) {
                    a = "failed";
                }
                System.out.println(a);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }
        System.out.println(returnB);
        return returnB;
    }
}