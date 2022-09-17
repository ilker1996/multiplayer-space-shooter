package com.swconstruction.frontend.game;

import java.util.HashMap;
import java.util.Map;

// Game settings class for individual levels and also for general game
public class GameSettings {

    // Corresponding individual level settings maps
    private static Map<String,Integer> lvl1;
    private static Map<String,Integer> lvl2;
    private static Map<String,Integer> lvl3;
    private static Map<String,Integer> lvl4;

    // Multi player game settings (constants)



    private static int maxLevel = 4; // Finishing level
    private static int bulletRadius = 5; // Radius of bullets
    private static int monsterNumPerRow = 15; // Number of monster per row in game
    private static double offsetRate = 0.1; // Offset rate of monsters (according to screen width and height) to the screen edges
    private static double hGapRate = 0.025; // Horizontal gap rate (according to screen width) between monsters
    private static double vGapRate = 0.05; // Vertical gap rate(according to screen height) between monsters
    private static int waitDuration = 3000; // Wait 3000 ms before starting the multiplayer game
    private static double distanceRate = 0.2; // Distance rate between the opponent and local player
    private static int playerSpeed = 5; // Speed of the player when key pressed

    // Nested map of settings for easiness of access of settings
    private static Map<Integer,Map<String,Integer>> settings = new HashMap<>();

    static {
        lvl1 = new HashMap<>();
        lvl2 = new HashMap<>();
        lvl3 = new HashMap<>();
        lvl4 = new HashMap<>();

        // Level of the game
        lvl1.put("level", 1);
        // Monster number of the level
        lvl1.put("monsterCount", 15);
        // Monster health of the level
        lvl1.put("monsterHealth", 1);
        // Player health of the level
        lvl1.put("playerHealth", 3);
        // Bullet speed of the player
        lvl1.put("playerBulletSpeed", 20);
        // Bullet speed of the monster
        lvl1.put("monsterBulletSpeed", 10);
        // Number of created bullets from monster in unit time
        lvl1.put("monsterBulletLuck", 2);
        // Score incrementer for every monster killing
        lvl1.put("scoreMultiplier", 1);

        lvl2.put("level", 2);
        lvl2.put("monsterCount", 30);
        lvl2.put("monsterHealth", 2);
        lvl2.put("playerHealth", 3);
        lvl2.put("playerBulletSpeed", 20);
        lvl2.put("monsterBulletSpeed", 15);
        lvl2.put("monsterBulletLuck", 3);
        lvl2.put("scoreMultiplier", 2);

        lvl3.put("level", 3);
        lvl3.put("monsterCount", 30);
        lvl3.put("monsterHealth", 2);
        lvl3.put("playerHealth", 3);
        lvl3.put("playerBulletSpeed", 20);
        lvl3.put("monsterBulletSpeed", 20);
        lvl3.put("monsterBulletLuck", 5);
        lvl3.put("scoreMultiplier", 3);

        lvl4.put("level", 4);
        // There will be only one monster(boss)
        lvl4.put("monsterCount", 1);
        lvl4.put("monsterHealth", 30);
        lvl4.put("playerHealth", 3);
        lvl4.put("playerBulletSpeed", 20);
        lvl4.put("monsterBulletSpeed", 20);
        lvl4.put("monsterBulletLuck", 100);
        lvl4.put("scoreMultiplier", 1000);
        // Displacement of the boss in every animation tick
        lvl4.put("monsterSpeed" , 10);

        // Put in a map for easiness of access
        settings.put(1, lvl1);
        settings.put(2, lvl2);
        settings.put(3, lvl3);
        settings.put(4, lvl4);

    }

    // return corresponding level settings
    public static Map<String,Integer> getSettings(int gameLevel)
    {

        return settings.get(gameLevel);
    }

    public static  int getPlayerSpeed(){return playerSpeed;}
    public static int getMaxLevel(){ return maxLevel;}
    // Return if level is valid (i.e. exceeds the max level? )
    public static boolean isValidLevel(int gameLevel)
    {
        return (gameLevel <= maxLevel && gameLevel > 0);
    }

    public static int getBulletRadius()
    {
        return bulletRadius;
    }

    public static int getMonsterNumPerRow()
    {
        return monsterNumPerRow;
    }

    public static double getOffsetRate()
    {
        return offsetRate;
    }

    public static double gethGapRate()
    {
        return hGapRate;
    }
    public static double getvGapRate()
    {
        return vGapRate;
    }
    public static int getWaitDuration(){ return waitDuration;}
    public static double getDistanceRate(){return distanceRate;}
}
