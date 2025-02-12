package com.example.lupusincampus.Play.GestioneLogicaPartita;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public int getNumPlayer(int code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NUM_PLAYER},
                COLUMN_CODE + " = ?", new String[]{String.valueOf(code)},
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();  // Sposta il cursore alla prima riga
            @SuppressLint("Range") int numPlayer = cursor.getInt(cursor.getColumnIndex(COLUMN_NUM_PLAYER));  // Ottiene il numero di giocatori
            cursor.close();
            return numPlayer;
        } else {
            cursor.close();
            return -1;  // Restituisce -1 se la lobby non è trovata
        }
    }

    //ultima lobby creata
    @SuppressLint("Range")
    public int getLastRow() {
        int lastId = -1;  // Valore predefinito se non vengono trovati risultati
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                // Estrai l'ID dell'ultima riga
                lastId = cursor.getInt(cursor.getColumnIndex(COLUMN_CODE));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return lastId;
    }

}
