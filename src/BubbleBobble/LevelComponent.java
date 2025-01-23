package BubbleBobble;
import BubbleBobble.model.*;
import BubbleBobble.designPattern.GameObserver;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;

/**
 *
 * LevelComponent has a timer and a current level. LevelComponent is responsible
 * for constructing levels and handling all appropriate GUI updates.
 *
 */
@SuppressWarnings("all")
public class LevelComponent extends JComponent {
	protected static final int NUM_TILES = 20;
	protected static final int TILE_SIZE = 35;
	private static final int NUM_LEVELS = 10; // including game over menu
	private static final int WIN_CONDITION = 10000;

	// Level control stuff
	public Level currentLevel;
	private Timer timer;
	private int levelIndex = 0;
	private boolean ranOnce = false;
	private Timer timer2 = null; // start as null
	// Keyboard input stuff
	private boolean leftKeyState = false;
	private boolean rightKeyState = false;
	private boolean upKeyState = false;

	public String name;

	public  boolean isCheck = false;
	// GUI stuff
	private Font font = new Font("Garamond", Font.BOLD, 20);
	private Color lightRed = new Color(233, 55, 72);
	private Color tangerine = new Color(237, 167, 4);

	// ranking
	private List<Map<String, Object>> scoreboard = new ArrayList<>();


	// GameObserver
	private List<GameObserver> observers = new ArrayList<>();

	// add observer
	public void addObserver(GameObserver observer) {
		observers.add(observer);
	}
	// remove observer
	public void removeObserver(GameObserver observer) {
		observers.remove(observer);
	}
	//notifyObservers
	private void notifyObservers() {
		for (GameObserver observer : observers) {
			observer.updateGameState();
		}
	}

	public LevelComponent() {
		// 4 ms
		this.timer = new Timer(4, e ->{
			timePassed();
		});
	}

