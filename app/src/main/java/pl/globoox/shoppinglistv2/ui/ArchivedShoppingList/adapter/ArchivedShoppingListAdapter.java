package pl.globoox.shoppinglistv2.ui.ArchivedShoppingList.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import pl.globoox.shoppinglistv2.R;
import pl.globoox.shoppinglistv2.model.ShoppingList;
import pl.globoox.shoppinglistv2.ui.ShoppingList.adapter.ItemListAdapter;


public class ArchivedShoppingListAdapter extends RecyclerView.Adapter<ArchivedShoppingListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ShoppingList> shoppingLists;

    public ArchivedShoppingListAdapter(Context context, ArrayList<ShoppingList> shoppingLists) {
        mContext = context;
        this.shoppingLists = shoppingLists;
    }


    @NonNull
    @Override
    public ArchivedShoppingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customlayout_shoplistelement, parent, false);
        return new ArchivedShoppingListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ArchivedShoppingListAdapter.ViewHolder holder, final int position) {
        final ShoppingList currentShopList = shoppingLists.get(position);

        holder.textView_name.setText(currentShopList.getName());
        holder.textView_createdTime.setText(currentShopList.getCreatedTime());
        holder.textView_doneCount.setText(String.valueOf(currentShopList.getDoneCount()));
        holder.textView_maxCount.setText(String.valueOf(currentShopList.getItemsCount()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(R.string.recoverAsk)
                        .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(mContext, R.string.recovered, Toast.LENGTH_SHORT).show();
                                currentShopList.setArchived(false);
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("lists").child(currentShopList.getId()).child("archived");
                                databaseReference.setValue(false);
                                notifyItemRemoved(position);
                                shoppingLists.remove(position);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Nope!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return shoppingLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_name;
        TextView textView_createdTime;
        TextView textView_doneCount;
        TextView textView_maxCount;
        View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            textView_name = itemView.findViewById(R.id.textView_name);
            textView_createdTime = itemView.findViewById(R.id.textView_createdTime);
            textView_doneCount = itemView.findViewById(R.id.textView_doneCount);
            textView_maxCount = itemView.findViewById(R.id.textView_maxCount);
        }
    }
}