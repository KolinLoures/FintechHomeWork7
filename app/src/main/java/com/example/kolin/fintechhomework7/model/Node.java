package com.example.kolin.fintechhomework7.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by kolin on 12.11.2017.
 */

public class Node implements Parcelable {
    private long id;
    private int value;
    private List<Node> nodes;

    public Node(long id, int value, List<Node> nodes) {
        this.id = id;
        this.value = value;
        this.nodes = nodes;
    }

    protected Node(Parcel in) {
        id = in.readLong();
        value = in.readInt();
    }

    public static final Creator<Node> CREATOR = new Creator<Node>() {
        @Override
        public Node createFromParcel(Parcel in) {
            return new Node(in);
        }

        @Override
        public Node[] newArray(int size) {
            return new Node[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value=" + value +
                ", nodes=" + nodes +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeInt(value);
    }
}
