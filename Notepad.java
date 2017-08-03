package xjx;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.undo.UndoManager;

/*
 * 此类中的部分函数直接使用了网上找的代码，其余部分为本人所写
 *
 */

public class Notepad extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1651590151789673694L;
	
	private Font f = new Font("微软雅黑",Font.PLAIN,15);
	private Font f2 = new Font("微软雅黑",Font.PLAIN,12);
	private Font default_font = new Font("微软雅黑",Font.PLAIN,16);
	private JTextArea workspace = new JTextArea("");
	private JScrollPane scroll;
	private int hour,minute,second;
	private Calendar Cld;
	private JLabel label,label2,label3;
	private int linenum = 1,columnnum = 1,text_length = 0;
    private int flag = 0;	//0:刚启动程序
    						//1：新建的文件 
    						//2：修改过的文件
    						//3：保存过的文件
    String currentFileName = null; //当前文件名
    String currentPath = null;	//当前文件路径
    
    public Clipboard clipboard = new Clipboard("系统剪切板"); 
    public UndoManager undoMgr = new UndoManager(); //撤销管理器
    
    public static String Search_history = "";
    private int startpos = 0;
    private Highlighter highLighter;
    
    private LineCounter sideLine = new LineCounter();
    
	public Notepad(){
		super("Notepad By XJX");
		Image img = Toolkit.getDefaultToolkit().getImage("title.png");//窗口图标
		setIconImage(img);
	    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    setSize(900,600);
	    setLocationRelativeTo(null);
	    
	    String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
	    
		scroll = new JScrollPane(workspace, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		sideLine.setLineHeight(22);
		scroll.setRowHeaderView(sideLine);
		workspace.setFont(default_font);
		workspace.getDocument().addUndoableEditListener(undoMgr);
		add(scroll,BorderLayout.CENTER);
		
		highLighter = workspace.getHighlighter();
		
	    //添加菜单栏
	    JMenuBar bar = new JMenuBar();
        bar.setBackground(Color.white);
  		setJMenuBar(bar);
  		JMenu File = new JMenu("文件(F)");
  		File.setFont(f);
  		JMenu Edit = new JMenu("编辑(E)");
  		Edit.setFont(f);
  		JMenu Format = new JMenu("格式(O)");
  		Format.setFont(f);
  		JMenu View = new JMenu("查看(V)");
  		View.setFont(f);
  		JMenu Help = new JMenu("帮助(H)");
  		Help.setFont(f);
  		bar.add(File);
  		bar.add(Edit);
  		bar.add(Format);	
  		bar.add(View);	
  		bar.add(Help);
  		
  		JMenuItem newfile = new JMenuItem("新建(N)");
  		newfile.setFont(f2);
  		newfile.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,
                java.awt.Event.CTRL_MASK));
  		JMenuItem openfile = new JMenuItem("打开(O)");
  		openfile.setFont(f2);
  		openfile.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O,
                java.awt.Event.CTRL_MASK));
  		JMenuItem savefile = new JMenuItem("保存(S)");
  		savefile.setFont(f2);
  		savefile.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,
                java.awt.Event.CTRL_MASK));
  		JMenuItem saveAs = new JMenuItem("另存为(A)");
  		saveAs.setFont(f2);
  		JSeparator separator = new JSeparator();
  		JMenuItem exit = new JMenuItem("退出(X)");
  		exit.setFont(f2);
  		File.add(newfile);
  		File.add(openfile);
  		File.add(savefile);
  		File.add(saveAs);
  		File.add(separator);
  		File.add(exit);
  		
  		JMenuItem undo = new JMenuItem("撤销(U)");
  		undo.setFont(f2);
  		undo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z,
                java.awt.Event.CTRL_MASK));
  		JSeparator separator2 = new JSeparator();//分隔线
  		JMenuItem cut = new JMenuItem("剪切(T)");
  		cut.setFont(f2);
  		cut.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X,
                java.awt.Event.CTRL_MASK));
  		JMenuItem copy = new JMenuItem("复制(C)");
  		copy.setFont(f2);
  		copy.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C,
                java.awt.Event.CTRL_MASK));
  		JMenuItem paste = new JMenuItem("粘贴(P)");
  		paste.setFont(f2);
  		paste.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V,
                java.awt.Event.CTRL_MASK));
  		JMenuItem delete = new JMenuItem("删除(L)");
  		delete.setFont(f2);
  		delete.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
  		JSeparator separator3 = new JSeparator();
  		JMenuItem find = new JMenuItem("查找(F)");
  		find.setFont(f2);
  		find.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F,
                java.awt.Event.CTRL_MASK));
  		JMenuItem findnext = new JMenuItem("查找下一个(N)");
  		findnext.setFont(f2);
  		findnext.setAccelerator(KeyStroke.getKeyStroke("F3"));
  		JMenuItem replace = new JMenuItem("替换(R)");
  		replace.setFont(f2);
  		replace.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R,
                java.awt.Event.CTRL_MASK));
  		JMenuItem go = new JMenuItem("转到(G)");
  		go.setFont(f2);
  		go.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G,
                java.awt.Event.CTRL_MASK));
  		JSeparator separator4 = new JSeparator();
  		JMenuItem selectall = new JMenuItem("全选(A)");
  		selectall.setFont(f2);
  		selectall.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A,
                java.awt.Event.CTRL_MASK));
  		JMenuItem date = new JMenuItem("时间/日期(D)");
  		date.setFont(f2);
  		date.setAccelerator(KeyStroke.getKeyStroke("F5"));
  		Edit.add(undo);
  		Edit.add(separator2);
  		Edit.add(cut);
  		Edit.add(copy);
  		Edit.add(paste);
  		Edit.add(delete);
  		Edit.add(separator3);
  		Edit.add(find);
  		Edit.add(findnext);
  		Edit.add(replace);
  		Edit.add(go);
  		Edit.add(separator4);
  		Edit.add(selectall);
  		Edit.add(date);
  		
  		JCheckBoxMenuItem linewrap = new JCheckBoxMenuItem("自动换行(W)      ");
  		linewrap.setSelected(false);
  		linewrap.setFont(f2);
  		JMenuItem font = new JMenuItem("字体(F)      ");
  		font.setFont(f2);
  		Format.add(linewrap);
  		Format.add(font);
  		
  		JCheckBoxMenuItem statusbar = new JCheckBoxMenuItem("状态栏(S)     ");
  		statusbar.setSelected(false);
  		statusbar.setFont(f2);
  		View.add(statusbar);
  		
  		JMenuItem help = new JMenuItem("查看帮助(H)   ");
  		help.setFont(f2);
  		JSeparator separator5 = new JSeparator();
  		JMenuItem about = new JMenuItem("关于记事本(A)   ");
  		about.setFont(f2);
  		Help.add(help);
  		Help.add(separator5);
  		Help.add(about);
  		
  		JToolBar toolState = new JToolBar();
  		toolState.setBackground(Color.white);
        toolState.setSize(workspace.getSize().width, 10);
        label = new JLabel();
        toolState.add(label);
        toolState.addSeparator();
        label2 = new JLabel();
        toolState.add(label2);
        toolState.addSeparator();
        label3 = new JLabel();
        toolState.add(label3);
        add(toolState,BorderLayout.SOUTH);
        toolState.setVisible(false);
  		
  		//添加监听器   
  		about.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		new About();
        	}
        });
		
		help.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		new Help();
        	}
        });

		statusbar.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		if(statusbar.getState())//激活状态栏
        		{
        			if(! linewrap.isSelected())
        			{
        				new Timer();
            			label2.setText("    第 " + linenum + " 行, 第 " + (columnnum+1)+" 列   ");
                        text_length = workspace.getText().toString().length();
                        label3.setText("   共 " + text_length + " 字  ");
            			toolState.setVisible(true);
            			
            			linewrap.setForeground(Color.gray);
            			linewrap.setEnabled(false);
        			}
        		}
        		else
        		{
        			toolState.setVisible(false);
        			linewrap.setForeground(Color.black);
        			linewrap.setEnabled(true);
        		}
        	}
        });
		
		linewrap.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		if(linewrap.getState())//激活自动换行
        		{
        			if(! statusbar.isSelected())//必须先取消状态栏才能激活自动换行
        			{
        				int caretpos = workspace.getCaretPosition();
                        try {
    						linenum = workspace.getLineOfOffset(caretpos);
    						 columnnum = caretpos - workspace.getLineStartOffset(linenum);
    					} catch (BadLocationException e1) {
    						e1.printStackTrace();
    					}
                        linenum += 1;
                        label2.setText("    第 " + linenum + " 行, 第 " + (columnnum+1)+" 列   ");
                        text_length = workspace.getText().toString().length();
                        label3.setText("   共 " + text_length + " 字  ");
            			workspace.setLineWrap(true);
            			workspace.setWrapStyleWord(true) ;
            			
            			statusbar.setForeground(Color.gray);
            			statusbar.setEnabled(false);
            			go.setForeground(Color.gray);
        			}

        		}
        		else
        		{
        			workspace.setLineWrap(false);
        			workspace.setWrapStyleWord(false) ;
        			statusbar.setForeground(Color.black);
        			statusbar.setEnabled(true);
        			go.setForeground(Color.black);
        		}
        	}
        });
		
		workspace.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                JTextArea editArea = (JTextArea)e.getSource();
                try {
                    int caretpos = editArea.getCaretPosition();
                    linenum = editArea.getLineOfOffset(caretpos);
                    columnnum = caretpos - workspace.getLineStartOffset(linenum);
                    linenum += 1;
                    label2.setText("    第 " + linenum + " 行, 第 " + (columnnum+1)+" 列   ");
                    text_length = workspace.getText().toString().length();
                    label3.setText("   共 " + text_length + " 字  ");
                    
                    if(workspace.getSelectedText() == null)
            		{
            			cut.setForeground(Color.gray);
            			copy.setForeground(Color.gray);
            			delete.setForeground(Color.gray);
//            			System.out.println("Not selected");
            		}
            		else
            		{
            			cut.setForeground(Color.black);
            			copy.setForeground(Color.black);
            			delete.setForeground(Color.black);
//            			System.out.println("selected");
            		}
                }
                catch(Exception ex) { }
            }
        });
		
		workspace.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                Character c=e.getKeyChar();
                if(c != null && !workspace.getText().toString().equals("")){
                    flag=2;
                }
            }
        });
		
		workspace.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) { 
				highLighter.removeAllHighlights();
//				System.out.println("mouseClicked");
			} 
		});
		
		font.addActionListener(new ActionListener(){
        	@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e){
        		FontChooser fontChooser = new FontChooser(workspace.getFont());
        		fontChooser.showFontDialog(Notepad.this);
        		workspace.setFont(fontChooser.getSelectFont());
        		
        		
        		FontMetrics fm = sun.font.FontDesignMetrics.getMetrics( fontChooser.getSelectFont() ); 
        		System.out.println("当前的字体高度为：" + fm.getHeight());
        		sideLine.setLineHeight(fm.getHeight());
        		
        		sideLine.start_pos =  fm.getHeight() / 2 - 6;
        		
        		repaint();
        	}
        });
		
		newfile.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		newFile();
        	}
        });
		
		openfile.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		openFile();
        	}
        });
		
		savefile.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		save();
        	}
        });
		
		saveAs.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		saveAs();
        	}
        });
		
		exit.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		exit();
        	}
        });
		
		copy.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		copy();
        	}
        });
		
		cut.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		cut();
        	}
        });
		
		delete.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		delete();
        	}
        });
		
		paste.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		paste();
        	}
        });
		
		undo.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		undoMgr.undo();
        	}
        });
		
		date.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		
        		String str = "";
    			Cld = Calendar.getInstance();
    			int hour = Cld.get(Calendar.HOUR_OF_DAY);
    	        int minute = Cld.get(Calendar.MINUTE);
    	        int second = Cld.get(Calendar.SECOND);
    			int year = Cld.get(Calendar.YEAR);
    	        int month = Cld.get(Calendar.MONTH) + 1;
    	        int day = Cld.get(Calendar.DATE);
    	        
    	        if(hour < 10)
    	        	str = "0" + hour + ":";
		        else
		        	str = "" + hour + ":";
		         
		        if(minute < 10)
		        	str = str + "0" + minute + ":";
		        else
		        	str = str + "" + minute + ":";
		         
		        if(second < 10)
		        	str = str + "0" + second;
		        else
		        	str = str + "" + second;
		        
    	        str += " " + year + "/" + month + "/" + day;
        		if(workspace.getSelectedText() != null)
                {
                    int start = workspace.getSelectionStart();
                    int end = workspace.getSelectionEnd();
                    workspace.replaceRange(str, start, end);
                }
                else
                {
                    //获取鼠标所在TextArea的位置
                    int mouse = workspace.getCaretPosition();
                    //在鼠标所在的位置粘贴内容
                    workspace.insert(str, mouse);
                }
        	}
        });
		
		find.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		Search search = new Search(workspace);
        		String T = workspace.getSelectedText();
        		if(T != null)
        		{
        			search.t.setText(T);
        			search.t.setBorder(BorderFactory.createLineBorder(Color.blue));
        		}
        	}
        });
		
		findnext.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		int ptr;
        		String S,T;
        		String temp;
				int pos;
				int pos_temp;
        		DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(new Color(65,105,225));
        		
        		if(!Search_history.equals(""))
				{
					if(!workspace.getText().equals(""))
					{
						ptr = workspace.getCaretPosition();
						System.out.println(ptr);
						temp = workspace.getText().substring(ptr, workspace.getText().length());
						
						S = '#' + temp;
	            		T = '#' + Search_history;
	       
	            		pos = Search.Index(S, T);
	            		if(pos > 0)//找到了
	            		{
	            			pos_temp = workspace.getText().length() - temp.length() + pos - 1;
	            			System.out.println("pos_temp:" + pos_temp);
		            	
	            			highLighter.removeAllHighlights();
		            		try {
		        				highLighter.addHighlight(pos_temp, pos_temp + T.length() - 1, p);
		        				workspace.setCaretPosition(pos_temp + T.length() - 1);
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
		
		replace.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		Replace r = new Replace(workspace);
        		String T = workspace.getSelectedText();
        		if(T != null)
        		{
        			r.t.setText(T);
        			r.t.setBorder(BorderFactory.createLineBorder(Color.blue));
        		}
        	}
        });
		
		go.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		if(! linewrap.isSelected())
        		{
        			new TurnTo();
        		}
        	}
        });
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				exit();
			}
		});
		
	    setVisible(true);
	}
	
	private void newFile() {
        if(flag == 0 || flag == 1){        //刚启动记事本为0，刚新建文档为1
            return;
        }else if(flag == 2 && this.currentPath == null){        //修改后
            //1、（刚启动记事本为0，刚新建文档为1）条件下修改后
            int result=JOptionPane.showConfirmDialog(Notepad.this, "是否将更改保存到无标题?", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                this.saveAs();        //另存为                
            }else if(result==JOptionPane.NO_OPTION){
                this.workspace.setText("");
                this.setTitle("无标题");
                flag = 1;
            }
            return;
        }else if(flag == 2 && this.currentPath != null ){
            //2、（保存的文件为3）条件下修改后
            int result=JOptionPane.showConfirmDialog(Notepad.this, "是否将更改保存到" + this.currentPath + "?", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                this.save();        //直接保存，有路径
            }else if(result == JOptionPane.NO_OPTION){
                this.workspace.setText("");
                this.setTitle("无标题");
                flag = 1;
            }
        }else if(flag == 3){        //保存的文件
            this.workspace.setText("");
            flag = 1;
            this.setTitle("无标题");
        }
    }
	
	private void openFile() {
        if(flag == 2 && this.currentPath == null){
            //1、（刚启动记事本为0，刚新建文档为1）条件下修改后
            int result=JOptionPane.showConfirmDialog(Notepad.this, "是否将更改保存到无标题?", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                this.saveAs();
            }
        }else if(flag==2 && this.currentPath!=null){
            //2、（打开的文件2，保存的文件3）条件下修改
            int result=JOptionPane.showConfirmDialog(Notepad.this, "是否将更改保存到"+this.currentPath+"?", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                this.save();
            }
        }
        //打开文件选择框
        JFileChooser choose=new JFileChooser();
        //选择文件
        int result=choose.showOpenDialog(this);
        if(result==JFileChooser.APPROVE_OPTION){
            //取得选择的文件
            File file=choose.getSelectedFile();
            //打开已存在的文件，提前将文件名存起来
            currentFileName=file.getName();
            //存在文件全路径
            currentPath=file.getAbsolutePath();
            flag=3;
            this.setTitle(this.currentPath);
            BufferedReader br = null;
            try {
                //建立文件流[字符流]
                InputStreamReader isr=new InputStreamReader(new FileInputStream(file),"GBK");
                br = new BufferedReader(isr);//动态绑定
                //读取内容
                StringBuffer sb = new StringBuffer();
                String line = null;
                while((line = br.readLine())!=null){
                    sb.append(line + System.getProperty("line.separator"));
                }
                //显示在文本框[多框]
                workspace.setText(sb.toString());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally{
                try {
                    if(br!=null) br.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
	
	private void save() {
        if(this.currentPath==null){
            this.saveAs();
            if(this.currentPath==null){
                return;
            }
        }
        FileWriter fw=null;
        //保存
        try {
            fw=new FileWriter(new  File(currentPath));
            fw.write(workspace.getText());
            //如果比较少，需要写
            fw.flush();
            flag = 3;
            this.setTitle(this.currentPath);
        } catch (IOException e1) {
            e1.printStackTrace();
        }finally{
            try {
                if(fw!=null) fw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
	
	private void saveAs() {
        //打开保存框
        JFileChooser choose = new JFileChooser();
        //选择文件
        int result = choose.showSaveDialog(this);
        if(result == JFileChooser.APPROVE_OPTION){
            //取得选择的文件[文件名是自己输入的]
            File file = choose.getSelectedFile();
            FileWriter fw = null;
            //保存
            try {
                fw = new FileWriter(file);
                fw.write(workspace.getText());
                currentFileName = file.getName();
                currentPath = file.getAbsolutePath();
                //如果比较少，需要写
                fw.flush();
                this.flag = 3;
                this.setTitle(currentPath);
            } catch (IOException e1) {
                e1.printStackTrace();
            }finally{
                try {
                    if(fw!=null) fw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
	
	private void exit() {
        if(flag == 2 && currentPath == null){
            //这是弹出小窗口
            //1、（刚启动记事本为0，刚新建文档为1）条件下修改后
            int result=JOptionPane.showConfirmDialog(Notepad.this, "是否将更改保存到无标题?", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                Notepad.this.saveAs();
            }else if(result==JOptionPane.NO_OPTION){
            	Notepad.this.dispose();
                Notepad.this.setVisible(false);
                System.exit(0);
            }
        }else if(flag==2 && currentPath!=null){
            //这是弹出小窗口
            //1、（刚启动记事本为0，刚新建文档为1）条件下修改后
            int result=JOptionPane.showConfirmDialog(Notepad.this, "是否将更改保存到"+currentPath+"?", "记事本", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                Notepad.this.save();
            }else if(result==JOptionPane.NO_OPTION){
            	Notepad.this.dispose();
                Notepad.this.setVisible(false);
                System.exit(0);
            }
        }else{
            //这是弹出小窗口
            int result=JOptionPane.showConfirmDialog(Notepad.this, "确定关闭？", "系统提示", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION)
            {
                Notepad.this.dispose();
                Notepad.this.setVisible(false);
                System.exit(0);
            }
        }
    }
	
	public void copy(){
		String temp = workspace.getSelectedText();
		//把获取的内容复制到连续字符器，这个类继承了剪贴板接口
		StringSelection text = new StringSelection(temp);
		//把内容放在剪贴板
		this.clipboard.setContents(text, null);
	}
	
	public void cut(){
		copy();
		int start = workspace.getSelectionStart();
		int end = workspace.getSelectionEnd();
		workspace.replaceRange("", start, end);	
	}
	
	public void delete(){
		int start = workspace.getSelectionStart();
		int end = workspace.getSelectionEnd();
		workspace.replaceRange("", start, end);	
	}
	
	public void paste(){
        //Transferable接口，把剪贴板的内容转换成数据
        Transferable contents = this.clipboard.getContents(this);
        //DataFalvor类判断是否能把剪贴板的内容转换成所需数据类型
        DataFlavor flavor = DataFlavor.stringFlavor;
        //如果可以转换
        if(contents.isDataFlavorSupported(flavor)){
            String str;
            try {//开始转换
               str=(String)contents.getTransferData(flavor);
               //如果要粘贴时，鼠标已经选中了一些字符
               if(this.workspace.getSelectedText() != null)
               {
                   int start = this.workspace.getSelectionStart();
                   int end = this.workspace.getSelectionEnd();
                   this.workspace.replaceRange(str, start, end);
               }
               else
               {
                   //获取鼠标所在TextArea的位置
                   int mouse = this.workspace.getCaretPosition();
                   //在鼠标所在的位置粘贴内容
                   this.workspace.insert(str, mouse);
               }
            } catch(UnsupportedFlavorException e) {
               e.printStackTrace();
            } catch (IOException e) {
               e.printStackTrace();
            } catch(IllegalArgumentException e){
               e.printStackTrace();
            }
        }
    }
	
	public static void main(String[] args) {
		new Notepad();
	}
	
	public class TurnTo extends JDialog{
		/**
		 * 
		 */
		private static final long serialVersionUID = -6907379246321977475L;
		private JLabel l = new JLabel("行号(L):");
		private JTextField t = new JTextField();
		private JButton b = new JButton("转到");
		private JButton b2 = new JButton("取消"); 
		private Font f = new Font("微软雅黑",Font.PLAIN,13);
		
		public TurnTo(){
			setTitle("转到指定行");
	        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        setModal(true);//设置为模态窗口
	        setSize(300,160);
	        setResizable(false);
	        setLocationRelativeTo(null);
	        setBackground(Color.white);
	        setLayout(null);
	        
	        add(l);
	        l.setFont(f);
	        l.setBounds(5, 10, 80, 20);
	        add(t);
	        t.setFont(f);
	        t.setBounds(5, 35, 280, 30);
	        add(b);
	        b.setFont(f);
	        b.setBounds(110, 80, 80, 30);
	        add(b2);
	        b2.setFont(f);
	        b2.setBounds(205, 80, 80, 30);
	        
	        t.setBorder(BorderFactory.createLineBorder(Color.blue));
	        b.setBorder(BorderFactory.createLineBorder(Color.blue));
	        
	        t.addKeyListener(new KeyAdapter(){
				public void keyPressed(KeyEvent e) {
					boolean flag = true;
					String temp;
					int line_count = 1;
					int line_num = 0;
					if(e.getKeyCode() == KeyEvent.VK_ENTER)
					{
						temp = t.getText();
						
						if(temp.equals(""))
						{
							JOptionPane.showMessageDialog(null,"不能为空！");
							flag = false;
						}
						else
						{
							for(int i = 0;i < temp.length();i++)
							{
								if(temp.charAt(i) >= 48 && temp.charAt(i) <= 57){}
								else
								{
									JOptionPane.showMessageDialog(null,"只能在此处输入数字！");
									flag = false;
									break;
								}
							}
							
							for(int i = 0;i < workspace.getText().length();i++)
							{
								if(workspace.getText().charAt(i) == '\n')
								{
									line_count ++;
								}
							}

							if(flag)
							{
								line_num = Integer.parseInt(temp);
								System.out.println("共有 " + line_count + " 行");
								
								if(line_num > line_count)
								{
									JOptionPane.showMessageDialog(null,"行数超过了文本域的总行数！");
									flag = false;
								}
							}
							
							if(flag)
							{
								int tag = 0;
								
								if(workspace.getText().equals(""))
								{
									workspace.setCaretPosition(0);
									TurnTo.this.setVisible(false);
									TurnTo.this.dispose();
								}
								else
								{
									for(int i = 0;i < workspace.getText().length();i++)
									{
										if(tag == line_num - 1)
										{
											workspace.setCaretPosition(i);
											TurnTo.this.setVisible(false);
											TurnTo.this.dispose();
											break;
										}
										else
										{
											if(workspace.getText().charAt(i) == '\n')
											{
												tag ++;
											}
										}							
									}
								}
							}	
						}
					}
				}
	        });
	        
	        b.addActionListener(new ActionListener(){
	        	public void actionPerformed(ActionEvent e){
	        		
	        		boolean flag = true;
					String temp;
					int line_count = 1;
					int line_num = 0;
	        		
	        		temp = t.getText();
					
					if(temp.equals(""))
					{
						JOptionPane.showMessageDialog(null,"不能为空！");
						flag = false;
					}
					else
					{
						for(int i = 0;i < temp.length();i++)
						{
							if(temp.charAt(i) >= 48 && temp.charAt(i) <= 57){}
							else
							{
								JOptionPane.showMessageDialog(null,"只能在此处输入数字！");
								flag = false;
								break;
							}
						}
						
						for(int i = 0;i < workspace.getText().length();i++)
						{
							if(workspace.getText().charAt(i) == '\n')
							{
								line_count ++;
							}
						}

						if(flag)
						{
							line_num = Integer.parseInt(temp);
							System.out.println("共有 " + line_count + " 行");
							
							if(line_num > line_count)
							{
								JOptionPane.showMessageDialog(null,"行数超过了文本域的总行数！");
								flag = false;
							}
						}
						
						if(flag)
						{
							int tag = 0;
							
							if(workspace.getText().equals(""))
							{
								workspace.setCaretPosition(0);
								TurnTo.this.setVisible(false);
								TurnTo.this.dispose();
							}
							else
							{
								for(int i = 0;i < workspace.getText().length();i++)
								{
									if(tag == line_num - 1)
									{
										workspace.setCaretPosition(i);
										TurnTo.this.setVisible(false);
										TurnTo.this.dispose();
										break;
									}
									else
									{
										if(workspace.getText().charAt(i) == '\n')
										{
											tag ++;
										}
									}							
								}
							}
						}
						
					}
	        	}
	        });
	        
	        b2.addActionListener(new ActionListener(){
	        	public void actionPerformed(ActionEvent e){
	        		TurnTo.this.setVisible(false);
					TurnTo.this.dispose();
	        	}
	        });
	        
	        setVisible(true);
		}
	}
	
	//计时器类
	class Timer extends Thread{  
		    public Timer(){
		        this.start();
		    }
		    @Override
		    public void run() {
		        while(true){
		            
		            if(true){
		            	Cld = Calendar.getInstance();
	        			hour = Cld.get(Calendar.HOUR_OF_DAY);
		    	        minute = Cld.get(Calendar.MINUTE);
		    	        second = Cld.get(Calendar.SECOND);
		                showTime();
		            }
		            
		            try {
		                Thread.sleep(1000);
		            } catch (InterruptedException e) {
		                e.printStackTrace();
		            }
		        }
		    }

		    private void showTime(){
		        String strTime ="" ;
		        if(hour < 10)
		            strTime = "0"+hour+":";
		        else
		            strTime = ""+hour+":";
		         
		        if(minute < 10)
		            strTime = strTime+"0"+minute+":";
		        else
		            strTime =strTime+ ""+minute+":";
		         
		        if(second < 10)
		            strTime = strTime+"0"+second;
		        else
		            strTime = strTime+""+second;
		         
		        //在窗体上设置显示时间
		        label.setText("当前系统时间：" + strTime + " " + "CST   ");
		    }
		}

}
