package com.example.calculator.DTO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.calculator.R;
import java.util.List;

public class HistoryAdapter extends BaseAdapter {

    private final Context context;
    private final int layout;
    private List<History> historyList;

    public HistoryAdapter(Context context, int layout, List<History> historyList) {
        this.context = context;
        this.layout = layout;
        this.historyList = historyList;
    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder = new ViewHolder();

            //ánh xạ view
            holder.phepTinh = (TextView) view.findViewById(R.id.lsPhepTinh);
            holder.ketQua = (TextView) view.findViewById(R.id.lsKetQua);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        // gán giá trị
        History history = historyList.get(i);
        holder.phepTinh.setText(history.getPhepTinh());
        holder.ketQua.setText("= "+ (history.getKetQua()));
        return view;
    }

    public class ViewHolder {
        TextView phepTinh, ketQua;
    }
}
