package com.yw.platform.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class AlwaysMarqueeTextView extends TextView {

	public AlwaysMarqueeTextView(Context context) {
	super(context);
	}

	public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
	super(context, attrs);
	}

	public AlwaysMarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	}
	@Override
	protected void onFocusChanged(boolean focused, int direction,
	Rect previouslyFocusedRect) {
	// TODO Auto-generated method stub
	if(focused)
	super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
	// TODO Auto-generated method stub
	if(hasWindowFocus)
	super.onWindowFocusChanged(hasWindowFocus);
	}



	@Override
	public boolean isFocused() {
	return true;
	}
}
