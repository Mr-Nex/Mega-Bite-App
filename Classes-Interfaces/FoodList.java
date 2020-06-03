package generator.nex.rexx.userregistration;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import generator.nex.rexx.userregistration.Database.Database;
import generator.nex.rexx.userregistration.Interface.ItemClickListener;
import generator.nex.rexx.userregistration.Model.Food;
import generator.nex.rexx.userregistration.Model.Order;

import com.example.rexx.userregistration.R;

import generator.nex.rexx.userregistration.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

    String categoryId = "";

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    //Search Function
    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> suggestions = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    //Favorites
    Database localDB;


    private void loadListFood(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item, FoodViewHolder.class, foodList.orderByChild("MenuId").equalTo(categoryId)){
            @Override
            protected void populateViewHolder(final FoodViewHolder viewHolder, final Food model, final int position) {
                viewHolder.food_Name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.food_Image);

                //quick cart
                viewHolder.btn_quick_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Database(getBaseContext()).addToCart(new Order(
                                adapter.getRef(position).getKey(),
                                model.getName(),
                                "1",
                                model.getPrice(),
                                model.getDiscount()
                        ));
                        Toast.makeText(FoodList.this, "Added To Cart", Toast.LENGTH_SHORT).show();
                    }
                });

                //Add Favorites
                if(localDB.isFavorite(adapter.getRef(position).getKey()))
                    viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                //click to change state of favorite
                viewHolder.fav_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!localDB.isFavorite(adapter.getRef(position).getKey())){
                            localDB.addToFavorites(adapter.getRef(position).getKey());
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(FoodList.this, ""+model.getName()+" is added to Favorites", Toast.LENGTH_SHORT).show();
                        }else{
                            localDB.removeFromFavorites(adapter.getRef(position).getKey());
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            Toast.makeText(FoodList.this, ""+model.getName()+" is removed from Favorites", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //start new Activity
                        Intent foodDetails = new Intent(FoodList.this, FoodDetails.class);
                        foodDetails.putExtra("FoodId", adapter.getRef(position).getKey());  //send foodId to new activity
                        startActivity(foodDetails);
                    }
                });
            }
        };
        //set adapter
        recyclerView.setAdapter(adapter);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");

        //Local DB
        localDB = new Database(this);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_food);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Get intent here...
        if(getIntent()!= null){
            categoryId = getIntent().getStringExtra("CategoryId");
        }
        if (!categoryId.isEmpty() && categoryId != null){
            loadListFood(categoryId);
        }

        //Search
        materialSearchBar = (MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter Your Choice");
        //materialSearchBar.setSpeechMode(false);
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestions);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //When user inputs the choice, suggestions get changed...

                List<String> suggest = new ArrayList<String>();
                for (String search:suggestions){
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //Restore original adapter when search bar is closed...
                if(!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //When search is finished...
                //show result of search adapter
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    //--------Grids----------------------------

    public  class GridSpacingItemDecoration extends RecyclerView.ItemDecoration{
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    //--------search box-------------------

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("Name").equalTo(text.toString()) //compare name
        ) {
            @Override
            protected void populateViewHolder(final FoodViewHolder viewHolder, final Food model, final int position) {
                viewHolder.food_Name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.food_Image);
                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //start new Activity
                        Intent foodDetails = new Intent(FoodList.this, FoodDetails.class);
                        foodDetails.putExtra("FoodId", searchAdapter.getRef(position).getKey());  //send foodId to new activity
                        startActivity(foodDetails);
                    }
                });
            }
        };
        recyclerView.setAdapter(searchAdapter); //set adapter for search
    }

    private void loadSuggest() {
        foodList.orderByChild("MenuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                        {
                            Food item = postSnapshot.getValue(Food.class);
                            suggestions.add(item.getName());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}
