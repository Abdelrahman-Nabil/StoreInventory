package com.example.abdu.bookstoreinventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abdu.bookstoreinventory.BookContract.BookEntry;


public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    public static void decreaseQuantity(int pos, Context context) {
        Uri currentUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, pos);
        ContentValues values = new ContentValues();
        values.put("decrease", 0);
        context.getContentResolver().update(currentUri, values, null, null);

    }

    public static void increaseQuantity(int pos, Context context) {
        Uri currentUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, pos);
        ContentValues values = new ContentValues();
        values.put("increase", 1);
        context.getContentResolver().update(currentUri, values, null, null);


    }

    public static void deleteItem(int pos, Context context) {
        Uri currentUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, pos);

        context.getContentResolver().delete(currentUri, null, null);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.name);
        TextView summaryTextView = view.findViewById(R.id.price);
        final TextView quantityTextView = view.findViewById(R.id.quantity);

        int _id = cursor.getColumnIndex(BookEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
        int PriceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);

        String BookName = cursor.getString(nameColumnIndex);
        String BookPrice = cursor.getString(PriceColumnIndex);
        int BookQuantity = cursor.getInt(quantityColumnIndex);
        int _ID = cursor.getInt(_id);

        if (TextUtils.isEmpty(BookPrice)) {
            BookPrice = context.getString(R.string.unknown_price);
        }


        nameTextView.setText(BookName);
        summaryTextView.setText(BookPrice);

        quantityTextView.setText(BookQuantity + "");

        Button editButton = view.findViewById(R.id.Edit);
        editButton.setTag(_ID);
        Button saleButton = view.findViewById(R.id.sale);
        saleButton.setTag(_ID);


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditActivity.class);

                Uri currentUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, (int) view.getTag());


                intent.setData(currentUri);


                context.startActivity(intent);
            }
        });

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantity = quantityTextView.getText().toString();
                int amount = Integer.parseInt(quantity);
                if (amount <= 0) {
                    Toast.makeText(context, R.string.no_negative, Toast.LENGTH_SHORT).show();
                    return;
                }
                decreaseQuantity((int) view.getTag(), context);
            }
        });
        Button detailsButton = view.findViewById(R.id.details);
        ContentValues details = new ContentValues();
        details.put(BookEntry._ID, cursor.getString(cursor.getColumnIndex(BookEntry._ID)));
        details.put(BookEntry.COLUMN_BOOK_NAME, cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME)));
        details.put(BookEntry.COLUMN_BOOK_QUANTITY, cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY)));
        details.put(BookEntry.COLUMN_BOOK_PRICE, cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE)));
        details.put(BookEntry.COLUMN_BOOK_SUPPLIER, cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER)));
        details.put(BookEntry.COLUMN_SUPPLIER_NUMBER, cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NUMBER)));
        detailsButton.setTag(details);

        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);

                ContentValues cV = (ContentValues) view.getTag();
                intent.putExtra(BookEntry._ID, cV.getAsInteger(BookEntry._ID));
                intent.putExtra(BookEntry.COLUMN_BOOK_NAME, cV.getAsString(BookEntry.COLUMN_BOOK_NAME));
                intent.putExtra(BookEntry.COLUMN_BOOK_PRICE, "Price:" + cV.getAsString(BookEntry.COLUMN_BOOK_PRICE));
                intent.putExtra(BookEntry.COLUMN_BOOK_QUANTITY, cV.getAsInteger(BookEntry.COLUMN_BOOK_QUANTITY));
                intent.putExtra(BookEntry.COLUMN_BOOK_SUPPLIER, "Supplier: " + cV.getAsString(BookEntry.COLUMN_BOOK_SUPPLIER));
                intent.putExtra(BookEntry.COLUMN_SUPPLIER_NUMBER, "Supplier Number: " + cV.getAsString(BookEntry.COLUMN_SUPPLIER_NUMBER));
                context.startActivity(intent);
            }
        });


    }
}
