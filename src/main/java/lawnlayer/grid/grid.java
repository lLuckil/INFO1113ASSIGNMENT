package lawnlayer.grid;

import lawnlayer.gridObject.gridObject;

public class grid extends gridObject {

    public grid(int x, int y, String spritePath){
        super(x, y, spritePath);
    }
    
     public int getX(){
         return this.x;
     }
     public int getY(){
         return this.y;
     }

}
