package BubbleBobble.model;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * The Level class represents a single level in the game. It manages the
 * various game objects, handles collisions, updates positions, and controls
 * the flow of the game level, including whether the game is over or ready
 * to change levels. It also handles adding and removing collidable objects,
 * such as monsters, fruits, platforms, and more.
 */
public class Level {

    // Level states and values
    public Image screen;  // The background image of the level
    public boolean isReadyToChange = false;  // Flag indicating whether the level is ready to transition
    public boolean gameOver = false;  // Flag indicating whether the game is over
    public Point2D heroLocation = new Point2D.Double();  // Hero's current location

    // Lists for game objects
    public ArrayList<Collidable> collidables = new ArrayList<>();  // All collidable objects in the level
    public ArrayList<Collidable> toAdd = new ArrayList<>();  // Objects to be added to the level
    public ArrayList<Collidable> toRemove = new ArrayList<>();  // Objects to be removed from the level
    public ArrayList<Platform> platforms = new ArrayList<>();  // Platforms in the level
    public ArrayList<Bubble> bubbles = new ArrayList<>();  // Bubbles in the level
    public ArrayList<Fireball> fireballs = new ArrayList<>();  // Fireballs in the level
    public ArrayList<FireIcon> fireIcons = new ArrayList<>();  // Fire Icons in the level
    public ArrayList<FireBubble> fireBubbles = new ArrayList<>();  // Fire Bubbles in the level
    public ArrayList<Fruit> fruits = new ArrayList<>();  // Fruits in the level
    public ArrayList<Monster> monsters = new ArrayList<>();  // Monsters in the level
    public ArrayList<Banana> bananas = new ArrayList<>();  // Bananas in the level
    public ArrayList<Apple> apples = new ArrayList<>();  // Apples in the level
    
    // Counter for monster addition tracking
    private int monsterAddCount = 0;

    // The hero in the level
    public Hero hero1;
    // Player scores
    public int score;
    public int score2;

    /**
     * Adds a collidable object to the level.
     *
     * @param c The collidable object to add.
     */
    public synchronized void addCollidable(Collidable c) {
        this.toAdd.add(c);
        c.addToList();  // Add the collidable object to the appropriate list in the game.
    }

    /**
     * Removes a collidable object from the level.
     *
     * @param c The collidable object to remove.
     */
    public synchronized void removeCollidable(Collidable c) {
        this.toRemove.add(c);  // Add the object to the removal list.
    }

    /**
     * Pauses all of the collidable objects in the level.
     */
    public void changePause() {
        for (Collidable c : this.collidables) {
            c.changePause();  // Change the pause state of each collidable object.
        }
    }

    /**
     * Sets the screen image for the level. This is used for the main menu,
     * game won screen, and game over screen. Adds quotes if it's the game over screen.
     *
     * @param image The image to set as the screen.
     * @param isGameScreen Flag indicating whether this is the game screen.
     */
    public void setLevelScreen(Image image, boolean isGameScreen) {
        this.screen = image;
        if (isGameScreen) {
            // Additional logic for game screen (if needed)
        }
    }

    /**
     * Called on each clock tick of the LevelComponent's timer to update the level.
     * This updates the positions of all collidables and checks for collisions.
     */
    public synchronized void timePassed() {

        // Update positions of all monsters and collidables
        for (Monster m : this.monsters) {
            this.heroLocation.setLocation(this.hero1.positionX, this.hero1.positionY);
            m.updateHeroLocation(this.heroLocation);
        }
        for (Collidable c : this.collidables) {
            if (!c.isPaused) {
                c.updatePosition();  // Update position for non-paused objects
            }
        }

        // Remove collidables that are marked for removal
        this.collidables.removeAll(this.toRemove);
        this.monsters.removeAll(this.toRemove);
        this.fireballs.removeAll(this.toRemove);
        this.fireIcons.removeAll(this.toRemove);
        this.fireBubbles.removeAll(this.toRemove);
        this.bubbles.removeAll(this.toRemove);
        this.fruits.removeAll(this.toRemove);
        this.bananas.removeAll(this.toRemove);
        this.apples.removeAll(this.toRemove);

        this.toRemove.clear();  // Clear the removal list
        this.collidables.addAll(this.toAdd);  // Add new collidables
        this.toAdd.clear();  // Clear the addition list

        // Handle collisions after updating positions
        handleCheckCollision();
    }

