package com.example.kolin.fintechhomework7.ui;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.kolin.fintechhomework7.R;
import com.example.kolin.fintechhomework7.db.query.NodeDAO;
import com.example.kolin.fintechhomework7.db.query.Queries;
import com.example.kolin.fintechhomework7.ui.adapters.AdapterNodesRV;
import com.example.kolin.fintechhomework7.ui.dialog.CreateDialog;
import com.example.kolin.fintechhomework7.ui.parentchild.ChildParentActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity
        implements CreateDialog.CreateDialogCallback {


    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private AdapterNodesRV adapter;
    private NodeDAO queries;

    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new AdapterNodesRV();
        queries = Queries.getInstance();
        compositeDisposable = new CompositeDisposable();


        recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adapter);

        fab = findViewById(R.id.main_activity_floating_button_add);
        fab.setOnClickListener(view -> openDialog());

        initAdapterCallback();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.clear();
        loadNodes();
    }

    private void initAdapterCallback() {
        adapter.setCallback(id -> ChildParentActivity.start(MainActivity.this, id));
    }

    private void openDialog() {
        CreateDialog dialogFragment = CreateDialog.newInstance();
        dialogFragment.show(getSupportFragmentManager(), CreateDialog.TAG);
    }

    private void loadNodes() {
        Disposable subscribe =
                queries
                        .getNodes()
                        .subscribe(nodes -> adapter.addAll(nodes));

        compositeDisposable.add(subscribe);
    }

    @Override
    public void onClickPositiveBtn(int value) {
        Disposable subscribe =
                queries
                        .addNode(adapter.getItemCount(), value)
                        .subscribe(node -> adapter.add(node));
        compositeDisposable.add(subscribe);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}
