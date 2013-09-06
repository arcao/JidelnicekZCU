package com.arcao.menza.fragment;

import android.os.Bundle;

/**
 * Created by msloup on 16.8.13.
 */
public interface UpdateableFragment {
    Bundle getArguments();
    void update();
}
