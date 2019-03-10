package v5;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class MyCanvas extends JPanel implements MouseListener
{

	static Color c1 = new Color(0.25f,0.25f,0.5f); //blue
	static Color c2 = new Color(0.7f,0.25f,0.25f); //red
	static int MaxGames = 100;
	static int MaxPlusMin = 45;
	static int dx; //deltax
	static int dy = 40;
	double r;
	boolean init = false;
	private static final long serialVersionUID = 1L;
	static boolean mode3 = false;
	boolean showWinLine = false;
	String name;
	
	Color[] moveColors = new Color[] {
			new Color(0x3F47CC),
			new Color(0xEC1B23),
			new Color(0xFF7F26),
			new Color(0xFEAEC9),
			new Color(0x79e620),
			new Color(0xA349A3),
			new Color(0xFEF200),
			new Color(0xC3C3C3),
			new Color(0x000000),
	};
	private boolean forceWinLine = false;
	
	public MyCanvas() {
        this("");
	}

    public MyCanvas(String string) {
		 name = string;
		 setBorder(BorderFactory.createLineBorder(Color.black));
	     dx = 0;
	     r = 0;
	     dy = 0;
	     this.setBackground(Color.white);
	}

	public Dimension getPreferredSize(Window w) {
        return new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2),(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2));
    }

    public void paintComponent(Graphics g) 
    {

    	super.paintComponent(g);

    	Graphics2D g2 = (Graphics2D)g;
    	
    	if(this.getName().equals("X_GRID"))
    	{
    		g2.setColor(Color.DARK_GRAY);
    		for(int i = 1; i <= MaxGames; i++ )
    		{
    			int x = (i+1)*dx;
    			//g2.fillRect(x, 8, x, 16);
    			
    			if(i%10 == 0)
    			{
    				g2.drawString(i+"", x-dx/2, this.getHeight()/2 + 16);
    				g2.drawLine(x, 4, x, this.getHeight()/2);
    			}
    			else
    			{
    				g2.drawLine(x, 4, x, this.getHeight()/4);
    			}
    		}
    	}else if(getName().equals("Y_GRID"))
    	{
    		int y = 0;
    		int i = 1;
    		while(y<=this.getPreferredSize().getHeight())
    		{
    			y = this.getHeight()/2+(i++)*dy;
    			//g2.fillRect(x, 8, x, 16);
    			
    			if(i%10 == 0)
    			{
    				g2.drawString("-"+i+"", this.getWidth()/2 +2, y+dy/2 );
    				g2.drawLine(4, y, this.getWidth()/2, y);
    			}
    			else
    			{
    				g2.drawLine(4, y, this.getWidth()/4,y);
    			}
    		}
    		
    		i = 0;
    		while(y>= -1*this.getPreferredSize().getHeight())
    		{
    			y = this.getHeight()/2-(i)*dy;
    			//g2.fillRect(x, 8, x, 16);
    			
    			if(i%10 == 0 || i == 0)
    			{
    				g2.drawString(i+"", this.getWidth()/2 +2, y+dy/2 );
    				g2.drawLine(4, y, this.getWidth()/2, y);
    			}
    			else
    			{
    				g2.drawLine(4, y, this.getWidth()/4,y);
    			}
    			i++;
    		}
    	}
    	else if(getName().equals("BOARD"))
    	{
    		g2.fillRect(this.getWidth()/3,(int)(this.getHeight()*0.05),3,(int)(this.getHeight()*0.9)); //left V line
    		g2.fillRect(2*this.getWidth()/3,(int)(this.getHeight()*0.05),3,(int)(this.getHeight()*0.9)); //right V line
    		g2.fillRect((int)(this.getWidth()*0.05),this.getHeight()/3,(int)(this.getHeight()*0.9),3); //top H line
    		g2.fillRect((int)(this.getWidth()*0.05),2*this.getHeight()/3,(int)(this.getHeight()*0.9),3); //Bottom H line
    		
    		int grid_width = (int)(this.getHeight()*0.9)-(int)(this.getWidth()*0.05);
    		
    		if(init)
    		{
    			int radius = ((int)(this.getWidth()*0.9-(int)(this.getWidth()*0.05))/5);
    			int length = ((int)(this.getWidth()*0.9-(int)(this.getWidth()*0.05))/5);
    			
    			int pos1 = grid_width/8;
				int pos2 = grid_width/2;
				int pos3 = grid_width*7/8;
				System.out.println("Main " + Main.lastBoard);
    			for(int i = 0; i < Main.lastBoard.length; i++)
    			{
					if(Main.lastBoard[i].equals("o"))
						g2.setStroke(new BasicStroke(4));
					else
						g2.setStroke(new BasicStroke(3));
	    			
    				switch(i)
    				{
    					case 0:
    						if(Main.lastBoard[i].equals("o"))
    						{
    							Ellipse2D outer = new Ellipse2D.Double(pos1,pos1,radius,radius);
    							
    							g2.setColor(c2);
    							g2.draw(outer);
    							
    						}else if(Main.lastBoard[i].equals("x"))
    						{
    							g2.setColor(c1);
    							g2.drawLine(pos1,pos1, pos1+length,pos1+length);
    							g2.drawLine(pos1,pos1+length,pos1+length,pos1);
        						
    						}
    						break;
    					case 1:
    						if(Main.lastBoard[i].equals("o"))
    						{
    							Ellipse2D outer = new Ellipse2D.Double(pos2,pos1,radius,radius);
    							
    							g2.setColor(c2);
    							g2.draw(outer);
    							
    						}else if(Main.lastBoard[i].equals("x"))
    						{
    							g2.setColor(c1);
    							g2.drawLine(pos2,pos1, pos2+length,pos1+length);
    							g2.drawLine(pos2,pos1+length, pos2+length,pos1);
        						
    						}
    						break;
    					case 2:
    						if(Main.lastBoard[i].equals("o"))
    						{
    							Ellipse2D outer = new Ellipse2D.Double(pos3,pos1,radius,radius);
    						
    							g2.setColor(c2);
    							g2.draw(outer);
    							
    						}else if(Main.lastBoard[i].equals("x"))
    						{
    							g2.setColor(c1);
    							g2.drawLine(pos3,pos1, pos3+length,pos1+length);
    							g2.drawLine(pos3,pos1+length, pos3+length,pos1);
        						
    						}
    						break;
    					case 3:
    						if(Main.lastBoard[i].equals("o"))
    						{
    							Ellipse2D outer = new Ellipse2D.Double(pos1,pos2,radius,radius);

    							g2.setColor(c2);
    							g2.draw(outer);
    							
    						}else if(Main.lastBoard[i].equals("x"))
    						{
    							g2.setColor(c1);
    							g2.drawLine(pos1,pos2, pos1+length,pos2+length);
    							g2.drawLine(pos1,pos2+length, pos1+length,pos2);
        						
    						}
    						break;
    					case 4:
    						if(Main.lastBoard[i].equals("o"))
    						{
    							Ellipse2D outer = new Ellipse2D.Double(pos2,pos2,radius,radius);
    							
    							g2.setColor(c2);
    							g2.draw(outer);
    							
    						}else if(Main.lastBoard[i].equals("x"))
    						{
    							g2.setColor(c1);
    							g2.drawLine(pos2,pos2, pos2+length,pos2+length);
    							g2.drawLine(pos2,pos2+length, pos2+length,pos2);
        						
    						}
    						break;
    					case 5:
    						if(Main.lastBoard[i].equals("o"))
    						{
    							Ellipse2D outer = new Ellipse2D.Double(pos3,pos2,radius,radius);
    							
    							g2.setColor(c2);
    							g2.draw(outer);
    							
    						}else if(Main.lastBoard[i].equals("x"))
    						{
    							g2.setColor(c1);
    							g2.drawLine(pos3,pos2, pos3+length,pos2+length);
    							g2.drawLine(pos3,pos2+length, pos3+length,pos2);
        						
    						}
    						break;
    					case 6:
    						if(Main.lastBoard[i].equals("o"))
    						{
    							Ellipse2D outer = new Ellipse2D.Double(pos1,pos3,radius,radius);

    							g2.setColor(c2);
    							g2.draw(outer);
    							
    						}else if(Main.lastBoard[i].equals("x"))
    						{
    							g2.setColor(c1);
    							g2.drawLine(pos1,pos3, pos1+length,pos3+length);
    							g2.drawLine(pos1,pos3+length, pos1+length,pos3);
        						
    						}
    						break;
    					case 7:
    						if(Main.lastBoard[i].equals("o"))
    						{
    							Ellipse2D outer = new Ellipse2D.Double(pos2,pos3,radius,radius);
    							
    							g2.setColor(c2);
    							g2.draw(outer);
    							
    						}else if(Main.lastBoard[i].equals("x"))
    						{
    							g2.setColor(c1);
    							g2.drawLine(pos2,pos3, pos2+length,pos3+length);
    							g2.drawLine(pos2,pos3+length, pos2+length,pos3);
        						
    						}
    						break;
    					case 8:
    						if(Main.lastBoard[i].equals("o"))
    						{
    							Ellipse2D outer = new Ellipse2D.Double(pos3,pos3,radius,radius);
    							
    							g2.setColor(c2);
    							g2.draw(outer);
    							
    						}else if(Main.lastBoard[i].equals("x"))
    						{
    							g2.setColor(c1);
    							g2.drawLine(pos3,pos3, pos3+length,pos3+length);
    							g2.drawLine(pos3,pos3+length, pos3+length,pos3);
        						
    						}
    						break;
    				}
    				
    			}
				if(showWinLine) {
	    			int[] px = new int[3];
					int[] py = new int[3];
					
					for(int z =0; z<Main.lastWinSet.length-1;z++)
					{
						int l = Main.lastWinSet[z];
						int px1 = 0;
						int py1 = 0;
						
						if(Main.lastWinSet[3]==Main.VERTICAL)
						{
							if(z==0)
								py1 = 0;
							else if(z==2)
								py1+=radius;
							px1 = radius/2;
							
						}else if(Main.lastWinSet[3]==Main.HORIZONTAL)
						{
							if(z==0)
								px1 = 0;
							else if(z==2)
								px1+=radius;
							py1 = radius/2;
						}else if(Main.lastWinSet[3]==Main.NDIAG)
						{
							if(z==0)
								{
									py1 = 0;
									px1 = 0;
								}
							else if(z==2)
								{
									py1+=radius;
									px1+=radius;
								}else {
									py1 = radius/2;
									px1 = radius/2;
								}
						}else if(Main.lastWinSet[3]==Main.PDIAG)
						{
							if(z==0)
							{
								py1 = 0;
								px1 = radius;
							}
							else if(z==2)
							{
								py1+=radius;
								px1+=0;
							}else {
								py1 = radius/2;
								px1 = radius/2;
							}
						}
							
						switch(l)
						{
							case 0:
								px[z]=pos1+px1;
								py[z]=pos1+py1;
								break;
							case 1:
								px[z]=pos2+px1;
								py[z]=pos1+py1;
								break;
							case 2:
								px[z]=pos3+px1;
								py[z]=pos1+py1;
								break;
							case 3:
								px[z]=pos1+px1;
								py[z]=pos2+py1;
								break;
							case 4:
								px[z]=pos2+px1;
								py[z]=pos2+py1;
								break;
							case 5:
								px[z]=pos3+px1;
								py[z]=pos2+py1;
								break;
							case 6:
								px[z]=pos1+px1;
								py[z]=pos3+py1;
								break;
							case 7:
								px[z]=pos2+px1;
								py[z]=pos3+py1;
								break;
							case 8:
								px[z]=pos3+px1;
								py[z]=pos3+py1;
								break;
						}
						
						
					}
					
					if(Main.winner == 1)
						g2.setColor(c1);
					else if(Main.winner == -1)
						g2.setColor(c2);
					else
						return;
						
					g2.setStroke(new BasicStroke(5));
					int t = 0;
					g2.drawLine(px[0]+((int)((Math.random()-0.5)*t)), py[0]+((int)((Math.random()-0.5)*t)), px[1]+((int)((Math.random()-0.5)*t)), py[1]+((int)((Math.random()-0.5)*t)));
					g2.drawLine(px[1]+((int)((Math.random()-0.5)*t)), py[1]+((int)((Math.random()-0.5)*t)), px[2]+((int)((Math.random()-0.5)*t)), py[2]+((int)((Math.random()-0.5)*t)));
					
					
				}
    			
    		}
    	}
    	else if(this.getName().equals("") && init) 
    	{     
    		Node n = Main.points.head;//p = new Point(r+200,r,Main.moves);
    		Node temp = n;
    		
    		Point p = n.getData();
    		
    		while(n!=null)
    		{
    			Point previous = p;
    			
    			if(Main.points.size > 1 && n.getNext()!=null)
				{

					g2.setColor(Color.black);
    				
					p = n.getNext().getData();
					g2.setStroke(new BasicStroke(2));
	        		g2.drawLine((int)(previous.x+(r)), (int)(previous.y-(r/2)), (int)(p.x+(r)), (int) (p.y-(r/2)));
					g2.setStroke(new BasicStroke(1));
				}
        		
        		Color color = getColor(previous);
        		Ellipse2D ellipse = new Ellipse2D.Double((double)(previous.x-r+this.getX()), (double)(previous.y-(r)), r, r);
    			
        		g2.setColor(color);
        		g2.fill(ellipse);
        		g2.setColor(Color.BLACK);
        		g2.draw(ellipse);
        		
        		
        		n=n.getNext();
    		}
    		
    	}
    	
    }

	private Color getColor(Point p)
	{
		// TODO: NOT LOGGING MOVE POSITIONS AFTER LOAD THEREFORE ALL POSITIONS GO TO DEFAULT.
		if(!mode3)
			return moveColors[p.moves[0].pos];
		else
			switch(p.moves[0].pos)
			{
				case 0:
				case 2:
				case 6:
				case 8:
					return moveColors[0];
				case 1:
				case 3:
				case 5:
				case 7:
					return moveColors[1];
				default:
					return moveColors[4];
			}
	}

	public void setDeltas() 
	{
		dx =this.getWidth()/MaxGames + 0;//4
        r=dx/3*2;
		dy = this.getHeight()/(2*MaxPlusMin);
	}
	
	public void resize()
	{
		this.setPreferredSize(new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.75),(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()-Toolkit.getDefaultToolkit().getScreenSize().getHeight()*.1)));
		
	}
	
	public String getName()
	{
		return name;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(!forceWinLine)
		{
			forceWinLine=true;
			showWinLine = true;
		}else
		{
			forceWinLine=false;
		}
		this.paintComponent(this.getGraphics());
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

		if(!forceWinLine) {
			showWinLine = true;
			this.paintComponent(this.getGraphics());
		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(!forceWinLine) {
			showWinLine = false;
			this.paintComponent(this.getGraphics());
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
