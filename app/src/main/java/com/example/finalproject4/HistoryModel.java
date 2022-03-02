package com.example.finalproject4;

public class HistoryModel {


    private String mIdBook;
    private String mTanggal;
    private String mJam;
    private String mRiwayat;
    private String mTotal;
    private int mImageResourceId;
    private static final int NO_IMAGE_PROVIDED = -1;

    public HistoryModel(String idBook,  String tanggal, String jam, String riwayat, String total, int imageResourceId) {
        mIdBook = idBook;
        mTanggal = tanggal;
        mJam = jam;
        mRiwayat = riwayat;
        mTotal = total;
        mImageResourceId = imageResourceId;
    }

    public String getIdBook() {
        return mIdBook;
    }

    public String getTanggal() {
        return mTanggal;
    }

    public String getJam() {
        return mJam;
    }

    public String getRiwayat() {
        return mRiwayat;
    }

    public String getTotal() {
        return mTotal;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

}