package lawnlayer.grid;

import processing.core.PApplet;

public class path extends grid {
    public path(int x, int y, String spritePath){
        super(x, y, spritePath);
    }

    public void draw(PApplet app){
        app.image(this.sprite, this.x+2, this.y+2);
    }
}
