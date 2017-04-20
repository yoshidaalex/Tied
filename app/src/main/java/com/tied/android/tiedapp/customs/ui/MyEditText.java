package com.tied.android.tiedapp.customs.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatEditText;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Femi on 7/20/2016.
 */


public class MyEditText extends AppCompatEditText {

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEditText(Context context) {
        super(context);

    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if(!isEditable) {
            setIsEditable(true);
            super.setText(text, type);
            setIsEditable(false);
        }else{
            setIsEditable(true);
            super.setText(text, type);
        }

    }

    boolean isEditable=true;
    InputFilter[] filter3 = new InputFilter[] { new InputFilter.LengthFilter(200) };
    public void setIsEditable(boolean editable) {
        isEditable=editable;
        if(editable) {
           // setFilters(null);
            setFilters(new InputFilter[] { new InputFilter.LengthFilter(200) });
        }else{
            setLongClickable(false);
            setFocusableInTouchMode(false);
            setClickable(false);
            setOnTouchListener(new OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of child view
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            setFilters(new InputFilter[] {
                    new InputFilter() {
                        public CharSequence filter(CharSequence src, int start,
                                                   int end, Spanned dst, int dstart, int dend) {
                            return src.length() < 1 ? dst.subSequence(dstart, dend) : "";
                        }
                    }
            });
        }
    }



}
