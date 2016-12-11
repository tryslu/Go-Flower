package tw.edu.ntut.game.flower;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Help extends Activity
{
	TextView _textView;
	TextView _addTextView;
	TextView _minusTextView;
	TextView _otherTextView;
	TextView _skillTextView;
	TextView _enemyTextView;
	ImageView _addFruitImageView;
	ImageView _minusFruitImageView;
	ImageView _otherImageView;
	ImageView _skillImageView;
	ImageView _enemyImageview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		init();
			
		_textView.setText("　　玩家須在限制時間內吃蔬果，盡可能累積分數。");
		
		_addTextView.setText("　　加分項：每吃一個加分項加一分");
		_minusTextView.setText("　　扣分項：每吃一個扣分項減一分");
		_otherTextView.setText("　　特殊食品：金蘋果可以加十分、彩色星星可以將總分X2");
		_skillTextView.setText("　　其他技能：技能維持十秒鐘(可重疊)，分別為加速、減速、方向鍵對調");
		_enemyTextView.setText("　　敵人：在遊玩期間不可觸碰到敵人否則會直接死亡。");
		
		_addFruitImageView.setImageBitmap(getBitmap(R.drawable.fruit_add));
		_minusFruitImageView.setImageBitmap(getBitmap(R.drawable.fruit_minus));
		_otherImageView.setImageBitmap(getBitmap(R.drawable.other));
		_skillImageView.setImageBitmap(getBitmap(R.drawable.skill));
		_enemyImageview.setImageBitmap(getBitmap(R.drawable.enemy));
	}
	
	private void init()
	{
		_textView = (TextView) findViewById(R.id._textView);
		_addTextView = (TextView) findViewById(R.id._addTextView);		
		_minusTextView = (TextView) findViewById(R.id._minusTextView);
		_otherTextView = (TextView) findViewById(R.id._otherTextView);
		_skillTextView = (TextView) findViewById(R.id._skillTextView);
		_enemyTextView = (TextView) findViewById(R.id._enemyTextView);
		
		_addFruitImageView = (ImageView) findViewById(R.id._addFruitImageView);
		_minusFruitImageView = (ImageView) findViewById(R.id._minusImageView);
		_otherImageView = (ImageView) findViewById(R.id._otherImageView);
		_skillImageView = (ImageView) findViewById(R.id._skillImageView);
		_enemyImageview = (ImageView) findViewById(R.id._enemyImageview);
	}
	
	public Bitmap getBitmap(int id){
	    //Only decode image size. Not whole image
        BitmapFactory.Options option = new BitmapFactory.Options();
//        option.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(getResources(), id, option);
 
        //The new size to decode to 
//        final int NEW_SIZE=100;
        
        //Now we have image width and height. We should find the correct scale value. (power of 2)
//        int width=option.outWidth;
//        int height=option.outHeight;
        int scale = 3;
//        while(true){
//            if(width/2<NEW_SIZE || height/2<NEW_SIZE)
//                break;
//            width/=2;
//            height/=2;
//            scale++;
//        }
        //Decode again with inSampleSize
//      option = new BitmapFactory.Options();
        option.inSampleSize = scale;
        return BitmapFactory.decodeResource(getResources(), id, option);
	}
}
