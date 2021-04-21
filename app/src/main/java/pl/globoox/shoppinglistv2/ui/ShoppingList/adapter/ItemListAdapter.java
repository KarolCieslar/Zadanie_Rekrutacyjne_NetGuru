package pl.globoox.shoppinglistv2.ui.ShoppingList.adapter;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import pl.globoox.shoppinglistv2.R;
import pl.globoox.shoppinglistv2.model.Item;
import pl.globoox.shoppinglistv2.model.ShoppingList;


public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private Context mContext;
    private ShoppingList shopList;
    private List<Item> itemList;

    public ItemListAdapter(Context context, List<Item> itemList, ShoppingList shopList) {
        mContext = context;
        this.itemList = itemList;
        this.shopList = shopList;
    }


    @NonNull
    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customlayout_itemelement, parent, false);
        return new ItemListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ItemListAdapter.ViewHolder holder, final int position) {

        // Set name, value and alpha
        holder.textView_itemName.setText(itemList.get(position).getName());
        holder.textView_itemValue.setText(String.valueOf(itemList.get(position).getValue()));
        holder.itemView.setAlpha(itemList.get(position).getAlpha());

        // Set onClickListener on delete item button
        holder.imageView_deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, R.string.itemRemoved, Toast.LENGTH_SHORT).show();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("lists").child(shopList.getId()).child("items").child(itemList.get(position).getId());
                databaseReference.removeValue();
                shopList.removeItem(itemList.get(position).getId());
                itemList.remove(position);
                notifyDataSetChanged();

            }
        });

       /* Set onClickListener on itemView
        Status are changed by clicking on item */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processStatus(position, holder);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("lists").child(shopList.getId()).child("items").child(itemList.get(position).getId()).child("status");
                databaseReference.setValue(itemList.get(position).getStatus());
            }
        });

    }

    /* Process status method
    This method change status and alpha */
    private void processStatus(int position, @NonNull ViewHolder holder) {
        if (itemList.get(position).getStatus().equalsIgnoreCase("done")) {
            itemList.get(position).setStatus("active");
        } else {
            itemList.get(position).setStatus("done");
        }
        holder.itemView.setAlpha(itemList.get(position).getAlpha());
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_listName;
        TextView textView_itemName;
        TextView textView_itemValue;
        ImageView imageView_deleteItem;
        View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            textView_listName = itemView.findViewById(R.id.textView_listName);
            textView_itemName = itemView.findViewById(R.id.textView_itemName);
            textView_itemValue = itemView.findViewById(R.id.textView_itemValue);
            imageView_deleteItem = itemView.findViewById(R.id.imageView_deleteItem);

        }
    }
}