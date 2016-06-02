package edu.scu.suragys.assignment3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by suragys on 5/20/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MyObject.TABLE_NAME + " (" +
                    MyObject.ID + " INTEGER PRIMARY KEY," +
                    MyObject.COLUMN_NAME_PATH + TEXT_TYPE + COMMA_SEP +
                    MyObject.COLUMN_NAME_CAPTION + TEXT_TYPE + COMMA_SEP +
                    MyObject.COLUMN_NAME_THUMBNAILPATH + TEXT_TYPE + COMMA_SEP +
                    MyObject.COLUMN_NAME_POSITION + " INTEGER" + COMMA_SEP +
                    MyObject.COLUMN_NAME_LATTITUDE + TEXT_TYPE + COMMA_SEP +
                    MyObject.COLUMN_NAME_LONGITUDE + TEXT_TYPE + COMMA_SEP +
                    MyObject.COLUMN_NAME_AUDIOPATH + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MyObject.TABLE_NAME;

//    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "photoNotes2.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        Log.i("create Table",SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long addData(MyObject object) {
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
//        values.put(MyObject.COLUMN_NAME_ENTRY_ID, id);

        values.put(MyObject.COLUMN_NAME_PATH, object.getPath());
        values.put(MyObject.COLUMN_NAME_CAPTION, object.getCaption());
        values.put(MyObject.COLUMN_NAME_THUMBNAILPATH, object.getThumbNailPath());
        values.put(MyObject.COLUMN_NAME_POSITION, object.getPosition());
        values.put(MyObject.COLUMN_NAME_LATTITUDE,object.getLattitude());
        values.put(MyObject.COLUMN_NAME_LONGITUDE,object.getLongitude());
        values.put(MyObject.COLUMN_NAME_AUDIOPATH,object.getAudioPath());

// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(MyObject.TABLE_NAME, null,values);
//        object.setId((int)newRowId);
        //Log.v("row Id gen", "------------------------ " + newRowId);
        return newRowId;
    }

    public ArrayList<MyObject> getDataObjects() {

        //List<MyObject> objects = MyObject.getObjectsList();

        SQLiteDatabase db = getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.

        String[] projection = {
                MyObject.ID,
                MyObject.COLUMN_NAME_PATH,
                MyObject.COLUMN_NAME_CAPTION,
                MyObject.COLUMN_NAME_THUMBNAILPATH,
                MyObject.COLUMN_NAME_POSITION,
                MyObject.COLUMN_NAME_LATTITUDE,
                MyObject.COLUMN_NAME_LONGITUDE,
                MyObject.COLUMN_NAME_AUDIOPATH
        };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                MyObject.COLUMN_NAME_POSITION + " ASC";

        Cursor c = db.query(
                MyObject.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );


        return getDataFromCursor(c);


    }

    private ArrayList<MyObject> getDataFromCursor(Cursor c) {

        ArrayList<MyObject> objects = new ArrayList<MyObject>();

        c.moveToFirst();

        while (c.moveToNext()) {
            MyObject o = new MyObject();
            int id = c.getInt(0);
            o.setId(id);
            String path = c.getString(1);
            o.setPath(path);
            String caption = c.getString(2);
            o.setCaption(caption);
            String thumbNailPath = c.getString(3);
            o.setThumbNailPath(thumbNailPath);
            int postion = c.getInt(4);
            o.setPosition(postion);
            o.setLattitude(c.getString(5));
            o.setLongitude(c.getString(6));
            o.setAudioPath(c.getString(7));

            //Log.v("db retrieval" , id + path + caption + thumbNailPath + postion);
            objects.add(o);
        }


        return objects;

    }

    public boolean deleteRow(MyObject object) {
        // Define 'where' part of query.
        int id = object.getId();
        String selection = MyObject.ID + " = " + id;
// Specify arguments in placeholder order.

        //Log.v("Object id to delete" ,  id+"");
//        String[] selectionArgs = {String.valueOf(id)};
// Issue SQL statement.
       return  getWritableDatabase().delete(MyObject.TABLE_NAME, selection, null) > 0;

    }

    public void update(ArrayList<MyObject> myDataset) {

        for (MyObject o:myDataset ) {


            ContentValues args = new ContentValues();
            args.put(MyObject.COLUMN_NAME_POSITION, o.getPosition());
            int i = getWritableDatabase().update(MyObject.TABLE_NAME, args, MyObject.ID + "=" + o.getId(), null);
        }
    }

    public void deleteTable(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
    }

    public void createTable(){
        SQLiteDatabase db = getWritableDatabase();
        Log.i("create Table",SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }
}
