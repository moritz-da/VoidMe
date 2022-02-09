package de.hdmstuttgart.voidme.database.di;

import android.content.res.Resources;
import android.util.Log;

import java.util.concurrent.ThreadLocalRandom;

import de.hdmstuttgart.voidme.R;
import de.hdmstuttgart.voidme.database.DbManager;
import de.hdmstuttgart.voidme.database.LocationEntity;

public class DummyFactory {
    private static final String TAG = DummyFactory.class.getSimpleName();

    private final int amount;
    private final Resources resources;

    public DummyFactory(int amount, Resources resources) {
        this.amount = amount;
        this.resources = resources;
    }


    public void createDummies() {
        for (int i = 0; i < amount; i++) {
            String[] catArr = resources.getStringArray(R.array.categories_array);
            LocationEntity dummy = new LocationEntity(
                    "VoidLocation Dummy No." + i,
                    "This is the description of dummy number " + i + " which is a dummy location, suitable for VoidMe.",
                    catArr[ThreadLocalRandom.current().nextInt(0, catArr.length)],
                    (ThreadLocalRandom.current().nextDouble(48.4000, 48.999)),
                    ThreadLocalRandom.current().nextDouble(8.97, 9.4000),
                    0,
                    10,
                    ThreadLocalRandom.current().nextInt(0, 3 + 1)
            );
            DbManager.voidLocation.locationDao().insert(dummy);
        }
        Log.d(TAG, "Saving dummy Entries..." + DbManager.voidLocation.locationDao().getAll().toString());
    }
}
