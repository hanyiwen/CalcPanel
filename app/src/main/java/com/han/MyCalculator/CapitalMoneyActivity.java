package com.han.MyCalculator;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class CapitalMoneyActivity extends BaseActivity {


    private TextView textOut;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    textOut.setText((String) msg.obj);
                    break;
                default:
                    break;
            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capital_money);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initTextIn();
        initTextOut();
    }

    private void initTextOut() {
        textOut = (TextView) findViewById(R.id.text_out);
        AutofitHelper.create(textOut).setMaxLines(6);
        textOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cmb =
                        (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(textOut.getText());
                Snackbar.make(v, "已复制转换结果", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void initTextIn() {
        EditText editIn = (EditText) findViewById(R.id.text_in);
        AutofitHelper.create(editIn);
        editIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String str = s.toString();
                if (s.length() == 0 || str.equals(".")) {
                    textOut.setText("···");
                    return;
                }
                int i = str.indexOf(".");
                if (i != -1) {
                    if (str.substring(i, str.length()).length() > 3) {
                        textOut.setText("小数点后不得超过2位");
                        return;
                    }
                }
                //这里以后要换成Callable
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String value = format(str);
                        Message message = new Message();
                        message.what = 100;
                        message.obj = value;
                        handler.sendMessage(message);
                    }
                }).start();
//                String value = "";
//                Callable<String> callable = new Callable<String>() {
//                    @Override
//                    public String call() throws Exception {
//                        return format(str);
//                    }
//                };
//                Future<String> future = Constants.cachedThreadPool.submit(callable);
//                try {
//                    value = future.get();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//                textOut.setText(format(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public String format(String s) {
        if (Double.parseDouble(s) > 10E50)
            return "数值太大，无法转换";
        return RMBTransform(s);
    }

    //华为机试里自己写的代码
    private static String chineseNumString = "零壹贰叁肆伍陆柒捌玖";

    private static String RMBTransform(String money) {
        StringBuilder stringBuilder = new StringBuilder();
        //根据小数点分割 得到两边的位数
        String[] moneyStrings = money.split("\\.");
        String integerPart = moneyStrings[0];

        long integer = 0;
        try {
            integer = Long.parseLong(integerPart);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String integerResult = parseIntegerPart(integer);

        String decimalPart = "";
        String decimalResult = "";
        if (moneyStrings.length > 1) {
            decimalPart = moneyStrings[1];
            decimalResult = parseDecimalPart(decimalPart);
        }


        //测试用例:
        //5.07
        //对应输出应该为:
        //人民币伍元柒分
        //你的输出为:
        //人民币伍元零柒分
        if (integerResult.equals("")) {//0.0 0.01 0.1等整数部分不存在的情况
            if (decimalResult.equals("")) {//小数部分不存在
                stringBuilder.append("零元整");
            } else {//小数部分存在
                stringBuilder.append(decimalResult);
            }
        } else {//整数部分存在
            if (decimalResult.equals("")) {//不存在小数部分
                stringBuilder.append(integerResult + "元整");//八百零二 要加 元
            } else {//存在小数部分
                stringBuilder.append(integerResult + "元" + decimalResult);//元放到最后添加
            }
        }

        //特殊处理 13.64  人民币拾叁元陆角肆分
        // 如果得到壹拾的开头 把壹删了
        if (stringBuilder.toString().substring(0, 2).equals("壹拾")) {
            stringBuilder.deleteCharAt(0);
        }

        //因为前面有人民币三个字符
        return stringBuilder.insert(0, "人民币").toString();
    }


    //这里0.01就是零一角 考虑了0 后面直接拼接
    private static String parseDecimalPart(String decimalPart) {
        if (decimalPart.length() == 1) {//长度为1
            if (decimalPart.charAt(0) != '0') {//.1 .2
                return chineseNumString.substring(
                        Integer.parseInt(decimalPart), Integer.parseInt(decimalPart) + 1) + "角";
            } else {//.0
                return "";
            }
        } else {//长度为2
            if (decimalPart.charAt(0) == '0') {//0.04
                return chineseNumString.substring(//注意这里题目要求不要零 5.07人民币伍元柒分
                        Integer.parseInt(decimalPart.substring(1, 2)),
                        Integer.parseInt(decimalPart.substring(1, 2)) + 1) + "分";
//                return "零" + chineseNumString.substring(//注意这里题目要求不要零 5.07人民币伍元柒分
//                        Integer.parseInt(decimalPart.substring(1, 2)), Integer.parseInt(decimalPart.substring(1, 2)) + 1) + "分";
            } else if (decimalPart.charAt(1) != '0') {//0.14
                return chineseNumString.substring(
                        Integer.parseInt(decimalPart.substring(0, 1)),
                        Integer.parseInt(decimalPart.substring(0, 1)) + 1) + "角"
                        + chineseNumString.substring(
                        Integer.parseInt(decimalPart.substring(1, 2)),
                        Integer.parseInt(decimalPart.substring(1, 2)) + 1) + "分";
            } else {
                return chineseNumString.substring(//0.40
                        Integer.parseInt(decimalPart.substring(0, 1)),
                        Integer.parseInt(decimalPart.substring(0, 1)) + 1) + "角";
            }
        }
    }

    //去0操作
    //阿拉伯数字中间有“0”时，中文大写要写“零”字，阿拉伯数字中间连续有几个“0”时，中文大写金额中间只写一个“零”字
    //同时零是最末尾时 把它删去 比如100
    //最后加“元” 递归里不加  同时注意去掉300这种情况最后的0（这种写法最后有且只会出现一个0）
    private static String parseIntegerPart(long integer) {
        StringBuilder stringBuilder = new StringBuilder();
        //下面这种递归 会让4000编程四千零 注意后面把这个零去掉
        if (integer >= 100000000) {
            if (integer % 100000000 >= 10000000) {//后面没有0
                stringBuilder.append(parseIntegerPart(integer / 100000000) + "亿"
                        + parseIntegerPart(integer % 100000000));
            } else {//后面有0
                stringBuilder.append(parseIntegerPart(integer / 100000000) + "亿零"
                        + parseIntegerPart(integer % 100000000));
            }
        } else if (integer >= 10000) {
            if (integer % 10000 >= 1000) {//后面没有0 如101001
                stringBuilder.append(parseIntegerPart(integer / 10000) + "万"
                        + parseIntegerPart(integer % 10000));
            } else {//后面有0 如100001
                stringBuilder.append(parseIntegerPart(integer / 10000) + "万零"
                        + parseIntegerPart(integer % 10000));
            }
        } else if (integer >= 1000) {
            if (integer % 1000 >= 100) {//后面没有0
                stringBuilder.append(parseIntegerPart(integer / 1000) + "仟"
                        + parseIntegerPart(integer % 1000));
            } else {//后面有0
                stringBuilder.append(parseIntegerPart(integer / 1000) + "仟零"
                        + parseIntegerPart(integer % 1000));
            }
        } else if (integer >= 100) {
            if (integer % 100 >= 10) {//后面没有0
                stringBuilder.append(parseIntegerPart(integer / 100) + "佰"
                        + parseIntegerPart(integer % 100));
            } else {//后面有0
                stringBuilder.append(parseIntegerPart(integer / 100) + "佰零"
                        + parseIntegerPart(integer % 100));
            }
        } else if (integer >= 10) {
            //这里不特殊处理 13.64  人民币拾叁元陆角肆分 后面处理  这里还是壹拾 只有开头才是拾
            stringBuilder.append(parseIntegerPart(integer / 10) + "拾"
                    + parseIntegerPart(integer % 10));
        } else {
            if (integer >= 1) {//从>=0改成这个
                String unitString = String.valueOf(integer);
                //这个递归 无法把元放进去了 所以最后放吧
                stringBuilder.append(chineseNumString.substring(
                        Integer.parseInt(unitString.substring(0, 1)),
                        Integer.parseInt(unitString.substring(0, 1)) + 1));
            }
        }
        //注意0.85的情况 前面
        if (stringBuilder.length() > 0 && stringBuilder.charAt(stringBuilder.length() - 1) == '零') {
            return stringBuilder.substring(0, stringBuilder.length() - 1);//最后一个零不要 后面再处理
        } else {
            return stringBuilder.toString();
        }
    }


    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, CapitalMoneyActivity.class));
    }
}
