package BubbleBobble.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

/**
 * 
 * The Collidable class is the superclass for all "collidable" objects in the game. Collidables are affected by
 * gravity and store information about their location, graphics, and current state. Collidables can move around
 * on screen and collide with each other.
 */
public abstract class Collidable {
	
	// Constant values for collision detection and object properties
	public static final double DT = 0.02; //differential time for calculating speeds
	public static final int PRECISION = 6; //precision in detecting collisions
	public static final double GRAVITY = 60;
	public static final double JUMP_VELOCITY = -100;
	public static final int DX = 1;
	public static final int SIZE = 35;
	
	// Instance variables for use by character-like objects (e.g., Hero, BubbleBuster)
    public double vy = 0; // Vertical velocity
    public int direction = 1; // Direction of movement (-1 = left, 1 = right)
    public double currentDistance = 0; // Tracks distance traveled for enemies
    public boolean isJumping = false; // Flag indicating if the object is jumping
    public boolean isFalling = true; // Flag indicating if the object is falling
    public boolean isCollidingLeft = false; // Flag indicating collision on the left side
    public boolean isCollidingRight = false; // Flag indicating collision on the right side

    // Instance variables for all Collidable objects
    public Level level; // The level the object belongs to
    public Image sprite; // The image representing the object
    public Image sprites[]; // Array of sprite images for the object
    public int positionX; // X-coordinate of the object's position
    public int positionY; // Y-coordinate of the object's position
    public int width; // Width of the object
    public int height; // Height of the object
    public Color color; // Color of the object
    public int scoreValue; // Score value awarded when the object is interacted with
    public boolean isPaused = false; // Flag indicating if the object is paused
    public boolean isAlive = true; // Flag indicating if the object is alive

    /**
     * Constructs a new Collidable object with the specified level, position, and size.
     *
     * @param level The level the object is part of.
     * @param x The X-coordinate of the object's position.
     * @param y The Y-coordinate of the object's position.
     * @param width The width of the object.
     * @param height The height of the object.
     */
    public Collidable(Level level, int x, int y, int width, int height) {
        this.level = level;
        this.positionX = x;
        this.positionY = y;
        this.width = width;
        this.height = height;
    }
	
	/**
	 * 
	 * Draws the collidable onto the given graphics object.
	 *
	 * @param g is graphics object
	 */
	public void draw(Graphics2D g) {
		g.drawImage(this.sprite, this.positionX, this.positionY, null);
	}
	
	// Mainly for use in the design phase (as well as object colors)
	public Rectangle2D getShape() {
		return new Rectangle2D.Double(this.positionX, this.positionY, this.width, this.height);
	}
	
	public void changePause() {
		this.isPaused = !this.isPaused;
	}
	
	public void die(){
		this.level.removeCollidable(this);
	}
	
	/**
	 * 
	 * For use by character-like collidables (i.e. Hero, Monster, etc.). Detects top, right, and left collisions
	 * with platform objects.
	 *
	 * @param p is Platform object
	 */
	public void collideWithPlatform(Platform p) {
		double bottomY = this.positionY + this.height;
		double rightX = this.positionX + this.width;
		
		if(this.isAlive) {
			
			//top collision
			if (((bottomY < (p.positionY + PRECISION)) && (bottomY > p.positionY) && this.isFalling)) {
				this.vy = 0;
				this.isJumping = false;
			}
			
			//right collision
			if (rightX > p.positionX && rightX < (p.positionX + PRECISION) && !(this.positionY < p.positionY)) {
				this.isCollidingRight = true;
			}
			
			//left collision
			if (this.positionX < (p.positionX + p.width) && this.positionX > (p.positionX + p.width - PRECISION)
					&& !(this.positionY < p.positionY)) {
				this.isCollidingLeft = true;
			}
		}
	}
	/**
     * Abstract method for updating the position of the collidable object.
     * This method should be implemented by subclasses to define specific movement logic.
     */
    public abstract void updatePosition();

    /**
     * Abstract method for adding the collidable object to the level's list of objects.
     * This method should be implemented by subclasses to properly register the object.
     */	
	public abstract void addToList();
}
