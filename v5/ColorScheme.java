package v5;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ColorScheme {
	
	public static ArrayList<ColorScheme> schemes = new ArrayList<ColorScheme>();
	public static ArrayList<ColorScheme> builtInSchemes = new ArrayList<ColorScheme>();
	
	
	final static String RIGHT = "right";
	final static String LEFT = "left";
	final static String ACCENT1 = "accent1";
	final static String BAR = "bar";
	final static String ACCENT2 = "accent2";
	final static String FONT = "font";
	
	public MyColor c_right = new MyColor(0xD2DD7E,RIGHT); // Green
	public MyColor c_left = new MyColor(0x75C7B2,LEFT); // Teal
	public MyColor c_accent1 = new MyColor(0x304463,ACCENT1); // Blue
	public MyColor c_bar = new MyColor(0xFAE9C7,BAR); // Soft Pink
	public MyColor c_accent2 = new MyColor(0xE0647B,ACCENT2); // Hard Pink
	public MyColor c_font = new MyColor(0xE0647B,FONT); // Hard Pink
	
	public static int colorCount = 6;
	
	public Font f1,f2,f3;
	
	public String name;
	
	public ColorScheme(String s, MyColor c1,MyColor c2,MyColor c3,MyColor c4, MyColor c5, MyColor c6)
	{
		name = s;
		c_right = c1;
		c_left = c2;
		c_accent1 = c3;
		c_bar = c4;
		c_accent2 = c5;
		c_font = c6;
	}
	
	public static ColorScheme SchemeFactory(String s)
	{
		for(ColorScheme c : builtInSchemes)
		{
			if(c.name.equalsIgnoreCase(s))
			{
				return c;
			}
		}
		
		for(ColorScheme c : schemes)
		{
			if(c.name.equalsIgnoreCase(s))
			{
				return c;
			}
		}
		
		return builtInSchemes.get(0);
	}
	
	public static void LoadDefaultSchemes()
	{
		//Right Panel, Left Panel, Accent1, Bar, Accent2,Font e2b03d
		if(!builtInSchemes.isEmpty())
			return;
		builtInSchemes.add(new ColorScheme("Default",new MyColor(0x363636,RIGHT),new MyColor(0xF1F1F1,LEFT),new MyColor(0xffb73a,ACCENT1),new MyColor(0x404044,BAR),new MyColor(0xffb73a,ACCENT2),new MyColor(0xFFFFFF,FONT)));
		builtInSchemes.add(new ColorScheme("Shoelace",new MyColor(0xD2DD7E,RIGHT),new MyColor(0x75C7B2,LEFT),new MyColor(0x304463,ACCENT1),new MyColor(0xFAE9C7,BAR),new MyColor(0xF0948B,ACCENT2),new MyColor(0xE0647c,FONT)));
		builtInSchemes.add(new ColorScheme("Royalty",new MyColor(0xA31F1F,RIGHT),new MyColor(0xF1F1F1,LEFT),new MyColor(0xefea45,ACCENT1),new MyColor(0x376087,BAR),new MyColor(0xF1F1F1,ACCENT2),new MyColor(0xF1F1F1,FONT)));
		builtInSchemes.add(new ColorScheme("Dark",new MyColor(0x212121,RIGHT),new MyColor(0x363636,LEFT),new MyColor(0xF2F2F2,ACCENT1),new MyColor(0x404048,BAR),new MyColor(0xEE4444,ACCENT2),new MyColor(0xEE4444,FONT)));
		builtInSchemes.add(new ColorScheme("D-Arch",new MyColor(0x212121,RIGHT),new MyColor(0x363636,LEFT),new MyColor(0x0192DD,ACCENT1),new MyColor(0x404048,BAR),new MyColor(0x0092DD,ACCENT2),new MyColor(0x0092DD,FONT)));
		builtInSchemes.add(new ColorScheme("Syrup",new MyColor(0xe42643,RIGHT),new MyColor(0x0766c0,LEFT),new MyColor(0xffe934,ACCENT1),new MyColor(0xe2b03d,BAR),new MyColor(0xe2b03d,ACCENT2),new MyColor(0x000000,FONT)));
	}
	
	public static void LoadSchemes()
	{

		if(!schemes.isEmpty())
			return;
		BufferedReader br = null;
		try {
			if(!Files.isDirectory(Main.themesDirectory))
				Files.createDirectory(Main.themesDirectory);
			
			DirectoryStream<Path> dir = Files.newDirectoryStream(Main.themesDirectory);
		
			for(Path p : dir)
			{
				String name = p.getFileName().toString();
				
				if(name.endsWith(".thm"))
				{
					br = new BufferedReader(new FileReader(p.toString()));
					MyColor[] arr = new MyColor[7];
					int i = 0;
					
					while(br.ready())
					{
						String s = br.readLine();
						if(i == arr.length)
							break;
						String n = "";
						switch(i)
						{
							case 0:
								n = RIGHT;
								break;
							case 1:
								n = LEFT;
								break;
							case 2:
								n = ACCENT1;
								break;
							case 3:
								n = BAR;
								break;
							case 4:
								n = ACCENT2;
								break;
							case 5:
								n = FONT;
								break;
						}
						arr[i] = new MyColor(Integer.valueOf(s,16),n);
						
						i++;
					}
					schemes.add(new ColorScheme(name.substring(0,name.indexOf(".")),arr[0],arr[1],arr[2],arr[3],arr[4], arr[5]));
				}
			}
		
		} catch (IOException e) {
				e.printStackTrace();
			}finally
		{
				if(br!=null)
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
		}
		
	}
}
