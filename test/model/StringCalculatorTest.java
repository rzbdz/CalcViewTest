package model;

import org.junit.Assert;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.util.*;

import static model.StringCalculator.*;


public class StringCalculatorTest {


    @Test
    public void test() {
        String a = infixToSuffix("(5/3+1)");
        System.out.println(a);
        System.out.println(getResultFromSuffix(a));
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
                String result;
                try {
                    result = (new BigDecimal(String.valueOf(engine.eval(s.toString())))).toPlainString();
                } catch (Exception ex) {
                    result = String.valueOf(engine.eval(s.toString()));
                }
                System.out.print(result + "      ");
                String a;
                if(result.contains("Inf")){
                    System.out.println("inf");
                    continue;
                }
                try {
                    a = getResultFromSuffix(infixToSuffix(s.toString()));
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