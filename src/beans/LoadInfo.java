/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import examples.ContactEditor;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author menghaoji
 */
public class LoadInfo implements Serializable {
    //最低价
    //private BufferedImage lowest;
    //出价
    //private BufferedImage yourBid;

    //最低价坐标位置
    private int lowestStartX;
    private int lowestStartY;
    private int lowestWidth;
    private int lowestHeight;
    
    public int getLowestStartX(){
        return this.lowestStartX;
    }
    
    public int getLowestStartY(){
        return this.lowestStartY;
    }
    
    public int getLowestWidth(){
        return this.lowestWidth;
    }
    
    public int getLowestHeight(){
        return this.lowestHeight;
    }

    //加价按钮中心点坐标
    private int addButtonX;
    private int addButtonY;
    
    public int getAddButtonX(){
        return this.addButtonX;
    }
    
    public int getAddButtonY(){
        return this.addButtonY;
    }

    transient private Robot ro1;
    transient private Toolkit tk1;
    transient private Dimension di1;
    transient private Rectangle rec1;


    //出价按钮中心点坐标
    private int bidButtonX;
    private int bidButtonY;

    public int getBidButtonX() {
        return bidButtonX;
    }

    public int getBidButtonY() {
        return bidButtonY;
    }

    //确认按钮中心点坐标
    private int subButtonX;
    private int subButtonY;

    public int getSubButtonX() {
        return subButtonX;
    }

    public int getSubButtonY() {
        return subButtonY;
    }

    //验证码框
    private int xxxxX;
    private int xxxxY;

    public int getXxxxX() {
        return xxxxX;
    }

    public int getXxxxY() {
        return xxxxY;
    }
    transient private ContactEditor contactEditor;
    public void setFrame(ContactEditor ce){
        this.contactEditor = ce;
    }
//    public LoadInfo() { 
//        
//
//    }

//    public BufferedImage getYourBid(){
//        if(yourBidStartX == 0){
//            return null;
//        }
//        try{
//            BufferedImage bi1=ro1.createScreenCapture(rec1);
//            
//            yourBid = bi1.getSubimage(yourBidStartX,yourBidStartY,yourBidWidth,yourBidHeight);
//         //contactEditor.setVisible(true);
//         contactEditor.getjLabel17().setText("");
//         contactEditor.getjLabel17().setIcon(new ImageIcon(yourBid));
//           return yourBid;
//        }catch(Exception e){
//            return null;
//        }
//        
//    }
    public BufferedImage getLowest(boolean isTest) {
        if (lowestStartX == 0) {
            return null;
        }

        BufferedImage bi1 = ro1.createScreenCapture(rec1);
        BufferedImage lowest = bi1.getSubimage(lowestStartX, lowestStartY, lowestWidth, lowestHeight);
        //contactEditor.setVisible(true);
        if (isTest) {
            //测试的情况下才去更新页面图片
            contactEditor.getjLabel7().setText("");
            contactEditor.getjLabel7().setIcon(new ImageIcon(lowest));
        }
        return lowest;

    }

//	public void setLowest(BufferedImage lowest) {
//            //  update(this.contactEditor.getjLabel7(),lowest);
//            this.lowest = lowest;
//            this.contactEditor.setVisible(true);
//            this.contactEditor.getjLabel7().setText("");
//            this.contactEditor.getjLabel7().setIcon(new ImageIcon(lowest));
//	}
    public void setLowest(BufferedImage bi, int x, int y, int width, int height) {
        BufferedImage lowest = bi.getSubimage(x, y, width, height);
        lowestStartX = x;
        lowestStartY = y;
        lowestWidth = width;
        lowestHeight = height;
        contactEditor.setVisible(true);
        contactEditor.getjLabel7().setText("");
        contactEditor.getjLabel7().setIcon(new ImageIcon(lowest));
        //System.out.println("x = "+lowestStartX+" y= "+lowestStartY);
        contactEditor.getjLabel29().setText(String.valueOf(lowestStartX));
        contactEditor.getjLabel30().setText(String.valueOf(lowestStartY));
        
        contactEditor.getjLabel31().setText(String.valueOf(lowestWidth));
        contactEditor.getjLabel32().setText(String.valueOf(lowestHeight));      
    }

//    public void setYourBid(BufferedImage bi, int x, int y, int width, int height) {
//         yourBid = bi.getSubimage(x,y,width,height);
//         yourBidStartX = x;
//         yourBidStartY = y;
//         yourBidWidth = width;
//         yourBidHeight = height;
//         contactEditor.setVisible(true);
//         contactEditor.getjLabel17().setText("");
//         contactEditor.getjLabel17().setIcon(new ImageIcon(yourBid));
//         //System.out.println("x = "+lowestStartX+" y= "+lowestStartY);
//    }
    public void setAddButton(int x, int y, int width, int height) {
        addButtonX = x + width / 2;
        addButtonY = y + height / 2;
        contactEditor.setVisible(true);
        contactEditor.getjLabel5().setText(String.valueOf(addButtonX));
        contactEditor.getjLabel19().setText(String.valueOf(addButtonY));
        //System.out.println(addButtonX + "  "+addButtonY);
    }

    public void setBidPriceButton(int x, int y, int width, int height) {
        bidButtonX = x + width / 2;
        bidButtonY = y + height / 2;
        contactEditor.setVisible(true);
        contactEditor.getjLabel16().setText(String.valueOf(bidButtonX));
        contactEditor.getjLabel20().setText(String.valueOf(bidButtonY));
        //System.out.println(addButtonX + "  "+addButtonY);
    }

    public void setSubButton(int x, int y, int width, int height) {
        subButtonX = x + width / 2;
        subButtonY = y + height / 2;
        contactEditor.setVisible(true);
        contactEditor.getjLabel18().setText(String.valueOf(subButtonX));
        contactEditor.getjLabel21().setText(String.valueOf(subButtonY));
    }

    public void setXxxx(int x, int y, int width, int height) {
        xxxxX = x + width / 2;
        xxxxY = y + height / 2;
        contactEditor.setVisible(true);
        contactEditor.getjLabel23().setText(String.valueOf(xxxxX));
        contactEditor.getjLabel25().setText(String.valueOf(xxxxY));
    }

    public boolean isLoaded() {
        if (lowestStartX == 0 || lowestStartY == 0 || lowestHeight == 0 || lowestWidth == 0
                || addButtonX == 0 || addButtonY == 0 || bidButtonX == 0
                || bidButtonY == 0 || subButtonX == 0 || subButtonY == 0
                || xxxxX == 0 || xxxxY == 0) {
            return false;
        }
        return true;
    }

    public void init() {
        try {
            ro1 = new Robot();
            tk1 = Toolkit.getDefaultToolkit();
            di1 = tk1.getScreenSize();
            rec1 = new Rectangle(0, 0, di1.width, di1.height);
        } catch (AWTException ex) {
            Logger.getLogger(LoadInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
