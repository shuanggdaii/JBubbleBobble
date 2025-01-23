package BubbleBobble.model;

/**
 * The Projectile class serves as a superclass for various projectile types, such as bubbles and fireballs.
 * Projectiles travel horizontally for a specific distance and interact with other collidable objects.
 */
public abstract class Projectile extends Collidable {

    // Maximum distance the projectile can travel
    private static final int MAX_DISTANCE = 125;
    
    // Horizontal and vertical movement speeds
    protected double dx = 3;
    protected double dy = 0;

    /**
     * Constructor to initialize a projectile with a given level, position, and direction.
     * 
     * @param level The level to which the projectile belongs.
     * @param x The x-coordinate of the projectile's initial position.
     * @param y The y-coordinate of the projectile's initial position.
     * @param dir The direction in which the projectile will travel (1 for right, -1 for left).
     */
    public Projectile(Level level, int x, int y, int dir) {
        super(level, x, y, SIZE, SIZE);  // Initialize the parent Collidable class with size and level info
        this.direction = dir;  // Set the direction of the projectile
        this.dx = this.dx * this.direction;  // Adjust the horizontal speed based on the direction
        this.width = SIZE;  // Set the width of the projectile
        this.height = SIZE;  // Set the height of the projectile
    }

    /**
     * Updates the position of the projectile based on its movement speed (dx, dy).
     * The projectile will stop moving if it has traveled beyond its maximum distance.
     */
    @Override
    public void updatePosition() {
        // Stop the projectile if it exceeds the maximum travel distance
        if (Math.abs(this.currentDistance) >= MAX_DISTANCE) {
            this.dx = 0;  // Stop horizontal movement if max distance is reached
        }
        
        // Update the position of the projectile
        this.positionX += this.dx;  // Update the x-coordinate
        this.currentDistance += this.dx;  // Track the distance traveled
        this.positionY += this.dy;  // Update the y-coordinate
    }
}

