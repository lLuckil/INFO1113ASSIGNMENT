package lawnlayer;

import java.util.ArrayList;
import java.util.List;

import lawnlayer.grid.gridMap;
import lawnlayer.enemy.enemy;
import lawnlayer.enemy.worm;
import lawnlayer.gridObject.backGround;
import lawnlayer.gridObject.player;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class App extends PApplet {

    public JSONObject json;

    public String configPath;
    public int lives = 0;
    public int areas = 0;
    public JSONArray levels;
    public int cover = 0;
    private lawnlayer.gridObject.player player;
    private backGround background;
    private List<enemy> enemies;

    public PFont font1;
    public PFont font2;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int TOPBAR = 80;
    public static final int FPS = 60;

    public App() {
        this.configPath = "config.json";
    }

    public PImage getImageFromPath(String path){
        return loadImage(this.getClass().getResource(path).getPath());
    }

    public void settings() {
        size(WIDTH, HEIGHT);
    }

    public void setup() {
        frameRate(FPS);
        json = loadJSONObject(configPath);
        lives = json.getInt("lives");
        levels = json.getJSONArray("levels");

        font1 = createFont("ArialMT", 40);
        font2 = createFont("ArialMT", 20);
        this.player = new player();
        this.player.setSprite(this);

        this.background = new backGround();
        this.background.setSprite(this);

        gridMap.InitialiseMap("level1.txt", this);

        this.enemies = new ArrayList<>();
        this.enemies.add(new worm("random"));
        this.enemies.add(new worm("random"));
        for (enemy enem : this.enemies)
            enem.setSprite(this);
        System.out.println("area:"+this.areas);

    }
 
    public void draw() {
        this.background.draw(this);
        textFont(font1);
        text("Lives: " + lives, 200, 40);
        text(cover+"%"+"/80%",1000,40);
        textFont(font2);
        text("Level1",1150,70);
        this.player.playerMove();
        this.areas += this.player.setPath(this);
//        System.out.println("are:"+this.areas);
        this.cover = areas*100 / (40*22);
       // System.out.println(this.lives);
        if (this.player.stepPathAgain()){
            this.player.died(this);

        }



        for (enemy en : this.enemies){
            en.EnemyMove();
            boolean flag = en.checkEnemyHitPlayerPath(this.player, this);
            if(flag) {
                this.lives--;
//                System.out.println("*******n"+this.lives);
            }

        }

        gridMap.updateMap(this.player, this, this.enemies);

        gridMap.printMap(this);
        this.player.draw(this);
        for (enemy en : this.enemies)
            en.draw(this);
    }

    public void keyPressed() {
        player.setInputPress(this.keyCode);
    }

    public static void main(String[] args) {
        PApplet.main("lawnlayer.App");
    }
    
}
