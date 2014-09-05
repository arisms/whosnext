package com.wobgames.whosnext;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class QuestionsAnswersListAdapter extends ArrayAdapter<String>{

	private LayoutInflater mInflater;
	private final List<String> strings;
	//private final Map<String, String> mMap;
	
	public QuestionsAnswersListAdapter(Context context, List<String> strings) {
		super(context, R.layout.questions_answers_list_item, strings);
		
		this.strings = strings; 
		
		this.mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = mInflater.inflate(R.layout.questions_answers_list_item, parent, false);
		
		TextView tv = (TextView) view.findViewById(R.id.list_question);
		tv.setText(strings.get(position));
		
//		EditText et = (EditText) view.findViewById(R.id.list_answer);
//		et.setText(strings.get(position));
//		
		return view;
	}
}
