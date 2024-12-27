package com.example.ptcarina;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class loginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Klik tombol back
        ImageView arrowBack = findViewById(R.id.imageView_arrow_login);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kembali ke MainActivity
                Intent intent = new Intent(loginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Klik tombol login
        Button btnLogin = findViewById(R.id.btn_masuk_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText emailField = findViewById(R.id.email);
                EditText passwordField = findViewById(R.id.pass);

                String username = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if (username.equals("admin") && password.equals("admin")) {
                    Toast.makeText(loginActivity.this, "Login berhasil!", Toast.LENGTH_SHORT).show();
                    Intent intentToHome = new Intent(loginActivity.this, TanamanActivity.class);
                    startActivity(intentToHome);
                } else {
                    Toast.makeText(loginActivity.this, "Username atau password salah!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
