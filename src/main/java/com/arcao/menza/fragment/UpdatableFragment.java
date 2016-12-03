package com.arcao.menza.fragment;

import android.os.Bundle;

public interface UpdatableFragment {
    Bundle getArguments();

    void update();
}
