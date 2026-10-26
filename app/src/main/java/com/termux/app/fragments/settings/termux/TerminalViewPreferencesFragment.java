package com.termux.app.fragments.settings.termux;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceDataStore;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.termux.R;
import com.termux.shared.termux.settings.preferences.TermuxAppSharedPreferences;

@Keep
public class TerminalViewPreferencesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Context context = getContext();
        if (context == null) return;

        PreferenceManager preferenceManager = getPreferenceManager();
        preferenceManager.setPreferenceDataStore(TerminalViewPreferencesDataStore.getInstance(context));

        setPreferencesFromResource(R.xml.termux_terminal_view_preferences, rootKey);
        PreferenceCategory tvPerformance = findPreference("tv_performance");
        if (tvPerformance != null) {
            int type = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_TYPE_MASK;
            boolean tv = type == Configuration.UI_MODE_TYPE_TELEVISION ||
                context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LEANBACK);
            tvPerformance.setVisible(tv);
        }
    }

}

class TerminalViewPreferencesDataStore extends PreferenceDataStore {

    private final Context mContext;
    private final TermuxAppSharedPreferences mPreferences;

    private static TerminalViewPreferencesDataStore mInstance;

    private TerminalViewPreferencesDataStore(Context context) {
        mContext = context;
        mPreferences = TermuxAppSharedPreferences.build(context, true);
    }

    public static synchronized TerminalViewPreferencesDataStore getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TerminalViewPreferencesDataStore(context);
        }
        return mInstance;
    }



    @Override
    public void putBoolean(String key, boolean value) {
        if (mPreferences == null) return;
        if (key == null) return;

        switch (key) {
            case "terminal_margin_adjustment":
                    mPreferences.setTerminalMarginAdjustment(value);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        if (mPreferences == null) return defValue;
        if ("terminal_margin_adjustment".equals(key))
            return mPreferences.isTerminalMarginAdjustmentEnabled();
        return defValue;
    }

    @Override
    public void putString(String key, String value) {
        if (mPreferences != null && "tv_memory_profile".equals(key))
            mPreferences.setTvMemoryProfile(value);
    }

    @Override
    public String getString(String key, String defValue) {
        if (mPreferences != null && "tv_memory_profile".equals(key))
            return mPreferences.getTvMemoryProfile();
        return defValue;
    }

}
