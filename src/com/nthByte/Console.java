package com.nthByte;

public class Console {
    private String msg;

    public Console(String msg) {
        this.msg = "[PlotRents] " + msg;
    }

    public void sendInfo() {
        PlotRentsPlugin.instance.getLogger().info(msg);
    }

    public void sendWarning() {
        PlotRentsPlugin.instance.getLogger().warning(msg);
    }

    public void sendFine() {
        PlotRentsPlugin.instance.getLogger().fine(msg);
    }
}
