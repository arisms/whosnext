package com.wobgames.whosnext;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StartScreenFragment extends Fragment{
	OnMainFragmentSelectedListener mListener;

	// Implemented in MainActivity
    public interface OnMainFragmentSelectedListener {
        //public void onArticleSelected(int position);
    }
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.start_screen, container, false);
	}
	
	
	
}
