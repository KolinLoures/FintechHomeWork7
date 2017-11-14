package com.example.kolin.fintechhomework7.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.kolin.fintechhomework7.R;

/**
 * {@link AdapterVH} Common ViewHolder for {@link AdapterChildParentRV} and {@link AdapterNodesRV}
 */

public class AdapterVH extends RecyclerView.ViewHolder {

    protected TextView textView;

    private AdapterClick listener;

    public interface AdapterClick{
        void onClick(int position);
    }

    public AdapterVH(View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.adapter_item_text_view);
        itemView.setOnClickListener(view -> {
            if (listener != null)
                listener.onClick(getAdapterPosition());
        });
    }

    public void setOnClick(AdapterClick listener){
        this.listener = listener;
    }
}
