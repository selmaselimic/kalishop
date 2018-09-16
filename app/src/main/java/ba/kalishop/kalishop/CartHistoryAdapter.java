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

public class CartHistoryAdapter extends RecyclerView.Adapter<CartHistoryAdapter.MyViewHolder> {

    private Context mContext;

    private CartHistoryAdapter adapter;
    private List<ba.kalishop.kalishop.Model.InCartHistoryProduct> productList;
    private List<InCartHistoryProduct>p;
    private Button menuButton;
    private ApiInterface apiInterface;
    private List<ba.kalishop.kalishop.Model.Product> lista;
    private Integer CartId;
    private Integer ProductId;
    private Integer s;
    private Float totalPrice=0f;
    private List<InCartHistoryProduct>liss;



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title,price, count;
        public TextView ukupno;
        public ImageView thumbnail, overflow;
        public List<ba.kalishop.kalishop.Model.InCartHistoryProduct>products;
        private Context ctx;


        private Button remove;


        public MyViewHolder(View view,Context ctx,List<ba.kalishop.kalishop.Model.InCartHistoryProduct>list) {
            super(view);
            //  view.setOnClickListener(this);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            price = (TextView) view.findViewById(R.id.price);
            ukupno = (TextView) view.findViewById(R.id.ukupno);
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


    public CartHistoryAdapter(Context mContext, List<ba.kalishop.kalishop.Model.InCartHistoryProduct> productList,List<InCartHistoryProduct>p,Integer CartId) {
        this.mContext = mContext;
        this.productList = productList;
        this.CartId=CartId;
        this.p=p ;
         s=0;





    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_history_card, parent, false);
        itemView.setMinimumWidth(parent.getMeasuredWidth());//will try this

        return new MyViewHolder(itemView,mContext,p);


    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
      // if(s==0){
       // p.add(this.productList.get(0));
        //for(int i=1;i< this.productList.size();i++){
          //  if(this.productList.get(i).getInCarHistorytId()!=productList.get(i-1).getInCarHistorytId()){
          //       p.add(this.productList.get(i));
          //  }
        //}

        //   liss=new ArrayList<InCartHistoryProduct>(productList);
        //   productList.clear();
       //    productList.addAll(p);
      // s++;}


        final  ba.kalishop.kalishop.Model.InCartHistoryProduct product;
        product= p.get(position);

        holder.title.setText("28.4.2018");
      //  holder.thumbnail.setImageResource(product.getThumbnail());
       // holder.count.setText(product.getOpis());
        Product allProducts=product.getProizvod();
          List<Product>pro;

        for (InCartHistoryProduct x:productList
             ) {
                 if(x.getInCarHistorytId()==product.getInCarHistorytId()){
                     holder.count.append("\n"+x.getProizvod().getNaziv());
                     holder.price.append("\n"+x.getProizvod().getCijena()+"BAM");
                     holder.title.setText(x.getDatum().toString());
                     totalPrice+=x.getProizvod().getCijena();


                 }

               }

               holder.ukupno.setText(Float.toString(totalPrice)+" BAM");
               totalPrice=0f;








        //holder.remove.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        prepareProducts2(product.getId());
         //   }
       // });
        // loading album cover using Glide library
       // Glide.with(mContext).load(product.getThumbnail()).into(holder.thumbnail);

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







    public int getItemCount() {

        return p.size();
    }
}
