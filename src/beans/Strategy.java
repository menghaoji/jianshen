/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;
/**
 *
 * @author menghaoji
 */
public class Strategy {

    private String startTime;
    private String bidTime;
    private String latestTime;
    private String addPrice;
    private int bidTimeGap;
    private int latestTimeGap;
    private int addPriceInt;
    
    public int getAddPriceInt(){
        return this.addPriceInt;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getBidTime() {
        return bidTime;
    }

    public void setBidTime(String bidTime) {
        this.bidTime = bidTime;
    }

    public String getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(String latestTime) {
        this.latestTime = latestTime;
    }

    public String getAddPrice() {
        return addPrice;
    }

    public void setAddPrice(String addPrice) {
        this.addPrice = addPrice;
        this.addPriceInt = Integer.valueOf(addPrice);
    }

    public int getBidTimeGap() {
        return bidTimeGap;
    }

    public void setBidTimeGap(int bidTimeGap) {
        this.bidTimeGap = bidTimeGap;
    }

    public int getLatestTimeGap() {
        return latestTimeGap;
    }

    public void setLatestTimeGap(int latestTimeGap) {
        this.latestTimeGap = latestTimeGap;
    }

}
