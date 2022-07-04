package com.example.calculator.Controller.Option;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.calculator.R;

public class DuLieuActivity extends AppCompatActivity {

    private TextView chonDuLieu1, chonDuLieu2, title_kqDuLieu1, title_kqDuLieu2;
    private EditText kqDuLieu1, kqDuLieu2;
    private Button format_DuLieuActivity, backSpace;
    private final String[] lst = {"Byte B", "Kilobyte KB", "Megabyte MB", "Gigabyte GB",
            "Terabyte TB"};
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_du_lieu);
        anhxa();
        flag = false;
        chonDuLieu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Load Ds chọn
                AlertDialog.Builder builder = new AlertDialog.Builder(DuLieuActivity.this);
                builder.setTitle("Chọn đơn vị");

                builder.setItems(lst, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] parts = lst[which].split(" ");
                        String part1 = parts[0];//sau khoảng cách
                        String part2 = parts[1];
                        chonDuLieu1.setText(part2);
                        title_kqDuLieu1.setText(part1);
                    }
                });
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        chonDuLieu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Load Ds chọn
                AlertDialog.Builder builder = new AlertDialog.Builder(DuLieuActivity.this);
                builder.setTitle("Chọn đơn vị");

                builder.setItems(lst, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] parts = lst[which].split(" ");
                        String part1 = parts[0];//sau khoảng cách
                        String part2 = parts[1];
                        chonDuLieu1.setText(part2);
                        title_kqDuLieu1.setText(part1);
                    }
                });
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        format_DuLieuActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    void anhxa() {

        chonDuLieu1 = findViewById(R.id.chonDuLieu1);
        chonDuLieu2 = findViewById(R.id.chonDuLieu2);
        title_kqDuLieu1 = findViewById(R.id.title_kqDuLieu1);
        title_kqDuLieu2 = findViewById(R.id.title_kqDuLieu2);
        kqDuLieu1 = findViewById(R.id.kqDuLieu1);
        kqDuLieu2 = findViewById(R.id.kqDuLieu2);
        format_DuLieuActivity = findViewById(R.id.format_DuLieuActivity);
        backSpace = findViewById(R.id.backSpace);

    }

    public String chuyenDoi(String s1, String s2){
        double B = 1;
        double KB = B * 1024;
        double MB = KB * 1024;
        double GB = MB * 1024;
        double TB = GB * 1024;
        return "";
    }
    //
}