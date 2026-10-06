package com.astroknights.turnorder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * Pantalla principal: muestra de quién es el turno actual. Tocar la pantalla roba la siguiente
 * carta del mazo de turnos de Astro Knights.
 */
public class MainActivity extends AppCompatActivity {

    static final String PREFS = "astro_knights_prefs";
    static final String PREF_PLAYERS = "players";
    static final String PREF_BOSS = "name_boss";
    static final String PREF_PLAYER_PREFIX = "name_player_";
    static final String PREF_RESET = "reset_requested";

    private static final String STATE_PLAYERS = "players";
    private static final String STATE_CARDS = "cards";
    private static final String STATE_WHO = "who";
    private static final String STATE_INDEX = "index";
    private static final String STATE_WILD = "wild";

    private TurnDeck deck;
    private String bossName;
    private final String[] playerNames = new String[4];

    private View root;
    private TextView tvCurrent;
    private TextView tvSubtitle;
    private TextView tvHistory;
    private TextView tvRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = findViewById(R.id.root);
        tvCurrent = findViewById(R.id.tv_current);
        tvSubtitle = findViewById(R.id.tv_subtitle);
        tvHistory = findViewById(R.id.tv_history);
        tvRemaining = findViewById(R.id.tv_remaining);

        root.setOnClickListener(v -> drawNext());
        findViewById(R.id.btn_undo).setOnClickListener(v -> undo());
        findViewById(R.id.btn_settings).setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));

        if (savedInstanceState != null) {
            deck = TurnDeck.restore(
                    savedInstanceState.getInt(STATE_PLAYERS),
                    savedInstanceState.getIntArray(STATE_CARDS),
                    savedInstanceState.getIntArray(STATE_WHO),
                    savedInstanceState.getInt(STATE_INDEX),
                    savedInstanceState.getInt(STATE_WILD));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        bossName = nameOrDefault(prefs.getString(PREF_BOSS, ""), getString(R.string.boss_default));
        for (int i = 0; i < 4; i++) {
            playerNames[i] = nameOrDefault(prefs.getString(PREF_PLAYER_PREFIX + (i + 1), ""),
                    getString(R.string.player_default, i + 1));
        }

        int players = Math.max(1, Math.min(4, prefs.getInt(PREF_PLAYERS, 2)));
        boolean resetRequested = prefs.getBoolean(PREF_RESET, false);
        if (resetRequested) {
            prefs.edit().putBoolean(PREF_RESET, false).apply();
        }
        // Mazo nuevo al empezar, si cambia el número de jugadores o si se pide reiniciar.
        if (deck == null || deck.getPlayers() != players || resetRequested) {
            deck = new TurnDeck(players);
        }
        render();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (deck != null) {
            outState.putInt(STATE_PLAYERS, deck.getPlayers());
            outState.putIntArray(STATE_CARDS, deck.cardsCopy());
            outState.putIntArray(STATE_WHO, deck.whoCopy());
            outState.putInt(STATE_INDEX, deck.getIndex());
            outState.putInt(STATE_WILD, deck.getWildHolder());
        }
    }

    private void drawNext() {
        if (deck == null) return;
        boolean reshuffled = deck.draw();
        if (reshuffled) {
            Toast.makeText(this, R.string.reshuffled, Toast.LENGTH_SHORT).show();
        }
        render();
    }

    private void undo() {
        if (deck != null && deck.undo()) {
            render();
        }
    }

    private static String nameOrDefault(String value, String fallback) {
        return (value == null || value.trim().isEmpty()) ? fallback : value.trim();
    }

    private String playerName(int p) {
        return playerNames[p - 1];
    }

    private int playerColor(int p) {
        switch (p) {
            case 1: return ContextCompat.getColor(this, R.color.player1_bg);
            case 2: return ContextCompat.getColor(this, R.color.player2_bg);
            case 3: return ContextCompat.getColor(this, R.color.player3_bg);
            default: return ContextCompat.getColor(this, R.color.player4_bg);
        }
    }

    /** Texto corto de una carta ya robada (para el historial de la pasada actual). */
    private String historyLabel(int pos) {
        int card = deck.getCardAt(pos);
        int who = deck.getWhoAt(pos);
        switch (card) {
            case TurnDeck.BOSS:
                return bossName;
            case TurnDeck.WILD:
                return getString(R.string.history_wild, playerName(who));
            case TurnDeck.PAIR_1_2:
            case TurnDeck.PAIR_3_4: {
                String pair = pairTitle(card);
                return who == 1 ? getString(R.string.history_pair_second, pair) : pair;
            }
            default:
                return playerName(card);
        }
    }

    private String pairTitle(int card) {
        int a = card == TurnDeck.PAIR_1_2 ? 1 : 3;
        return getString(R.string.pair_title, playerName(a), playerName(a + 1));
    }

    private void render() {
        if (deck == null) return;

        int card = deck.getCurrentCard();
        int who = deck.getCurrentWho();
        String title;
        String subtitle;

        switch (card) {
            case TurnDeck.BOSS:
                title = bossName;
                subtitle = getString(R.string.subtitle_boss);
                root.setBackgroundColor(ContextCompat.getColor(this, R.color.boss_bg));
                break;
            case TurnDeck.WILD:
                title = playerName(who);
                subtitle = getString(R.string.subtitle_wild, playerName(deck.getWildHolder()));
                root.setBackgroundColor(playerColor(who));
                break;
            case TurnDeck.PAIR_1_2:
            case TurnDeck.PAIR_3_4: {
                int a = card == TurnDeck.PAIR_1_2 ? 1 : 3;
                title = pairTitle(card);
                subtitle = getString(who == 0 ? R.string.subtitle_pair_first
                        : R.string.subtitle_pair_second);
                int c1 = playerColor(a);
                int c2 = playerColor(a + 1);
                // Fondo partido en dos colores (uno por cada jugador de la pareja)
                GradientDrawable bg = new GradientDrawable(
                        GradientDrawable.Orientation.LEFT_RIGHT, new int[]{c1, c1, c2, c2});
                root.setBackground(bg);
                break;
            }
            default: // carta de un jugador concreto
                title = playerName(card);
                subtitle = getString(R.string.subtitle_player);
                root.setBackgroundColor(playerColor(card));
                break;
        }

        tvCurrent.setText(title);
        tvSubtitle.setText(subtitle);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= deck.getIndex(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(historyLabel(i));
        }
        tvHistory.setText(getString(R.string.history_title, sb.toString()));
        tvRemaining.setText(getString(R.string.remaining, deck.getRemaining()));
    }
}
