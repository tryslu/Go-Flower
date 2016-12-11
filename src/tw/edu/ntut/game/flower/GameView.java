package tw.edu.ntut.game.flower;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class GameView extends SurfaceView {
	private Context _context;
	private WindowManager wm;
	private int _width;
	private int _height;
	private SurfaceHolder _holder;
	private GameThread _gameThread;
	private Bitmap _background;
	private Bitmap _mazeOfStage1;
	private Flower _flower;
	//private static final int WALL_WIDTH = 20;//pixel //not use
	private int _scaledWallWidth = 0;
	private int _pieceOfMazeWidth;
	private Banana _banana;
	private int _playingTime = 30;
	private Paint _playingTimePaint = new Paint();
	private Handler _mHandler = new Handler();
	private Runnable _mRunnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(_gameThread.getRunning()){
				if(_playingTime > 10){
					_playingTimePaint.setColor(getResources().getColor(R.color.black));
					_playingTime--;
					_mHandler.postDelayed(_mRunnable, 1000);
				}else if(_playingTime > 0){
					if(_playingTimePaint.getColor() == getResources().getColor(R.color.black)){
						_playingTimePaint.setColor(getResources().getColor(R.color.red));
					}else{
						_playingTimePaint.setColor(getResources().getColor(R.color.black));
					}
					_playingTime--;
					_mHandler.postDelayed(_mRunnable, 1000);
				}else{
					synchronized (getHolder()) {
						try {
							Bundle extras = new Bundle();
							extras.putBoolean("KEY_IS_WIN", true);
							extras.putInt("KEY_SCORE", _score);
							Intent intent = new Intent(_context, GameOver.class);
							intent.putExtras(extras);
							_context.startActivity(intent);
							((Activity)_context).finish();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
			}else{
				_mHandler.removeCallbacks(_mRunnable);
			}
		}
	};
	private Runnable _upSpeed = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(_gameThread.getRunning()){
				_flower.setSpeed(4);
			}else{
				_mHandler.removeCallbacks(_upSpeed);
			}
		}
	};
	private Runnable _downSpeed = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(_gameThread.getRunning()){
				_flower.setSpeed(-4);
			}else{
				_mHandler.removeCallbacks(_downSpeed);
			}
		}
	};
	private Runnable _notConfused = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			_flower.setConfused(false);
		}
	};
	private int _score = 0;
	private Paint _scorePaint = new Paint();
	private ArrayList<Food> _foodList = new ArrayList<Food>();
	private Vibrator myVibrator;
	private SharedPreferences _sp;
	private boolean _isVibrate;
	
	
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
	
	public void createBackground(){
		//create and scale background
		Bitmap temp = getBitmap(R.drawable.background);
		_background = Bitmap.createScaledBitmap(temp, this._width, this._height, false);
	}
	
	public void createStage1(){
		//generate and scale stage1
		Bitmap temp = getBitmap(R.drawable.stage1);
		_mazeOfStage1 = Bitmap.createScaledBitmap(temp, (int)( ((double)temp.getWidth()) * ( ((double)this._height) / ((double)temp.getHeight()) ) ), this._height, false);
		//Log.i("debug", "tempMaze.width="+temp.getWidth() + " tempMaze.height="+temp.getHeight());
		//Log.i("debug", "maze.width="+_mazeOfStage1.getWidth() + " maze.height="+_mazeOfStage1.getHeight());
	}
	
	public void scaleWallWidth(){
		//scale stage's wall width
		int i=0;
		while(_mazeOfStage1.getPixel(i, i) != Color.TRANSPARENT){
			i++;
		}
		_scaledWallWidth = i;
		Log.i("debug", "scaledWallWidth=" + _scaledWallWidth);
	}
	
	private void getPieceOfMazeWidth(){
		int i = _scaledWallWidth;
		while(_mazeOfStage1.getPixel(i, i) == Color.TRANSPARENT){
			i++;
		}
		int width = i - _scaledWallWidth;
		width-=2;
		this._pieceOfMazeWidth = width;
	} 
	
	public void createFlower(){
		Bitmap temp = getBitmap(R.drawable.flower);
		int flowerWidth = this._pieceOfMazeWidth;
		int flowerHeight = flowerWidth;
		Bitmap flower = Bitmap.createScaledBitmap(temp, flowerWidth * 4, flowerHeight * 2, false);
		//Log.i("debug", "temp flower.width="+temp.getWidth() + " temp flower.height="+temp.getHeight());
		//Log.i("debug", "flower.width="+flower.getWidth() + " flower.height="+flower.getHeight());
		_flower = new Flower(this, flower, _scaledWallWidth);
	}
	
	public void createBanana(){
		Bitmap temp = getBitmap(R.drawable.banana);
		int bananaWidth = this._pieceOfMazeWidth;
		int bananaHeight = bananaWidth;
		Bitmap banana = Bitmap.createScaledBitmap(temp,  bananaWidth * 4, bananaHeight * 2, false);
		_banana = new Banana(this, banana, _scaledWallWidth);
	}
	
	private void createFood(){
		for(int i=0; i<5; i++){
			_foodList.add(newFood());
			Random random = new Random();
			int maxC = 13;
			int minC = 0;
			int maxR = 9;
			int minR = 0;
			int count = 0;
			while(true){
				int randC = random.nextInt(maxC - minC) + minC;
				int randR = random.nextInt(maxR - minR) + minR;
				for(int j=0; j<_foodList.size(); j++){
					if(randC!=_foodList.get(j).getColumnPosition() || randR!=_foodList.get(j).getRowPosition()){
						count++;
					}
				}
				if(count == _foodList.size()){
					_foodList.get(i).setPosition(randC, randR);
					break;
				}
			}
		}
	}
	
	private Food newFood(){
		int maxNum = 100;
		int minNum = 0;
		Random random = new Random();
		int randomNumber = random.nextInt(maxNum - minNum) + minNum;
		if(/*randomNumber >= 0 &&*/ randomNumber < 15){
			//golden apple and star
			//Bitmap temp = getBitmap(R.drawable.other);
			Bitmap food = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.other), this._pieceOfMazeWidth * 2, this._pieceOfMazeWidth, false);
			return new Food(this, food, false, true, true, _scaledWallWidth);
		}else if(randomNumber >= 15 && randomNumber < 40){
			//skills
			//Bitmap temp = getBitmap(R.drawable.skill);
			Bitmap food = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.skill), this._pieceOfMazeWidth * 3, this._pieceOfMazeWidth, false);
			return new Food(this, food, true, false, false, _scaledWallWidth);
		}else if(randomNumber >= 40 && randomNumber < 80){
			//good food
			//Bitmap temp = getBitmap(R.drawable.fruit_add);
			Bitmap food = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.fruit_add), this._pieceOfMazeWidth * 8, this._pieceOfMazeWidth, false);
			return new Food(this, food, false, true, false, _scaledWallWidth);
		}else if(randomNumber >= 80){
			//bad food
			//Bitmap temp = getBitmap(R.drawable.fruit_minus);
			Bitmap food = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.fruit_minus), this._pieceOfMazeWidth * 4, this._pieceOfMazeWidth, false);
			return new Food(this, food, false, false, false, _scaledWallWidth);
		}
		//Bitmap temp = getBitmap(R.drawable.fruit_minus);
		Bitmap food = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.fruit_minus), this._pieceOfMazeWidth * 4, this._pieceOfMazeWidth, false);
		return new Food(this, food, false, false, false, _scaledWallWidth);
	}
	
	private void setLastFoodPosition(){
		Random random = new Random();
		int maxC = 13;
		int minC = 0;
		int maxR = 9;
		int minR = 0;
		int count = 0;
		while(true){
			int randC = random.nextInt(maxC - minC) + minC;
			int randR = random.nextInt(maxR - minR) + minR;
			for(int j=0; j<_foodList.size(); j++){
				if(randC!=_foodList.get(j).getColumnPosition() || randR!=_foodList.get(j).getRowPosition()){
					count++;
				}
			}
			if(count == _foodList.size()){
				_foodList.get(_foodList.size()-1).setPosition(randC, randR);
				break;
			}
			count = 0;
		}
	}
	
	@SuppressWarnings("deprecation")
	public GameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this._context = context;
		
		//get window size
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display d = wm.getDefaultDisplay();
		//Point size = new Point();
		//d.getSize(size);
		this._width = d.getWidth();//size.x; //This method(display.getWidth() or getHeight()) is deprecated.
		this._height = d.getHeight();//size.y;
		
		_playingTimePaint.setColor(getResources().getColor(R.color.black));
		_playingTimePaint.setTextSize(72);
		_scorePaint.setColor(getResources().getColor(R.color.black));
		_scorePaint.setTextSize(72);
		
		myVibrator = (Vibrator) _context.getSystemService(Service.VIBRATOR_SERVICE);
		_sp = _context.getSharedPreferences("mypref", Context.MODE_PRIVATE);
		_isVibrate = _sp.getBoolean("KEY_IS_VIBRATE", false);
		
		createBackground();
		createStage1();
		scaleWallWidth();
		getPieceOfMazeWidth();
		createFlower();
		_flower.setStage(_mazeOfStage1);
		createBanana();
		_banana.setStage(_mazeOfStage1);
		_banana.setInitPosition();
		createFood();
		_mHandler.postDelayed(_mRunnable, 1000);
		
		//create game thread
		_holder = getHolder();
	    _holder.addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				try {
					_gameThread = new GameThread(GameView.this);
					_gameThread.setRunning(true);
					_gameThread.start();
				} catch (Exception e) {
					// TODO: handle exception
					boolean isRetry = true;
					_gameThread.setRunning(false);
					while(isRetry){
						try {
							_gameThread.join();
						} catch (InterruptedException ec) {
							// TODO Auto-generated catch block
							ec.printStackTrace();
						}
						isRetry = false;
					}
					_gameThread = new GameThread(GameView.this);
					_gameThread.setRunning(true);
					_gameThread.start();
				}
