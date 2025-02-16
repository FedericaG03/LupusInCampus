package com.example.lupusincampus.Play.GestioneLogicaPartita;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LobbyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lobby_database.db";
    private static final int DATABASE_VERSION = 1;

    // Nome della tabella
    private static final String TABLE_NAME = "lobbies";

    // Colonne della tabella
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CODE = "code";
    private static final String COLUMN_CREATOR_ID = "creatorID";
    private static final String COLUMN_CREATION_DATE = "creationDate";
    private static final String COLUMN_NUM_PLAYER = "numPlayer";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_STATE = "state";

    // Tabella player_lobbies
    private static final String TABLE_PLAYER_LOBBIES = "partecipants";
    private static final String COLUMN_LOBBY_ID = "lobbyID";
    private static final String COLUMN_PLAYER_NAME = "playerName";


    public LobbyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Creazione della tabella con le nuove colonne
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CODE + " INTEGER, "
                + COLUMN_CREATOR_ID + " INTEGER, "
                + COLUMN_CREATION_DATE + " TEXT, "
                + COLUMN_NUM_PLAYER + " INTEGER, "
                + COLUMN_TYPE + " TEXT, "
                + COLUMN_STATE + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE);

        String CREATE_PARTECUPANTS = "CREATE TABLE partecipants ( " +
                "lobbyID INTEGER NOT NULL," +
                "playerName TEXT NOT NULL," +
                "PRIMARY KEY (lobbyID, playerName)," +
                "FOREIGN KEY (lobbyID) REFERENCES lobbies(id) ON DELETE CASCADE" +
                ");";

        sqLiteDatabase.execSQL(CREATE_PARTECUPANTS);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se necessario, gestisci l'aggiornamento del database
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Metodo per inserire una lobby nel database
    public void insertLobby(int code, int creatorID, String creationDate, int numPlayer, String type, String state) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODE, code);
        values.put(COLUMN_CREATOR_ID, creatorID);
        values.put(COLUMN_CREATION_DATE, creationDate);
        values.put(COLUMN_NUM_PLAYER, numPlayer);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_STATE, state);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Metodo per recuperare tutte le lobby
    public Cursor getAllLobbies() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public void clearLobbies() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }


    public void updateNumPlayer(int code, int numPlayer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("numPlayer", numPlayer);  // Aggiorna il numero di giocatori

        // Esegui l'aggiornamento della lobby
        db.update("Lobbies", values, "code = ?", new String[]{String.valueOf(code)});
        db.close();
    }

    @SuppressLint("Range")
    public int getNumPlayer(int code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NUM_PLAYER},
                COLUMN_CODE + " = ?", new String[]{String.valueOf(code)},
                null, null, null);
        int numPlayer = 0;

        if (cursor != null) {
            if(cursor.moveToFirst()){
                numPlayer = cursor.getInt(cursor.getColumnIndex(COLUMN_NUM_PLAYER));  // Ottiene il numero di giocatori
            }
            cursor.close();
            return numPlayer;
        }  // Restituisce -1 se la lobby non è trovata
        return numPlayer;
    }

    //ultima lobby creata
    @SuppressLint("Range")
    public int getLastRow() {
        int lastId = 0;  // Valore predefinito se non vengono trovati risultati
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT " + COLUMN_CODE + " FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                // Estrai l'ID dell'ultima riga
                lastId = cursor.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return lastId;
    }

    public String getLobbyType(int code) {
        // Valore predefinito se non vengono trovati risultati
        SQLiteDatabase db = this.getReadableDatabase();
        String type = "";
        try (Cursor cursor = db.rawQuery("SELECT " + COLUMN_TYPE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_CODE + " = " + code, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                // Estrai l'ID dell'ultima riga
                type = cursor.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return type;
    }


    /**
     * Ottiene tutti i playerName di una determinata lobby.
     */
    public List<String> getPlayesByLobbyID(int lobbyID) {
        List<String> playerNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_PLAYER_NAME + " FROM " + TABLE_PLAYER_LOBBIES + " WHERE " + COLUMN_LOBBY_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(lobbyID)});

        if (cursor.moveToFirst()) {
            do {
                String playerName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLAYER_NAME));
                playerNames.add(playerName);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return playerNames;
    }

    /**
     * Inserisce una lista di playerID in una specifica lobby.
     * @param lobbyID L'ID della lobby in cui inserire i giocatori.
     * @param playerNames Lista dei nomi dei giocatori da aggiungere.
     * @return true se tutti gli inserimenti hanno successo, false in caso di errore.
     */
    public boolean insertPlayersIntoLobby(int lobbyID, List<String> playerNames) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            for (String playerName : playerNames) {
                ContentValues values = new ContentValues();
                values.put("lobbyID", lobbyID);
                values.put("playerName", playerName);

                long result = db.insert(TABLE_PLAYER_LOBBIES, null, values);
                if (result == -1) {
                    throw new SQLException("Errore nell'inserimento del player: " + playerName);
                }
            }
            db.setTransactionSuccessful();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

}
