package tw.edu.ntut.game.flower;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

@SuppressWarnings("unused")
public class Flower {
	private GameView _gameView;
	private Bitmap _flower;
	private int _width;
	private int _height;
	private int _x = 0;
	private int _y = 0;
	private int _direction = 3;//0上,1下,2左,3右
	private int _currentFrame = 0;
	private int _currentFrameDelay = 0;
	private static final int CURRENT_FRAME_DELAY_TIMES = 3;
	private static final int SPEED = 9;
	private int _speed = SPEED;
	private static final int STEP = 1;
	private int _speedX = STEP;
	private int _speedY = -STEP;
	private int _numOfRow;
	private int _numOfColumn;
	//private static final int WALL_WIDTH = 20;//pixel
	private int _scaledWallWidth = 0;
	private Bitmap _maze;
	private boolean _isWantToTurn = false;
	private int _expectDirection = -1;
	private boolean _isConfused = false;

	public void setStage(Bitmap stage) 
	{
		this._maze = stage;
	}
	
	public void setConfused(boolean c){
		this._isConfused = c;
	}
	
	public void setSpeed(int s){
		if(s>0){
			this._speed+=s;
		}else if(s<0){
			if((_speed+s)<0){
				
			}else{
				_speed+=s;
			}
		}
	}
	
	public Flower(GameView _gameView, Bitmap _flower, int scaledWallWidth) {
		super();
		this._gameView = _gameView;
		this._flower = _flower;
		this._width = _flower.getWidth()/4;
		this._height = _flower.getHeight()/2;
		this._scaledWallWidth = scaledWallWidth;
		this._x = (int) (0 + scaledWallWidth);
		this._y = (int) (0 + scaledWallWidth);
		_numOfColumn = 4;
		_numOfRow = 2;
	}

	private void update()
	{
		if(_currentFrameDelay < CURRENT_FRAME_DELAY_TIMES){
			++_currentFrameDelay;
		}else{
			_currentFrameDelay = 0;
			_currentFrame = ++_currentFrame % _numOfRow;
		}
		
		switch(_direction){
			case 0://上
			case 1://下
				if(!isOccurWall(_x, _y + _speedY, _direction)){
					_y = _y + _speedY;
				}
				break;
			case 2://左
			case 3://右
				if(!isOccurWall(_x + _speedX, _y, _direction)){
					_x = _x + _speedX;
				}
				break;
			default:
				break;
		}
	}
	
	public boolean isOccurWall(int nextX, int nextY, int direction){
		switch(direction){
			case 0://往上
				for(int i = nextX; i < nextX + this._width; i++){
					if(_maze.getPixel(i, nextY) != Color.TRANSPARENT){
						return true;
					}
				}
				break;
			case 1://往下
				for(int i = nextX; i < nextX + this._width; i++){
					if(_maze.getPixel(i, nextY + this._height) != Color.TRANSPARENT){
						return true;
					}
				}
				break;
			case 2://往左
				for(int i = nextY; i < nextY + this._height; i++){
					if(_maze.getPixel(nextX, i) != Color.TRANSPARENT){
						return true;
					}
				}
				break;
			case 3://往右
				for(int i = nextY; i < nextY + this._height; i++){
					if(_maze.getPixel(nextX + this._width, i) != Color.TRANSPARENT){
						return true;
					}
				}
				break;
			default:
				break;
		}
		return false;
	}
	
	public void setExpectDirection(int expectDirection){
		if(_isConfused){
			if(expectDirection==0){
				this._expectDirection = 1;
			}else if(expectDirection==1){
				this._expectDirection =0;
			}else if(expectDirection==2){
				this._expectDirection=3;
			}else if(expectDirection==3){
				this._expectDirection=2;
			}
		}else{
			this._expectDirection = expectDirection;
		}
		this._isWantToTurn = true;
	}
	
	public void turn(){
		boolean readyToTurn = false;
		switch(this._expectDirection){
			case 0://上
				if(!isOccurWall(_x, _y - 1 - 10, _expectDirection)){
					readyToTurn = true;
				}
				break;
			case 1://下
				if(!isOccurWall(_x, _y + 1 + 10, _expectDirection)){
					readyToTurn = true;
				}
				break;
			case 2://左
				if(!isOccurWall(_x - 1 - 7, _y, _expectDirection)){
					readyToTurn = true;
				}
				break;
			case 3://右
				if(!isOccurWall(_x + 1 + 7, _y, _expectDirection)){
					readyToTurn = true;
				}
				break;
			default:
				break;
		}
		if(readyToTurn){
			switch(this._expectDirection){
				case 0:
					this._speedY = -STEP;
					break;
				case 1:
					this._speedY = STEP;
					break;
				case 2:
					this._speedX = -STEP;
					break;
				case 3:
					this._speedX = STEP;
					break;
				default:
					break;
			}
			this._direction = _expectDirection;
			this._isWantToTurn = false;
		}
	}
	
	public int getX(){
		return this._x;
	}
	
	public int getY(){
		return this._y;
	}
	
	public void setX(int x){
		this._x = x;
	}
	
	public void setY(int y){
		this._y = y;
	}
	
	public int getWidth(){
		return this._width;
	}
	
	public int getHeight(){
		return this._height;
	}
	
	@SuppressLint({ "DrawAllocation" })
	public void onDraw(Canvas canvas)
	{
		for(int i=0; i<_speed; i++){
			if(_isWantToTurn){
				turn();
			}
			update();
		}
		
		int srcX = _direction * this._width;
		int srcY = _currentFrame * this._height;
		Rect src = new Rect(srcX, srcY, srcX+this._width, srcY+this._height);
		Rect dst = new Rect(_x, _y, _x + this._width, _y + this._height);
		canvas.drawBitmap(this._flower, src, dst, null);
	}
}
