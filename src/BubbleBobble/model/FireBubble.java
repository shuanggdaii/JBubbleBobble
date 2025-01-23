package BubbleBobble.model;

import javax.swing.*;
import java.awt.*;

/**
 * FireBubble is a special type of projectile shot by the hero. It behaves similarly to a regular bubble but has additional properties.
 * FireBubbles travel a certain distance before disappearing and can interact with the hero and other objects in the game.
 */
public class FireBubble extends Projectile {

    private int type; // The type of the fire bubble, used to identify which hero shot it.

    /**
     * Constructs a new FireBubble with the specified level, position, and direction.
     * 
     * @param level The level in which the fire bubble exists.
     * @param x The X-coordinate of the fire bubble.
     * @param y The Y-coordinate of the fire bubble.
     * @param dir The direction of the fire bubble (-1 for left, 1 for right).
     */
    public FireBubble(Level level, int x, int y, int dir) {
        super(level, x, y, dir);
        this.color = Color.RED;
        // Set the appropriate sprite based on the direction of the fire bubble
        if (this.direction == -1) {
            this.sprite = (new ImageIcon("sprites/fireBubbleLeft.png")).getImage();
        } else {
            this.sprite = (new ImageIcon("sprites/fireBubbleRight.png")).getImage();
        }
    }

    /**
     * Updates the position of the fire bubble. If the fire bubble reaches the end of its path, it will disappear.
     */
    @Override
    public void updatePosition() {
        super.updatePosition();
        // If the horizontal velocity is zero, the fire bubble has traveled its distance and should be removed
        if (this.dx == 0) {
            this.die();
        }
    }

    /**
     * Adds the fire bubble to the level's list of active fire bubbles.
     */
    @Override
    public void addToList() {
        this.level.fireBubbles.add(this);
    }

    /**
     * Sets the type of the fire bubble. This is used to identify which hero shot the fire bubble.
     * 
     * @param type The type of the fire bubble (typically represents the hero who shot it).
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Gets the type of the fire bubble.
     * 
     * @return The type of the fire bubble.
     */
    public int getType() {
        return type;
    }
}
