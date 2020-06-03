package generator.nex.rexx.userregistration.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import generator.nex.rexx.userregistration.Interface.ItemClickListener;

import com.example.rexx.userregistration.R;

/**
 * Created by rexx on 21-04-2018.
 */

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView menu_name;
    public ImageView menu_image;

    private ItemClickListener itemClickListener;

    public MenuViewHolder(View itemView) {
        super(itemView);

        menu_name = (TextView)itemView.findViewById(R.id.menu_name);
        menu_image = (ImageView)itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}
