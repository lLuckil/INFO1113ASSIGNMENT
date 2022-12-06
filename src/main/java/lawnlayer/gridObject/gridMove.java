package lawnlayer.gridObject;

import lawnlayer.App;
import lawnlayer.grid.grid;
import lawnlayer.grid.gridCement;
import lawnlayer.grid.gridGrass;
import lawnlayer.grid.gridMap;

public class gridMove extends gridObject {

    public grid onTile;
    public int[] tileIdx;
    public boolean isSafeGrid(grid t){
        if (t instanceof gridCement || t instanceof gridGrass)
            return true;
        return false;
    }


    public void onWhichTile(){
        int left = this.left / App.SPRITESIZE;
        int top = (this.top - App.TOPBAR) / App.SPRITESIZE;

        if (this.onPixel()){
            this.onTile = gridMap.LvlMap[top][left];
            this.tileIdx[0] = top;
            this.tileIdx[1] = left;
        }
    }
    public void onWhichTile(int horizon, int Portrait){
        horizon = horizon / App.SPRITESIZE;
        Portrait = (Portrait - App.TOPBAR) / App.SPRITESIZE;

        this.onTile = gridMap.LvlMap[Portrait][horizon];
    }
    public gridMove(int x, int y, String spritePath){
        super(x, y, spritePath);
    }

    public boolean onPixel(){
        if (this.x % App.SPRITESIZE + this.y % App.SPRITESIZE == 0)
            return true;
        return false;
    }

}
