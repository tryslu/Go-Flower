package tw.edu.ntut.game.flower;

import java.util.Random;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Food {
	private GameView _gameView;
	private Bitmap _food;
	private boolean _isSkill;
	private boolean _isGood;
	private boolean _isSpecial;
	private String _type;
	private int _x;
	private int _y;
	private int _width;
	private int _height;
	private int _frame;
	private int _columnPosition;
	private int _rowPosition;
	private int _scaledWallWidth = 0;
	
	public Food(GameView _gameView, Bitmap _food, boolean isSkill, boolean isGood, boolean isSpecial, int scaledWallWidth) {
		super();
		this._gameView = _gameView;
		this._food = _food;
		if(isSkill){
			this._width = _food.getWidth()/3;
			this._height = _food.getHeight();
			this._frame = getRandomNumber(0, 3);
			switch(this._frame){
				case 0:
					this._type = "up";
					break;
				case 1:
					this._type = "slow";
					break;
				case 2:
					this._type = "change";
					break;
				default:
					break;
			}
		}else{
			if(isGood){
				if(isSpecial){
					this._width = _food.getWidth()/2;
					this._height = _food.getHeight();
					this._frame = getRandomNumber(0, 2);
					switch(this._frame){
					case 0:
						this._type = "goldenApple";
						break;
					case 1:
						this._type = "star";
						break;
					default:
						break;
				}
				}else{
					this._width = _food.getWidth()/8;
					this._height = _food.getHeight();
					this._frame = getRandomNumber(0, 8);
					this._type = "goodFood";
				}
			}else{
				this._width = _food.getWidth()/4;
				this._height = _food.getHeight();
				this._frame = getRandomNumber(0, 4);
				this._type = "badFood";
			}
		}
		
		this._scaledWallWidth = scaledWallWidth;
	}
	
	private int getRandomNumber(int min, int max){
		int maxNum = max;
		int minNum = min;
		Random random = new Random();
		int randomNumber = random.nextInt(maxNum - minNum) + minNum;
		return randomNumber;
	}
	
	public void setPosition(int column, int row){
		this._columnPosition = column;
		this._rowPosition = row;
		this._x = ((this._scaledWallWidth + this._width + 6)*this._columnPosition) + this._scaledWallWidth;
		this._y = ((this._scaledWallWidth + this._height + 6)*this._rowPosition) + this._scaledWallWidth;
	}
	
	public int getColumnPosition(){
		return this._columnPosition;
	}
	
	public int getRowPosition(){
		return this._rowPosition;
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
	
	public String getType(){
		return this._type;
	}
	
	@SuppressLint({ "DrawAllocation" })
	public void onDraw(Canvas canvas)
	{
		int srcX = _frame * this._width;
		int srcY = 0;//this._height;
		Rect src = new Rect(srcX, srcY, srcX+this._width, srcY+this._height);
		Rect dst = new Rect(_x, _y, _x + this._width, _y + this._height);
		canvas.drawBitmap(this._food, src, dst, null);
	}
}
