package com.example.abdu.bookstoreinventory;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abdu.bookstoreinventory.BookContract.BookEntry;

public class DetailsActivity extends AppCompatActivity {
    public Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        intent = getIntent();

        TextView name = findViewById(R.id.name);
        name.setText(intent.getStringExtra(BookEntry.COLUMN_BOOK_NAME));

        TextView price = findViewById(R.id.price);
        price.setText(intent.getStringExtra(BookEntry.COLUMN_BOOK_PRICE));

        final TextView quantity = findViewById(R.id.quantity);
        int quant = intent.getIntExtra(BookEntry.COLUMN_BOOK_QUANTITY, 0);
        quantity.setText(quant + "");
        if (quant > 0) {
            quantity.setTextColor(getResources().getColor(R.color.green));
        } else {
            quantity.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        TextView supplier = findViewById(R.id.supplier);
        supplier.setText(intent.getStringExtra(BookEntry.COLUMN_BOOK_SUPPLIER));

        final TextView supplier_number = findViewById(R.id.supplier_number);

        supplier_number.setText(intent.getStringExtra(BookEntry.COLUMN_SUPPLIER_NUMBER));

        Button saleButton = findViewById(R.id.sale);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String QuantityString = quantity.getText().toString();
                int amount = Integer.parseInt(QuantityString);
                if (amount <= 0) {
                    Toast.makeText(DetailsActivity.this, R.string.no_negative, Toast.LENGTH_SHORT).show();
                    return;
                }
                BookCursorAdapter.decreaseQuantity(getIntent().getIntExtra(BookEntry._ID, 0), getApplicationContext());
                amount--;
                quantity.setText(amount + "");

            }
        });
        Button increaseButton = findViewById(R.id.increase);

        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookCursorAdapter.increaseQuantity(getIntent().getIntExtra(BookEntry._ID, 0), getApplicationContext());
                String QuantityString = quantity.getText().toString();
                int amount = Integer.parseInt(QuantityString);
                amount++;
                quantity.setText(amount + "");
                quantity.setTextColor(getResources().getColor(R.color.green));
            }
        });
        Button deleteButton = findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                BookCursorAdapter.deleteItem(getIntent().getIntExtra(BookEntry._ID, 0), getApplicationContext());
                finish();
            }
        });

        Button orderButton = findViewById(R.id.order);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numberString = supplier_number.getText().toString();
                String[] numberInt = numberString.split(" ");
                String supp_number = numberInt[2];
                Intent orderFromSupplier = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + supp_number));
                startActivity(orderFromSupplier);
            }
        });


    }

}
