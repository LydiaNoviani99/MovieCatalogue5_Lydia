package com.lydia.moviecatalogue5.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.lydia.moviecatalogue5.R;
import com.lydia.moviecatalogue5.reminder.DailyReminder;
import com.lydia.moviecatalogue5.reminder.ReleaseReminder;

public class SettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    Switch dailySwitch, releaseSwitch;
    ReleaseReminder releaseReminder = new ReleaseReminder();
    DailyReminder dailyReminder = new DailyReminder();
    SharedPreferences dailySp, releaseSp;

    private String keyDaily, keyRelease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        keyDaily = getResources().getString(R.string.key_daily);
        keyRelease = getResources().getString(R.string.key_relase);

        dailySwitch = findViewById(R.id.switch_daily);
        dailySwitch.setOnCheckedChangeListener(this);

        releaseSwitch = findViewById(R.id.switch_release);
        releaseSwitch.setOnCheckedChangeListener(this);

        dailySp = getSharedPreferences(keyDaily, MODE_PRIVATE);
        dailySwitch.setChecked(dailySp.getBoolean(keyDaily, false));

        releaseSp = getSharedPreferences(keyRelease, MODE_PRIVATE);
        releaseSwitch.setChecked(releaseSp.getBoolean(keyRelease, false));

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean flag) {
        switch (compoundButton.getId()) {
            case R.id.switch_release:
                if (flag) {
                    releaseReminder.setAlarm(getApplicationContext());
                    SharedPreferences.Editor editor = getSharedPreferences(keyRelease, MODE_PRIVATE).edit();
                    editor.putBoolean(keyRelease, true);
                    editor.apply();
                } else {
                    releaseReminder.cancelAlarm(getApplicationContext());
                    SharedPreferences.Editor editor = getSharedPreferences(keyRelease, MODE_PRIVATE).edit();
                    editor.remove(keyRelease);
                    editor.apply();
                }
                break;

            case R.id.switch_daily:
                if (flag) {
                    dailyReminder.setAlarm(getApplicationContext());
                    SharedPreferences.Editor editor = getSharedPreferences(keyDaily, MODE_PRIVATE).edit();
                    editor.putBoolean(keyDaily, true);
                    editor.apply();
                } else {
                    dailyReminder.cancelAlarm(getApplicationContext());
                    SharedPreferences.Editor editor = getSharedPreferences(keyDaily, MODE_PRIVATE).edit();
                    editor.remove(keyDaily);
                    editor.apply();
                }
                break;
            }
        }
    }


