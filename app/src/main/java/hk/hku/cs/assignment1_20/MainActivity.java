package hk.hku.cs.assignment1_20;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by hutia on 10/30/2016.
 */

public class MainActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Button next = (Button) findViewById(R.id.button);
        next.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view){
                                        Intent myIntent = new Intent(view.getContext(),SecondActivity.class);
                                        startActivityForResult(myIntent,0);
                                    }
                                }
        );
    }
}
