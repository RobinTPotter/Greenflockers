package robin.plop;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;


import java.util.HashMap;
import java.util.Random;

public class Simulation implements View.OnTouchListener, GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener {


    private static final String GREEN_LINE = "green_line";
    private static final String BLACK_LINE = "black_line";
    private static final String WHITE_LINE = "white_line";

    Plop3dView plop3DView;
    int width;
    int height;

    Bug[] bugs;

    public class Bug {
        double x;
        double y;
        double dx;
        double dy;
        int bt=-1;

        Bug() {
            this.x = (0.8*width * (Math.random()-0.5));
            this.y = (0.8*height * (Math.random()-0.5));
            this.dx = 1.5*(Math.random() - 0.5);
            this.dy = 1.5*(Math.random() - 0.5);
        }

        public void move() {
            this.x = this.x + this.dx;
            this.y = this.y + this.dy;
        }

        public void match(Bug[] bugs) {
            int bt = -1;
            int btd = 0;
            for (int bb = 0; bb < bugs.length; bb++) {
                if (bugs[bb].getX() != this.getX() && bugs[bb].getY() != this.getY()) {
                    int ddx = bugs[bb].getX() - this.getX();
                    int ddy = bugs[bb].getY() - this.getY();
                    int dist = ddx * ddx + ddy * ddy;
                    if (bt == -1 || dist < btd) {
                        bt = bb;
                        btd = dist;
                    }
                }
            }

            if (bt>=0) {
            this.bt = bt;
            this.dx = 0.5 * this.dx + 0.5 * bugs[bt].dx ;
            this.dy = 0.5 * this.dy + 0.5 * bugs[bt].dy ;
        }}

        public int getX() {
            return (int) this.x;
        }

        public int getY() {
            return (int) this.y;
        }
    }

    HashMap<String, Paint> pallette;

    /**
     * constructor for the simulation allows contact between main app activity (for menu etc)
     * sets up the hashmaps for optional paintable decorations and sets up the colour pallette.
     *
     * @param plop3DView
     */
    public Simulation(Plop3dView plop3DView) {

        this.plop3DView = plop3DView;

        setupColours();
        //setupPaintableOptions();
        //addWorm(initialWorms);

        bugs = new Bug[100];

        Log.d("Simulation", "Create Simulation");

    }


    /**
     * set up colour palette for use
     */
    private void setupColours() {
        pallette = new HashMap<String, Paint>();
        pallette.put(GREEN_LINE, newPaint(0, 255, 0, Paint.Style.STROKE));
        pallette.put(BLACK_LINE, newPaint(0, 0, 0, Paint.Style.STROKE));
        pallette.put(WHITE_LINE, newPaint(255, 255, 255, Paint.Style.STROKE));


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

        if (bugs[0]==null) for (int bb = 0; bb < bugs.length; bb++) bugs[bb] = new Bug();


    }

    protected void drawMethod(int width, int height) {

        Log.d("Simulation", "draw");
        Canvas c = new Canvas(plop3DView.getBuffer());
        Paint black = new Paint();
        black.setARGB(255, 0, 0, 0);
        c.drawRect(0, 0, width, height, black);

        float[] lines = new float[0];


        for (int bb = 0; bb < bugs.length; bb++) {
            if (bugs[bb] == null) return;
            bugs[bb].move();
            bugs[bb].match(bugs);

            lines = addCrossToLinesAt(lines, bugs[bb].getX(), bugs[bb].getY());
        }


        c.drawLines(lines, pallette.get(GREEN_LINE));

    }



    /*
    gesture methods
     */

    private float[] addCrossToLinesAt(float[] lines, float x, float y) {
        float[] newlines = new float[lines.length + 8];

        float[] cross = new float[]{
                x + (float) width / 2 - 2, y + (float) height / 2, x + (float) width / 2 + 3, y + (float) height / 2
                ,
                x + (float) width / 2, y + (float) height / 2 - 2, x + (float) width / 2, y + (float) height / 2 + 3
        };

        for (int ff = 0; ff < lines.length; ff++) newlines[ff] = lines[ff];
        for (int ff = 0; ff < cross.length; ff++) newlines[lines.length + ff] = cross[ff];

        return newlines;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        plop3DView.message("touch");
        boolean ret = plop3DView.getGestureDetector().onTouchEvent(event);
        ret = plop3DView.getScaleGestureDetector().onTouchEvent(event) || ret;
        return ret;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        plop3DView.message("down");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        plop3DView.message("show press");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        plop3DView.message("single tap up");

        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        plop3DView.message("scroll");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        plop3DView.message("long");
        bugs[0]=null;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        plop3DView.message("fling " + velocityX + " " + velocityY);
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {


        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        plop3DView.message("scale begin");

        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        plop3DView.message("scale end");

    }


/*
interface methods WormWrangler
 */

}