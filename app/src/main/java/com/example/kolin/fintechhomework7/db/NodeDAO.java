package com.example.kolin.fintechhomework7.db;

import com.example.kolin.fintechhomework7.model.Node;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by kolin on 12.11.2017.
 */

public interface NodeDAO {

    Single<List<Node>> getNodes();

    Completable addNode(int id);

    Single<Boolean> checkNodeIsAdded(int id);

    Single<Node> getParents(int id);

    Single<Node> getChildren(int id);
}
