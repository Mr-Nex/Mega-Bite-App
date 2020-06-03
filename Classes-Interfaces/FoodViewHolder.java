package generator.nex.rexx.userregistration.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import generator.nex.rexx.userregistration.Interface.ItemClickListener;

import com.example.rexx.userregistration.R;

/**
 * Created by rexx on 26-04-2018.
 */

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView food_Name;
    public ImageView food_Image, fav_image, btn_quick_cart;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder(View itemView) {
        super(itemView);

        food_Name = (TextView)itemView.findViewById(R.id.food_name);
        food_Image = (ImageView)itemView.findViewById(R.id.food_image);
        fav_image = (ImageView)itemView.findViewById(R.id.fav);
        btn_quick_cart = (ImageView)itemView.findViewById(R.id.btn_quick_cart);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
