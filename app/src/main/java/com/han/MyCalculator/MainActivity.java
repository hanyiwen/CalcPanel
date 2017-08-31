package com.han.MyCalculator;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.han.ExpressionHandler.Constants;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.han.ExpressionHandler.Constants.hintsMap;

/**
 *
 */


public class MainActivity extends BaseActivity {

    public static MainActivity activity;
    public static int screenOrient;
    private Context context;
    private Toolbar toolbar;
    private EditText editText;
    private EditText editTextVice;
    private TextView stateText;
    private TextView stateTextVice;
    private TextView out;
    private TextView outVice;
    private ViewPager drawerPager;
    private DrawerLayout drawer;
    private ArrayList<View> drawerPageList;
    private Button indicatorButton;
    private SwipeRefreshLayout swipeRefresh;

    final private String[] operator = {"DEL", "/", "*", "-", "+", ","};
    final private String[] vice = {"CLR", "()", "[]", "{}", "%", ""};

    final private String[][] function = {
            {"sqrt", "cbrt", "randInt", "log", "ln", "logab",
                    "min", "max", "fact", "sin", "cos", "tan", "exp", "gcd", "lcm", "perm", "comb", "remn",
                    "simp", "isPrime", "reStart"},
            {"π", "e", "F", "h", "ћ", "γ", "φ", "c", "N", "R", "k", "G", "Φ", "me", "mn", "mp"}};

    final private String[][] functionVice = {
            {"平方根", "立方根", "随机整数", "十底对数", "e底对数", "对数",
                    "最小", "最大", "阶乘", "正弦", "余弦", "正切",
                    "e底指数", "最大公约数", "最小公倍数", "排列", "组合", "取余", "分数化简", "判断质数",
                    "重启APP"}, {
            "圆周率", "自然底数", "法拉第", "普朗克", "约化普朗克", "欧拉", "黄金分割",
            "光速", "阿伏伽德罗", "理想气体", "玻尔兹曼", "重力", "磁通量子", "电子质量", "质子质量", "中子质量"}};

    final private String[] stringList = {"简单计算", "大数计算", "进制转换", "人民币写法", "24点计算"};

