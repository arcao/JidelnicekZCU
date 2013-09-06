package com.arcao.menza.fragment;

import android.os.Bundle;

public interface UpdateableFragment {
    Bundle getArguments();
    void update();
}
