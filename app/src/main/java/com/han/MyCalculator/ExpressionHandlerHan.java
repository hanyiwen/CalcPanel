package com.han.MyCalculator;

import com.han.ExpressionHandler.Constants;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;


/**
 * Created by user on 2017/8/5.
 */

public class ExpressionHandlerHan {

    //只有出现在这的函数才 写了功能
    final private static String[] function =
            {"sqrt", "cbrt", "randInt", "log", "ln", "logab",
                    "abs", "min", "max", "fact", "sin", "cos", "tan",
                    "exp", "gcd", "lcm", "perm", "comb", "remn",
                    "simp", "isPrime", "toDEG", "toRAD", "reStart"};


    private static HashSet<String> funcSet = load(function);

    private static HashSet<String> load(String[] strings) {
        HashSet<String> hashSet = new HashSet<>();
        for (String stringTemp : strings) {
            hashSet.add(stringTemp);
        }
        return hashSet;
    }

    //这里我们要将所有的函数都包括进去 包括常数
    public static String calculateFather(String expression) {
        //如果是函数func 切取括号前面的字母来找到相应的方法
        //如果是常数 同理
        if (Constants.constantsMap.containsKey(expression)) {
            return Constants.constantsMap.get(expression);
        }
        //判断是函数 还是一般的四则表达式 判断左小括号前的字符串是否在funcSet中
        StringBuilder stringBuilder = new StringBuilder();
        int indexHelper = 0;
        if (expression.contains("(")) {//一定要判断
            while (expression.charAt(indexHelper) != '(') {
                stringBuilder.append(expression.charAt(indexHelper));
                indexHelper++;
            }
        }

        String possibleFuncString = stringBuilder.toString();
        String result = "";
        if (funcSet.contains(possibleFuncString)) {//是函数
            switch (possibleFuncString) {
                case ("sqrt"):
                    result = sqrt(expression);
                    break;
                case ("cbrt"):
                    result = cbrt(expression);
                    break;
                case ("randInt"):
                    result = randInt(expression);
                    break;
                case ("log"):
                    result = log(expression);
                    break;
                case ("ln"):
                    result = ln(expression);
                    break;
                case ("logab"):
                    result = logab(expression);
                    break;
                case ("min"):
                    result = min(expression);
                    break;
                case ("max"):
                    result = max(expression);
                    break;
                case ("fact"):
                    result = fact(expression);
                    break;
                case ("exp"):
                    result = exp(expression);
                    break;
                case ("gcd"):
                    result = gcd(expression);
                    break;
                case ("lcm"):
                    result = lcm(expression);
                    break;
                case ("perm"):
                    result = perm(expression);
                    break;
                case ("comb"):
                    result = comb(expression);
                    break;
                case ("remn"):
                    result = remn(expression);
                    break;
                case ("simp"):
                    result = simp(expression);
                    break;
                case ("isPrime"):
                    result = isPrime(expression);
                    break;
                case ("sin"):
                    result = sin(expression);
                    break;
                case ("cos"):
                    result = cos(expression);
                    break;
                case ("tan"):
                    result = tan(expression);
                    break;
                case ("reStart"):
                    MainActivity.activity.finish();
                    MainActivity.actionStart(MainActivity.activity);
                    result = "正在重启应用";
                    break;
                default:
                    return result;
            }


        } else {//是一般四则表达式
            result = calculate(expression);
        }

        return result;
    }

