package com.chuyu.nfc.upgrade.util;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chuyu.nfc.R;

import org.xutils.common.util.DensityUtil;

/**
 * Created by Grady on 15/8/21.
 */
public class UpUtils {

    public static void alert(Context mcontext, String title, String message) {
        try{
            alert(mcontext,title, message, false, "确定", null);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static void alert(Context mcontext, String title, String message, boolean cancelable, String p, String n) {
        if(mcontext==null){
            return;
        }
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mcontext, R.style.AlertDialogCustom1);
        builder.setCancelable(cancelable);
        View view = LayoutInflater.from(mcontext).inflate(R.layout.dialog_upgrade, null);
        TextView biaoti = (TextView) view.findViewById(R.id.biaoti);
        TextView neirong = (TextView) view.findViewById(R.id.neirong);
        TextView queding = (TextView) view.findViewById(R.id.queding);
        TextView quxiao = (TextView) view.findViewById(R.id.quxiao);
        biaoti.setText(title);
        neirong.setText(message);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(DensityUtil.dip2px(230), LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(view);
        if (!TextUtils.isEmpty(p)) {
            queding.setText(p);
            queding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }else {
            queding.setVisibility(View.INVISIBLE);
        }
        if (!TextUtils.isEmpty(n)) {
            quxiao.setText(n);
            quxiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }else {
            quxiao.setVisibility(View.INVISIBLE);
        }
    }


}
