package BubbleBobble.model;

import BubbleBobble.controller.GameController;
import BubbleBobble.view.GameView;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * FireIcon represents an item that can be collected by the hero in the game.
 * When the hero collects the FireIcon, it grants the hero the ability to shoot fireballs for a limited time.
 */
public class FireIcon extends Collidable {

    private boolean heroCanTouch;

    /**
     * Constructs a new FireIcon at the specified position and size.
     * The FireIcon starts as not interactable, but after a grace period, it can be collected by the hero.
     * 
     * @param level The level in which the FireIcon exists.
     * @param x The X-coordinate of the FireIcon.
     * @param y The Y-coordinate of the FireIcon.
     * @param width The width of the FireIcon.
     * @param height The height of the FireIcon.
     */
    public FireIcon(Level level, int x, int y, int width, int height) {
        super(level, x, y, width, height);
        this.heroCanTouch = false;
        this.sprite = (new ImageIcon("sprites/fire.png")).getImage();

        // Allow a grace period before the FireIcon can be touched by the hero
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                FireIcon.this.heroCanTouch = true;
            }
        }, 500);
    }

    /**
     * Updates the position of the FireIcon. Since the FireIcon does not move, this method effectively does nothing.
     * It ensures the vertical velocity is zero and the FireIcon is no longer falling.
     */
    @Override
    public void updatePosition() {
        this.vy = 0;                     // Stops vertical speed
        this.isFalling = false;          // Marks as no longer falling
    }

    /**
     * Adds the FireIcon to the level's list of fire icons.
     * This allows the FireIcon to be tracked and interactable within the level.
     */
    @Override
    public void addToList() {
        this.level.fireIcons.add(this);
    }

    /**
     * Handles the collision of the FireIcon with the hero.
     * When the hero collects the FireIcon, it activates fireball mode, allowing the hero to shoot fireballs.
     */
    public void collideWithHero() {
        if (this.heroCanTouch) {
            // Only allow interaction if the level is playable
            if (this.level.isPlayable()) {
                // Activates the fireball mode for the hero
                GameController.BubbleKeyListener.activateFireballMode();
            }
            // Destroy the FireIcon after collection
            this.die();
            // Display message indicating the ability to shoot fireballs
            GameView.showMessage("add fire attack to bubble");
        }
    }
}

