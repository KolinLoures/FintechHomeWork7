package com.example.kolin.fintechhomework7.ui.parentchild;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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


public class ParentFragment extends Fragment implements AskDialog.AskDialogCallback, Updatable {

    private static final String ARG_ID = "entity";
    private static final String TAG = ParentFragment.class.getSimpleName();

    private Node currentNode;
    private NodeDAO queries;
    private AdapterChildParentRV adapter;

    private RecyclerView recyclerView;

    private CompositeDisposable compositeDisposable;

    private ParentFragmentListener listener;

    public ParentFragment() {
        // Required empty public constructor
    }

    public static ParentFragment newInstance(Node node) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ID, node);
        ParentFragment fragment = new ParentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface ParentFragmentListener {
        void parentReferenceChanged();
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
        return inflater.inflate(R.layout.fragment_parent, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setCallback(this::openDialog);

        loadParents();
    }

    private void openDialog(Node node) {
        AskDialog dialog = null;
        if (node.getNodes() != null)
            dialog = AskDialog.newInstance(1, node.getId());
        else
            dialog = AskDialog.newInstance(0, node.getId());

        dialog.show(getChildFragmentManager(), AskDialog.TAG);
    }

    private void loadParents() {
        Disposable subscribe = queries.getParentsReferences(currentNode).subscribe(node -> {
            adapter.addAll(node.getNodes());
        });

        compositeDisposable.add(subscribe);
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        compositeDisposable.dispose();
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ParentFragmentListener)
            listener = (ParentFragmentListener) context;
        else
            throw new RuntimeException(context.toString() + " must implement ParentFragmentListener");
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    @Override
    public void onClickPositiveBtn(int type, long id) {
        if (type == 0) {
            adapter.updateRelation(id);
            Disposable add = queries.addReference(currentNode.getId(), id).subscribe(() -> {
                Snackbar.make(getView(), "ADDED", Snackbar.LENGTH_LONG).show();
                listener.parentReferenceChanged();
            });
            compositeDisposable.add(add);
        } else {
            adapter.removeRelation(id);
            Disposable remove = queries.removeReference(currentNode.getId(), id).subscribe(() -> {
                Snackbar.make(getView(), "REMOVED", Snackbar.LENGTH_LONG).show();
                listener.parentReferenceChanged();
            });
            compositeDisposable.add(remove);
        }
    }

    @Override
    public void update() {
        adapter.clear();
        loadParents();
    }
}
