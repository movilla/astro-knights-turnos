package com.astroknights.turnorder;

import java.util.Arrays;
import java.util.Random;

/**
 * Mazo de orden de turnos de Astro Knights (lógica pura, sin dependencias de Android).
 *
 * Composición según el reglamento oficial (siempre 2 cartas de Jefe):
 *  - 1 jugador : 3 cartas de jugador + 2 de Jefe
 *  - 2 jugadores: 2 cartas por jugador + 2 de Jefe
 *  - 3 jugadores: 1 carta por jugador + carta comodín + 2 de Jefe
 *  - 4 jugadores: 2 cartas "1/2" + 2 cartas "3/4" + 2 de Jefe
 * Cuando el mazo se agota y hay que robar otra carta, se barajan todas de nuevo.
 */
public class TurnDeck {

    public static final int BOSS = 0;
    public static final int PLAYER_1 = 1;
    public static final int PLAYER_2 = 2;
    public static final int PLAYER_3 = 3;
    public static final int PLAYER_4 = 4;
    public static final int WILD = 5;
    public static final int PAIR_1_2 = 6;
    public static final int PAIR_3_4 = 7;

    private final int players;
    private final Random random;
    private final int[] cards;
    // Información resuelta de cada posición robada:
    //  - WILD: jugador (1..3) que tiene la ficha comodín y juega ese turno
    //  - PAIR_x: 0 si es la primera copia de la pareja en esta pasada, 1 si es la segunda
    private final int[] who;
    private int index;
    private int wildHolder;

    public TurnDeck(int players) {
        this(players, new Random());
    }

    public TurnDeck(int players, Random random) {
        checkPlayers(players);
        this.players = players;
        this.random = random;
        this.cards = baseCards(players);
        this.who = new int[cards.length];
        this.wildHolder = 1;
        shuffle();
        this.index = 0;
        resolve(0);
    }

    private TurnDeck(int players, int[] cards, int[] who, int index, int wildHolder) {
        this.players = players;
        this.random = new Random();
        this.cards = cards;
        this.who = who;
        this.index = index;
        this.wildHolder = wildHolder;
    }

    /** Reconstruye un mazo guardado. Devuelve null si los datos no son válidos. */
    public static TurnDeck restore(int players, int[] cards, int[] who, int index, int wildHolder) {
        if (players < 1 || players > 4 || cards == null || who == null) return null;
        int[] expected = baseCards(players);
        if (cards.length != expected.length || who.length != expected.length) return null;
        if (index < 0 || index >= cards.length) return null;
        int[] a = cards.clone();
        int[] b = expected.clone();
        Arrays.sort(a);
        Arrays.sort(b);
        if (!Arrays.equals(a, b)) return null;
        if (wildHolder < 1 || wildHolder > Math.max(players, 1)) wildHolder = 1;
        return new TurnDeck(players, cards.clone(), who.clone(), index, wildHolder);
    }

    private static void checkPlayers(int players) {
        if (players < 1 || players > 4) {
            throw new IllegalArgumentException("players must be 1..4");
        }
    }

    private static int[] baseCards(int players) {
        switch (players) {
            case 1:
                return new int[]{PLAYER_1, PLAYER_1, PLAYER_1, BOSS, BOSS};
            case 2:
                return new int[]{PLAYER_1, PLAYER_1, PLAYER_2, PLAYER_2, BOSS, BOSS};
            case 3:
                return new int[]{PLAYER_1, PLAYER_2, PLAYER_3, WILD, BOSS, BOSS};
            default:
                return new int[]{PAIR_1_2, PAIR_1_2, PAIR_3_4, PAIR_3_4, BOSS, BOSS};
        }
    }

    // Fisher-Yates
    private void shuffle() {
        for (int i = 0; i < cards.length - 1; i++) {
            int j = i + random.nextInt(cards.length - i);
            int t = cards[i];
            cards[i] = cards[j];
            cards[j] = t;
        }
    }

    private void resolve(int pos) {
        int c = cards[pos];
        if (c == WILD) {
            who[pos] = wildHolder;
            wildHolder = wildHolder % players + 1; // la ficha pasa al siguiente jugador
        } else if (c == PAIR_1_2 || c == PAIR_3_4) {
            int n = 0;
            for (int i = 0; i < pos; i++) {
                if (cards[i] == c) n++;
            }
            who[pos] = n;
        } else {
            who[pos] = 0;
        }
    }

    /** Roba la siguiente carta. Devuelve true si hubo que barajar el mazo de nuevo. */
    public boolean draw() {
        boolean reshuffled = false;
        if (index == cards.length - 1) {
            shuffle();
            index = 0;
            reshuffled = true;
        } else {
            index++;
        }
        resolve(index);
        return reshuffled;
    }

    public boolean canUndo() {
        return index > 0;
    }

    /** Deshace el último robo dentro de la pasada actual (no deshace un barajado). */
    public boolean undo() {
        if (index == 0) return false;
        if (cards[index] == WILD) {
            wildHolder = who[index]; // la ficha vuelve a quien la tenía
        }
        index--;
        return true;
    }

    public int getPlayers() { return players; }
    public int getIndex() { return index; }
    public int getSize() { return cards.length; }
    public int getRemaining() { return cards.length - 1 - index; }
    public int getCardAt(int pos) { return cards[pos]; }
    public int getWhoAt(int pos) { return who[pos]; }
    public int getCurrentCard() { return cards[index]; }
    public int getCurrentWho() { return who[index]; }
    /** Jugador que tendrá la ficha comodín a partir de ahora. */
    public int getWildHolder() { return wildHolder; }

    public int[] cardsCopy() { return cards.clone(); }
    public int[] whoCopy() { return who.clone(); }
}
