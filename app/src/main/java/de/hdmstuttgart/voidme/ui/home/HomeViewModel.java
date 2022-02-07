package de.hdmstuttgart.voidme.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.hdmstuttgart.voidme.R;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Integer> mInstructionHeader = new MutableLiveData<>();
    private final MutableLiveData<Integer> mInstruction = new MutableLiveData<>();

    /**
     * AndroidViewModel anti-pattern with recourse ID's, avoid dealing with objects that have a lifecycle in ViewModels
     * <a href="https://medium.com/androiddevelopers/locale-changes-and-the-androidviewmodel-antipattern-84eb677660d9">More Info: Android Developers</a>
     */
    public HomeViewModel() {
        mInstructionHeader.setValue(R.string.instructionsHeader);
        mInstruction.setValue(R.string.instruction);
    }

    public LiveData<Integer> getHeader() {
        return mInstructionHeader;
    }

    public LiveData<Integer> getBody() {
        return mInstruction;
    }
}