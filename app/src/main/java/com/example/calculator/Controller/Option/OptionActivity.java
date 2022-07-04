package com.example.calculator.Controller.Option;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.calculator.Controller.MainActivity;
import com.example.calculator.Controller.OptionMenu.LichSuActivity;
import com.example.calculator.R;

import java.util.Locale;
import java.util.Objects;

public class OptionActivity extends AppCompatActivity {
    private Button optionDate, optionDienTich;
    private ImageButton optioImgdate;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        Toolbar tb = (Toolbar)findViewById(R.id.toolbar);
        tb.setTitle("");
        setSupportActionBar(tb);
        Objects.requireNonNull(getSupportActionBar()).setDisplayUseLogoEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        anhXa();

        optioImgdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OptionActivity.this, DateActivity.class);
                startActivity(intent);
            }
        });

        optionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OptionActivity.this, DateActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ////////////////////////////////

        optionDienTich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OptionActivity.this, DienTichActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option2, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLichSu: {
                Intent intent = new Intent(OptionActivity.this, LichSuActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void anhXa(){
        optionDate = findViewById(R.id.optionDate);
        optioImgdate = findViewById(R.id.optioImgdate);
        optionDienTich = findViewById(R.id.optionDientich);

    }
}