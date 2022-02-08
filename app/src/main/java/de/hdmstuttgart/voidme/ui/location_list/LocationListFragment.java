package de.hdmstuttgart.voidme.ui.location_list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdmstuttgart.voidme.R;
import de.hdmstuttgart.voidme.database.DbManager;
import de.hdmstuttgart.voidme.database.LocationEntity;
import de.hdmstuttgart.voidme.shared.utils.ui.ItemSpaceDecorator;

public class LocationListFragment extends Fragment {

    private static final String TAG = "-LIST-";
    private LocationListAdapter adapter;
    private RecyclerView recView;
    List<LocationEntity> foundLocations = new ArrayList<>();
    RecyclerView.AdapterDataObserver observer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView(view);
        handleRecyclerViewState(view);
        EditText input = view.findViewById(R.id.searchLocation);
        ImageButton btn = view.findViewById(R.id.btnSearchTitle);
        btn.setOnClickListener(v -> search(input.getText().toString()));
    }

    private void setUpRecyclerView(View view) {
        foundLocations.clear();
        foundLocations.addAll(DbManager.voidLocation.locationDao().getAll());
        Log.d(TAG, "List:" + foundLocations);

        adapter = new LocationListAdapter(foundLocations, this.getActivity());

        recView = view.findViewById(R.id.locationListRecyclerView);
        observer = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                Log.d(TAG, "Removed from Data");
                if (itemCount == 0) handleRecyclerViewState(view);
            }

            @Override
            public void onChanged() {
                super.onChanged();
                Log.d(TAG, "Data Set Changed");
            }
        };
        adapter.registerAdapterDataObserver(observer);
        recView.addItemDecoration(new ItemSpaceDecorator(-80));
        recView.setAdapter(adapter);
        recView.setOnClickListener(e -> {
            handleRecyclerViewState(view);
            Toast.makeText(getContext(), "Clicked!", Toast.LENGTH_SHORT).show();
        });
        recView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void search(String title) {
        Log.d(TAG, "Input: " + title);
        int s = foundLocations.size();
        foundLocations.clear();
        adapter.notifyItemRangeRemoved(0, s);
        foundLocations.addAll(DbManager.voidLocation.locationDao().searchLocation("%" + title +"%"));
        if(foundLocations.size() > 0) {
            adapter.notifyItemRangeChanged(0, foundLocations.size());
            Log.d(TAG, "found List:" + foundLocations.toString());
        } else {
            Toast.makeText(getContext(), "No Items found!", Toast.LENGTH_SHORT).show();
            handleRecyclerViewState((View) recView.getParent());
        }
    }

    private void handleRecyclerViewState(View view) {
        View emptyView = view.findViewById(R.id.empty_view);
        if (adapter.getItemCount() == 0) {
            recView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}