package draw_panel;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;

/**
 * by gilded-sky
 */
public class DrawPictureCanvas extends Canvas{
    private Image image = null;
    /**
     * 设置画板中图片
     *
     * @param -画板中展示图片对象
     */
    public void setImage(Image image){
        this.image = image;
    }

    /**
     * 重写paint方法
     */
    public void paint(Graphics g){
        g.drawImage(image,0,0,null);
    }

    /**
     * 重写update方法
     */
    public void update(Graphics g){
        paint(g);
    }
}
