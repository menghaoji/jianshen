/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;
import examples.ContactEditor;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import readers.BaseImageReader;
import readers.LowestPriceImageReader;
/**
 *
 * @author menghaoji
 */
public class BitIt {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(BitIt.class);
    private ContactEditor contactEditor;
    //图片位置信息和按钮位置信息
    private LoadInfo loadInfo;
    //策略信息，比如什么时候出价
    private Strategy stra;
    //是否是真实操作
    private boolean isReal;
    //你的出价
    private int yourBidPrice;
    //最近一次读取到的最低价
    private int currentLowestPrice;
    //0 初始
    //1 出价按下
    //2 确认按下
    private int status = 0;
    //按下出价的动作需要定时触发
    private TimerTask clickBidTask;
    //验证码输入完以后的确认按钮需要定时触发
    private TimerTask clickSubTask;
    private boolean isStopped;
    
    public void setStopped(boolean stopped){
        this.isStopped = stopped;
    }

    public BitIt(ContactEditor contactEditor,LoadInfo loadInfo, Strategy stra, boolean isReal) {
        this.loadInfo = loadInfo;
        this.isReal = isReal;
        this.stra = stra;
        this.contactEditor = contactEditor;
        initScheduleTasks();
    }
    public void doIt() {
        while (!isStopped) {
            //循环读取最低价
            try {
                logger.info("start reg......");
                BaseImageReader imageReader = new LowestPriceImageReader();
                String result = imageReader.getAllOcr(loadInfo.getLowest(false));
                logger.info("end reg......"+result);
                currentLowestPrice = Integer.valueOf(result);
                //this.jLabel2.setText(result);
                //显示在日志看板
                //
                //logger.info("stop reg....");
                //时间
                contactEditor.getjLabel12().setText(new Date().toLocaleString());
                contactEditor.getjLabel14().setText(result);
            } catch (Exception ex) {
                Logger.getLogger(ContactEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    //根据设置的策略来定时任务
    private void initScheduleTasks() {
        Timer t = new Timer();
        //isReal = false的时候说明是测试,最后的确认改为鼠标右击
        clickBidTask = new TimerTask() {
            public void run() {
                System.out.println("clickBidTask");
                clickButton(3);
                calculateYourBidPrice();
                clickButton(5);
                
            }
            private void clickButton(int type) {
                try {
                    if (type == 3) {//点击加价按钮
                        Robot robot = new Robot();
                        robot.mouseMove(loadInfo.getAddButtonX(), loadInfo.getAddButtonY());
                        robot.delay(30);
                        robot.mousePress(InputEvent.BUTTON1_MASK);
                        robot.delay(15);
                        robot.mouseRelease(InputEvent.BUTTON1_MASK);
                    } else {
                        //点击出价按钮
                        Robot robot = new Robot();
                        robot.mouseMove(loadInfo.getBidButtonX(), loadInfo.getBidButtonY());
                        robot.delay(30);
                        robot.mousePress(InputEvent.BUTTON1_MASK);
                        robot.delay(15);
                        robot.mouseRelease(InputEvent.BUTTON1_MASK);

                    } 

                } catch (AWTException ex) {
                    Logger.getLogger(BitIt.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            private void calculateYourBidPrice() {
                int lowestPrice = currentLowestPrice;
                yourBidPrice = lowestPrice + stra.getAddPriceInt();
                contactEditor.getjLabel26().setText(String.valueOf(lowestPrice));
                contactEditor.getjLabel15().setText(String.valueOf(yourBidPrice));
            }
        };

        clickSubTask = new TimerTask() {
            public void run() {
                System.out.println("clickSubTask");
                try {
                    if (isReal) {
                        Robot robot = new Robot();
                        robot.mouseMove(loadInfo.getSubButtonX(), loadInfo.getSubButtonY());
                        robot.delay(15);
                        robot.mousePress(InputEvent.BUTTON1_MASK);
                        robot.delay(15);
                        robot.mouseRelease(InputEvent.BUTTON1_MASK);
                    } else {
                        //测试的时候最后的提交按钮是右击
                        Robot robot = new Robot();
                        robot.mouseMove(loadInfo.getSubButtonX(), loadInfo.getSubButtonY());
                        robot.delay(30);
                        robot.mousePress(InputEvent.BUTTON3_MASK);
                        robot.delay(15);
                        robot.mouseRelease(InputEvent.BUTTON3_MASK);
                    }
                } catch (AWTException ex) {
                    Logger.getLogger(BitIt.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        t.schedule(clickBidTask, this.stra.getBidTimeGap());
        t.schedule(clickSubTask, this.stra.getLatestTimeGap());

    }
}
