package BubbleBobble.model;

import java.awt.Color;
import javax.swing.ImageIcon;

/**
 * Fireball is a projectile shot by an Incendo monster. It travels a certain distance before disappearing.
 * Fireballs collide with the hero in the same way that monsters do. If a fireball touches the hero, it can harm them.
 */
public class Fireball extends Projectile {

    /**
     * Constructs a new Fireball object with the specified level, position, and direction.
     * 
     * @param level The level the fireball belongs to.
     * @param x The initial X-coordinate of the fireball.
     * @param y The initial Y-coordinate of the fireball.
     * @param dir The direction of the fireball (-1 for left, 1 for right).
     */
    public Fireball(Level level, int x, int y, int dir) {
        super(level, x, y, dir);
        this.color = Color.RED;
        // Set the appropriate sprite based on the direction of the fireball
        if (this.direction == -1) {
            this.sprite = (new ImageIcon("sprites/fireball_left.png")).getImage();
        } else {
            this.sprite = (new ImageIcon("sprites/fireball_right.png")).getImage();
        }
    }

    /**
     * Updates the position of the fireball. If the fireball reaches the end of its path, it will disappear.
     */
    @Override
    public void updatePosition() {
        super.updatePosition();
        // If the horizontal velocity is zero, the fireball has traveled its distance and should be removed
        if (this.dx == 0) {
            this.die();
        }
    }

    /**
     * Adds the fireball to the level's list of active fireballs.
     */
    @Override
    public void addToList() {
        this.level.fireballs.add(this);
    }
}
