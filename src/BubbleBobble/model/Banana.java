package BubbleBobble.model;

import BubbleBobble.view.GameView;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents a Banana object in the game, which can be collected by the hero to gain points.
 * The Banana falls from a certain height and becomes collectible after a short grace period.
 * 
 * The Banana class extends {@link Collidable} and is added to the list of bananas in the level.
 * When the hero collides with the banana, the player's score is increased by 300 points.
 */
public class Banana extends Collidable {

    /** A flag to indicate if the hero can touch and collect the banana. */
    boolean heroCanTouch;

    /**
     * Creates a Banana instance at the specified position with the given dimensions.
     * The banana starts falling and becomes collectible after a grace period.
     * 
     * @param level The level in which the banana will exist.
     * @param x The x-coordinate of the banana's position.
     * @param y The y-coordinate of the banana's position.
     * @param width The width of the banana.
     * @param height The height of the banana.
     */
    public Banana(Level level, int x, int y, int width, int height) {
        super(level, x, y, width, height);
        this.heroCanTouch = false;  // Initially, the hero cannot touch the banana
        this.sprite = (new ImageIcon("sprites/fruit_bubble.png")).getImage();  // Set the banana's image

        java.util.Timer timer = new java.util.Timer();
        // Allow grace period for fruit to fall before colliding with hero
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Banana.this.heroCanTouch = true;  // Make the banana collectible after 500ms
            }
        }, 500);  // 500ms grace period before the banana is collectible
    }

    /**
     * Updates the position of the banana. In this case, it stops falling once it has reached the ground.
     * The vertical speed is set to 0, and the banana is no longer considered as falling.
     */
    @Override
    public void updatePosition() {
        this.vy = 0;                     // Stop the vertical speed
        this.isFalling = false;          // Mark the banana as not falling anymore
    }

    /**
     * Adds the banana to the list of bananas in the current level.
     * This allows the banana to be tracked and interacted with by the game system.
     */
    @Override
    public void addToList() {
        this.level.bananas.add(this);  // Add the banana to the list of bananas
        // The commented-out code below could be used to remove the banana after a time period, if desired.
        // Banana that = this;
        // new Timer().schedule(new TimerTask() {
        //     @Override
        //     public void run() {
        //         that.level.bananas.forEach(Banana::die);  // Call die on all bananas in the level
        //     }
        // }, 30000);  // Set the timer for 30 seconds (this is currently commented out)
    }

    /**
     * Checks if the banana has collided with the hero.
     * If the hero can touch the banana, the score is increased by 300 points.
     * The banana is then removed from the game.
     */
    public void collideWithHero() {
        if (this.heroCanTouch) {
            if (this.level.isPlayable()) {
                if (this.level.overlapping(this, this.level.hero1)) {  // Check if the banana overlaps with the hero
                    this.level.score += 300;  // Increase the score by 300 points
                    // Display a message indicating the banana was eaten
                    SwingUtilities.invokeLater(() -> GameView.showMessage("eat Banana add 300 point"));
                }
            }
            this.die();  // Call the die method to remove the banana from the game
        }
    }
}
