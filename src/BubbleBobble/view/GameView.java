package BubbleBobble.view;

import BubbleBobble.LevelComponent;
import BubbleBobble.designPattern.GameObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The GameView class is responsible for managing the graphical user interface (GUI) of the game.
 * It observes the LevelComponent and updates the view accordingly based on changes in the game state.
 * This includes displaying the main game window, title, buttons, and handling transitions such as fading messages.
 */
public class GameView implements GameObserver {

    /**
     * The screen size of the user's device.
     */
    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    /**
     * The width of the screen.
     */
    public static final int SCREEN_WIDTH = SCREEN_SIZE.width;

    /**
     * The height of the screen.
     */
    public static final int SCREEN_HEIGHT = SCREEN_SIZE.height;

    /**
     * The height of the title panel.
     */
    public static final int TITLE_HEIGHT = 50;

    /**
     * The size of the game panel.
     */
    public static final int PANEL_SIZE = 700;

    /**
     * The X-coordinate of the top-left corner of the game panel, positioned in the center of the screen.
     */
    public static final int PANEL_X = (SCREEN_WIDTH / 2) - (PANEL_SIZE / 2);

    /**
     * The Y-coordinate of the top-left corner of the game panel, positioned just below the title.
     */
    public static final int PANEL_Y = (SCREEN_HEIGHT / 2) - (PANEL_SIZE / 2) - TITLE_HEIGHT;

    private JFrame mainFrame;
    private LevelComponent levelComponent;
    private JPanel titlePanel;
    private JPanel buttonPanel;

    /**
     * Constructs the GameView object and initializes the main frame, panels, and registers as an observer
     * to receive updates on the game state.
     *
     * @param component the LevelComponent instance that represents the game level.
     */
    public GameView(LevelComponent component) {
        this.levelComponent = component;
        initialize();
        // Register as an observer
        this.levelComponent.addObserver(this);
    }

    /**
     * Initializes the main game window and layout.
     */
    private void initialize() {
        mainFrame = new JFrame("BubbleBobble");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocation(PANEL_X, PANEL_Y);
        mainFrame.setSize(715, 800);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setFocusable(true);

        titlePanel = new JPanel();
        buttonPanel = new JPanel();

        mainFrame.add(this.levelComponent, BorderLayout.CENTER);
        mainFrame.add(titlePanel, BorderLayout.NORTH);
        mainFrame.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets the title of the game in the title panel.
     *
     * @param title the title text to display in the title panel.
     */
    public void setTitle(String title) {
        JLabel titleLabel = new JLabel(title);
        titlePanel.add(titleLabel);
    }

    /**
     * Displays the game window and loads the level.
     */
    public void show() {
        this.levelComponent.loadLevel();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    /**
     * Adds an exit button to the button panel.
     * The exit button allows the user to exit the game when clicked.
     *
     * @param actionListener the action listener to handle the exit button click event.
     */
    public void addExitButton(ActionListener actionListener) {
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(actionListener);
        buttonPanel.add(exitButton);
    }

    /**
     * Returns the main frame of the game.
     *
     * @return the main JFrame of the game.
     */
    public JFrame getMainFrame() {
        return this.mainFrame;
    }

    /**
     * Updates the game state. This method reloads the level when the game state changes.
     */
    @Override
    public void updateGameState() {
        // Update the view, e.g., redraw or refresh the game level
        this.levelComponent.loadLevel();
    }

    /**
     * Displays a message to the user in a popup window.
     * The message window will appear at the center of the screen and fade out after a short delay.
     *
     * @param message the message to display in the popup window.
     */
    public static void showMessage(String message) {
        // Create a borderless window (JWindow)
        JWindow window = new JWindow();
        window.setAlwaysOnTop(true); // Keep the window on top
        window.setBackground(new Color(255, 255, 255, 150)); // Semi-transparent background

        // Create JLabel to display the message content
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setOpaque(false); // Transparent background
        label.setFont(new Font("SansSerif", Font.BOLD, 17)); // Set font size to 17
        label.setForeground(Color.BLUE); // Set font color to blue
        window.add(label);
        window.pack();

        // Set the window position to the center of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation(screenSize.width / 2 - window.getWidth() / 2,
                screenSize.height / 2 - window.getHeight() / 2);

        // Create a timer for the fade-out effect
        javax.swing.Timer fadeTimer = new javax.swing.Timer(150, null);
        fadeTimer.addActionListener(e -> {
            // Gradually reduce the opacity of the window
            float opacity = window.getOpacity();
            opacity -= 0.1f; // Decrease opacity by 0.1 each time
            if (opacity <= 0) {
                fadeTimer.stop();
                window.dispose();
            } else {
                window.setOpacity(opacity);
            }
        });

        // Show the window and start the fade-out effect after a delay of 2 seconds
        window.setVisible(true);
        javax.swing.Timer delayTimer = new javax.swing.Timer(2000, e -> fadeTimer.start()); // Start fading after 2 seconds
        delayTimer.setRepeats(false);
        delayTimer.start();
    }
}


