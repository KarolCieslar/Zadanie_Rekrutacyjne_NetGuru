package pl.globoox.shoppinglistv2.ui.ShoppingList;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pl.globoox.shoppinglistv2.R;
import pl.globoox.shoppinglistv2.model.ShoppingList;

public class ShoppingListFragment extends Fragment {

    View rootView;

    public static ArrayList<ShoppingList> listOfShoppingLists;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        listOfShoppingLists = new ArrayList<>();
        firebaseDatabase.getReference("lists").orderByChild("owner").equalTo("userid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                ProgressBar progressBar_shopList = rootView.findViewById(R.id.progressBar_shopList);
                progressBar_shopList.setVisibility(View.GONE);

                if (!snapshot.exists()) {
                    rootView.findViewById(R.id.textView_noData).setVisibility(View.VISIBLE);
                    return;
                }

                for (DataSnapshot data : snapshot.getChildren()) {
                    listOfShoppingLists.add(data.getValue(ShoppingList.class));
                    RecyclerView recyclerView_shoppingList = rootView.findViewById(R.id.recyclerView_shoppingList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView_shoppingList.setLayoutManager(layoutManager);
                    ShoppingListAdapter adapter = new ShoppingListAdapter(getContext(), listOfShoppingLists);
                    recyclerView_shoppingList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return rootView;
    }
}