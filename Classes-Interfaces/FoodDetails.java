package generator.nex.rexx.userregistration;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import generator.nex.rexx.userregistration.Database.Database;
import generator.nex.rexx.userregistration.Model.Food;
import generator.nex.rexx.userregistration.Model.Order;

import com.example.rexx.userregistration.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetails extends AppCompatActivity {

    TextView food_name, food_price, food_description;
    ImageView img_food;
    CollapsingToolbarLayout collapsingToolbarLayout;
    CounterFab btnCart;
    ElegantNumberButton elegantNumberButton;

    String foodId="";

    FirebaseDatabase database;
    DatabaseReference foodDatabase;

    Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        Intent foodDetails = getIntent();

        //Firebase
        database = FirebaseDatabase.getInstance();
        foodDatabase = database.getReference("Food");

        //Init View
        elegantNumberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (CounterFab) findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(FoodDetails.this).addToCart(new Order(
                        foodId,
                        currentFood.getName(),
                        elegantNumberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()
                ));
                Toast.makeText(FoodDetails.this, "Added To Cart", Toast.LENGTH_SHORT).show();
            }
        });

        btnCart.setCount(new Database(this).getCountCart());

        food_description = (TextView)findViewById(R.id.food_description);
        food_name = (TextView)findViewById(R.id.name_of_food);
        food_price = (TextView)findViewById(R.id.price_of_food);
        img_food = (ImageView)findViewById(R.id.img_food);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppbar);

        //get FoodId from intent
        if (getIntent() != null) {
            foodId = getIntent().getStringExtra("FoodId");
        }
        if (!foodId.isEmpty() && foodId != null){
            getFoodDetails(foodId);
        }
    }

    private void getFoodDetails(final String foodId) {
        foodDatabase.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);

                //Set Image
                Picasso.with(getBaseContext()).load(currentFood.getImage())
                        .into(img_food);

                collapsingToolbarLayout.setTitle(currentFood.getName());

                food_price.setText(currentFood.getPrice());

                food_name.setText(currentFood.getName());

                food_description.setText(currentFood.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
