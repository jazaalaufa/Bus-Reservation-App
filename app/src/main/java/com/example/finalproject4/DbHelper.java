package com.example.finalproject4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    //database
    public static final String DATABASE_NAME = "db_bus";

    //user
    public static final String TABLE_USER = "tb_user";
    public static final String USERNAME = "email";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String HP = "hp";

    //booking
    public static final String TABLE_BOOK = "tb_book";
    public static final String ID_BOOK = "id_book";
    public static final String ASAL = "asal";
    public static final String TUJUAN = "tujuan";
    public static final String TANGGAL = "tanggal";
    public static final String JAM = "jam";
    public static final String DEWASA = "dewasa";
    public static final String ANAK = "anak";

    //harga
    public static final String TABLE_HARGA = "tb_harga";
    public static final String HARGA_DEWASA = "harga_dewasa";
    public static final String HARGA_ANAK = "harga_anak";
    public static final String HARGA_TOTAL = "harga_total";

    private SQLiteDatabase db;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //foreign
        db.execSQL("PRAGMA foreign_keys=ON");

        //table user
        db.execSQL("create table " + TABLE_USER + " (" + USERNAME + " TEXT PRIMARY KEY, " + PASSWORD +
                " TEXT, " + NAME + " TEXT, " + HP + " TEXT)");

        //table booking
        db.execSQL("create table " + TABLE_BOOK + " (" + ID_BOOK + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ASAL + " TEXT, " + TUJUAN + " TEXT" + ", " + TANGGAL + " TEXT, " + JAM + " TEXT, " + DEWASA + " TEXT, "
                + ANAK + " TEXT)");

        //table harga
        db.execSQL("create table " + TABLE_HARGA + " (" + USERNAME + " TEXT, " + ID_BOOK + " INTEGER, " +
                HARGA_DEWASA + " TEXT, " + HARGA_ANAK + " TEXT, " + HARGA_TOTAL +
                " TEXT, FOREIGN KEY(" + USERNAME + ") REFERENCES " + TABLE_USER
                + ", FOREIGN KEY(" + ID_BOOK + ") REFERENCES " + TABLE_BOOK + ")");

        //table user
        db.execSQL("insert into " + TABLE_USER + " values ('prdn@gmail.com','prdn123','Prdn','085231454852');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void open() throws SQLException {
        db = this.getWritableDatabase();
    }

    public boolean Register(String email, String password, String name, String hp) throws SQLException {

        @SuppressLint("Recycle") Cursor mCursor = db.rawQuery("INSERT INTO " + TABLE_USER + "(" + USERNAME + ", " + PASSWORD + ", " + NAME + ", " + HP + ") VALUES (?,?,?,?)", new String[]{email, password, name, hp});
        if (mCursor != null) {
            return mCursor.getCount() > 0;
        }
        return false;
    }

    public boolean Login(String email, String password) throws SQLException {
        @SuppressLint("Recycle") Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + USERNAME + "=? AND " + PASSWORD + "=?", new String[]{email, password});
        if (mCursor != null) {
            return mCursor.getCount() > 0;
        }
        return false;
    }

}