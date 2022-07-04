package com.example.calculator.Controller.OptionMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.calculator.Controller.MainActivity;
import com.example.calculator.Controller.Option.OptionActivity;
import com.example.calculator.DTO.DataHelper;
import com.example.calculator.DTO.History;
import com.example.calculator.DTO.HistoryAdapter;
import com.example.calculator.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class LichSuActivity extends AppCompatActivity {

    private DataHelper dataHelper;
    private ListView lvNoiDung;
    private ArrayList<History> historyArrayList;
    private HistoryAdapter historyAdapter;
    int vt, id;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su);

        Toolbar tb = (Toolbar)findViewById(R.id.toolbar);
        tb.setTitle("Lịch sử");
        setSupportActionBar(tb);
        Objects.requireNonNull(getSupportActionBar()).setDisplayUseLogoEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        lvNoiDung = findViewById(R.id.lvNoiDung);
        historyArrayList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(LichSuActivity.this, R.layout.dong_lich_su, historyArrayList);
        lvNoiDung.setAdapter(historyAdapter);

        dataHelper = new DataHelper(this,"History.sqlite",null,1);
        getData();
        registerForContextMenu(lvNoiDung);
        lvNoiDung.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                id = historyArrayList.get(i).getId();
                vt = i;
                return false;
            }
        });

    }

    private void getData(){
    //select data
        Cursor dataHistory = dataHelper.Getdata("SELECT * FROM Content");
        historyArrayList.clear();
        while (dataHistory.moveToNext()) {
            int id = dataHistory.getInt(0);
            String phepTinh = (dataHistory.getString(1));
            String ketqua = (dataHistory.getString(2));
            historyArrayList.add(new History(id, phepTinh, ketqua));
        }
        historyAdapter.notifyDataSetChanged();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.contextMenuDelete) {
            dataHelper.QueryData("DELETE FROM Content WHERE ID = '"+id+"'");
            getData();
            return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option3, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.xoaLichSu: {
                dataHelper.QueryData("DELETE FROM Content");
                getData();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
