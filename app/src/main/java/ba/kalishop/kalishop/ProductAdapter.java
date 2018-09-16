package ba.kalishop.kalishop;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import ba.kalishop.kalishop.Model.*;

/**
 * Created by Korisnik on 12/02/2018.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private Context mContext;
    private List<ba.kalishop.kalishop.Model.Product> productList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, count,cijena;
        public ImageView thumbnail, overflow;
        public List<ba.kalishop.kalishop.Model.Product>products;
        private Context ctx;

        public MyViewHolder(View view,Context ctx,List<ba.kalishop.kalishop.Model.Product>list) {
            super(view);
            view.setOnClickListener(this);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            cijena=(TextView) view.findViewById(R.id.price);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            products=list;
            this.ctx=ctx;
        }
        @Override
        public void onClick(View v){
               int position=getAdapterPosition();
               ba.kalishop.kalishop.Model.Product product=this.products.get(position);
               Intent intent=new Intent(this.ctx,ProductDetailsActivity.class);
               intent.putExtra("img_id",product.getThumbnail());
               intent.putExtra("cijena",product.getCijena());
               intent.putExtra("sizes",product.getVelicina());
               intent.putExtra("colors",product.getBoje());
               intent.putExtra("details",product.getOpis());
               intent.putExtra("Id",product.getId());
               this.ctx.startActivity(intent);
        }
    }


    public ProductAdapter(Context mContext, List<ba.kalishop.kalishop.Model.Product> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_card, parent, false);
                itemView.setMinimumWidth(parent.getMeasuredWidth());//will try this
        return new MyViewHolder(itemView,mContext,productList);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ba.kalishop.kalishop.Model.Product product = productList.get(position);
        holder.title.setText(product.getNaziv());
        holder.thumbnail.setImageResource(product.getThumbnail());
        holder.count.setText(product.getOpis());
        holder.cijena.setText(Float.toString(product.getCijena())+" BAM");
       // holder.count.setText(product.getNumOfSongs() + " songs");

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




    public int getItemCount() {
        return productList.size();
    }
}
