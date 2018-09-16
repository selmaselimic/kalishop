package ba.kalishop.kalishop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ba.kalishop.kalishop.Model.*;
import ba.kalishop.kalishop.Model.Product;
import ba.kalishop.kalishop.web.ApiClient;
import ba.kalishop.kalishop.web.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Korisnik on 12/02/2018.
 */

public class ProductAdapterCart extends RecyclerView.Adapter<ProductAdapterCart.MyViewHolder> {

    private Context mContext;

    private ProductAdapterCart adapter;
    private List<ba.kalishop.kalishop.Model.Product> productList;
    private Button menuButton;
    private ApiInterface apiInterface;
    private List<ba.kalishop.kalishop.Model.Product> lista;
    private Integer CartId;
    private float price=0;

    private Integer ProductId;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, count;
        public ImageView thumbnail, overflow;
        public List<ba.kalishop.kalishop.Model.Product>products;
        private Context ctx;


        private Button remove;


        public MyViewHolder(View view,Context ctx,List<ba.kalishop.kalishop.Model.Product>list) {
            super(view);
          //  view.setOnClickListener(this);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            remove=view.findViewById(R.id.remove);
            overflow = (ImageView) view.findViewById(R.id.overflow);

            products=list;
            this.ctx=ctx;


            //remove.setOnClickListener(this);
        }
       // @Override
        public void onClick(View v){
          //  int position=getAdapterPosition();
           // ba.kalishop.kalishop.Model.Product product=this.products.get(position);
            //Intent intent=new Intent(this.ctx,ProductDetailsActivity.class);
            //intent.putExtra("img_id",product.getThumbnail());
            //intent.putExtra("name",product.getNaziv());
            //intent.putExtra("details",product.getOpis());
            //intent.putExtra("Id",product.getId());
            //this.ctx.startActivity(intent);


                    //ovdje sad trebam pozvati api za brisanje producta iz baze i da se ponovo loadaju producti u cart koji
                    //su ostali tu

                   // prepareProducts2();




        }
    }


    public ProductAdapterCart(Context mContext, List<ba.kalishop.kalishop.Model.Product> productList,Integer CartId) {
        this.mContext = mContext;
        this.productList = productList;
        this.CartId=CartId;




    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.incart_card, parent, false);
        itemView.setMinimumWidth(parent.getMeasuredWidth());//will try this

        return new MyViewHolder(itemView,mContext,productList);


    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
       final  ba.kalishop.kalishop.Model.Product product = productList.get(position);
        holder.title.setText(product.getNaziv());
        holder.thumbnail.setImageResource(product.getThumbnail());
        holder.count.setText(product.getOpis());
        price+=product.getCijena();
        SharedPreferences preferences=mContext.getSharedPreferences("priceCart",mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor =preferences.edit();
        editor.putFloat("price",price);
        editor.commit();


        // holder.count.setText(product.getNumOfSongs() + " songs");
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareProducts2(product.getId());
            }
        });
        // loading album cover using Glide library
        Glide.with(mContext).load(product.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // showPopupMenu(holder.overflow);
                Intent intent=new Intent(mContext,LoginActivity.class);//this is just a test
                mContext.startActivity(intent);


            }
        });


    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_product, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }


    private void prepareProducts2(int PId) {
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

        productList = new ArrayList<>();

        adapter = new ProductAdapterCart(mContext ,productList,CartId);




        apiInterface = ApiClient.getApiClient() .create(ApiInterface.class);
        //ovo radi samo trebam promj parametre da nisu hradcoded ,to cu kasnije sad cu malo odmorit
        Call<List<Product>> call = apiInterface.removeFromCart(CartId,PId);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<ba.kalishop.kalishop.Model.Product>> call, Response<List<Product>> response) {
                lista = response.body();
                // ne dobijam response ovdje dont know why
                //  Toast.makeText(getApplicationContext(),user.getUsername().toString(),Toast.LENGTH_LONG).show();

                int i=0;
                for (final ba.kalishop.kalishop.Model.Product s : lista) {
                    ba.kalishop.kalishop.Model.Product a = new ba.kalishop.kalishop.Model.Product(s.getId(),s.getNaziv(), s.getOpis(),covers[i],s.getCijena(),s.getBoje(),s.getVelicina(),2);
                    productList.add(a);
                    if(i==10){
                        i=0;
                    }else {
                        i++;
                    }


                }
                Intent intent=new Intent(mContext,InCartActivity.class);
                mContext.startActivity(intent);


            }

            @Override
            public void onFailure(Call<List<ba.kalishop.kalishop.Model.Product>> call, Throwable t) {
               // Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();

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





    public int getItemCount() {
        return productList.size();
    }
}
