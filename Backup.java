/*****************************/
// questions_fragment.xml

<ListView
        	android:id="@id/android:list"
        	android:layout_height="0dp"
        	android:layout_weight="1"
        	android:layout_width="match_parent"
        	android:background="@color/grey1" >
</ListView>
		

// QuestionsFragment.java

//Link ListView to the data
mListView = (ListView) view.findViewById(android.R.id.list);

QuestionsAnswersListAdapter mAdapter = new QuestionsAnswersListAdapter(getActivity(), questions_strings);

mListView.setAdapter(mAdapter);



/**QUESTIONS ACTIVITY**/
public class QuestionsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questions);
		
		Intent intent = getIntent();
	    String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
	    TextView temp = (TextView) findViewById(R.id.temp_text);
	    temp.setText(message);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.questions, menu);
		return true;
	}
}






// Member Data
	Button mCreateGame;
	Button mJoinGame;
	
	
	
	// Buttons
    mCreateGame = (Button) findViewById(R.id.creategame);
    mCreateGame.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			createGame();				
		}
	});
    mJoinGame = (Button) findViewById(R.id.joingame);
    mJoinGame.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			joinGame();				
		}
	});





import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.wobgames.whosnext.MainActivity;
import com.wobgames.whosnext.QuestionsAnswersListAdapter;
import com.wobgames.whosnext.QuestionsFragment;
import com.wobgames.whosnext.R;

        DatabaseHelper mDBHelper = new DatabaseHelper(getApplication());
        
        mDBHelper.init();
        
        List<Question> questions_list = mDBHelper.getQuestions();
        
        Log.i("Question.text: ", Integer.toString(questions_list.size()));
        
        for(int i=0; i<questions_list.size(); i++)
        	Log.i("Question.text: ", questions_list.get(i).text());
        
        
        
        
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {
        	
        	// If being restored from a previous state, return
            if (savedInstanceState != null) {
                return;
            }
            
            // Create a new Fragment to be placed in the activity layout
            MainFragment mainFragment = new MainFragment();
            
            // Pass the Intent's extras to the fragment as arguments
            mainFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mainFragment).commit();


        }
        
        
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }