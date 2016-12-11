package tw.edu.ntut.game.flower;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

public class GameRunning extends Activity
{
	private MediaPlayer mMediaPlayer = new MediaPlayer();
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(new GameView(GameRunning.this));
		mMediaPlayer = MediaPlayer.create(GameRunning.this, R.raw.gavotte);
		mMediaPlayer.setLooping(true);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(mMediaPlayer.isPlaying())
			mMediaPlayer.pause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMediaPlayer.start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mMediaPlayer.release();
		super.onDestroy();
	}
}
