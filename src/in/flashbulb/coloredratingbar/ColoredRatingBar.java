package in.flashbulb.coloredratingbar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by jagadeeshwarank on 24/7/13.
 */
public class ColoredRatingBar extends View {

    private static final String TAG="ColoredRatingBar";
    private static final int NORMAL = 0;
    private static final int SMALL = 1;

    Bitmap[] drawables;
    Bitmap progressBackground;
    Context mContext;
    private int mNumStars =5;
    private float mRating =0;
    private boolean mIndicator;
    private float slidePosition;
    private int mType;

    /**
     * A callback that notifies clients when the rating has been changed. This
     * includes changes that were initiated by the user through a touch gesture
     * or arrow key/trackball as well as changes that were initiated
     * programmatically.
     */
    public interface OnRatingBarChangeListener {

        /**
         * Notification that the rating has changed. Clients can use the
         * fromUser parameter to distinguish user-initiated changes from those
         * that occurred programmatically. This will not be called continuously
         * while the user is dragging, only when the user finalizes a rating by
         * lifting the touch.
         *
         * @param ratingBar The RatingBar whose rating has changed.
         * @param rating The current rating. This will be in the range
         *            0..numStars.
         * @param fromUser True if the rating change was initiated by a user's
         *            touch gesture or arrow key/horizontal trackbell movement.
         */
        void onRatingChanged(ColoredRatingBar ratingBar, float rating, boolean fromUser);

    }

    private OnRatingBarChangeListener mOnRatingBarChangeListener;

    public ColoredRatingBar(Context context) {
        this(context, null);
    }
    public ColoredRatingBar(Context context, AttributeSet attrs) {
        this(context, attrs,R.attr.coloredRatingBarStyle);
    }

    public ColoredRatingBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColoredRatingBar,
                defStyle, 0);
        final boolean indicator = a.getBoolean(R.styleable.ColoredRatingBar_indicator, false);
        final float rating = a.getFloat(R.styleable.ColoredRatingBar_rating, -1);
        final int type = a.getInt(R.styleable.ColoredRatingBar_type, 0);
        a.recycle();

        setIndicator(indicator);
        setRating(rating);
        setType(type);
        init(context);
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    private void init(Context context) {
        mContext = context;
        Resources res = getResources();
        if(mType==SMALL){
            drawables = new Bitmap[]{BitmapFactory.decodeResource(res, R.drawable.star_red_small),BitmapFactory.decodeResource(res, R.drawable.star_orange_small),BitmapFactory.decodeResource(res, R.drawable.star_green_small)};
            progressBackground = BitmapFactory.decodeResource(res, R.drawable.star_black_small);
        }else{
            drawables = new Bitmap[]{BitmapFactory.decodeResource(res, R.drawable.star_red),BitmapFactory.decodeResource(res, R.drawable.star_orange),BitmapFactory.decodeResource(res, R.drawable.star_green)};
            progressBackground = BitmapFactory.decodeResource(res, R.drawable.star_black);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //draw empty stars bg
        for(int i=0;i< mNumStars;i++){
            drawStar(canvas,i);
        }

    }


    private void drawStar(Canvas canvas, int position) {
        float fraction = mRating -(position);
        Bitmap ratedStar = getRatedStar();
        if((position+1)< mRating){
            canvas.drawBitmap(ratedStar,(position* ratedStar.getWidth()),0,null);
        } else{
            if(fraction>0&&fraction<=1){
                int sourceWidth = ratedStar.getWidth();
                int sourceHeight = ratedStar.getHeight();

                int targetWidth =(int) (ratedStar.getWidth()*fraction);
                int bgWidth = sourceWidth-targetWidth;

                if(targetWidth>0){
                    Bitmap croppedBmp = Bitmap.createBitmap(ratedStar, 0, 0, targetWidth, sourceHeight);
                    canvas.drawBitmap(croppedBmp,(position*sourceWidth),0,null);
//                    croppedBmp.recycle();
                }
                if(bgWidth>0){
                    Bitmap croppedBg = Bitmap.createBitmap(progressBackground, targetWidth, 0, bgWidth, sourceHeight);
                    canvas.drawBitmap(croppedBg,(position*sourceWidth)+targetWidth,0,null);
//                    croppedBg.recycle();
                }
            }else{
                canvas.drawBitmap(progressBackground,(position*progressBackground.getWidth()),0,null);
            }
        }


    }

    private Bitmap getRatedStar() {
        if(mRating <=1.6f){
            return drawables[0];
        }else if(mRating <=3.2f){
            return drawables[1];
        }else {
            return drawables[2];
        }
    }

    public int getNumStars() {
        return mNumStars;
    }

    public void setNumStars(int numStars) {
        this.mNumStars = numStars;
    }

    public float getRating() {
        return mRating;
    }

    public void setRating(float rating) {
        setRating(rating,false);
    }

    void setRating(float rating,boolean fromUser) {
        if(rating>mNumStars){
            this.mRating = mNumStars;
        }
        this.mRating = rating;
        invalidate();
        dispatchRatingChange(fromUser);
    }

    public boolean isIndicator() {
        return mIndicator;
    }

    public void setIndicator(boolean indicator) {
        this.mIndicator = indicator;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (progressBackground != null) {

            final int width = progressBackground.getWidth() * mNumStars;
            final int height = progressBackground.getHeight();
            setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, 0),
                    resolveSizeAndState(height, heightMeasureSpec, 0));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mIndicator){
            return false;
        }

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                slidePosition = getRelativePosition(event.getX());
                int newRating = (int) slidePosition + 1;
                if (newRating != mRating) {
                    setRating(newRating,true);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }

        return true;
    }

    private float getRelativePosition(float x) {
        float position = x / progressBackground.getWidth();
        position = Math.max(position, 0);
        return Math.min(position, mNumStars - 1);
    }

    /**
     * Sets the listener to be called when the rating changes.
     *
     * @param listener The listener.
     */
    public void setOnRatingBarChangeListener(OnRatingBarChangeListener listener) {
        mOnRatingBarChangeListener = listener;
    }

    /**
     * @return The listener (may be null) that is listening for rating change
     *         events.
     */
    public OnRatingBarChangeListener getOnRatingBarChangeListener() {
        return mOnRatingBarChangeListener;
    }

    void dispatchRatingChange(boolean fromUser) {
        if (mOnRatingBarChangeListener != null) {
            mOnRatingBarChangeListener.onRatingChanged(this, getRating(),
                    fromUser);
        }
    }
}
