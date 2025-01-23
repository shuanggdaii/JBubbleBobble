package BubbleBobble.model;

import BubbleBobble.view.GameView;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents an Apple object in the game, which can be collected by the hero to gain an extra life.
 * The Apple falls from a certain height and becomes collectible after a short grace period.
 * 
 * The Apple class extends {@link Collidable} and is added to the list of apples in the level.
 * When the hero collides with the apple, the hero's lives are increased by 1.
 */
public class Apple extends Collidable {

    /** A flag to indicate if the hero can touch and collect the apple. */
    boolean heroCanTouch;

    /**
     * Creates an Apple instance at the specified position with the given dimensions.
     * The apple starts falling and becomes collectible after a grace period.
     * 
     * @param level The level in which the apple will exist.
     * @param x The x-coordinate of the apple's position.
     * @param y The y-coordinate of the apple's position.
     * @param width The width of the apple.
     * @param height The height of the apple.
     */
    public Apple(Level level, int x, int y, int width, int height) {
        super(level, x, y, width, height);
        this.heroCanTouch = false;  // Initially, the hero cannot touch the apple
        this.sprite = (new ImageIcon("sprites/apple.png")).getImage(); // Set the apple's image

        java.util.Timer timer = new java.util.Timer();
        // Allow grace period for fruit to fall before colliding with hero
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Apple.this.heroCanTouch = true;  // Make the apple collectible after 500ms
            }
        }, 500);  // 500ms grace period before the apple is collectible
    }

    /**
     * Updates the position of the apple. In this case, it stops falling once it has reached the ground.
     * The vertical speed is set to 0, and the apple is no longer considered as falling.
     */
    @Override
    public void updatePosition() {
        this.vy = 0;                     // Stop the vertical speed
        this.isFalling = false;          // Mark the apple as not falling anymore
    }

    /**
     * Adds the apple to the list of apples in the current level.
     * This allows the apple to be tracked and interacted with by the game system.
     */
    @Override
    public void addToList() {
        this.level.apples.add(this); // Add the apple to the list of apples
        Apple that = this;
        // The commented-out code below could be used to remove the apple after a time period, if desired.
        // new Timer().schedule(new TimerTask() {
        //     @Override
        //     public void run() {
        //         that.level.apples.forEach(Apple::die);  // Call die on all apples in the level
        //     }
        // }, 30000);  // Set the timer for 30 seconds (this is currently commented out)
    }

    /**
     * Checks if the apple has collided with the hero.
     * If the hero can touch the apple, the hero's life is increased by 1.
     * The apple is then removed from the game.
     */
    public void collideWithHero() {
        if (this.heroCanTouch) {
            // Only increase the score if the level is playable
            if (this.level.isPlayable()) {
                if (this.level.overlapping(this, this.level.hero1)) {  // Check if the apple overlaps with the hero
                    this.level.hero1.lives += 1;  // Increase the hero's lives by 1
                }
                GameView.showMessage("eat Apple add 1 life");  // Show a message when the apple is collected
            }
            this.die();  // Call the die method to remove the apple from the game
        }
    }
}


