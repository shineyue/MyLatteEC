package com.mymxhbyf.dongjk.lattecore.ui.recycler;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by DongJK on 2018/1/30.
 */

public class MultipleViewHolder extends BaseViewHolder{
    public MultipleViewHolder(View view) {
        super(view);
    }

    public static MultipleViewHolder create(View view){
        return new MultipleViewHolder(view);
    }

}
