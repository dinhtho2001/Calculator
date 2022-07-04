package com.example.calculator.Controller.Option;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.calculator.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DateActivity extends AppCompatActivity {

    private TextView edtNgayBD, edtNgayKT, ngay, thang, nam, date1, date2;
    private Button tinh_Date;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        tb.setTitle("Tính ngày");
        Objects.requireNonNull(getSupportActionBar()).setDisplayUseLogoEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        edtNgayBD = findViewById(R.id.edtNgayBD);
        edtNgayKT = findViewById(R.id.edtNgayKT);
        ngay = findViewById(R.id.ngay1);
        thang = findViewById(R.id.thang);
        nam = findViewById(R.id.nam);
        tinh_Date = findViewById(R.id.Tinh_Date);
        date1 = findViewById(R.id.date1);
        date2 = findViewById(R.id.date2);


        if (edtNgayBD.getText() == "ngay/thang/nam") {
            //Set Ngày hiện tại
            Date currentTime = Calendar.getInstance().getTime();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf =
                    new SimpleDateFormat("dd/MM/yyyy");
            edtNgayBD.setText(sdf.format(currentTime));
            edtNgayKT.setText(sdf.format(currentTime));
        }

        tinh_Date.setOnClickListener(new View.OnClickListener() {
            int i, i2, i3, ng,count_thang, x_nam, count_nam;
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date d1 = sdf.parse(edtNgayBD.getText().toString());
                    Date d2 = sdf.parse(edtNgayKT.getText().toString());
                    assert d2 != null;
                    assert d1 != null;
                    long miliGiay = d2.getTime() - d1.getTime();
                    long soNgay = miliGiay / (1000 * 60 * 60 * 24);
                    date1.setText(edtNgayBD.getText().toString());
                    date2.setText(edtNgayKT.getText().toString());
                    if (soNgay < 0){
                        Toast.makeText(DateActivity.this, "lỗi", Toast.LENGTH_SHORT).show();
                    }else if (soNgay <= 30) {
                        ngay.setText(""+ soNgay);
                    }else if (soNgay <= 365) {
                        for (i = 0; i < soNgay/2; i++) {
                            ng = (int)(soNgay - 30);
                            count_thang++;
                            if (ng < 30) {
                                ngay.setText(""+ ng);
                                ng = 0;
                                thang.setText(""+count_thang);
                                count_thang = 0;
                                break;
                            }
                        }
                    }else {
                        for (i2 = 0; i2 < soNgay; i2++) {
                            x_nam = (int)(soNgay - 365);
                            count_nam++;
                            if (x_nam < 365) {
                                for (i3 = 0; i3 < x_nam; i3++) {
                                    ng = x_nam - 30;
                                    count_thang++;
                                    if (ng < 30) {
                                        ngay.setText(""+ng);
                                        ng = 0;
                                        thang.setText(""+count_thang);
                                        count_thang = 0;
                                        nam.setText(""+count_nam);
                                        count_nam = 0;
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        edtNgayBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    chonNgayBatDau();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        edtNgayKT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonNgayKetThuc();
            }
        });
    }

    // Option Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    public void chonNgayBatDau() throws ParseException {
        final Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);

        DatePickerDialog dp = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf =
                                new SimpleDateFormat("dd/MM/yyyy");
                        edtNgayBD.setText(sdf.format(calendar.getTime()));
                    }
                }, nam, thang, ngay);
        dp.show();
    }

    //
    public void chonNgayKetThuc() {
        final Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);

        DatePickerDialog dp = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        calendar.set(year, month, dayOfMonth);
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf =
                                new SimpleDateFormat("dd/MM/yyyy");
                        edtNgayKT.setText(sdf.format(calendar.getTime()));
                    }

                }, nam, thang, ngay);
        dp.show();
    }

}