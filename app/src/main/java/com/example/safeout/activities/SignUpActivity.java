package com.example.safeout.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.safeout.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "SignUpActivity";
    private EditText etSUsername;
    private EditText etSPassword;
    private EditText etPhone;
    private EditText etSMail;
    private Button btnSSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        etSUsername = findViewById(R.id.etSUsername);
        etSPassword = findViewById(R.id.etSPassword);
        etSMail = findViewById(R.id.etSMail);
        etPhone = findViewById(R.id.etPhone);
        btnSSignup = findViewById(R.id.btnSSignup);
        btnSSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = etSUsername.getText().toString();
                String password = etSPassword.getText().toString();
                String mail = etSMail.getText().toString();
                String phone = etPhone.getText().toString();
                if (checkPassword(password) == true){
                    registerUser(user, password, mail, phone);
                } else {
                    Toast.makeText(SignUpActivity.this, "Password length must be 8 characters long", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkPassword(String password) {
        if (password.length() >= 8){
            return true;
        }
        return false;
    }

    private void registerUser(String user, String password, String mail, String phone) {
        ParseUser newUser = new ParseUser();
        newUser.setEmail(mail);
        newUser.setPassword(password);
        newUser.setUsername(user);
        newUser.put("phoneNumber", phone);
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(SignUpActivity.this,"Account created" , Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}