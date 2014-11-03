package com.wobgames.whosnext;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class LevelDialogFragment extends DialogFragment {
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 
		 //builder.setMessage(R.string.choose_level);
		 builder.setTitle(R.string.choose_level);
		 
		 return builder.create();
	}

}
