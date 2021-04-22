package pl.globoox.shoppinglistv2.ui.ShoppingList;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import pl.globoox.shoppinglistv2.R;
import pl.globoox.shoppinglistv2.model.Item;
import pl.globoox.shoppinglistv2.model.ShoppingList;
import pl.globoox.shoppinglistv2.ui.ShoppingList.adapter.ItemListAdapter;
import pl.globoox.shoppinglistv2.ui.ShoppingList.adapter.ShoppingListAdapter;

public class ShoppingListFragment extends Fragment {

    View rootView;
    RecyclerView recyclerView_shoppingList;
    ShoppingListAdapter adapter;

    public static ArrayList<ShoppingList> listOfShoppingLists;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        listOfShoppingLists = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("lists").orderByChild("owner").equalTo("globooxmail@gmail.com").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                ProgressBar progressBar_shopList = rootView.findViewById(R.id.progressBar_shopList);
                progressBar_shopList.setVisibility(View.GONE);

                // Get data from firebase database
                for (DataSnapshot data : snapshot.getChildren()) {
                    ShoppingList newShopList = data.getValue(ShoppingList.class);
                    newShopList.setId(data.getKey());
                    if (!newShopList.isArchived()) {
                        listOfShoppingLists.add(newShopList);
                    }

                    sortShoppingList();
                    createAdapter();
                }

                // If list are empty swhow noData textView
                if (listOfShoppingLists.size() == 0) {
                    rootView.findViewById(R.id.textView_noData).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FloatingActionButton floatButton_addList = rootView.findViewById(R.id.floatButton_addList);
        floatButton_addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = LayoutInflater.from(getContext());
                final View dialogView = inflater.inflate(R.layout.dialog_addlist, null);
                mBuilder.setView(dialogView);
                final AlertDialog dialog = mBuilder.create();

                // Declare
                Button button_addList = dialogView.findViewById(R.id.button_addList);
                final EditText editText_name = dialogView.findViewById(R.id.editText_name);

                button_addList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = editText_name.getText().toString();
                        if (name.trim().length() == 0) {
                            editText_name.setError(getContext().getString(R.string.additem_emptyField));
                            return;
                        }

                        String id = FirebaseDatabase.getInstance().getReference().child("lists").push().getKey();
                        ShoppingList newList = new ShoppingList(id, name, System.currentTimeMillis() / 1000L);
                        Map<String, Object> values = newList.toMap();
                        FirebaseDatabase.getInstance().getReference().child("lists").child(id).setValue(values);

                        listOfShoppingLists.add(newList);
                        adapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });


        return rootView;
    }


    private void createAdapter() {
        recyclerView_shoppingList = rootView.findViewById(R.id.recyclerView_shoppingList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView_shoppingList.setLayoutManager(layoutManager);
        adapter = new ShoppingListAdapter(getContext(), listOfShoppingLists);
        recyclerView_shoppingList.setAdapter(adapter);
    }


    private void sortShoppingList() {
        Collections.sort(listOfShoppingLists, new Comparator<ShoppingList>() {
            @Override
            public int compare(ShoppingList s1, ShoppingList s2) {
                if (s1.getCreatedTime() > s2.getCreatedTime())
                    return -1;
                if (s1.getCreatedTime() < s2.getCreatedTime())
                    return 1;
                return 0;
            }
        });
    }
}