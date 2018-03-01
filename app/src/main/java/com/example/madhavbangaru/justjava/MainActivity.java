package com.example.madhavbangaru.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

import static java.util.logging.Level.parse;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    int numberOfCoffees = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        CheckBox wippedcream = (CheckBox) findViewById(R.id.whipped_cream_checkBox);
        CheckBox chocolate = (CheckBox) findViewById(R.id.chocolate_checkBox);
        boolean haswippedcream = wippedcream.isChecked();
        boolean hasChocolate = chocolate.isChecked();

        EditText userName = (EditText) findViewById(R.id.editText);
        String name = userName.getText().toString();

        display(numberOfCoffees);
        displayPrice(numberOfCoffees * 5);
        displayMessage(createorderSummary(name, numberOfCoffees, haswippedcream, hasChocolate));
        calculatePrice(haswippedcream, hasChocolate);

        String priceMessage = createorderSummary(name, numberOfCoffees, haswippedcream, hasChocolate);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.javamail) + name);
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public int calculatePrice(boolean addwippedcream, boolean addchocolate) {
        int pricePerCup = 5;
        int price = numberOfCoffees * pricePerCup;

        if (addwippedcream) {
            price = numberOfCoffees * pricePerCup + 1 * numberOfCoffees;
        }
        if (addchocolate) {
            price = numberOfCoffees * pricePerCup + 2 * numberOfCoffees;
        }
        return price;
    }

    public String createorderSummary(String n, int i, boolean haswippedcream, boolean hasChocolate) {
        String message = "" + getString(R.string.order_summary_name, n);
        message = message + "\n" + getString(R.string.add_wipped_cream, haswippedcream);
        message = message + "\n" + getString(R.string.add_chocolate, hasChocolate);
        message = message + "\n" + getString(R.string.order_summary_quantity, i);
        message = message + "\n" + getString(R.string.order_summary_price, NumberFormat.getCurrencyInstance().format(calculatePrice(haswippedcream, hasChocolate))) + "\n" + getString(R.string.thank_you);
        return message;
    }

    public void decrement(View view) {
        numberOfCoffees -= 1;
        if (numberOfCoffees < 1) {
            Toast toast = Toast.makeText(this, getString(R.string.toast1), Toast.LENGTH_SHORT);
            toast.show();
            numberOfCoffees = 1;
//            display(1);
            return;
        } else {
            display(numberOfCoffees);
            displayPrice(numberOfCoffees * 5);
        }
    }

    public void increment(View view) {
        numberOfCoffees += 1;
        if (numberOfCoffees > 50) {
            Toast toast = Toast.makeText(this, getString(R.string.toast2), Toast.LENGTH_SHORT);
            toast.show();
            display(50);
            numberOfCoffees = 50;
        } else {
            display(numberOfCoffees);
            displayPrice(numberOfCoffees * 5);
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int num) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + num);
    }

    /**
     * This method displays the given price on the screen.
     */
    private void displayPrice(int number) {
        TextView priceTextView = (TextView) findViewById(R.id.order_summary_text_view);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }
}