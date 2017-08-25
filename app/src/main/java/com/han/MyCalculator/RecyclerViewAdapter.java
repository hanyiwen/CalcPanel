package com.han.MyCalculator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aw on 2017/8/24.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private ArrayList<String> expressionList;

    public RecyclerViewAdapter(Context context, ArrayList<String> expressionList) {
        inflater = LayoutInflater.from(context);
        this.expressionList = expressionList;
    }//构造函数，传入数据

    public RecyclerViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }//构造函数，传入数据

    //定义ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView expression;

        public ViewHolder(View root) {
            super(root);
            expression = (TextView) root.findViewById(R.id.text_item);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.button_pokers, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        vh.expression.setText(expressionList.get(position));
    }

    @Override
    public int getItemCount() {
        return expressionList.size();
    }

    public void setList(ArrayList<String> expressionList) {
        this.expressionList = expressionList;
    }
}
