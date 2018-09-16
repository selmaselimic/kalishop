package ba.kalishop.kalishop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ba.kalishop.kalishop.Model.*;
import ba.kalishop.kalishop.Model.Product;
import ba.kalishop.kalishop.web.ApiClient;
import ba.kalishop.kalishop.web.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.sql.Types.NULL;

public class InCartActivity extends AppCompatActivity
       implements NavigationView.OnNavigationItemSelectedListener
{

    private RecyclerView recyclerView;
    private ProductAdapterCart adapter;

    private List<ba.kalishop.kalishop.Model.Product> productList;
    private Button menuButton;
    private ApiInterface apiInterface;
    private List<Product> lista;
    private Button buy;
    private TextView price;
    private  Integer CartId;
    private List<Product> cart;
    private float subtotal=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_cart);

        //trebam dodat da kad se klikne na + i - da se mijenja kolicina
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarrr);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_vieew);
        buy=findViewById(R.id.buy);
        price=(TextView)findViewById(R.id.incart_price) ;


        productList = new ArrayList<>();
        SharedPreferences sp2=getSharedPreferences("Cart", 0);
        CartId=sp2.getInt("Id",0);


        for (int d=0; d<productList.size(); d++) {
            // calculate total value

            Float totalPrice = productList.get(d).getCijena();


            // set it for subTotal
            subtotal = subtotal + totalPrice;
        }
       /* SharedPreferences sp1=getSharedPreferences ("priceCart",0);

        Float pricee=sp1.getFloat("price", 00);*/
        price.setText(Float.toString(subtotal));

        adapter = new ProductAdapterCart(this, productList,CartId);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new InCartActivity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);



        //  prepareProducts(); ovo mi sad ne treba


        //sad treba pozvat api da se preuzme cart i svi proizvodi u incart
        apiInterface = ApiClient.getApiClient() .create(ApiInterface.class);
        Call<List< ba.kalishop.kalishop.Model.Product >> call = apiInterface.getInCart(CartId);
        call.enqueue(new Callback<List< ba.kalishop.kalishop.Model.Product >>() {
            //ovo mozda jos nisam zavrsila,testirat treba
            @Override
            public void onResponse(Call<List< ba.kalishop.kalishop.Model.Product >> call, Response<List< ba.kalishop.kalishop.Model.Product >> response) {
                cart = response.body();
                 //ovdje trebam dodati da uzme iz cart sve products
                //ovo ce trebat dinamicki slike da se mijenjaju i druge slike mi trebaju
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
                    };

                int i=0;
                for (final  ba.kalishop.kalishop.Model.Product  s : cart) {
                   ba.kalishop.kalishop.Model.Product a = new ba.kalishop.kalishop.Model.Product(s.getId(),s.getNaziv(), s.getOpis(),covers[s.getThumbnail()],s.getCijena(),s.getBoje(),s.getVelicina(),s.getKolicina());
                   productList.add(a);
                   i++;}

                for (int d=0; d<productList.size(); d++) {
                    // calculate total value

                    Float totalPrice = productList.get(d).getCijena()*productList.get(d).getKolicina();


                    // set it for subTotal
                    subtotal = subtotal + totalPrice;
                }
       /* SharedPreferences sp1=getSharedPreferences ("priceCart",0);

        Float pricee=sp1.getFloat("price", 00);*/
                price.setText(Float.toString(subtotal));








                //  Toast.makeText(getApplicationContext(),user.getUsername().toString(),Toast.LENGTH_LONG).show();


                //Add your data to bundle,should preuzeti ovaj parametar u incartactivity


                //   for (final String s : words) {


                //       word.setText(s);

                //  }


            }

            @Override
            public void onFailure(Call<List< ba.kalishop.kalishop.Model.Product >> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();

            }
        });

        adapter.notifyDataSetChanged();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layouttt);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_viewww);
        navigationView.setNavigationItemSelectedListener(this);



        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
         //treba ocistiti cart u aplikaciji tj lista da bude null
                   productList=null;

                apiInterface = ApiClient.getApiClient() .create(ApiInterface.class);
                Call<Void> call = apiInterface.Buy(CartId);//ovo je sad hardcoded but will change later nmgu sad stv,msm da trebam samo preuzet iz bundle id

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        // lista = response.body();
                        SharedPreferences sp2=getSharedPreferences("Cart", 0);
                        SharedPreferences.Editor Ed2=sp2.edit();
                        Ed2.putInt("Id",0);
                        Ed2.commit();
                        Toast.makeText(getApplicationContext(),"Uspjesno ste kupili odabrane proizvode",Toast.LENGTH_LONG).show();
                        Intent i=new Intent(InCartActivity.this,ProductsActivity.class);
                        startActivity(i);
                        finish();


                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();

                    }
                });

            }
        });


    }


    @Override
    public void onBackPressed() {//menu activity
        DrawerLayout drawer =  findViewById(R.id.drawer_layouttt);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")//menu activity
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {//menu activity
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            Intent intent=new Intent(InCartActivity.this,ProductsActivity.class);

            startActivity(intent);
            item.setChecked(true);
        } else if (id == R.id.nav_gallery) {
            Intent intent=new Intent(InCartActivity.this,CartHistoryActivity.class);

            startActivity(intent);
            item.setChecked(true);

        } else if (id == R.id.nav_slideshow) {


            Intent intent=new Intent(InCartActivity.this,ProfileActivity.class);
            startActivity(intent);
            item.setChecked(true);

        } else if (id == R.id.nav_manage) {
            Intent intent=new Intent(InCartActivity.this,LoginActivity.class);
            startActivity(intent);
            item.setChecked(true);

        } //else if (id == R.id.nav_share) {

        // } else if (id == R.id.nav_send) {

        // }

        DrawerLayout drawer = findViewById(R.id.drawer_layouttt);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_c);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_c);
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
