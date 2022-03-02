package com.example.finalproject4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class History extends AppCompatActivity {

    TextView tvNotFound;
    String id_book = "", asal, tujuan, tanggal, jam, dewasa, anak, riwayat, total;
    String email;
    DbHelper dbHelper;
    SQLiteDatabase db;
    Session session;
    protected Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        dbHelper = new DbHelper(this);
        db = dbHelper.getReadableDatabase();

        tvNotFound = findViewById(R.id.noHistory);

        session = new Session(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();

        email = user.get(Session.KEY_EMAIL);

        refreshList();
        setupToolbar();

    }

    private void refreshList() {
        final ArrayList<HistoryModel> hasil = new ArrayList<>();
        cursor = db.rawQuery("SELECT * FROM TB_BOOK, TB_HARGA WHERE TB_BOOK.id_book = TB_HARGA.id_book AND email='" + email + "'", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            id_book = cursor.getString(0);
            asal = cursor.getString(1);
            tujuan = cursor.getString(2);
            tanggal = cursor.getString(3);
            jam = cursor.getString(4);
            dewasa = cursor.getString(5);
            anak = cursor.getString(6);
            total = cursor.getString(11);
            riwayat = "Anda telah melakukan booking bus untuk melakukan perjalanan dari " + asal + " menuju " + tujuan + " pada tanggal " + tanggal +" pukul "+ jam + ". " +
                    "Jumlah pembelian tiket dewasa sejumlah " + dewasa + " dan tiket anak-anak sejumlah " + anak + ".";
            hasil.add(new HistoryModel(id_book, jam, tanggal, riwayat, total, R.drawable.ic_profile));
        }

        ListView listBook = findViewById(R.id.list_booking);
        HistoryAdapter arrayAdapter = new HistoryAdapter(this, hasil);
        listBook.setAdapter(arrayAdapter);

        //delete data
        listBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String selection = hasil.get(i).getIdBook();
                final CharSequence[] dialogitem = {"Hapus Data"};
                AlertDialog.Builder builder = new AlertDialog.Builder(History.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        try {
                            db.execSQL("DELETE FROM TB_BOOK where id_book = " + selection + "");
                            id_book = "";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        refreshList();
                    }
                });
                builder.create().show();
            }
        });

        if (id_book.equals("")) {
            tvNotFound.setVisibility(View.VISIBLE);
            listBook.setVisibility(View.GONE);
        } else {
            tvNotFound.setVisibility(View.GONE);
            listBook.setVisibility(View.VISIBLE);
        }

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.tbHistory);
        toolbar.setTitle("Booking History");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}