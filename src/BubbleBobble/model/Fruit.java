package BubbleBobble.model;

import java.awt.Color;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

/**
 * The Fruit class represents collectible items in the game that increase the player's score
 * when picked up. Different types of fruit spawn under different conditions and have different score values:
 * 
 *  - Banana (10 points) - Spawned by bubbles.
 *  - Eggplant (500 points) - Spawned by BubbleBuster upon death.
 *  - Watermelon (3000 points) - Spawned by Incendo upon death.
 *  - Rascal (Special character) - Spawned upon completing the game.
 */
public class Fruit extends Collidable {

    private boolean heroCanTouch;

    /**
     * Constructs a Fruit object with a specific position, size, and score value.
     * The fruit has a grace period before it can be collected by the hero.
     *
     * @param level The level in which the fruit exists.
     * @param x The X-coordinate of the fruit.
     * @param y The Y-coordinate of the fruit.
     * @param width The width of the fruit.
     * @param height The height of the fruit.
     * @param scoreValue The score value associated with the fruit (10, 500, 3000, or special score).
     */
    public Fruit(Level level, int x, int y, int width, int height, int scoreValue) {
        super(level, x, y, width, height);
        this.scoreValue = scoreValue;
        this.color = Color.GREEN;
        this.heroCanTouch = false;

        // Initialize fruit sprites based on score value
        this.sprites = new Image[4];
        this.sprites[0] = (new ImageIcon("sprites/banana.png")).getImage();
        this.sprites[1] = (new ImageIcon("sprites/eggplant.png")).getImage();
        this.sprites[2] = (new ImageIcon("sprites/watermelon.png")).getImage();
        this.sprites[3] = (new ImageIcon("sprites/ur_boi_rascal.gif")).getImage();

        // Set the sprite based on the fruit type
        if (scoreValue == 10) {
            this.sprite = this.sprites[0];
        } else if (scoreValue == 500) {
            this.sprite = this.sprites[1];
        } else if (scoreValue == 3000) {
            this.sprite = this.sprites[2];
        } else {
            this.sprite = this.sprites[3];
        }

        // Allow the fruit to be collected after a brief delay
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Fruit.this.heroCanTouch = true;
            }
        }, 500);
    }

    /**
     * Updates the position of the fruit, making it fall under the influence of gravity.
     * The fruit's vertical speed (vy) is updated based on gravity, and the position is updated accordingly.
     */
    @Override
    public void updatePosition() {
        this.isFalling = !(this.vy < 1);  // The fruit is falling unless its vertical speed is negligible
        this.vy = this.vy + GRAVITY * DT;  // Apply gravity
        this.positionY += this.vy * DT;  // Update the vertical position
    }

    /**
     * Adds the fruit to the level's list of fruits so it can be managed and rendered.
     */
    @Override
    public void addToList() {
        this.level.fruits.add(this);
    }

    /**
     * Handles the collision of the fruit with the hero.
     * If the hero touches the fruit, the score is increased by the fruit's score value,
     * and the fruit is removed from the level.
     */
    public void collideWithHero() {
        if (this.heroCanTouch) {
            // Only increase the score if the level is playable
            if (this.level.isPlayable()) {
                // Check if the hero is overlapping with the fruit
                if (this.level.overlapping(this, this.level.hero1)) {
                    this.level.score += this.scoreValue;  // Add fruit's score value to the total score
                }
            }
            this.die();  // Remove the fruit from the level
        }
    }
}