    private static String tan(String expression) {
        //括号里的数拿出来
        String innerExpression = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));
        try {
            double afterCalc = Double.parseDouble(calculate(innerExpression));
            return String.format("%.8f", Math.tan(afterCalc * Math.PI / 180));
        } catch (Exception e) {
            e.printStackTrace();
            return "请输入正确的角度数！";
        }
    }

    private static String cos(String expression) {
        //括号里的数拿出来
        String innerExpression = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));
        try {
            double afterCalc = Double.parseDouble(calculate(innerExpression));
            return String.format("%.8f", Math.cos(afterCalc * Math.PI / 180));
        } catch (Exception e) {
            e.printStackTrace();
            return "请输入正确的角度数！";
        }
    }

    private static String sin(String expression) {
        //括号里的数拿出来
        String innerExpression = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));
        try {
            double afterCalc = Double.parseDouble(calculate(innerExpression));
            return String.format("%.8f", Math.sin(afterCalc * Math.PI / 180));
        } catch (Exception e) {
            e.printStackTrace();
            return "请输入正确的角度数！";
        }
    }

    private static String isPrime(String expression) {
        //括号里的数拿出来
        String innerExpression = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));
        long num = 0;
        try {
            num = Long.parseLong(innerExpression);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "输入需为整数！";
        }

        if (num <= 1) {
            return "非素数";
        } else {
            for (int i = 2; i <= Math.sqrt(num); i++) {
                if (num % i == 0) {
                    return "非素数";
                }
            }
        }

        return "素数";
    }

    private static String simp(String expression) {
        //先分母 再分子
        //括号里的数拿出来
        String innerExpression = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));
        BigInteger b1;
        BigInteger b2;
        //逗号分割
        try {
            String[] innerExpressions = innerExpression.split(",");
            b1 = new BigInteger(innerExpressions[0].trim());
            b2 = new BigInteger(innerExpressions[1].trim());
        } catch (Exception e) {
            e.printStackTrace();
            return "输入需为两个正整数，并用逗号隔开，请检查！";
        }
        //求最大公约数
        BigInteger gcd = b1.gcd(b2);
        //分母分子都除以最大公约数  最后输出
        b1 = b1.divide(gcd);
        b2 = b2.divide(gcd);
        return "分母：" + b1.toString() + " " + "分子：" + b2.toString();
    }

    private static String remn(String expression) {
        //括号里的数拿出来
        String innerExpression = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));
        BigInteger b1;
        BigInteger b2;

        //逗号分割
        try {
            String[] innerExpressions = innerExpression.split(",");
            b1 = new BigInteger(innerExpressions[0].trim());
            b2 = new BigInteger(innerExpressions[1].trim());
        } catch (Exception e) {
            e.printStackTrace();
            return "输入需为两个正整数，并用逗号隔开，请检查！";
        }

        return String.valueOf(b1.mod(b2));
    }

    private static String comb(String expression) {
        //括号里的数拿出来
        String innerExpression = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));
        //逗号分割
        String[] innerExpressions = innerExpression.split(",");
        long num1 = 0;
        long num2 = 0;
        try {
            num1 = Long.parseLong(innerExpressions[0].trim());
            num2 = Long.parseLong(innerExpressions[1].trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "输入需为两个正整数，并用逗号隔开，请检查！";
        }
        //Cmn = Pmn / Pmm = n(n-1)(n-2)...(n-m+1)/m! = n!/m!/(n-m)!
        BigDecimal result = new BigDecimal(1);
        BigDecimal a;
        for (long i = num1 - num2 + 1; i <= num1; i++) {
            a = new BigDecimal(i);//将i转换为BigDecimal类型
            result = result.multiply(a);//不用result*a，因为BigDecimal类型没有定义*操作
        }
        //m!
        BigDecimal mFact = new BigDecimal(1);
        BigDecimal b;
        for (long i = 2; i <= num2; i++) {
            b = new BigDecimal(i);//将i转换为BigDecimal类型
            mFact = mFact.multiply(b);//不用result*a，因为BigDecimal类型没有定义*操作
        }

        result = result.divide(mFact);
        return String.valueOf(result);
    }

    private static String perm(String expression) {
        //括号里的数拿出来
        String innerExpression = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));
        //逗号分割
        String[] innerExpressions = innerExpression.split(",");
        long num1 = 0;
        long num2 = 0;
        try {
            num1 = Long.parseLong(innerExpressions[0].trim());
            num2 = Long.parseLong(innerExpressions[1].trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "输入需为两个正整数，并用逗号隔开，请检查！";
        }
        // Pmn =n(n-1)(n-2)...(n-m+1)    Pmn = n!/(n-m)!
        BigDecimal result = new BigDecimal(1);
        BigDecimal a;
        for (long i = num1 - num2 + 1; i <= num1; i++) {
            a = new BigDecimal(i);//将i转换为BigDecimal类型
            result = result.multiply(a);//不用result*a，因为BigDecimal类型没有定义*操作
        }
        return String.valueOf(result);
    }

    private static String lcm(String expression) {
        //最小公倍数
        //括号里的数拿出来
        String innerExpression = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));
        //逗号分割
        String[] innerExpressions = innerExpression.split(",");
        long num1 = 0;
        long num2 = 0;
        try {
            num1 = Long.parseLong(innerExpressions[0].trim());
            num2 = Long.parseLong(innerExpressions[1].trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "输入需为两个正整数，并用逗号隔开，请检查！";
        }

        long gcdNum = gcdRecur(num1, num2);
        long result = (num1 * num2) / gcdNum;
        return String.valueOf(result);
    }

    private static String gcd(String expression) {
        //最大公约数
        //括号里的数拿出来
        String innerExpression = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));
        //逗号分割
        String[] innerExpressions = innerExpression.split(",");
        long num1 = 0;
        long num2 = 0;
        try {
            num1 = Long.parseLong(innerExpressions[0].trim());
            num2 = Long.parseLong(innerExpressions[1].trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "输入需为两个正整数，并用逗号隔开，请检查！";
        }

        long result = gcdRecur(num1, num2);
        return String.valueOf(result);
    }

    private static long gcdRecur(long num1, long num2) {
        if (num2 == 0) {
            return num1;
        } else {
            return gcdRecur(num2, num1 % num2);
        }
    }

    private static String exp(String expression) {
        //括号里的数拿出来
        String innerExpression = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));
        try {
            double afterCalc = Double.parseDouble(calculate(innerExpression));
            return String.valueOf(Math.exp(afterCalc));
        } catch (Exception e) {
            e.printStackTrace();
            return "输入格式有误，请检查！";
        }
    }

    private static String fact(String expression) {
        //括号里的数拿出来
        String innerExpression = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));
        long num = 1;
        try {
            num = Long.parseLong(innerExpression);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "输入应为大于等于1的正整数！";
        }

        BigDecimal result = new BigDecimal(1);
        BigDecimal a;
        for (long i = 2; i <= num; i++) {
            a = new BigDecimal(i);//将i转换为BigDecimal类型
            result = result.multiply(a);//不用result*a，因为BigDecimal类型没有定义*操作
        }
        return String.valueOf(result);
    }

    private static String max(String expression) {
        //括号里的数拿出来
        String innerExpression = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));
        //逗号分割
        String[] innerExpressions = innerExpression.split(",");
        double[] nums = new double[innerExpressions.length];
        try {
            for (int i = 0; i < innerExpressions.length; i++) {
                nums[i] = Double.parseDouble(calculate(innerExpressions[i].trim()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "输入格式有误，请检查！";
        }
        Arrays.sort(nums);
        return String.valueOf(nums[nums.length - 1]);
    }

    private static String min(String expression) {
        //括号里的数拿出来
        String innerExpression = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));
        //逗号分割
        String[] innerExpressions = innerExpression.split(",");
        double[] nums = new double[innerExpressions.length];
        try {
            for (int i = 0; i < innerExpressions.length; i++) {
                nums[i] = Double.parseDouble(calculate(innerExpressions[i].trim()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "输入格式有误，请检查！";
        }
        Arrays.sort(nums);
        return String.valueOf(nums[0]);
    }

    private static String logab(String expression) {
        //括号里的数拿出来
        String innerExpression = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));
        //逗号分割
        String[] innerExpressions = innerExpression.split(",");
        int base = 0;
        try {
            base = Integer.parseInt(innerExpressions[0].trim());
            if (base <= 0) return "底数应该为正整数";
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "底数应该为整数";
        }
        try {
            double afterCalc = Double.parseDouble(calculate(innerExpressions[2].trim()));
            double result = Math.log(afterCalc) / Math.log(base);
            return String.valueOf(result);
        } catch (Exception e) {
            e.printStackTrace();
            return "输入格式有误，请检查！";
        }

    }

    private static String ln(String expression) {
        //括号里的数拿出来
        String innerExpression = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));
        try {
            double afterCalc = Double.parseDouble(calculate(innerExpression));
            return String.valueOf(Math.log(afterCalc));
        } catch (Exception e) {
            e.printStackTrace();
            return "输入格式有误，请检查！";
        }
    }

    private static String log(String expression) {
        //括号里的数拿出来
        String innerExpression = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));
        try {
            double afterCalc = Double.parseDouble(calculate(innerExpression));
            double result = Math.log10(afterCalc);
            return String.valueOf(result);
        } catch (Exception e) {
            e.printStackTrace();
            return "输入格式有误，请检查！";
        }
    }

    private static String randInt(String expression) {
        //括号里的数拿出来
        String innerExpression = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));
        try {
            double afterCalc = Double.parseDouble(calculate(innerExpression));
            if (afterCalc < 1) return "输入需为大于等于1的正整数";
            Random random = new Random();
            int randomNum = random.nextInt((int) afterCalc);
            return String.valueOf(randomNum);
        } catch (Exception e) {
            e.printStackTrace();
            return "输入格式有误，请检查！";
        }
    }

    private static String cbrt(String expression) {
        //括号里的数拿出来
        String innerExpression = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));
        try {
            double afterCalc = Double.parseDouble(calculate(innerExpression));
            return String.valueOf(Math.cbrt(afterCalc));
        } catch (Exception e) {
            e.printStackTrace();
            return "输入格式有误，请检查！";
        }
    }

    private static String sqrt(String expression) {
        //括号里的数拿出来
        String innerExpression = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));
        try {
            double afterCalc = Double.parseDouble(calculate(innerExpression));
            return String.valueOf(Math.sqrt(afterCalc));
        } catch (Exception e) {
            e.printStackTrace();
            return "输入格式有误，请检查！";
        }
    }


    public static String calculate(String expression) {
        try {
            //另外 考虑把中括号大括号全部转成小括号 将“-”号（不是减）变成0-
            char[] expressionChars = expression.toCharArray();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < expressionChars.length; i++) {
                if (expressionChars[i] == '{' || expressionChars[i] == '[') {
                    stringBuilder.append("(");
                } else if (expressionChars[i] == '}' || expressionChars[i] == ']') {
                    stringBuilder.append(")");
                } else if (expressionChars[i] == '-') {
                    //“-” 前面是右括号或者数字的话 当减号处理
                    if (i != 0 &&
                            (expressionChars[i - 1] == ')'
                                    || (expressionChars[i - 1] >= '0' && expressionChars[i - 1] <= '9'))) {
                        stringBuilder.append("-");
                        //3-10+(0+(10+5+3)-10)
                    } else {//其他情况当负数处理 变成0-a形式  -3 >>0-3
                        stringBuilder.append("0-");
                    }
                } else {
                    stringBuilder.append(expressionChars[i]);
                }
            }
            expressionChars = stringBuilder.toString().toCharArray();

            //arrayList存放 分隔好 顺序正确的 符号 和 数字
            //现在要考虑怎么把小数加上去
            ArrayList<String> arrayList = new ArrayList<>();
            for (int i = 0; i < expressionChars.length; ) {
                if (expressionChars[i] > '9' || expressionChars[i] < '0') {//非数字的处理
                    arrayList.add(String.valueOf(expressionChars[i++]));
                } else {
                    String tempString = "";
                    while (i < expressionChars.length
                            && ((expressionChars[i] <= '9' && expressionChars[i] >= '0') || (expressionChars[i] == '.'))) {
                        tempString = tempString + expressionChars[i];
                        i++;
                    }
                    //如果是小数 验证是否正确
                    if (tempString.contains(".")) {
                        if (tempString.indexOf(".") == 0 || tempString.indexOf(".") == tempString.length() - 1) {
                            return "小数输入错误";
                        }
                        if (tempString.indexOf(".") != tempString.lastIndexOf(".")) {
                            return "小数存在两个小数点";
                        }
                    }
                    arrayList.add(tempString);
                }
            }

            //验证括号是否能够成对  这一步检查过后 由中后缀表达式的计算中判断表达式正确性
            int leftBracketCount = 0;
            int rightBracketCount = 0;
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).equals("(")) {
                    leftBracketCount++;
                } else if (arrayList.get(i).equals(")")) {
                    rightBracketCount++;
                }
            }
            if (leftBracketCount != rightBracketCount) return "表达式错误，请检查括号是否正确！";

            //用map来标记运算符优先级 值越大优先级越高
            Map<String, Integer> map = new HashMap<>();
            map.put("+", 1);
            map.put("-", 1);
            map.put("*", 2);
            map.put("/", 2);
            map.put("(", 3);//注意他是最高优先级
            map.put(")", 0);//注意他是特殊

            //先中缀表达式转后缀表达式
            List<String> stringOfPostExp = new ArrayList<>();
            Stack<String> stack1 = new Stack<>();
            //特别注意if else的排序
            for (int i = 0; i < arrayList.size(); i++) {
                String curString = arrayList.get(i);
                if (!map.containsKey(curString)) {
                    //当是数字时 直接输出
                    stringOfPostExp.add(curString);
                } else {
                    //记住 高优先级的运算符一定要在栈顶 不然就它就要出栈并输出
                    if (stack1.empty() || map.get(curString) > map.get(stack1.peek())) {
                        stack1.push(curString);
                    } else {
                        if (curString.equals(")")) {//要确保')'进这个判断
                            //遇到右边小括号时 stack出栈输出 知道 左半小括号 但左半小括号不输出
                            while (!stack1.peek().equals("(")) {
                                stringOfPostExp.add(stack1.pop());
                            }
                            stack1.pop();//左半小括号出栈但不输出
                        } else {
                            //如果有左半括号在栈中， 遇到右小括号就停止
                            //高优先级全部弹出
                            while (!stack1.empty()
                                    && map.get(stack1.peek()) >= map.get(curString)
                                    && !stack1.peek().equals("(")) {
                                stringOfPostExp.add(String.valueOf(stack1.pop()));
                            }
                            stack1.push(curString);
                        }
                    }
                }
            }
            //最后再将栈弹空
            while (!stack1.empty()) {
                stringOfPostExp.add(stack1.pop());
            }

            //由后缀表达式计算
            Stack<Double> numStack = new Stack<>();//用来存放数字的
            for (int i = 0; i < stringOfPostExp.size(); i++) {
                String curString = stringOfPostExp.get(i);
                if (isNumber(curString)) {//如果是数字
                    numStack.push(Double.parseDouble(curString));
                } else {
                    //四则运算 后出栈的 运算符（-） 先出栈的 如a-b-c > ab-c-
                    if (numStack.size() < 2) {//由于此时栈内全是数字 如果不够两个 而运算符还没有用完 显然表达式错误
                        return "表达式错误，请检查！";
                    }
                    double before = numStack.pop();
                    double after = numStack.pop();
                    if (curString.equals("+")) {
                        numStack.push(after + before);
                    } else if (curString.equals("-")) {
                        numStack.push(after - before);
                    } else if (curString.equals("*")) {
                        numStack.push(after * before);
                    } else if (curString.equals("/")) {
                        numStack.push(after / before);
                    }
                }
            }
            return String.valueOf(numStack.peek());
        } catch (Exception e) {
            e.printStackTrace();
            return "表达式输入有误，请检查";
        }

    }

    //以后再改方法吧 只要全是数字 且开头不是0就行 现在直接异常来得快
    //同时要考虑小数
    private static boolean isNumber(String curString) {
        try {
            Double.parseDouble(curString);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
