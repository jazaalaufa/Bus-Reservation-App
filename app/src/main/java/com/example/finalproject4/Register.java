package com.example.finalproject4;

import static com.example.finalproject4.Login.setWindowFlag;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    SQLiteDatabase db;
    DbHelper dbHelper;
    EditText Email, Password, Name, Hp;
    Button btnLogin, btnRegister;
    String name, email, password, hp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        Name = findViewById(R.id.reg_nama);
        Email = findViewById(R.id.reg_email);
        Password = findViewById(R.id.reg_password);
        Hp = findViewById(R.id.reg_hp);

        btnRegister = findViewById(R.id.daftar);
        btnLogin = findViewById(R.id.login);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    name = Name.getText().toString();
                    email = Email.getText().toString();
                    password = Password.getText().toString();
                    hp = Hp.getText().toString();

                    try {
                        if (email.trim().length() > 0 && password.trim().length() > 0 && name.trim().length() > 0 && hp.trim().length() > 0) {
                            dbHelper.open();
                            dbHelper.Register(email, password, name, hp);
                            Toast.makeText(Register.this, "Daftar berhasil", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(Register.this, "Daftar gagal, lengkapi form!", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //validasi input dari user
    public boolean validate() {
        boolean valid = false;

        //Get values dari EditText

        name = Name.getText().toString();
        email = Email.getText().toString();
        password = Password.getText().toString();
        hp = Hp.getText().toString();

        //nama
        if (name.isEmpty()) {
            valid = false;
            Name.setError("Please enter your name");
        } else {
            valid = true;
            Name.setError(null);
        }

        //hp
        if (hp.isEmpty()) {
            valid = false;
            Hp.setError("Please enter your phone number");
        } else {
            valid = true;
            Hp.setError(null);
        }

        //validasi untuk Email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false;
            Email.setError("Please enter valid email!");
        } else {
            valid = true;
            Email.setError(null);
        }

        //password
        if (password.isEmpty()) {
            valid = false;
            Password.setError("Please enter valid password!");
        } else {
            if (password.length() > 5) {
                valid = true;
                Password.setError(null);
            } else {
                valid = false;
                Password.setError("Password is to short!");
            }
        }

        return valid;
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}
