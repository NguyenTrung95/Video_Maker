package com.devchie.photoeditor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.devchie.videomaker.R;
import com.devchie.photoeditor.photoeditor.PhotoEditor;
import java.util.ArrayList;

public class EmojiFragment extends Fragment {

    public EmojiListener mEmojiListener;
    RecyclerView recyclerView;

    public interface EmojiListener {
        void onEmojiClick(String str);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_image_emoji, viewGroup, false);
        this.recyclerView = inflate.findViewById(R.id.recyclerEmoji);
        this.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        this.recyclerView.setAdapter(new EmojiAdapter());
        return inflate;
    }

    public void setEmojiListener(EmojiListener emojiListener) {
        this.mEmojiListener = emojiListener;
    }

    public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {
        ArrayList<String> emojiList = PhotoEditor.getEmojis(EmojiFragment.this.getActivity());

        public EmojiAdapter() {
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_emoji, viewGroup, false));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.txtEmoji.setText(this.emojiList.get(i));
        }

        public int getItemCount() {
            return this.emojiList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtEmoji;

            public ViewHolder(View view) {
                super(view);
                this.txtEmoji =  view.findViewById(R.id.txtEmoji);
                view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (EmojiFragment.this.mEmojiListener != null) {
                            EmojiFragment.this.mEmojiListener.onEmojiClick(EmojiAdapter.this.emojiList.get(ViewHolder.this.getLayoutPosition()));
                        }
                    }
                });
            }
        }
    }
}
