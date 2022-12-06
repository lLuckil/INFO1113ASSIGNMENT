package lawnlayer.enemy;

import lawnlayer.App;
import lawnlayer.grid.gridCement;
import lawnlayer.grid.gridGrass;
import lawnlayer.grid.pathRed;
import lawnlayer.grid.grid;
import lawnlayer.grid.gridMap;
import lawnlayer.gridObject.gridMove;
import lawnlayer.gridObject.player;
import lawnlayer.gridObject.gridObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

enum enemyDirection{
    TOPLEFT,
    TOPRIGHT,
    BOTTOMLEFT,
    BOTTOMRIGHT,
    STAYSTILL;
}


public class enemy extends gridMove {

    public enemyDirection enemDir;
    public int speed;
    private String spawn;
    private int interruptIdx;
    public static int NOTINTERRUPT = -1;


    public enemy(String spawn, String spritePath){
        super(0, 0, spritePath);
        this.speed = 2;
        this.spawn = spawn;
        this.setInitialPosition();
        this.setInitialDir();
        this.interruptIdx = NOTINTERRUPT;
    }


    public void setInitialPosition(){
        if (this.spawn.equals("random")){
            Random random = new Random();
            List<List<grid>> emptyMap = gridMap.correctMisplaceGroups();
            List<grid> totalEmptyMap = new ArrayList<>();
            for (List<grid> ls : emptyMap)
                totalEmptyMap.addAll(ls);
            int randidx = random.nextInt(totalEmptyMap.size());
            grid randomTile = totalEmptyMap.get(randidx);
            this.x = randomTile.x;
            this.y = randomTile.y;
        }
    }

    public void setInitialDir(){
        Random random = new Random();
        this.enemDir = enemyDirection.values()[random.nextInt(4)];
    }

    public void moveDiagnollay(){
        if (this.enemDir == enemyDirection.TOPLEFT)
            this.move(-this.speed, -this.speed);
        else if (this.enemDir == enemyDirection.TOPRIGHT)
            this.move(this.speed, -this.speed);
        else if (this.enemDir == enemyDirection.BOTTOMLEFT)
            this.move(-this.speed, this.speed);
        else if (this.enemDir == enemyDirection.BOTTOMRIGHT)
            this.move(this.speed, this.speed);
        else if (this.enemDir == enemyDirection.STAYSTILL)
            this.move(0, 0);
    }

    public void checkTile(){
        if (this.enemDir == enemyDirection.TOPLEFT)
            this.onWhichTile(this.left, this.top);
        else if (this.enemDir == enemyDirection.TOPRIGHT)
            this.onWhichTile(this.right, this.top);
        else if (this.enemDir == enemyDirection.BOTTOMLEFT)
            this.onWhichTile(this.left, this.bottom);
        else if (this.enemDir == enemyDirection.BOTTOMRIGHT)
            this.onWhichTile(this.right, this.bottom);
    }

