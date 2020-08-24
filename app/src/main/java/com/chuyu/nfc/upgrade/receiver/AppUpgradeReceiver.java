package com.chuyu.nfc.upgrade.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.chuyu.nfc.upgrade.util.AppUtil;


/**
 * Created by xxu on 03/05/2018.
 */
public class AppUpgradeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context,"接收到广播", Toast.LENGTH_LONG).show();
        //接收覆盖安装广播
        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            //Toast.makeText(context,"安装一个安装包", Toast.LENGTH_LONG).show();
        }
        if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
            //Toast.makeText(context,"卸载了一个安装包", Toast.LENGTH_LONG).show();
        }
        if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {

            String pgName=intent.getDataString();
            String pkname= AppUtil.getPackageName(context);
            if(pgName !=null && pgName.equals("package:"+ pkname)){

                Intent intent2 = new Intent();
                //第二种方式
                intent2.setClassName(pkname, pkname+".ui.FirstActivity");
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent2);
            }
        }
    }

}
