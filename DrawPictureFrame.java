package draw_panel;
/**
 * by gilded-sky 2019/4/26
 */
import javax.swing.JFrame;//导入窗体类
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.BorderLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import java.awt.BasicStroke;
import javax.swing.JColorChooser;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import com.mr.util.FrameGetShape;
import com.mr.util.ShapeWindow;
import com.mr.util.Shapes;
import com.mr.util.DrawImageUtil;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.AlphaComposite;
import java.awt.Font;
import javax.swing.JOptionPane;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Cursor;


public class DrawPictureFrame extends JFrame implements FrameGetShape{

    BufferedImage image = new BufferedImage(570,390,BufferedImage.TYPE_INT_BGR);
    Graphics gs = image.getGraphics();//获得对象的绘图对象
    Graphics2D g = (Graphics2D) gs;//将绘图对象转化为Graphics2D类型
    DrawPictureCanvas canvas  = new DrawPictureCanvas();//创建画布对象
    Color forecolor = Color.BLACK;//定义前景色
    Color bgColor = Color.WHITE;//定义背景色
    int x= -1;//上一次鼠标绘制点的横坐标
    int y = -1;//上一次鼠标绘制点的纵坐标
    boolean rubber = false; //橡皮标识常量
    private JToolBar toolbar;
    private JButton eraserButton;
    private JToggleButton strokeButton1;
    private JToggleButton strokeButton2;
    private JToggleButton strokeButton3;
    private JButton backgroundButton;
    private JButton foregroundButton;
    private JButton clearButton;
    private JButton saveButton;
    private JButton shapeButton;

    boolean drawShape = false;
    Shapes shape;

    private JMenuItem strokeMenuItem1;
    private JMenuItem strokeMenuItem2;
    private JMenuItem strokeMenuItem3;
    private JMenuItem clearMenuItem;
    private JMenuItem foregroundMenuItem;
    private JMenuItem backgroundMenuItem;
    private JMenuItem eraserMenuItem;
    private JMenuItem exitMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem shuiyinMenuItem;
    private String shuiyin="";

    private PictureWindow picWindow;
    private JButton showPicBUtton;

    /**
     * 构造方法
     */
    public DrawPictureFrame(){
        setResizable(false);//窗体不能改变大小
        setTitle("画板("+shuiyin+")");//设置标题
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//窗体关闭则停止程序
        setBounds(500,100,574,460);//设置窗口大小和位置
        init();
        addListener();
    }

