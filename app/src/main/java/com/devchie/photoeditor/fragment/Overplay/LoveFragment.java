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

public class LoveFragment extends BottomSheetDialogFragment {

    public OverlayListener listener;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        public void onSlide(View view, float f) {
        }

        public void onStateChanged(View view, int i) {
            if (i == 5) {
                LoveFragment.this.dismiss();
            }
        }
    };

    public void setOverlayListener(OverlayListener overlayListener) {
        this.listener = overlayListener;
    }

    @SuppressLint({"RestrictedApi", "ResourceType"})
    public void setupDialog(Dialog dialog, int i) {
        LoveFragment.super.setupDialog(dialog, i);
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
        findViewById.setAdapter(new LoveAdapter());
    }

    public class LoveAdapter extends RecyclerView.Adapter<LoveAdapter.ViewHolder> {
        int[] loveList = {R.drawable.love_photo, R.drawable.love0, R.drawable.love1, R.drawable.love2, R.drawable.love3, R.drawable.love4, R.drawable.love5, R.drawable.love6, R.drawable.love7, R.drawable.love8, R.drawable.love9, R.drawable.love10, R.drawable.love11, R.drawable.love12, R.drawable.love13, R.drawable.love14, R.drawable.love15, R.drawable.love16, R.drawable.love17, R.drawable.love18, R.drawable.love19, R.drawable.love20, R.drawable.love21, R.drawable.love22, R.drawable.love23, R.drawable.love24, R.drawable.love25, R.drawable.love26, R.drawable.love27, R.drawable.love28, R.drawable.love29, R.drawable.love30, R.drawable.love31, R.drawable.love32, R.drawable.love33, R.drawable.love34, R.drawable.love35, R.drawable.love36, R.drawable.love37, R.drawable.love38, R.drawable.love39, R.drawable.love40, R.drawable.love41, R.drawable.love42};

        public LoveAdapter() {
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_sticker, viewGroup, false));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            Glide.with(LoveFragment.this.getActivity()).load(Integer.valueOf(this.loveList[i])).thumbnail(0.1f).into(viewHolder.imgSticker);
        }

        public int getItemCount() {
            return this.loveList.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgSticker;

            ViewHolder(View view) {
                super(view);
                this.imgSticker =  view.findViewById(R.id.imgSticker);
                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (LoveFragment.this.listener != null) {
                            LoveFragment.this.listener.onOverplayClick(BitmapFactory.decodeResource(LoveFragment.this.getResources(), LoveAdapter.this.loveList[ViewHolder.this.getLayoutPosition()]));
                        }
                        LoveFragment.this.dismiss();
                    }
                });
            }
        }
    }
}
