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

public class MotivationFragment extends BottomSheetDialogFragment {

    public OverlayListener listener;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        public void onSlide(View view, float f) {
        }

        public void onStateChanged(View view, int i) {
            if (i == 5) {
                MotivationFragment.this.dismiss();
            }
        }
    };

    public void setOverlayListener(OverlayListener overlayListener) {
        this.listener = overlayListener;
    }

    @SuppressLint({"RestrictedApi", "ResourceType"})
    public void setupDialog(Dialog dialog, int i) {
        MotivationFragment.super.setupDialog(dialog, i);
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
        findViewById.setAdapter(new MotivationAdapter());
    }

    public class MotivationAdapter extends RecyclerView.Adapter<MotivationAdapter.ViewHolder> {
        int[] motivationList = {R.drawable.motivation, R.drawable.motivation0, R.drawable.motivation1, R.drawable.motivation2, R.drawable.motivation3, R.drawable.motivation4, R.drawable.motivation5, R.drawable.motivation6, R.drawable.motivation7, R.drawable.motivation8, R.drawable.motivation9, R.drawable.motivation10, R.drawable.motivation11, R.drawable.motivation12, R.drawable.motivation13, R.drawable.motivation14, R.drawable.motivation15, R.drawable.motivation16, R.drawable.motivation17, R.drawable.motivation18, R.drawable.motivation19, R.drawable.motivation20, R.drawable.motivation21, R.drawable.motivation22, R.drawable.motivation23, R.drawable.motivation24, R.drawable.motivation25, R.drawable.motivation26, R.drawable.motivation27, R.drawable.motivation28, R.drawable.motivation29, R.drawable.motivation30, R.drawable.motivation31, R.drawable.motivation32, R.drawable.motivation33, R.drawable.motivation34, R.drawable.motivation35, R.drawable.motivation36, R.drawable.motivation37};

        public MotivationAdapter() {
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_sticker, viewGroup, false));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            Glide.with(MotivationFragment.this.getActivity()).load(Integer.valueOf(this.motivationList[i])).thumbnail(0.1f).into(viewHolder.imgSticker);
        }

        public int getItemCount() {
            return this.motivationList.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgSticker;

            ViewHolder(View view) {
                super(view);
                this.imgSticker =  view.findViewById(R.id.imgSticker);
                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (MotivationFragment.this.listener != null) {
                            MotivationFragment.this.listener.onOverplayClick(BitmapFactory.decodeResource(MotivationFragment.this.getResources(), MotivationAdapter.this.motivationList[ViewHolder.this.getLayoutPosition()]));
                        }
                        MotivationFragment.this.dismiss();
                    }
                });
            }
        }
    }
}
