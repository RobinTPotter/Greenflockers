package robin.plop;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Simulation implements View.OnTouchListener, GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener {

    private int initial_bugs = 200;

    enum Colour {GREEN_LINE, BLACK_LINE, WHITE_LINE}

    Plop3dView plop3DView;
    int width;
    int height;

    float scale = 1;

    int offsetx = 0;
    int offsety = 0;
    ArrayList<Bug> bugs;
    double[][] distance;

    public interface Distant {
	public int getX();
	public int getY();
    }

    public class Bug implements Distant {
        double x;
        double y;
        double dx;
        double dy;
        int bt = -1;

        Bug() {
            this.x = (0.8 * width * (Math.random() - 0.5));
            this.y = (0.8 * height * (Math.random() - 0.5));
            this.dx = 1.5 * (Math.random() - 0.5);
            this.dy = 1.5 * (Math.random() - 0.5);
        }

        public void move() {
            this.x = this.x + this.dx;
            this.y = this.y + this.dy;
        }

        public void match(ArrayList<Distant> bugs) {
            int bt = -1;
            int btd = 0;
            for (int bb = 0; bb < bugs.size(); bb++) {
                Bug bugtarget = bugs.get(bb);
                if (bugtarget != null && bugtarget.getX() != this.getX() && bugtarget.getY() != this.getY()) {
                    int ddx = bugtarget.getX() - this.getX();
                    int ddy = bugtarget.getY() - this.getY();
                    int dist = ddx * ddx + ddy * ddy;
                    if (bt == -1 || dist < btd) {
                        bt = bb;
                        btd = dist;
                    }
                }
            }

            this.bt = bt;
            if (bt >= 0) {
                this.dx = 0.5 * this.dx + 0.5 * bugs.get(bt).dx;
                this.dy = 0.5 * this.dy + 0.5 * bugs.get(bt).dy;
            }
        }

        public int getX() {
            return (int) this.x;
        }

        public int getY() {
            return (int) this.y;
        }
    }

    HashMap<Colour, Paint> pallette;

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

        bugs = new ArrayList<Bug>();

        Log.d("Simulation", "Create Simulation");

    }


    /**
     * set up colour palette for use
     */
    private void setupColours() {
        pallette = new HashMap<Colour, Paint>();
        pallette.put(Colour.GREEN_LINE, newPaint(0, 255, 0, Paint.Style.STROKE));
        pallette.put(Colour.BLACK_LINE, newPaint(0, 0, 0, Paint.Style.STROKE));
        pallette.put(Colour.WHITE_LINE, newPaint(255, 255, 255, Paint.Style.STROKE));


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

        if (bugs.size() == 0) for (int bb = 0; bb < initial_bugs; bb++) bugs.add(new Bug());


    }

    protected void drawMethod(int width, int height) {

        Log.d("Simulation", "draw");
        Canvas c = new Canvas(plop3DView.getBuffer());
        Paint black = new Paint();
        black.setARGB(255, 0, 0, 0);
        c.drawRect(0, 0, width, height, black);

        float[] lines = new float[0];
	boolean lines=false;

        for (int bb = 0; bb < bugs.size(); bb++) {
            if (bugs.get(bb) == null) return;
            bugs.get(bb).move();
            bugs.get(bb).match(bugs);

            if (lines) lines = addCrossToLinesAt(lines, scale * (bugs.get(bb).getX()) + offsetx, scale * (bugs.get(bb).getY()) + offsety);
            else c.drawCircle( scale * (bugs.get(bb).getX()) + offsetx, scale * (bugs.get(bb).getY()) + offsety,3,Colour.GREEN_LINE);

        }


    if (lines)    c.drawLines(lines, pallette.get(Colour.GREEN_LINE));

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
        this.offsetx = this.offsetx - (int) distanceX;
        this.offsety = this.offsety - (int) distanceY;
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        plop3DView.message("long");
        bugs.clear();
        offsetx = 0;
        offsety = 0;
        scale = 1f;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        plop3DView.message("fling " + velocityX + " " + velocityY);
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        this.scale *= detector.getScaleFactor();
        if (scale < 0.1) scale = 0.1f;
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
