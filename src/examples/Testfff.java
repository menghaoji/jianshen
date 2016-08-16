/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

/**
 *
 * @author menghaoji
 */
public class Testfff {
    public static void main(String[] args) throws AWTException, InterruptedException{
        Thread.sleep(10000);
                Robot robot = new Robot();
                robot.mouseMove(1071, 448);
                robot.delay(100);
                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.delay(15);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
    
}
