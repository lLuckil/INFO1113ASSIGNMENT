package lawnlayer.grid;

import lawnlayer.App;
import processing.core.PApplet;
import lawnlayer.enemy.enemy;
import lawnlayer.gridObject.player;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class gridMap {

    public static grid[][] LvlMap;
    public static final int WIDTH = 64;
    public static final int HEIGHT = 32;
    public static final String CEMENT = "X";
    public static final String GRASS = " ";
    
    public static String[][] readMap(String path){
        // List<String> MapScan = new ArrayList<>(WIDTH*HEIGHT);
        String[][] MapScan = new String[HEIGHT][WIDTH];
        try{
            File f = new File(path);
            Scanner sc = new Scanner(f);
            sc.useDelimiter("");
            
            int i = 0;
            while (sc.hasNext()){
                String nextstr = sc.next();
                if (nextstr.equals(" ") || nextstr.equals("X")){
                    MapScan[i/WIDTH][i%WIDTH] = nextstr;
                    i += 1;
                }
            }
            
            sc.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
        System.out.println(MapScan.length * MapScan[31].length);
        return MapScan;
     }

     public static int TileToCoordX(int x){
        return x * App.SPRITESIZE;
     }
     public static int TileToCoordY(int y){
         return y * App.SPRITESIZE + App.TOPBAR;
     }
     public static int CoordXToTile(grid tile){
         return tile.getX() / App.SPRITESIZE;
     }
     public static int CoordYToTile(grid tile){
         return (tile.getY() - App.TOPBAR) / App.SPRITESIZE;
     }

    public static void InitialiseMap(String mapFilePath, App app){
        String[][] currentMap = readMap(mapFilePath);
        LvlMap = new grid[HEIGHT][WIDTH];

        for (int i=0; i<HEIGHT; i++){
            for (int j=0; j<WIDTH; j++){
                String currentTile = currentMap[i][j];
                grid t;
                if (currentTile.equals(CEMENT))
                    t = new gridCement(TileToCoordX(j), TileToCoordY(i));
                else
                    t = new gridDirt(TileToCoordX(j), TileToCoordY(i));
                t.setSprite(app);
                LvlMap[i][j] = t;
            }
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
    public static void printMap(PApplet app){
        for (grid[] tileROw : LvlMap){
            for (grid tiles : tileROw){
                tiles.draw(app);
            }
        }
    }

    public static void setPath(int[] tileIdx, App app){
        int y = tileIdx[0];
        int x = tileIdx[1];
        grid playerTile = LvlMap[y][x];
        if (playerTile instanceof gridDirt){
            grid t = new greenPath(TileToCoordX(x), TileToCoordY(y));
            t.setSprite(app);
            LvlMap[y][x] = t;
        }
    }

    public static int PathFill(List<Integer[]> pathidx, App app){
//        System.out.println("size:"+pathidx.size());
        for (int i=1; i<pathidx.size()-1; i++){
            int x = pathidx.get(i)[1];
            int y = pathidx.get(i)[0];
//            System.out.println("x:"+x+",y:"+y);
            grid t = new gridGrass(TileToCoordX(x), TileToCoordY(y));
            t.setSprite(app);
            LvlMap[y][x] = t;
        }
        return pathidx.size();
    }
    public static void PathRemove(List<Integer[]> pathidx, App app){
        for (int i=1; i<pathidx.size(); i++){
            int x = pathidx.get(i)[1];
            int y = pathidx.get(i)[0];

            grid t = new gridDirt(TileToCoordX(x), TileToCoordY(y));
            t.setSprite(app);
            LvlMap[y][x] = t;
        }
    }

    public static List<grid> makeNewGroup(grid t){
        List<grid> newGroup =  new ArrayList<>();
        newGroup.add(t);
        return newGroup;
    }

    public static List<List<grid>> groupTiles(){
        List<List<grid>> Groups = new ArrayList<>();
        for (int i=0; i<HEIGHT; i++){
            for (int j=0; j<WIDTH; j++){
                grid t = LvlMap[i][j];
                if (t instanceof gridDirt == false)
                    continue;
                if (Groups.size() == 0)
                    Groups.add(makeNewGroup(t));
                else{
                    boolean createNew = true;
                    for (List<grid> group : Groups){
                        if (belongToGroup(group, t)){
                            group.add(t);
                            createNew = false;
                            break;
                        }
                    }
                    if (createNew)
                        Groups.add(makeNewGroup(t));
                }
            }
        }
        return Groups;
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
    public static boolean belongToGroup(List<grid> lsTile, grid tile){
        for (grid t: lsTile){
            if (adjacentTiles(t, tile))
                return true;
        }
        return false;
    }


    public static boolean adjacentTiles(grid control, grid subject){
        int xDiff = Math.abs(control.getX() - subject.getX());
        int yDiff = Math.abs(control.getY() - subject.getY());
        if (xDiff + yDiff == 20){
            return true;
        }
        return false;
    }

    public static boolean needToMerge(List<grid> a, List<grid> b){
        for (grid tb : b){
            if (belongToGroup(a, tb))
                return true;
        }
        return false;
    }

    public static List<List<grid>> correctMisplaceGroups(){
        List<List<grid>> Groups = groupTiles();

        int idx = 0;
        while (true){
            try{
            List<Integer> remove = new ArrayList<>();
            for (int i=idx+1; i<Groups.size(); i++){
                if (needToMerge(Groups.get(idx), Groups.get(i))){
                    Groups.get(idx).addAll(Groups.get(i));
                    remove.add(i);
                }
            }
            
            for (int i=0; i<remove.size(); i++){
                int r = remove.get(i) -i;
                Groups.remove(Groups.get(r));
            }

            idx += 1;
            if (idx+1 > Groups.size())
                break;
        } catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }
        return Groups;
    }

    public static boolean groupHasEnemy(List<grid> group, List<enemy> enemyList){
        for (grid tile : group){
            for (enemy enemy : enemyList){
                int xDiff = Math.abs(tile.getX() - enemy.getX());
                int yDiff = Math.abs(tile.getY() - enemy.getY());
                if (xDiff + yDiff <= App.SPRITESIZE)
                    return true;
            }
        }
        return false;
    }


    public static void colorMap(List<grid> group, App app){
        for (grid tile : group){
            grid t = new gridGrass(tile.getX(), tile.getY());
            t.setSprite(app);
            LvlMap[CoordYToTile(tile)][CoordXToTile(tile)] = t;
        }
    }

    public static void updateMap(player p, App app, List<enemy> enemyList){
        setPath(p.getTileIdx(), app);
        if (p.getCheckPath()){
            List<List<grid>> Groups = correctMisplaceGroups();
            for (List<grid> group : Groups){
                if (groupHasEnemy(group, enemyList) == false){
                    colorMap(group, app);
                }
            }
        }
    }

}
