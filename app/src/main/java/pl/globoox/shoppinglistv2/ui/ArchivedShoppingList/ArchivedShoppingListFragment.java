package pl.globoox.shoppinglistv2.ui.ArchivedShoppingList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import pl.globoox.shoppinglistv2.R;

public class ArchivedShoppingListFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_archived_shopping_list, container, false);

        return root;
    }
}