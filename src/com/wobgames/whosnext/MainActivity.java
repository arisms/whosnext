package com.wobgames.whosnext;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity {
	// Debug
	private static final String TAG = "MainActivity";

	// On Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);
        
        // Start Screen Fragment
        if (findViewById(R.id.start_screen_fragment_container) != null) {
        	
        	if (savedInstanceState != null) {
                return;
            }

        	// Create a new Fragment to be placed in the activity layout
            StartScreenFragment startFragment = new StartScreenFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            startFragment.setArguments(getIntent().getExtras());
        	
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.start_screen_fragment_container, startFragment).commit();
        }
        

    }


    
}
