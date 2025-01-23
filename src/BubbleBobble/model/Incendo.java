package BubbleBobble.model;

import java.awt.Color;
import java.util.Calendar;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 * Incendo is a subclass of Monster. In addition to the abilities of a BubbleBuster,
 * Incendo periodically shoots fireballs in the current direction.
 * 
 * The fireballs are shot at random intervals between 2 to 4 seconds.
 * This class extends the Monster class by adding fireball shooting functionality.
 */
public class Incendo extends Monster {

    // Last time the fireball was shot (in milliseconds)
    private long lastFireballTime = 0;
    // Randomized interval between shooting fireballs (in milliseconds), ranging from 2000 to 4000 ms
    private long fireballInterval;
    // Random instance used to generate random numbers for fireball intervals
    private static final Random random = new Random();

    /**
     * Constructs an Incendo object with a given level, position, and size.
     * The Incendo character has a specific sprite and a random fireball shooting interval.
     *
     * @param level The level where the Incendo is placed.
     * @param x The X-coordinate of the Incendo's initial position.
     * @param y The Y-coordinate of the Incendo's initial position.
     * @param width The width of the Incendo sprite.
     * @param height The height of the Incendo sprite.
     */
    public Incendo(Level level, int x, int y, int width, int height) {
        super(level, x, y, width, height);
        this.color = Color.BLUE;  // Set color of the Incendo
        this.scoreValue = 3000;  // Set score value for defeating Incendo
        // Initialize sprite images for different directions
        this.sprites[0] = (new ImageIcon("sprites/incendo_left.gif")).getImage();
        this.sprites[1] = (new ImageIcon("sprites/incendo_right.gif")).getImage();
        this.sprites[2] = (new ImageIcon("sprites/incendo_bubble.gif")).getImage();
        // Set the sprite based on the direction of Incendo
        this.sprite = (this.direction == 1) ? this.sprites[1] : this.sprites[0];
        // Initialize a random fireball shooting interval between 2000 and 4000 ms
        this.fireballInterval = (long) (random.nextDouble() * (5000 - 2000) + 2000);
    }

    /**
     * Updates the position of Incendo. This method checks whether it's time to shoot a fireball
     * based on the randomized fireball interval. If the time has passed, Incendo shoots a fireball.
     */
    @Override
    public void updatePosition() {
        super.updatePosition();  // Call the updatePosition method of the superclass
        // Get the current timestamp in milliseconds
        long currentTime = Calendar.getInstance().getTimeInMillis();

        // Check if the time has passed for the next fireball to be shot
        if (currentTime - lastFireballTime >= fireballInterval && !this.isBubbled) {
            // Update the last fireball shooting time
            lastFireballTime = currentTime;
            // Generate a new random interval for the next fireball shot
            this.fireballInterval = (long) (random.nextDouble() * (4000 - 2000) + 2000);

            // Shoot the fireball
            this.shootFireball();
        }
    }

    /**
     * Adds Incendo to the list of monsters in the level.
     */
    @Override
    public void addToList() {
        this.level.monsters.add(this);
    }

    /**
     * Shoots a fireball in the current direction.
     * A new Fireball object is created and added to the level's collidables.
     */
    public void shootFireball() {
        // Create a Fireball object and add it to the level's collidables
        Fireball fireball = new Fireball(this.level, this.positionX, this.positionY, this.direction);
        this.level.addCollidable(fireball);
    }
}
