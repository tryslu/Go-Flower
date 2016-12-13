package tw.edu.ntut.game.flower;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GameReady extends Activity 
{
	private Button _startButton, _settingsButton, _exitButton, _helpButton;
	private ImageView _tabToStart;
	private SharedPreferences _tabToStartSharedPreference;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        init();
        _startButton.setOnClickListener(new Button.OnClickListener()
        {
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub		
			_tabToStart.setVisibility(View.VISIBLE);
			_tabToStart.setImageBitmap(getBitmap(R.drawable.tab_to_start));
		}
	});
        
        _tabToStart.setOnClickListener(new ImageView.OnClickListener()
	{
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			try
			{
				Intent intent = new Intent(GameReady.this, GameRunning.class);
				startActivity(intent);
				_tabToStart.setVisibility(View.GONE);
			}
			catch(Exception e){}
		}					
	});
        
        _settingsButton.setOnClickListener(new Button.OnClickListener()
        {
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub				
			try
			{
				Intent intent = new Intent(GameReady.this, Setting.class);
				startActivity(intent);
			}
			catch(Exception e){}	
		}
	});
        
        _helpButton.setOnClickListener(new Button.OnClickListener()
        {
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			try
			{
				Intent intent = new Intent(GameReady.this, Help.class);
				startActivity(intent);
			}
			catch(Exception e){}
		}
	});
        
        _exitButton.setOnClickListener(new Button.OnClickListener()
        {
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			finish();
		}
	});
    }
    
    private void init()
    {
    	_tabToStart = (ImageView) findViewById(R.id._tabImageView);
    	_startButton = (Button) findViewById(R.id._indexStartButton);
    	_settingsButton = (Button) findViewById(R.id._indexSettingsButton);
    	_exitButton = (Button) findViewById(R.id._indexExitButton);
    	_helpButton = (Button) findViewById(R.id._indexHelpButton);
    	
    	_tabToStart.setVisibility(View.GONE);
    }
    
	public Bitmap getBitmap(int id){
	    //Only decode image size. Not whole image
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), id, option);
 
        //The new size to decode to 
        final int NEW_SIZE=100;
        
        //Now we have image width and height. We should find the correct scale value. (power of 2)
        int width=option.outWidth;
        int height=option.outHeight;
        int scale=1;
        while(true){
            if(width/2<NEW_SIZE || height/2<NEW_SIZE)
                break;
            width/=2;
            height/=2;
            scale++;
        }
        //Decode again with inSampleSize
        option = new BitmapFactory.Options();
        option.inSampleSize=scale;
        return BitmapFactory.decodeResource(getResources(), id, option);
	}
}
