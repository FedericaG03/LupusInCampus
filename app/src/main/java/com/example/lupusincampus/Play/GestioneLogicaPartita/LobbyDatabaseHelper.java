package com.example.lupusincampus.Play.GestioneLogicaPartita;

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
    private static final String COLUMN_MIN_NUM_PLAYER = "minNumPlayer";
    private static final String COLUMN_NUM_PLAYER = "numPlayer";
    private static final String COLUMN_MAX_NUM_PLAYER = "maxNumPlayer";
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
                + COLUMN_MIN_NUM_PLAYER + " INTEGER, "
                + COLUMN_NUM_PLAYER + " INTEGER, "
                + COLUMN_MAX_NUM_PLAYER + " INTEGER, "
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
    public void insertLobby(int code, int creatorID, String creationDate, int minNumPlayer,
                            int numPlayer, int maxNumPlayer, String type, String state) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODE, code);
        values.put(COLUMN_CREATOR_ID, creatorID);
        values.put(COLUMN_CREATION_DATE, creationDate);
        values.put(COLUMN_MIN_NUM_PLAYER, minNumPlayer);
        values.put(COLUMN_NUM_PLAYER, numPlayer);
        values.put(COLUMN_MAX_NUM_PLAYER, maxNumPlayer);
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

}
