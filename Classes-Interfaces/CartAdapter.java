package generator.nex.rexx.userregistration.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import generator.nex.rexx.userregistration.Cart;
import generator.nex.rexx.userregistration.Database.Database;
import generator.nex.rexx.userregistration.Interface.ItemClickListener;
import generator.nex.rexx.userregistration.Model.Order;

import com.example.rexx.userregistration.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by rexx on 29-04-2018.
 */
class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView cart_item_name, cart_item_price;
    public ElegantNumberButton btn_quantity;

    private ItemClickListener itemClickListener;

    public void setCart_item_name(TextView cart_item_name) {
        this.cart_item_name = cart_item_name;
    }

    public CartViewHolder(View itemView) {
        super(itemView);
        cart_item_name = (TextView)itemView.findViewById(R.id.cart_item_name);
        cart_item_price = (TextView)itemView.findViewById(R.id.cart_item_price);
        btn_quantity = (ElegantNumberButton) itemView.findViewById(R.id.btn_quantity);
    }

    @Override
    public void onClick(View view) {

    }
}
public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData = new ArrayList<>();
    private Cart cart;

    public CartAdapter(List<Order> listData, Cart cart) {
        this.listData = listData;
        this.cart = cart;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_layout, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, final int position) {
        /***********
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+listData.get(position).getQuantity(), Color.GRAY);
        holder.cart_item_count.setImageDrawable(drawable);
        ***********/

        holder.btn_quantity.setNumber(listData.get(position).getQuantity());

        holder.btn_quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order = listData.get(position);
                order.setQuantity(String.valueOf(newValue));
                new Database(cart).updateCart(order);

                //Calculate total price
                int total = 0;
                List<Order> orders = new Database(cart).getCarts();
                for(Order item: orders)
                    total += (Integer.parseInt(order.getPrice()))*(Integer.parseInt(item.getQuantity()));
                Locale locale = new Locale("en", "IN");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                cart.textTotalPrice.setText(fmt.format(total));
            }
        });

        Locale locale = new Locale("en", "IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));
        holder.cart_item_price.setText(fmt.format(price));
        holder.cart_item_name.setText(listData.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
