package com.ini_package_umar.testuts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextUsia;
    private EditText editTextEkskul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextKelas);
        editTextUsia = findViewById(R.id.editTextUsia);
        editTextEkskul = findViewById(R.id.editTextEkskul);

        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mendapatkan input dari EditText
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();

                // Memastikan input tidak kosong
                if (name.isEmpty() || email.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Anda belum mengisi semua data diri", Toast.LENGTH_SHORT).show();
                } else {
                    // Mengirim data pendaftaran ke server menggunakan AsyncTask
                    new RegisterAsyncTask().execute(name, email);
                    Toast.makeText(MainActivity.this, "Selamat anda sudah mendaftar ekskul tersebut, silahkan menunggu konfirmasi berikutnya", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class RegisterAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String email = params[1];

            // Mengirim data ke server menggunakan URL Connection
            try {
                String url = "http://example.com/register.php";
                String postData = "name=" + URLEncoder.encode(name, "UTF-8") +
                        "&email=" + URLEncoder.encode(email, "UTF-8");

                URL registerUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) registerUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.getOutputStream().write(postData.getBytes());

                // Menerima respons dari server
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                reader.close();
                connection.disconnect();

                return stringBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Menampilkan pesan hasil pendaftaran
            showRegistrationResult(result);
        }
    }

    private void showRegistrationResult(String result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hasil Pendaftaran");
        builder.setMessage(result);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Reset input setelah pendaftaran berhasil
                editTextName.setText("");
                editTextEmail.setText("");
            }
        });

        AlertDialog dialog = builder.create();
    }
}
