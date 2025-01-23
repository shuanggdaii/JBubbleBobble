package BubbleBobble.model;

import java.awt.Graphics2D;
import java.awt.Image;

/**
 * Platform class represents a static surface in the game where other collidable objects
 * (such as monsters or heroes) can walk on or jump through. The platform itself does not 
 * move or interact with physics but serves as an important object in the level.
 */
public class Platform extends Collidable {

    /**
     * Constructor to initialize a platform with a specific position, size, and sprite.
     *
     * @param level The level this platform belongs to.
     * @param x The x-coordinate of the platform.
     * @param y The y-coordinate of the platform.
     * @param width The width of the platform.
     * @param height The height of the platform.
     * @param sprite The image sprite to represent the platform.
     */
    public Platform(Level level, int x, int y, int width, int height, Image sprite) {
        super(level, x, y, width, height);
        this.sprite = sprite;  // Assign the platform's sprite image
    }

    /**
     * Updates the platform's position. Since the platform is stationary in this game,
     * this method does not change anything.
     */
    @Override
    public void updatePosition() {
        // Platform does not change position, so nothing happens here
    }

    /**
     * Draws the platform on the screen using the provided graphics object.
     * The platform's sprite is rendered at its current position.
     *
     * @param g The graphics object used to draw the platform on the screen.
     */
    @Override
    public void draw(Graphics2D g) {
        g.drawImage(this.sprite, this.positionX, this.positionY, null);  // Draw the platform's sprite
    }

    /**
     * Adds this platform to the list of platforms in the level.
     * This ensures that the platform is managed by the level's platform collection.
     */
    @Override
    public void addToList() {
        this.level.platforms.add(this);  // Add the platform to the level's platform list
    }
}

