package de.hdmstuttgart.voidme.ui.location_list;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private LocationListAdapter adapter;
    //private Drawable icon;
//    private ColorDrawable background;

    public SwipeToDeleteCallback(LocationListAdapter adapter) {
        super(0, ItemTouchHelper.LEFT);
        this.adapter = adapter;
        //this.icon = ContextCompat.getDrawable(mAdapter.getContext(), R.drawable.ic_delete_white_36);
//        this.background = new ColorDrawable(Color.RED);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        adapter.deleteItem(position);
        adapter.notifyItemRemoved(position);
        Log.d("-LOCATION ADAPTER-", "DELETE: successful");
    }

    /*
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 0; //so background is behind the rounded corners of itemView

//        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
//        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
//        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX < 0) { // Swiping to the left
//            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
//            int iconRight = itemView.getRight() - iconMargin;
//            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
//            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
//                    itemView.getTop()+50, itemView.getRight(), itemView.getBottom()-50);
        } else { // view is unSwiped
//            background.setBounds(0, 0, 0, 0);
        }
//        background.draw(c);
//        icon.draw(c);
    }
     */
}
