package in.cdac.inventoryap;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import in.cdac.inventoryap.InventoryContract.InventoryEntry;

/**
 * Created by Dell1 on 15/03/2018.
 */

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    TextView nameText, priceText, valueText, quantityText, emailText;
    Button btnPlus, btnMinus;
    ImageView imgView;
    Uri uri;
    private int INVENTORY_LOADER = 1;
    ContentResolver contentResolver;
    ContentValues contentValues;
    String selection;
    String[] selectionArg;
    private int success = 0;

    Cursor cursor1;
    private int quant;
    private String q;
    protected static String TAG = DetailActivity.class.getName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);


        Intent in = getIntent();
        uri = in.getData();

        Log.e(TAG, "uri value:" + uri);


        nameText = (TextView) findViewById(R.id.productName1);
        priceText = (TextView) findViewById(R.id.price1);
        valueText = (TextView) findViewById(R.id.price_value1);
        quantityText = (TextView) findViewById(R.id.valueQuantity1);
        emailText = (TextView) findViewById(R.id.textEmailId1);
        btnPlus = (Button) findViewById(R.id.plusmodify);
        btnMinus = (Button) findViewById(R.id.minumodify);
        imgView = (ImageView) findViewById(R.id.imageView12);


        if (uri != null) {
            setTitle("Product Detail..");
        } else {
            setTitle("go back");
        }


        contentResolver = getContentResolver();
        contentValues = new ContentValues();


        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri mu = uri;
                long id = ContentUris.parseId(mu);
                selection = InventoryEntry.COLUMN_ID + "=?";
                selectionArg = new String[]{String.valueOf(id)};


                try {
                    q = quantityText.getText().toString().trim();

                    if (q == null) {
                        return;
                    } else {
                        quant = Integer.parseInt(q);

                        if (quant >= 0) {
                            quant = quant + 1;

                            contentValues.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, quant);
                            if (contentValues != null) {

                                UpdatQuantity(mu, contentValues, selection, selectionArg);

                            }
                            quantityText.setText(String.valueOf(quant));

                        }
                    }
                } catch (NumberFormatException ne) {
                    Log.e(TAG, "" + ne.getMessage());
                }


            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Uri muri = uri;
                long id1 = ContentUris.parseId(muri);
                selection = InventoryEntry.COLUMN_ID + "=?";
                selectionArg = new String[]{String.valueOf(id1)};

                try {
                    q = quantityText.getText().toString().trim();


                    if (q == null) {
                        return;
                    } else {
                        quant = Integer.parseInt(q);


                        if (quant > 0) {
                            quant = quant - 1;
                            contentValues.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, quant);
                            if (contentValues != null) {
                                UpdatQuantity(muri, contentValues, selection, selectionArg);
                            }
                            quantityText.setText(String.valueOf(quant));


                        } else {
                            quant = 0;

                            contentValues.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, quant);
                            if (contentValues != null) {

                                UpdatQuantity(muri, contentValues, selection, selectionArg);
                            }

                            quantityText.setText(String.valueOf(quant));

                        }
                    }

                } catch (NumberFormatException en) {
                    Log.e(TAG, "" + en.getMessage());
                }
            }
        });


        getSupportLoaderManager().initLoader(INVENTORY_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri muri = uri;

        String projection[] = {InventoryEntry.COLUMN_ID, InventoryEntry.COLUMN_PRODUCT_IMAGE, InventoryEntry.COLUMN_PRODUCT_NAME, InventoryEntry.COLUMN_PRODUCT_PRICE, InventoryEntry.COLUMN_PRODUCT_QUANTITY, InventoryEntry.COLUMN_PRODUCT_SUPPLIER};

        String selection = InventoryEntry.COLUMN_ID + "=?";
        String selectionArg[] = new String[]{String.valueOf(ContentUris.parseId(muri))};

        return new CursorLoader(this, muri, projection, selection, selectionArg, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        cursor1 = data;
        Bitmap bitmap;
        cursor1.moveToFirst();

        int id = cursor1.getInt(cursor1.getColumnIndex(InventoryEntry.COLUMN_ID));
        Log.e(TAG, "id_value >>>>>>" + id);

        byte[] bytesImage = cursor1.getBlob(cursor1.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_IMAGE));

        InputStream inputStream1 = new ByteArrayInputStream(bytesImage);
        bitmap = BitmapFactory.decodeStream(inputStream1);
        imgView.setImageBitmap(bitmap);
        Log.e(TAG, "byte-value >>>>>" + bytesImage);

        String nm = cursor1.getString(cursor1.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME));
        Log.e(TAG, "nm-value>>>>>" + nm);

        nameText.setText(nm);

        Double pris = cursor1.getDouble(cursor1.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_PRICE));
        Log.e(TAG, "price_value >>>>" + pris);
        valueText.setText(String.valueOf(pris));
        int quant = cursor1.getInt(cursor1.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_QUANTITY));
        Log.e(TAG, "quant_value >>>>" + quant);
        quantityText.setText(String.valueOf(quant));
        String eml = cursor1.getString(cursor1.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_SUPPLIER));
        Log.e(TAG, "email_value >>>>" + eml);
        emailText.setText(eml);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        imgView.setImageURI(null);
        nameText.setText("");
        priceText.setText("");
        valueText.setText("");
        valueText.setText("");
        quantityText.setText("");
        emailText.setText("");

    }


    protected int UpdatQuantity(Uri uri, ContentValues contentValues, String s, String[] s2) {

        success = contentResolver.update(uri, contentValues, s, s2);

        if (success == 1) {
            Toast.makeText(DetailActivity.this, "quantity is updated", Toast.LENGTH_LONG).show();

            return success;
        }

        return success;

    }


    private void deltionConfirmation(Uri uriDeletion, String selection1, String[] selectionArg2) {

        int deletionSuccess = contentResolver.delete(uriDeletion, selection1, selectionArg2);
        if (deletionSuccess > 0) {

            imgView.setImageBitmap(null);
            nameText.setText("");
            valueText.setText("");
            quantityText.setText("");
            emailText.setText("");
            Intent intentBacktoMain = new Intent(DetailActivity.this, MainActivity.class);
            startActivity(intentBacktoMain);

            Toast.makeText(this, " data deleted", Toast.LENGTH_LONG).show();

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String p = "required";
        String infoP = nameText.getText().toString().trim();
        String infoQ = quantityText.getText().toString().trim();
        String infoPQ = infoP + "," + infoQ + " " + p;
        String em = emailText.getText().toString().trim();

        int id = item.getItemId();

        switch (id) {
            case R.id.order1:

                Intent inEmailUri = new Intent(Intent.ACTION_SENDTO);
                inEmailUri.setType("text/plain");
                inEmailUri.setData(Uri.parse("mailto:" + em));

                inEmailUri.putExtra(Intent.EXTRA_SUBJECT, infoP);
                inEmailUri.putExtra(Intent.EXTRA_TEXT, infoPQ);
                startActivity(Intent.createChooser(inEmailUri, p));

                break;

            case R.id.delete1:

                final Uri uriDeletion = uri;
                final String selection1 = InventoryEntry.COLUMN_ID + "=?";
                final String[] selectionArg2 = {String.valueOf(ContentUris.parseId(uriDeletion))};


                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setIcon(R.drawable.ic_launcher_foreground);
                builder.setMessage(R.string.message);
                builder.setTitle(R.string.title);
                builder.setPositiveButton(R.string.dialog_positive_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deltionConfirmation(uriDeletion, selection1, selectionArg2);

                    }
                });

                builder.setNegativeButton(R.string.dialog_negative_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();


            default:

                Toast.makeText(this, "select options", Toast.LENGTH_LONG).show();

        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("uri", uri.toString());

    }

    @Override
    protected void onResume() {
        super.onResume();

        btnPlus.setVisibility(View.VISIBLE);
        btnMinus.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {

        uri = getIntent().getData();

        return super.onRetainCustomNonConfigurationInstance();
    }
}


