package advent.day19.game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GameIcon extends ImageIcon{
    //
    private boolean loaded = true;
    private final String iconPath;
    private float iconRatio;
    private float widthDistribution;
    private final float heightDistribution;
    private final Component component;

    public GameIcon(String resourcePath, float widthDistribution, float heightDistribution, Component component){
        super();
        iconPath = resourcePath;
        if(widthDistribution > 1 || widthDistribution < 0){
            throw new IllegalArgumentException("Distribution cannot be negative or larger than 1, given: " + widthDistribution);
        }
        if(heightDistribution > 1 || heightDistribution < 0){
            throw new IllegalArgumentException("Distribution cannot be negative or larger than 1, given: " + heightDistribution);
        }
        this.widthDistribution = widthDistribution;
        this.heightDistribution = heightDistribution;
        this.component = component;
        initialize();
    }
    private void initialize(){
        ImageIcon icon = loadIcon();
        if(!loaded){
            System.err.println("Image at " + iconPath + " wasn't loaded properly");
            return;
        }
        setImage(icon.getImage());
        iconRatio = this.getIconWidth()/(float)this.getIconHeight();
    }
    private ImageIcon loadIcon(){
        BufferedImage img;
        try {
            InputStream iconStream = getClass().getResourceAsStream(iconPath);
            if(iconStream == null){
                System.err.println("Icon at " + iconPath + " does not exist");
                loaded = false;
                return null;
            }
            img = ImageIO.read(iconStream);
        } catch (IOException e) {
            System.err.println("Icon at " + iconPath + " could not be loaded");
            loaded = false;
            return null;
        }
        if(img == null){
            loaded = false;
            return null;
        }
        return new ImageIcon(img);
    }
    public ImageIcon scale(int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(this.getImage(), 0, 0, w, h, null);
        g2.dispose();

        return new ImageIcon(resizedImg);
    }
    public ImageIcon scale(){
        if(!loaded){
            return null;
        }
        //maintain ratio
        int scaledWidth = (int) (component.getWidth() * widthDistribution);
        int scaledHeight = (int) (component.getWidth() * heightDistribution);
        if(scaledWidth/(double)scaledHeight > iconRatio){
            scaledWidth = (int) (scaledHeight * iconRatio);
        }else{
            scaledHeight = (int)(scaledWidth / iconRatio);
        }
        return scale(scaledWidth, scaledHeight);
    }

    public boolean isLoaded(){
        return loaded;
    }

    public String getIconPath(){
        return iconPath;
    }

    public void setWidthDistribution(float widthDistribution){
        this.widthDistribution = widthDistribution;
    }

    @Override
    public String toString(){
        return "GameIcon{" +
                ", loaded=" + loaded +
                ", size=" + new Dimension(this.getIconWidth(), this.getIconHeight()) +
                ", path=" + iconPath +
                ", ratio=" + iconRatio +
                '}';
    }
}
