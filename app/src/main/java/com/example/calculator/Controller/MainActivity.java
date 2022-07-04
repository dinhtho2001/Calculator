package com.example.calculator.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calculator.Controller.Option.OptionActivity;
import com.example.calculator.Controller.OptionMenu.LichSuActivity;
import com.example.calculator.DTO.DataHelper;
import com.example.calculator.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final int[] numericButtons = {R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four,
            R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine};

    private final int[] operatorsButtons = {R.id.addition, R.id.subtraction, R.id.multiplication, R.id.division};

    private TextView workingsTV, resultsTV;
    private boolean lastNumeric, stateError, lastDot;
    private TextToSpeech speak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private ImageView imageToText;

    private static final int STORAGE_REQUEST_CODE = 201;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;
    String cameraPermission[];
    String storagePermission[];
    Uri image_uri;
    DataHelper dataHelper;
    String calculation;
    double result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tb = findViewById(R.id.toolbar);
        tb.setTitle("");
        setSupportActionBar(tb);

        cameraPermission = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

        workingsTV = findViewById(R.id.workingsTV);
        resultsTV = findViewById(R.id.resultsTV);
        imageToText = findViewById(R.id.imageToText);
        setNumericOnClickListener();

        speak = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    speak.setLanguage(new Locale("vi_VN"));
                    speak.setSpeechRate((float) 1);
                }
            }
        });
        setOperatorOnClickListener();
    }

    // camera
    private void showImageImageImportDialog() {
        String[] items = {" Camera", "Thư viện"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        //permission not allowed
                        requestCameraPermission();
                    } else {
                        pickCamera();
                    }
                }

                if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickGallery();
                    }
                }
            }
        });
        dialog.create().show();
    }

    private boolean checkCameraPermission() {

        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                cameraPermission, CAMERA_REQUEST_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,
                storagePermission, STORAGE_REQUEST_CODE);
    }

    private void pickGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        pickCamera();
                    } else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        pickGallery();
                    } else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
        }
    }

    private void pickCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Camera của tôi");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }
    //end camera

    // Option Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icImage:{
                showImageImageImportDialog();
                break;
            }
            case R.id.icOption:{
                Intent intent = new Intent(MainActivity.this, OptionActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.menu_lichSu: {
                Intent intent = new Intent(MainActivity.this, LichSuActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.menu_cai_dat: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Chọn ngôn ngữ");

                String[] animals = {"Eglish", "Việt Nam", "Trung Quốc"};
                int checkedItem = 1;
                builder.setSingleChoiceItems(animals, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:{
                                speak.setLanguage(Locale.US);
                                break;
                            }
                            case 1:{
                                speak.setLanguage(new Locale("vi_VN"));
                                break;
                            }
                            case 2:{
                                speak.setLanguage(Locale.CHINA);
                                break;
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            }
            case R.id.menu_gioi_thieu:{
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    // end Option Menu

    private void setNumericOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) view;
                if (stateError) {
                    resultsTV.setText(button.getText());
                    stateError = false;
                } else {
                    resultsTV.append(button.getText());
                }
                lastNumeric = true;
            }
        };
        for (int id : numericButtons) {
            findViewById(id).setOnClickListener(listener);
        }
    }
    //
    private void setOperatorOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastNumeric && !stateError) {
                    Button button = (Button) view;
                    resultsTV.append(button.getText());
                    lastNumeric = false;
                    lastDot = false;
                }
            }
        };
        for (int id : operatorsButtons) {
            findViewById(id).setOnClickListener(listener);
        }

        // backSpace
        findViewById(R.id.backSpace).setOnClickListener(new View.OnClickListener() {
            String str, result;
            @Override
            public void onClick(View view) {
                if (!resultsTV.getText().toString().isEmpty()){
                    str = resultsTV.getText().toString();
                    result = str.substring(0, str.length() - 1);
                    resultsTV.setText(result);
                    lastNumeric = false;
                    stateError = false;
                    lastDot = false;
                }
            }
        });

        //decimal point ","
        findViewById(R.id.dot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastNumeric && !stateError && !lastDot) {
                    resultsTV.append(".");
                    lastNumeric = false;
                    lastDot = false;
                }
            }
        });

        // clear button
        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageToText.setImageURI(Uri.parse(""));
                workingsTV.setText("");
                resultsTV.setText("");
                lastNumeric = false;
                stateError = false;
                lastDot = false;
            }
        });

        //equal button
        findViewById(R.id.equals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEqual();
            }
        });

        //speak button
        findViewById(R.id.speak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stateError) {
                    workingsTV.setText("Try Again");
                    stateError = false;
                } else {
                    promptSpeechInput();
                }
                lastNumeric = true;
            }
        });
    }
    //

    private void onEqual() {
        if (lastNumeric && !stateError) {
            calculation = resultsTV.getText().toString();
            workingsTV.setText(calculation);
            try {
                Expression exception;
                try {
                    exception = new ExpressionBuilder(calculation).build();
                    result = exception.evaluate();
                    dataHelper = new DataHelper(MainActivity.this,
                            "History.sqlite",null, 1);
                    dataHelper.QueryData
                            ("CREATE TABLE IF NOT EXISTS Content(ID INTEGER PRIMARY KEY AUTOINCREMENT, calculation VARCHAR(200), result VARCHAR(200))");
                    if (result % ((int)(result)) == 0) {
                        resultsTV.setText("= " + ((int) result));
                        dataHelper.QueryData("INSERT INTO Content VALUES(null,'"+calculation+"','"+((int)result)+"')");
                    } else {
                        resultsTV.setText("= " + (result));
                        dataHelper.QueryData("INSERT INTO Content VALUES(null,'"+calculation+"','"+result+"')");
                    }
                } catch (Exception e) {
                    resultsTV.setText("Không tính được");
                }
            } catch (ArithmeticException ex) {
                workingsTV.setText("Error");
                stateError = true;
                lastNumeric = false;
            }
        }
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String change = result.get(0);
                    change = change.replace("add", "+");
                    change = change.replace("plus", "+");
                    change = change.replace("cộng với", "+");
                    change = change.replace("cộng", "+");
                    change = change.replace("cong", "+");

                    change = change.replace("trừ", "-");
                    change = change.replace("trừ với", "-");
                    change = change.replace("tru voi", "-");

                    change = change.replace("x", "*");
                    change = change.replace("X", "*");
                    change = change.replace("nhân", "*");
                    change = change.replace("nhan", "*");
                    change = change.replace("Nhân", "*");
                    change = change.replace("nhân với", "*");
                    change = change.replace("nhan voi", "*");
                    change = change.replace("multiply by", "*");

                    change = change.replace("chia", "/");
                    change = change.replace("devide", "/");
                    change = change.replace("chia với", "/");
                    change = change.replace("chia voi", "/");
                    change = change.replace("devide by", "/");

                    change = change.replace("equal", "=");
                    change = change.replace("bằng mấy", "=");
                    change = change.replace("bằng bao nhiêu", "=");
                    change = change.replace("bằng", "=");
                    change = change.replace("==", "=");
                    change = change.replace("===", "=");

                    if (change.contains("=")) {
                        change = change.replace("=", "");
                        resultsTV.setText(change);
                        onEqual();
                        String kq = resultsTV.getText().toString();
                        textToSpeech("" + kq);
                    }else {
                        promptSpeechInput();
                    }
                }
                break;
            }

            case IMAGE_PICK_GALLERY_CODE: {
                Uri imageUri = data.getData();
                imageToText.setImageURI(imageUri);
                BitmapDrawable bitmapDrawable = (BitmapDrawable)imageToText.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!recognizer.isOperational()){
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
                else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb =new StringBuilder();

                    for (int i = 0; i<items.size(); i++){
                        TextBlock myItems = items.valueAt(i);
                        sb.append(myItems.getValue());
                    }
                    resultsTV.setText(sb.toString());
                    lastNumeric = true;
                    stateError = false;
                    onEqual();
                }
                break;
            }
            case IMAGE_PICK_CAMERA_CODE:
                imageToText.setImageURI(image_uri);
                BitmapDrawable bitmapDrawable = (BitmapDrawable)imageToText.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!recognizer.isOperational()){
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
                else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb =new StringBuilder();

                    for (int i = 0; i<items.size(); i++){
                        TextBlock myItems = items.valueAt(i);
                        sb.append(myItems.getValue());
                    }
                    resultsTV.setText(sb.toString());
                    lastNumeric = true;
                    stateError = false;
                    onEqual();
                }
                break;
        }
    }

    //
    private void textToSpeech(String text) {
        speak.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}