	/**
	 * Paints the current level and all of its collidable objects.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (this.currentLevel == null) {
			return;
		}

		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.font);

		// paint black background
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, 700, 700);

		// paint image for screen (only for 'main menu', 'game over', and 'game
		// won' screens)
		if (this.currentLevel.screen != null) {
			g2.drawImage(this.currentLevel.screen, 35, 35, null);
			g2.setColor(this.lightRed);
		}

		// paint all level objects
		for (Collidable c : this.currentLevel.collidables) {
			c.draw(g2); // Draw all collidable objects

			// Check if c is an instance of MyMonster
			if (c instanceof Giant) {
				Giant monster = (Giant) c; // Type cast
				monster.drawAttackCount(g2); // Draw the attack count
			}
		}

		Font chineseFont = new Font("Garamond", Font.PLAIN, 17);
		g2.setFont(chineseFont);
		// paint current lives and score
		g2.setColor(this.tangerine);
		if (this.levelIndex <10) {
			g2.drawString("Bub: "+name,40,20);
			g2.drawString("Lives: " + this.currentLevel.hero1.lives, 40, 40);
			g2.drawString("Score: " + this.currentLevel.score, 40, 60);
			g2.drawString("Level: " + this.levelIndex, 280, 30);
		}

		// scores rank
		if (this.levelIndex == 10 || this.levelIndex == 11) {
			// Set the font and color
			g2.setFont(new Font("Garamond", Font.PLAIN, 25));
			g2.setColor(Color.WHITE);
			int startX = 220;
			int startY = 220;
			int rowHeight = 65;
			Color headerBgColor = new Color(108, 168, 229);
			Color rowBgColor = new Color(240, 240, 240);
			Color borderColor = Color.BLACK;

			g2.setColor(headerBgColor);
			g2.fillRect(startX - 40, startY - 25, 340, rowHeight);
			g2.setColor(Color.WHITE);

			g2.drawString("r", startX - 30, startY);
			g2.drawString("avatar", startX, startY);
			g2.drawString("name", startX + 100, startY);
			g2.drawString("score", startX + 200, startY);
			//Sort scoreboards in descending order of scores
			scoreboard = scoreboard.stream()
					.distinct()
					.sorted((o1, o2) -> {
						return Integer.compare((Integer) o2.get("score"),
								(Integer) o1.get("score"));
					})
					.limit(5)
					.collect(Collectors.toList());
			for (int i = 0; i < scoreboard.size(); i++) {
				int currentY = startY + (i + 1) * rowHeight;
				// Draw the row background
				g2.setColor(rowBgColor);
				g2.fillRect(startX - 40, currentY - 25, 340, rowHeight);
				// Set the text color
				g2.setColor(new Color(132, 110, 212));
				Map<String, Object> scoreData = scoreboard.get(i);

				// Draw the ranking, player avatar, name, and score
				g2.drawString(String.valueOf(i + 1), startX - 30, currentY); // Rank
				String playerName = (String) scoreData.get("playerName");
				try {
					BufferedImage avatar = ImageIO.read(new File("avatars/"+(this.name==playerName? "hero1" : playerName)+".png"));  
					Image scaledAvatar = avatar.getScaledInstance(40, 40, Image.SCALE_SMOOTH);  
					g2.drawImage(scaledAvatar, startX, currentY - 15, null);  
				} catch (IOException e) {}
				g2.drawString(playerName, startX + 100, currentY);
				g2.drawString(scoreData.get("score").toString(), startX + 200, currentY);

				// Draw the row border
				g2.setColor(borderColor);
				g2.drawRect(startX - 40, currentY - 25, 340, rowHeight);
			}
			g2.setColor(borderColor);
			g2.drawRect(startX - 40, startY - 25, 340, rowHeight);
		}

	}

	/**
	 *
	 * Draws a string onto a Graphics2D object, centered within the given
	 * rectangle.
	 *
	 * @param g2
	 * @param text
	 * @param rect
	 */
	public void drawStringCentered(Graphics2D g2, String text, Rectangle2D rect) {
		FontMetrics metrics = g2.getFontMetrics(this.font);
		double x = rect.getX() + (rect.getWidth() - metrics.stringWidth(text)) / 2;
		double y = rect.getY() + ((rect.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
		g2.drawString(text, (int) x, (int) y);
	}

	public boolean checkGameOver() {
		return this.currentLevel.gameOver;
	}

	/**
	 *
	 * Loads the appropriate level.
	 *
	 */
	public void loadLevel() {
		// main menu
		if (this.levelIndex == 0) {
			this.currentLevel = constructLevel(this.levelIndex);
			if (!this.timer.isRunning()) {
				this.timer.start();
				AudioManager.getInstance().play("audio/bubbleBobble.mid");
			}
		}
		// game won level
		else if (this.levelIndex == NUM_LEVELS + 1) {
			int oldScore = this.currentLevel.score;
			int oldLives = this.currentLevel.hero1.lives;
			this.currentLevel = constructLevel(this.levelIndex);
			this.currentLevel.gameOver = true;
			this.ranOnce = true;
			this.currentLevel.score += oldScore;
			this.currentLevel.hero1.lives = oldLives;
			this.currentLevel.monsters.stream().forEach(Monster::die);
		}

		// game over menu
		else if (this.levelIndex == NUM_LEVELS) {
			int oldScore = this.currentLevel.score;
			int oldLives = this.currentLevel.hero1.lives;
			int oldScore2 = this.currentLevel.score2;

			this.currentLevel = constructLevel(this.levelIndex);
			this.currentLevel.gameOver = true;
			this.currentLevel.score += oldScore;
			this.currentLevel.hero1.lives = oldLives;

			this.currentLevel.monsters.forEach(Monster::die);
		}

		// Levels 1-9
		else {
			int oldScore = this.currentLevel.score;
			int oldLives = this.currentLevel.hero1.lives;
			int oldScore2 = this.currentLevel.score2;
			this.currentLevel = constructLevel(this.levelIndex);
			this.currentLevel.score += oldScore;
			this.currentLevel.hero1.lives = oldLives;
			this.currentLevel.score2 += oldScore2;

		}
	}
	String[] names = {"Alex", "Chris", "Jordan", "Taylor", "Morgan", "Sam", "Jamie", "Riley", "Casey", "Peyton"};
	/**
	 *
	 * Updates the current level index based on a command:
	 *
	 * command = 1 is increment the level by one. command = -1 is decrement the
	 * level by one. command = 0 is load the main menu. command = NUM_LEVELS is
	 * loads the game over screen
	 *
	 * Levels will loop through levels 1-9 when scrolling through with 'u' and
	 * 'd'.
	 *
	 * @param command
	 */
	public void changeLevel(int command) {
		// change to game over menu
		if ((command >= 10) && (command <= 11)) {
			this.levelIndex = command;
			this.loadLevel();
			isCheck = false;
			// Record the score when the game ends
			Random random = new Random();
			Map<String, Object> player1 = new HashMap<>();
			player1.put("playerName", name);
			player1.put("score", this.currentLevel.score);
			player1.put("status", (command == 11) ? "Win" : "Lose");
			scoreboard.add(player1);
			for (int i = 0; i < 5; i++) {
				Map<String, Object> randomPlayer = new HashMap<>();
				String randomName = names[random.nextInt(names.length)];
				randomPlayer.put("playerName", randomName);
				int randomScore = random.nextInt(901) * 100+1000;
				randomPlayer.put("score", randomScore);
				String randomStatus = random.nextBoolean() ? "Win" : "Lose";
				randomPlayer.put("status", randomStatus);
				scoreboard.add(randomPlayer);
			}
		}
		// change to main menu
		else if (command == 0) {
			this.levelIndex = 0;
			this.ranOnce = false;
			this.currentLevel.gameOver = false;
			this.currentLevel.score = 0;
			this.loadLevel();
		}

		// increment level
		else if (command == 1) {
			// Stop the previous timer
			if (timer2 != null && timer2.isRunning()) {
				timer2.stop();
			}
			timer2 = new Timer(1000, e -> {
				this.currentLevel.startMonsterCheckTimer(timer2,levelIndex); 
			});
			timer2.start();
			isCheck = true;
			this.levelIndex++;
			// change to game won level if score is bigger than 10000
			if (this.levelIndex == NUM_LEVELS && this.currentLevel.score > WIN_CONDITION) {
				this.levelIndex++;
				this.loadLevel();
			} else {
				if (this.levelIndex > NUM_LEVELS - 1) {
					this.levelIndex = 1;
				}
				this.loadLevel();
			}
		}

		// decrement level
		else if (command == -1) {
			// Stop the previous timer
			if (timer2 != null && timer2.isRunning()) {
				timer2.stop();
			}
			timer2 = new Timer(1000, e -> {
				this.currentLevel.startMonsterCheckTimer(timer2,levelIndex); 
			});
			isCheck = true;
			timer2.start();
			this.levelIndex--;
			if (this.levelIndex < 1) {
				this.levelIndex = NUM_LEVELS - 1;
			}
			this.loadLevel();
		}else {
			this.levelIndex = command;
			this.loadLevel();
		}
	}

	/**
	 *
	 * Tells the current level to update. Repaints the component. TimePassed
	 * will be called every tick of the timer.
	 *
	 */
	public void timePassed() {
		this.handleMoveHero();
		this.currentLevel.timePassed();

		// Decide whether to notify observers
		if (this.currentLevel.isReadyToChange && !this.checkGameOver() && this.currentLevel.screen == null) {
			this.changeLevel(1);
			notifyObservers();  // Notify observers
		}
		if (isCheck){  //Initially false
			if (this.currentLevel.monsters.size()==0){
				if (this.levelIndex==9) this.changeLevel(11);
				else this.changeLevel(this.levelIndex+1);
			}
			 if (this.currentLevel.hero1.lives==0){
				this.changeLevel(10);
			}
		}
		// ranOnce is set to true to prevent this if statement from running more
		// than once
		if (this.checkGameOver() && !this.ranOnce) {
			this.changeLevel(NUM_LEVELS);
			this.ranOnce = true;
		}
		repaint(100);
	}

	/**
	 *
	 * Tells hero to move based on the button state of the arrow keys.
	 *
	 */
	public void handleMoveHero() {
		if (this.leftKeyState) {
			this.currentLevel.hero1.move("Left");
		}
		if (this.rightKeyState) {
			this.currentLevel.hero1.move("Right");
		}
		if (this.upKeyState) {
			this.currentLevel.hero1.move("Up");
		}
	}

	/**
	 *
	 * Tells hero to shoot a bubble.
	 *
	 */
	public void handleBubble(int type) {
		switch (type){
			case 1:
				this.currentLevel.hero1.shootBubble();
				break;
			case 2:
//				if (this.currentLevel.hero2!=null)
//					this.currentLevel.hero2.shootBubble();
				break;
		}
	}
	public void handleFireball(int type) {
		switch (type){
			case 1:
				this.currentLevel.hero1.shootFireBubble(1);
				break;
			case 2:
//				if (this.currentLevel.hero2!=null)
//					this.currentLevel.hero2.shootFireBubble(2);
				break;
		}
	}

	/**
	 *
	 * Set the state of the given key to the given state.
	 *
	 * @param key
	 * @param state
	 */
	public void setKeyState(String key, boolean state) {
		if (key.equals("Left")) {
			this.leftKeyState = state;
		}
		if (key.equals("Right")) {
			this.rightKeyState = state;
		}
		if (key.equals("Up")) {
			this.upKeyState = state;
		}
	}

	/**
	 *
	 * Takes in a level number and constructs the corresponding level using a
	 * file scanner.
	 *
	 * @param levelNum
	 * @return a newly constructed Level
	 */
	public Level constructLevel(int levelNum) {
		try {
			Level level = new Level();
			File level1 = new File("levels/level" + levelNum + ".txt");
			Scanner r1 = new Scanner(level1);
			int currentRow = 0;
			while (r1.hasNextLine()) {
				String currentLine = r1.nextLine();
				for (int i = 0; i < NUM_TILES; i++) {
					// tiny platform (1 space)
					if (currentLine.charAt(i) == '7') {
						level.addCollidable(new Platform(level, TILE_SIZE * i, TILE_SIZE * currentRow, TILE_SIZE,
								TILE_SIZE, (new ImageIcon("sprites/tinyPlatform2.png")).getImage()));
					}
					// small platform (2 spaces)
					if (currentLine.charAt(i) == 'S') {
						level.addCollidable(new Platform(level, TILE_SIZE * i, TILE_SIZE * currentRow, 2 * TILE_SIZE,
								TILE_SIZE, (new ImageIcon("sprites/smallPlatform2.png")).getImage()));
					}
					// medium platform (3 spaces)
					if (currentLine.charAt(i) == 'M') {
						level.addCollidable(new Platform(level, TILE_SIZE * i, TILE_SIZE * currentRow, 3 * TILE_SIZE,
								TILE_SIZE, (new ImageIcon("sprites/mediumPlatform2.png")).getImage()));
					}
					// large platform (5 spaces)
					if (currentLine.charAt(i) == 'L') {
						level.addCollidable(new Platform(level, TILE_SIZE * i, TILE_SIZE * currentRow, 5 * TILE_SIZE,
								TILE_SIZE, (new ImageIcon("sprites/bigPlatform2.png")).getImage()));
					}
					// floor platform
					if (currentLine.charAt(i) == 'F') {
						level.addCollidable(new Platform(level, TILE_SIZE * i, TILE_SIZE * currentRow,
								NUM_TILES * TILE_SIZE, TILE_SIZE, (new ImageIcon("sprites/floor2.png")).getImage()));
					}
					// wall platform
					if (currentLine.charAt(i) == 'W') {
						level.addCollidable(new Platform(level, TILE_SIZE * i, TILE_SIZE * currentRow, TILE_SIZE,
								NUM_TILES * TILE_SIZE, (new ImageIcon("sprites/wall2.png")).getImage()));
					}
					// hero
					if (currentLine.charAt(i) == 'H') {
						level.addCollidable(new Hero(level, TILE_SIZE * i, TILE_SIZE * currentRow, TILE_SIZE, TILE_SIZE));
					}
					// BubbleBuster
					if (currentLine.charAt(i) == 'B') {
						level.addCollidable(
								new BubbleBuster(level, TILE_SIZE * i, TILE_SIZE * currentRow, TILE_SIZE, TILE_SIZE));
					}
					// Incendo
					if (currentLine.charAt(i) == 'I') {
						level.addCollidable(
								new Incendo(level, TILE_SIZE * i, TILE_SIZE * currentRow, TILE_SIZE, TILE_SIZE));
					}
					if (currentLine.charAt(i) == '+') {
						level.addCollidable(
								new Giant(level, TILE_SIZE * i, TILE_SIZE * currentRow, TILE_SIZE, TILE_SIZE));
					}
					// Fireicon -A
					if (currentLine.charAt(i) == 'A') {
						level.addCollidable(
								new FireIcon(level, TILE_SIZE * i, TILE_SIZE * currentRow, TILE_SIZE, TILE_SIZE));
					}
					if (currentLine.charAt(i) == '9') { //Banana
						level.addCollidable(new Banana(level, TILE_SIZE * i, TILE_SIZE * currentRow, TILE_SIZE, TILE_SIZE));
					}
					if (currentLine.charAt(i) == 'C') { //Apple
						level.addCollidable(new Apple(level, TILE_SIZE * i, TILE_SIZE * currentRow, TILE_SIZE, TILE_SIZE));
					}
					// Game Over level
					if (currentLine.charAt(i) == 'T') {
						level.setLevelScreen((new ImageIcon("images/game_over.png")).getImage(), true);
					}
					// Main Menu level
					if (currentLine.charAt(i) == 't') {
						level.setLevelScreen((new ImageIcon("images/main_menu.png")).getImage(), false);
					}
					// Game Won level
					if (currentLine.charAt(i) == 'Z') {
						level.setLevelScreen((new ImageIcon("images/game_won.png")).getImage(), false);
					}
					// Spawn my boi Rascal
					if (currentLine.charAt(i) == 'R') {
						level.addCollidable(
								new Fruit(level, TILE_SIZE * i, TILE_SIZE * currentRow, TILE_SIZE, TILE_SIZE, 69));
					}
				}
				currentRow++;
			}
			r1.close();
			return level;

		} catch (IOException e) {
			System.err.println("File not found");

		}
		return null;
	}
}