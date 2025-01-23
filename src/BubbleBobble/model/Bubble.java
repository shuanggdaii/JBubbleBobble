package BubbleBobble.model;

import java.awt.Color;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

/**
 * Represents a Bubble shot by the hero. The bubble can trap monsters upon contact
 * and transport them to the top of the screen. After reaching the top, the bubble
 * may spawn fruit after a delay. If the bubble remains untouched for a set period of
 * time, it will pop.
 * 
 * The bubble moves horizontally first, then vertically upwards until it reaches
 * the top. It can generate fruit after a brief period of time.
 */
public class Bubble extends Projectile {

    /** A flag indicating whether the bubble has reached the top of the screen. */
    private boolean reachedTop = false;

    /** A flag indicating whether the bubble has generated fruit. */
    boolean hasFruit = false;

    /**
     * Constructs a new Bubble at the specified position and direction.
     * 
     * @param level The level in which the bubble exists.
     * @param x The x-coordinate of the bubble's position.
     * @param y The y-coordinate of the bubble's position.
     * @param dir The direction in which the bubble will be shot.
     */
    public Bubble(Level level, int x, int y, int dir) {
        super(level, x, y, dir);
        this.color = Color.CYAN;  // Set the bubble's color
        this.scoreValue = 10;     // Set the score value when the bubble is collected
        this.sprites = new Image[3]; // Initialize the array of sprites for different states of the bubble
        this.sprites[0] = (new ImageIcon("sprites/bubble.gif")).getImage();  // Default sprite
        this.sprites[1] = (new ImageIcon("sprites/bubble6.png")).getImage();  // Sprite when the bubble is rising
        this.sprites[2] = (new ImageIcon("sprites/fruit_bubble.png")).getImage();  // Sprite when the bubble has fruit
        this.sprite = this.sprites[0];  // Set the initial sprite
    }

    /**
     * Updates the position of the bubble. The bubble moves horizontally for a
     * certain distance, and then starts moving vertically upwards until it reaches
     * the top of the screen. Once at the top, it may spawn fruit after a delay and
     * will pop after a longer delay.
     */
    @Override
    public void updatePosition() {
        super.updatePosition(); // Call the superclass's updatePosition method to update position
        if (this.dx == 0 && !this.reachedTop) {
            this.dy = -DX;  // Start moving upwards
            this.sprite = this.sprites[1];  // Change sprite to rising bubble
        }
        if (this.positionY <= 0 && !this.reachedTop) {  // If the bubble reaches the top
            this.reachedTop = true;  // Mark as reached top
            this.dy = 0;  // Stop vertical movement

            // Start a timer to spawn fruit after 5 seconds
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Bubble.this.hasFruit = true;  // Mark that the bubble now has fruit
                    Bubble.this.sprite = Bubble.this.sprites[2];  // Change sprite to fruit bubble
                }
            }, 5000);  // 5000 ms delay before fruit is spawned

            // Start a timer to pop the bubble after 8 seconds
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Bubble.this.die();  // Pop the bubble after 8000 ms
                }
            }, 8000);  // 8000 ms delay before bubble pops
        }
    }

    /**
     * Adds the bubble to the list of bubbles in the level so it can be updated and interacted with.
     */
    @Override
    public void addToList() {
        this.level.bubbles.add(this);  // Add the bubble to the level's list of bubbles
    }

    /**
     * Handles collision between the bubble and the hero. If the bubble has fruit,
     * it will create a new fruit item in the level at the bubble's position.
     * 
     * If the bubble does not have fruit, it simply dies.
     */
    public void collideWithHero() {
        if (this.hasFruit) {
            this.die();  // Remove the bubble from the game
            int x = this.positionX;  // Get the bubble's position
            int y = this.positionY;
            // Spawn fruit at the bubble's position
            this.level.addCollidable(new Fruit(this.level, x, y, SIZE, SIZE, this.scoreValue));
        } else {
            this.die();  // Simply remove the bubble if it does not have fruit
        }
    }

    /**
     * Sets the score based on the specified type.
     * 
     * @param type The type of the score (1 for player 1, other types for player 2).
     */
    public void setScore(int type) {
        if (type == 1) {
            this.level.score += this.scoreValue;  // Add score to player 1
        } else {
            this.level.score2 += this.scoreValue;  // Add score to player 2
        }
    }
}
