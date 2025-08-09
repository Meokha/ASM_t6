package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.myapplication.databases.UserModel;
import com.example.myapplication.databases.UserRepository;

public class SettingsFragment extends Fragment {

    private TextView tvUserName, tvUserEmail;
    private Button btnChangePassword;
    private UserRepository userRepository;
    private UserModel currentUser;
    private String currentUsername;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Ánh xạ các view từ layout
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvUserEmail = view.findViewById(R.id.tv_user_email);
        btnChangePassword = view.findViewById(R.id.btn_change_password);

        // 2. Khởi tạo UserRepository
        userRepository = new UserRepository(getContext());

        // 3. Tải thông tin người dùng lên giao diện
        loadUserProfile();

        // 4. Gán sự kiện cho nút "Đổi mật khẩu"
        btnChangePassword.setOnClickListener(v -> {
            if (currentUser != null) {
                showChangePasswordDialog();
            } else {
                Toast.makeText(getContext(), "User information could not be loaded.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Lấy username đã lưu và tải thông tin chi tiết của người dùng từ database.
     */
    private void loadUserProfile() {
        // Giả sử bạn lưu username vào SharedPreferences sau khi đăng nhập thành công
        SharedPreferences prefs = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        currentUsername = prefs.getString("username", null); // Lấy username đã lưu

        if (currentUsername != null) {
            // Nếu có username, gọi hàm trong repository để lấy thông tin
            currentUser = userRepository.getInfoAccountByUsername(currentUsername);

            if (currentUser != null) {
                // Nếu tìm thấy user, cập nhật giao diện
                tvUserName.setText(currentUser.getUsername());
                tvUserEmail.setText(currentUser.getEmail());
            } else {
                tvUserName.setText("Error: User not found");
                tvUserEmail.setText("");
            }
        } else {
            // Nếu không có username nào được lưu
            Toast.makeText(getContext(), "Unable to load login information.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Hiển thị hộp thoại để người dùng nhập thông tin đổi mật khẩu.
     */
    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Nạp layout cho dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);

        final EditText etOldPassword = dialogView.findViewById(R.id.et_old_password);
        final EditText etNewPassword = dialogView.findViewById(R.id.et_new_password);
        final EditText etConfirmPassword = dialogView.findViewById(R.id.et_confirm_password);

        builder.setView(dialogView)
                .setTitle("Change password")
                .setPositiveButton("Save", (dialog, id) -> {
                    String oldPass = etOldPassword.getText().toString();
                    String newPass = etNewPassword.getText().toString();
                    String confirmPass = etConfirmPassword.getText().toString();

                    // --- Bắt đầu kiểm tra dữ liệu ---
                    if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                        Toast.makeText(getContext(), "Please enter complete information", Toast.LENGTH_SHORT).show();
                        return; // Dừng lại không làm gì cả
                    }

                    // Kiểm tra mật khẩu cũ. Bạn cần có hàm getPassword() trong UserModel
                    if (!currentUser.getPassword().equals(oldPass)) {
                        Toast.makeText(getContext(), "Old password is incorrect", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!newPass.equals(confirmPass)) {
                        Toast.makeText(getContext(), "New password does not match", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // --- Kết thúc kiểm tra dữ liệu ---

                    // Nếu mọi thứ hợp lệ, gọi hàm cập nhật trong UserRepository
                    // Bạn cần tạo hàm updatePassword trong UserRepository
                    boolean isUpdated = userRepository.updatePassword(currentUsername, newPass);
                    if (isUpdated) {
                        // Cập nhật lại mật khẩu trong đối tượng currentUser để lần sau đổi tiếp không bị sai
                        currentUser.setPassword(newPass);
                        Toast.makeText(getContext(), "Password changed successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Password change failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    // Người dùng nhấn Hủy, không làm gì cả
                    dialog.dismiss();
                });

        builder.create().show(); // Hiển thị dialog
    }
}