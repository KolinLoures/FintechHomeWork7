package com.example.kolin.fintechhomework7.db.query;

import com.example.kolin.fintechhomework7.db.tables.ParentNodeTable;
import com.example.kolin.fintechhomework7.model.Node;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by kolin on 12.11.2017.
 */

public interface NodeDAO {

    /**
     * Get all nodes in data base
     * @return list of {@link Node}
     */
    Single<List<Node>> getNodes();

    /**
     * Add {@link Node} to Data base, and then return it
     * @param afterId after that was added, need to get Node
     * @param value value of Node
     * @return Added node
     */
    Single<Node> addNode(long afterId, int value);

    /**
     * Return parents references. Closer look {@link ParentNodeTable#selectParentsReferences(long)}
     *
     * @param node entity
     * @return Node with parents references
     */
    Single<Node> getParentsReferences(Node node);


    /**
     * Return child references. Closer look {@link ParentNodeTable#selectChildrenReferences(long)}
     *
     * @param node entity
     * @return Node with children references
     */
    Single<Node> getChildrenReferences(Node node);

    /**
     * Add new reference between child and parent
     *
     * @param idChild ID child
     * @param idParent ID parent
     * @return Completable source
     */
    Completable addReference(long idChild, long idParent);

    /**
     * Delete reference between child and parent
     *
     * @param idChild ID child
     * @param idParent ID parent
     * @return Completable source
     */
    Completable removeReference(long idChild, long idParent);
}
