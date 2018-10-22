package ba.kalishop.kalishop;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ba.kalishop.kalishop.Model.*;
import ba.kalishop.kalishop.web.ApiClient;
import ba.kalishop.kalishop.web.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<ba.kalishop.kalishop.Model.Product> productList;
    private Button menuButton;
    private ApiInterface apiInterface;
    private List<ba.kalishop.kalishop.Model.Product> lista;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_vieew);

        productList = new ArrayList<>();

        adapter = new ProductAdapter(this, productList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        prepareProducts();
     //start code from menu activity for navigation drawer


       // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      //  fab.setOnClickListener(new View.OnClickListener() {
      //      @Override
      //      public void onClick(View view) {
      //          Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
       //                 .setAction("Action", null).show();


       //     }
      //  });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //try {
         //   Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
       // } catch (Exception e) {
        //   e.printStackTrace();
        //}





    }



    @Override
    public void onBackPressed() {//menu activity
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//menu activity
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        //ovo ispod sad dodala sve to return
        SharedPreferences sp1=getSharedPreferences ("Login",0);

        String unm=sp1.getString("email", null);
        String name=sp1.getString("name",null)+" "+sp1.getString("lastname",null);
        TextView mail=findViewById(R.id.drawerEmail);
        TextView nameD=findViewById(R.id.drawerName);
        mail.setText(unm);
        nameD.setText(name);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//menu activity
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.action_cart){

            Intent i=new Intent(ProductsActivity.this,InCartActivity.class);//ovdje trebam izmijeniti na incart da otvara al puca mi nešto oj a nije pucalo

            startActivity(i);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")//menu activity
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {//menu activity
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent=new Intent(ProductsActivity.this,ProductsActivity.class);

            startActivity(intent);
            item.setChecked(true);
        } else if (id == R.id.nav_gallery) {
            Intent intent=new Intent(ProductsActivity.this,CartHistoryActivity.class);

            startActivity(intent);
            item.setChecked(true);

        } else if (id == R.id.nav_slideshow) {


            Intent intent=new Intent(ProductsActivity.this,ProfileActivity.class);
            startActivity(intent);
            item.setChecked(true);

        } else if (id == R.id.nav_manage) {
            Intent intent=new Intent(ProductsActivity.this,LoginActivity.class);
            startActivity(intent);
            item.setChecked(true);

        } //else if (id == R.id.nav_share) {

       // } else if (id == R.id.nav_send) {

       // }

       DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }




    private void prepareProducts() {
        final int[] covers = new int[]{
                R.drawable.album1,
                R.drawable.album2,
                R.drawable.album3,
                R.drawable.album4,
                R.drawable.album5,
                R.drawable.album6,
                R.drawable.album7,
                R.drawable.album8,

                R.drawable.album9
               };//should change this,cant be hardcoded


        apiInterface = ApiClient.getApiClient() .create(ApiInterface.class);
        Call<List<ba.kalishop.kalishop.Model.Product>> call = apiInterface.getAll();

        call.enqueue(new Callback<List<ba.kalishop.kalishop.Model.Product>>() {
            @Override
            public void onResponse(Call<List<ba.kalishop.kalishop.Model.Product>> call, Response <List<ba.kalishop.kalishop.Model.Product>> response) {
                lista = response.body();

                //  Toast.makeText(getApplicationContext(),user.getUsername().toString(),Toast.LENGTH_LONG).show();

                 int i=0;
                  for (final ba.kalishop.kalishop.Model.Product s : lista) {
                      ba.kalishop.kalishop.Model.Product a = new ba.kalishop.kalishop.Model.Product(s.getId(),s.getNaziv(), s.getOpis(),covers[s.getThumbnail()],s.getCijena(),s.getBoje(),s.getVelicina(),2);
                      productList.add(a);
                      i++;


                }
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<List<ba.kalishop.kalishop.Model.Product>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();

            }
        });



        //Product a = new Product("Maroon5", "test", covers[0]);
        //productList.add(a);

        //a = new Product("Sugar Ray", "test", covers[1]);
        //productList.add(a);

        //a = new Product("Bon Jovi", "test", covers[2]);
        //productList.add(a);

//        a = new Product("The Corrs", "test", covers[3]);
        //      productList.add(a);

        //a = new Product("The Cranberries", "test", covers[4]);
        //productList.add(a);

        //a = new Product("Westlife", "test", covers[5]);
        //productList.add(a);

        //   a = new Product("Black Eyed Peas", "test", covers[6]);
        //productList.add(a);

//        a = new Product("VivaLaVida", "test", covers[7]);
        //      productList.add(a);

        //a = new Product("The Cardigans", "test", covers[8]);
        //productList.add(a);

        //a = new Product("Pussycat Dolls", "test", covers[9]);
        //productList.add(a);

        adapter.notifyDataSetChanged();
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

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
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

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


    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


}

