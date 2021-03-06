package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;



public class gameSetUp implements Runnable{
	
	private Thread thread;
	private Display display;
	private String title;
	private int width,height;
	private BufferStrategy buffer;
	private Graphics g;
	//private int y;
	
	private gameManager manager;
	public gameSetUp(String title,int width,int height){
		this.title=title;
		this.height=height;
		this.width=width;
		
	}
	public void init(){
		display=new Display(title,width,height);
		manager =new gameManager();
		manager.init();
	}
	
	public synchronized void start(){
		if(thread==null){
			
		
		thread=new Thread(this);
		thread.start();
		}
	}
	public synchronized void stop(){
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void tick(){
		//y +=1;
		manager.tick();
	}
	public void render(){
		buffer =display.canvas.getBufferStrategy();
		if(buffer==null){
			display.canvas.createBufferStrategy(3);
			return;
		}
		g=buffer.getDrawGraphics();
		g.clearRect(0,0,width,height);
		
		//draw
		//g.setColor(Color.RED);
		//g.fillRect(12,y,40,40);
		
		manager.render(g);
		
		//draw end
		buffer.show();
		g.dispose();
	}
	public void run(){
		init();
		
		int fps =50;
		double timePerTick=1000000000/fps;
		double delta = 0;
		long OutLoopTime=System.nanoTime();
		
		while(true){
			
			delta=delta + (System.nanoTime()-OutLoopTime)/timePerTick;
			OutLoopTime=System.nanoTime();
			if(delta>=1){
			tick();
			render();
			delta--;
			}
		}
	}

}