//				_gameThread = new GameThread(GameView.this);
//				_gameThread.setRunning(true);
//				_gameThread.start();
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				boolean _isRetry = true;
				_gameThread.setRunning(false);
				while(_isRetry)
				{
					try
					{
						_gameThread.join();
					}
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
					_isRetry = false;
				}
			}
		});
	}
	
	private boolean isCollision(int b1X, int b1Y, int b1Width, int b1Height, int b2X, int b2Y, int b2Width, int b2Height){
		if((b1X < b2X+b2Width) && (b1Y < b2Y+b2Height) && (b1X+b1Width > b2X) && (b1Y+b1Height > b2Y)){
			return true;
		}
		return false;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawBitmap(_background, 0, 0, null);
		canvas.drawBitmap(_mazeOfStage1, 0, 0, null);

		for (Food f : _foodList) {
			f.onDraw(canvas);
		}
		
		_flower.onDraw(canvas);
		_banana.onDraw(canvas, _banana.checkFlowerPosition(this._flower.getX(), this._flower.getY()));
		
		int removeCount = 0;
		int removeTarget = -1;
		for (int i=0; i<_foodList.size(); i++) {
			if(isCollision(_foodList.get(i).getX(), _foodList.get(i).getY(), _foodList.get(i).getWidth(), _foodList.get(i).getHeight(), _flower.getX(), _flower.getY(), _flower.getWidth(), _flower.getHeight())){
				if(_isVibrate){
					myVibrator.vibrate(250);
				}
				if(_foodList.get(i).getType().equalsIgnoreCase("goldenApple")){
					_score+=10;
				}else if(_foodList.get(i).getType().equalsIgnoreCase("star")){
					_score*=2;
				}else if(_foodList.get(i).getType().equalsIgnoreCase("goodFood")){
					_score++;
				}else if(_foodList.get(i).getType().equalsIgnoreCase("badFood")){
					_score--;
				}else if(_foodList.get(i).getType().equalsIgnoreCase("up")){
					_flower.setSpeed(4);
					_mHandler.postDelayed(_downSpeed, 10000);//10sec effect
				}else if(_foodList.get(i).getType().equalsIgnoreCase("slow")){
					_flower.setSpeed(-4);
					_mHandler.postDelayed(_upSpeed, 10000);
				}else if(_foodList.get(i).getType().equalsIgnoreCase("change")){
					_flower.setConfused(true);
					_mHandler.postDelayed(_notConfused, 10000);
				}
				removeTarget = i;
			}
		}
		if(removeTarget!=-1){
			Log.i("debug", "removeTarget="+removeTarget);
			_foodList.remove(removeTarget);
			removeCount++;
			Log.i("debug", "removeCount="+removeCount);
			_foodList.add(newFood());
			setLastFoodPosition();
			
		}
		
		if(isCollision(_banana.getX(), _banana.getY(), _banana.getWidth(), _banana.getHeight(), _flower.getX(), _flower.getY(), _flower.getWidth(), _flower.getHeight())){
			Log.i("debug", "banana kill the flower");
			Bitmap temp = getBitmap(R.drawable.blood);
			Bitmap temp2 = Bitmap.createScaledBitmap(temp, _pieceOfMazeWidth, _pieceOfMazeWidth, false);
			canvas.drawBitmap(temp2, _flower.getX(), _flower.getY(), null);
			//_flower.setX(-999);
			//_flower.setY(-999);
			try {
				Bundle extras = new Bundle();
				extras.putBoolean("KEY_IS_WIN", false);
				extras.putInt("KEY_SCORE", _score);
				Intent intent = new Intent(_context, GameOver.class);
				intent.putExtras(extras);
				_context.startActivity(intent);
				((Activity)_context).finish();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		canvas.drawText("Time: " + _playingTime, this.getWidth() - 250, 90, _playingTimePaint);
		canvas.drawText("Score", this.getWidth() - 270, this._height-144, _scorePaint);
		canvas.drawText("" + _score, this.getWidth() - 270, this._height-72, _scorePaint);
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		synchronized (getHolder()) {
			if((event.getX() > (float)this._width / 3) && (event.getX() < (float)this._width / 3 * 2) && (event.getY() < (float)this._height / 3)){
				_flower.setExpectDirection(0);
			}else if((event.getX() > (float)this._width / 3) && (event.getX() < (float)this._width / 3 * 2) && (event.getY() > (float)this._height / 3 * 2)){
				_flower.setExpectDirection(1);
			}else if((event.getX() < (float)this._width / 3) && (event.getY() > (float)this._height / 3) && (event.getY() < (float)this._height / 3 * 2)){
				_flower.setExpectDirection(2);
			}else if((event.getX() > (float)this._width / 3 * 2) && (event.getY() > (float)this._height / 3) && (event.getY() < (float)this._height / 3 * 2)){
				_flower.setExpectDirection(3);
			}
		}
		return super.onTouchEvent(event);
	}
}
