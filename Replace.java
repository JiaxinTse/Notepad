package xjx;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

/*
 * 此类为本人所写
 */

class Coordinate{
	int x,y;
	public Coordinate(int x,int y){
		this.x = x;
		this.y = y;
	}
}

public class Replace extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8283481187792736196L;
	
	private JLabel l = new JLabel("查找内容(N):");
	private JLabel l2 = new JLabel("替换为(P):");
	public JTextField t = new JTextField();
	private JTextField t2 = new JTextField();
	private JButton b = new JButton("查找下一个(F)");
	private JButton b2 = new JButton("替换(R)");
	private JButton b3 = new JButton("全部替换(A)");
	private JButton b4 = new JButton("取消");
	private JCheckBox matchcase = new JCheckBox("区分大小写(C)");
	private Font f = new Font("微软雅黑",Font.PLAIN,13);
	private Highlighter highLighter;
	private int startpos = 0;
    private String temp;
    private int start_pos = 0,end_pos = 0;
    private String history_content = "";
    
	public Replace(JTextArea workspace){
		setTitle("替换");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setSize(430,220);
        setResizable(false);
        setLocationRelativeTo(null);
        setBackground(Color.white);
        setLayout(null);
        
        temp = workspace.getText();
        highLighter = workspace.getHighlighter();
        
        add(l);
        l.setFont(f);
        l.setBounds(5, 10, 80, 30);
        add(t);
        t.setFont(f);
        t.setBounds(90, 10, 200, 30);
        add(l2);
        l2.setFont(f);
        l2.setBounds(5, 50, 80, 30);
        add(t2);
        t2.setFont(f);
        t2.setBounds(90, 50, 200, 30);
        
        add(b);
        b.setFont(f);
        b.setBounds(300, 10, 120, 30);
        add(b2);
        b2.setFont(f);
        b2.setBounds(300, 50, 120, 30);
        add(b3);
        b3.setFont(f);
        b3.setBounds(300, 90, 120, 30);
        add(b4);
        b4.setFont(f);
        b4.setBounds(300, 130, 120, 30);
        add(matchcase);
        matchcase.setFont(f);
        matchcase.setBounds(10, 130, 120, 30);
        
        //添加监听器
        b.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		
        		String S,T,temp_S,temp_T;
				int pos;
				int pos_temp;
        		DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(new Color(65,105,225));
        		
        		if(!t.getText().equals(""))
				{
					if(!workspace.getText().equals(""))
					{
						if(! t.getText().equals(history_content))//查找内容改变了，重新从头开始查找
						{
							temp = workspace.getText();
							startpos = 0;
						}
						
						S = '#' + temp;
//						System.out.println(S);
	            		T = '#' + t.getText();
	            		
	            		history_content = t.getText();//把此次查找框中的内容重新赋给history_content
	            		
	            		if(!matchcase.isSelected())
	            		{
	            			temp_S = S;
	            			temp_T = T;
	            			for(int i = 0;i < 26;i++)//统一把26个英文字母换成小写字母
	            			{
	            				temp_S = temp_S.replace((char)(i + 65), (char)(i + 97));
	            				temp_T = temp_T.replace((char)(i + 65), (char)(i + 97));
	            			}
	            			pos = Search.Index(temp_S, temp_T);
	            			System.out.println(temp_S);
	            			System.out.println(temp_T);
	            		}
	            		else
	            		{
	            			pos = Search.Index(S, T);
	            		}

	            		if(pos > 0)//找到了
	            		{
	            			pos_temp = workspace.getText().length() - temp.length() + pos - 1;
	            			System.out.println("pos_temp:" + pos_temp);
		            		
	            			highLighter.removeAllHighlights();
		            		try {
		        				highLighter.addHighlight(pos_temp, pos_temp + T.length() - 1, p);
		        				start_pos = pos_temp;
		        				end_pos = pos_temp + T.length() - 2;
		        				workspace.setCaretPosition(pos_temp);
		        			}catch (BadLocationException e1) {
		        				e1.printStackTrace();
		        			}
		            		
		            		startpos = pos - 1 + T.length() - 1;
		            		System.out.println("startpos:" + startpos);
		            		temp = temp.substring(startpos, temp.length());
	            		}
	            		else
	            		{
	            			Toolkit.getDefaultToolkit().beep();
	            		}
	            		
					}
					else
					{
						Toolkit.getDefaultToolkit().beep();
//						JOptionPane.showMessageDialog(null, "无法找到结果!", "记事本", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else
				{
					Toolkit.getDefaultToolkit().beep();
//					JOptionPane.showMessageDialog(null, "查找内容不能为空!", "记事本", JOptionPane.INFORMATION_MESSAGE);
				}
        	}
        });
        
        b2.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(new Color(220,20,60));
        		String temp = t2.getText();
        		String temp2 = workspace.getText();
        		System.out.println(start_pos);
        		System.out.println(end_pos);
        		temp2 = temp2.substring(0, start_pos) + temp + temp2.substring(end_pos + 1, temp2.length());
        		workspace.setText(temp2);
        		try {
					highLighter.addHighlight(start_pos, start_pos + temp.length(), p);
					workspace.setCaretPosition(start_pos);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
        	}
        });
        
        b3.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		String temp;
        		String temp4 = workspace.getText();
        		String S,T,temp_S,temp_T;
				int pos;
				int pos_temp;
				DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(new Color(220,20,60));
				Coordinate[] coordinate = new Coordinate[10000];//最多查到10000个结果
				int ptr = 0;
				
        		if(!t.getText().equals(""))
				{
					if(!workspace.getText().equals(""))
					{
						temp4 = temp4.replaceAll(t.getText(), t2.getText());//全部替换
						workspace.setText(temp4);
						
						temp = workspace.getText();
						while(true)
						{
							S = '#' + temp;
		            		T = '#' + t2.getText();

		            		if(!matchcase.isSelected())
		            		{
		            			temp_S = S;
		            			temp_T = T;
		            			for(int i = 0;i < 26;i++)//统一把26个英文字母换成小写字母
		            			{
		            				temp_S = temp_S.replace((char)(i + 65), (char)(i + 97));
		            				temp_T = temp_T.replace((char)(i + 65), (char)(i + 97));
		            			}
		            			pos = Search.Index(temp_S, temp_T);
		            			System.out.println(temp_S);
		            			System.out.println(temp_T);
		            		}
		            		else
		            		{
		            			pos = Search.Index(S, T);
		            		}

		            		if(pos > 0)//找到了
		            		{
		            			pos_temp = workspace.getText().length() - temp.length() + pos - 1;
		            			System.out.println("pos_temp:" + pos_temp);
			            		
			            		coordinate[ptr++] = new Coordinate(pos_temp,pos_temp + T.length() - 1);
			            		
			            		startpos = pos - 1 + T.length() - 1;
			            		System.out.println("startpos:" + startpos);
			            		temp = temp.substring(startpos, temp.length());
		            		}
		            		else
		            		{
		            			Toolkit.getDefaultToolkit().beep();
		            			highLighter.removeAllHighlights();
		            			
		            			try {
		            				for(int i = 0;i < ptr;i++)
		            				{
		            					highLighter.addHighlight(coordinate[i].x, coordinate[i].y, p);
		            				}
			    					
			    					workspace.setCaretPosition(coordinate[ptr - 1].x);
			    				} catch (BadLocationException e1) {
			    					e1.printStackTrace();
			    				}
		            			
		            			break;
		            		}
						}    		
					}
					else
					{
						Toolkit.getDefaultToolkit().beep();
//						JOptionPane.showMessageDialog(null, "无法找到结果!", "记事本", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else
				{
					Toolkit.getDefaultToolkit().beep();
//					JOptionPane.showMessageDialog(null, "查找内容不能为空!", "记事本", JOptionPane.INFORMATION_MESSAGE);
				}
        		
        	}
        });
        
        b4.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		Replace.this.setVisible(false);
        		Replace.this.dispose();
        		highLighter.removeAllHighlights();
        	}
        });
        
        t.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				String S,T,temp_S,temp_T;
				int pos;
				int pos_temp;
        		DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(new Color(65,105,225));
        		
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if(!t.getText().equals(""))
					{
						if(!workspace.getText().equals(""))
						{
							if(! t.getText().equals(history_content))//查找内容改变了，重新从头开始查找
							{
								temp = workspace.getText();
								startpos = 0;
							}
							
							S = '#' + temp;
//							System.out.println(S);
		            		T = '#' + t.getText();
		            		
		            		history_content = t.getText();//把此次查找框中的内容重新赋给history_content
		            		
		            		if(!matchcase.isSelected())
		            		{
		            			temp_S = S;
		            			temp_T = T;
		            			for(int i = 0;i < 26;i++)//统一把26个英文字母换成小写字母
		            			{
		            				temp_S = temp_S.replace((char)(i + 65), (char)(i + 97));
		            				temp_T = temp_T.replace((char)(i + 65), (char)(i + 97));
		            			}
		            			pos = Search.Index(temp_S, temp_T);
		            			System.out.println(temp_S);
		            			System.out.println(temp_T);
		            		}
		            		else
		            		{
		            			pos = Search.Index(S, T);
		            		}

		            		if(pos > 0)//找到了
		            		{
		            			pos_temp = workspace.getText().length() - temp.length() + pos - 1;
		            			System.out.println("pos_temp:" + pos_temp);
			            		
		            			highLighter.removeAllHighlights();
			            		try {
			        				highLighter.addHighlight(pos_temp, pos_temp + T.length() - 1, p);
			        				start_pos = pos_temp;
			        				end_pos = pos_temp + T.length() - 2;
			        				workspace.setCaretPosition(pos_temp);
			        			}catch (BadLocationException e1) {
			        				e1.printStackTrace();
			        			}
			            		
			            		startpos = pos - 1 + T.length() - 1;
			            		System.out.println("startpos:" + startpos);
			            		temp = temp.substring(startpos, temp.length());
		            		}
		            		else
		            		{
		            			Toolkit.getDefaultToolkit().beep();
		            		}
		            		
						}
						else
						{
							Toolkit.getDefaultToolkit().beep();
//							JOptionPane.showMessageDialog(null, "无法找到结果!", "记事本", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					else
					{
						Toolkit.getDefaultToolkit().beep();
//						JOptionPane.showMessageDialog(null, "查找内容不能为空!", "记事本", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
        });
        
        t2.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				String S,T,temp_S,temp_T;
				int pos;
				int pos_temp;
        		DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(new Color(65,105,225));
        		
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if(!t.getText().equals(""))
					{
						if(!workspace.getText().equals(""))
						{
							if(! t.getText().equals(history_content))//查找内容改变了，重新从头开始查找
							{
								temp = workspace.getText();
								startpos = 0;
							}
							
							S = '#' + temp;
//							System.out.println(S);
		            		T = '#' + t.getText();
		            		
		            		history_content = t.getText();//把此次查找框中的内容重新赋给history_content
		            		
		            		if(!matchcase.isSelected())
		            		{
		            			temp_S = S;
		            			temp_T = T;
		            			for(int i = 0;i < 26;i++)//统一把26个英文字母换成小写字母
		            			{
		            				temp_S = temp_S.replace((char)(i + 65), (char)(i + 97));
		            				temp_T = temp_T.replace((char)(i + 65), (char)(i + 97));
		            			}
		            			pos = Search.Index(temp_S, temp_T);
		            			System.out.println(temp_S);
		            			System.out.println(temp_T);
		            		}
		            		else
		            		{
		            			pos = Search.Index(S, T);
		            		}

		            		if(pos > 0)//找到了
		            		{
		            			pos_temp = workspace.getText().length() - temp.length() + pos - 1;
		            			System.out.println("pos_temp:" + pos_temp);
			            		
		            			highLighter.removeAllHighlights();
			            		try {
			        				highLighter.addHighlight(pos_temp, pos_temp + T.length() - 1, p);
			        				start_pos = pos_temp;
			        				end_pos = pos_temp + T.length() - 2;
			        				workspace.setCaretPosition(pos_temp);
			        			}catch (BadLocationException e1) {
			        				e1.printStackTrace();
			        			}
			            		
			            		startpos = pos - 1 + T.length() - 1;
			            		System.out.println("startpos:" + startpos);
			            		temp = temp.substring(startpos, temp.length());
		            		}
		            		else
		            		{
		            			Toolkit.getDefaultToolkit().beep();
		            		}
		            		
						}
						else
						{
							Toolkit.getDefaultToolkit().beep();
//							JOptionPane.showMessageDialog(null, "无法找到结果!", "记事本", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					else
					{
						Toolkit.getDefaultToolkit().beep();
//						JOptionPane.showMessageDialog(null, "查找内容不能为空!", "记事本", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
        });
        
        this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				Replace.this.setVisible(false);
				Replace.this.dispose();
        		highLighter.removeAllHighlights();
			}
		});
        
        setVisible(true);
	}
	
//	public static void main(String[] args) {
//		new Replace();
//	}
}
