package com.shuangsetoolsserver.meta;

public class CodeItem {
    public int red[] = new int[6];
    public int blue;
    public int id;
    
    public CodeItem(int _id, int r1, int r2, int r3, int r4,
                    int r5, int r6, int b) {
        id = _id;
        
        red[0] = r1;
        red[1] = r2;
        red[2] = r3;
        red[3] = r4;
        red[4] = r5;
        red[5] = r6;
        
        blue = b;        
    }
    
    public CodeItem(int _id, int r[], int b) {
        id = _id;
        
        System.arraycopy(r, 0, red, 0, 6);
        
        blue = b;
        
    }
}
