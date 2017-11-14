package com.example.kolin.fintechhomework7.ui.parentchild;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kolin.fintechhomework7.R;
import com.example.kolin.fintechhomework7.db.query.NodeDAO;
import com.example.kolin.fintechhomework7.db.query.Queries;
import com.example.kolin.fintechhomework7.model.Node;
import com.example.kolin.fintechhomework7.ui.adapters.AdapterChildParentRV;
import com.example.kolin.fintechhomework7.ui.dialog.AskDialog;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Fragment to show child references
 */
public class ChildFragment extends Fragment
        implements AskDialog.AskDialogCallback, Updatable {


    private static final String ARG_ID = "entity";

    //Current parent entity for children
    private Node currentNode;

    private NodeDAO queries;
    private RecyclerView recyclerView;
    private AdapterChildParentRV adapter;
    private CompositeDisposable compositeDisposable;

    public ChildFragmentListener listener;

    public ChildFragment() {
        // Required empty public constructor
    }

    public static ChildFragment newInstance(Node node) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ID, node);
        ChildFragment fragment = new ChildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Interface class to listen changes in children references
     */
    public interface ChildFragmentListener {
        void childReferenceChange();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queries = Queries.getInstance();
        adapter = new AdapterChildParentRV();
        compositeDisposable = new CompositeDisposable();

        if (getArguments() != null)
            currentNode = getArguments().getParcelable(ARG_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_child, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setCallback(this::openDialog);

        loadChildren();
    }

    /**
     * Open {@link AskDialog }
     * TODO remove node from params
     */
    private void openDialog(Node node) {
        AskDialog dialog = null;
        if (node.getNodes() != null)
            dialog = AskDialog.newInstance(1, node.getId());
        else
            dialog = AskDialog.newInstance(0, node.getId());

        dialog.show(getChildFragmentManager(), AskDialog.TAG);
    }

    private void loadChildren() {
        Disposable subscribe = queries.getChildrenReferences(currentNode).subscribe(node -> adapter.addAll(node.getNodes()));
        compositeDisposable.add(subscribe);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        compositeDisposable.dispose();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChildFragmentListener)
            listener = (ChildFragmentListener) context;
        else
            throw new RuntimeException(context.toString() + " must implement ChildFragmentListener");
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    @Override
    public void onClickPositiveBtn(int type, long id) {
        // type 0 add reference
        if (type == 0) {
            adapter.updateRelation(id);
            //add reference to DB
            Disposable add = queries.addReference(id, currentNode.getId()).subscribe(() -> {
                Snackbar.make(getView(), "ADDED", Snackbar.LENGTH_LONG).show();
                listener.childReferenceChange();
            });

            compositeDisposable.add(add);
        } else {
            adapter.removeRelation(id);
            //add reference to DB
            Disposable remove = queries.removeReference(id, currentNode.getId()).subscribe(() -> {
                Snackbar.make(getView(), "REMOVED", Snackbar.LENGTH_LONG).show();
                listener.childReferenceChange();
            });
            compositeDisposable.add(remove);
        }

    }

    @Override
    public void update() {
        adapter.clear();
        loadChildren();
    }
}
