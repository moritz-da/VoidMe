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


/**
 *  LocationListFragment defines base functions of list view screen.
 */
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setUpRecyclerView(View view) {
        foundLocations.clear();
        foundLocations.addAll(DbManager.voidLocation.locationDao().getAll());
        Log.d(TAG, "List:" + foundLocations);
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
        adapter = new LocationListAdapter(foundLocations, this.getActivity());
        adapter.registerAdapterDataObserver(observer);
        recView = view.findViewById(R.id.locationListRecyclerView);
        recView.addItemDecoration(new ItemSpaceDecorator(-80));
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        setUpItemTouchHelper();
    }

    /**
     * These callbacks are triggered when a recyclerview item is moved or swiped.
     * This is where we can set the functionality. In this case, we only use the
     * onSwiped callback to delete the swiped item.
     */
    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Toast.makeText(getContext(), "Deleted: " + foundLocations.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                adapter.deleteItem(position);
                handleRecyclerViewState((View) recView.getParent());
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recView);
    }

    private void search(String title) {
        Log.d(TAG, "Input: " + title);
        foundLocations.clear();
        adapter.notifyItemRangeRemoved(0, foundLocations.size());
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