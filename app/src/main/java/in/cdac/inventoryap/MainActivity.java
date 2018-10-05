package in.cdac.inventoryap;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import in.cdac.inventoryap.InventoryContract.InventoryEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static String TAG = MainActivity.class.getName();
    ContentResolver contentResolver;
    TextView queryData;
    FloatingActionButton floatingActionAddNewProduct;
    ListView listView;
    AdapterInventory adapter;
    Cursor cursor1 = null;
    private int INVENTORY_LOADER = 1;
    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_list_layout);

        contentResolver = getContentResolver();
        floatingActionAddNewProduct = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        queryData = (TextView) findViewById(R.id.textView6);
        listView = (ListView) findViewById(R.id.listInventory);
        listView.setEmptyView(queryData);

        uri = InventoryEntry.COTENT_URI_TAB;


        floatingActionAddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(MainActivity.this, AddProductActivity.class);
                startActivity(in);

            }
        });


        adapter = new AdapterInventory(MainActivity.this, cursor1, 0);

        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        Log.e(TAG, "adapter value--" + adapter);
        getSupportLoaderManager().initLoader(INVENTORY_LOADER, null, this);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                adapterView.getItemAtPosition(position);

                id = adapterView.getItemIdAtPosition(position);
                Uri muri = ContentUris.withAppendedId(InventoryEntry.COTENT_URI_TAB, id);
                Intent in = new Intent(MainActivity.this, DetailActivity.class);
                in.setData(muri);
                startActivity(in);


            }
        });


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        String projection[] = {InventoryEntry.COLUMN_ID, InventoryEntry.COLUMN_PRODUCT_IMAGE, InventoryEntry.COLUMN_PRODUCT_NAME, InventoryEntry.COLUMN_PRODUCT_PRICE, InventoryEntry.COLUMN_PRODUCT_QUANTITY, InventoryEntry.COLUMN_PRODUCT_SUPPLIER};

        return new CursorLoader(this, uri, projection, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        cursor1 = data;

        adapter.swapCursor(cursor1);

        Log.e(TAG, "" + adapter);
        Log.e(TAG, "" + cursor1);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        adapter.swapCursor(null);

    }

    @Override
    protected void onStart() {
        super.onStart();


    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putString("uri", uri + "");


    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {

        uri = InventoryEntry.COTENT_URI_TAB;

        return super.onRetainCustomNonConfigurationInstance();
    }

}
