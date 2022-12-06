package lawnlayer.gridObject;

import processing.core.PImage;
import lawnlayer.App;
import processing.core.PApplet;

class position {
    public int x;
    public int y;
    public int top;
    public int bottom;
    public int left;
    public int right;

    public void syncLoc(){
        this.top = this.y;
        this.bottom = this.y + App.SPRITESIZE;
        this.left = this.x;
        this.right = this.x + App.SPRITESIZE;
    }

    public position(int x, int y){
        this.x = x;
        this.y = y;
        this.syncLoc();
    }
}

public class gridObject extends position {

    public PImage sprite;
    public String spritePath;

    public gridObject(int x, int y, String spritePath){
        super(x, y);
        this.spritePath = spritePath;
    }

    public void setSprite(App app){
        this.sprite = app.getImageFromPath(this.spritePath);
    }

    public void move(int horizon, int Portrait){
        this.x  += horizon;
        this.y += Portrait;
        this.syncLoc();
    }

    public void draw(PApplet app){
        app.image(this.sprite, this.x, this.y);
    }
}
