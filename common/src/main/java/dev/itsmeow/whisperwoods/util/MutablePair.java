package dev.itsmeow.whisperwoods.util;

public class MutablePair<A, B> {
    private A a;
    private B b;

    public MutablePair(A aIn, B bIn) {
        this.a = aIn;
        this.b = bIn;
    }

    public A getLeft() {
        return this.a;
    }

    public B getRight() {
        return this.b;
    }

    public void setLeft(A aIn) {
        this.a = aIn;
    }

    public void setRight(B bIn) {
        this.b = bIn;
    }
}
