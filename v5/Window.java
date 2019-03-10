package v5;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

public class Window extends JFrame implements ActionListener
{
	
	private static final long serialVersionUID = 1L;
	MyCanvas canvas;
	MyCanvas boardCanvas;
	JPanel center;
	
	JLabel lastMoveSetM = new JLabel();
	JLabel lastMoveSetP = new JLabel();
	
	JLabel rbw;
	JLabel rbt;
	JLabel rbl;
	
	static ColorScheme scheme;
	
	public Window(String s, String schemeName)
	{
		super(s);
		
		//this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		try
			{
				BufferedImage icon = ImageIO.read(new File("res"+File.separator+"icon.jpeg"));
				this.setIconImage(icon);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		
		ColorScheme.LoadDefaultSchemes();
		ColorScheme.LoadSchemes();
		if(scheme == null)
			scheme = ColorScheme.SchemeFactory("Default");
		else
			scheme = ColorScheme.SchemeFactory(schemeName);
		Container contentPane = this.getContentPane();
		
		center = new JPanel();
		JPanel centerLeft = new JPanel(); // JPanel for Canvas and x Grid
		JPanel centerLeftT = new JPanel(); //Panel for Canvas and YGrid
		
		canvas = new MyCanvas("");
		
		MyCanvas X_GRID = new MyCanvas("X_GRID");
		centerLeft.setBackground(scheme.c_left);
		
		MyCanvas Y_GRID = new MyCanvas("Y_GRID");
	
		/////
		centerLeftT.setBackground(scheme.c_left);
		
		JPanel xgrid = new JPanel();
		
		MyCanvas nullC = new MyCanvas("null");

		nullC.setBorder(BorderFactory.createLineBorder(new Color(0f,0f,0f,0f)));
		nullC.setBackground(new Color(0f,0f,0f,0f));
		
		JPanel rBottom = new JPanel();
			rBottom.setLayout(new GridLayout(1,3));
			rbw = new JLabel();
			rbt = new JLabel();
			rbl = new JLabel();

			this.setRBTexts();
			
			rBottom.add(rbw);
			rBottom.add(rbt);
			rBottom.add(rbl);
			rBottom.setBackground(scheme.c_accent2);
			rBottom.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.DARK_GRAY),BorderFactory.createEmptyBorder(4, 4, 4, 4)));
		
		JPanel rTop = new JPanel();
			lastMoveSetM = new JLabel("Menace's Last Move Set [ x, x, x, x, x ] ");
			lastMoveSetP = new JLabel("Player's Last Move Set [ x, x, x, x, x ] ");
			rTop.setLayout(new GridLayout(2,1));
			rTop.add(lastMoveSetM);
			rTop.add(lastMoveSetP);
			rTop.setBackground(scheme.c_accent2);
			rTop.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.DARK_GRAY),BorderFactory.createEmptyBorder(4, 4, 4, 4)));
		
		boardCanvas = new MyCanvas("BOARD");	
			boardCanvas.addMouseListener(boardCanvas);
			boardCanvas.setToolTipText("Click to lock/unlock the win line");
		JPanel right = new JPanel();	
			//right.setBackground(scheme.Color.white);
		GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = g.getScreenDevices();
		
		if(g.getDefaultScreenDevice().equals(devices[0]) && devices.length > 1)
		{
			canvas.setPreferredSize(new Dimension((int)(devices[1].getDefaultConfiguration().getBounds().getWidth()*0.75),(int) (devices[1].getDefaultConfiguration().getBounds().getHeight()-devices[1].getDefaultConfiguration().getBounds().getHeight()*.15)));
			X_GRID.setPreferredSize(new Dimension((int)(devices[1].getDefaultConfiguration().getBounds().getWidth()*0.75),(int) (devices[1].getDefaultConfiguration().getBounds().getHeight()-devices[1].getDefaultConfiguration().getBounds().getHeight()*.95)));
			Y_GRID.setPreferredSize(new Dimension((int) (devices[1].getDefaultConfiguration().getBounds().getWidth()*0.035),(int)(canvas.getPreferredSize().getHeight())));
			
			this.setLocation(devices[1].getDefaultConfiguration().getBounds().x,devices[1].getDefaultConfiguration().getBounds().y);
			right.setPreferredSize(new Dimension((int) (devices[1].getDefaultConfiguration().getBounds().width*0.19),devices[1].getDefaultConfiguration().getBounds().height));
			centerLeft.setPreferredSize(new Dimension((int)(devices[1].getDefaultConfiguration().getBounds().getWidth()*.8),(int) devices[1].getDefaultConfiguration().getBounds().getHeight()));
			centerLeftT.setPreferredSize(new Dimension(centerLeft.getPreferredSize().width,(int) canvas.getPreferredSize().getHeight()));
			
			
			int bCSize = (int) (right.getPreferredSize().getWidth()-devices[1].getDefaultConfiguration().getBounds().width*0.05);	
			boardCanvas.setPreferredSize(new Dimension(bCSize,bCSize));

			//make center left not be too big in height
			//centerLeft.setPreferredSize(new Dimension((int)(devices[1].getDefaultConfiguration().getBounds().getHeight()*0.2),(int) (devices[1].getDefaultConfiguration().getBounds().getHeight()*0.4)));
			/*
		 	this.setLocation(devices[1].getDefaultConfiguration().getBounds().x,devices[1].getDefaultConfiguration().getBounds().y);
			right.setPreferredSize(new Dimension((int) (devices[1].getDefaultConfiguration().getBounds().width*0.19),devices[1].getDefaultConfiguration().getBounds().height));
			centerLeftT.setPreferredSize(new Dimension(centerLeft.getPreferredSize().width,(int) canvas.getPreferredSize().getHeight()));
			nullC.setPreferredSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.035),(int)(X_GRID.getPreferredSize().getHeight())));
			int bCSize = (int) (right.getPreferredSize().getWidth()-Toolkit.getDefaultToolkit().getScreenSize().width*0.05);	
			boardCanvas.setPreferredSize(new Dimension(bCSize,bCSize));
			 */
		}else
		{
			canvas.setPreferredSize(new Dimension((int)(devices[0].getDefaultConfiguration().getBounds().getWidth()*0.75),(int) (devices[0].getDefaultConfiguration().getBounds().getHeight()-devices[0].getDefaultConfiguration().getBounds().getHeight()*.15)));
			X_GRID.setPreferredSize(new Dimension((int)(devices[0].getDefaultConfiguration().getBounds().getWidth()*0.75),(int) (devices[0].getDefaultConfiguration().getBounds().getHeight()-devices[0].getDefaultConfiguration().getBounds().getHeight()*.95)));
			Y_GRID.setPreferredSize(new Dimension((int) (devices[0].getDefaultConfiguration().getBounds().getWidth()*0.035),(int)(canvas.getPreferredSize().getHeight())));
			
			this.setLocation(devices[0].getDefaultConfiguration().getBounds().x,devices[0].getDefaultConfiguration().getBounds().y);
			right.setPreferredSize(new Dimension((int) (devices[0].getDefaultConfiguration().getBounds().width*0.19),devices[0].getDefaultConfiguration().getBounds().height));
			centerLeft.setPreferredSize(new Dimension((int)(devices[0].getDefaultConfiguration().getBounds().getWidth()*.8),(int) devices[0].getDefaultConfiguration().getBounds().getHeight()));
			centerLeftT.setPreferredSize(new Dimension(centerLeft.getPreferredSize().width,(int) canvas.getPreferredSize().getHeight()));
			
			
			int bCSize = (int) (right.getPreferredSize().getWidth()-devices[0].getDefaultConfiguration().getBounds().width*0.05);	
			boardCanvas.setPreferredSize(new Dimension(bCSize,bCSize));
			
		}
			nullC.setPreferredSize(new Dimension((int) (Y_GRID.getPreferredSize().getWidth()),(int)(X_GRID.getPreferredSize().getHeight())));
		
			centerLeftT.add(canvas,BorderLayout.CENTER);
			centerLeftT.add(Y_GRID,BorderLayout.EAST);
		
			centerLeft.add(centerLeftT,BorderLayout.NORTH);
			xgrid.add(X_GRID,BorderLayout.WEST);
			xgrid.add(nullC,BorderLayout.EAST);
			centerLeft.add(xgrid,BorderLayout.SOUTH);
			center.add(centerLeft);
			right.add(rTop,BorderLayout.NORTH);
			right.add(boardCanvas,BorderLayout.CENTER);
			right.add(rBottom,BorderLayout.SOUTH);

		boardCanvas.setBackground(Color.white);
		JMenuBar bar = new JMenuBar();
		//bar.setBackground(scheme.);
		JMenuItem themes = new JMenu("Themes");
			themes.setBackground(scheme.c_bar);
			themes.setForeground(scheme.c_font);
		
		for(ColorScheme c : ColorScheme.builtInSchemes)
		{
			JMenuItem temp = new JMenuItem(c.name);
			temp.setBackground(scheme.c_bar);
			temp.setForeground(scheme.c_font);
			temp.addActionListener(this);
			themes.add(temp);
		}
		
		themes.add(new JSeparator(JSeparator.HORIZONTAL));
		
		for(ColorScheme c : ColorScheme.schemes)
		{
			JMenuItem temp = new JMenuItem(c.name);
			temp.setBackground(scheme.c_bar);
			temp.setForeground(scheme.c_font);
			temp.addActionListener(this);
			themes.add(temp);
		}

		themes.add(new JSeparator(JSeparator.HORIZONTAL));
		
		JMenuItem tb = new JMenuItem("Theme Builder");
			tb.setBackground(scheme.c_bar);
			tb.setForeground(scheme.c_font);
			tb.addActionListener(this);
			themes.add(tb);
			
		JMenu file = new JMenu("File");
			file.setBackground(scheme.c_bar);
			file.setForeground(scheme.c_font);//colorAccent4);
			file.getPopupMenu().setBackground(scheme.c_bar);
			file.getPopupMenu().setForeground(scheme.c_font);
			

		JMenuItem newGame = new JMenuItem("New");	
			newGame.setBackground(scheme.c_bar);
			newGame.setForeground(scheme.c_font);//colorAccent4);
			newGame.addActionListener(this);
		
		JMenuItem save = new JMenuItem("Save");
			save.setBackground(scheme.c_bar);
			save.setForeground(scheme.c_font);//colorAccent4);
			save.addActionListener(this);
		
		JMenuItem load = new JMenuItem("Load");
			load.setBackground(scheme.c_bar);
			load.setForeground(scheme.c_font);//colorAccent4);
			load.addActionListener(this);
			
		JMenuItem exit = new JMenuItem("Exit");
			
			//bar.setBorder(BorderFactory.createEtchedBorder(colorAccent3, colorAccent4));
			exit.addActionListener(this);
			exit.setBackground(scheme.c_bar);
			exit.setForeground(scheme.c_font);//colorAccent4);
			
			JSeparator js = new JSeparator(JSeparator.VERTICAL);
			js.setForeground(scheme.c_font);
			js.setBackground(scheme.c_bar);
			
			file.add(newGame);
			file.add(save);
			file.add(load);
			file.add(themes);
			file.addSeparator();
			file.add(exit);
			
			bar.setBackground(scheme.c_bar);
			bar.add(file);
			bar.add(js);
			
		this.setJMenuBar(bar);
		
		contentPane.add(center,BorderLayout.CENTER);
		contentPane.add(right,BorderLayout.EAST);

		xgrid.setBackground(scheme.c_left);
		
		right.setBorder(BorderFactory.createLineBorder(scheme.c_accent1, 2));
		center.setBorder(BorderFactory.createLineBorder(scheme.c_accent1, 2));
		center.setBackground(scheme.c_left);
		right.setBackground(scheme.c_right);
		
		
		//this.set
		//this.setLocation(devices[1].getFullScreenWindow()., y);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setUndecorated(true);

		this.pack();
		
		canvas.setDeltas();
		//X_GRID.setLocation(canvas.getX()-40, X_GRID.getY());
		
		//this.setResizable(false);
		this.setVisible(true);
	}

	private void setRBTexts() {
		rbw.setText("Wins: " + Main.mWins);
		rbt.setText("Ties: " + Main.mTies);
		rbl.setText("Losses: " + Main.mLosses);
		
		String movesM = "";
		String movesP = "";
		if(Main.moves != null)
		{
			for(int i = 1; i<Main.moves.length; i+=2)
			{
			if(Main.moves[i-1] == null)
				{
					movesM += -1;
				}
			else
				{
					movesM += Main.moves[i-1].pos+"";
				}
			
			if(i!=7)
			{
				movesM += ", ";
			}
			
			if(Main.moves[i] == null)
			{
				movesP += -1;
			}
			else
			{
				movesP += Main.moves[i].pos+"";
			}
			
			if(i!=7)
				movesP += ", ";
			}
		
			lastMoveSetM.setText("Menace's Last Move Set [ " + movesM + " ]");
			lastMoveSetP.setText("Player's Last Move Set [ " + movesP + " ]");
		}
	}

	public void Update()
	{
		canvas.init = true;
		canvas.paintComponent(canvas.getGraphics());
		canvas.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		//canvas.init = false;
		boardCanvas.init = true;
		boardCanvas.paintComponent(boardCanvas.getGraphics());
		boardCanvas.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		//boardCanvas.init = false;
		setRBTexts();
		PreviousGames.saved=false;
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String name = arg0.getActionCommand().toLowerCase();
		
		if(true) {
			switch(name)
			{
				case "exit":
					//this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
					//System.exit(0);
					dispose();
					break;
				case "theme builder":
					new ThemeBuilder();
					break;
				case "load":
					PreviousGames.Load();
					System.out.println(PreviousGames.loadedGame);
					break;
				case "save":
					//int JO = JOptionPane.showInternalConfirmDialog(Main.window, "Saved games cannot be modified after closing");
					int i = PreviousGames.getSaveResult();
					break;
					
				case "new":
					PreviousGames.New();
					break;
				default:
					ColorScheme newScheme = ColorScheme.SchemeFactory(name);
					UpdateColors(this,newScheme);
					scheme = newScheme;
					break;
			
					
			}
		}
	}

	private void UpdateColors(Component component, ColorScheme newScheme) {
		SetColor(component,newScheme);
		if(component instanceof Container)
		{
			for(Component c : ((Container) component).getComponents())
			{
				UpdateColors(c,newScheme);
			}
		}
		
		
	}

	private void SetColor(Component component, ColorScheme newScheme) {
		
		if(component instanceof MyCanvas || component instanceof JLabel)
			return;
		
		if(component.isBackgroundSet() && component.getBackground() instanceof MyColor)
		{
			if(((MyColor)(component.getBackground())).name.equals(ColorScheme.ACCENT1))
				component.setBackground(newScheme.c_accent1);
			else if(((MyColor)(component.getBackground())).name.equals(ColorScheme.ACCENT2))
				component.setBackground(newScheme.c_accent2);
			else if(((MyColor)(component.getBackground())).name.equals(ColorScheme.BAR))
				component.setBackground(newScheme.c_bar);
			else if(((MyColor)(component.getBackground())).name.equals(ColorScheme.RIGHT))
				component.setBackground(newScheme.c_right);
			else if(((MyColor)(component.getBackground())).name.equals(ColorScheme.LEFT))
				component.setBackground(newScheme.c_left);
			else if(((MyColor)(component.getBackground())).name.equals(ColorScheme.FONT))
				component.setBackground(newScheme.c_font);
		}
		
		if(component.isForegroundSet()&& component.getForeground() instanceof MyColor)
		{	
			if(((MyColor)(component.getForeground())).name.equals(ColorScheme.ACCENT1))
				component.setForeground(newScheme.c_accent1);
			else if(((MyColor)(component.getForeground())).name.equals(ColorScheme.ACCENT2))
				component.setForeground(newScheme.c_accent2);
			else if(((MyColor)(component.getForeground())).name.equals(ColorScheme.BAR))
				component.setForeground(newScheme.c_bar);
			else if(((MyColor)(component.getForeground())).name.equals(ColorScheme.RIGHT))
				component.setForeground(newScheme.c_right);
			else if(((MyColor)(component.getForeground())).name.equals(ColorScheme.LEFT))
				component.setForeground(newScheme.c_left);
			else if(((MyColor)(component.getForeground())).name.equals(ColorScheme.FONT))
				component.setForeground(newScheme.c_font);
			
		}
		
		if(component instanceof JComponent && ((JComponent) component).getBorder()!=null)
		{
			Border b = ((JComponent)component).getBorder();
			if(b instanceof LineBorder && component.getBackground() instanceof MyColor)
			{
				// Get the color of the line border
				 if(((MyColor)((LineBorder) b).getLineColor()).name.equals(ColorScheme.ACCENT1))
					 ((JComponent)component).setBorder(BorderFactory.createLineBorder(newScheme.c_accent1,((LineBorder) b).getThickness()));
				 else if(((MyColor)((LineBorder) b).getLineColor()).name.equals(ColorScheme.ACCENT2))
					 ((JComponent)component).setBorder(BorderFactory.createLineBorder(newScheme.c_accent2,((LineBorder) b).getThickness()));
				 else if(((MyColor)((LineBorder) b).getLineColor()).name.equals(ColorScheme.BAR))
					 ((JComponent)component).setBorder(BorderFactory.createLineBorder(newScheme.c_bar,((LineBorder) b).getThickness()));
				 else if(((MyColor)((LineBorder) b).getLineColor()).name.equals(ColorScheme.LEFT))
					 ((JComponent)component).setBorder(BorderFactory.createLineBorder(newScheme.c_left,((LineBorder) b).getThickness()));
				 else if(((MyColor)((LineBorder) b).getLineColor()).name.equals(ColorScheme.RIGHT))
					 ((JComponent)component).setBorder(BorderFactory.createLineBorder(newScheme.c_right,((LineBorder) b).getThickness()));
				 
			} else if(b instanceof BevelBorder && component.getBackground() instanceof MyColor)
			{
				 if(((MyColor)((BevelBorder) b).getHighlightInnerColor()).name.equals(ColorScheme.ACCENT1))
					 ((JComponent)component).setBorder(BorderFactory.createBevelBorder(((BevelBorder) b).getBevelType(),newScheme.c_accent1,((BevelBorder) b).getShadowInnerColor()));
				 else if(((MyColor)((BevelBorder) b).getHighlightInnerColor()).name.equals(ColorScheme.ACCENT2))
					 ((JComponent)component).setBorder(BorderFactory.createBevelBorder(((BevelBorder) b).getBevelType(),newScheme.c_accent2,((BevelBorder) b).getShadowInnerColor()));
				 else if(((MyColor)((BevelBorder) b).getHighlightInnerColor()).name.equals(ColorScheme.BAR))
					 ((JComponent)component).setBorder(BorderFactory.createBevelBorder(((BevelBorder) b).getBevelType(),newScheme.c_bar,((BevelBorder) b).getShadowInnerColor()));
				 else if(((MyColor)((BevelBorder) b).getHighlightInnerColor()).name.equals(ColorScheme.LEFT))
					 ((JComponent)component).setBorder(BorderFactory.createBevelBorder(((BevelBorder) b).getBevelType(),newScheme.c_left,((BevelBorder) b).getShadowInnerColor()));
				 else if(((MyColor)((BevelBorder) b).getHighlightInnerColor()).name.equals(ColorScheme.RIGHT))
					 ((JComponent)component).setBorder(BorderFactory.createBevelBorder(((BevelBorder) b).getBevelType(),newScheme.c_right,((BevelBorder) b).getShadowInnerColor()));
				 else if(((MyColor)((BevelBorder) b).getHighlightInnerColor()).name.equals(ColorScheme.FONT))
					 ((JComponent)component).setBorder(BorderFactory.createBevelBorder(((BevelBorder) b).getBevelType(),newScheme.c_font,((BevelBorder) b).getShadowInnerColor()));
			
				 if(((MyColor)((BevelBorder) b).getShadowInnerColor()).name.equals(ColorScheme.ACCENT1))
					 ((JComponent)component).setBorder(BorderFactory.createBevelBorder(((BevelBorder) b).getBevelType(),((BevelBorder) b).getHighlightInnerColor(),newScheme.c_accent1));
				 else if(((MyColor)((BevelBorder) b).getShadowInnerColor()).name.equals(ColorScheme.ACCENT2))
					 ((JComponent)component).setBorder(BorderFactory.createBevelBorder(((BevelBorder) b).getBevelType(),((BevelBorder) b).getHighlightInnerColor(),newScheme.c_accent2));
				 else if(((MyColor)((BevelBorder) b).getShadowInnerColor()).name.equals(ColorScheme.BAR))
					 ((JComponent)component).setBorder(BorderFactory.createBevelBorder(((BevelBorder) b).getBevelType(),((BevelBorder) b).getHighlightInnerColor(),newScheme.c_bar));
				 else if(((MyColor)((BevelBorder) b).getShadowInnerColor()).name.equals(ColorScheme.LEFT))
					 ((JComponent)component).setBorder(BorderFactory.createBevelBorder(((BevelBorder) b).getBevelType(),((BevelBorder) b).getHighlightInnerColor(),newScheme.c_left));
				 else if(((MyColor)((BevelBorder) b).getShadowInnerColor()).name.equals(ColorScheme.RIGHT))
					 ((JComponent)component).setBorder(BorderFactory.createBevelBorder(((BevelBorder) b).getBevelType(),((BevelBorder) b).getHighlightInnerColor(),newScheme.c_right));
				 else if(((MyColor)((BevelBorder) b).getShadowInnerColor()).name.equals(ColorScheme.FONT))
					 ((JComponent)component).setBorder(BorderFactory.createBevelBorder(((BevelBorder) b).getBevelType(),((BevelBorder) b).getHighlightInnerColor(),newScheme.c_font));
			
			
			} else if(b instanceof CompoundBorder && component.getBackground() instanceof MyColor)
			{
				Border bI = ((CompoundBorder) b).getInsideBorder();
				Border bO = ((CompoundBorder) b).getOutsideBorder();
				//SetColor((Component) bI, newScheme);
				//SetColor((Component) bO, newScheme);
			}
		}
		
	}
	
	@Override
	public void dispose()
	{
		int JO;
		if(!PreviousGames.saved && !PreviousGames.loadedGame)
			JO = JOptionPane.showInternalConfirmDialog(this.getContentPane(), "This game data has not yet been saved.  Would you like to?");
		else
			JO = JOptionPane.NO_OPTION;
		int i = JOptionPane.CANCEL_OPTION;
		
		if(JO == JOptionPane.CANCEL_OPTION)
		{
			return;
		}else if(JO == JOptionPane.YES_OPTION){
			
			if(Main.points.size ==0)
			{
				JOptionPane.showInternalMessageDialog(this.getContentPane(), "There are no points to save!");
			}else
				{
				System.out.println("SAVEDIAG");
				i = PreviousGames.getSaveResult();
				}
			
			Main.window.Active(true);
		}else
			PreviousGames.saved = true;
		
		if(PreviousGames.saved && !PreviousGames.newGame)
		{
			System.exit(0);
		}else if(PreviousGames.newGame)
		{
			String sc = scheme.name;
			super.dispose();
			Main.window = new Window("Menace - By Holden Milne",sc);
			Main.previousBoards.clear();
			PreviousGames.newGame = false;
			PreviousGames.loadedGame = false;
			Main.clearCaches();
			Main.DisplayBoard();
			//PreviousGames.ResetThread();
		}
		
	}

	protected void Active(boolean b) {
		this.setEnabled(b);
		
	}
	
	public class MyThread extends Thread
	{
		@Override
		public void run()
		{
				new PreviousGames();
				Main.window.Active(false);
				//while(g != null)
					//{
					//}

				
				//this.interrupt();
				//return;
		}
		
	}

}


