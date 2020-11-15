package dev.itsmeow.whisperwoods.util;

public enum WispColors {

    BLUE(0x00efef),
    ORANGE(0xf28900),
    YELLOW(0xffc61c),    
    PURPLE(0xca27ea),
    GREEN(0x2bff39);

    private final int color;

    private WispColors(int color) {
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }
}