    public void bounceOff(){
        if (this.isSafeGrid(this.onTile) == false){
            return;
        }
        if (this.enemDir == enemyDirection.TOPLEFT){
            this.onWhichTile(this.right, this.top);
            if (isSafeGrid(this.onTile))
                this.enemDir = enemyDirection.BOTTOMLEFT;
            else
                this.enemDir = enemyDirection.TOPRIGHT;
        } else if (this.enemDir == enemyDirection.TOPRIGHT){
            this.onWhichTile(this.left, this.top);
            if (isSafeGrid(this.onTile))
                this.enemDir = enemyDirection.BOTTOMRIGHT;
            else
                this.enemDir = enemyDirection.TOPLEFT;
        }
        else if (this.enemDir == enemyDirection.BOTTOMLEFT){
            this.onWhichTile(this.right, this.bottom);
            if (isSafeGrid(this.onTile))
                this.enemDir = enemyDirection.TOPLEFT;
            else
                this.enemDir = enemyDirection.BOTTOMRIGHT;
        }
        else if (this.enemDir == enemyDirection.BOTTOMRIGHT){
            this.onWhichTile(this.left, this.bottom);
            if (isSafeGrid(this.onTile))
                this.enemDir = enemyDirection.TOPRIGHT;
            else
                this.enemDir = enemyDirection.BOTTOMLEFT;
        } else{
            this.enemDir = enemyDirection.STAYSTILL;
        }
    }
    public boolean exist(char[][] board, String word) {
        char[] words = word.toCharArray();
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board[0].length; j++){
                if(dfs(board, words, i, j, 0)) return true;
            }
        }
        return false;
    }
    boolean dfs(char board[][],char word[],int i,int j,int k){
        if(i>=board.length || i<0 || j>=board[0].length || j<0 ||  board[i][j]!=word[k]) return false;
        if(k==word.length-1) return true;
        board[i][j] = '\0';
        boolean res = dfs(board, word, i+1, j, k+1) || dfs(board, word, i-1, j, k+1)|| dfs(board, word, i, j+1, k+1) || dfs(board, word, i, j-1, k+1) ;
        board[i][j] = word[k];
        return res;
    }


    public boolean isdirecMap(String s, String p) {
        int n = s.length();
        int m = p.length();
        boolean[][] f = new boolean[n + 1][m + 1];
        for(int i = 0;i <= n; i++){
            for(int j = 0; j <= m; j++){
                if(j == 0){
                    f[i][j] = i == 0;
                }else{
                    if(p.charAt(j - 1) != '*'){
                        if(i > 0 && (s.charAt(i - 1) == p.charAt(j - 1) || p.charAt(j - 1) == '.')){
                            f[i][j] = f[i-1][j-1];
                        }
                    }else{

                        if(j >= 2){
                            f[i][j] |= f[i][j-2];
                        }
                        if(i >= 1 && j >= 2 && (s.charAt(i - 1) == p.charAt(j - 2)|| p.charAt(j-2) == '.')){
                            f[i][j] |= f[i - 1][j];
                        }
                    }
                }
            }
        }
        return f[n][m];
    }
    public void interruptPath(List<Integer[]> playerPath, player p){
        int flag = 0;
        if (p.isSafeGrid(p.onTile)){
            this.interruptIdx = NOTINTERRUPT;
        }
        if (this.interruptIdx > NOTINTERRUPT)
            return;
        int enemX = gridMap.CoordXToTile(this.onTile);
        int enemY = gridMap.CoordYToTile(this.onTile);

        for (int i=0; i<playerPath.size(); i++){
            int x = playerPath.get(i)[1];
            int y = playerPath.get(i)[0];
            if (x == enemX && y == enemY){
                this.interruptIdx = i;
                System.out.println("interrupted!");
                return;  //--lives
            }
        }
    }

    public boolean destroyPath(List<Integer[]> pathIdx, List<grid> path, App app, player p){
        if (this.interruptIdx <= NOTINTERRUPT)
            return false;
        if (app.frameCount % 3 != 0)
            return false;
        try{
            if (this.interruptIdx >= pathIdx.size() -1){
                p.died(app);
                this.interruptIdx = NOTINTERRUPT;
                return true;
            }
            if (path.get(this.interruptIdx) instanceof gridCement){
                throw new IndexOutOfBoundsException();
            } else if (path.get(this.interruptIdx) instanceof gridGrass)
                throw new IndexOutOfBoundsException();

            Integer[] destoryPathIdx = pathIdx.get(this.interruptIdx);

            int x = destoryPathIdx[1];
            int y = destoryPathIdx[0];
            
            grid t =  new pathRed(gridMap.TileToCoordX(x), gridMap.TileToCoordY(y));
            t.setSprite(app);
            gridMap.LvlMap[y][x] = t;
            this.interruptIdx += 1;
        } catch(IndexOutOfBoundsException e){
            System.out.println("player already reached safe tile! dang it :D");
            this.interruptIdx = NOTINTERRUPT;
            e.printStackTrace();
            p.resetPlayerPath();
        } 
        return false;
        
    }


    public grid getOnTile(){
        return this.onTile;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }

    public void EnemyMove(){
        this.moveDiagnollay();
        this.checkTile();
        this.bounceOff();
    }

    public boolean checkEnemyHitPlayerPath(player p, App app){
        this.interruptPath(p.getPlayerPathIdx(), p);
        boolean flag = this.destroyPath(p.getPlayerPathIdx(), p.getPlayerPath(), app, p);
        return flag;
    }

}
