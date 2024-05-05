package me.theentropyshard.isoblock.render;

public class RenderOptions {
    private int scale = 1;
    private boolean noShadow;

    public RenderOptions() {

    }

    public int getScale() {
        return this.scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public boolean isNoShadow() {
        return this.noShadow;
    }

    public void setNoShadow(boolean noShadow) {
        this.noShadow = noShadow;
    }
}
