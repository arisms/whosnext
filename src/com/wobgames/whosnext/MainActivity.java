package com.wobgames.whosnext;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	// Debug
	private static final String TAG = "MainActivity";
	
	public final static String EXTRA_MESSAGE = "com.wobgames.whosnext.MESSAGE";

	// On Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Create the Database
        DatabaseHelper mDBHelper = new DatabaseHelper(getApplication());
        
        mDBHelper.init();
        
        List<Question> questions_list = mDBHelper.getQuestions();
        
    }

    public void startGame(View view) {
    	// When the user taps the Start Game button
    	//setContentView(R.layout.activity_questions);
    	
    	Intent intent = new Intent(this, QuestionsActivity.class);
        //EditText editText = (EditText) findViewById(R.id.startgame);
        //String message = editText.getText().toString();
    	
        String message = "Start Game";
    	intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    	
    }
    
    public void joinGame(View view) {
    	// When the user taps the Join Game button
    	Intent intent = new Intent(this, QuestionsActivity.class);
    	
    	String message = "Join Game";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    	
    }
}
