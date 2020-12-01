package robin.arg3d;

import android.graphics.Canvas;

public class Arg3dThread extends Thread {

        Arg3dView arg3dView;
        private boolean running = false;

        public Arg3dThread(Arg3dView view) {
            arg3dView = view;
        }

        public void setRunning(boolean run) {
            running = run;
        }

        @Override
        public void run() {
            while (running) {

                Canvas canvas = arg3dView.getHolder().lockCanvas();

                if (canvas != null) {
                    synchronized (arg3dView.getHolder()) {
                        arg3dView.drawSomething(canvas);
                    }
                    arg3dView.getHolder().unlockCanvasAndPost(canvas);
                }


                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }



}
