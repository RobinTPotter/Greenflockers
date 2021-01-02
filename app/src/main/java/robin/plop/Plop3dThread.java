package robin.plop;

import android.graphics.Canvas;

public class Plop3dThread extends Thread {

        Plop3dView plop3DView;
        private boolean running = false;

        public Plop3dThread(Plop3dView view) {
            plop3DView = view;
        }

        public void setRunning(boolean run) {
            running = run;
        }

        @Override
        public void run() {
            while (running) {

                Canvas canvas = plop3DView.getHolder().lockCanvas();

                if (canvas != null) {
                    synchronized (plop3DView.getHolder()) {
                        plop3DView.drawSomething(canvas);
                    }
                    plop3DView.getHolder().unlockCanvasAndPost(canvas);
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
