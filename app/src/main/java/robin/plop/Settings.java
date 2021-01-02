package robin.plop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by deadmeat on 15/03/17.
 */
public class Settings extends Dialog {
    Simulation simulation;

    public Settings(Context c, Simulation simulation) {
        super(c);
        this.simulation = simulation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);



    }

    protected void onStart() {

    }

}
