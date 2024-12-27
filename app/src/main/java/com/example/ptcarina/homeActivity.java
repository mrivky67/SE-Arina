package com.example.ptcarina;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import java.io.IOException;

public class homeActivity extends AppCompatActivity {

    private TextView  suhuView, tdsView;
    private TextView textViewKelembapan1;
    private TextView textNamaTanaman;
    private TextView textUmurTanaman;
    private TextView textStatusKelembapan, textStatusSuhu, textStatusNutrisi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        kelembapanView = findViewById(R.id.textklmbp);
        suhuView = findViewById(R.id.textsuhu);
        tdsView = findViewById(R.id.textntrsi);
        DatabaseReference sensorRef = FirebaseDatabase.getInstance().getReference("sensor");

// Mendengarkan perubahan data di Firebase
        sensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Ambil data dari Firebase
//                    int kelembaban = dataSnapshot.child("kelembaban").getValue(Double.class).intValue();
                    double suhu = dataSnapshot.child("suhu").getValue(Double.class);
                    double tds = dataSnapshot.child("tds").getValue(Double.class);

                    // Tampilkan data ke UI
//                    kelembapanView.setText(String.valueOf(kelembaban));
                    suhuView.setText(String.format("%.2f", suhu));
                    tdsView.setText(String.format("%.2f", tds));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Tangani kesalahan jika ada
            }
        });




        // Inisialisasi TextView
        textNamaTanaman = findViewById(R.id.jenisTanaman);
        textUmurTanaman = findViewById(R.id.textumr);
        textStatusKelembapan = findViewById(R.id.kondisiklmbpn);
        textStatusSuhu = findViewById(R.id.textkondisisuhu);
        textStatusNutrisi = findViewById(R.id.textKondisiNutrisi);
//        textKategoriUmur = findViewById(R.id.textKategoriUmur);
//        textStatusKetinggianAir = findViewById(R.id.textStatusKetinggianAir)

        // Lakukan HTTP request untuk mendapatkan data tanaman
        fetchTanamanData();
    }

    private void fetchTanamanData() {
        OkHttpClient client = new OkHttpClient();

        String url = "https://fuzzy-arina-production.up.railway.app/get_data";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    textNamaTanaman.setText("Gagal memuat data");
                    textUmurTanaman.setText("Gagal memuat data");
                    textStatusKelembapan.setText("Gagal memuat data");
                    textStatusSuhu.setText("Gagal memuat data");
                    textStatusNutrisi.setText("Gagal memuat data");

                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();



                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        // Parsing data dari JSON
                        final String namaTanaman = jsonObject.getString("tanaman");
                        final int umur = jsonObject.getInt("umur");
                        final String kategoriUmur = jsonObject.getString("kategori_umur");
                        final String statusKelembapan = jsonObject.getString("status_kelembaban");
                        final String statusKetinggianAir = jsonObject.getString("status_ketinggian_air");
                        final String statusPPM = jsonObject.getString("status_ppm");
                        final String statusSuhu = jsonObject.getString("status_suhu");


                        runOnUiThread(() -> {
                            // Update UI dengan data baru
                            textNamaTanaman.setText("Tanaman: " + namaTanaman);
                            textUmurTanaman.setText(umur + " hari");
                            textStatusKelembapan.setText("Kelembapan: " + statusKelembapan);
                            textStatusSuhu.setText("Suhu: " + statusSuhu);
                            textStatusNutrisi.setText("Nutrisi (PPM): " + statusPPM);
//                            textKategoriUmur.setText("Kategori Umur: " + kategoriUmur);
//                            textStatusKetinggianAir.setText("Ketinggian Air: " + statusKetinggianAir);
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            textNamaTanaman.setText("Error parsing data");
                            textUmurTanaman.setText("Error parsing data");
                            textStatusKelembapan.setText("Error parsing data");
                            textStatusSuhu.setText("Error parsing data");
                            textStatusNutrisi.setText("Error parsing data");
//                            textKategoriUmur.setText("Error parsing data");
//                            textStatusKetinggianAir.setText("Error parsing data");
                        });
                    }
                }
            }
        });

        // Inisialisasi TextView
        textViewKelembapan1 = findViewById(R.id.textklmbp);

        // Ambil data dari Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sensor");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Ambil nilai sebagai Integer atau Double
                    Integer nilai = snapshot.child("kelembaban").getValue(Integer.class);
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

//        ;
//        //klick tombol kelembapan
//        Button btnKelembapan = findViewById(R.id.button2);
//        btnKelembapan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(homeActivity.this, kelembapanActivity.class));
//            }
//        });
//
//        //klick tombol suhu
//        Button btnSuhu = findViewById(R.id.button3);
//        btnSuhu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(homeActivity.this, suhuActivity.class));
//            }
//        });
//
//        //klick tombol nutrisi
//        Button btnNutrisi = findViewById(R.id.button4);
//        btnNutrisi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(homeActivity.this, nutrisiActivity.class));
//            }
//        });
//
//        //klick tombol umur
//        Button btnUmur = findViewById(R.id.button5);
//        btnUmur.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(homeActivity.this, testActivity.class));
//            }
//        });


        //klick tombol umur
        ImageView btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(homeActivity.this, loginActivity.class));
            }
        });

    }
}