package tw.edu.ntut.game.flower;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class GameOver extends Activity
{
	private TextView _scoreTextView;
	private SharedPreferences _highScore;
	private TextView _hightScoreTextView;
	private static final String PREF = "highScorePref";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle b = (getIntent().getExtras());
		
	    if(b.getBoolean("KEY_IS_WIN", false))
	    {
	    	setContentView(R.layout.score);
	    	_hightScoreTextView = (TextView) findViewById(R.id._highScoreTextView);
	    	_scoreTextView = (TextView) findViewById(R.id._scoreTextView);
	    	_scoreTextView.setText("       "+String.valueOf(b.getInt("KEY_SCORE", -1)));
	    	_scoreTextView.setTextColor(GameOver.this.getResources().getColor(R.color.col_brown));
	    	
	    	_highScore = getSharedPreferences(PREF, Context.MODE_PRIVATE);
	    	if(b.getInt("KEY_SCORE", -1)>=_highScore.getInt("HIGH_SCORE", 0))
	    	{
	    		SharedPreferences.Editor _editHighScore = _highScore.edit();
	    		_editHighScore.putInt("HIGH_SCORE",b.getInt("KEY_SCORE", -1));
	    		_editHighScore.commit();
	    	}
	    	
	    	_hightScoreTextView.setText(GameOver.this.getResources().getString(R.string.str_best)+_highScore.getInt("HIGH_SCORE", 0));
	    }
	    else
	    {
	    	setContentView(R.layout.gameover);
	    }
	}
}
