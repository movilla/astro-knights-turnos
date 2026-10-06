package com.astroknights.turnorder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/** Ajustes: número de jugadores y nombres. Se guardan al salir de la pantalla. */
public class SettingsActivity extends AppCompatActivity {

    private RadioGroup rgPlayers;
    private EditText etBoss;
    private final EditText[] etPlayers = new EditText[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        rgPlayers = findViewById(R.id.rg_players);
        etBoss = findViewById(R.id.et_boss);
        etPlayers[0] = findViewById(R.id.et_p1);
        etPlayers[1] = findViewById(R.id.et_p2);
        etPlayers[2] = findViewById(R.id.et_p3);
        etPlayers[3] = findViewById(R.id.et_p4);

        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS, MODE_PRIVATE);
        switch (prefs.getInt(MainActivity.PREF_PLAYERS, 2)) {
            case 1: rgPlayers.check(R.id.rb_1); break;
            case 3: rgPlayers.check(R.id.rb_3); break;
            case 4: rgPlayers.check(R.id.rb_4); break;
            default: rgPlayers.check(R.id.rb_2); break;
        }

        etBoss.setText(prefs.getString(MainActivity.PREF_BOSS, ""));
        for (int i = 0; i < 4; i++) {
            etPlayers[i].setHint(getString(R.string.player_default, i + 1));
            etPlayers[i].setText(prefs.getString(MainActivity.PREF_PLAYER_PREFIX + (i + 1), ""));
        }
        etBoss.setHint(getString(R.string.boss_default));

        findViewById(R.id.btn_reset).setOnClickListener(v -> {
            getSharedPreferences(MainActivity.PREFS, MODE_PRIVATE)
                    .edit().putBoolean(MainActivity.PREF_RESET, true).apply();
            Toast.makeText(this, R.string.settings_reset_done, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        int players;
        int checked = rgPlayers.getCheckedRadioButtonId();
        if (checked == R.id.rb_1) players = 1;
        else if (checked == R.id.rb_3) players = 3;
        else if (checked == R.id.rb_4) players = 4;
        else players = 2;

        SharedPreferences.Editor ed = getSharedPreferences(MainActivity.PREFS, MODE_PRIVATE).edit();
        ed.putInt(MainActivity.PREF_PLAYERS, players);
        ed.putString(MainActivity.PREF_BOSS, etBoss.getText().toString().trim());
        for (int i = 0; i < 4; i++) {
            ed.putString(MainActivity.PREF_PLAYER_PREFIX + (i + 1),
                    etPlayers[i].getText().toString().trim());
        }
        ed.apply();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
