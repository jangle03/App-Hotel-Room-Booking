package com.example.appbooking.page.customer;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appbooking.Model.UuDai;
import com.example.appbooking.R;

public class EditCustomerInfoActivity extends AppCompatActivity {

    private EditText etCustomerName, etCustomerContact, etCustomerCCCD;
    private Button btnSaveCustomerInfo, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer_info);

        // Initialize UI components
        etCustomerName = findViewById(R.id.etCustomerName);
        etCustomerContact = findViewById(R.id.etCustomerContact);
        etCustomerCCCD = findViewById(R.id.etCustomerCCCD);
        btnSaveCustomerInfo = findViewById(R.id.btnSaveCustomerInfo);
        btnCancel = findViewById(R.id.btnCancel);

        // Handle save button click
        btnSaveCustomerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCustomerInfo();
            }
        });

        // Handle cancel button click
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelEditing();
            }
        });
    }

    private void saveCustomerInfo() {
        // Get the text entered in the fields
        String customerName = etCustomerName.getText().toString().trim();
        String customerContact = etCustomerContact.getText().toString().trim();
        String customerCCCD = etCustomerCCCD.getText().toString().trim();

        // Validate the input
        if (customerName.isEmpty() || customerContact.isEmpty() || customerCCCD.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Process the information (e.g., save to database, send to server)
        // Here, for simplicity, we just show a toast with the entered information
        Toast.makeText(this, "Thông tin đã được lưu:\n" +
                        "Tên: " + customerName + "\n" +
                        "Số điện thoại: " + customerContact + "\n" +
                        "Số CCCD: " + customerCCCD,
                Toast.LENGTH_LONG).show();

        // Optionally, you can finish the activity if the information is successfully saved
        // finish();
    }

    private void cancelEditing() {
        // Optionally, close the activity without saving
        finish();
    }
}
