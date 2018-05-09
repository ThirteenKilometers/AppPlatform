package com.yw.platform.view;

import android.util.SparseArray;
import android.view.View;

/**
 * 版权: 金风科技集团 版权所有(c)2015 
 * 作者: tanger 
 * 创建日期: 2016-2-18 
 * 描述: 万能的viewHolder 
 * 修订历史:
 */
public class BaseViewHolder {
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
