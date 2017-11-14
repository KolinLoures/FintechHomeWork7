package com.example.kolin.fintechhomework7.ui.parentchild;

/**
 * Updatable interface to communication between {@link ChildFragment} and {@link ParentFragment}
 * Indicate change and force update in one of fragments.
 */

public interface Updatable {
    /**
     * Method to update data
     */
    void update();
}
