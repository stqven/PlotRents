package com.nthByte.Exception;

public class WandNotSetException extends Exception {

    private int num;

    public WandNotSetException(int num) {
        super();
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
