package com.wobgames.whosnext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ClickableViewAccessibility") public class GameSetupFragment extends Fragment {
	//private static final String TAG = "GameSetupFragment";
	
	TextView tvHeader;
	TextView tvFamiliarity;
	TextView tvTimer;
	TextView tvGroupName;
	EditText etGroupName;
	Button button;
	
	String famLevel = "";
	String timerDuration = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.setup_fragment, container, false);
        final MainActivity mActivity = (MainActivity) getActivity();
        
        // Set text views and edit view
        etGroupName = (EditText) view.findViewById(R.id.setGroupName_edittext);
        etGroupName.setHint("Group Name");
        etGroupName.setTypeface(mActivity.exoregular);
        
        tvHeader = (TextView) view.findViewById(R.id.setup_header);
        tvHeader.setTypeface(mActivity.exoregular);
        
        tvFamiliarity = (TextView) view.findViewById(R.id.setFamiliarity_text);
        tvFamiliarity.setTypeface(mActivity.exoregular);
        tvFamiliarity.setText("Select familiarity level");
        
        tvTimer = (TextView) view.findViewById(R.id.setTimer_text);
        tvTimer.setTypeface(mActivity.exoregular);
        tvTimer.setText("Set timer duration");
        
        tvGroupName = (TextView) view.findViewById(R.id.setGroupName_text);
        tvGroupName.setTypeface(mActivity.exoregular);
        tvGroupName.setText("Set the group's name");
        
        /** Set spinners **/
        
        // Spinner 1 - Familiarity level
        final Spinner spinner1 = (Spinner) view.findViewById(R.id.setFamiliarity_spinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(),
             R.array.familiarity_levels_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        
        // OnItemSelectedListener for familiarity level spinner
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        	
        	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
        		
        		famLevel = parent.getItemAtPosition(pos).toString();
            }
        	
            public void onNothingSelected(AdapterView<?> parent) {
            	Toast toast = Toast.makeText(getActivity(), "Please select familiarity level", Toast.LENGTH_SHORT);
        		toast.show();
            }
        });
        // OnTouchListener for familiarity level spinner
        spinner1.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Hide the soft keyboard
				InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(tvGroupName.getWindowToken(), 0);
				return false;
			}
		});
        
     	// Spinner 2 - Timer duration
        final Spinner spinner2 = (Spinner) view.findViewById(R.id.setTimer_spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),
             R.array.timer_duration_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        
        // OnItemSelectedListener for familiarity level spinner
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        	
        	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
        		
        		timerDuration = parent.getItemAtPosition(pos).toString();
            }
        	
            public void onNothingSelected(AdapterView<?> parent) {
            	Toast toast = Toast.makeText(getActivity(), "Please set the timer duration!", Toast.LENGTH_SHORT);
        		toast.show();
            }
        });
        // OnTouchListener for familiarity level spinner
        spinner2.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Hide the soft keyboard
				InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(tvGroupName.getWindowToken(), 0);
				return false;
			}
		});
        
        button = (Button) view.findViewById(R.id.submit_settings_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	// Check if group name is empty
        		if(etGroupName.getText().toString().equals(""))
        			mActivity.showToast("Group name cannot be empty!");
        		else {
	            	int level;
	            	if(spinner1.getSelectedItem().toString().equals("Strangers"))
	            		level = 1;
	            	else if(spinner1.getSelectedItem().toString().equals("Acquaintances"))
	            		level = 2;
	            	else if(spinner1.getSelectedItem().toString().equals("Friends"))
	            		level = 3;
	            	else
	            		level = 0;
	            	
	            	int duration;
	            	if(spinner2.getSelectedItem().toString().equals("1:00"))
	            		duration = 60000;
	            	else if(spinner2.getSelectedItem().toString().equals("1:30"))
	            		duration = 90000;
	            	else if(spinner2.getSelectedItem().toString().equals("2:00"))
	            		duration = 120000;
	            	else if(spinner2.getSelectedItem().toString().equals("2:30"))
	            		duration = 150000;
	            	else if(spinner2.getSelectedItem().toString().equals("3:00"))
	            		duration = 180000;
	            	else if(spinner2.getSelectedItem().toString().equals("3:30"))
	            		duration = 210000;
	            	else if(spinner2.getSelectedItem().toString().equals("4:00"))
	            		duration = 240000;
	            	else
	            		duration = 300000;
	            		
	            	// Call MainActivity's createGame()
	            	mActivity.createGame(etGroupName.getText().toString(), level, duration);
        		}
            }
        });
        
        return view;
	}
	
}
