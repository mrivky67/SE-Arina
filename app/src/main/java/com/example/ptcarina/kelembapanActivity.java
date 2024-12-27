package com.example.ptcarina;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class kelembapanActivity extends AppCompatActivity {

    private TextView textViewKelembapan1;
    private TextView textViewKelembapan2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_kelembapan);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi ImageView untuk tombol kembali
        ImageView arrowBack = findViewById(R.id.imageView6);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kembali ke homeActivity
                Intent intent = new Intent(kelembapanActivity.this, homeActivity.class);
                startActivity(intent);
            }
        });

        // Inisialisasi TextView
        textViewKelembapan1 = findViewById(R.id.textView4);

        // Ambil data dari Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sensor");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Ambil nilai sebagai Integer atau Double
                    Integer nilai = snapshot.child("value").getValue(Integer.class);
                    if (nilai != null) {
                        textViewKelembapan1.setText(String.valueOf(nilai));
                    } else {
                        textViewKelembapan1.setText("Data tidak ditemukan");
                    }
                } else {
                    textViewKelembapan1.setText("Data tidak ditemukan");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                textViewKelembapan1.setText("Gagal memuat data");
            }
        });
    }
}
