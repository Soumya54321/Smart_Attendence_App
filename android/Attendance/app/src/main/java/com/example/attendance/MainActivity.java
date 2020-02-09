package com.example.attendance;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.attendance.model.Result;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    TextView textView;
    Button btn_pic, upload_btn;
    VideoView videoView;
    Spinner dept_spinner, year_spinner, sec_spinner;
    Uri videoUri;
    EditText editTextSubject;

    String dept, year, section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);
        upload_btn = findViewById(R.id.upload_btn);
        btn_pic = findViewById(R.id.btn_pic);
        videoView = findViewById(R.id.videoView);
        videoView.setVisibility(View.INVISIBLE);
        dept_spinner = findViewById(R.id.dept_spinner);
        year_spinner = findViewById(R.id.year_spinner);
        sec_spinner = findViewById(R.id.sec_spinner);
        editTextSubject = findViewById(R.id.edit_text_subject);

        if (!new SharedPrefs().getLoggedInStatus(MainActivity.this)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        String displayString = getIntent().getStringExtra("teacher_name");
        textView.setText("Hello " + displayString);

        btn_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

        ArrayAdapter<CharSequence> dept_adapter = ArrayAdapter.createFromResource(this, R.array.dept, android.R.layout.simple_spinner_item);
        dept_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dept_spinner.setAdapter(dept_adapter);
        dept_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dept = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> year_adapter = ArrayAdapter.createFromResource(this, R.array.year, android.R.layout.simple_spinner_item);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_spinner.setAdapter(year_adapter);
        year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> section_adapter = ArrayAdapter.createFromResource(this, R.array.section, android.R.layout.simple_spinner_item);
        section_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sec_spinner.setAdapter(section_adapter);
        sec_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                section = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                logout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        new SharedPrefs().setLoggedIn(MainActivity.this);
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    private void uploadFile() {
        Api api = RetrofitFactory.getRetrofit("5001").create(Api.class);

        if(currentFile == null){
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            return;
        }

        final File file = new File(currentFile);
        System.out.println(file.getAbsolutePath());

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part partFile = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RequestBody deptPart = RequestBody.create(MediaType.parse("multipart/form-data"),dept);
        RequestBody yearPart = RequestBody.create(MediaType.parse("multipart/form-data"), year);
        RequestBody sectionPart = RequestBody.create(MediaType.parse("multipart/form-data"), section);
        RequestBody subjectPart = RequestBody.create(MediaType.parse("multipart/form-data"),
                                    editTextSubject.getText().toString());
        RequestBody teacherPart = RequestBody.create(MediaType.parse("multipart/form-data"),
                                      new SharedPrefs().getEmail(MainActivity.this));


        Call<Result> call = api.upload(partFile,
                                       deptPart,
                                       yearPart,
                                       sectionPart,
                                       subjectPart,
                                       teacherPart);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Code: " + response.code() , Toast.LENGTH_SHORT).show();
                    return;
                }
                Result result = response.body();
                if(result.getSuccess().equals("true")){
                    Toast.makeText(MainActivity.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                    file.delete();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    String currentFile = null;

    static final int REQUEST_VIDEO_CAPTURE = 1;
    private void takePicture() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            File videoFile = null;
            try {
                videoFile = createVideoFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(videoFile != null) {
                Uri videoUri = FileProvider.getUriForFile(this,
                        "com.example.FileProvider",
                        videoFile);
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_CANCELED){
            File file = new File(currentFile);
            if(file.exists()) {
                file.delete();
            }
            currentFile = null;
        }
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = data.getData();
            System.out.println(videoUri);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(videoUri);
            videoView.start();
        }
    }

    private File createVideoFile() throws IOException{
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "VID_"+timestamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(fileName,".mp4", storageDir);
        currentFile = image.getAbsolutePath();
        //System.out.println(currentFile);
        return image;
    }

}
