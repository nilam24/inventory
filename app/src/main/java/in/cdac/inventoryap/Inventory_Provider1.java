package in.cdac.inventoryap;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import in.cdac.inventoryap.InventoryContract.InventoryEntry;

/**
 * Created by Dell1 on 13/03/2018.
 */

public class Inventory_Provider1 extends ContentProvider {


    InventryHelperDb inventoryHelper;
    SQLiteDatabase sqLiteDatabaseRead;
    SQLiteDatabase sqLiteDatabaseWrite;
    static final int INVENTORY = 100, INVENTORY_ID = 101;
    String projection[] = {InventoryEntry.COLUMN_ID, InventoryEntry.COLUMN_PRODUCT_IMAGE, InventoryEntry.COLUMN_PRODUCT_NAME, InventoryEntry.COLUMN_PRODUCT_PRICE, InventoryEntry.COLUMN_PRODUCT_QUANTITY, InventoryEntry.COLUMN_PRODUCT_SUPPLIER};

    private String TAG = Inventory_Provider1.class.getName();
    Cursor cursor = null;
    Uri uri;
    ContentResolver contentResolver;

    protected static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        matcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryEntry.PATH2, INVENTORY);

        matcher.addURI(InventoryContract.CONTENT_AUTHORITY, "inventry_tab/#", INVENTORY_ID);

    }


    @Override
    public boolean onCreate() {


        inventoryHelper = new InventryHelperDb(getContext());
        sqLiteDatabaseRead = inventoryHelper.getReadableDatabase();
        sqLiteDatabaseWrite = inventoryHelper.getWritableDatabase();

        contentResolver = getContext().getContentResolver();

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {


        int match = matcher.match(uri);

        Log.e(TAG, "provider" + match);


        switch (match) {

            case INVENTORY:

                cursor = sqLiteDatabaseRead.query(InventoryEntry.TABLE2, projection, null, null, null, null, null);

                Log.e(TAG, "cursor ==" + cursor);

                break;

            case INVENTORY_ID:

                cursor = detailViewQuery(uri, projection, selection, selectionArgs);

                break;

            default:

                throw new IllegalArgumentException("cannt query unknown uri " + uri);

        }

        cursor.setNotificationUri(contentResolver, uri);


        return cursor;
    }

    protected Cursor detailViewQuery(Uri uri, String[] strings, String s, String[] s1) {
        String selection1 = InventoryContract.InventoryEntry.COLUMN_ID + "=?";
        String selectionArg1[] = new String[]{String.valueOf(ContentUris.parseId(uri))};

        cursor = sqLiteDatabaseRead.query(InventoryEntry.TABLE2, projection, selection1, selectionArg1, null, null, null);

        Log.e(TAG, "******" + cursor);


        cursor.setNotificationUri(contentResolver, uri);

        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        int match = matcher.match(uri);

        switch (match) {
            case INVENTORY:

                return InventoryEntry.CONTENT_LIST_TYPE2;

            default:

                throw new IllegalArgumentException("Exception @@@@@");

        }


    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {


        int match = matcher.match(uri);

        Log.e("", "" + match);

        switch (match) {
            case INVENTORY:

                Log.e("provider", "insert " + uri);

                InsertInventory(uri, contentValues);

                return uri;

            default:

                throw new IllegalArgumentException("exception ....**" + uri);

        }


    }


    protected Uri InsertInventory(Uri uri, ContentValues Values) {

        long product_id = sqLiteDatabaseWrite.insert(InventoryEntry.TABLE2, null, Values);


        Log.e(TAG, "" + Values + " , " + product_id);

        if (product_id == -1) {
            return null;
        }

        if (product_id != -1) {
            return uri;

        }

        Log.e("", "" + uri);

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, product_id);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int match = matcher.match(uri);

        selection = InventoryEntry.COLUMN_ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

        int succes;

        switch (match) {

            case INVENTORY_ID:

                succes = deleteInventoryItems(uri, selection, selectionArgs);

                break;

            default:
                throw new IllegalArgumentException("delete query is invalid");

        }

        return succes;

    }


    protected int deleteInventoryItems(Uri uri, String selection, String[] selectionArg) {

        int count = sqLiteDatabaseRead.delete(InventoryEntry.TABLE2, selection, selectionArg);

        Log.e("provider call", "deletion ");
        if (count != -0) {

            Log.e("provider call", "deletion success ");

            return count;
        }

        contentResolver.notifyChange(uri, null);
        return count;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {


        int match = matcher.match(uri);
        int s = 0;
        switch (match) {
            case INVENTORY_ID:

                s = UpdateInventory(uri, values, selection, selectionArgs);

                return s;

            default:

                throw new IllegalArgumentException("invalid query for updation");

        }

    }


    protected int UpdateInventory(Uri uri, ContentValues contentValues, String selection1, String[] selectionArg2) {

        int updt = sqLiteDatabaseWrite.update(InventoryEntry.TABLE2, contentValues, selection1, selectionArg2);


        if (updt == 1) {
            return updt;
        }

        return updt;
    }


}
