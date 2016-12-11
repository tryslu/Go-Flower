package tw.edu.ntut.game.flower;

import android.graphics.Canvas;

public class GameThread extends Thread {
	private boolean _isRunning = false;
    private GameView _view;
    private final long FPS = 30;
    
	public GameThread(GameView view) {
		super();
		this._view = view;
	}
	
    public void setRunning(boolean run){
    	this._isRunning = run;
    }
    
    public boolean getRunning(){
    	return this._isRunning;
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long ticksPS = 1000/FPS;
		long startTime;
		long sleepTime;
		
		while (_isRunning)
		{
		    Canvas c = null;
		    startTime = System.currentTimeMillis();
		    try
		    {
		    	c = this._view.getHolder().lockCanvas();
		    	synchronized (this._view.getHolder())
		    	{
					_view.onDraw(c);
				}
		    }
		    catch (Exception e) {}
		    finally
		    {
		    	if(c!=null)
		    	    this._view.getHolder().unlockCanvasAndPost(c);
		    }
		    
		    sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
		    try
		    {
		    	if(sleepTime>0)
		    	{
		    		sleep(sleepTime);
		    	}
		    	else
		    	{
		    		sleep(10);
		    	}
		    }
		    catch (Exception e) {}
		}
		super.run();
	}
}
