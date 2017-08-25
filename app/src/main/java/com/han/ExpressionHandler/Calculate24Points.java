package com.han.ExpressionHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by aw on 2017/8/24.
 */

public class Calculate24Points {

    private static ArrayList<String> expressionsList;
    private static Set<String> expressionsSet;
    private static double ErrorThreshold = 1e-6;

    public static ArrayList<String> calculate(String pokerString) {
        String[] pokerStrings = pokerString.split(" ");
        expressionsList = new ArrayList<>();
        if (pokerString.length() < 4) {
            expressionsList.add("请选择四张扑克！");
            return expressionsList;
        }
        ArrayList<Double> pokers = new ArrayList<>();
        ArrayList<String> expression = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int poker = Constants.pokersMap.get(pokerStrings[i]);
            pokers.add((double) poker);
            expression.add(pokerStrings[i]);
        }

        expressionsSet = new HashSet<>();
        isTwentyFourPoint(pokers, expression);
        //用Set来消除重复
        expressionsList.addAll(expressionsSet);

        if (expressionsList.size() == 0) {
            expressionsList.add("这种牌面无法构成24点！");
        }

        return expressionsList;
    }

    private static void isTwentyFourPoint(ArrayList<Double> pokers, ArrayList<String> expression) {
        if (pokers.size() < 2) {
            //浮点数容错
            if (Math.abs((pokers.get(0) - 24)) < ErrorThreshold) {
                expressionsSet.add(expression.get(0) + "");//""一定要加上 java内存管理相关
            }
            return;
        }

        char[] operationChar = {'+', '-', '*', '/'};
        //不同位置的两个数的组合
        for (int i = 0; i < pokers.size(); i++) {
            for (int j = 0; j < pokers.size(); j++) {
                if (i != j) {
                    for (int k = 0; k < operationChar.length; k++) {//每种运算符都尝试
                        int operationIndicator = 0;
                        double operationOfTwoNum = 0;
                        if (operationChar[k] == '+') {
                            operationOfTwoNum = pokers.get(i) + pokers.get(j);
                            operationIndicator = 1;
                        } else if (operationChar[k] == '-') {
                            operationOfTwoNum = pokers.get(i) - pokers.get(j);
                            operationIndicator = 2;
                        } else if (operationChar[k] == '*') {
                            operationOfTwoNum = pokers.get(i) * pokers.get(j);
                            operationIndicator = 3;
                        } else {
                            if (pokers.get(j) == 0) {
                                continue;
                            } else {
                                operationOfTwoNum = pokers.get(i) / pokers.get(j);
                            }
                            operationIndicator = 4;
                        }
                        double iPoker = 0;
                        double jPoker = 0;
                        String iPokerString = null;
                        String jPokerString = null;
                        //千万注意 这样可以保证移除的顺序 先把大序号的移除了 不影响小序号的移除
                        if (i > j) {
                            iPoker = pokers.remove(i);
                            jPoker = pokers.remove(j);
                            iPokerString = expression.remove(i);
                            jPokerString = expression.remove(j);
                        } else {
                            jPoker = pokers.remove(j);
                            iPoker = pokers.remove(i);
                            jPokerString = expression.remove(j);
                            iPokerString = expression.remove(i);
                        }

                        pokers.add(operationOfTwoNum);
                        switch (operationIndicator) {
                            case 1:
                                expression.add("(" + iPokerString + "+" + jPokerString + ")");
                                break;
                            case 2:
                                expression.add("(" + iPokerString + "-" + jPokerString + ")");
                                break;
                            case 3:
                                expression.add("(" + iPokerString + "*" + jPokerString + ")");
                                break;
                            case 4:
                                expression.add("(" + iPokerString + "/" + jPokerString + ")");
                                break;
                            default:
                        }

                        //如果想全部找到
                        isTwentyFourPoint(pokers, expression);
                        //如果想找到一个就退出
//                        if (isTwentyFourPoint(pokers, expression)) {
//                            return true;
//                        }

                        //后面都是恢复现场 进行后一轮递归尝试
                        pokers.remove(pokers.size() - 1);
                        expression.remove(expression.size() - 1);

                        //千万注意 这样可以保证把元素加回去的顺序 先把小序号的加回去
                        //不影响大序号的恢复
                        if (i < j) {
                            pokers.add(i, iPoker);
                            pokers.add(j, jPoker);
                            expression.add(i, iPokerString);
                            expression.add(j, jPokerString);
                        } else {
                            pokers.add(j, jPoker);
                            pokers.add(i, iPoker);
                            expression.add(j, jPokerString);
                            expression.add(i, iPokerString);
                        }
                    }
                }
            }
        }

    }


}
