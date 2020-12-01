package robin.arg3d;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.HashMap;

public class Simulation implements View.OnTouchListener, GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener {


    private static final String GREEN_LINE = "green_line";
    private static final String BLACK_LINE = "black_line";
    private static final String WHITE_LINE = "white_line";

    Arg3dView arg3dView;
    int width;
    int height;

    HashMap<String, Paint> pallette;

    /**
     * constructor for the simulation allows contact between main app activity (for menu etc)
     * sets up the hashmaps for optional paintable decorations and sets up the colour pallette.
     *
     * @param arg3dView
     */
    public Simulation(Arg3dView arg3dView) {

        this.arg3dView = arg3dView;

        setupColours();
        //setupPaintableOptions();
        //addWorm(initialWorms);

        Log.d("Simulation", "Create Simulation");

    }


    /**
     * set up colour palette for use
     */
    private void setupColours() {
        pallette = new HashMap<String, Paint>();
        pallette.put(GREEN_LINE, newPaint(0, 255, 0, Paint.Style.STROKE));
        pallette.put(BLACK_LINE, newPaint(0, 0, 0, Paint.Style.STROKE));
        pallette.put(WHITE_LINE, newPaint(255, 255,255, Paint.Style.STROKE));


    }

    private static float distSquared(float x1, float y1, float x2, float y2) {
        return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);

    }

    public static Paint newPaint(int r, int g, int b, Paint.Style style) {
        Paint p = new Paint();
        p.setARGB(255, r, g, b);
        p.setStyle(style);
        return p;
    }

    protected void updateProperties(int width, int height) {

        Log.d("Simulation", "update props");

        this.width = width;
        this.height = height;

    }

    protected void drawMethod(int width, int height) {

        Log.d("Simulation", "draw");
        Canvas c = new Canvas(arg3dView.getBuffer());
        Paint black = new Paint();
        black.setARGB(255,0,0,0);
        c.drawRect(0, 0, width, height, black);


        float[] lines = new float[]{
                width/2 - 2,height/2 , width/2 + 3, height/2
                ,
                width/2 ,height/2 - 2, width/2 ,height/2 + 3
        };
        c.drawLines(lines,pallette.get(GREEN_LINE));

    }



    /*
    gesture methods
     */

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        arg3dView.message("touch");
        boolean ret = arg3dView.getGestureDetector().onTouchEvent(event);
        ret = arg3dView.getScaleGestureDetector().onTouchEvent(event) || ret;
        return ret;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        arg3dView.message("down");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        arg3dView.message("show press");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        arg3dView.message("single tap up");

        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        arg3dView.message("scroll");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        arg3dView.message("long");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        arg3dView.message("fling " + velocityX + " " + velocityY);
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {


        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        arg3dView.message("scale begin");

        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        arg3dView.message("scale end");

    }


/*
interface methods WormWrangler
 */

}