    /**
     * 添加监听动作
     */
    private void addListener(){
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {//当鼠标拖拽时
                if(x > 0 && y > 0){
                    if(rubber){
                        g.setColor(bgColor);
                        g.fillRect(x,y,10,10);
                    }
                    else{
                        g.drawLine(x,y,e.getX(),e.getY());
                    }
                }
                x = e.getX();
                y = e.getY();
                canvas.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if(rubber){
                    Toolkit kit = Toolkit.getDefaultToolkit();

                    Image img = kit.createImage("D:\\project\\project_java\\java_sample\\Code\\01\\Src\\img\\icon\\鼠标橡皮.png");
                    Cursor c = kit.createCustomCursor(img,new Point(0,0),"clear");
                    setCursor(c);
                }
                else{
                    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                }
            }
        });
        canvas.addMouseListener(new MouseAdapter() {//添加鼠标单击事件监听
            @Override
            public void mouseReleased(final MouseEvent arg0) {//当鼠标抬起
                x = -1;
                y = -1;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

            public void mousePressed(MouseEvent e){
                if(drawShape){
                    switch (shape.getType()){
                        case Shapes.YUAN:
                            int yuanX = e.getX() - shape.getWidth() / 2;
                            int yuanY = e.getY() - shape.getHeigth() / 2;

                            Ellipse2D yuan = new Ellipse2D.Double(yuanX,yuanY,shape.getWidth(),shape.getHeigth());
                            g.draw(yuan);
                            break;
                        case Shapes.FANG:
                            int fangX = e.getX() - shape.getWidth() / 2;
                            int fangY = e.getY() - shape.getHeigth() / 2;

                            Rectangle2D fang = new Rectangle2D.Double(fangY,fangY,shape.getWidth(),shape.getHeigth());

                            g.draw(fang);
                            break;
                    }
                    canvas.repaint();

                    drawShape = false;
                }
            }
        });
        strokeButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed( final ActionEvent arg0) {
                    BasicStroke bs = new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
                    g.setStroke(bs);
            }
        });
        strokeButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed( final ActionEvent arg0) {
                BasicStroke bs = new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
                g.setStroke(bs);
            }
        });
        strokeButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed( final ActionEvent arg0) {
                BasicStroke bs = new BasicStroke(4,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
                g.setStroke(bs);
            }
        });

        backgroundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                Color bgColor1 = JColorChooser.showDialog(DrawPictureFrame.this,"选择颜色",Color.CYAN);
                if(bgColor1 != null){
                    bgColor = bgColor1;
                }
                backgroundButton.setBackground(bgColor1);
                g.setColor(bgColor1);
                g.fillRect(0,0,570,390);
                g.setColor(forecolor);
                canvas.repaint();
            }
        });
        foregroundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                Color fColor = JColorChooser.showDialog(DrawPictureFrame.this,"选择颜色",Color.CYAN);
                if(fColor != null){
                    forecolor = fColor;
                }
                foregroundButton.setBackground(fColor);
                g.setColor(fColor);
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                g.setColor(bgColor);
                g.fillRect(0,0,570,390);
                g.setColor(forecolor);
                canvas.repaint();
            }
        });
        eraserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(eraserButton.getText().equals("橡皮")){
                    rubber = true;
                    eraserButton.setText("画图");
                }
                else{
                    rubber = false;
                    eraserButton.setText("橡皮");
                    g.setColor(forecolor);
                }
            }
        });
        shapeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShapeWindow shapewindow = new ShapeWindow(DrawPictureFrame.this);
                int shapeButtonWidth = shapeButton.getWidth();
                int shapeWindowWidth = shapewindow.getWidth();
                int shapeButtonHeight = shapeButton.getHeight();
                int shapeButtonX = shapeButton.getX();
                int shapeButtonY = shapeButton.getY();

                int shapeWindowX = getX() + shapeButtonX - (shapeWindowWidth - shapeButtonWidth) / 2;
                int shapeWindowY = getY() + shapeButtonY + 80;

                shapewindow.setLocation(shapeWindowX,shapeWindowY);

                shapewindow.setVisible(true);

            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addWatermark();
                DrawImageUtil.saveImage(DrawPictureFrame.this,image);
            }
        });
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        eraserMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(eraserMenuItem.getText().equals("橡皮")){
                    rubber = true;
                    eraserMenuItem.setText("画图");
                    eraserButton.setText("画图");
                }
                else{
                    rubber = false;
                    eraserMenuItem.setText("橡皮");
                    eraserButton.setText("橡皮");
                    g.setColor(forecolor);
                }
            }
        });
        clearMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                g.setColor(bgColor);
                g.fillRect(0,0,570,390);
                g.setColor(forecolor);
                canvas.repaint();
            }
        });
        strokeMenuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BasicStroke bs = new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
                g.setStroke(bs);
                strokeButton1.setSelected(true);
            }
        });
        strokeMenuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BasicStroke bs = new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
                g.setStroke(bs);
                strokeButton2.setSelected(true);
            }
        });
        strokeMenuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BasicStroke bs = new BasicStroke(4,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
                g.setStroke(bs);
                strokeButton3.setSelected(true);
            }
        });

        foregroundMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color fColor = JColorChooser.showDialog(DrawPictureFrame.this,"选择颜色对话框",Color.CYAN);
                if(fColor != null){
                    forecolor = fColor;
                }
                foregroundButton.setForeground(forecolor);
                g.setColor(forecolor);
            }
        });
        backgroundMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color bgColor1 = JColorChooser.showDialog(DrawPictureFrame.this,"选择颜色对话框",Color.CYAN);
                if(bgColor1 != null){
                    bgColor = bgColor1;
                }
                backgroundButton.setBackground(bgColor);
                g.setColor(bgColor);
                g.fillRect(0,0,570,390);
                g.setColor(forecolor);
                canvas.repaint();
            }
        });
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addWatermark();
                DrawImageUtil.saveImage(DrawPictureFrame.this,image);
            }
        });
        shuiyinMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shuiyin = JOptionPane.showInputDialog(DrawPictureFrame.this,"自定义水印");
                if(null == shuiyin){
                    shuiyin = "";
                }
                else{
                    setTitle("画板("+shuiyin+")");
                }
            }
        });

        showPicBUtton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isVisible = picWindow.isVisible();
                if(isVisible){
                    showPicBUtton.setText("展开简笔画");
                    picWindow.setVisible(false);
                }
                else{
                    showPicBUtton.setText("隐藏简笔画");
                    picWindow.setLocation(getX() - picWindow.getWidth() - 5,getY());
                    picWindow.setVisible(true);
                }
            }
        });
    }
    public void initShowPicButton(){
        showPicBUtton.setText("展开简笔画");
    }
    private void addWatermark(){
        if(!"".equals(shuiyin.trim())){//如果水印不是空串
            g.rotate(Math.toRadians(-30));//旋转-30度
            Font font = new Font("楷体",Font.BOLD,72);
            g.setFont(font);
            g.setColor(Color.GRAY);
            AlphaComposite alpha = AlphaComposite.SrcOver.derive(0.4f);
            g.setComposite(alpha);
            g.drawString(shuiyin,150,500);
            canvas.repaint();
            g.rotate(Math.toRadians(30));
            alpha = AlphaComposite.SrcOver.derive(1f);
            g.setComposite(alpha);
            g.setColor(forecolor);
        }
    }
    private void init(){
        g.setColor(bgColor);
        g.fillRect(0,0,570,390);
        g.setColor(forecolor);
        canvas.setImage(image);
        getContentPane().add(canvas);//将画布添加到窗体容器默认布局的中部位置
        toolbar= new JToolBar();//初始化工具栏

        showPicBUtton = new JButton("展开简笔画");
        toolbar.add(showPicBUtton);

        getContentPane().add(toolbar,BorderLayout.NORTH);
        saveButton = new JButton("保存");
        toolbar.add(saveButton);
        toolbar.addSeparator();

        strokeButton1 = new JToggleButton("细线");
        strokeButton1.setSelected(true);
        toolbar.add(strokeButton1);
        strokeButton2 = new JToggleButton("粗线");
        toolbar.add(strokeButton2);
        strokeButton3 = new JToggleButton("较粗");
        ButtonGroup strokeGroup = new ButtonGroup();
        strokeGroup.add(strokeButton1);

        strokeGroup.add(strokeButton2);
        strokeGroup.add(strokeButton3);
        toolbar.add(strokeButton3);
        toolbar.addSeparator();
        backgroundButton = new JButton("背景颜色");
        toolbar.add(backgroundButton);
        foregroundButton = new JButton("前景颜色");
        toolbar.add(foregroundButton);
        toolbar.addSeparator();
        shapeButton = new JButton("图形");
        toolbar.add(shapeButton);

        clearButton = new JButton("清除");
        toolbar.add(clearButton);
        eraserButton = new JButton("橡皮");
        toolbar.add(eraserButton);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu systemMenu = new JMenu("系统");
        menuBar.add(systemMenu);
        saveMenuItem = new JMenuItem("保存");
        systemMenu.add(saveMenuItem);
        systemMenu.addSeparator();
        shuiyinMenuItem = new JMenuItem("设置水印");
        systemMenu.add(shuiyinMenuItem);
        systemMenu.addSeparator();
        exitMenuItem = new JMenuItem("退出");
        systemMenu.add(exitMenuItem);


        JMenu strokeMenu = new JMenu("线性");
        menuBar.add(strokeMenu);
        strokeMenuItem1 = new JMenuItem("细线");
        strokeMenu.add(strokeMenuItem1);
        strokeMenuItem2 = new JMenuItem("粗线");
        strokeMenu.add(strokeMenuItem2);
        strokeMenuItem3 = new JMenuItem("较粗");
        strokeMenu.add(strokeMenuItem3);

        JMenu colorMenu = new JMenu("颜色");
        menuBar.add(colorMenu);
        foregroundMenuItem = new JMenuItem("前景颜色");
        colorMenu.add(foregroundMenuItem);
        backgroundMenuItem = new JMenuItem("背景颜色");
        colorMenu.add(backgroundMenuItem);

        JMenu editMenu = new JMenu("编辑");
        menuBar.add(editMenu);
        clearMenuItem = new JMenuItem("清除");
        editMenu.add(clearMenuItem);
        eraserMenuItem = new JMenuItem("橡皮");
        editMenu.add(eraserMenuItem);

        picWindow = new PictureWindow(DrawPictureFrame.this);
    }

    public void getShape(Shapes shape){
        this.shape = shape;
        drawShape = true;
    }

    /**
     *
     * @param args 运行参数时，此程序用不到
     */
    public static void main(String[] args){

        DrawPictureFrame Frame = new DrawPictureFrame();
        Frame.setVisible(true);//可见
    }
}
