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
			
		_textView.setText("�@�@���a���b����ɶ����Y���G�A�ɥi��ֿn���ơC");
		
		_addTextView.setText("�@�@�[�����G�C�Y�@�ӥ[�����[�@��");
		_minusTextView.setText("�@�@�������G�C�Y�@�Ӧ�������@��");
		_otherTextView.setText("�@�@�S���~�G��ī�G�i�H�[�Q���B�m��P�P�i�H�N�`��X2");
		_skillTextView.setText("�@�@��L�ޯ�G�ޯ�����Q����(�i���|)�A���O���[�t�B��t�B��V����");
		_enemyTextView.setText("�@�@�ĤH�G�b�C���������iĲ�I��ĤH�_�h�|�������`�C");
		
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
