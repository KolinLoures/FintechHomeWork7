package com.example.kolin.fintechhomework7.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.kolin.fintechhomework7.R;
import com.example.kolin.fintechhomework7.model.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolin on 14.11.2017.
 */

public class AdapterChildParentRV extends RecyclerView.Adapter<AdapterVH> {

    private List<Node> data;

    private int holo_green_dark;
    private int transparent;

    private AdapterChildParentCallback callback;

    public interface AdapterChildParentCallback {
        void onClickNode(Node node);
    }

    public AdapterChildParentRV() {
        this.data = new ArrayList<>();
        holo_green_dark = android.R.color.holo_green_dark;
        transparent = android.R.color.transparent;
    }

    @Override
    public AdapterVH onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterVH adapterVH = new AdapterVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter, parent, false));
        adapterVH.setOnClick(position -> {
            if (callback != null)
                callback.onClickNode(data.get(position));
        });
        return adapterVH;
    }

    @Override
    public void onBindViewHolder(AdapterVH holder, int position) {
        Node current = data.get(position);

        holder.textView.setText(String.format("ID: %d | VALUE: %d", current.getId(), current.getValue()));

        int color = current.getNodes() != null ? holo_green_dark : transparent;

        holder.textView.setBackgroundResource(color);
    }

    public void addAll(List<Node> data){
        int oldSize = this.data.size();
        this.data.addAll(data);
        notifyItemRangeInserted(oldSize, data.size());
    }

    public void updateRelation(long id){
        for (int i = 0; i < data.size(); i++)
            if (data.get(i).getId() == id){
                data.get(i).setNodes(new ArrayList<>());
                notifyItemChanged(i);
                return;
            }
    }

    public void removeRelation(long id){
        for (int i = 0; i < data.size(); i++)
            if (data.get(i).getId() == id){
                data.get(i).setNodes(null);
                notifyItemChanged(i);
                return;
            }
    }

    public void clear(){
        int size = this.data.size();
        this.data.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void removeItem(Node node){
        int i = this.data.indexOf(node);
        this.data.remove(node);
        notifyItemRemoved(i);
    }

    public void add(Node node){
        this.data.add(node);
        notifyItemInserted(data.size());
    }

    public Node getItemById(long id){
        for (Node n: data)
            if (n.getId() == id)
                return n;
        return null;
    }

    public void setCallback(AdapterChildParentCallback callback) {
        this.callback = callback;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
