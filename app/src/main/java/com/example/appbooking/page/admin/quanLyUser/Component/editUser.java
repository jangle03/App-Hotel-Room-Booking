package com.example.appbooking.page.admin.quanLyUser.Component;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbooking.Database.MySQLite;
import com.example.appbooking.MainActivity;
import com.example.appbooking.Model.TaiKhoan;
import com.example.appbooking.R;
import com.example.appbooking.page.admin.quanLyUser.quanLyUser;

public class editUser extends AppCompatActivity {

    TextView tk_username;
    EditText tk_name, tk_email, tk_adress, tk_cccd, tk_sdt;
    Spinner tk_role;
    ImageView tk_img;
    Button tk_btn_save;
    MySQLite db1 = new MySQLite();
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tk_username = findViewById(R.id.tk_username);
        back = findViewById(R.id.back);
        tk_name = findViewById(R.id.tk_name);
        tk_email = findViewById(R.id.tk_email);
        tk_adress = findViewById(R.id.tk_adress);
        tk_cccd = findViewById(R.id.tk_cccd);
        tk_role = findViewById(R.id.tk_role);
        tk_img = findViewById(R.id.tk_img);
        tk_sdt = findViewById(R.id.tk_sdt);
        tk_btn_save = findViewById(R.id.tk_btn_save);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Tạo một ArrayAdapter với các giá trị "Admin" và "User"
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Admin", "User"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tk_role.setAdapter(adapter);

        // Nhận Intent đã được truyền từ Activity trước đó
        Intent intent = getIntent();
        TaiKhoan tk = (TaiKhoan) intent.getSerializableExtra("data");

        // Kiểm tra nếu đối tượng TaiKhoan không phải là null trước khi sử dụng
        if (tk != null) {
            tk_username.setText(tk.getUsername().toString()); // Hiển thị thông tin người dùng
            tk_name.setText(tk.getName().toString());
            tk_email.setText(tk.getEmail().toString());
            tk_adress.setText(tk.getAddress().toString());
            tk_cccd.setText(tk.getCccd().toString());
            tk_img.setImageURI(Uri.parse(db1.getDrawableResourceUrl(editUser.this, tk.getHinh().toString())));
            tk_role.setSelection(tk.getRole() == 0 ? 0 : 1);
            tk_sdt.setText(tk.getSdt().toString());
        } else {
            tk_username.setText("Không có dữ liệu người dùng.");
        }

        tk_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ EditText
                String name = tk_name.getText().toString().trim();
                String email = tk_email.getText().toString().trim();
                String address = tk_adress.getText().toString().trim();
                String cccd = tk_cccd.getText().toString().trim();
                String sdt = tk_sdt.getText().toString().trim();
                // Lấy giá trị từ Spinner (0 = Admin, 1 = User)
                int role = tk_role.getSelectedItemPosition(); // 0 cho Admin, 1 cho User

                // Validate dữ liệu nhập
                if (name.isEmpty()) {
                    tk_name.setError("Tên không được để trống");
                    tk_name.requestFocus();
                    return;
                }

                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    tk_email.setError("Email không hợp lệ");
                    tk_email.requestFocus();
                    return;
                }

                if (address.isEmpty()) {
                    tk_adress.setError("Địa chỉ không được để trống");
                    tk_adress.requestFocus();
                    return;
                }

                if (cccd.isEmpty() || !cccd.matches("\\d{12}")) { // CCCD phải gồm 12 chữ số
                    tk_cccd.setError("CCCD phải có 12 chữ số");
                    tk_cccd.requestFocus();
                    return;
                }

                if (sdt.isEmpty() || !sdt.matches("\\d{10}")) { // Số điện thoại phải có 10 chữ số
                    tk_sdt.setError("Số điện thoại không hợp lệ (phải là 10 chữ số)");
                    tk_sdt.requestFocus();
                    return;
                }

                // Hiển thị dialog xác nhận
                AlertDialog.Builder dialogthoat = new AlertDialog.Builder(editUser.this);
                dialogthoat.setTitle("Lưu thay đổi");
                dialogthoat.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Câu lệnh UPDATE
                        String updateQuery = "UPDATE TAI_KHOAN SET " +
                                "name = '" + name + "', " +
                                "sdt = '" + sdt + "', " +
                                "email = '" + email + "', " +
                                "address = '" + address + "', " +
                                "cccd = '" + cccd + "', " +
                                "role = " + role + " " +
                                "WHERE id = " + tk.getId() + ";";

                        try {
                            db1.updateSQL(updateQuery);
                            // Gửi kết quả về Activity 1
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("isUpdated", true); // Cung cấp thông tin đã cập nhật
                            setResult(RESULT_OK, resultIntent); // Trả về cho Activity 1
                            finish(); // Đóng Activity 2
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(editUser.this, "Đã xảy ra lỗi trong quá trình cập nhật", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialogthoat.setNegativeButton("Không", null);
                dialogthoat.show();
            }
        });
    }
}