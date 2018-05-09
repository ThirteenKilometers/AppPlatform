package com.yw.platform.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yw.platform.R;


public class PageControlView extends LinearLayout {
    private Context context;

    private int count;

    public void bindViewGroup(int count) {
        this.count = count;
        System.out.println("count=" + count);
        generatePageControl(0);
    }

    public PageControlView(Context context) {
        super(context);
        this.init(context);
    }

    public PageControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    private void init(Context context) {
        this.context = context;
    }

    public void generatePageControl(int currentIndex) {
        this.removeAllViews();

        int pageNum = 6;
        int pageNo = currentIndex + 1;
        int pageSum = this.count;

        if (pageSum >= 1) {
            int currentNum = (pageNo % pageNum == 0 ? (pageNo / pageNum) - 1
                    : (int) (pageNo / pageNum))
                    * pageNum;

            if (currentNum < 0)
                currentNum = 0;

            if (pageNo > pageNum) {
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(R.drawable.page_indicator_left);
                this.addView(imageView);
            }

            for (int i = 0; i < pageNum; i++) {
                if ((currentNum + i + 1) > pageSum || pageSum < 1)
                    break;

                ImageView imageView = new ImageView(context);
                if (currentNum + i + 1 == pageNo) {
                    imageView.setImageResource(R.drawable.page_indicator_focused);
                } else {
                    imageView.setImageResource(R.drawable.page_indicator);
                }
                this.addView(imageView);
            }

            if (pageSum > (currentNum + pageNum)) {
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(R.drawable.page_indicator_right);
                this.addView(imageView);
            }
        }
    }
}
