package com.landsoft.demoreadxml;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.landsoft.demoreadxml.Adapter.NewsAdapter;
import com.landsoft.demoreadxml.Model.DownloadXml;
import com.landsoft.demoreadxml.Model.News;
import com.landsoft.demoreadxml.Receiver.NetworkReceiver;
import com.landsoft.demoreadxml.Settings.NetworkSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static com.landsoft.demoreadxml.Constant.ActionConstant.ANY;
import static com.landsoft.demoreadxml.Constant.ActionConstant.REQUEST_CODE;
import static com.landsoft.demoreadxml.Constant.ActionConstant.RESULT_CODE;
import static com.landsoft.demoreadxml.Constant.ActionConstant.RESULT_PREFERENCE;
import static com.landsoft.demoreadxml.Constant.ActionConstant.URL_WEB;
import static com.landsoft.demoreadxml.Constant.ActionConstant.WIFI;


public class MainActivity extends AppCompatActivity {
    private   List<News> newsList = null;
    private ListView lvNews;
    TextView tvErrors;

    // Whether the display should be refreshed.
    private String sPref = null;
    // The user's current network preference setting.

    public boolean refreshDisplay = false;

    public static boolean connInternet;

    // Whether there is a Wi-Fi connection.
    private static boolean wifiConnected = false;
    // Whether there is a mobile connection.
    private static boolean mobileConnected = false;

    // The BroadcastReceiver that tracks network connectivity changes.
    private NetworkReceiver  networkReceiver ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAndRequestPermissions();
        lvNews = findViewById(R.id.lvNews);
        tvErrors = findViewById(R.id.tv_error);

    }

    private void setOnItemlistView() {
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              News news = newsList.get(i);
                String urlPath = news.getLink();
//                Log.d("lvView", "onItemClick: " + urlPath);
               Intent intent = new Intent(MainActivity.this,DetailWeb.class);
               intent.putExtra(URL_WEB,urlPath);
               startActivity(intent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        // connect file SharePreference
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //get list share preference. if  have list preference equal setting, feedback get value default WI-FI
        sPref = preferences.getString("listPref", "Wi-Fi");

        // registration filter manifest

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        // registration BroadcastReceiver
        networkReceiver = new NetworkReceiver(sPref,refreshDisplay);
        this.registerReceiver(networkReceiver,filter);
        updateConnectedFlags();
        if(refreshDisplay){
            loadPage();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void loadPage() {
        if ((sPref.equals(ANY) && (wifiConnected || mobileConnected)) || (sPref.equals(WIFI) && wifiConnected)){
            setListView();
        }else {
            showErrorPage();
        }
    }

    private void showErrorPage() {
        tvErrors.setVisibility(View.VISIBLE);
        tvErrors.setText(getResources().getString(R.string.connection_error));
    }

    private void updateConnectedFlags() {
        ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            wifiConnected = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        }else{
            wifiConnected = false;
            mobileConnected = false;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkReceiver != null){
           this.unregisterReceiver(networkReceiver);
        }
    }

    // xin quyen
    private void checkAndRequestPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }

    private void setListView(){
        newsList = new ArrayList<>();
        DownloadXml downloadXml = new DownloadXml();
        String urlString = "http://vietnamnet.vn/rss/home.rss";
        downloadXml.execute(urlString);
        try {
            newsList = downloadXml.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (newsList != null){
            NewsAdapter newsAdapter = new NewsAdapter(this,R.layout.list_view,newsList);
            tvErrors.setVisibility(View.GONE);
            newsAdapter.notifyDataSetChanged();
            lvNews.setAdapter(newsAdapter);
            setOnItemlistView();
        }
    }


    // Populates the activity's options menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu,menu);
        return true;
    }

    // Handles the user's menu selection.

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mn_setting:
                Intent intent = new Intent(MainActivity.this, NetworkSetting.class);
                startActivityForResult(intent,REQUEST_CODE);
                return true;
            case R.id.mn_refresh:
                loadPage();
               default:
                   return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode){
            if (RESULT_CODE == resultCode) {
                refreshDisplay = data.getBooleanExtra(RESULT_PREFERENCE,false);
              //  Log.d("insideresult", "onActivityResult: " + refreshDisplay);
            }
        }
    }
}
