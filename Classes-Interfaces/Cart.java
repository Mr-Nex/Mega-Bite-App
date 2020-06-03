package generator.nex.rexx.userregistration;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import generator.nex.rexx.userregistration.Common.Common;
import generator.nex.rexx.userregistration.Database.Database;
import generator.nex.rexx.userregistration.Model.Order;
import generator.nex.rexx.userregistration.Model.Request;

import com.example.rexx.userregistration.R;

import generator.nex.rexx.userregistration.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    public TextView textTotalPrice;
    Button btnPlaceOrder;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Database
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //Init
        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        btnPlaceOrder = (Button)findViewById(R.id.btnPlaceOrder);
        textTotalPrice = (TextView)findViewById(R.id.total);

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                showAlertDialog();
            }
        });

        loadFoodList();

    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("Final Step");
        alertDialog.setMessage("Enter Your Address: ");

        final EditText editAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editAddress.setLayoutParams(lp);
        alertDialog.setView(editAddress); //to add edittext to alertDialog...
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //create a new Request
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        editAddress.getText().toString(),
                        textTotalPrice.getText().toString(),
                        cart
                );
                //Submitting data to Firebase
                //Using System.currentMilli to key...
                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);
                //Empty Cart after confirming order...
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Thank You, Your order has been placed", Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();

    }

    private void loadFoodList() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        //Calculate total price
        int total = 0;
        for(Order order:cart)
            total += (Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("en", "IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        textTotalPrice.setText(fmt.format(total));
    }
}
