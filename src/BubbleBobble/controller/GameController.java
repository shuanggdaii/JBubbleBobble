package BubbleBobble.controller;

import BubbleBobble.LevelComponent;
import BubbleBobble.designPattern.InvincibleHeroAction;
import BubbleBobble.designPattern.NormalHeroAction;
import BubbleBobble.view.GameView;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The GameController class manages the interactions between the game view and the level component.
 * It initializes the game, sets up keyboard controls, and updates hero status based on events.
 */
public class GameController {
    private GameView view;
    private LevelComponent level;
    
    /**
     * Creates a new GameController to manage the given game view and level component.
     *
     * @param view the GameView to display the game interface.
     * @param level the LevelComponent to manage game levels and hero actions.
     */
    public GameController(GameView view, LevelComponent level) {
        this.view = view;
        this.level = level;
        initialize();
    }
    
    /**
     * Initializes the game controller by setting up the view's title and adding a keyboard listener.
     */
    private void initialize() {
        view.addExitButton(e -> System.exit(1));
        view.setTitle("BubbleBobble Game");

     // Add a keyboard listener
        view.getMainFrame().addKeyListener(new BubbleKeyListener(level));
    }
    
    /**
     * Starts the game by displaying the game view.
     */
    public void startGame() {
        view.show();
    }

    // Implementation of BubbleKeyListener
    /**
     * Handles keyboard input for controlling the hero's movement and actions.
     */
	public static class BubbleKeyListener implements KeyListener {
        private LevelComponent comp;
		private static boolean isFireballMode = false; //Indicates whether it's fireball mode

			public BubbleKeyListener(LevelComponent comp) {
				this.comp = comp;
			}
			
			/**
			 * Responds to key press events to move the hero or initiate actions.
			 *
			 * @param e the KeyEvent triggered by a key press.
			 */
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					this.comp.currentLevel.hero1.move("Right");
					this.comp.currentLevel.hero1.sprite =(new ImageIcon("sprites/hero_right.gif")).getImage();
					this.comp.setKeyState("Right", true);
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					this.comp.currentLevel.hero1.move("Left");
					this.comp.setKeyState("Left", true);
					this.comp.currentLevel.hero1.sprite = (new ImageIcon("sprites/hero_left.gif")).getImage();
				}
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					this.comp.setKeyState("Up", true);
					this.comp.handleMoveHero();
				}
			}
			
			/**
			 * Responds to key release events to stop movement or execute special actions.
			 *
			 * @param e the KeyEvent triggered by a key release.
			 */
			@Override
			public void keyReleased(KeyEvent e) {
				char key = e.getKeyChar();
				if (key == 'u' && !this.comp.checkGameOver()) {
					this.comp.changeLevel(1);
				}
				if (key == 'd' && !this.comp.checkGameOver()) {
					this.comp.changeLevel(-1);
				}
				if (key == 'p') {
					this.comp.currentLevel.changePause();
				}
				if (key == 'm') {
					this.comp.changeLevel(0);
				}
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					this.comp.setKeyState("Up", false);
					this.comp.handleMoveHero();
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					this.comp.setKeyState("Left", false);
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					this.comp.setKeyState("Right", false);
				}
				if (e.getKeyCode() == KeyEvent.VK_SPACE){
					if (isFireballMode) {
						this.comp.handleFireball(1);
					} else {
						this.comp.handleBubble(1);
					}
				}
				if (key >= '1' && key <= '9') { // change levels from keyboard
					this.comp.changeLevel(Character.getNumericValue(key));
				}
			}
			
			@Override
			public void keyTyped(KeyEvent e) {
				// Not used
			}
			
			/**
			 * Activates fireball mode for the hero, allowing them to shoot fireballs.
			 * Fireball mode lasts for 1 minute and then automatically deactivates.
			 */
		    // Activate fireball mode when collision is detected
			public static void activateFireballMode() {
				isFireballMode = true;
				// Use a timer to reset fireball mode after 1 minute
				Timer timer = new Timer(60000, e -> {
					isFireballMode = false;
				});
				timer.setRepeats(false); // Timer triggers only once
				timer.start();
			}
		}

	/**
	 * Checks the hero's current status (e.g., invincibility) and sets the appropriate action strategy.
	 */
	public void checkHeroStatus() {
		if (this.level.currentLevel.hero1.invincible) {
			this.level.currentLevel.hero1.setActionStrategy(new InvincibleHeroAction());
		} else {
			this.level.currentLevel.hero1.setActionStrategy(new NormalHeroAction());
		}

//		if (this.level.currentLevel.hero2 !=null){
//			if (this.level.currentLevel.hero2.invincible) {
//				this.level.currentLevel.hero2.setActionStrategy(new InvincibleHeroAction());
//			} else {
//				this.level.currentLevel.hero2.setActionStrategy(new NormalHeroAction());
//			}
//		}
	}
}