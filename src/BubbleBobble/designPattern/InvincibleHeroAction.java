package BubbleBobble.designPattern;

import BubbleBobble.model.Hero;

/**
 * Implementation of the HeroActionStrategy for an invincible hero.
 * This strategy defines how an invincible hero behaves when performing actions.
 * 
 * Invincible heroes might have different movement logic or behaviors, but in
 * this case, their actions are the same as the normal hero, and the logic 
 * could be extended if needed in the future.
 */
public class InvincibleHeroAction implements HeroActionStrategy {

    /**
     * Defines how the invincible hero moves.
     * In this case, the invincible hero uses the normal move behavior.
     * 
     * @param hero The hero who will perform the movement.
     * @param direction The direction in which the hero should move (e.g., "Left", "Right", "Up").
     */
    @Override
    public void move(Hero hero, String direction) {
        // Invincible heroes can move but with different logic (if needed)
        hero.move(direction); // Calls the normal movement method of the hero
    }

    /**
     * Defines how the invincible hero shoots a bubble.
     * The invincible hero uses the same bubble-shooting behavior as a normal hero.
     * 
     * @param hero The hero who will shoot the bubble.
     */
    @Override
    public void shootBubble(Hero hero) {
        hero.shootBubble(); // Calls the normal bubble-shooting method of the hero
    }

    /**
     * Defines how the invincible hero shoots a fire bubble.
     * The invincible hero uses the normal fire bubble shooting behavior.
     * 
     * @param hero The hero who will shoot the fire bubble.
     * @param type The type of fire bubble to be shot (e.g., different fireball types).
     */
    @Override
    public void shootFireBubble(Hero hero, int type) {
        hero.shootFireBubble(type); // Calls the normal fire bubble shooting method of the hero
    }
}

