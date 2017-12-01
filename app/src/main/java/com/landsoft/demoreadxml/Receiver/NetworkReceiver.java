package com.landsoft.demoreadxml.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.landsoft.demoreadxml.R;

import static com.landsoft.demoreadxml.Constant.ActionConstant.ANY;
import static com.landsoft.demoreadxml.Constant.ActionConstant.WIFI;
import static com.landsoft.demoreadxml.MainActivity.connInternet;


/**
 * Created by TRANTUAN on 01-Dec-17.
 */

public class NetworkReceiver extends BroadcastReceiver {

    String sPref;
    boolean refreshDisplay;


    public NetworkReceiver(String sPref, boolean refreshDisplay ) {
        this.sPref = sPref;
        this.refreshDisplay = refreshDisplay;
    }



    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(WIFI.equals(sPref) && networkInfo!= null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
            refreshDisplay = true;
            Toast.makeText(context, "WIFI "+context.getResources().getString(R.string.wifi_connected), Toast.LENGTH_SHORT).show();
        }else if (ANY.equals(sPref) && networkInfo != null){
                refreshDisplay=true;
                Toast.makeText(context,"ANY "+context.getResources().getString( R.string.any_connected) , Toast.LENGTH_SHORT).show();
            }else {
            refreshDisplay = false;
            Toast.makeText(context, "NoConnect "+context.getResources().getString( R.string.lost_connection), Toast.LENGTH_SHORT).show();
        }

    }

}
