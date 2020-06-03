package generator.nex.rexx.userregistration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.andremion.counterfab.CounterFab;
import generator.nex.rexx.userregistration.Common.Common;
import generator.nex.rexx.userregistration.Database.Database;
import generator.nex.rexx.userregistration.Interface.ItemClickListener;
import generator.nex.rexx.userregistration.Model.Category;

import com.example.rexx.userregistration.R;

import generator.nex.rexx.userregistration.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference category;

    TextView txtFullName;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    CounterFab fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        fab = (CounterFab) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartintent = new Intent(Home.this, Cart.class);
                startActivity(cartintent);
            }
        });

        fab.setCount(new Database(this).getCountCart());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set Name for User
        View headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView)headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getName());

        //Load Menu
        recycler_menu = (RecyclerView)findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();
    }

    //ctrl+o
    @Override
    protected void onResume() {
        super.onResume();
        fab.setCount(new Database(this).getCountCart());
    }

    private void loadMenu() {

        //Process Dialog
        final ProgressDialog mDialog = new ProgressDialog(Home.this);
        mDialog.setMessage("Please wait...");
        mDialog.show();

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class, R.layout.menu_item, MenuViewHolder.class, category) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                viewHolder.menu_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.menu_image);
                mDialog.dismiss();

                final Category clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        // Get categoryId and send it to new activity
                        Intent foodlist = new Intent(Home.this, FoodList.class);
                        // we just have to get the key of item i.e., CategoryId...
                        foodlist.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(foodlist);
                    }
                });
            }
        };
        recycler_menu.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {

        } else if (id == R.id.nav_cart) {

            Intent cartIntent = new Intent(Home.this, Cart.class);
            startActivity(cartIntent);

        } else if (id == R.id.nav_orders) {

            Intent orderIntent = new Intent(Home.this, OrderStatus.class);
            startActivity(orderIntent);

        } else if (id == R.id.nav_log_out) {
            //LogOut
            Intent logout = new Intent(Home.this, SignIn.class);
            logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logout);

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
