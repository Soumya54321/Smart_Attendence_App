package com.example.attendance;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.attendance.model.Post;
import com.example.attendance.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private boolean loggedIn = false;

    TextView textView;
    Button logout_btn, btn_pic;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);
        logout_btn = findViewById(R.id.logout_btn);
        btn_pic = findViewById(R.id.btn_pic);
        videoView = findViewById(R.id.videoView);

        if (!new SharedPrefs().getLoggedInStatus(MainActivity.this)) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        String displayString = new SharedPrefs().getEmail(MainActivity.this);
        textView.setText("Hello " + displayString);

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPrefs().setLoggedIn(MainActivity.this);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        btn_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
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
            Uri videoUri = data.getData();
            System.out.println(videoUri);
            videoView.setVideoURI(videoUri);
            videoView.start();
        }
    }
}
