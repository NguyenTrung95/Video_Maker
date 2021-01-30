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

public class PhrasesFragment extends BottomSheetDialogFragment {

    public OverlayListener listener;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        public void onSlide(View view, float f) {
        }

        public void onStateChanged(View view, int i) {
            if (i == 5) {
                PhrasesFragment.this.dismiss();
            }
        }
    };

    public void setOverlayListener(OverlayListener overlayListener) {
        this.listener = overlayListener;
    }

    @SuppressLint({"RestrictedApi", "ResourceType"})
    public void setupDialog(Dialog dialog, int i) {
        PhrasesFragment.super.setupDialog(dialog, i);
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
        findViewById.setAdapter(new PhrasesAdapter());
    }

    public class PhrasesAdapter extends RecyclerView.Adapter<PhrasesAdapter.ViewHolder> {
        int[] phrasesList = {R.drawable.phrase, R.drawable.phrase0, R.drawable.phrase1, R.drawable.phrase2, R.drawable.phrase3, R.drawable.phrase4, R.drawable.phrase5, R.drawable.phrase6, R.drawable.phrase7, R.drawable.phrase8, R.drawable.phrase9, R.drawable.phrase10, R.drawable.phrase11, R.drawable.phrase12, R.drawable.phrase13, R.drawable.phrase14, R.drawable.phrase15, R.drawable.phrase16, R.drawable.phrase17, R.drawable.phrase18, R.drawable.phrase19, R.drawable.phrase20, R.drawable.phrase21, R.drawable.phrase22, R.drawable.phrase23, R.drawable.phrase24, R.drawable.phrase25, R.drawable.phrase26, R.drawable.phrase27, R.drawable.phrase28, R.drawable.phrase29, R.drawable.phrase30, R.drawable.phrase31, R.drawable.phrase32, R.drawable.phrase33, R.drawable.phrase34, R.drawable.phrase35, R.drawable.phrase36, R.drawable.phrase37, R.drawable.phrase38, R.drawable.phrase39};

        public PhrasesAdapter() {
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_sticker, viewGroup, false));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            Glide.with(PhrasesFragment.this.getActivity()).load(Integer.valueOf(this.phrasesList[i])).thumbnail(0.1f).into(viewHolder.imgSticker);
        }

        public int getItemCount() {
            return this.phrasesList.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgSticker;

            ViewHolder(View view) {
                super(view);
                this.imgSticker =  view.findViewById(R.id.imgSticker);
                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (PhrasesFragment.this.listener != null) {
                            PhrasesFragment.this.listener.onOverplayClick(BitmapFactory.decodeResource(PhrasesFragment.this.getResources(), PhrasesAdapter.this.phrasesList[ViewHolder.this.getLayoutPosition()]));
                        }
                        PhrasesFragment.this.dismiss();
                    }
                });
            }
        }
    }
}
