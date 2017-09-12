package com.han.MyCalculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.han.ExpressionHandler.Calculate24Points;
import com.han.ExpressionHandler.Constants;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class PointsActivity extends BaseActivity {
    private static String[] pokers = {"A", "2", "3", "4", "5", "6", "7",
            "8", "9", "10", "J", "Q", "K", "=", "CLR"};

    private EditText editText;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<String> expressions;
    private ArrayList<String> hintList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_24_points);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initEditText();
        initPokers();
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_points);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        hintList.add("请输入要计算的四张牌。");
//        expressions = new ArrayList<>();
//        expressions.add("请输入要计算的四张牌。");
        recyclerViewAdapter = new RecyclerViewAdapter(this, hintList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initPokers() {
        GridView pokersGridView = (GridView) findViewById(R.id.gridView_points);
        GridViewPointsAdapter pokersAdapter = new GridViewPointsAdapter(this, pokersGridView, Arrays.asList(pokers), R.layout.button_pokers, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = ((TextView) v.findViewById(R.id.text_item)).getText().toString();
                int index = editText.getSelectionStart();
                //计算24点
                if (str.equals("=")) {
                    Callable<ArrayList<String>> callable = new Callable<ArrayList<String>>() {
                        @Override
                        public ArrayList<String> call() throws Exception {
                            return Calculate24Points.calculate(editText.getText().toString());
                        }
                    };
                    Future<ArrayList<String>> future = Constants.cachedThreadPool.submit(callable);
                    try {
                        expressions = future.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if (expressions != null) {
                        recyclerViewAdapter.setList(expressions);
                        recyclerViewAdapter.notifyDataSetChanged();
                    }

                    return;
                }

                if (str.equals("CLR")) {
                    editText.setText(null);
                    recyclerViewAdapter.setList(hintList);
                    recyclerViewAdapter.notifyDataSetChanged();
                    return;
                }

                editText.getText().insert(index, str + " ");
            }
        });
        pokersGridView.setAdapter(pokersAdapter);
        pokersGridView.setNumColumns(5);
        pokersAdapter.setValue(3);
    }

    private void initEditText() {
        editText = (EditText) findViewById(R.id.text_in);
        editText.setInputType(InputType.TYPE_NULL);
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(editText, false);
        } catch (Exception e) {
            ExceptionsHandler.show(this, e.toString());
        }
        //获得焦点
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.requestFocusFromTouch();
    }


    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, PointsActivity.class));
    }
}
