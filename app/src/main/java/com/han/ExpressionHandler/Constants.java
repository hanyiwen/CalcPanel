package com.han.ExpressionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Constants {
    public static Map<String, String> constantsMap = loadConstantsMap();
    public static Map<String, String> hintsMap = loadHintsMap();
    public static Map<String, Integer> pokersMap = loadPokersMap();
    public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    private static Map<String, Integer> loadPokersMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("2", 2);
        map.put("3", 3);
        map.put("4", 4);
        map.put("5", 5);
        map.put("6", 6);
        map.put("7", 7);
        map.put("8", 8);
        map.put("9", 9);
        map.put("10", 10);
        map.put("J", 11);
        map.put("Q", 12);
        map.put("K", 13);
        return map;
    }

    private static Map<String, String> loadHintsMap() {
        Map<String, String> map = new HashMap<>();
        map.put("sqrt", "sqrt(num)");
        map.put("cbrt", "cqrt(num)");
        map.put("randInt", "randInt(num)，num为正整数，生成0至(num - 1)的随机数");
        map.put("log", "log(num)");
        map.put("ln", "ln(num)");
        map.put("logab", "longab(num1, num2), 其中num1为底数");
        map.put("min", "min(num1, num2, ...), 可输入多个用逗号隔开的表达式");
        map.put("max", "max(num1, num2, ...), 可输入多个用逗号隔开的表达式");
        map.put("fact", "fact(num), num为自然数");
        map.put("sin", "sin(num)");
        map.put("cos", "cos(num)");
        map.put("tan", "tan(num)");
        map.put("exp", "exp(num)");
        map.put("gcd", "gcd(num1, num2), num1, num2均为非0整数");
        map.put("lcm", "lcm(num1, num2), num1, num2均为非0整数");
        map.put("perm", "perm(num1, num2), num1个数中选取num2个数的排列种数");
        map.put("comb", "comb(num1, num2), num1个数中选取num2个数的组合种数");
        map.put("remn", "remn(num1, num2), 求取num1 mod num2, num1和num2均为整数");
        map.put("simp", "simp(num1, num2), 整数num1, num2为分母或分子");
        map.put("isPrime", "isPrime(num), num为整数");
        map.put("reStart", "reStart(), 输入后点击“=”号即能重启应用");
        return map;
    }

    private static Map<String, String> loadConstantsMap() {
        Map<String, String> map = new HashMap<>();
        map.put("h", "6.62606876E-34");
        map.put("ћ", "1.0545718E-34");
        map.put("c", "299792458");
        map.put("N", "6.0221409E23");
        map.put("R", "8.3144621");
        map.put("k", "1.38064852E-23");
        map.put("G", "6.67E-11");
        map.put("F", "9.64853399E4");
        map.put("γ", "0.5772156649");
        map.put("Φ", "2.067833636E-15");
        map.put("φ", "0.61803398875");
        map.put("me", "9.10938188E-31");
        map.put("mn", "1.67262158E-27");
        map.put("mp", "1.67492716E-27");
        map.put("π", String.valueOf(Math.PI));
        map.put("e", String.valueOf(Math.E));
        return map;
    }
}