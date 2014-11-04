package com.wobgames.whosnext;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class GameSetupFragment extends Fragment {
	private static final String TAG = "GameSetupFragment";
	
	TextView tvFamiliarity;
	TextView tvTimer;
	TextView tvGroupName;
	EditText etGroupName;
	
	String famLevel = "";
	String timerDuration = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.setup_fragment, container, false);
        
        
        // Set text views and edit view
        etGroupName = (EditText) view.findViewById(R.id.setGroupName_edittext);
        
        tvFamiliarity = (TextView) view.findViewById(R.id.setFamiliarity_text);
        tvFamiliarity.setText("Select familiarity level");
        tvTimer = (TextView) view.findViewById(R.id.setTimer_text);
        tvTimer.setText("Set timer duration");
        tvGroupName = (TextView) view.findViewById(R.id.setGroupName_text);
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
        		
        		Toast toast = Toast.makeText(getActivity(), famLevel, Toast.LENGTH_SHORT);
        		toast.show();
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
        		
        		Toast toast = Toast.makeText(getActivity(), timerDuration, Toast.LENGTH_SHORT);
        		toast.show();
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
        
        return view;
	}
	
//	// Hide the soft keyboard
//				InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//				imm.hideSoftInputFromWindow(mAnswerEt.getWindowToken(), 0);
//				
//				mAnswerEt.clearFocus();
//				button.requestFocus();

	
	
}
