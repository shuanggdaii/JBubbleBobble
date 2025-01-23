package BubbleBobble.model;

import java.awt.*;
import java.util.Calendar;
import java.util.Random;

import javax.swing.ImageIcon;

/**
 * MyMonster class extends the Monster class and represents a specific type of
 * monster with unique behaviors, including shooting fireballs at random intervals,
 * handling attacks, and drawing the attack count.
 */
public class Giant extends Monster {

    public int count = 50;  // Number of attacks remaining (i.e., health of the monster)

    // The timestamp of the last fireball shot (in milliseconds)
    private long lastFireballTime = 0;

    // Interval between fireball shots (in milliseconds), randomized between 4 and 6 seconds
    private long fireballInterval;

    // Random instance used for generating random values
    private static final Random random = new Random();

    /**
     * Constructor to initialize a MyMonster with specific position, size, and appearance.
     * The monster will also have a randomized fireball interval between 4000 and 6000 milliseconds.
     *
     * @param level The level this monster belongs to.
     * @param x The x-coordinate of the monster.
     * @param y The y-coordinate of the monster.
     * @param width The width of the monster.
     * @param height The height of the monster.
     */
    public Giant(Level level, int x, int y, int width, int height) {
        super(level, x, y, width, height);
        this.sprites[0] = (new ImageIcon("sprites/m_left.gif")).getImage();  // Left-facing sprite
        this.sprites[1] = (new ImageIcon("sprites/m_right.gif")).getImage();  // Right-facing sprite
        this.sprite = (this.direction == 1) ? this.sprites[1] : this.sprites[0];  // Set initial sprite
        // Initialize the fireball shooting interval to a random value between 4000 and 6000 ms
        this.fireballInterval = (long) (random.nextDouble() * (6000 - 4000) + 4000);
    }

    /**
     * Updates the position of the monster, checking if it's time to shoot a fireball
     * based on the random fireball interval.
     */
    @Override
    public void updatePosition() {
        super.updatePosition();  // Update basic monster position and movement

        // Get the current timestamp (in milliseconds)
        long currentTime = Calendar.getInstance().getTimeInMillis();

        // If the random time interval has passed, shoot a fireball
        if (currentTime - lastFireballTime >= fireballInterval && !this.isBubbled) {
            // Update the timestamp of the last fireball shot
            lastFireballTime = currentTime;

            // Generate a new random interval for the next fireball shot
            this.fireballInterval = (long) (random.nextDouble() * (6000 - 4000) + 4000);

            // Shoot a fireball
            this.shootFireball();
        }
    }

    /**
     * Adds this monster to the level's list of monsters.
     */
    @Override
    public void addToList() {
        this.level.monsters.add(this);
    }

    /**
     * Handles the collision of this monster with a bubble. The monster loses
     * one attack count upon collision, and dies when the attack count reaches 0.
     */
    @Override
    public void collideWithBubble() {
        if (--this.count <= 0) {
            this.die();  // The monster dies when its count reaches 0
        }
    }

    /**
     * Handles the collision of this monster with a fire bubble. The monster loses
     * 3 attack counts upon collision. If the count reaches 0 or below, it dies and
     * the level score is increased by 2000 for both players.
     *
     * @param type The type of the fire bubble (1 for Hero, 2 for Player 2).
     * @param level The current game level.
     */
    @Override
    public void collideWithFireBubble(int type, Level level) {
        if (this.count - 3 <= 0) {
            this.die();  // The monster dies if its count reaches 0
            level.score += 2000;  // Award points to Hero
            level.score2 += 2000;  // Award points to Player 2
        } else {
            this.count -= 3;  // Reduce the attack count by 3
        }
    }

    /**
     * Shoots a fireball in the current direction. The fireball is added to the level.
     */
    public void shootFireball() {
        Fireball fireball = new Fireball(this.level, this.positionX, this.positionY, this.direction);
        this.level.addCollidable(fireball);  // Add the fireball to the level's list of collidables
    }

    /**
     * Draws the attack count on the screen above the monster's head.
     *
     * @param g The graphics object used to draw the attack count.
     */
    public void drawAttackCount(Graphics g) {
        // Set the font and color for the attack count
        g.setFont(new Font("SansSerif", Font.BOLD, 15));
        g.setColor(Color.BLUE);

        // Calculate the position to draw the count (above the monster's head)
        int textX = this.positionX + 14;  // Slightly offset from the monster's X position
        int textY = this.positionY - 4;   // Slightly offset above the monster's Y position

        // Draw the attack count as text
        g.drawString(String.valueOf(this.count), textX, textY);
    }
}
