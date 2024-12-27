package com.example.ptcarina;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class TanamanActivity extends AppCompatActivity {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private Spinner spinnerJenisTanaman;
    private EditText umurTanamanEditText;
    private Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tanaman);

        // Inisialisasi elemen UI
        spinnerJenisTanaman = findViewById(R.id.spinnerJenisTanaman);
        umurTanamanEditText = findViewById(R.id.UmurTanaman);
        btnSimpan = findViewById(R.id.ButtonNext);

        // Set adapter untuk spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.jenis_tanaman_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenisTanaman.setAdapter(adapter);

        // Listener tombol simpan
        btnSimpan.setOnClickListener(view -> validateAndSendData());
    }

    private void validateAndSendData() {
        // Ambil data dari input
        String namaTanaman = spinnerJenisTanaman.getSelectedItem().toString();
        String umurTanamanStr = umurTanamanEditText.getText().toString().trim();

        // Validasi input
        if (umurTanamanStr.isEmpty()) {
            umurTanamanEditText.setError("Umur tanaman tidak boleh kosong!");
            umurTanamanEditText.requestFocus();
            return;
        }

        if (!umurTanamanStr.matches("\\d+")) {
            umurTanamanEditText.setError("Umur tanaman harus berupa angka!");
            umurTanamanEditText.requestFocus();
            return;
        }

        int umurTanaman = Integer.parseInt(umurTanamanStr);

        // Jika validasi berhasil, kirim data ke server
        sendPostRequest(namaTanaman, umurTanaman);
    }

    private void sendPostRequest(String nama, int umur) {
        OkHttpClient client = new OkHttpClient();

        // Buat JSON body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("tanaman", nama);
            jsonBody.put("umur", umur);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal membuat data JSON", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);

        // Buat request POST
        String url = "https://fuzzy-arina-production.up.railway.app/start";
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        // Kirim request secara asynchronous
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(TanamanActivity.this, "Gagal mengirim data ke server", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(TanamanActivity.this, "Data berhasil dikirim!", Toast.LENGTH_SHORT).show();
                        // Pindah ke halaman berikutnya
                        Intent intent = new Intent(TanamanActivity.this, homeActivity.class);
                        intent.putExtra("namaTanaman", nama);
                        intent.putExtra("umurTanaman", String.valueOf(umur));
                        startActivity(intent);
                        finish(); // Tutup TanamanActivity
                    } else {
                        Toast.makeText(TanamanActivity.this, "Gagal: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
