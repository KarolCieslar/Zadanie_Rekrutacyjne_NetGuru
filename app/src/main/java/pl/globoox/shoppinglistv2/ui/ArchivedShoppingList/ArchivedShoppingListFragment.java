package pl.globoox.shoppinglistv2.ui.ArchivedShoppingList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pl.globoox.shoppinglistv2.R;
import pl.globoox.shoppinglistv2.model.ShoppingList;
import pl.globoox.shoppinglistv2.ui.ArchivedShoppingList.adapter.ArchivedShoppingListAdapter;
import pl.globoox.shoppinglistv2.ui.ShoppingList.adapter.ShoppingListAdapter;

public class ArchivedShoppingListFragment extends Fragment {

    View rootView;

    public static ArrayList<ShoppingList> listOfShoppingLists;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_archived_shopping_list, container, false);

        listOfShoppingLists = new ArrayList<>();
        firebaseDatabase.getReference("lists").orderByChild("owner").equalTo("globooxmail@gmail.com").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                ProgressBar progressBar_shopList = rootView.findViewById(R.id.progressBar_shopList);
                progressBar_shopList.setVisibility(View.GONE);

                // Downloading data from Firebase
                for (DataSnapshot data : snapshot.getChildren()) {
                    ShoppingList newShopList = data.getValue(ShoppingList.class);
                    newShopList.setId(data.getKey());
                    if (newShopList.isArchived()) {
                        listOfShoppingLists.add(newShopList);
                    }
                    RecyclerView recyclerView_shoppingList = rootView.findViewById(R.id.recyclerView_shoppingList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView_shoppingList.setLayoutManager(layoutManager);
                    ArchivedShoppingListAdapter adapter = new ArchivedShoppingListAdapter(getContext(), listOfShoppingLists);
                    recyclerView_shoppingList.setAdapter(adapter);
                }

                // Display textView if no data exists
                if (listOfShoppingLists.size() == 0){
                    rootView.findViewById(R.id.textView_noData).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return rootView;
    }
}