    /**
     * Checks whether the level is in a playable state.
     * 
     * @return true if the level is playable, false otherwise.
     */
    public boolean isPlayable() {
        return this.screen == null;  // Level is playable if screen is not set (i.e., no special screen like game over or menu)
    }

    /**
     * Updates the state of the level based on game progress. If there are no monsters left,
     * the level is ready to change. If the hero has no lives left, the game over flag is set.
     */
    public boolean overlapping(Collidable c1, Collidable c2) {
        return c1.getShape().intersects(c2.getShape());  // Checks if the collidable objects overlap
    }

    /**
     * Iterates through all collidables to check for collisions, including platform, enemy,
     * bubble, and fruit collisions.
     */
    public void handleCheckCollision() {

        // Check for collisions with platforms
        for (Platform p : this.platforms) {
            if (overlapping(p, this.hero1)) {
                this.hero1.collideWithPlatform(p);
            }

            // Check for platform collisions with other objects (monsters, fruits, etc.)
            for (Monster m : this.monsters) {
                if (overlapping(p, m)) {
                    m.collideWithPlatform(p);
                }
            }
            for (Fruit f : this.fruits) {
                if (overlapping(p, f)) {
                    f.collideWithPlatform(p);
                }
            }
            for (Banana f : this.bananas) {
                if (overlapping(p, f)) {
                    f.collideWithPlatform(p);
                }
            }
            for (Apple f : this.apples) {
                if (overlapping(p, f)) {
                    f.collideWithPlatform(p);
                }
            }
        }

        // Check for collisions with monsters (enemies)
        for (Monster m : this.monsters) {
            if (overlapping(m, this.hero1)) {
                if (m.isBubbled) {
                    this.addCollidable(
                            new Fruit(this, m.positionX, m.positionY, Collidable.SIZE, Collidable.SIZE, m.scoreValue));
                    m.die();
                } else {
                    this.hero1.collideWithEnemy();
                }
            }
        }

        // Check for bubble collisions
        for (Bubble b : this.bubbles) {
            if (overlapping(b, this.hero1)) {
                b.collideWithHero();
                if (this.isPlayable()) {
                    b.setScore(1);
                }
            }

            for (Monster m : this.monsters) {
                if (overlapping(b, m) && b.dx != 0) {
                    b.die();
                    m.collideWithBubble();
                }
            }
        }

        // Handle collisions for fire bubbles, fruits, bananas, apples, and fire icons
        for (FireBubble f : this.fireBubbles) {
            for (Monster m : this.monsters) {
                if (overlapping(f, m) && f.dx != 0) {
                    f.die();
                    m.collideWithFireBubble(f.getType(), this);
                }
            }
        }

        for (Fruit f : this.fruits) {
            if (overlapping(f, this.hero1)) {
                f.collideWithHero();
            }
        }
        for (Fireball f : this.fireballs) {
            if (overlapping(f, this.hero1)) {
                this.hero1.collideWithEnemy();
            }
        }
        for (Banana f : this.bananas) {
            if (overlapping(f, this.hero1)) {
                f.collideWithHero();
            }
        }
        for (Apple f : this.apples) {
            if (overlapping(f, this.hero1)) {
                f.collideWithHero();
            }
        }
        for (FireIcon f : this.fireIcons) {
            if (overlapping(f, this.hero1)) {
                f.collideWithHero();
            }
        }
    }

    /**
     * Starts the timer to check for monsters. Adds new monsters periodically
     * and tracks the number of added monsters.
     *
     * @param t The timer to schedule periodic checks.
     * @param index The current index or stage of the monster check.
     */
    public void startMonsterCheckTimer(javax.swing.Timer t, int index) {
        if (index < 10) {
            Level _this = this;
            if (monsters.size() < 3 && monsterAddCount < 5) {
                addMonster();  // Add two new monsters
                addMonster();
                monsterAddCount++;  // Increment the monster add count
            }
            if (_this.hero1.lives == 0) {
                _this.gameOver = true;
            }
            if (monsterAddCount >= 2) {
                t.stop();  // Stop the timer after two monsters have been added
            }
        }
    }

    /**
     * Adds a new monster to the level, randomly choosing between two types of monsters.
     */
    private void addMonster() {
        Monster newMonster;
        Random random = new Random();
        if (random.nextDouble() >= 0.5) {
            newMonster = new BubbleBuster(this, 150, 500, 35, 35);
        } else {
            newMonster = new Incendo(this, 600, 600, 35, 35);
        }
        addCollidable(newMonster);  // Add the new monster to the level's collidable list
    }
}
