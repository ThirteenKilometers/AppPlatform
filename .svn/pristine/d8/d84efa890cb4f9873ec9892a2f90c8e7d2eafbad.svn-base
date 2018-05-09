/**   
 * Copyright © 2016 GW-SES. All rights reserved.
 * @Prject: AppPlatform
 */
 
package com.yw.platform.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class JZADScoreTextView extends TextView{  
    
    
    public JZADScoreTextView(Context context) {  
        super(context);  
    }  
       
    public JZADScoreTextView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
   
    @Override  
    protected void onDraw(Canvas canvas) {  
        //倾斜度45,上下左右居中  
        canvas.rotate(-45, getMeasuredWidth()/2, getMeasuredHeight()/2);  
        super.onDraw(canvas);  
    }  
           
}  
