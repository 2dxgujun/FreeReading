package me.gujun.mybook.db.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

import me.gujun.mybook.db.table.BrowseRecordTable;

/**
 * File browse record model.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-26 12:50:49
 */
public class BrowseRecord implements Parcelable {
    public static final Parcelable.Creator<BrowseRecord> CREATOR = new Creator<BrowseRecord>() {
        @Override
        public BrowseRecord createFromParcel(Parcel source) {
            return new BrowseRecord(source);
        }

        @Override
        public BrowseRecord[] newArray(int size) {
            return new BrowseRecord[size];
        }
    };

    private int id;
    private String fileName;
    private String filePath;
    private String date;

    public BrowseRecord() {
    }

    public BrowseRecord(File file, String date) {
        this.fileName = file.getName();
        this.filePath = file.getAbsolutePath();
        this.date = date;
    }

    private BrowseRecord(Parcel in) {
        id = in.readInt();
        fileName = in.readString();
        filePath = in.readString();
        date = in.readString();
    }

    public static BrowseRecord resolve(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(BrowseRecordTable._ID));
        String fileName = cursor.getString(cursor.getColumnIndex(BrowseRecordTable.FILE_NAME));
        String filePath = cursor.getString(cursor.getColumnIndex(BrowseRecordTable.FILE_PATH));
        String date = cursor.getString(cursor.getColumnIndex(BrowseRecordTable.DATE));

        BrowseRecord browseRecord = new BrowseRecord();
        browseRecord.setId(id)
                .setFileName(fileName)
                .setFilePath(filePath)
                .setDate(date);

        return browseRecord;
    }

    public ContentValues assemble() {
        ContentValues values = new ContentValues();
        values.put(BrowseRecordTable.FILE_NAME, fileName);
        values.put(BrowseRecordTable.FILE_PATH, filePath);
        values.put(BrowseRecordTable.DATE, date);

        return values;
    }

    public String getFileName() {
        return fileName;
    }

    public BrowseRecord setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public int getId() {
        return id;
    }

    public BrowseRecord setId(int id) {
        this.id = id;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public BrowseRecord setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getDate() {
        return date;
    }

    public BrowseRecord setDate(String date) {
        this.date = date;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(fileName);
        dest.writeString(filePath);
        dest.writeString(date);
    }
}