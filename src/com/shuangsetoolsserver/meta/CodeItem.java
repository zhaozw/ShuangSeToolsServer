package com.shuangsetoolsserver.meta;

public class CodeItem {
    private int id;
    private int red1;
    private int red2;
    private int red3;
    private int red4;
    private int red5;
    private int red6;
    private int blue;
    private String openDate;
    private int totalSale;
    private int poolTotal;
    private int firstPrizeCnt;
    private int firstPrizeValue;
    private int secondPrizeCnt;
    private int secondPrizeValue;
    
    public CodeItem(int _id, int r1, int r2, int r3, int r4,
                    int r5, int r6, int b) {
        setId(_id);
        
        red1 = r1;
        red2 = r2;
        red3 = r3;
        red4 = r4;
        red5 = r5;
        red6 = r6;
        
        setBlue(b);
    }
    
    public CodeItem(int _id, int r[], int b) {
        setId(_id);
        
        this.red1 = r[0];
        this.red2 = r[1];
        this.red3 = r[2];
        this.red4 = r[3];
        this.red5 = r[4];
        this.red6 = r[5];
        
        setBlue(b);
        
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public int getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(int totalSale) {
        this.totalSale = totalSale;
    }

    public int getPoolTotal() {
        return poolTotal;
    }

    public void setPoolTotal(int poolTotal) {
        this.poolTotal = poolTotal;
    }

    public int getFirstPrizeCnt() {
        return firstPrizeCnt;
    }

    public void setFirstPrizeCnt(int firstPrizeCnt) {
        this.firstPrizeCnt = firstPrizeCnt;
    }

    public int getFirstPrizeValue() {
        return firstPrizeValue;
    }

    public void setFirstPrizeValue(int firstPrizeValue) {
        this.firstPrizeValue = firstPrizeValue;
    }

    public int getSecondPrizeCnt() {
        return secondPrizeCnt;
    }

    public void setSecondPrizeCnt(int secondPrizeCnt) {
        this.secondPrizeCnt = secondPrizeCnt;
    }

    public int getSecondPrizeValue() {
        return secondPrizeValue;
    }

    public void setSecondPrizeValue(int secondPrizeValue) {
        this.secondPrizeValue = secondPrizeValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getRed(int index) {
        switch(index) {
        case 0:
            return red1;
        case 1:
            return red2;
        case 2:
            return red3;
        case 3:
            return red4;
        case 4:
            return red5;
        case 5:
            return red6;
        default:
            return 0;
        }
    }
    public int getRed1() {
        return red1;
    }

    public void setRed1(int red1) {
        this.red1 = red1;
    }

    public int getRed2() {
        return red2;
    }

    public void setRed2(int red2) {
        this.red2 = red2;
    }

    public int getRed3() {
        return red3;
    }

    public void setRed3(int red3) {
        this.red3 = red3;
    }

    public int getRed4() {
        return red4;
    }

    public void setRed4(int red4) {
        this.red4 = red4;
    }

    public int getRed5() {
        return red5;
    }

    public void setRed5(int red5) {
        this.red5 = red5;
    }

    public int getRed6() {
        return red6;
    }

    public void setRed6(int red6) {
        this.red6 = red6;
    }
}
