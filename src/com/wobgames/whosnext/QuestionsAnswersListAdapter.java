package com.wobgames.whosnext;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class QuestionsAnswersListAdapter extends ArrayAdapter<String>{

	//private static final String TAG = "QuestionsAnswerListAdapter";
	
	private LayoutInflater mInflater;
	private final List<String> strings;
	private final List<User> mUsersList;
	//private final Map<String, String> mMap;
	final CharSequence emptyAnswerToast = "Wrong player! Try again...";
	Button button;
	OnSelectPlayerListener mListener;
	MainActivity mActivity;
	
	public QuestionsAnswersListAdapter(Context context, List<String> strings, OnSelectPlayerListener listener, List<User> users) {
		super(context, R.layout.questions_answers_list_item, strings);
		
		this.mUsersList = new ArrayList<User>(users);
		this.mListener = listener;
		this.strings = strings; 
		
		this.mInflater = LayoutInflater.from(context);
		mActivity = (MainActivity) context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = mInflater.inflate(R.layout.questions_answers_list_item, parent, false);
		
		//final int id = mUsersList.get(position).id();
		final int pos = position;
		
		TextView tv = (TextView) view.findViewById(R.id.list_question);
		//tv.setText(strings.get(position));
		tv.setText(mUsersList.get(position).name());
		tv.setTypeface(mActivity.exoregular);
		
//		button = (Button) view.findViewById(R.id.select_player_button);
//		button.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//          	// If the answer text is empty
//          	mListener.onSelectPlayer(pos);
//				
//          }
//      });
		
//		EditText et = (EditText) view.findViewById(R.id.list_answer);
//		et.setText(strings.get(position));
//		
		return view;
	}
	
	public interface OnSelectPlayerListener {
		
		public void onSelectPlayer(int id);
		
	}
}