    private String[] numeric = {"7", "8", "9", "4", "5", "6", "1", "2", "3",
            "·", "0", "=", "A", "B", "C", "D", "E", "F"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        context = this;
        screenOrient = getScreenOrient(this);
        barAdapter.removeAll(barAdapter);
        barView.removeAll(barView);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();
        initEditText();
        initTextView();
        initDrawer();
        initPages();
        initTabs();
        initSideBar();
        initNumeric();
        initOperator();
        initOperatorPro();
        initCR();
        initSwipeRefresh();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    private void initSwipeRefresh() {
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                editText.setText("");
                editTextVice.setText("");
                stateText.setText("");
                stateTextVice.setText("");
                out.setText("");
                outVice.setText("");
                swipeRefresh.setRefreshing(false);
            }
        });
    }


    private void initCR() {
        //初始化输入面板布局
        if (screenOrient == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            //竖屏
            //gridView分为多少列
            int[] y = {1, 3, 1, 3, 3};
            for (int i = 0; i < y.length; i++)
                barView.get(i).setNumColumns(y[i]);
//                barView.get(i).setNumColumns(preferences.getInt("CRy" + screenOrient + ("" + i), y[i]));

            int[] z = {6, 4, 6, 5, 5};
            //适配器中需要计算 每个格子的高度设置为多少 需要设置好
            for (int i = 0; i < z.length; i++)
                barAdapter.get(i).setValue(z[i]);
//            barAdapter.get(i).setValue(preferences.getInt("CRz" + screenOrient + ("" + i), z[i]));
        } else {
            //横屏
            stateText.setTextSize(16);
            out.setTextSize(24);

            int[] y = {2, 4, 2, 5, 5};
            for (int i = 0; i < y.length; i++)
                barView.get(i).setNumColumns(preferences.getInt("CRy" + screenOrient + ("" + i), y[i]));

            int[] z = {3, 3, 3, 3, 3};
            for (int i = 0; i < z.length; i++)
                barAdapter.get(i).setValue(preferences.getInt("CRz" + screenOrient + ("" + i), z[i]));
        }
    }

    //判断当前屏幕水平还是竖直
    public int getScreenOrient(Activity activity) {
        int orient = activity.getRequestedOrientation();
        if (orient != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE && orient != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            WindowManager windowManager = activity.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            int screenWidth = display.getWidth();
            int screenHeight = display.getHeight();
            orient = screenWidth < screenHeight ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
        return orient;
    }


    private void initTextView() {
        stateText = (TextView) findViewById(R.id.text_state);//显示运算状态 所耗时间
        stateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateText.setText(null);
            }
        });//点击状态清除

        stateTextVice = (TextView) findViewById(R.id.text_state_vice);//显示运算状态 所耗时间
        stateTextVice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateTextVice.setText(null);
            }
        });//点击状态清除

        out = (TextView) findViewById(R.id.text_out);//输出结果
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击直接复制
                if (out.getText().toString().indexOf("重启") != -1) {
                    editText.setText("reStart()");
                    return;
                }
                ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(rootValue);
                Snackbar.make(v, "已复制运算结果", Snackbar.LENGTH_SHORT).show();
            }
        });
        out.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {//长按 到新界面查看详细结果
                ResultsActivity.actionStart(v.getContext(), rootValue);
                return true;
            }
        });

        outVice = (TextView) findViewById(R.id.text_out_vice);//输出结果
        outVice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击直接复制
                if (out.getText().toString().indexOf("重启") != -1) {
                    editTextVice.setText("reStart()");
                    return;
                }
                ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(rootValue);
                Snackbar.make(v, "已复制运算结果", Snackbar.LENGTH_SHORT).show();
            }
        });
        outVice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {//长按 到新界面查看详细结果
                ResultsActivity.actionStart(v.getContext(), rootValue);
                return true;
            }
        });
    }

    //初始化左边抽屉
    private void initSideBar() {
        final GridView sideBar = (GridView) findViewById(R.id.sideBar);
        barView.add(sideBar);
        GridViewAdapter sideBarAdapter = new GridViewAdapter(this, sideBar, Arrays.asList(stringList), R.layout.button_sidebar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v.findViewById(R.id.text_item);
                //生成很多TextView放入GridView(sideBar中) 同时也是AutofitTextView
                String text = textView.getText().toString();
                //根据点击的String找到对应的Activity启动
                int i = 0;
                for (String str : stringList) {
                    i++;
                    if (text.equals(str))
                        break;
                }
                //遍历找到应该到哪个界面
                switch (i) {
                    case 1:
                        break;
                    case 2:
                        BigDecimalActivity.actionStart(context);
                        break;
                    case 3:
                        BaseConversionActivity.actionStart(context);
                        break;
                    case 4:
                        CapitalMoneyActivity.actionStart(context);
                        break;
                    case 5:
                        PointsActivity.actionStart(context);
                        break;
                    default:
                        Snackbar.make(sideBar, "功能还未完善", Snackbar.LENGTH_SHORT).show();
                }
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        barAdapter.add(sideBarAdapter);
        sideBar.setAdapter(sideBarAdapter);
    }

    //初始化操作面板
    private void initDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_main);
        //在点击空白区域时，也想要关闭侧边栏
        drawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    drawer.closeDrawers();
                }
                return false;
            }
        });
    }

    //右边抽屉的tab
    private void initTabs() {
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs_main);
        tabs.setupWithViewPager(drawerPager);//绑定viewPager
        tabs.getTabAt(0).setText("函数");
        tabs.getTabAt(1).setText("常数");
    }

    //右边抽屉的翻页初始化
    private void initPages() {
        drawerPageList = new ArrayList<>();//ArrayList<View>
        //为viewPage添加每页的内容 两个gridView
        for (int i = 0; i < 2; i++) {
            GridView gridView = new GridView(this);
            gridView.setFastScrollEnabled(true);//条数多时 快速可见
            drawerPageList.add(gridView);
        }

        drawerPager = (ViewPager) findViewById(R.id.viewPager_drawer);
        MainPagerAdapter drawerPagerAdapter = new MainPagerAdapter(drawerPageList);
        drawerPager.setAdapter(drawerPagerAdapter);
        drawerPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //保证右边侧滑出来后 可以再侧滑里面滑动 而不是手势关闭该侧滑 即可以在函数和常数两个标签间滑动
                if (position == 0) {//在“函数”page时 可以手势关闭右侧滑
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                } else {//在“常数”page时 在page里不可以手势关闭右侧滑 保证能划回“函数”page
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    drawer.openDrawer(GravityCompat.END);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public static ArrayList<GridView> barView = new ArrayList<>();//每个新的GridView都放这里
    public static ArrayList<GridViewAdapter> barAdapter = new ArrayList<>();//和上面对应的Adapter

    //Expression中处理  手动设置布局
    public void setBarCR(final int x, final int y, final int z) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("CRy" + screenOrient + ("" + x), y);
        editor.putInt("CRz" + screenOrient + ("" + x), z);
        editor.apply();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                barView.get(x).setNumColumns(y);//设置gridView列列数
                barAdapter.get(x).setValue(z);//设置行数
            }
        });
    }

    //初始化右边抽屉里的两个GridView
    private void initOperatorPro() {
        int i = 0;
        for (View view : drawerPageList) {//drawerPageList装的是ViewPager里的两个GridView
            GridView operatorProBar = (GridView) view;
            barView.add(operatorProBar);
            final String s = i == 0 ? "()" : "";//函数有括号 常数没括号
            int id = i == 0 ? R.layout.button_function : R.layout.button_constant;//两个页面
            GridViewAdapter operatorProAdapter = new GridViewAdapter(
                    this, operatorProBar, Arrays.asList(function[i]), id, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = ((TextView) v.findViewById(R.id.text_item)).getText().toString();
                    if (str.equals("reduc")) {
                        Snackbar.make(v, "此函数还未完善", Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    if (editText.hasFocus()) {
                        Editable editable = editText.getText();
                        int index = editText.getSelectionStart();
                        editable.insert(index, str + s);//将函数或常数插入编辑界面
                        if (s.length() != 0)//如果是函数符号 游标放到函数括号中间
                            editText.setSelection(index + str.length() + s.length() - 1);
                    }

                    if (editTextVice.hasFocus()) {
                        Editable editable = editTextVice.getText();
                        int index = editTextVice.getSelectionStart();
                        editable.insert(index, str + s);//将函数或常数插入编辑界面
                        if (s.length() != 0)//如果是函数符号 游标放到函数括号中间
                            editTextVice.setSelection(index + str.length() + s.length() - 1);
                    }
                }
            });
            operatorProAdapter.setViceText(Arrays.asList(functionVice[i]));//function对应的中文字符

            if (i == 0) {//函数部分设置一个alertDialog的提醒 常数部分不需设置
                operatorProAdapter.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        String funcString = ((TextView) v.findViewById(R.id.text_item)).getText().toString();
                        String hintString = hintsMap.get(funcString);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("函数输入格式说明");
                        builder.setMessage(hintString);
                        builder.setPositiveButton("我已了解", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                        return true;
                    }
                });
            }

            barAdapter.add(operatorProAdapter);
            operatorProBar.setAdapter(operatorProAdapter);
            i++;
        }
    }

    //初始化基础运算面板
    private void initOperator() {
        GridView operatorBar = (GridView) findViewById(R.id.bar_operator);
        barView.add(operatorBar);
        GridViewAdapter operatorAdapter = new GridViewAdapter(this, operatorBar, Arrays.asList(operator), R.layout.button_operator, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = ((TextView) v.findViewById(R.id.text_item)).getText().toString();
                if (editText.hasFocus()) {
                    Editable editable = editText.getText();
                    int index = editText.getSelectionStart();//获取要插入或删除的位置
                    if (str.equals("DEL")) {
                        if (index == 0) {
                            return;
                        }
                        editable.delete(index - 1, index);
                        return;
                    }
                    editable.insert(index, str);
                } else {
                    Editable editable = editTextVice.getText();
                    int index = editTextVice.getSelectionStart();//获取要插入或删除的位置
                    if (str.equals("DEL")) {
                        if (index == 0) {
                            return;
                        }
                        editable.delete(index - 1, index);
                        return;
                    }
                    editable.insert(index, str);
                }
            }
        });
        barAdapter.add(operatorAdapter);
        operatorAdapter.setViceText(Arrays.asList(vice));
        //长按更改运算符
        operatorAdapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String str = ((TextView) v.findViewById(R.id.text_vice_item)).getText().toString();
                if (str.equals("CLR")) {
                    if (editText.hasFocus()) {
                        editText.setText(null);
                    } else {
                        editTextVice.setText(null);
                    }
                    return true;
                }

                if (editText.hasFocus()) {
                    int index = editText.getSelectionStart();
                    editText.getText().insert(index, str);
                    if (str.equals("()") || str.equals("[]") || str.equals("{}"))
                        editText.setSelection(index + str.length() - 1);
                } else {
                    int index = editTextVice.getSelectionStart();
                    editTextVice.getText().insert(index, str);
                    if (str.equals("()") || str.equals("[]") || str.equals("{}"))
                        editTextVice.setSelection(index + str.length() - 1);
                }
                return true;
            }
        });
        operatorBar.setAdapter(operatorAdapter);

        indicatorButton = (Button) findViewById(R.id.button_indicator);
        indicatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.END);
            }
        });

    }

    //初始化数字面板
    private void initNumeric() {
        if (screenOrient == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//            numeric = new String[]{"7", "8", "9", "A", "4", "5", "6", "B", "1", "2", "3", "C", "·", "0", "=", "D",
//                    "⑵", "⑶", "⑷", "E", "⑸", "⑹", "⑺", "F", "⑻", "⑼", "⑽", "⑾", "⑿", "⒀", "⒁", "⒂", "⒃"};
            numeric = new String[]{"7", "8", "9", "A", "4", "5", "6", "B", "1", "2", "3", "C", "·", "0", "=", "D",
                    "E", "F"};
        GridView numericBar = (GridView) findViewById(R.id.bar_numeric);
        barView.add(numericBar);
        GridViewAdapter numericAdapter = new GridViewAdapter(this, numericBar, Arrays.asList(numeric), R.layout.button_numeric, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = ((TextView) v.findViewById(R.id.text_item)).getText().toString();
                if (editText.hasFocus()) {
                    int index = editText.getSelectionStart();
                    if (str.equals("=")) {
                        stateText.setText("运算中...");
                        final long t = System.nanoTime();
                        String value = "";
                        Callable<String> callable = new Callable<String>() {
                            @Override
                            public String call() throws Exception {
                                final String value = ExpressionHandlerHan.calculateFather(editText.getText().toString());
                                return value;
                            }
                        };
                        Future<String> future = Constants.cachedThreadPool.submit(callable);
                        long consumedTime = (System.nanoTime() - t) / 1000;
                        stateText.setText("运算结束，耗时 " + consumedTime + " 微秒");
                        try {
                            value = future.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        if (value.getBytes().length > 100) {
                            out.setText("数值太大，请长按此处显示结果");
                            ResultsActivity.actionStart(context, value);
                        } else {
                            out.setText(value);
                        }
                        rootValue = value;//方便复制到粘贴板
                        return;
                    }

                    str = str.equals("·") ? "." : str;
                    editText.getText().insert(index, str);
                } else {
                    int index = editTextVice.getSelectionStart();
                    if (str.equals("=")) {
                        stateTextVice.setText("运算中...");
                        final long t = System.nanoTime();
                        String value = "";
                        Callable<String> callable = new Callable<String>() {
                            @Override
                            public String call() throws Exception {
                                final String value = ExpressionHandlerHan.calculateFather(
                                        editTextVice.getText().toString());
                                return value;
                            }
                        };
                        //放入线程池
                        Future<String> future = Constants.cachedThreadPool.submit(callable);
                        long consumedTime = (System.nanoTime() - t) / 1000;
                        stateTextVice.setText("运算结束，耗时 " + consumedTime + " 微秒");
                        try {
                            value = future.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        if (value.getBytes().length > 100) {
                            outVice.setText("数值太大，请长按此处显示结果");
                            ResultsActivity.actionStart(context, value);
                        } else {
                            outVice.setText(value);
                        }
                        rootValue = value;//方便复制到粘贴板
                        return;
                    }

                    str = str.equals("·") ? "." : str;
                    editTextVice.getText().insert(index, str);
                }

            }
        });
        barAdapter.add(numericAdapter);
        numericBar.setAdapter(numericAdapter);
    }

    private String rootValue;

    private void initEditText() {
        editText = (EditText) findViewById(R.id.editText);
        //点击edit禁止弹出输入法
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(editText, false);
        } catch (Exception e) {
            ExceptionsHandler.show(this, e.toString());
        }
        AutofitHelper.create(editText).setMinTextSize(28);//控制editText 使其美观
        //获得焦点
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.requestFocusFromTouch();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //输入非等号的任一一个数字面板上的数都会开始计算
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    stateText.setText(null);
                    out.setText("···");
                    return;
                }

                final long t = System.nanoTime();
                String value = "";
                Callable<String> callable = new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return ExpressionHandlerHan.calculateFather(editText.getText().toString());
                    }
                };
                Future<String> future = Constants.cachedThreadPool.submit(callable);
                long consumedTime = (System.nanoTime() - t) / 1000;
                stateText.setText("运算结束，耗时 " + consumedTime + " 微秒");
                try {
                    value = future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (value.getBytes().length > 100) {
                    out.setText("数值太大，请长按此处显示结果");
                    ResultsActivity.actionStart(context, value);
                } else {
                    out.setText(value);
                }
                rootValue = value;//方便复制到粘贴板
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextVice = (EditText) findViewById(R.id.editTextVice);
        //点击edit禁止弹出输入法
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(editTextVice, false);
        } catch (Exception e) {
            ExceptionsHandler.show(this, e.toString());
        }
        AutofitHelper.create(editTextVice).setMinTextSize(28);//控制editText 使其美观
        //Vice不获得焦点
        editTextVice.setFocusable(true);
        editTextVice.setFocusableInTouchMode(true);

        editTextVice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //输入非等号的任一一个数字面板上的数都会开始计算
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    stateTextVice.setText(null);
                    outVice.setText("···");
                    return;
                }

                final long t = System.nanoTime();
                String value = "";
                Callable<String> callable = new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return ExpressionHandlerHan.calculateFather(editTextVice.getText().toString());
                    }
                };
                Future<String> future = Constants.cachedThreadPool.submit(callable);
                long consumedTime = (System.nanoTime() - t) / 1000;
                stateTextVice.setText("运算结束，耗时 " + consumedTime + " 微秒");
                try {
                    value = future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (value.getBytes().length > 100) {
                    outVice.setText("数值太大，请长按此处显示结果");
                    ResultsActivity.actionStart(context, value);
                } else {
                    outVice.setText(value);
                }
                rootValue = value;//方便复制到粘贴板
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(null);
        toolbar.setSubtitle("简单计算");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("关于").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AboutActivity.actionStart(context);
                return true;
            }
        });
        menu.add("退出").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return true;
            }
        });
        return true;
    }

    //手机返回键的响应
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
            return;
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://左上角键的响应
                if (drawer.isDrawerOpen(GravityCompat.END))
                    drawer.closeDrawer(GravityCompat.END);
                if (drawer.isDrawerOpen(GravityCompat.START))
                    drawer.closeDrawer(GravityCompat.START);
                else
                    drawer.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }
}
