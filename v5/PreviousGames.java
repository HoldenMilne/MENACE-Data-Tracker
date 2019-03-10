package v5;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.text.PlainDocument;

public class PreviousGames extends JFrame implements ActionListener, KeyListener {

	public static boolean saved = false;
	public static boolean loadedGame = false;
	public static boolean newGame = false;
	JTextArea textArea = new JTextArea();
	JButton cancel;
	JButton save;
	public static PreviousGames g = null;
	public static boolean running = true;
	
	public PreviousGames()
	{
		//TODO: Set button colors
		g = this;
		this.setUndecorated(true);
		JPanel footer = new JPanel();
		footer.setBackground(Window.scheme.c_left);
		PlainDocument doc = (PlainDocument) textArea.getDocument();
		doc.setDocumentFilter(new FileFilter());
		textArea.setToolTipText("Do not add a file type.  The file type will be added for you.");
		textArea.setPreferredSize(new Dimension(200,18));
		textArea.setBorder(BorderFactory.createLoweredBevelBorder());
		textArea.addKeyListener(this);
		
		Dimension d = new Dimension(84,36);
		save = new JButton("Save");
			save.setPreferredSize(d);
			//save.addActionListener(this);
		cancel = new JButton("Cancel");
			cancel.setPreferredSize(d);
			//cancel.addActionListener(this);
		
		footer.add(new JLabel("Note: Saved games cannot be changed after being reloaded"));
		footer.add(textArea);
		//footer.add(cancel);
		//footer.add(save);
		this.getRootPane().setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,Window.scheme.c_bar, Window.scheme.c_accent1));
		
		this.getContentPane().add(footer);
		this.getContentPane().setBackground(Window.scheme.c_left);
		pack();
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);
		//newGame = false;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		String name = arg0.getActionCommand();
		
		if(name.equals("Cancel"))
			{
				Main.window.Active(true);
				this.dispose();
			}
		else
			if(Save())
			{
				Main.window.Active(true);
				saved = true;
				//SaveClose();
				this.dispose();
			}else
			{
				textArea.setBackground(Color.red);
			}

	}

	private void SaveClose() {
		if(PreviousGames.saved && !PreviousGames.newGame)
		{
			System.exit(0);
		}else if(PreviousGames.newGame)
		{
			super.dispose();
			Main.window = new Window("Menace - By Holden Milne","Default");
			PreviousGames.newGame = false;
			PreviousGames.loadedGame = false;
			Main.clearCaches();
			Main.DisplayBoard();
			//PreviousGames.ResetThread();
		}
		
	}

	static void Load() 
	{	
		if(Main.points.size>0)
			{
				int JO = JOptionPane.showInternalConfirmDialog(Main.window.getContentPane(), "This game data has not yet been saved.  Would you like to?");
				if(JO == JOptionPane.CANCEL_OPTION)
					return;
				else if(JO == JOptionPane.YES_OPTION )
				{
					if(getSaveResult()==JOptionPane.CANCEL_OPTION) return;
				}
			}
		
		JFileChooser jfc = new JFileChooser(Main.previousRunsDirectory.toString());
		jfc.showOpenDialog(Main.window);
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		File f = jfc.getSelectedFile();
		System.out.println(f.getPath());
		if(f.getPath().endsWith(".mgd"))
		{
			System.out.println("dundon");
			Main.points = new LinkedList();
			Main.previousBoards.clear();
			Main.previousMoves.clear();
			Main.mWins=0;Main.mLosses=0;Main.mTies=0;
			
			boolean Mode3;
			
			int x;
			int y;
			boolean menace;
			
			ArrayList<Integer> movesPos = new ArrayList<Integer>();
			ArrayList<Boolean> movesMenace = new ArrayList<Boolean>();
			
			BufferedReader br = null;
			
			try {
				br = new BufferedReader(new FileReader(f));
				Scanner sc = null;
				
				while(br.ready())
				{
					String line = br.readLine();
					sc = new Scanner(line);
					
					while(sc.hasNext())
					{
						if(line.contains("Mode3"))
							{
								sc.useDelimiter(":");
								sc.next();
								String s = sc.next();
								if(s.toLowerCase().charAt(0) == 't')
									MyCanvas.mode3 = true;
								else MyCanvas.mode3 = false;
							}
						else
							{
								System.out.println(line);
								sc.useDelimiter(",");
								x = sc.nextInt();
								System.out.println(x);
								y = sc.nextInt();
								String win = sc.next();
								if(win.toLowerCase().charAt(0)=='m')
									{
									Main.winner = 1;
									Main.mWins+=1;
									}
								else if(win.toLowerCase().charAt(0)=='p')
									{
									Main.winner = -1;
									Main.mLosses+=1;
									}
								else
									{
									Main.winner = 0;
									Main.mTies+=1;
									}
								
								for(int i = 0; i < 9; i++) {
								String pos = sc.next();
								String move = sc.next();
								if(pos.toLowerCase().charAt(0)=='n')
									{
										movesPos.add(i,-1);
										movesMenace.add(i,false);
									}
								else
								{
									movesPos.add(i,Integer.valueOf(pos));
									System.out.println(pos+"lll");
									if(move.toLowerCase().charAt(0)=='t')
										movesMenace.add(i,true);
									else
										movesMenace.add(i,false);
								}
								}
								Move[] moves = new Move[9];
								for(int i =0; i < 9; i++)
								{
									// error
									System.out.println(movesMenace.get(i)+" " +movesPos.get(i)+":x");
									if(movesPos.get(i)==null)
										continue;
									moves[i] = new Move(movesMenace.get(i), movesPos.get(i));
								}
								for(Move i : moves)
									;//System.out.println(i.pos+"X");
								Main.points.Add(new Node(new Point(x, y, moves)));
								//movesPos.clear();
								//movesMenace.clear();
								break;
							}
					
					}
				}	
					// Print the values
					for(Integer i : movesPos)
					{
						System.out.println(i);
					}
					for(Boolean i : movesMenace)
					{
						System.out.println(i);
					}
					Node temp = Main.points.head;
					while(temp!=null)
					{
						System.out.println(temp.getData().x+" " +temp.getData().y+" " +temp.getData().winner);
						temp = temp.getNext();
					}
					
					String[] arr = new String[9];
					for(int i =0; i < 9; i++)
						arr[i] = "-1";
						
					for(int i =0; i < 9; i++)
						if(movesPos.get(i)==-1)
							{
								System.out.println(movesPos.get(i));
								continue;
							}
						else
							if(movesMenace.get(i))
								arr[movesPos.get(i)] = "x";
							else 
								arr[movesPos.get(i)] = "o";
					
					Main.lastBoard = arr;
					
					for(int i = 0 ; i < 9; i++)
						Main.moves[i] = new Move(movesMenace.get(i), movesPos.get(i));
					Main.board = Main.lastBoard;
					Main.rTestWin();
					Main.DetermineShape();
					Main.window.Update();
					//Load into points, also reminder to make sure the previous games has been saved if there are points that exist
					
					movesPos.clear();
					movesMenace.clear();
					
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				if(br!=null)
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
		
		
		loadedGame = !loadedGame;
		
		ResetThread();
	}
	
	static void New()
	{
		newGame = true;
		Main.window.dispose();
		loadedGame = false;
	}
	public static void ResetThread() {
		if(!loadedGame)	
		{
			Main.t = new Main.MyThread();
			Main.t.start();	
			
		}
	}

	private boolean Save() {
		BufferedWriter bw = null;
		String s = textArea.getText();
		int x = s.lastIndexOf('.');
		while(x>=0)
			{
				s = s.substring(0, x);
				x = s.lastIndexOf('.');
			}
		if(s.equals(""))
			return false;
		s = Main.previousRunsDirectory + File.separator + s+".mgd";
		File f = new File(s);
	
		if(f.exists())
		{
			int l = JOptionPane.showInternalConfirmDialog(Main.window.getContentPane(),"File already exists!  Would you like to overwrite?","File Exists", JOptionPane.YES_NO_OPTION);
			if(l == JOptionPane.NO_OPTION)
			{
				return false;
			}
		}else
		{
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			bw = new BufferedWriter(new FileWriter(f));
			StringBuilder sb = new StringBuilder();
			sb.append("Mode3:"+MyCanvas.mode3+"\n");
			Node p = Main.points.head;
			while(p!=null)
			{
				// Stores by p.x,p.y,p.winner,move0.pos,isMennace,move1.pos,isMennace... newline repeat
				sb.append(p.getData().x+","+p.getData().y+","+p.getData().winner+",");
				for(int i = 0; i < p.getData().moves.length;i++)
					if(p.getData().moves[i]==null)
						sb.append("null,null,");
					else
						sb.append(p.getData().moves[i].pos+"," +p.getData().moves[i].isMenace+",");
				sb.append("\n");
				p=p.getNext();
			}
			System.out.println(sb.toString());
			saved=true;
			bw.write(sb.toString());
			return true;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return false;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(textArea.getBackground().equals(Color.red))
			textArea.setBackground(Color.white);
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public static int getSaveResult() {

		//int jo = JOptionPane.showInternalConfirmDialog(Main.window, "Saved games cannot be modified after closing.  Would you still like to save?");
		//if(jo == JOptionPane.NO_OPTION)
			//return -1;
		
		g = new PreviousGames();
		
		String[] jba = new String[] {"Save","Cancel"};

		int i = JOptionPane.showInternalOptionDialog(Main.window.getContentPane(), g.textArea, "Save", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,null,jba,null);
		//JOptionPane.show
		if(i == JOptionPane.OK_OPTION)
		{
			g.Save();
		}
		
		System.out.println("..."+i);
		return i;
		
		}

	
}
