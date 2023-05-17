package com.example.it_food.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateValidator {
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    public static boolean isValidDate(String inputDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        dateFormat.setLenient(false);

        try {
            // Chuyển đổi ngày nhập vào thành đối tượng Date
            Date date = dateFormat.parse(inputDate);

            // Kiểm tra ngày nhập vào có hợp lệ không
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            return isValidYear(year) && isValidMonth(month) && isValidDayOfMonth(year, month, dayOfMonth);
        } catch (ParseException e) {
            return false; // Ngày không hợp lệ nếu không thể chuyển đổi thành đối tượng Date
        }
    }

    private static boolean isValidYear(int year) {
        // Kiểm tra năm nhập vào có hợp lệ hay không (ví dụ: từ 1900 đến năm hiện tại)
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return year >= 1900 && year <= currentYear;
    }

    private static boolean isValidMonth(int month) {
        // Kiểm tra tháng nhập vào có hợp lệ hay không (từ 0 đến 11)
        return month >= 0 && month <= 11;
    }

    private static boolean isValidDayOfMonth(int year, int month, int dayOfMonth) {
        // Kiểm tra ngày trong tháng nhập vào có hợp lệ hay không
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        int maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return dayOfMonth >= 1 && dayOfMonth <= maxDayOfMonth;
    }
}
