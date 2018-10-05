package in.cdac.inventoryap;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Dell1 on 23/02/2018.
 */

public class InventoryContract {


    protected static String BASE_URI = "content://";
    protected static String CONTENT_AUTHORITY = "in.cdac.inventoryap";
    protected static Uri BASE_CONTENT_URI = Uri.parse(BASE_URI + CONTENT_AUTHORITY);


    public InventoryContract() {
    }

    protected static class InventoryEntry implements BaseColumns {

        static String TABLE2 = "inventry_tab";
        static String COLUMN_ID = BaseColumns._ID;
        static String COLUMN_PRODUCT_NAME = "product_name";
        static String COLUMN_PRODUCT_PRICE = "price";
        static String COLUMN_PRODUCT_QUANTITY = "quantity";
        static String COLUMN_PRODUCT_IMAGE = "image";
        static String COLUMN_PRODUCT_SUPPLIER = "supplier";


        protected static String PATH2 = InventoryEntry.TABLE2;

        protected static Uri COTENT_URI_TAB = Uri.withAppendedPath(BASE_CONTENT_URI, PATH2);

        protected static String CONTENT_LIST_TYPE2 = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH2;


    }

}
