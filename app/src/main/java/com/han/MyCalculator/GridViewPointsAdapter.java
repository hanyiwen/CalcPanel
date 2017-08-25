package com.han.MyCalculator;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

public class GridViewPointsAdapter extends BaseAdapter {

    private Context context;
    private List<String> text;
    private GridView gridView;
    private View.OnClickListener onClickListener;
    private int layoutId;
    private int value = 2;//高度划分成5份

    public GridViewPointsAdapter(Context context, GridView gridView, List<String> text, int layoutId,
                                 View.OnClickListener onClickListener) {
        super();
        this.context = context;
        this.text = text;
        this.gridView = gridView;
        this.layoutId = layoutId;
        this.onClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return text.size();
    }

    @Override
    public Object getItem(int position) {

        return text.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        convertView.setOnClickListener(onClickListener);

        GridView.LayoutParams param = new GridView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                gridView.getHeight() / value);
        convertView.setLayoutParams(param);//设置gridView中每个控件的高度

        TextView textView = (TextView) convertView.findViewById(R.id.text_item);
        String text = this.text.get(position);
        if (text.equals("DEL"))
            textView.setTextSize(18);
        textView.setText(text);

        return convertView;
    }

    //设置分的段数
    public void setValue(int value) {
        this.value = value;
        notifyDataSetChanged();
    }

}
