package com.example.cram_.projectjedi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.twitter.sdk.android.core.internal.TwitterCollection;

/**
 * Created by cram_ on 29/01/2016.
 */
public class Users extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "users";

    public static final String USER_TABLE = "User";

    public static final String SCORE_TABLE = "Score";

    public static final String USER_TABLE_CREATE = "CREATE TABLE " + USER_TABLE + " (name TEXT PRIMARY KEY, pass INTEGER, image TEXT, notif TEXT)";
    public static final String SCORE_TABLE_CREATE = "CREATE TABLE " + SCORE_TABLE + " (id INTEGER PRIMARY KEY, name TEXT, score INTEGER)";
    public Users(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE_CREATE);
        db.execSQL(SCORE_TABLE_CREATE);
    }

    public boolean createUser (ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        long error = db.insert(
                USER_TABLE,
                null,
                values
        );
        return error!=-1;
    }

    public Cursor getImage(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {"image"};
        String[] where = {name};
        Cursor c = db.query(
                USER_TABLE,
                columns,
                "name=?",
                where,
                null,
                null,
                null
        );
        return c;
    }

    public boolean setImage(String imageURI, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = {name};
        ContentValues cv = new ContentValues();
        cv.put("image",imageURI);
        long error = db.update(
                USER_TABLE,
                cv,
                "name=?",
                where
        );
        return error!=-1;
    }

    public Cursor getNotif(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {"notif"};
        String[] where = {name};
        Cursor c = db.query(
                USER_TABLE,
                columns,
                "name=?",
                where,
                null,
                null,
                null
        );
        return c;
    }

    public boolean setNotif(String notif, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = {name};
        ContentValues cv = new ContentValues();
        cv.put("notif",notif);
        long error = db.update(
                USER_TABLE,
                cv,
                "name=?",
                where
        );
        return error!=-1;
    }

    public boolean resetScores () {
        SQLiteDatabase db = this.getWritableDatabase();
        long error = db.delete(
                SCORE_TABLE,
                null,
                null
        );
        return error!=-1;
    }

    public boolean createScore (ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        long error = db.insert(
                SCORE_TABLE,
                null,
                values
        );
        return error!=-1;
    }

    public Cursor getUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {"name"};
        Cursor c = db.query(
                USER_TABLE,
                columns,
                null,
                null,
                null,
                null,
                null
        );
        return c;
    }

    public Cursor getScores() {
        SQLiteDatabase db = this.getWritableDatabase();
        //get all users and scores
        Cursor c = db.query(
                SCORE_TABLE,
                null,
                null,
                null,
                null,
                null,
                "score ASC"
        );
        return c;
    }

    public Cursor getHighScore(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {"score"};
        String[] where = {name};
        Cursor c = db.query(
                SCORE_TABLE,
                columns,
                "name=?",
                where,
                null,
                null,
                "score ASC"
        );
        return c;
    }

    public Cursor getPassByName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {"pass"};
        String[] where = {name};
        Cursor c = db.query(
                USER_TABLE,
                columns,
                "name=?",
                where,
                null,
                null,
                null
        );
        return c;
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {}
}
