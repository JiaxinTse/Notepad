package xjx;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

/*
 * 此类为本人所写，去掉了逆向查找功能
 */

public class Search extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3693026233123660076L;
	
	private JLabel l = new JLabel("查找内容(N):");
	public JTextField t = new JTextField();
	private JButton b = new JButton("查找下一个(F)");
	private JButton b2 = new JButton("取消");
	private JCheckBox matchcase = new JCheckBox("区分大小写(C)");
	private JPanel p = new JPanel();
	private Font f = new Font("微软雅黑",Font.PLAIN,13);
//	private ButtonGroup bGroup = new ButtonGroup();
//    private JRadioButton up = new JRadioButton("向上(U)");
//    private JRadioButton down = new JRadioButton("向下(D)");
    private int startpos = 0;
    private String temp;
    private Highlighter highLighter;
    private String history_content = "";
    
	public Search(JTextArea workspace){
		setTitle("查找");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setSize(430,180);
        setResizable(false);
        setLocationRelativeTo(null);
        setBackground(Color.white);
        setLayout(null);

        temp = workspace.getText();
        highLighter = workspace.getHighlighter();
        
//		down.setSelected(true);
//        bGroup.add(up);
//        bGroup.add(down);
        
        add(l);
        l.setBounds(5,5,100,25);
        l.setFont(f);
        add(t);
        t.setBounds(90,5,200,28);
        t.setFont(f);
        add(b);
        b.setBounds(300,8,120,25);
        b.setFont(f);
        b.setBorder(BorderFactory.createLineBorder(Color.blue));
        add(b2);
        b2.setBounds(300,50,120,25);
        b2.setFont(f);
        add(matchcase);
        matchcase.setBounds(5,100,120,30);
        matchcase.setFont(f);
//        p.setBorder(BorderFactory.createTitledBorder("方向 "));
//        add(p);
//        p.setBounds(132, 60, 160, 70);
//        p.setLayout(null);
        
//        p.add(up);
//        up.setBounds(5, 20, 75, 30);
//        up.setFont(f);
//        p.add(down);
//        down.setBounds(78, 20, 75, 30);
//        down.setFont(f);
        
        //添加监听器
        b2.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		Search.this.setVisible(false);
        		Search.this.dispose();
        		highLighter.removeAllHighlights();
        	}
        });
        
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
	            		Notepad.Search_history = t.getText();
	            		
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
		            		Notepad.Search_history = t.getText();
		            		
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
				Search.this.setVisible(false);
        		Search.this.dispose();
        		highLighter.removeAllHighlights();
			}
		});
        
        setVisible(true);
	}
	
	/*
	 * 以下查找算法为本人写的KMP算法，当时是用C++写的
	 * 关于此算法的详情请参考本人写的一篇博客
	 * 原文地址：http://www.cnblogs.com/journal-of-xjx/p/5966420.html
	 * 
	 */
	
	public static int[] Next(String s){
		int length = s.length();
		int[] next = new int[length];
//		int[] next = new int[20];
		boolean flag = true;
		int time;
		
		if(length < 2)
		{
			System.out.println("待匹配的字符串的长度不能为0");
		}
		else
		{
			next[0] = length - 1;
		    next[1] = 0;
		    
		    if(length >= 3)
		    {
		    	next[2] = 1;
		    	for(int i = 3;i <= length - 1;i++)
		        {
		            for(time = 1;time <= i-2; time++)
		            {
		                for(int j = 1,k = j + time;j <= i - 1 - time; j++,k++)
		                {
		                	if(s.charAt(j) != s.charAt(k))
		                	{
		                		flag = false;
		                		break;
		                	}
		                }
		                if(flag) break;
		                flag = true;
		            }		            
		            next[i] = i - time;
		        }
		    }
		}
		
		return next;
	}

	public static int Index(String S,String T)
	{
	    int[] next = new int[T.length()];
	    int i=1,j=1,s_len=0,t_len=0,pos=1;
	    s_len = S.length() - 1;
	    t_len = T.length() - 1;
	    next = Next(T);
	    
	    while(i <= s_len && j <= t_len)
	    {
	        if(j == 0) {i++;j++;}
	        
	        if(i > s_len)
	        {
	        	break;
	        }
	        
	        if(S.charAt(i) == T.charAt(j))
	        {
	        	i++;
	        	j++;
	        }
	        else 
	        	j = next[j];
	    }
	    
	    if(j > t_len)
	    {
	        pos = i - t_len;
	        System.out.println( "模式串在主串中第 " + pos + " 个位置匹配!");
	        return pos;
	    }
	    if(i > s_len && j <= t_len)
	    {
	        System.out.println( "模式串无法与主串匹配!");
	        return 0;
	    }
	    return 0;
	}
}
