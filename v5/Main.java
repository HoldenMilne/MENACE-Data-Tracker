package v5;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Main
{
	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;
	public static final int NDIAG = 2;
	public static final int PDIAG = 3;

	public static MyThread t;
	
	static Stack<String[]> previousBoards;
	static Stack<Move[]>  previousMoves;
	
	static Move[] moves;
	static String[] board;
	static String[] lastBoard;
	static LinkedList points;
	static int[] lastWinSet = new int[4];
	static int winner;
	
	static int moveCount; // Counts number of moves in the current game
	
	static int runs=1;

	static int mWins;
	static int mLosses;
	static int mTies;
	
	static Scanner input;
	static Scanner sc;

	static boolean menaceTurn = true;
	
	static Window window;
	
	static int won = 0;
	
	public static String OS = System.getProperty("os.name");
	public static String HOME_DIRECTORY;// = System.getProperty("user.home");
	public static String WORKING_FOLDER = "Menace";
	public static Path themesDirectory;
	public static Path previousRunsDirectory;
	
	public static void main(String[] args)
	{

		if(!RunCMD(args))
			{
				System.exit(0);
			}
		BuildWorkingFolder();
		
		points = new LinkedList();
		
		if(args.length > 0)
		{
			switch(args[0].toLowerCase()) {
				case "f":
				case "false":
				case "0":
					MyCanvas.mode3 = false;
					break;
				default:
					MyCanvas.mode3 = true;
					break;
			}
				
		}else
			{
				Scanner sc = new Scanner(System.in);
				boolean b;
				System.out.println("9 color version or 3 color version? (type 3 or 9)");
				do {
					String s = sc.nextLine();
					if(s.length()==0)
						continue;
					if(s.charAt(0) == '3')
						{
							b = true;
							break;
						}
					else if(s.charAt(0) == '9')
						{
							b=false;
							break;
						}
				
				}while(true);
				MyCanvas.mode3 = b;
			}

		window = new Window("Menace - By Holden Milne","Default");
		t = new MyThread();
		
		t.start();

	}
	
	private static boolean RunCMD(String[] s) {
		if(s.length>1&& (s[1].equals("f")||s[1].equals("false")||s[1].equals("0")))
			return true;
		if(!new File("./menace.jar").exists())
			return true;
		try {
			//Runtime.getRuntime().exec(new String[]{"cmd.exe","/c","java -jar ./Menace.jar false false"});
			ProcessBuilder builder = new ProcessBuilder(
		            "CMD", "/k", "start java -jar \"./menace.jar\" false false");
		        builder.redirectErrorStream(true);
		        Process p = builder.start();
		        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        String line;
		        while (true) {
		            line = r.readLine();
		            if (line == null) { break; }
		            System.out.println(line);
		        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private static void BuildWorkingFolder() {
		System.out.println(OS);
		
		HOME_DIRECTORY = System.getProperty("user.home")+File.separator+"Holden Milne";//System.getenv("ProgramFiles(x86)");
		System.out.println(HOME_DIRECTORY);
		WORKING_FOLDER = HOME_DIRECTORY + File.separator+"Menace";
		System.out.println(WORKING_FOLDER);
		themesDirectory = Paths.get(WORKING_FOLDER+File.separator+"Themes");
		System.out.println(themesDirectory.toString());
		previousRunsDirectory = Paths.get(WORKING_FOLDER+File.separator+"Previous Runs");
		
		try {
			
			if(!Files.isDirectory(Paths.get(WORKING_FOLDER))||!Files.isDirectory(previousRunsDirectory)||!Files.isDirectory(themesDirectory))
				System.out.println(CountFolders() + " Folder(s) Missing:\n"+ListMissingFolders()+ "\nCreating those folders\n"); 
				
			
			if(!Files.isDirectory(Paths.get(WORKING_FOLDER)))
			{
				Files.createDirectories(Paths.get(WORKING_FOLDER));
			}
			
			if(!Files.isDirectory(themesDirectory))
			{
				Files.createDirectory(themesDirectory);
			}
			
			if(!Files.isDirectory(previousRunsDirectory))
			{
				Files.createDirectory(previousRunsDirectory);
			}
			
				return;
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//System.exit(0);
		
		
		
	}

	private static String ListMissingFolders() {
		ArrayList<String> al = new ArrayList<String>();

		al.add(WORKING_FOLDER);
		al.add(themesDirectory.toString());
		al.add(previousRunsDirectory.toString());
		
		if(Files.isDirectory(Paths.get(WORKING_FOLDER)))
		{
			al.remove(WORKING_FOLDER);
			for(File p : new File(WORKING_FOLDER).listFiles())
			{
				if(al.contains(p.getPath()))
					{
						System.out.println(p.getPath());
						al.remove(p.getPath());
					}
			}
		}
		String s = "";
		for(String st : al)
		{
			s+=st+"\n";
		}
		
		return s;
	}

	private static int CountFolders() {
		
		int count = 3;
		if(Files.isDirectory(Paths.get(WORKING_FOLDER)))
		{
			count--;
			for(File p : new File(WORKING_FOLDER).listFiles())
			{
				System.out.println(p.toString());
				count--;
			}
		}
		
		
		return count;
	}

	public static void clearCaches()
	{
		mWins = 0;
		mLosses = 0;
		mTies = 0;
		previousMoves.empty();
		previousBoards.empty();
		lastWinSet = new int[4];
		runs = 1;
		moves = new Move[9];
		moveCount = 0;
		won = -2;
		board = new String[] {
				"1","2","3","4","5","6","7","8","9"
		}; 
		points = new LinkedList();
		menaceTurn = true;
		
	}
	
	public static void run()
	{
		menaceTurn = true;

		input = new Scanner(System.in);
		
		board = new String[] {
				"1","2","3","4","5","6","7","8","9"
		};

		moves = new Move[9];
		moveCount = 0;
		won = -2;
		
		previousBoards = new Stack<String[]>();
		previousMoves = new Stack<Move[]>();
		
		previousBoards.push(board.clone());
		previousMoves.push(moves.clone());
		
		while(moveCount<9)
			{
				boolean valid = true;
				
				do {
					if(PreviousGames.loadedGame)
						return;
					DisplayBoard();

					valid = true;
					String inx = input.nextLine();
					if(inx.equals(""))
						{
							valid = false;
							continue;
						}
					char inn = inx.charAt(0);
					int in = Integer.valueOf(inn)-48;
					
					if(in <1 || in >9)
						{
							if(in+48 == 'b'|| in+48=='B')
							{
								//getfrom stack;
								if(moveCount>0) {
									previousBoards.pop();
									previousMoves.pop();

									for(String[] s :previousBoards)
										for(String s1 : s)
											System.out.println(s1);
									board = previousBoards.peek().clone();
									moves = previousMoves.peek().clone();
									moveCount--;
									menaceTurn = !menaceTurn;
								}else
									System.out.println("Could not retrieve previous setup");
							}else if(in+48 == 'r'|| in+48 == 'R')
							{
								run();
								return;
							}
							else
								System.out.println("That's not a number!");
							
							valid = false;
							continue;
						}

					if(board[(in)-1]=="x"||board[(in)-1]=="o")
						{

							System.out.println("That was not a valid place to put a move");
							valid = false;
							continue;
						}
					
					if(menaceTurn)
						board[(in)-1] = "x";
					else
						board[(in)-1] = "o";
					
					moves[moveCount] = new Move(menaceTurn,in-1);
					moveCount++;
				}while(!valid);
				
				previousBoards.push(board.clone());
				previousMoves.push(moves.clone());
				
				if(moveCount>4)
					{
						won = rTestWin();
						winner = won;
						DetermineShape();
						
					}
				menaceTurn = !menaceTurn;
				
				if(won==0 && moveCount == 9)//draw
				{
					System.out.println("DRAW!");
					mTies++;
				}
				else if(won==1)//draw
				{
					System.out.println("Menace Won!");
					mWins++;
				}
				else if(won==-1)//draw
				{

					System.out.println("Menace Lost!");
					mLosses++;
				}else
					won = -2;
			
				if(won!=-2)
				{
					System.out.println("Is this okay? (y/n)");

					Scanner rc = new Scanner(System.in);
					boolean b = false;
					do {
						if(PreviousGames.loadedGame)
							{
								rc.close();
								return;
							}
					String approve = rc.nextLine().toLowerCase();
						if(approve.length()==0)
							continue;
					
						if(approve.charAt(0) == 'y')
						{
							b = true;
							break;
						}
						else if(approve.charAt(0)=='n')
							{
								won = -2;
								break;
							}
					}while(true);
						
					if(!b)
						{
						previousBoards.pop();
						previousMoves.pop();
						
						board = previousBoards.peek().clone();
						moves = previousMoves.peek().clone();
						
						moveCount--;
						menaceTurn = !menaceTurn;
						}
					else break;
				}
			}
		
		
		points.Add(new Node(new Point((int)(MyCanvas.dx*(runs+1)),window.canvas.getHeight()/2+(mLosses-mWins)*MyCanvas.dy,moves)));
		
		lastBoard = board.clone();
		window.Update();
		runs++;
		System.out.println("\nPress Enter to Start a New Game\n");
		sc = new Scanner(System.in);
		sc.nextLine();
		
	}

	public static void DetermineShape() {
		
		int i = lastWinSet[1]+lastWinSet[2]-lastWinSet[0]*2;
		System.out.println(lastWinSet[0]+" " + lastWinSet[1]+" "+lastWinSet[2]);
		if(i == 9)
		{
			lastWinSet[3] = VERTICAL;
		}else if(i == 12)
		{
			lastWinSet[3] = NDIAG;
		}
		else if(i == 6)
		{
			lastWinSet[3] = PDIAG;
		}else if(i == 3)
		{
			lastWinSet[3] = HORIZONTAL;
		}
		
	}

	private static int TestWin()
	{
		if(!menaceTurn)//players turn
			{
				if((board[0]==board[3] && board[0]==board[6] && board[0] == "o") || (board[0]==board[1] && board[1]==board[2]&& board[0] == "o") 
						|| (board[0]==board[4] && board[0]==board[8]&& board[0] == "o") || (board[1]==board[4] && board[1]==board[7]&& board[1] == "o") 
						|| (board[2]==board[4] && board[2]==board[6]&& board[2] == "o") ||(board[2]==board[5] && board[2]==board[8]&& board[2] == "o") 
						|| (board[3]==board[4] && board[3]==board[5]&& board[3] == "o") ||(board[6]==board[7] && board[6]==board[8]&& board[6] == "o"))
					{
						//Player won
						return -1;
					}
			}
		else
			{
				if((board[0]==board[3] && board[0]==board[6] && board[0] == "x") || (board[0]==board[1] && board[1]==board[2]&& board[0] == "x") 
						|| (board[0]==board[4] && board[0]==board[8]&& board[0] == "x") || (board[1]==board[4] && board[1]==board[7]&& board[1] == "x") 
						|| (board[2]==board[4] && board[2]==board[6]&& board[2] == "x") ||(board[2]==board[5] && board[2]==board[8]&& board[2] == "x") 
						|| (board[3]==board[4] && board[3]==board[5]&& board[3] == "x") ||(board[6]==board[7] && board[6]==board[8]&& board[6] == "x"))
					{
						//Menace won
						return 1;
					}
			}
		return 0;
		
	}

	static int rTestWin()
	{
		if(!menaceTurn)//players turn
			{
			if((board[0]==board[3] && board[0]==board[6] && board[0] == "o"))
			{
				lastWinSet = new int[] {
					0,3,6,0
				};
				return -1;
			}
			else if((board[0]==board[1] && board[1]==board[2] && board[0] == "o"))
			{
				lastWinSet = new int[] {
					0,1,2,0
				};
				
				return -1;
			}
			else if((board[0]==board[4] && board[4]==board[8] && board[0] == "o"))
			{
				lastWinSet = new int[] {
					0,4,8,0
				};
				return -1;
			}else if((board[1]==board[4] && board[1]==board[7] && board[1] == "o"))
			{
				lastWinSet = new int[] {
					1,4,7,0
				};
				
				return -1;
			}
			else if((board[2]==board[4] && board[4]==board[6] && board[2] == "o"))
			{
				lastWinSet = new int[] {
					2,4,6,0
				};
				return -1;
			}else if((board[2]==board[5] && board[2]==board[8] && board[2] == "o"))
			{
				lastWinSet = new int[] {
					2,5,8,0
				};
				
				return -1;
			}
			else if((board[3]==board[4] && board[4]==board[5] && board[3] == "o"))
			{
				lastWinSet = new int[] {
					3,4,5,0
				};
				return -1;
			}else if((board[6]==board[7] && board[6]==board[8] && board[6] == "o"))
			{
				lastWinSet = new int[] {
					6,7,8,0
				};
				return -1;
			}
			}
		else
			{
			if((board[0]==board[3] && board[0]==board[6] && board[0] == "x"))
			{
				lastWinSet = new int[] {
					0,3,6,0
				};
				return 1;
			}
			else if((board[0]==board[1] && board[1]==board[2] && board[0] == "x"))
			{
				lastWinSet = new int[] {
					0,1,2,0
				};
				
				return 1;
			}
			else if((board[0]==board[4] && board[4]==board[8] && board[0] == "x"))
			{
				lastWinSet = new int[] {
					0,4,8,0
				};
				return 1;
			}else if((board[1]==board[4] && board[1]==board[7] && board[1] == "x"))
			{
				lastWinSet = new int[] {
					1,4,7,0
				};
				
				return 1;
			}
			else if((board[2]==board[4] && board[4]==board[6] && board[2] == "x"))
			{
				lastWinSet = new int[] {
					2,4,6,0
				};
				return 1;
			}else if((board[2]==board[5] && board[2]==board[8] && board[2] == "x"))
			{
				lastWinSet = new int[] {
					2,5,8,0
				};
				
				return 1;
			}
			else if((board[3]==board[4] && board[4]==board[5] && board[3] == "x"))
			{
				lastWinSet = new int[] {
					3,4,5,0
				};
				return 1;
			}else if((board[6]==board[7] && board[6]==board[8] && board[6] == "x"))
			{
				lastWinSet = new int[] {
					6,7,8,0
				};
				return 1;
			}
			}
		return 0;
		
	}
	static void DisplayBoard()
	{
		if(menaceTurn)
			System.out.println("What was MENACE's move?");
		else
			System.out.println("What was PLAYER's move?");
		
		System.out.println("========================");
		System.out.println("  " + board[0] + " | " + board[1] + " | " + board[2]);
		System.out.println(" -----------");
		System.out.println("  " + board[3] + " | " + board[4] + " | " + board[5]);
		System.out.println(" -----------");
		System.out.println("  " + board[6] + " | " + board[7] + " | " + board[8]);	
		System.out.println("========================");
		
	}

	public static void close() {
		System.exit(0);
	}
	
	// Internal Classes
	public static class MyThread extends Thread
	{
		@Override
		public void run()
		{
			while(true) {
				synchronized(window)
				{
					while(PreviousGames.loadedGame)
						try {
							window.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
				
				if(!PreviousGames.loadedGame)
					Main.run();
				}
		}
		
		
	}

}
