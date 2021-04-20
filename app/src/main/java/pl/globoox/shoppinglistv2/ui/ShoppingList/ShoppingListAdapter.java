package pl.globoox.shoppinglistv2.ui.ShoppingList;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pl.globoox.shoppinglistv2.R;
import pl.globoox.shoppinglistv2.model.ShoppingList;


public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ShoppingList> shoppingLists;

    public ShoppingListAdapter(Context context, ArrayList<ShoppingList> shoppingLists) {
        mContext = context;
        this.shoppingLists = shoppingLists;
    }


    @NonNull
    @Override
    public ShoppingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customlayout_shoplist, parent, false);
        return new ShoppingListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ShoppingListAdapter.ViewHolder holder, final int position) {
        holder.textView_name.setText(shoppingLists.get(position).getName());
        holder.textView_createdTime.setText(shoppingLists.get(position).getCreatedTime());
        holder.textView_doneCount.setText(String.valueOf(shoppingLists.get(position).getDoneCount()));
        holder.textView_maxCount.setText(String.valueOf(shoppingLists.get(position).getItemsCount()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                final View dialogView = inflater.inflate(R.layout.dialog_rules, null);
                mBuilder.setView(dialogView);
                final AlertDialog dialog = mBuilder.create();

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
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