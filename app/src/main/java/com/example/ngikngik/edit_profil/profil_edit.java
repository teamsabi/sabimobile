package com.example.ngikngik.edit_profil;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ngikngik.R;
import com.example.ngikngik.api.DbContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class profil_edit extends AppCompatActivity {
    private EditText etName, etPhoneNumber, etBirthDate, etNamaOrangTua, etAlamatEdit;
    private Spinner spGender;
    private Button btnSaveProfile;
    private TextView tvUserId; // New TextView for User ID
    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_edit);

        initializeViews();
        setupDatePicker();
        setupSpinner();
        loadProfileData();
        setupSaveButton();

    }

    private void initializeViews() {
        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        etName = findViewById(R.id.etnamaprofil);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etBirthDate = findViewById(R.id.etBirthDate);
        etNamaOrangTua = findViewById(R.id.etNamaOrangTua);
        etAlamatEdit = findViewById(R.id.etAlamatEdit);
        spGender = findViewById(R.id.spGender);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        tvUserId = findViewById(R.id.tvUserId); // Initialize User ID TextView
    }

    private void setupDatePicker() {
        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            etBirthDate.setText(dateFormatter.format(calendar.getTime()));
        };

        etBirthDate.setOnClickListener(v -> {
            new DatePickerDialog(profil_edit.this, dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void setupSpinner() {
        String[] genderArray = new String[]{"Laki-laki", "Perempuan"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, genderArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(adapter);
    }

    private void loadProfileData() {
        String storedName = sharedPreferences.getString("nama", "");
        String storedPhone = sharedPreferences.getString("phone", "");
        String storedBirthdate = sharedPreferences.getString("birthdate", "");
        String storedParentName = sharedPreferences.getString("nama_orang_tua", "");
        String storedAddress = sharedPreferences.getString("alamat", "");
        String storedGender = sharedPreferences.getString("gender", "");
        String userId = sharedPreferences.getString("id_user", ""); // Get userId

        Log.d("User ID", "User ID: " + userId);
        tvUserId.setText("User ID: " + userId); // Display user ID in TextView

        etName.setText(storedName);
        etPhoneNumber.setText(storedPhone);
        etBirthDate.setText(storedBirthdate);
        etNamaOrangTua.setText(storedParentName);
        etAlamatEdit.setText(storedAddress);

        ArrayAdapter adapter = (ArrayAdapter) spGender.getAdapter();
        if (storedGender != null && !storedGender.isEmpty()) {
            int position = adapter.getPosition(storedGender);
            if (position >= 0) {
                spGender.setSelection(position);
            }
        }
    }

    private void setupSaveButton() {
        btnSaveProfile.setOnClickListener(v -> validateAndSaveData());
    }

    private void validateAndSaveData() {
        String updatedName = etName.getText().toString().trim();
        String updatedPhoneNumber = etPhoneNumber.getText().toString().trim();
        String updatedBirthDate = etBirthDate.getText().toString().trim();
        String updatedNamaOrangTua = etNamaOrangTua.getText().toString().trim();
        String updatedAlamat = etAlamatEdit.getText().toString().trim();
        String selectedGender = spGender.getSelectedItem().toString();

        if (validateFields(updatedName, updatedPhoneNumber, updatedBirthDate,
                updatedNamaOrangTua, updatedAlamat)) {
            sendUpdateRequest(updatedName, updatedPhoneNumber, updatedBirthDate,
                    updatedNamaOrangTua, updatedAlamat, selectedGender);
        }
    }

    private boolean validateFields(String name, String phone, String birthDate,
                                   String parentName, String address) {
        if (name.isEmpty() || phone.isEmpty() || birthDate.isEmpty() ||
                parentName.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phone.length() < 10 || phone.length() > 13) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void sendUpdateRequest(String name, String phone, String birthDate,
                                   String parentName, String address, String gender) {
        String url = DbContract.SERVER_EDIT_PROFIL_URL; // Replace with your API URL
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("id_user", ""); // Get userId

        Map<String, String> params = new HashMap<>();
        params.put("id_user", userId); // Send userId to server
        params.put("username", name);
        params.put("jenis_kelamin", gender);
        params.put("tanggal_lahir", birthDate);
        params.put("telepon", phone);
        params.put("nama_ortu_wali", parentName); // Match the PHP parameter name
        params.put("alamat", address);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> handleApiResponse(response, params),
                error -> handleApiError(error)) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        requestQueue.add(request);
    }

    private void handleApiResponse(String response, Map<String, String> params) {
        try {
            // Parsing the JSON response from the server
            JSONObject jsonResponse = new JSONObject(response);

            // Check if there is a 'status' key in the response to determine success
            String status = jsonResponse.getString("status");
            String message = jsonResponse.getString("message");

            // Handle success or failure based on the 'status' value
            if ("OK".equals(status)) {
                // Save updated data to SharedPreferences
                saveToSharedPreferences(params);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();  // Show success message
                finish();  // Close the activity
            } else {
                Toast.makeText(this, "Update failed: " + message, Toast.LENGTH_SHORT).show();  // Show error message
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveToSharedPreferences(Map<String, String> params) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id_user", params.get("id_user"));  // Save id_user here
        editor.putString("nama", params.get("username"));
        editor.putString("phone", params.get("telepon"));
        editor.putString("birthdate", params.get("tanggal_lahir"));
        editor.putString("nama_orang_tua", params.get("nama_ortu_wali"));
        editor.putString("alamat", params.get("alamat"));
        editor.putString("gender", params.get("jenis_kelamin"));
        editor.apply();
    }

    private void handleApiError(VolleyError error) {

        String errorMessage = error.getMessage();
        if (errorMessage == null) {
            errorMessage = "Network error occurred";  // Default error message if none is found
        }
        Toast.makeText(this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();  // Show error message
    }
}