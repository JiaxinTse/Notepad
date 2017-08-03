package xjx;

import java.awt.*;
import javax.swing.*;

public class Help extends JDialog {
    private static final long serialVersionUID = 4693799019369193520L;
    private JPanel contentPane;
    private Font f = new Font("微软雅黑",Font.PLAIN,15);
	
    public Help() {
        setTitle("帮助");//设置窗体标题
        Image img=Toolkit.getDefaultToolkit().getImage("title.png");//窗口图标
        setIconImage(img);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModal(true);//设置为模态窗口
        setSize(410,200);
        setResizable(false);
        setLocationRelativeTo(null);
        contentPane = new JPanel();// 创建内容面板
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        ShadePanel shadePanel = new ShadePanel();// 创建渐变背景面板
        contentPane.add(shadePanel, BorderLayout.CENTER);// 添加面板到窗体内容面板
        shadePanel.setLayout(null);
        
        JTextArea J1 = new JTextArea("使用说明书\n"
        		+ "此记事本软件基本遵照win10上的界面以及使用规则，可以用来新建或者修改文本文件。另外加入了一些win10记事本"
        		+ "没有的功能，同时对部分界面以及功能做出了改进。去掉了一些不必要的功能，保留了大多数主要功能。");
        J1.setFocusable(false);
    	J1.setFont(f);
    	J1.setEditable(false);
    	J1.setOpaque(false);//背景透明
    	J1.setLineWrap(true);
    	shadePanel.add(J1);
    	J1.setBounds(10, 10, 380, 200);
    	setVisible(true);
    }
    
    public static void main(String[] args) {
		new Help();
	}
}
