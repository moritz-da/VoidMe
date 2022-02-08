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
import de.hdmstuttgart.voidme.ui.di.Helper;

/**
 *
 */
public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {
    private static final String TAG = "-LOCATION_ADAPTER-";
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
        holder.locationItemIcon.setImageBitmap(Helper.vectorToBitmap(activity.getApplicationContext(), Helper.getCategoryIcon(list.get(position).getCategory(), activity.getApplicationContext().getResources()), list.get(position).getSeverity()));
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
        DbManager.voidLocation.locationDao().delete(list.get(position));
        list.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View listView;
        public LocationEntity listItem;
        public final ImageView locationItemIcon;
        public final TextView locationItemTitle;
        public final TextView locationItemDescription;
        public final TextView locationItemLatitude;
        public final TextView locationItemLongitude;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listView = itemView;
            locationItemIcon =  itemView.findViewById(R.id.listItemIcon);
            locationItemTitle =  itemView.findViewById(R.id.itemTitle);
            locationItemDescription =  itemView.findViewById(R.id.itemDescription);
            locationItemLatitude =  itemView.findViewById(R.id.itemLatitude);
            locationItemLongitude =  itemView.findViewById(R.id.itemLongitude);

            itemView.setOnClickListener(v -> {
                Toast.makeText(v.getContext(), "Clicked: " + list.get(getLayoutPosition()).getTitle(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}