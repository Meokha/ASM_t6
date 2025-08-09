package com.example.myapplication.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.myapplication.R;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationHelper {

    private static final String CHANNEL_ID = "expense_channel";
    private static final String CHANNEL_NAME = "Expense Notifications";

    private Context context;
    private NotificationManager notificationManager;

    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    // Tạo Notification Channel (bắt buộc từ Android 8.0 trở lên)
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notification of new expenses");
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Hiển thị thông báo về biến động số dư.
     * @param amount Số tiền đã chi (dạng double).
     * @param budgetName Tên ngân sách bị trừ tiền.
     */
    public void showExpenseNotification(double amount, String budgetName) {
        // Định dạng số tiền và ngày tháng
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedAmount = currencyFormatter.format(amount);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        String notificationTime = sdf.format(new Date());

        // Nội dung thông báo
        String title = "Balance fluctuations";
        String content = "Account " + budgetName + " -" + formattedAmount + " at the time " + notificationTime;

        // Xây dựng thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_icon) // <-- BẠN CẦN TẠO ICON NÀY
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content)) // Để hiển thị đầy đủ nội dung
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); // Tự động xóa thông báo khi người dùng nhấn vào

        // Hiển thị thông báo với một ID duy nhất (dựa trên thời gian hiện tại)
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}