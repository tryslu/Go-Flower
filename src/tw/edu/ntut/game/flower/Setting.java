package tw.edu.ntut.game.flower;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.ToggleButton;

public class Setting extends Activity
{
    private AudioManager _audioManager;
    
    private int _version;
    //volumn
    private ImageView _volumeImageView;
    private SeekBar _volumeSeekBar;   
    //vibrate
    private ImageView _vibrateImageView;
    private Switch _vibrateSwitch;
    private CheckBox _vibrateCheckBox;
    private ToggleButton _vibrateToggleButton;
    private Vibrator _vibrator;

    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		init();

		_version = Build.VERSION.SDK_INT;
		_volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
			{
				// TODO Auto-generated method stub
				_audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
				ChangeVolumnImage(progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}			
		});

		_vibrateToggleButton.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				// TODO Auto-generated method stub
				//_vibrator.
				ChangeVibrateImage(isChecked);
			}		    
		});
	}
	
	private void init()
	{
	    //volumn
	    _volumeImageView = (ImageView) findViewById(R.id._volumeImageView);
	    _volumeSeekBar = (SeekBar) findViewById(R.id._volumeSeekBar); 
	    _audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        _volumeSeekBar.setMax(_audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        _volumeSeekBar.setProgress(_audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)); 
        ChangeVolumnImage(_volumeSeekBar.getProgress());
        
	    //vibrate
	    _vibrateImageView = (ImageView) findViewById(R.id._vibreateImageView);
	    _vibrateToggleButton = (ToggleButton) findViewById(R.id._vibrateToggleButton);
	  //  _telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
	    _vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	    SharedPreferences _sp = getSharedPreferences("mypref", Context.MODE_PRIVATE);
	    _vibrateToggleButton.setChecked(_sp.getBoolean("KEY_IS_VIBRATE", false));
	    //ChangeVibrateImage(_vibrateToggleButton.isChecked());
	  //  _vibrateSwitch = (Switch) findViewById(R.id._vibrateSwitch);
	  //  _vibrateCheckBox = (CheckBox) findViewById(R.id._vibrateCheckBox);
	}
	
	private void ChangeVolumnImage(int progress)
	{
		if(progress==0)
		{
			_volumeImageView.setImageResource(R.drawable.volume_off);
		}
		else
		{
			_volumeImageView.setImageResource(R.drawable.volume_on);
		}
	}
	
	private void ChangeVibrateImage(boolean isChecked)
	{
		if(isChecked==false)
		{
			_vibrateImageView.setImageResource(R.drawable.vibrate_off);
			SharedPreferences sp = getSharedPreferences("mypref", Context.MODE_PRIVATE);
			SharedPreferences.Editor se = sp.edit();
			se.putBoolean("KEY_IS_VIBRATE", false);
			se.commit();
		}
		else
		{
			_vibrateImageView.setImageResource(R.drawable.vibrate_on);
			SharedPreferences sp = getSharedPreferences("mypref", Context.MODE_PRIVATE);
			SharedPreferences.Editor se = sp.edit();
			se.putBoolean("KEY_IS_VIBRATE", true);
			se.commit();
			_vibrator.vibrate(250);
		}
	}
}
