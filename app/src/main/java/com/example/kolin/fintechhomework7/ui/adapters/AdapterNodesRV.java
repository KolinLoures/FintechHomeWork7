package com.example.kolin.fintechhomework7.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.kolin.fintechhomework7.R;
import com.example.kolin.fintechhomework7.model.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolin on 12.11.2017.
 */

public class AdapterNodesRV extends RecyclerView.Adapter<AdapterVH> {


    private List<Node> data;

    private AdapterNodesRVCallback callback;

    private int holo_blue_dark ;
    private int transparent;
    private int holo_red_dark;
    private int holo_orange_light ;

    public AdapterNodesRV() {
        data = new ArrayList<>();
        holo_blue_dark = android.R.color.holo_blue_dark;
        transparent = android.R.color.transparent;
        holo_red_dark = android.R.color.holo_red_dark;
        holo_orange_light = android.R.color.holo_orange_light;
    }

    public interface AdapterNodesRVCallback {
        void onClickNode(Node node);
    }

    @Override
    public AdapterVH onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterVH adapterVH = new AdapterVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter, parent, false));
        adapterVH.setOnClick(position -> {
            if (callback != null) {
                Node node = data.get(position);
                node.setNodes(null);
                callback.onClickNode(node);
            }
        });
        return adapterVH;
    }

    @Override
    public void onBindViewHolder(AdapterVH holder, int position) {
        Node current = data.get(position);
        List<Node> nodes = current.getNodes();
        int hasChild = 0;
        int hasParent = 0;

        holder.textView.setText(String.format("ID: %d | VALUE: %d", current.getId(), current.getValue()));

        if (nodes != null) {
             hasParent = nodes.get(0).getValue();
             hasChild = nodes.get(1).getValue();
        }

        int color =
                hasParent == 1 && hasChild == 1 ? holo_red_dark :
                        hasParent == 0 && hasChild == 1 ? holo_orange_light :
                                hasParent == 1 && hasChild == 0 ? holo_blue_dark :
                                        transparent;

        holder.textView.setBackgroundResource(color);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addAll(List<Node> data) {
        int oldSize = this.data.size();
        this.data.addAll(data);
        notifyItemRangeInserted(oldSize, this.data.size());
    }

    public void clear(){
        int oldSize = this.data.size();
        this.data.clear();
        notifyItemRangeRemoved(0, oldSize);
    }

    public void add(Node node) {
        this.data.add(node);
        notifyItemInserted(this.data.size() - 1);
    }

    public void setCallback(AdapterNodesRVCallback callback) {
        this.callback = callback;
    }
}
