package in.cdac.inventoryap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import in.cdac.inventoryap.InventoryContract.InventoryEntry;

/**
 * Created by Dell1 on 14/03/2018.
 */

public class InventryHelperDb extends SQLiteOpenHelper {


    private static String TAG = InventryHelperDb.class.getName();
    private String DATABASE_NAME = "inventory_database";
    private int DATABASE_VERSION = 1;
    private String CREATE_ENTRY2 = "CREATE TABLE  " + InventoryEntry.TABLE2 + "(" + InventoryEntry.COLUMN_ID + " INTEGER PRIMARY KEY ," + InventoryEntry.COLUMN_PRODUCT_IMAGE + "  BLOB ," + InventoryEntry.COLUMN_PRODUCT_NAME + " TEXT ," + InventoryEntry.COLUMN_PRODUCT_PRICE + " REAL , " + InventoryEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER ," + InventoryEntry.COLUMN_PRODUCT_SUPPLIER + " TEXT )";
    Context context;
    private String sql = "SELECT * from " + " " + InventoryEntry.TABLE2;
    private String selectAr[] = {InventoryContract.InventoryEntry.COLUMN_ID, InventoryContract.InventoryEntry.COLUMN_PRODUCT_IMAGE, InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE, InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER};

    private String UPDATE_ENTRY = "UPDATE " + InventoryEntry.TABLE2 + "set " + InventoryEntry.COLUMN_PRODUCT_QUANTITY + " WHERE " + InventoryEntry.COLUMN_ID + " =?";

    public InventryHelperDb(Context context, String DATABASE_NAME, SQLiteDatabase.CursorFactory factory, int DATABASE_VERSION) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.DATABASE_NAME = DATABASE_NAME;
        this.DATABASE_VERSION = DATABASE_VERSION;

    }


    InventryHelperDb(Context context) {
        super(context, "inventory_database", null, 2);

        this.DATABASE_NAME = DATABASE_NAME;
        this.DATABASE_VERSION = DATABASE_VERSION;
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_ENTRY2);
        Log.e(TAG, "table created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);

        Log.e(TAG, "call");


    }

    public void selectQuery(SQLiteDatabase db) {
        Cursor c = db.rawQuery(sql, selectAr);
        c.moveToNext();
        int id = c.getInt(0);
        byte[] bytes = c.getBlob(1);
        String nm = c.getString(2);
        Double pn = c.getDouble(3);
        int qn = c.getInt(4);
        String sn = c.getString(5);
        String sem = c.getString(6);
        String sph = c.getString(7);


        Log.e(TAG, "" + id + bytes + nm + qn + sn + sem + sph);

    }


    public void updateQuery(SQLiteDatabase db) {
        db.execSQL(UPDATE_ENTRY);

        Log.e(TAG, "updat entries");

    }


}
