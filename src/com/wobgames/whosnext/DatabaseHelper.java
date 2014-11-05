package com.wobgames.whosnext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	// Debug
	private static final String TAG = "DatabaseHelper";
	
	/** Constants **/
	// Database Details
	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WhosNext.db";
    
    // Questions Table
    public static final String TABLE_QUESTIONS = "questions_table";
    public static final String QUESTIONS_COLUMN_ID = "id";
    public static final String QUESTIONS_COLUMN_TEXT = "text";
    public static final String QUESTIONS_COLUMN_ROUND = "round";
    
    // Users Table
    public static final String TABLE_USERS = "users_table";
    public static final String USERS_COLUMN_ID = "id";
    public static final String USERS_COLUMN_NAME = "name";

    // Answers Table
    public static final String TABLE_ANSWERS = "answers_table";
    public static final String ANSWERS_COLUMN_ID = "id";
    public static final String ANSWERS_COLUMN_TEXT = "text";
    public static final String ANSWERS_COLUMN_USERID = "userId";
    public static final String ANSWERS_COLUMN_QUESTIONID = "questionId";

    // Strings
    public static final String[] QUESTIONS_ALL_COLUMNS= {
    	QUESTIONS_COLUMN_ID,
    	QUESTIONS_COLUMN_TEXT,
    	QUESTIONS_COLUMN_ROUND
    	};
    
    public static final String[] USERS_ALL_COLUMNS= {
    	USERS_COLUMN_ID,
    	USERS_COLUMN_NAME,
    	};
    
    public static final String[] ANSWERS_ALL_COLUMNS= {
    	ANSWERS_COLUMN_ID,
    	ANSWERS_COLUMN_TEXT,
    	ANSWERS_COLUMN_USERID,
    	ANSWERS_COLUMN_QUESTIONID
    	};
    
    /** SQL Queries **/
    // Create Questions Table
    private static final String QUERY_QUESTIONS_TABLE_CREATE = 
    		"CREATE TABLE IF NOT EXISTS " + TABLE_QUESTIONS + " (" +
    		QUESTIONS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
    		QUESTIONS_COLUMN_TEXT + " TEXT, " +
    		QUESTIONS_COLUMN_ROUND + " INTEGER" + ");";
    
    // Create Users Table
    private static final String QUERY_USERS_TABLE_CREATE = 
    		"CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
    		USERS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
    		USERS_COLUMN_NAME + " TEXT" + ");";
    
 // Create Answers Table
    private static final String QUERY_ANSWERS_TABLE_CREATE = 
    		"CREATE TABLE IF NOT EXISTS " + TABLE_ANSWERS + " (" +
    		ANSWERS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
    		ANSWERS_COLUMN_TEXT + " TEXT, " +
    		ANSWERS_COLUMN_USERID + " INTEGER, " + 
    		ANSWERS_COLUMN_QUESTIONID + " INTEGER" + ");";
    
    /** Members **/
    private Context context;
    
    // Database constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    
    // onCreate
    public void onCreate(SQLiteDatabase db) {
    	Log.d(TAG, "onCreate()");
        db.execSQL(QUERY_QUESTIONS_TABLE_CREATE);
        db.execSQL(QUERY_USERS_TABLE_CREATE);
        db.execSQL(QUERY_ANSWERS_TABLE_CREATE);
    }
    
    // onUpgrade
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        //db.execSQL();
        //onCreate(db);
    }
    
    // onDowngrade
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //onUpgrade(db, oldVersion, newVersion);
    }

    // Check if Questions Table is empty
    public boolean IsTableEmpty (String table) {
    	SQLiteDatabase db = getReadableDatabase();
    	Cursor mCursor = db.rawQuery("SELECT * FROM " + table, null);
    	boolean empty;

    	if (mCursor.getCount() != 0)
    	{
    	   // Table is not empty
    	  empty = false;

    	} else
    	{
    	   // Table is empty
    	   empty = true;
    	}
    	db.close();   // ?????
    	return empty;
    }
    
    // Initialize Database
    public void init(int level) {
    	int current_round = level;
    	Log.d(TAG, "DB init with level: " + level);
    	
    	// QUESTIONS table initialization
        if(IsTableEmpty(TABLE_QUESTIONS)) {
        	// If Questions table is empty, insert default questions from text file
        	
        	BufferedReader reader = null;
        	try {
        		// Open txt file in Assets folder, for reading
        	    reader = new BufferedReader(new InputStreamReader(context.getAssets().open("questions.txt"), "UTF-8")); 

        	    // Parse each line of the text file
        	    String text;
        	    String delim = "_";
        	    int round;
        	    String mLine = reader.readLine();
        	    while (mLine != null) {
        	       
        	       String[] tokens = mLine.split(delim);
        	       round = Integer.parseInt(tokens[0]);
        	       text = tokens[1];
        	       
        	       if(current_round == round) {
        	    	   Question question = new Question(text, round);
        	    	   addQuestion(question);
        	       }
        	       
        	       mLine = reader.readLine();
        	    }
        	} catch (IOException e) {
        		Log.e("DatabaseHelper", "open txt file error 1" + e);
        	} 
        	finally {
        	    if (reader != null) {
        	         try {
        	             reader.close();
        	         } catch (IOException e) {
        	        	 Log.e("DatabaseHelper", "open txt file error 2" + e);
        	         }
        	    }
        	}
        }
        // QUESTIONS clear table
        else
        {
        	deleteQuestions();
        	init(level);
        }
        
        // USERS clear table
        if(!IsTableEmpty(TABLE_USERS)) {
        	deleteUsers();
        }
        
        // ANSWERS clear table
        if(!IsTableEmpty(TABLE_ANSWERS)) {
        	deleteAnswers();
        }
        
    }
    
    // INSERT Question
    public void addQuestion(Question question) {
    	ContentValues values = new ContentValues();
    	
    	values.put(QUESTIONS_COLUMN_TEXT, question.text());
    	values.put(QUESTIONS_COLUMN_ROUND, question.round());
    	
    	SQLiteDatabase db = getWritableDatabase();
    	//long ret = db.insert(TABLE_QUESTIONS, null, values);
    	long ret = db.insert(TABLE_QUESTIONS, null, values);
    	db.close();
    	//Log.i(TAG, "ID? = " + ret + " TEXT = " + question.text());
    }
    
    
    // SELECT * FROM QUESTIONS
    public List<Question> getQuestions () {
    	SQLiteDatabase db = getReadableDatabase();
    	
    	Cursor mCursor = db.query(TABLE_QUESTIONS,
    			QUESTIONS_ALL_COLUMNS,
    			null,
    			null,
    			null,
    			null,
    			null);
    	
    	// Get ids of columns
    	final int idIdx = mCursor.getColumnIndex(QUESTIONS_COLUMN_ID);    	
    	final int textIdx = mCursor.getColumnIndex(QUESTIONS_COLUMN_TEXT);    	
    	final int roundIdx = mCursor.getColumnIndex(QUESTIONS_COLUMN_ROUND);    	

    	List<Question> questions_list = new ArrayList<Question>();
    	
    	// Add the questions to the list
    	while(mCursor.moveToNext()) {
    		final int id = mCursor.getInt(idIdx);
        	final String text = mCursor.getString(textIdx);
        	final int round = mCursor.getInt(roundIdx);
        	Question question = new Question(text, round);
        	question.setId(id);
        	//Log.i(TAG, "id = " + id + " q.id = " + question.id());
    		questions_list.add(question);
    	}
    	
    	return questions_list;
    }
    
    // SELECT FROM QUESTIONS WHERE ID = ID
    public Question getQuestionById (int id) {
    	SQLiteDatabase db = getReadableDatabase();
    	String where = QUESTIONS_COLUMN_ID + "=" + id;
    	//String[] args = {Integer.toString(id)};
    	
    	Cursor mCursor = db.query(TABLE_QUESTIONS,
    			QUESTIONS_ALL_COLUMNS,
    			where,
    			null,
    			null,
    			null,
    			null);
    		
    	// Get ids of columns
    	final int idIdx = mCursor.getColumnIndex(QUESTIONS_COLUMN_ID);    	
    	final int textIdx = mCursor.getColumnIndex(QUESTIONS_COLUMN_TEXT);    	
    	final int roundIdx = mCursor.getColumnIndex(QUESTIONS_COLUMN_ROUND); 
    	
    	// Select the correct question
    	mCursor.moveToNext();
    	final int id_final = mCursor.getInt(idIdx);
    	final String text = mCursor.getString(textIdx);
    	final int round = mCursor.getInt(roundIdx);
    	
    	Question result = new Question(text, round);
    	result.setId(id_final);
    	return result;
    }
    
    // DELETE * FROM QUESTIONS
    public void deleteQuestions () {
    	Log.i(TAG, "Deleting questions");
    	SQLiteDatabase db = getWritableDatabase();
    	
    	db.execSQL("delete from " + TABLE_QUESTIONS);
    	db.close();
    }
    
    // INSERT User
    public long addUser (User user) {
    	Log.i(TAG, "Adding user: " + user.name());
    	ContentValues values = new ContentValues();
    	
    	values.put(USERS_COLUMN_NAME, user.name());
    	
    	SQLiteDatabase db = getWritableDatabase();
    	//long ret = db.insert(TABLE_USERS, null, values);
    	//Log.i(TAG, "ret value = " + ret);
    	long ret = db.insert(TABLE_USERS, null, values);
    	return ret;
    }
    
    // SELECT * FROM USERS
    public List<User> getUsers () {
    	SQLiteDatabase db = getReadableDatabase();
    	
    	Cursor mCursor = db.query(TABLE_USERS,
    			USERS_ALL_COLUMNS,
    			null,
    			null,
    			null,
    			null,
    			null);
    	
    	// Get ids of columns
    	final int idIdx = mCursor.getColumnIndex(USERS_COLUMN_ID);    	
    	final int nameIdx = mCursor.getColumnIndex(USERS_COLUMN_NAME);    	

    	List<User> users_list = new ArrayList<User>();
    	
    	// Add the users to the list
    	while(mCursor.moveToNext()) {
    		final int id = mCursor.getInt(idIdx);
        	final String name = mCursor.getString(nameIdx);
        	User user = new User(id, name);
    		users_list.add(user);
    	}
    	
    	return users_list;
    }
    
    // DELETE * FROM USERS
    public void deleteUsers () {
    	Log.i(TAG, "Deleting users");
    	SQLiteDatabase db = getWritableDatabase();
    	
    	db.execSQL("delete from " + TABLE_USERS);
    	db.close();
    }
    
    // INSERT Answer
    public long addAnswer (Answer answer) {
    	Log.i(TAG, "Adding answer: " + answer.text() + " with userId: " + answer.userId());
    	ContentValues values = new ContentValues();
    	
    	values.put(ANSWERS_COLUMN_TEXT, answer.text());
    	values.put(ANSWERS_COLUMN_USERID, answer.userId());
    	values.put(ANSWERS_COLUMN_QUESTIONID, answer.questionId());

    	SQLiteDatabase db = getWritableDatabase();
    	//long ret = db.insert(TABLE_ANSWERS, null, values);
    	//Log.i(TAG, "ret value = " + ret);
    	long ret = db.insert(TABLE_ANSWERS, null, values);
    	
    	return ret;
    }
    
    // SELECT * FROM ANSWERS
    public List<Answer> getAnswers () {
    	SQLiteDatabase db = getReadableDatabase();
    	
    	Cursor mCursor = db.query(TABLE_ANSWERS,
    			ANSWERS_ALL_COLUMNS,
    			null,
    			null,
    			null,
    			null,
    			null);
    	
    	// Get ids of columns
    	final int idIdx = mCursor.getColumnIndex(ANSWERS_COLUMN_ID);    	
    	final int textIdx = mCursor.getColumnIndex(ANSWERS_COLUMN_TEXT);    	
    	final int userIdIdx = mCursor.getColumnIndex(ANSWERS_COLUMN_USERID); 
    	final int questionIdIdx = mCursor.getColumnIndex(ANSWERS_COLUMN_QUESTIONID); 
    	
    	List<Answer> answers_list = new ArrayList<Answer>();
    	
    	// Add the questions to the list
    	while(mCursor.moveToNext()) {
    		final int id = mCursor.getInt(idIdx);
        	final String text = mCursor.getString(textIdx);
        	final int userId = mCursor.getInt(userIdIdx);
        	final int questionId = mCursor.getInt(questionIdIdx);
        	Answer answer = new Answer(text, userId, questionId);
        	answer.setId(id);
    		answers_list.add(answer);
    	}
    	
    	return answers_list;
    }
    
    // DELETE * FROM ANSWERS
    public void deleteAnswers () {
    	Log.i(TAG, "Deleting answers");
    	SQLiteDatabase db = getWritableDatabase();
    	
    	db.execSQL("delete from " + TABLE_ANSWERS);
    	db.close();
    }
    
    
    
}
