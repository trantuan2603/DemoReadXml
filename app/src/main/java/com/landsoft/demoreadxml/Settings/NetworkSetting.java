package com.landsoft.demoreadxml.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

import com.landsoft.demoreadxml.R;

import static com.landsoft.demoreadxml.Constant.ActionConstant.RESULT_CODE;
import static com.landsoft.demoreadxml.Constant.ActionConstant.RESULT_PREFERENCE;


/**
 * Created by TRANTUAN on 01-Dec-17.
 */

public class NetworkSetting extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_PREFERENCE,true);
        setResult(RESULT_CODE,intent);
        finish();
    }
}
