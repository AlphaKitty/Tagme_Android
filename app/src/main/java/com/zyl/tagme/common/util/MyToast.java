package com.zyl.tagme.common.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class MyToast {
    private static Toast mToast = null;

    private MyToast() {

    }

    public static void showToast(Context context, String toastInfo) {
        if (null == context || TextUtils.isEmpty(toastInfo)) {
            return;
        }
        if (null == mToast) {
            mToast = Toast.makeText(context, toastInfo, Toast.LENGTH_LONG);
        } else {
            mToast.cancel();
            mToast = Toast.makeText(context, toastInfo, Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    public static void hideToast() {
        mToast.cancel();
    }
}