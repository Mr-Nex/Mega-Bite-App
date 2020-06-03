package generator.nex.rexx.userregistration.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import generator.nex.rexx.userregistration.Interface.ItemClickListener;
import com.example.rexx.userregistration.R;

/**
 * Created by rexx on 08-05-2018.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId, txtOrderStatus, txtOrderPhone, txtOrderAdress;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderId = (TextView)itemView.findViewById(R.id.order_id);
        txtOrderStatus = (TextView)itemView.findViewById(R.id.order_status);
        txtOrderPhone = (TextView)itemView.findViewById(R.id.order_phone);
        txtOrderAdress = (TextView)itemView.findViewById(R.id.order_address);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
