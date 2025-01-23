package BubbleBobble.designPattern;

import BubbleBobble.model.Bubble;
import BubbleBobble.model.FireBubble;
import BubbleBobble.model.Hero;

/**
 * Defines the normal hero actions for moving and shooting in the game.
 * This class implements the {@link HeroActionStrategy} interface and provides
 * behavior for a regular hero, such as movement and shooting bubbles or fire bubbles.
 * 
 * The normal hero is able to move left, right, jump, and shoot bubbles or fire bubbles
 * while alive and not paused.
 */
public class NormalHeroAction implements HeroActionStrategy {

    /**
     * Defines how the normal hero moves.
     * The hero can move left, right, or jump depending on the direction provided.
     * Movement is only allowed if the hero is alive, not paused, and not colliding with boundaries.
     * 
     * @param hero The hero that will move.
     * @param direction The direction to move the hero in ("Left", "Right", "Up").
     */
    @Override
    public void move(Hero hero, String direction) {
        if (hero.lives == 0) {
            hero.isAlive = false; // If the hero is dead, mark them as not alive
            return;
        }
        if (hero.isAlive && !hero.isPaused) {
            if (direction.equals("Right") && !hero.isCollidingRight) {
                hero.positionX += hero.DX;  // Move right
                hero.direction = 1; // Set the direction to right
            }
            if (direction.equals("Left") && !hero.isCollidingLeft) {
                hero.positionX -= hero.DX;  // Move left
                hero.direction = -1; // Set the direction to left
            }
            if (direction.equals("Up") && !hero.isJumping && !hero.isFalling) {
                hero.vy = hero.JUMP_VELOCITY; // Initiate jump with velocity
                hero.isJumping = true; // Mark as jumping
            }
        }
    }

    /**
     * Defines how the normal hero shoots a bubble.
     * The hero can shoot a bubble if they are alive and not paused.
     * The bubble is created at the hero's position, with the hero's direction.
     * 
     * @param hero The hero who will shoot the bubble.
     */
    @Override
    public void shootBubble(Hero hero) {
        if (hero.isAlive && !hero.isPaused) {
            int x = hero.positionX + ((hero.width + hero.PRECISION) * hero.direction); // Calculate bubble's spawn position
            Bubble bubble = new Bubble(hero.level, x, hero.positionY, hero.direction); // Create a new bubble
            hero.level.addCollidable(bubble); // Add bubble to the level
        }
    }

    /**
     * Defines how the normal hero shoots a fire bubble.
     * The hero can shoot a fire bubble if they are alive and not paused.
     * The fire bubble is created at the hero's position and its type is set.
     * 
     * @param hero The hero who will shoot the fire bubble.
     * @param type The type of fire bubble to shoot (indicating which hero shot it).
     */
    @Override
    public void shootFireBubble(Hero hero, int type) {
        if (hero.isAlive && !hero.isPaused) {
            int x = hero.positionX + ((hero.width + hero.PRECISION) * hero.direction); // Calculate fire bubble's spawn position
            FireBubble fireBubble = new FireBubble(hero.level, x, hero.positionY, hero.direction); // Create a new fire bubble
            fireBubble.setType(type); // Set the type of fire bubble based on hero
            hero.level.addCollidable(fireBubble); // Add fire bubble to the level
        }
    }
}
