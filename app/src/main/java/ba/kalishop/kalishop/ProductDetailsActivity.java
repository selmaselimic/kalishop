package ba.kalishop.kalishop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import ba.kalishop.kalishop.Model.*;
import ba.kalishop.kalishop.Model.Product;
import ba.kalishop.kalishop.web.ApiClient;
import ba.kalishop.kalishop.web.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private ApiInterface apiInterface;
    private Incart cart;
    ImageView image;
    TextView name;
    TextView details;
    Button addCart;
    private Button increase;
    private Button decrease;
    private TextView quantity;
    private EditText counter;//ovo treba napraviti u layoutu
    private Integer CartId=0;
    private Integer quan=1;
    private String color="",size="";
    private List<String>colorss;
    private List<String>sizess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        image=(ImageView)findViewById(R.id.details_image);
        name=(TextView)findViewById(R.id.details_name);

        addCart=findViewById(R.id.add_cart_button);

        colorss= Arrays.asList(getIntent().getStringExtra("colors").split(","));//ovo jos nisam dodala u sh preferences
        for(String x :colorss){

          Button myButton = new Button(this);
          myButton.setTag(x);

          // myButton.setText(x);
           myButton.setBackgroundResource(R.drawable.oval);
           if(x.equals("brown")) {
               GradientDrawable backgroundGradient = (GradientDrawable)myButton.getBackground();
               backgroundGradient.setColor(getResources().getColor(R.color.brown));
           }
           else if(x.equals("pink")) {
                GradientDrawable backgroundGradient = (GradientDrawable)myButton.getBackground();
                backgroundGradient.setColor(getResources().getColor(R.color.pink));
            }
            else if(x.equals("black")) {
                GradientDrawable backgroundGradient = (GradientDrawable)myButton.getBackground();
                backgroundGradient.setColor(getResources().getColor(R.color.black));
            }
           LinearLayout ll = (LinearLayout)findViewById(R.id.colors);
           LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(50,50);
            lp.setMargins(15,0,15,0);
           ll.addView(myButton, lp);

           myButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   color=v.getTag().toString();
                   Toast.makeText(getApplicationContext(),color,Toast.LENGTH_LONG).show();
               }
           });

       }

        sizess= Arrays.asList(getIntent().getStringExtra("sizes").split(","));//ovo jos nisam dodala u sh preferences
        for(final String x :sizess){

            Button myButton = new Button(this);
            myButton.setText(x);
            myButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            myButton.setTextColor(getResources().getColor(R.color.colorWhite));

            LinearLayout ll = (LinearLayout)findViewById(R.id.sizes);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 105);
            lp.setMargins(15,0,15,0);
            ll.addView(myButton, lp);

            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    size=x;
                    Toast.makeText(getApplicationContext(),size,Toast.LENGTH_LONG).show();
                }
            });
       }


        increase=findViewById(R.id.quantitiy_plus);
        decrease=findViewById(R.id.quantitiy_minus);
        quantity=findViewById(R.id.quantitiyNmb);
        // dodat treba u layout counter=(EditText)findViewById(R.id.counter);

        image.setImageResource(getIntent().getIntExtra("img_id",00));
        name.setText(Float.toString(getIntent().getFloatExtra("cijena",0))+" BAM");




        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quan++;
                quantity.setText(quan.toString());
            }
        });

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quan--;
                if(quan<0){
                    quan=0;
                }
                quantity.setText(quan.toString());
            }
        });



       addCart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //sad treba pozvat api i poslat vrijednosti add to cart
               SharedPreferences sp1=getSharedPreferences ("Login",0);

               Integer id=sp1.getInt("Id",0);
               Integer productId=getIntent().getIntExtra("Id",0);
               ba.kalishop.kalishop.Model.Product p=new Product();
               p.setId(productId);
               p.setCijena(getIntent().getFloatExtra("cijena",0));
               p.setOpis("nesto");
               p.setVelicina(size);
               p.setKolicina(quan);
               p.setBoje(color);
               p.setThumbnail(getIntent().getIntExtra("img_id",00));

               SharedPreferences sp2=getSharedPreferences("Cart", 0);
               CartId=sp2.getInt("Id",0);
               apiInterface = ApiClient.getApiClient() .create(ApiInterface.class);
               Call<Integer> call = apiInterface.storeInCart(CartId,id,p);//ovdje bi trebala cartId

               call.enqueue(new Callback<Integer>() {
                   //ovo mozda jos nisam zavrsila,testirat treba
                   @Override
                   public void onResponse(Call<Integer> call, Response<Integer> response) {
                       CartId = response.body();

                       SharedPreferences sp2=getSharedPreferences("Cart", 0);
                       SharedPreferences.Editor Ed2=sp2.edit();
                       Ed2.putInt("Id",CartId);
                       Ed2.commit();

                       Toast.makeText(getApplicationContext(),"Uspjesno dodato",Toast.LENGTH_LONG).show();


                       Intent i=new Intent(ProductDetailsActivity.this,InCartActivity.class);//ovdje trebam izmijeniti na incart da otvara al puca mi nešto oj a nije pucalo
                       Bundle bundle = new Bundle();

                         //Add your data to bundle,should preuzeti ovaj parametar u incartactivity
                      bundle.putInt("Id",CartId);
                       i.putExtras(bundle);
                      startActivity(i);
                       finish();

                       //   for (final String s : words) {


                       //       word.setText(s);

                       //  }


                   }

                   @Override
                   public void onFailure(Call<Integer> call, Throwable t) {
                       Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();

                   }
               });

           }
       });
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarr);
      setSupportActionBar(toolbar);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutt);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
               this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
       drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_vieww);



        navigationView.setNavigationItemSelectedListener(this);



        //try {
        //   Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        // } catch (Exception e) {
        //   e.printStackTrace();
        //}


    }


    @Override
    public void onBackPressed() {//menu activity
        DrawerLayout drawer =  findViewById(R.id.drawer_layoutt);
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
            Intent intent=new Intent(ProductDetailsActivity.this,ProductsActivity.class);

            startActivity(intent);
            item.setChecked(true);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent=new Intent(ProductDetailsActivity.this,CartHistoryActivity.class);

            startActivity(intent);
            item.setChecked(true);

        } else if (id == R.id.nav_slideshow) {


            Intent intent=new Intent(ProductDetailsActivity.this,ProfileActivity.class);
            startActivity(intent);
            item.setChecked(true);

        } else if (id == R.id.nav_manage) {
            Intent intent=new Intent(ProductDetailsActivity.this,LoginActivity.class);
            startActivity(intent);
            item.setChecked(true);

        } //else if (id == R.id.nav_share) {

        // } else if (id == R.id.nav_send) {

        // }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
}




