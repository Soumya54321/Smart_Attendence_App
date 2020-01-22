package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendance.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button login_btn;
    TextView textView_error;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        textView_error = findViewById(R.id.text_view_error);

        login_btn = findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

        /* TODO : check if email and password are empty and display proper msg and do not proceed*/
                User user = new User(email, password);
//                System.out.println(user.getEmail());

                Toast.makeText(LoginActivity.this,
                        user.getEmail()+" "+user.getPassword(),
                        Toast.LENGTH_SHORT).show();

                login(user);
            }
        });

    }

    private void login(User user) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.29.12:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

//        System.out.println("INSIDE LOGIN"+user.getEmail());

        Call<User> call = api.login(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    textView_error.setText("Code"+response.code());
                    return;
                }

                User userResponse = response.body();

                String name = userResponse.getName();
//                System.out.println("EMAIL:"+email);

                SharedPrefs sharedPrefs = new SharedPrefs();
                sharedPrefs.setLoggedIn(LoginActivity.this);
                sharedPrefs.setEmail(LoginActivity.this, name);

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                textView_error.setText(t.getMessage());
            }
        });
    }
}