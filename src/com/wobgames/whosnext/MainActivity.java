package com.wobgames.whosnext;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.wobgames.whosnext.ButtonsFragment.OnButtonSelectedListener;

public class MainActivity extends FragmentActivity implements OnButtonSelectedListener {
	// Debug
	private static final String TAG = "MainActivity";
	public final static String EXTRA_MESSAGE = "com.wobgames.whosnext.MESSAGE";
	
	// Members
	ButtonsFragment mButtonsFragment;
	QuestionsFragment mQuestionsFragment;
	
	// On Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().hide();
        setContentView(R.layout.activity_main);
        
        if (savedInstanceState == null) {
			// Add the fragment on initial activity setup
        	mButtonsFragment = new ButtonsFragment();
			getSupportFragmentManager().beginTransaction().add(R.id.rootlayout, mButtonsFragment).commit();
		} else {
			// Or set the fragment from restored state info
			mButtonsFragment = (ButtonsFragment) getSupportFragmentManager().findFragmentById(R.id.rootlayout);
		}
        
        // Fragments
//        mButtonsFragment = new ButtonsFragment();
        mQuestionsFragment = new QuestionsFragment();
//        getSupportFragmentManager().beginTransaction().show(mButtonsFragment).commit();
        
        // Create the Database
        DatabaseHelper mDBHelper = new DatabaseHelper(getApplication());
        mDBHelper.init();
        List<Question> questions_list = mDBHelper.getQuestions();
        
    }

    @Override
    public void onCreateGame() {
    	// When the user taps the Create Game button
    	Log.d(TAG, "onCreateGame()");
//    	getSupportFragmentManager().beginTransaction()
//    		.replace(((ViewGroup)(getRootView().getParent())).getId(), mQuestionsFragment).addToBackStack(null).commit();
    	getSupportFragmentManager().beginTransaction()
    		.replace(R.id.rootlayout, mQuestionsFragment).addToBackStack(null).commit();
    }
    
    @Override
    public void onJoinGame() {
    	// When the user taps the Join Game button
    
    	
    }
}
