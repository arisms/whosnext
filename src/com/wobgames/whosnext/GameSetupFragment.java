package com.wobgames.whosnext;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class GameSetupFragment extends Fragment {
	private static final String TAG = "GameSetupFragment";
	
	TextView tvFamiliarity;
	TextView tvTimer;
	TextView tvGroupName;
	EditText etGroupName;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.setup_fragment, container, false);
        
        
        // Set text views and edit view
        tvFamiliarity = (TextView) view.findViewById(R.id.setFamiliarity_text);
        tvFamiliarity.setText("Select familiarity level");
        tvTimer = (TextView) view.findViewById(R.id.setTimer_text);
        tvTimer.setText("Set timer duration");
        tvGroupName = (TextView) view.findViewById(R.id.setGroupName_text);
        tvGroupName.setText("Set the group's name");
        
        etGroupName = (EditText) view.findViewById(R.id.setGroupName_edittext);
        
        // Set spinners
        Spinner spSetFamiliarity = (Spinner) view.findViewById(R.id.setFamiliarity_spinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(),
             R.array.familiarity_levels_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSetFamiliarity.setAdapter(adapter1);
        
        Spinner spSetTimer = (Spinner) view.findViewById(R.id.setTimer_spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),
             R.array.timer_duration_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSetTimer.setAdapter(adapter2);
        
        return view;
	}

}
