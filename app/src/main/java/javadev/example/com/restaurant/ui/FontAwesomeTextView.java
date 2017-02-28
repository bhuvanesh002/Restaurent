package javadev.example.com.restaurant.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import javadev.example.com.restaurant.R;
import javadev.example.com.restaurant.util.FontUtil;

/**
 * Created by earul on 4/28/15.
 */
public class FontAwesomeTextView extends TextView {

    String symbol, text;
    float symbolRelativeSize;

    public FontAwesomeTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public FontAwesomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public FontAwesomeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FontAwesomeTextView);
            symbol = a.getString(R.styleable.FontAwesomeTextView_symbol);
            text = a.getString(R.styleable.FontAwesomeTextView_android_text);
            symbolRelativeSize = a.getFloat(R.styleable.FontAwesomeTextView_symbolRelativeSize, 1.0f);
            if (symbol == null) symbol = "";
            if (text == null) text = "";

            a.recycle();

            refresh();
        }
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
        refresh();
    }

    public String getLabel() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        refresh();
    }

    private void refresh() {
        if (!TextUtils.isEmpty(symbol)) {
            if (text == null) {
                text = "";
            }
            Spannable spannable = new SpannableString(symbol + " " + text);
            spannable.setSpan(new FontAwesomeTypefaceSpan("sans-serif", FontUtil.getTypeface(getContext(), SmartAppConstants.FONT_AWESOME_FONT_NAME)), 0, symbol.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new RelativeSizeSpan(symbolRelativeSize), 0, symbol.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new TypefaceSpan("sans-serif"), symbol.length(), symbol.length() + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            setText(spannable);
        }
    }

    public float getSymbolRelativeSize() {
        return symbolRelativeSize;
    }

    public void setSymbolRelativeSize(float symbolRelativeSize) {
        this.symbolRelativeSize = symbolRelativeSize;
    }

    public class FontAwesomeTypefaceSpan extends TypefaceSpan {
        private final Typeface newType;

        public FontAwesomeTypefaceSpan(String family, Typeface type) {
            super(family);
            newType = type;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            applyFontAwesomeTypeFace(ds, newType);
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            applyFontAwesomeTypeFace(paint, newType);
        }

        private void applyFontAwesomeTypeFace(Paint paint, Typeface tf) {
            paint.setTypeface(tf);
        }
    }

    public void setDrawableTop(String icon, Context context, int color) {
        TextDrawable d = new TextDrawable(context);
        d.setText(Html.fromHtml(icon));
        d.setTextSize(16);
        d.setTextColor(color);
        setTextColor(color);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
    }

}

