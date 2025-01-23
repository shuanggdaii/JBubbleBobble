package BubbleBobble.model;

import java.awt.Color;
import javax.swing.ImageIcon;

/**
 * The BubbleBuster is the most basic type of monster in the game. 
 * It follows all the methods defined in the `Monster` superclass.
 * This monster is a key opponent in the game that interacts with the player's bubble and movement mechanics.
 * It is colored magenta and has a score value of 500.
 * 
 * The BubbleBuster can be shot with bubbles and can cause players to lose lives if they come into contact.
 */
public class BubbleBuster extends Monster {

    /**
     * Constructs a new BubbleBuster monster with a specified position and size.
     * Initializes the monster's color, score value, and sprite images based on direction.
     * 
     * @param level The level in which the BubbleBuster exists.
     * @param x The x-coordinate of the monster's position.
     * @param y The y-coordinate of the monster's position.
     * @param width The width of the monster.
     * @param height The height of the monster.
     */
    public BubbleBuster(Level level, int x, int y, int width, int height) {
        super(level, x, y, width, height);
        this.color = Color.MAGENTA;  // Set the monster's color to magenta
        this.scoreValue = 500;       // Set the score value when the monster is defeated
        // Set the sprite images for the monster's different directions and states
        this.sprites[0] = (new ImageIcon("sprites/bubble_buster_left.gif")).getImage();
        this.sprites[1] = (new ImageIcon("sprites/bubble_buster_right.gif")).getImage();
        this.sprites[2] = (new ImageIcon("sprites/bubble_buster_bubble.gif")).getImage();
        // Set the initial sprite based on the direction the monster is facing
        this.sprite = (this.direction == 1) ? this.sprites[1] : this.sprites[0];
    }

    /**
     * Adds this BubbleBuster monster to the list of monsters in the level.
     * This method allows the level to manage all the monsters that exist within it.
     */
    @Override
    public void addToList() {
        this.level.monsters.add(this);  // Add the BubbleBuster to the list of monsters in the current level
    }
}
