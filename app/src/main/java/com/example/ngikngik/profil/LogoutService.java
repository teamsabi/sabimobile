package com.example.ngikngik.profil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ngikngik.api.DbContract;
import com.example.ngikngik.login;

import org.json.JSONException;
import org.json.JSONObject;

public class LogoutService {

    private static final String BASE_URL = DbContract.SERVER_LOGOUTSERVICE_URL;  // Ganti dengan URL API logout Anda

    public static void logoutUser(Context context) {
        // Menggunakan Volley untuk mengirim request logout
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Menangani response dari server (misalnya status dan pesan)
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            if ("success".equals(status)) {
                                // Berhasil logout, hapus data dari SharedPreferences
                                clearSharedPreferences(context);

                                // Redirect ke halaman login
                                Intent intent = new Intent(context, login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);

                                // Menyelesaikan aktivitas saat ini
                                if (context instanceof Activity) {
                                    ((Activity) context).finish();
                                }

                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            } else {
                                // Tanggapan error dari API
                                Toast.makeText(context, "Logout gagal: " + message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // Tangani kesalahan JSON
                            Log.e("Logout", "Error parsing response: " + e.getMessage());
                            Toast.makeText(context, "Terjadi kesalahan, coba lagi", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Menangani kesalahan pada permintaan API
                        Log.e("Logout", "Error: " + error.getMessage());
                        Toast.makeText(context, "Terjadi kesalahan, coba lagi", Toast.LENGTH_SHORT).show();
                    }
                });

        // Menambahkan request ke queue Volley
        Volley.newRequestQueue(context).add(stringRequest);
    }

    // Fungsi untuk menghapus data SharedPreferences (seperti login info)
    private static void clearSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
