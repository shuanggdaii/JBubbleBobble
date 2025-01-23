package BubbleBobble.model;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The Monster abstract class defines the basic characteristics of any monster
 * type in the game, including platform collisions, movement, and interactions
 * with other game objects (e.g., Hero, Bubble, FireBubble).
 */
public abstract class Monster extends Collidable {

    // Constants to control monster behavior
    private static final double PROB_OF_JUMP = 0.005;  // Probability of jumping
    private static final double PROB_OF_DIR_CHANGE = 0.2;  // Probability of changing direction
    private boolean readyToJump;  // Flag to indicate if the monster is ready to jump
    protected boolean isBubbled = false;  // Flag to indicate if the monster is bubbled
    private Point2D heroLocation;  // The location of the hero for AI decisions
    private double maxDistance;  // Maximum distance the monster will travel in one direction

    /**
     * Constructor to initialize a Monster with a given position and size in the level.
     *
     * @param level The level this monster belongs to.
     * @param x The x-coordinate of the monster.
     * @param y The y-coordinate of the monster.
     * @param width The width of the monster.
     * @param height The height of the monster.
     */
    public Monster(Level level, int x, int y, int width, int height) {
        super(level, x, y, width, height);
        this.sprites = new Image[3];  // Placeholder for sprite images
        Random rand = new Random();
        this.direction = (rand.nextBoolean()) ? 1 : -1;  // Random initial direction
        this.maxDistance = 100 + (Math.random() * 100) * this.direction;  // Random max distance for movement
    }

    /**
     * Updates the monster's position based on its current movement state,
     * including jumping and gravity effects.
     */
    @Override
    public void updatePosition() {
        if (!this.isBubbled) {
            this.determineMovement();  // Decide movement based on AI

            // Attempt to jump if ready
            if (this.readyToJump && !this.isJumping && !this.isFalling) {
                this.vy = JUMP_VELOCITY;  // Apply vertical velocity to jump
                this.isJumping = true;
                this.readyToJump = false;
            }

            // Update position based on current direction and gravity
            this.isCollidingLeft = false;
            this.isCollidingRight = false;
            this.positionX += this.direction;  // Move horizontally
            this.currentDistance += this.direction;

            this.isFalling = !(this.vy < 1);  // Check if the monster is falling
            this.vy = this.vy + GRAVITY * DT;  // Apply gravity
            this.positionY += this.vy * DT;  // Move vertically
        } else {
            // If the monster is bubbled, it rises upwards
            if (this.positionY >= 0) {
                this.sprite = this.sprites[2];  // Change sprite to the bubbled state
                this.positionY -= DX;  // Move the monster upwards
            }
        }
    }

    /**
     * Handles the collision of the monster with a platform. The monster changes
     * direction when it collides with a platform.
     *
     * @param p The platform that the monster collided with.
     */
    @Override
    public void collideWithPlatform(Platform p) {
        super.collideWithPlatform(p);  // Call superclass to handle basic platform collision

        if (this.isAlive) {
            // Handle collision on the right side
            if (this.isCollidingRight) {
                this.isCollidingRight = false;
                this.positionX -= PRECISION;  // Adjust position to avoid getting stuck
                this.direction *= -1;  // Change direction
                this.changeSpriteDirection();  // Change sprite direction
                this.currentDistance = 0;  // Reset distance counter
            }

            // Handle collision on the left side
            if (this.isCollidingLeft) {
                this.isCollidingLeft = false;
                this.positionX += PRECISION;  // Adjust position to avoid getting stuck
                this.direction *= -1;  // Change direction
                this.changeSpriteDirection();  // Change sprite direction
                this.currentDistance = 0;  // Reset distance counter
            }
        }
    }

    /**
     * Determines the monster's movement based on the location of the Hero and
     * random probabilities. The monster may jump or change direction periodically.
     */
    public void determineMovement() {
        // Decide if the monster should jump (probability of 0.5%)
        if (this.positionY > (this.heroLocation.getY() + PRECISION) && Math.random() < PROB_OF_JUMP) {
            this.readyToJump = true;
        }

        // If the monster has reached the target distance, change direction
        if (Math.abs(this.currentDistance) >= Math.abs(this.maxDistance)) {
            this.changeDirection();
            this.maxDistance = 100 + (Math.random() * 100) * this.direction;  // Randomize new max distance
            this.currentDistance = 0;  // Reset the current distance
        }
    }

    /**
     * Updates the monster's knowledge of the hero's location.
     *
     * @param heroLocation1 The location of the hero.
     */
    public void updateHeroLocation(Point2D heroLocation1) {
        this.heroLocation = heroLocation1;
    }

    /**
     * Gives the monster a 20% chance of changing direction. The direction is
     * reversed, and the sprite direction is updated accordingly.
     */
    public void changeDirection() {
        this.direction = (Math.random() < PROB_OF_DIR_CHANGE) ? 1 : -1;  // Randomly choose a direction
        this.changeSpriteDirection();  // Change sprite direction to match the new direction
    }

    /**
     * Handles the situation when the monster is hit by a bubble. The monster
     * becomes bubbled and starts rising upwards. After a delay based on its current
     * height, the monster will un-bubble itself.
     */
    public void collideWithBubble() {
        this.isBubbled = true;  // Set the monster to the bubbled state
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Monster.this.isBubbled = false;  // Un-bubble the monster after a delay
            }
        }, 10000 + this.positionY / 4);  // Delay time depends on monster's current height
    }

    /**
     * Handles collision with a FireBubble. The monster dies upon collision,
     * and points are awarded based on the type of FireBubble (Hero or Player 2).
     *
     * @param type The type of FireBubble (1 for Hero, 2 for Player 2).
     * @param level The current level of the game.
     */
    public void collideWithFireBubble(int type, Level level) {
        this.die();  // The monster dies upon FireBubble collision
        if (type == 1) {
            level.score += 1000;  // Award points if Hero's FireBubble hit the monster
        } else {
            level.score2 += 1000;  // Award points if Player 2's FireBubble hit the monster
        }
    }

    /**
     * Changes the sprite direction based on the monster's current movement direction.
     * The sprite is updated to reflect the direction of movement.
     */
    public void changeSpriteDirection() {
        if (this.direction == -1) {
            this.sprite = this.sprites[0];  // Set sprite for left-facing monster
        }
        if (this.direction == 1) {
            this.sprite = this.sprites[1];  // Set sprite for right-facing monster
        }
    }
}
