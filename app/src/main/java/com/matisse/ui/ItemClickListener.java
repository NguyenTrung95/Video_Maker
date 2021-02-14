package com.matisse.ui;

import android.view.View;

public interface ItemClickListener {
    void onItemClick(View view, int i);

    void onItemDeleteClick(View view, int i);
}
