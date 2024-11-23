package com.example.ngikngik.Dashboard;

import static com.example.ngikngik.DbContract.IP;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ngikngik.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class fragment_daftar extends Fragment {

    private Dialog dialog;
    private Button btnLanjutDaftar;
    private EditText namalengkap, nomerwa;
    private CheckBox jenjangCheck, kelas11, kelas12;

    public fragment_daftar() {
        super(R.layout.fragment_daftar);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daftar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inisialisasi elemen UI
        namalengkap = view.findViewById(R.id.etNamaLengkap);
        nomerwa = view.findViewById(R.id.etNomerWa);
        btnLanjutDaftar = view.findViewById(R.id.btnlanjutdaftar);
        jenjangCheck = view.findViewById(R.id.jenjangcheck);
        kelas11 = view.findViewById(R.id.kelas11);
        kelas12 = view.findViewById(R.id.kelas12);

        // Listener untuk memastikan hanya satu CheckBox dipilih untuk kelas
        kelas11.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) kelas12.setChecked(false);
        });

        kelas12.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) kelas11.setChecked(false);
        });

        // Listener tombol daftar
        btnLanjutDaftar.setOnClickListener(v -> {
            String namalengkapText = namalengkap.getText().toString().trim();
            String nomerwaText = nomerwa.getText().toString().trim();
            String selectedKelas = kelas11.isChecked() ? "Kelas 11" : kelas12.isChecked() ? "Kelas 12" : null;
            String selectedJenjang = jenjangCheck.isChecked() ? "Jenjang Terpilih" : null;

            // Validasi input
            if (namalengkapText.isEmpty() || nomerwaText.isEmpty() || selectedJenjang == null || selectedKelas == null) {
                Toast.makeText(getActivity(), "Isi semua kolom", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kirim data ke server
            registerUser(namalengkapText, nomerwaText, selectedJenjang, selectedKelas);
        });
    }

    private void registerUser(String namalengkap, String nomerwa, String jenjang, String kelas) {
        if (checkNetworkConnection()) {
            new RegisterTask().execute(namalengkap, nomerwa, jenjang, kelas);
        } else {
            Toast.makeText(getActivity(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void showOtpDialog() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.daftar_berhasil);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button lanjut = dialog.findViewById(R.id.btnlanjutdaftar);
        lanjut.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), beranda.class);
            startActivity(intent);
            dialog.dismiss();
            getActivity().finish();
        });

        dialog.show();
    }

    // AsyncTask untuk proses HTTP POST
    private class RegisterTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String namalengkap = params[0];
            String nomerwa = params[1];
            String jenjang = params[2];
            String kelas = params[3];
            String urlString = "http://" + IP + "/db_sabiproject/daftaruser";

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write("namalengkap=" + URLEncoder.encode(namalengkap, "UTF-8") +
                        "&nomerwa=" + URLEncoder.encode(nomerwa, "UTF-8") +
                        "&jenjang=" + URLEncoder.encode(jenjang, "UTF-8") +
                        "&kelas=" + URLEncoder.encode(kelas, "UTF-8"));
                writer.flush();
                writer.close();
                outputStream.close();

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (result != null) {
                    JSONObject jsonResponse = new JSONObject(result);
                    String status = jsonResponse.getString("status");
                    String message = jsonResponse.getString("message");

                    if ("OK".equalsIgnoreCase(status)) {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        showOtpDialog();
                    } else {
                        Toast.makeText(getActivity(), "Registrasi gagal: " + message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Kesalahan saat memproses data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
