package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.attendance.model.Post;
import com.example.attendance.model.User;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private boolean loggedIn = false;

    TextView textView;
    Button btn_pic, upload_btn;
    VideoView videoView;

    Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);
        upload_btn = findViewById(R.id.upload_btn);
        btn_pic = findViewById(R.id.btn_pic);
        videoView = findViewById(R.id.videoView);
        videoView.setVisibility(View.INVISIBLE);

        if (!new SharedPrefs().getLoggedInStatus(MainActivity.this)) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        String displayString = new SharedPrefs().getEmail(MainActivity.this);
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



/* code for http calling api
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<User>> call = jsonPlaceHolderApi.getUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(!response.isSuccessful()){
                    textView.setText("Code:" + response.code());
                    return;
                }

                List<User> users = response.body();
                System.out.println(users);
                for(User user: users){
                    String content="";
                    content += "ID:" + user.getId()+"\n";
                    content += "Name:" + user.getName()+"\n\n";

                    textView.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
 */
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
    }

    private void uploadFile() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("192.168.29.12:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);

        File file = new File(videoUri.getPath());

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part partFile = MultipartBody.Part.createFormData("upload", file.getName(), requestFile);


        Call<ResponseBody> call = api.upload(partFile);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Code: " + response.code() , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    static final int REQUEST_VIDEO_CAPTURE = 1;
    private void takePicture() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = data.getData();
            System.out.println(videoUri);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(videoUri);
            videoView.start();
        }
    }


}
