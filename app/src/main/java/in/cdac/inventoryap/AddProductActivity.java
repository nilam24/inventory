package in.cdac.inventoryap;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by Dell1 on 27/02/2018.
 */

public class AddProductActivity extends AppCompatActivity {


    TextView textUploadImage, textProductName, textProductPrice, textProductQuantity, textSupplierEmail;
    EditText editProductName, editProductPrice, editProductQuantity, editEmail;
    ImageView imageProduct;
    ContentValues contentValues;
    ContentResolver contentResolver;
    Uri uri;
    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;
    byte[] bytesArray;
    private static String TAG=AddProductActivity.class.getName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textUploadImage = (TextView) findViewById(R.id.text_Image1);
        textProductName = (TextView) findViewById(R.id.text_Name1);
        textProductPrice = (TextView) findViewById(R.id.text_Price1);
        textProductQuantity = (TextView) findViewById(R.id.text_Quantity1);
        textSupplierEmail = (TextView) findViewById(R.id.text_supplierName1);

        editProductName = (EditText) findViewById(R.id.editText_Name1);
        editProductPrice = (EditText) findViewById(R.id.editText_Price1);
        editProductQuantity = (EditText) findViewById(R.id.editText_Quantity1);
        editEmail = (EditText) findViewById(R.id.editEmail12);

        imageProduct = (ImageView) findViewById(R.id.imageView1);

        contentResolver = getContentResolver();
        contentValues = new ContentValues();

        textUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bitmap = uploadImage();
                Log.e(TAG, "******" + bitmap);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        byte[] bytes = bytesArray;

        int item_id = item.getItemId();

        if (item_id == R.id.save) {

            saveInventory(bytes);

        }

        return super.onOptionsItemSelected(item);

    }


    protected Bitmap uploadImage() {

        Intent inImageUpload = new Intent(Intent.ACTION_GET_CONTENT);
        inImageUpload.setType("image/*");
        startActivityForResult(Intent.createChooser(inImageUpload, ""), PICK_IMAGE_REQUEST);

        Toast.makeText(this, "pick image", Toast.LENGTH_LONG).show();

        return bitmap;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == PICK_IMAGE_REQUEST) && (resultCode == RESULT_OK) && (data != null) && (data.getData() != null)) {
            Uri uri = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream);
                bytesArray = outputStream.toByteArray();

                imageProduct.setImageBitmap(bitmap);
                imageProduct.setMaxWidth(imageProduct.getWidth());
                imageProduct.getMaxWidth();
                imageProduct.setMaxHeight(imageProduct.getHeight());
                imageProduct.getMaxHeight();

            } catch (IOException e) {
                Log.e(TAG, "" + e.getMessage());
            }
        }
    }

    protected void saveInventory(byte[] bytes1) {

        int i = 0;
        int q = 0;
        double pr = 0.0;
        String nameProduct = "";
        String price = "";
        String quantity = "";
        String supplierEmailID = "";
        String semal = "";
        bytes1 = bytesArray;


        try {


            if (bytes1 == null) {

                Toast.makeText(this, " select small size image(in kb) ", Toast.LENGTH_SHORT).show();

                return;

            }


            nameProduct = editProductName.getText().toString().trim();
            if (TextUtils.isEmpty(nameProduct)) {

                Toast.makeText(this, "product name is empty", Toast.LENGTH_LONG).show();

                return;
            }

            price = editProductPrice.getText().toString().trim();

            if (TextUtils.isEmpty(price)) {

                Toast.makeText(this, "product price is empty", Toast.LENGTH_LONG).show();

                return;


            } else {


                pr = Double.parseDouble(price);

            }

            quantity = editProductQuantity.getText().toString().trim();

            if (TextUtils.isEmpty(quantity)) {

                Toast.makeText(this, "product quantity is empty", Toast.LENGTH_LONG).show();

                return;

            } else {

                q = Integer.parseInt(quantity);

            }


            supplierEmailID = editEmail.getText().toString().trim();

            if (TextUtils.isEmpty(supplierEmailID)) {

                Toast.makeText(this, "supplier email id is empty or invalid ", Toast.LENGTH_LONG).show();

            } else if (isValidateEmail(supplierEmailID)) {

                semal = supplierEmailID;
            } else {

                Toast.makeText(this, "supplier email id is invalid ", Toast.LENGTH_LONG).show();

            }


            if ((TextUtils.isEmpty(nameProduct) && (TextUtils.isEmpty(price)) && (TextUtils.isEmpty(quantity)) && (TextUtils.isEmpty(supplierEmailID)) && (bytes1 == null))) {

                Toast.makeText(this, "fields should not be empty", Toast.LENGTH_LONG).show();

                return;

            }

        } catch (NumberFormatException n) {

            Log.e(TAG, "" + n.getMessage());

        } catch (NullPointerException e) {

            Log.e(TAG, "" + e.getMessage());

        } finally {

            Log.e(TAG+"finally..", "exception");

        }


        try {

            contentValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_IMAGE, bytes1);
            contentValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, nameProduct);
            contentValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE, pr);
            contentValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, q);
            contentValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_SUPPLIER, semal);

            contentValues.putAll(contentValues);

            Log.e(TAG, "data save  " + bytes1 + nameProduct + pr + q + supplierEmailID);

            uri = InventoryContract.InventoryEntry.COTENT_URI_TAB;

            contentResolver.insert(uri, contentValues);

            Toast.makeText(AddProductActivity.this, "product information saved" + contentValues, Toast.LENGTH_LONG).show();


        } catch (NullPointerException e) {

            Log.e(TAG, "class..." + e.getMessage());

        }

    }


    protected static boolean isValidateEmail(String s) {
        boolean b = false;
        b = (Patterns.EMAIL_ADDRESS.matcher(s).matches());

        return b;

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        imageProduct.setImageBitmap(bitmap);
        editProductName.getText();
        editProductPrice.getText();
        editProductQuantity.getText();
        editEmail.getText();


    }

    @Override
    protected void onPause() {
        super.onPause();

        imageProduct.setImageBitmap(bitmap);
        editProductName.getText();
        editProductPrice.getText();
        editProductQuantity.getText();
        editEmail.getText();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        if (bitmap != null) {
            outState.putParcelable("img", bitmap);
        }
        imageProduct.setImageBitmap(bitmap);
        outState.putString("nameProduct", editProductName.getText().toString());
        outState.putString("price", editProductPrice.getText().toString().trim());
        outState.putString("quantity", editProductQuantity.getText().toString().trim());
        outState.putString("supplierEmail", editEmail.getText().toString().trim());
        outState.putParcelable("values", contentValues);

    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {


        contentValues.putAll(contentValues);

        return super.onRetainCustomNonConfigurationInstance();
    }


}
