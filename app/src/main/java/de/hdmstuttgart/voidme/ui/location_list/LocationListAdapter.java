package de.hdmstuttgart.voidme.ui.location_list;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdmstuttgart.voidme.R;
import de.hdmstuttgart.voidme.database.DbManager;
import de.hdmstuttgart.voidme.database.LocationEntity;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {
    private static final String TAG = "-LOCATION ADAPTER-";
    private final List<LocationEntity> list;
    private final Activity activity;

    public LocationListAdapter(List<LocationEntity> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public LocationListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: " + viewType);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationListAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        holder.listItem = list.get(position);
        holder.locationItemTitle.setText(list.get(position).getTitle());
        holder.locationItemDescription.setText(list.get(position).getDescription());
        holder.locationItemLatitude.setText(activity.getString(R.string.longitude_short, list.get(position).getLatitude()));
        holder.locationItemLongitude.setText(activity.getString(R.string.latitude_short, list.get(position).getLongitude()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void deleteItem(int position) {
//            mRecentlyDeletedItem = mListItems.get(position);
//            mRecentlyDeletedItemPosition = position;
        Log.d(TAG, "DELETE: Position: " + position);
        Log.d(TAG, "DELETE: Item: " + list.get(position));
        Log.d(TAG, "DELETE: List before: " + list);
        Log.d(TAG, "DELETE: List DB before: " + DbManager.voidLocation.locationDao().getAll());
        DbManager.voidLocation.locationDao().delete(list.get(position));
        list.remove(position);

        Log.d(TAG, "DELETE: List after: " + list);
        Log.d(TAG, "DELETE: List DB after: " + DbManager.voidLocation.locationDao().getAll());
        notifyDataSetChanged();
        //notifyItemRemoved(position);
        //notifyItemRangeChanged(position, getItemCount());
//            showUndoSnackbar();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View listView;
        public LocationEntity listItem;
        public final TextView locationItemTitle;
        public final TextView locationItemDescription;
        public final TextView locationItemLatitude;
        public final TextView locationItemLongitude;
        //public final ImageView locationItemImage;

        //List<LocationEntity> voidLocations = DbManager.voidLocation.locationDao().getAll();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            listView = itemView;
            locationItemTitle =  itemView.findViewById(R.id.itemTitle);
            locationItemDescription =  itemView.findViewById(R.id.itemDescription);
            locationItemLatitude =  itemView.findViewById(R.id.itemLatitude);
            locationItemLongitude =  itemView.findViewById(R.id.itemLongitude);

            itemView.setOnClickListener(v -> {
                Toast.makeText(v.getContext(), "Clicked: " + list.get(getLayoutPosition()).getTitle(), Toast.LENGTH_SHORT).show();
                DbManager.voidLocation.locationDao().delete(list.get(getLayoutPosition()));
                list.remove(getLayoutPosition());
                notifyItemRemoved(getLayoutPosition());
            });
        }
    }
}