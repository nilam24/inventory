package in.cdac.inventoryap;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;


/**
 * Created by Dell1 on 05/03/2018.
 */

public class AdapterInventory extends CursorAdapter {

    Context context;
    Cursor c;
    ContentResolver contentResolver;
    ContentValues contentValues;
    Uri uri;
    String selection;
    String selectionArg[];

    private int qn = 0, i = 1, quant = 0, id;
    int flags;
    private static String TAG = Adapter.class.getName();

    public AdapterInventory(Context context, Cursor c, int flags) {
        super(context, c, flags);

        this.context = context;
        this.c = c;
        this.flags = flags;
        contentResolver = context.getContentResolver();
        contentValues = new ContentValues();


    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {


        context = viewGroup.getContext();

        return LayoutInflater.from(context).inflate(R.layout.inventory_item_layout, viewGroup, false);


    }


    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {


        ImageView img;
        final View itemView = view;
        final TextView text_product_name, text_product_price, text_product_price2,  textViewq2;
        Button sale_button;
        Bitmap bitmap = null;


        text_product_name = (TextView) itemView.findViewById(R.id.textProductNm);
        text_product_price = (TextView) itemView.findViewById(R.id.textProductPr);
        text_product_price2 = (TextView) itemView.findViewById(R.id.textQ);
        textViewq2 = (TextView) itemView.findViewById(R.id.textq2);
        img = (ImageView) itemView.findViewById(R.id.imageviewId);
        sale_button = (Button) itemView.findViewById(R.id.btnSal);


        try {

            id = cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_ID));

            byte[] bytesImageFormDatabase = cursor.getBlob(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_IMAGE));

            InputStream inputStream = new ByteArrayInputStream(bytesImageFormDatabase);

            bitmap = BitmapFactory.decodeStream(inputStream);

            img.setImageBitmap(bitmap);

            Log.e(TAG, "" + bytesImageFormDatabase + "-----");

            String n = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME));
            text_product_name.setText(n);
            Double p = cursor.getDouble(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE));

            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            String priceFormated = decimalFormat.format(p);
            text_product_price2.setText(priceFormated);

            qn = cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY));

            final String qa = String.valueOf(qn);

            textViewq2.setText(qa);


            Log.e(TAG, "cursor values---" + n + p + qn);

        } catch (Exception e) {
            e.printStackTrace();
        }

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, DetailActivity.class);
                Uri mu = ContentUris.withAppendedId(InventoryContract.InventoryEntry.COTENT_URI_TAB, id);
                intent.setData(mu);
                Toast.makeText(context, "item click" + intent, Toast.LENGTH_SHORT).show();

                context.startActivity(intent);

            }
        });

        quant = qn;


        sale_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = cursor.getPosition();

                Uri m = ContentUris.withAppendedId(InventoryContract.InventoryEntry.COTENT_URI_TAB, id);

                selection = InventoryContract.InventoryEntry.COLUMN_ID + "=?";
                selectionArg = new String[]{String.valueOf(ContentUris.parseId(m))};

                int r = 0;
                quant = Integer.parseInt(textViewq2.getText().toString().trim());

                try {
                    if (quant > 0) {

                        quant = quant - i;
                        contentValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, quant);

                        if (contentValues != null) {
                            r = contentResolver.update(m, contentValues, selection, selectionArg);
                        }
                        textViewq2.setText(String.valueOf(quant));

                        if (r == 1) {
                            Log.e(TAG, "sccess");
                        }

                    } else {
                        quant = 0;

                        textViewq2.setText(String.valueOf(quant));
                        contentValues.put("quantity", quant);
                        int s = contentResolver.update(m, contentValues, selection, selectionArg);
                        if (s == 1) {
                            Log.e(TAG, "sccess");
                        }
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }


            }
        });


    }


    @Override
    public long getItemId(int position) {

        return super.getItemId(position);

    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);

    }


}

