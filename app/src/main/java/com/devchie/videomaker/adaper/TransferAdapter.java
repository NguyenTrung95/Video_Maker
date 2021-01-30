package com.devchie.videomaker.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.devchie.videomaker.R;
import com.devchie.videomaker.model.TransferItem;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;

public class TransferAdapter extends RecyclerView.Adapter<TransferAdapter.ViewHolderTransfer> {
    TransferAdapterListener listener;
    Context mContext;
    int row_selected = 1;
    ArrayList<TransferItem> transferItemArrayList;

    public interface TransferAdapterListener {
        void onTransferSelected(TransferItem transferItem);
    }

    public TransferAdapter(ArrayList<TransferItem> arrayList, Context context, TransferAdapterListener transferAdapterListener) {
        this.transferItemArrayList = arrayList;
        this.mContext = context;
        this.listener = transferAdapterListener;
    }

    public ViewHolderTransfer onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolderTransfer(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_transfer, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolderTransfer viewHolderTransfer, int i) {
        if (this.row_selected == i) {
            viewHolderTransfer.imgTransfer.setImageResource(this.transferItemArrayList.get(i).getImgRes());
            viewHolderTransfer.imgTransfer.setBorderWidth(4);
            viewHolderTransfer.imgTransfer.setBorderColor(this.mContext.getResources().getColor(R.color.selected_border));
            viewHolderTransfer.nameTransfer.setText(this.transferItemArrayList.get(i).getName());
            viewHolderTransfer.nameTransfer.setTextColor(this.mContext.getResources().getColor(R.color.selected_border));
            return;
        }
        viewHolderTransfer.imgTransfer.setImageResource(this.transferItemArrayList.get(i).getImgRes());
        viewHolderTransfer.imgTransfer.setBorderWidth(2);
        viewHolderTransfer.imgTransfer.setBorderColor(this.mContext.getResources().getColor(17170444));
        viewHolderTransfer.nameTransfer.setText(this.transferItemArrayList.get(i).getName());
        viewHolderTransfer.nameTransfer.setTextColor(this.mContext.getResources().getColor(R.color.un_selected_white));
    }

    public int getItemCount() {
        return this.transferItemArrayList.size();
    }

    public class ViewHolderTransfer extends RecyclerView.ViewHolder {
        CircleImageView imgTransfer;
        TextView nameTransfer;

        public ViewHolderTransfer(View view) {
            super(view);
            this.imgTransfer = (CircleImageView) view.findViewById(R.id.transfer_img);
            this.nameTransfer = (TextView) view.findViewById(R.id.transfer_txt);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (ViewHolderTransfer.this.getAdapterPosition() < TransferAdapter.this.transferItemArrayList.size()) {
                        TransferAdapter.this.listener.onTransferSelected(TransferAdapter.this.transferItemArrayList.get(ViewHolderTransfer.this.getAdapterPosition()));
                        TransferAdapter.this.row_selected = ViewHolderTransfer.this.getAdapterPosition();
                        TransferAdapter.this.notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
