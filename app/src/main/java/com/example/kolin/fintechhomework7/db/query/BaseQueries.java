package com.example.kolin.fintechhomework7.db.query;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Base Query
 */

public class BaseQueries {

    /**
     * Add necessary schedulers to {@link io.reactivex.Single}
     * @param source SingleSource
     * @return Single with schedulers
     */
    protected Single addSchedulers(Single source){
        return source
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
