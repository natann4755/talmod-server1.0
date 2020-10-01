package com.example.myapp.utils;

import android.app.Activity;

public class Toast {
    public static void MyToast(Activity activity, String msg) {
        android.widget.Toast.makeText(activity, msg,
                android.widget.Toast.LENGTH_SHORT).show();
    }
}
