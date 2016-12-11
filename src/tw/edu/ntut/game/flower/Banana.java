package tw.edu.ntut.game.flower;

import java.util.Random;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

@SuppressWarnings("unused")
public class Banana {
	private GameView _gameView;
	private Bitmap _banana;
	private int _width;
	private int _height;
	private int _x = 0;
	private int _y = 0;
	private int _direction = 2;//0�W,1�U,2��,3�k
	private int _currentFrame = 0;
	private int _currentFrameDelay = 0;
	private static final int CURRENT_FRAME_DELAY_TIMES = 3;
	private static final int SPEED = 9;
	private int _speed = SPEED;
	private static final int STEP = 1;
	private int _speedX = -STEP;
	private int _speedY = -STEP;
	private int _numOfRow;
	private int _numOfColumn;
	//private static final int WALL_WIDTH = 20;//pixel
	private int _scaledWallWidth = 0;
	private Bitmap _maze;
	private boolean _isWantToTurn = false;
	private int _expectDirection = -1;
	private int _changeDirectionDelay = 0;
	private static final int CHANGE_DIRECTION_DELAY = 10;
	private static final int TURN_DELAY = 30;
	private int _turnDelay = 0;

	public Banana(GameView _gameView, Bitmap _banana, int scaledWallWidth) {
		super();
		this._gameView = _gameView;
		this._banana = _banana;
		this._width = _banana.getWidth()/4;
		this._height = _banana.getHeight()/2;
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
			case 0://�W
			case 1://�U
				if(!isOccurWall(_x, _y + _speedY, _direction)){
					_y = _y + _speedY;
				}
				break;
			case 2://��
			case 3://�k
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
			case 0://���W
				for(int i = nextX; i < nextX + this._width; i++){
					if(_maze.getPixel(i, nextY) != Color.TRANSPARENT){
						return true;
					}
				}
				break;
			case 1://���U
				for(int i = nextX; i < nextX + this._width; i++){
					if(_maze.getPixel(i, nextY + this._height) != Color.TRANSPARENT){
						return true;
					}
				}
				break;
			case 2://����
				for(int i = nextY; i < nextY + this._height; i++){
					if(_maze.getPixel(nextX, i) != Color.TRANSPARENT){
						return true;
					}
				}
				break;
			case 3://���k
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
		this._expectDirection = expectDirection;
		this._isWantToTurn = true;
	}
	
	public void turn(){
		boolean readyToTurn = false;
		switch(this._expectDirection){
			case 0://�W
				if(!isOccurWall(_x, _y - 1 - 10, _expectDirection)){
					readyToTurn = true;
				}
				break;
			case 1://�U
				if(!isOccurWall(_x, _y + 1 + 10, _expectDirection)){
					readyToTurn = true;
				}
				break;
			case 2://��
				if(!isOccurWall(_x - 1 - 7, _y, _expectDirection)){
					readyToTurn = true;
				}
				break;
			case 3://�k
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

	public void setStage(Bitmap stage) 
	{
		this._maze = stage;
	}
	
	public void setInitPosition(){
		_x = (this._maze.getWidth() - this._width) - this._scaledWallWidth;
		_y = (this._maze.getHeight() - this._height) - this._scaledWallWidth;
	}
	
	public int getX(){
		return this._x;
	}
	
	public int getY(){
		return this._y;
	}
	
	public int getWidth(){
		return this._width;
	}
	
	public int getHeight(){
		return this._height;
	}
	
	public boolean isWantTurn(){
		return this._isWantToTurn;
	}
	
	/*
	   �٦��ݭק���o�����P�_�A�H�U�P�_�|��bug�A
	 ex:���b�k�W�A�������k��P�W����������ɷ|�d��(�|�@�����k�W��)
	 */
	public int checkFlowerPosition(int flowerX, int flowerY){
		if(_x <= flowerX && _y <= flowerY){//�b�ᥪ�W
			if((flowerX - _x) >= (flowerY - _y)){//���k�Z���j��W�U�Z��
				return 3;//���k
			}else{
				return 1;//���U
			}
		}else if(_x <= flowerX && _y >= flowerY){//�b�ᥪ�U
			if((flowerX - _x) >= (_y - flowerY)){//���k�Z���j��W�U�Z��
				return 3;//���k
			}else{
				return 0;//���W
			}
		}else if(_x >= flowerX && _y <= flowerY){//�b��k�W
			if((_x - flowerX) >= (flowerY - _y)){//���k�Z���j��W�U�Z��
				return 2;//����
			}else{
				return 1;//���U
			}
		}else if(_x >= flowerX && _y >= flowerY){//�b��k�U
			if((_x - flowerX) >= (_y - flowerY)){//���k�Z���j��W�U�Z��
				return 2;//����
			}else{
				return 0;//���W
			}
		}
		//�L�k��O���H����V
		int maxNum = 4;
		int minNum = 0;
		Random random = new Random();
		int randomNumber = random.nextInt(maxNum - minNum) + minNum;
		return randomNumber;
	}
	
	@SuppressLint({ "DrawAllocation" })
	public void onDraw(Canvas canvas, int flowerPosition)
	{
		if(!_isWantToTurn){
			if(_changeDirectionDelay > CHANGE_DIRECTION_DELAY){
				setExpectDirection(flowerPosition);
				_changeDirectionDelay=0;
			}else{
				_changeDirectionDelay++;
			}
		}
		for(int i=0; i<_speed; i++){
			if(_isWantToTurn){
				_turnDelay++;
				if(_turnDelay > TURN_DELAY){
					_turnDelay = 0;
					_isWantToTurn = false;//���ɷ|�µۦ�������a����V�A�|�d��A�n������V�N�@�A���s�P�O�᪺��m
				}
				turn();
			}
			update();
		}
		
		int srcX = _direction * this._width;
		int srcY = _currentFrame * this._height;
		Rect src = new Rect(srcX, srcY, srcX+this._width, srcY+this._height);
		Rect dst = new Rect(_x, _y, _x + this._width, _y + this._height);
		canvas.drawBitmap(this._banana, src, dst, null);
		//canvas.drawBitmap(_banana, _x, _y, null);
	}
}
