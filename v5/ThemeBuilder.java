package v5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.text.PlainDocument;

public class ThemeBuilder extends JFrame implements ActionListener {

	private static final long serialVersionUID = -3719475893938101926L;
	
	JPanel[] swatches;
	JTextArea[] texts;
	JTextArea title;
	JButton update;
	
	public ThemeBuilder()
	{
		super("Theme Builder");
		
		Container contentPane = this.getContentPane();
		
		JPanel Bottom = new JPanel();
		JPanel Middle = new JPanel();
		JPanel Top = new JPanel();
		
		update = new JButton("Update");
			update.addActionListener(this);
			update.setPreferredSize(new Dimension(80,32));
		JButton accept = new JButton("Accept");
			accept.addActionListener(this);
			accept.setPreferredSize(new Dimension(80,32));
		
		title = new JTextArea(Window.scheme.name);
			title.setPreferredSize(new Dimension(220,20));
			title.setBorder(BorderFactory.createLoweredBevelBorder());
			title.setName("Title");
			//title.addKeyListener(this);
		

		PlainDocument doc = (PlainDocument) title.getDocument();
		doc.setDocumentFilter(new FileFilter());
		swatches = new JPanel[ColorScheme.colorCount];
		texts = new JTextArea[ColorScheme.colorCount];
		
		for(int i = 0; i < ColorScheme.colorCount; i++)
		{	
			String n = "";
			
			JPanel p = new JPanel();
			p.setPreferredSize(new Dimension(80,80));
			
			switch(i)
				{
					case 0:
						n = ColorScheme.RIGHT;
						p.setBackground(Window.scheme.c_right);
						p.setToolTipText("The color of the right panel");
						break;
					case 1:
						n = ColorScheme.LEFT;
						p.setBackground(Window.scheme.c_left);
						p.setToolTipText("The color of the left panel");
						break;
					case 2:
						n = ColorScheme.ACCENT1;
						p.setBackground(Window.scheme.c_accent1);
						p.setToolTipText("The color of the border between the left and right panels");
						break;
					case 3:
						n = ColorScheme.BAR;
						p.setBackground(Window.scheme.c_bar);
						p.setToolTipText("The color of the menu bar");
						break;
					case 4:
						n = ColorScheme.ACCENT2;
						p.setBackground(Window.scheme.c_accent2);
						p.setToolTipText("The color of the background of the information boxes in the right panel");
						break;
					case 5:
						n = ColorScheme.FONT;
						p.setBackground(Window.scheme.c_font);
						p.setToolTipText("The color of the font");
						break;
				}
			//TitledBorder outside = BorderFactory.createTitledBorder(n);
				//outside.setTitleColor((jp.getBackground().getRGB() < 0xFFFFFF/2 * -1)?Color.white:Color.black);
			LineBorder inside = new LineBorder(Color.black,2);	
			//jp.setBorder(outside);
			p.setBorder(inside);
			
			String red = Integer.toHexString(p.getBackground().getRed());
			String green = Integer.toHexString(p.getBackground().getGreen());
			String blue = Integer.toHexString(p.getBackground().getBlue());
			
			if(red.length() <= 1)
				red = "0"+red;
			if(blue.length() <= 1)
				blue = "0"+blue;
			if(green.length() <= 1)
				green = "0"+green;
			
			JTextArea jta = new JTextArea(red+""+green+""+blue);
				jta.setPreferredSize(new Dimension(80,20));
				jta.setBorder(BorderFactory.createLoweredBevelBorder());
				//jta.addKeyListener(this);
				doc = (PlainDocument) jta.getDocument();
				doc.setDocumentFilter(new HexFilter());
			JLabel label = new JLabel(n.toUpperCase().charAt(0)+n.substring(1));
			label.setForeground((p.getBackground().getRGB() < 0xFFFFFF/2 * -1)?Color.white:Color.black);
			p.add(label,BorderLayout.NORTH);
			
		
			swatches[i] = p;
			texts[i] = jta;
			Top.add(swatches[i]);
			Middle.add(texts[i]);
			
		}
		
		Top.setBackground(Window.scheme.c_left);
		Middle.setBackground(Window.scheme.c_left);
		Bottom.setBackground(Window.scheme.c_right);
		
		JLabel l = new JLabel("Title:");
		l.setForeground((Bottom.getBackground().getRGB() < 0xFFFFFF/2 * -1)?Color.white:Color.black);

		Bottom.add(l);
		Bottom.add(title);
		Bottom.add(update);
		Bottom.add(accept);
		
		contentPane.add(Top,BorderLayout.NORTH);
		contentPane.add(Middle,BorderLayout.CENTER);
		contentPane.add(Bottom,BorderLayout.SOUTH);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		String name = arg0.getActionCommand().toLowerCase();
		switch(name)
		{
			case "update":
				Update();
				break;
			case "accept":
				if(Update())
				{
					System.out.println(true);
					MyColor[] colors = new MyColor[ColorScheme.colorCount];
					
					if(WriteToFile())
						{
							ColorScheme.schemes.clear();
							ColorScheme.LoadSchemes();

							JMenuItem menu = GetJMenuItem((((JMenu)(Main.window.getJMenuBar().getComponent(0)))),"Themes");
							System.out.println(menu.getClass());
							JMenuItem item = new JMenuItem(title.getText());
								item.setBackground((MyColor)(menu.getBackground()));
								item.setForeground((MyColor)(menu.getForeground()));
								item.addActionListener(Main.window);
							menu.add(item,((JMenu)(menu)).getItemCount()-2);	
							this.dispose();
						}	
				}
				break;
		}
		
	}
	private JMenuItem GetJMenuItem(JMenu jMenu, String string) {
		for(int i = 0; i < jMenu.getItemCount(); i++)
		{
			JComponent j = jMenu.getItem(i);
			
			if(j instanceof JMenuItem && ((JMenuItem)j).getText().equals(string))
				return (JMenu)j;
		}
		return null;
	}
	private boolean WriteToFile() {

		for(ColorScheme c :ColorScheme.builtInSchemes)
		{
			if(c.name.equals(title.getText()))
				{
					JOptionPane.showMessageDialog(this, "That scheme already exists");
					return false;
				}
		}
		
		BufferedWriter br = null;
		
		try {
			File f = new File(Main.themesDirectory.toString()+File.separator+title.getText()+".thm");
		
			if(f.exists())
			{
				int i = JOptionPane.showConfirmDialog(this,null,"File already exists!  Would you like to overwrite it?", JOptionPane.OK_CANCEL_OPTION);
				
				if(i == JOptionPane.CANCEL_OPTION)
					throw new Exception();
				
			} else
				f.createNewFile();
				
			br=new BufferedWriter(new FileWriter(f));
			String s = "";
			for(JTextArea t : texts)
			{
				s += Integer.toHexString(Integer.valueOf((t.getText()),16))+(t.equals(texts[texts.length-1])?"":"\n");
			}
			
			br.write(s);
			return true;
		
		} catch (IOException e) {
			//e.printStackTrace();
		} catch (Exception e) {
			//e.printStackTrace();
		}finally {
			if(br!=null)
			{
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
		
	}
	private boolean Update() {
		boolean b = true;
		for(int i = 0; i<swatches.length;i++)
		{
			if(((JTextArea)texts[i]).getText().length()!=6)
				{
					b = false;
					((JTextArea)texts[i]).setBackground(Color.RED);
					
					continue;
				}else
				{
					((JTextArea)texts[i]).setBackground(Color.WHITE);
					
				}
			String n = "";
			
			switch(i)
			{
				case 0:
					n = ColorScheme.RIGHT;
					break;
				case 1:
					n = ColorScheme.LEFT;
					break;
				case 2:
					n = ColorScheme.ACCENT1;
					break;
				case 3:
					n = ColorScheme.BAR;
					break;
				case 4:
					n = ColorScheme.ACCENT2;
					break;
				case 5:
					n = ColorScheme.FONT;
					break;
			}
			
			swatches[i].setBackground(new MyColor(Integer.valueOf(texts[i].getText(),16), n));
			swatches[i].getComponent(0).setForeground((swatches[i].getBackground().getRGB() < 0xFFFFFF/2 * -1)?Color.white:Color.black);;
		}
		if(!b)
		{
			JOptionPane.showMessageDialog(this,"Some inputs are invalid!");
		}
		
		return b;
	}
	
}
