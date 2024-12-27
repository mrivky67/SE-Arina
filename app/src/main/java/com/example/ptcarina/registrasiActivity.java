package com.example.ptcarina;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registrasiActivity extends AppCompatActivity {

    private EditText namaEditText, nimEditText, emailEditText, passEditText, konfPassEditText;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        // Inisialisasi view
        namaEditText = findViewById(R.id.nama1);
        nimEditText = findViewById(R.id.nimRegis);
        emailEditText = findViewById(R.id.email1);
        passEditText = findViewById(R.id.password);
        konfPassEditText = findViewById(R.id.confirm_password);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Tombol kembali
        ImageView arrowBack = findViewById(R.id.arrow_back);
        arrowBack.setOnClickListener(view -> {
            Intent intent = new Intent(registrasiActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Link ke halaman login
        TextView linkMasuk = findViewById(R.id.link_login);
        linkMasuk.setOnClickListener(view -> {
            Intent intent = new Intent(registrasiActivity.this, loginActivity.class);
            startActivity(intent);
        });

        // Tombol daftar
        Button btnDaftar = findViewById(R.id.btn_daftar_register);
        btnDaftar.setOnClickListener(view -> saveUserToDatabase());
    }

    private void saveUserToDatabase() {
        // Ambil data dari EditText
        String nama = namaEditText.getText().toString();
        String nim = nimEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String pass = passEditText.getText().toString();
        String konfPass = konfPassEditText.getText().toString();

        // Validasi data
        if (nama.isEmpty() || nim.isEmpty() || email.isEmpty() || pass.isEmpty() || konfPass.isEmpty()) {
            Toast.makeText(this, "Mohon isi semua data!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(konfPass)) {
            Toast.makeText(this, "Password dan konfirmasi password tidak cocok!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Membuat ID unik untuk pengguna di database
//        String userId = databaseReference.push().getKey();
//        if (userId != null) {
//            HelperClass user = new HelperClass(nama, email, pass, konfPass);
//
//            // Menyimpan data pengguna ke database
//            databaseReference.child(userId).setValue(user).addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    Toast.makeText(registrasiActivity.this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(registrasiActivity.this, loginActivity.class);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(registrasiActivity.this, "Pendaftaran gagal. Coba lagi!", Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }

