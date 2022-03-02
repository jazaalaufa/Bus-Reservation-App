package com.example.finalproject4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;

public class Booking extends AppCompatActivity {

    protected Cursor cursor;
    DbHelper dbHelper;
    SQLiteDatabase db;
    Spinner spinAsal, spinTujuan, spinJam, spinDewasa, spinAnak;
    Session session;
    String email;
    int id_book;
    public String sAsal, sTujuan, sTanggal, sJam, sDewasa, sAnak;
    int jmlDewasa, jmlAnak;
    int hargaDewasa, hargaAnak;
    int hargaTotalDewasa, hargaTotalAnak, hargaTotal;
    private EditText etTanggal;
    private DatePickerDialog dpTanggal;
    Calendar newCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        dbHelper = new DbHelper(Booking.this);
        db = dbHelper.getReadableDatabase();

        final String[] asal = {"DKI Jakarta", "Bandung", "Solo", "Yogyakarta", "Malang"};
        final String[] tujuan = {"DKI Jakarta", "Bandung", "Solo", "Yogyakarta", "Malang"};
        final String[] jam = {"6 AM", "9 AM", "4 PM", "7 PM", "10 PM"};
        final String[] dewasa = {"0", "1", "2", "3", "4", "5"};
        final String[] anak = {"0", "1", "2", "3", "4", "5"};

        spinAsal = findViewById(R.id.asal);
        spinTujuan = findViewById(R.id.tujuan);
        spinJam = findViewById(R.id.jam);
        spinDewasa = findViewById(R.id.dewasa);
        spinAnak = findViewById(R.id.anak);

        ArrayAdapter<CharSequence> adapterAsal = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, asal);
        adapterAsal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAsal.setAdapter(adapterAsal);

        ArrayAdapter<CharSequence> adapterTujuan = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, tujuan);
        adapterTujuan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTujuan.setAdapter(adapterTujuan);

        ArrayAdapter<CharSequence> adapterJam = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, jam);
        adapterJam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinJam.setAdapter(adapterJam);

        ArrayAdapter<CharSequence> adapterDewasa = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, dewasa);
        adapterDewasa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDewasa.setAdapter(adapterDewasa);

        ArrayAdapter<CharSequence> adapterAnak = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, anak);
        adapterAnak.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAnak.setAdapter(adapterAnak);


        spinAnak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sAnak = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinDewasa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sDewasa = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinTujuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sTujuan = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinAsal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sAsal = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinJam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sJam = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        etTanggal = findViewById(R.id.tanggal_berangkat);
        etTanggal.setInputType(InputType.TYPE_NULL);
        etTanggal.requestFocus();
        session = new Session(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(Session.KEY_EMAIL);
        setDateTimeField();

        Button btnBook = findViewById(R.id.book);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                perhitunganHarga();
                if (sAsal != null && sTujuan != null && sTanggal != null && sJam != null && sDewasa != null && sAnak != null) {
                    if ((sAsal.equalsIgnoreCase("DKI Jakarta") && sTujuan.equalsIgnoreCase("DKI Jakarta"))
                            || (sAsal.equalsIgnoreCase("bandung") && sTujuan.equalsIgnoreCase("bandung"))
                            || (sAsal.equalsIgnoreCase("Solo") && sTujuan.equalsIgnoreCase("Solo"))
                            || (sAsal.equalsIgnoreCase("yogyakarta") && sTujuan.equalsIgnoreCase("yogyakarta"))
                            || (sAsal.equalsIgnoreCase("Malang") && sTujuan.equalsIgnoreCase("Malang"))) {
                        Toast.makeText(Booking.this, "Asal dan Tujuan tidak boleh sama !", Toast.LENGTH_LONG).show();
                    } else {
                        AlertDialog dialog = new AlertDialog.Builder(Booking.this)
                                .setTitle("Ingin booking bus sekarang?")
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        try {
                                            db.execSQL("INSERT INTO TB_BOOK (asal, tujuan, tanggal, jam, dewasa, anak) VALUES ('" +
                                                    sAsal + "','" +
                                                    sTujuan + "','" +
                                                    sTanggal + "','" +
                                                    sJam + "','" +
                                                    sDewasa + "','" +
                                                    sAnak + "');");
                                            cursor = db.rawQuery("SELECT id_book FROM TB_BOOK ORDER BY id_book DESC", null);
                                            cursor.moveToLast();
                                            if (cursor.getCount() > 0) {
                                                cursor.moveToPosition(0);
                                                id_book = cursor.getInt(0);
                                            }
                                            db.execSQL("INSERT INTO TB_HARGA (email, id_book, harga_dewasa, harga_anak, harga_total) VALUES ('" +
                                                    email + "','" +
                                                    id_book + "','" +
                                                    hargaTotalDewasa + "','" +
                                                    hargaTotalAnak + "','" +
                                                    hargaTotal + "');");
                                            Toast.makeText(Booking.this, "Booking berhasil", Toast.LENGTH_LONG).show();
                                            finish();
                                        } catch (Exception e) {
                                            Toast.makeText(Booking.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .setNegativeButton("Tidak", null)
                                .create();
                        dialog.show();
                    }
                } else {
                    Toast.makeText(Booking.this, "Mohon lengkapi data pemesanan!", Toast.LENGTH_LONG).show();
                }
            }
        });

        setupToolbar();
    }


    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Bus Detail");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setDateTimeField() {
        etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpTanggal.show();
            }
        });

        dpTanggal = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(i, i1, i2);
                String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei",
                        "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
                sTanggal = i2 + " " + bulan[i1] + " " + i;
                etTanggal.setText(sTanggal);

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    private void perhitunganHarga() {
        if (sAsal.equalsIgnoreCase("DKI Jakarta") && sTujuan.equalsIgnoreCase("bandung")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("DKI Jakarta") && sTujuan.equalsIgnoreCase("Malang")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("DKI Jakarta") && sTujuan.equalsIgnoreCase("Solo")) {
            hargaDewasa = 150000;
            hargaAnak = 120000;
        } else if (sAsal.equalsIgnoreCase("DKI Jakarta") && sTujuan.equalsIgnoreCase("yogyakarta")) {
            hargaDewasa = 180000;
            hargaAnak = 140000;
        } else if (sAsal.equalsIgnoreCase("bandung") && sTujuan.equalsIgnoreCase("DKI Jakarta")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sAsal.equalsIgnoreCase("bandung") && sTujuan.equalsIgnoreCase("Malang")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("bandung") && sTujuan.equalsIgnoreCase("Solo")) {
            hargaDewasa = 120000;
            hargaAnak = 90000;
        } else if (sAsal.equalsIgnoreCase("bandung") && sTujuan.equalsIgnoreCase("yogyakarta")) {
            hargaDewasa = 190000;
            hargaAnak = 160000;
        } else if (sAsal.equalsIgnoreCase("Malang") && sTujuan.equalsIgnoreCase("DKI Jakarta")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Malang") && sTujuan.equalsIgnoreCase("bandung")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sAsal.equalsIgnoreCase("Malang") && sTujuan.equalsIgnoreCase("Solo")) {
            hargaDewasa = 170000;
            hargaAnak = 130000;
        } else if (sAsal.equalsIgnoreCase("Malang") && sTujuan.equalsIgnoreCase("yogyakarta")) {
            hargaDewasa = 180000;
            hargaAnak = 150000;
        } else if (sAsal.equalsIgnoreCase("Solo") && sTujuan.equalsIgnoreCase("DKI Jakarta")) {
            hargaDewasa = 150000;
            hargaAnak = 120000;
        } else if (sAsal.equalsIgnoreCase("Solo") && sTujuan.equalsIgnoreCase("bandung")) {
            hargaDewasa = 120000;
            hargaAnak = 90000;
        } else if (sAsal.equalsIgnoreCase("Solo") && sTujuan.equalsIgnoreCase("yogyakarta")) {
            hargaDewasa = 80000;
            hargaAnak = 40000;
        } else if (sAsal.equalsIgnoreCase("Solo") && sTujuan.equalsIgnoreCase("Malang")) {
            hargaDewasa = 170000;
            hargaAnak = 130000;
        } else if (sAsal.equalsIgnoreCase("yogyakarta") && sTujuan.equalsIgnoreCase("DKI Jakarta")) {
            hargaDewasa = 180000;
            hargaAnak = 140000;
        } else if (sAsal.equalsIgnoreCase("yogyakarta") && sTujuan.equalsIgnoreCase("bandung")) {
            hargaDewasa = 190000;
            hargaAnak = 160000;
        } else if (sAsal.equalsIgnoreCase("yogyakarta") && sTujuan.equalsIgnoreCase("Solo")) {
            hargaDewasa = 80000;
            hargaAnak = 40000;
        } else if (sAsal.equalsIgnoreCase("yogyakarta") && sTujuan.equalsIgnoreCase("Malang")) {
            hargaDewasa = 180000;
            hargaAnak = 150000;
        }


        jmlDewasa = Integer.parseInt(sDewasa);
        jmlAnak = Integer.parseInt(sAnak);

        hargaTotalDewasa = jmlDewasa * hargaDewasa;
        hargaTotalAnak = jmlAnak * hargaAnak;
        hargaTotal = hargaTotalDewasa + hargaTotalAnak;
    
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
