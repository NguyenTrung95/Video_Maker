package com.devchie.photoeditor.fragment.Overplay;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.devchie.videomaker.R;
import com.devchie.photoeditor.interfaces.OverlayListener;

public class SayingsFragment extends BottomSheetDialogFragment {

    public OverlayListener listener;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        public void onSlide(View view, float f) {
        }

        public void onStateChanged(View view, int i) {
            if (i == 5) {
                SayingsFragment.this.dismiss();
            }
        }
    };

    public void setOverlayListener(OverlayListener overlayListener) {
        this.listener = overlayListener;
    }

    @SuppressLint({"RestrictedApi", "ResourceType"})
    public void setupDialog(Dialog dialog, int i) {
        SayingsFragment.super.setupDialog(dialog, i);
        View inflate = View.inflate(getContext(), R.layout.fragment_bottom_sticker_emoji_dialog, (ViewGroup) null);
        dialog.setContentView(inflate);

        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) inflate.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        ((View) inflate.getParent()).setBackgroundColor(getResources().getColor(17170445));
        RecyclerView findViewById = inflate.findViewById(R.id.rvEmoji);
        findViewById.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        findViewById.setAdapter(new SayAdapter());
    }

    public class SayAdapter extends RecyclerView.Adapter<SayAdapter.ViewHolder> {
        int[] sayList = {R.drawable.saying, R.drawable.saying0, R.drawable.saying1, R.drawable.saying2, R.drawable.saying3, R.drawable.saying4, R.drawable.saying5, R.drawable.saying6, R.drawable.saying7, R.drawable.saying8, R.drawable.saying9, R.drawable.saying10, R.drawable.saying11, R.drawable.saying12, R.drawable.saying13, R.drawable.saying14, R.drawable.saying15, R.drawable.saying16, R.drawable.saying17, R.drawable.saying18, R.drawable.saying19, R.drawable.saying20, R.drawable.saying21, R.drawable.saying22, R.drawable.saying23, R.drawable.saying24, R.drawable.saying25, R.drawable.saying26, R.drawable.saying27, R.drawable.saying28, R.drawable.saying29, R.drawable.saying30, R.drawable.saying31, R.drawable.saying32, R.drawable.saying33, R.drawable.saying34, R.drawable.saying35, R.drawable.saying36, R.drawable.saying37, R.drawable.saying38};

        public SayAdapter() {
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_sticker, viewGroup, false));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            Glide.with(SayingsFragment.this.getActivity()).load(Integer.valueOf(this.sayList[i])).thumbnail(0.1f).into(viewHolder.imgSticker);
        }

        public int getItemCount() {
            return this.sayList.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgSticker;

            ViewHolder(View view) {
                super(view);
                this.imgSticker =  view.findViewById(R.id.imgSticker);
                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (SayingsFragment.this.listener != null) {
                            SayingsFragment.this.listener.onOverplayClick(BitmapFactory.decodeResource(SayingsFragment.this.getResources(), SayAdapter.this.sayList[ViewHolder.this.getLayoutPosition()]));
                        }
                        SayingsFragment.this.dismiss();
                    }
                });
            }
        }
    }
}
