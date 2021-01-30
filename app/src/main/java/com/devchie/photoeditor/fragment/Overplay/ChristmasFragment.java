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

public class ChristmasFragment extends BottomSheetDialogFragment {

    public OverlayListener listener;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        public void onSlide(View view, float f) {
        }

        public void onStateChanged(View view, int i) {
            if (i == 5) {
                ChristmasFragment.this.dismiss();
            }
        }
    };

    public void setOverlayListener(OverlayListener overlayListener) {
        this.listener = overlayListener;
    }

    @SuppressLint({"RestrictedApi", "ResourceType"})
    public void setupDialog(Dialog dialog, int i) {
        ChristmasFragment.super.setupDialog(dialog, i);
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
        findViewById.setAdapter(new ChristmasAdapter());
    }

    public class ChristmasAdapter extends RecyclerView.Adapter<ChristmasAdapter.ViewHolder> {
        int[] christmasList = {R.drawable.christmas0, R.drawable.christmas1, R.drawable.christmas2, R.drawable.christmas3, R.drawable.christmas4, R.drawable.christmas5, R.drawable.christmas6, R.drawable.christmas7, R.drawable.christmas8, R.drawable.christmas9, R.drawable.christmas10, R.drawable.christmas11, R.drawable.christmas12, R.drawable.christmas13, R.drawable.christmas14, R.drawable.christmas15, R.drawable.christmas16, R.drawable.christmas17, R.drawable.christmas18, R.drawable.christmas19, R.drawable.christmas20, R.drawable.christmas21, R.drawable.christmas22, R.drawable.christmas23, R.drawable.christmas24, R.drawable.christmas25, R.drawable.christmas26, R.drawable.christmas27, R.drawable.christmas28, R.drawable.christmas29, R.drawable.christmas30};

        public ChristmasAdapter() {
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_sticker, viewGroup, false));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            Glide.with(ChristmasFragment.this.getActivity()).load(Integer.valueOf(this.christmasList[i])).thumbnail(0.1f).into(viewHolder.imgSticker);
        }

        public int getItemCount() {
            return this.christmasList.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgSticker;

            ViewHolder(View view) {
                super(view);
                this.imgSticker =  view.findViewById(R.id.imgSticker);
                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (ChristmasFragment.this.listener != null) {
                            ChristmasFragment.this.listener.onOverplayClick(BitmapFactory.decodeResource(ChristmasFragment.this.getResources(), ChristmasAdapter.this.christmasList[ViewHolder.this.getLayoutPosition()]));
                        }
                        ChristmasFragment.this.dismiss();
                    }
                });
            }
        }
    }
}
