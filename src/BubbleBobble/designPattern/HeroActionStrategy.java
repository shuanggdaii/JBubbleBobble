package BubbleBobble.designPattern;

import BubbleBobble.model.Hero;

/**
 * Strategy interface for defining different actions a hero can perform.
 * Implementations of this interface define specific behaviors for the hero's actions.
 */
public interface HeroActionStrategy {

    /**
     * Defines how the hero moves based on the given direction.
     * This method should be implemented with specific movement behavior.
     * 
     * @param hero The hero that will perform the movement.
     * @param direction The direction in which the hero should move (e.g., "Left", "Right", "Up").
     */
    void move(Hero hero, String direction);

    /**
     * Defines how the hero shoots a bubble.
     * This method should be implemented with specific bubble-shooting behavior.
     * 
     * @param hero The hero that will shoot the bubble.
     */
    void shootBubble(Hero hero);

    /**
     * Defines how the hero shoots a fire bubble.
     * This method should be implemented with specific fire bubble behavior.
     * 
     * @param hero The hero that will shoot the fire bubble.
     * @param type The type of fire bubble to be shot (e.g., different kinds of fireballs).
     */
    void shootFireBubble(Hero hero, int type);
}
