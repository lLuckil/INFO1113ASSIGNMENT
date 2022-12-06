package lawnlayer;

import lawnlayer.gridObject.player;
import org.junit.jupiter.api.Test;
import lawnlayer.enemy.*;
public class SampleTest {
    @Test
    public void simpleTest() {
        App app = new App();
        player p = new player();
    }
    @Test
    public void enempyCreatTest(){
        enemy worm_enemy = new worm("worm.png");
        enemy beetle_enemy = new beetle("beetle.png");
    }
}

