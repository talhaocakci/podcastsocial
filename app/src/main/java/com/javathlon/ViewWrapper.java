package com.javathlon;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

class ViewWrapper {
    View base;
    CheckBox checker = null;
    TextView label = null;

    ViewWrapper(View base) {
        this.base = base;
    }

    CheckBox getCheckBox() {
        if (checker == null) {
            checker = (CheckBox) base.findViewById(R.id.playlistchecker);
        }

        return (checker);
    }

    TextView getLabel() {
        if (label == null) {
            label = (TextView) base.findViewById(R.id.label);
        }

        return (label);
    }
}
