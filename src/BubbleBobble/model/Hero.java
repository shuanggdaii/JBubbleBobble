package BubbleBobble.model;

import BubbleBobble.designPattern.HeroActionStrategy;
import BubbleBobble.designPattern.NormalHeroAction;

import java.awt.Color;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;

/**
 * The Hero class represents the main character of the game. The hero can move, shoot bubbles,
 * collect fruit, and interact with monsters. The hero has a limited number of lives, and the game 
 * ends when the hero either completes the final level or runs out of lives.
 * 
 * The class uses the Strategy Design Pattern to allow different behaviors for the hero depending
 * on its current state.
 */
public class Hero extends Collidable {

    public static final int MAX_LIVES = 5;  // Maximum number of lives the hero can have
    public int lives;  // Current number of lives
    public Point2D startingPoint;  // Starting point of the hero (for respawning)
    public boolean invincible = false;  // Whether the hero is invincible (e.g., after colliding with an enemy)
    boolean type = false;  // Placeholder for any hero type (potentially for future customization)
    private HeroActionStrategy actionStrategy;  // Strategy for hero's actions (e.g., moving, shooting)

    /**
     * Constructs a Hero object with specified position, size, and initial lives.
     * The hero starts with a default action strategy and a set of predefined sprites.
     *
     * @param level The level where the hero is located.
     * @param x The initial X-coordinate of the hero.
     * @param y The initial Y-coordinate of the hero.
     * @param width The width of the hero sprite.
     * @param height The height of the hero sprite.
     */
    public Hero(Level level, int x, int y, int width, int height) {
        super(level, x, y, width, height);
        this.startingPoint = new Point2D.Double(this.positionX, this.positionY);
        this.lives = MAX_LIVES;
        this.color = Color.YELLOW;  // Default hero color

        // Choose initial strategy
        this.actionStrategy = new NormalHeroAction();

        // Initialize hero sprites for different states
        this.sprites = new Image[5];
        this.sprites[0] = (new ImageIcon("sprites/hero0.png")).getImage();  // Standing sprite
        this.sprites[1] = (new ImageIcon("sprites/hero_left.gif")).getImage();  // Left direction sprite
        this.sprites[2] = (new ImageIcon("sprites/hero_right.gif")).getImage();  // Right direction sprite
        this.sprites[3] = (new ImageIcon("sprites/hero_invincible_left.gif")).getImage();  // Invincible left sprite
        this.sprites[4] = (new ImageIcon("sprites/hero_invincible_right.gif")).getImage();  // Invincible right sprite
        this.sprite = this.sprites[0];  // Default sprite
    }

    /**
     * Sets the action strategy for the hero, allowing different behaviors based on the hero's current state.
     *
     * @param actionStrategy The strategy to set for the hero's actions.
     */
    public void setActionStrategy(HeroActionStrategy actionStrategy) {
        this.actionStrategy = actionStrategy;
    }

    /**
     * Updates the position of the hero based on gravity and collision states.
     * The hero's sprite direction is also updated based on the current movement direction.
     */
    @Override
    public void updatePosition() {
        this.changeSpriteDirection();  // Change sprite direction based on movement
        this.isCollidingLeft = false;
        this.isCollidingRight = false;
        this.isFalling = !(this.vy < 1);  // Check if the hero is falling
        this.vy = this.vy + GRAVITY * DT;  // Apply gravity
        this.positionY += this.vy * DT;  // Update vertical position
    }

    /**
     * Adds the hero to the level's hero list (if not already added).
     */
    @Override
    public void addToList() {
        if (this.level.hero1 == null) {
            this.level.hero1 = this;  // Assign hero to level if not already assigned
        }
    }

    /**
     * Handles collisions with platforms, adjusting the hero's position if necessary.
     * This method is responsible for preventing the hero from passing through platforms.
     *
     * @param p The platform with which the hero may collide.
     */
    @Override
    public void collideWithPlatform(Platform p) {
        super.collideWithPlatform(p);

        if (this.isAlive) {
            // Adjust position if colliding with platform
            if (this.isCollidingRight) {
                this.positionX -= PRECISION / 2;
            }
            if (this.isCollidingLeft) {
                this.positionX += PRECISION / 2;
            }
        }
    }

    /**
     * Changes the direction of the hero's sprite based on its current movement direction.
     * The hero's sprite is updated to reflect either the left or right movement.
     */
    public void changeSpriteDirection() {
        if (!this.invincible) {
            if (this.direction == -1) {
                this.sprite = this.sprites[1];  // Left movement
            }
            if (this.direction == 1) {
                this.sprite = this.sprites[2];  // Right movement
            }
        }
    }

    /**
     * Moves the hero in the specified direction. The action strategy determines the behavior of the movement.
     *
     * @param direction The direction in which to move the hero ("left", "right", etc.).
     */
    public void move(String direction) {
        actionStrategy.move(this, direction);
    }

    /**
     * Shoots a bubble from the hero. The behavior is determined by the hero's current action strategy.
     */
    public void shootBubble() {
        actionStrategy.shootBubble(this);
    }

    /**
     * Shoots a fire bubble from the hero. The behavior is determined by the hero's current action strategy.
     * The type of fire bubble is passed as a parameter.
     *
     * @param type The type of fire bubble to shoot.
     */
    public void shootFireBubble(int type) {
        actionStrategy.shootFireBubble(this, type);
    }

    /**
     * Handles the collision between the hero and an enemy (e.g., Fireball, BubbleBuster, or Incendo).
     * If the hero is not invincible, it loses a life, and the game pauses briefly. The hero respawns
     * after a brief invincibility period.
     */
    public void collideWithEnemy() {
        if (!this.invincible) {
            if (this.lives == 0) {
                this.isAlive = false;  // Hero is dead
                return;
            }
            this.invincible = true;  // Make the hero invincible for a short time
            this.isAlive = false;  // Hero is temporarily not alive
            this.lives -= 1;  // Decrease the number of lives
            this.vy = JUMP_VELOCITY;  // Set the vertical speed for respawning

            Timer timer = new Timer();

            // Schedule respawn event after 1.5 seconds
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Hero.this.isAlive = true;  // Hero is respawned
                    Hero.this.positionX = (int) Hero.this.startingPoint.getX();
                    Hero.this.positionY = (int) Hero.this.startingPoint.getY() - 3 * PRECISION;
                    Hero.this.color = Color.PINK;  // Temporarily change color for respawn
                }
            }, 1500);

            // Schedule end of invincibility period after 5 seconds
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Hero.this.invincible = false;  // End invincibility
                    Hero.this.color = Color.YELLOW;  // Restore original color
                }
            }, 5000);
        }
    }
}
