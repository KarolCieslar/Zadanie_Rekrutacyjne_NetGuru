package pl.globoox.shoppinglistv2.ui.ShoppingList.adapter;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import pl.globoox.shoppinglistv2.R;
import pl.globoox.shoppinglistv2.model.Item;
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customlayout_shoplistelement, parent, false);
        return new ShoppingListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ShoppingListAdapter.ViewHolder holder, final int position) {
        final ShoppingList currentShopList = shoppingLists.get(position);

        holder.textView_name.setText(currentShopList.getName());
        holder.textView_createdTime.setText(String.valueOf(getDateFromUnix(currentShopList)));
        holder.textView_doneCount.setText(String.valueOf(currentShopList.getDoneCount()));
        holder.textView_maxCount.setText(String.valueOf(currentShopList.getItemsCount()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                itemsDialogShow(currentShopList, position);

            }
        });
    }

    private String getDateFromUnix(ShoppingList currentShopList) {
        Date date = new Date();
        date.setTime(currentShopList.getCreatedTime() * 1000);
        String pattern = "dd-MM-yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }


    /* Display dialog with items and form
     where user can add new item to list */
    private void itemsDialogShow(final ShoppingList currentShopList, final int position) {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View dialogView = inflater.inflate(R.layout.dialog_listofitems, null);
        mBuilder.setView(dialogView);
        final AlertDialog dialog = mBuilder.create();

        final RecyclerView recyclerView_shoppingListDetails = dialogView.findViewById(R.id.recyclerView_itemList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView_shoppingListDetails.setLayoutManager(layoutManager);
        final ItemListAdapter adapter = new ItemListAdapter(mContext, currentShopList.getItems(), currentShopList);
        recyclerView_shoppingListDetails.setAdapter(adapter);

        // If list does not have any items show noData textView
        if (currentShopList.getItemsCount() == 0) {
            dialogView.findViewById(R.id.textView_noItemsData).setVisibility(View.VISIBLE);
        }

        TextView textView_listName = dialogView.findViewById(R.id.textView_listName);
        textView_listName.setText(currentShopList.getName());

        // Set onClickListener on archive button
        dialogView.findViewById(R.id.button_archiveList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, R.string.archivedSuccessfull, Toast.LENGTH_SHORT).show();
                currentShopList.setArchived(true);
                dialog.dismiss();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("lists").child(currentShopList.getId()).child("archived");
                databaseReference.setValue(true);
                shoppingLists.remove(position);
                notifyItemRemoved(position);
            }
        });

        // Declare
        Button button_remove1FromValue = dialogView.findViewById(R.id.button_remove1FromValue);
        Button button_add1ToValue = dialogView.findViewById(R.id.button_add1ToValue);
        Button button_addItem = dialogView.findViewById(R.id.button_addItem);
        final TextView textView_value = dialogView.findViewById(R.id.textView_value);
        final EditText editText_name = dialogView.findViewById(R.id.editText_name);

        // Remove 1 form value
        button_remove1FromValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.parseInt(textView_value.getText().toString());
                if (value != 0) {
                    textView_value.setText(String.valueOf(--value));
                }
            }
        });

        // Add 1 to value
        button_add1ToValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_value.setText(String.valueOf(Integer.parseInt(textView_value.getText().toString()) + 1));
            }
        });

        // Add new item to database and list
        button_addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText_name.getText().toString();
                if (name.trim().length() == 0) {
                    editText_name.setError(mContext.getString(R.string.additem_emptyField));
                    return;
                }

                String id = FirebaseDatabase.getInstance().getReference().child("lists").child(currentShopList.getId()).child("items").push().getKey();
                Item newItem = new Item(id, name, Integer.parseInt(textView_value.getText().toString()));
                Map<String, Object> values = newItem.toMap();
                FirebaseDatabase.getInstance().getReference().child("lists").child(currentShopList.getId()).child("items").child(id).setValue(values);

                currentShopList.addItem(newItem);
                final ItemListAdapter adapter = new ItemListAdapter(mContext, currentShopList.getItems(), currentShopList);
                recyclerView_shoppingListDetails.setAdapter(adapter);

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
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