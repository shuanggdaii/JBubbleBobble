package BubbleBobble.designPattern;

/**
 * Defines the Observer interface for the game. Classes that implement this interface
 * can observe changes in the game state and react accordingly when notified.
 */
public interface GameObserver {
    /**
     * Notifies the observer to update its status based on changes in the game state.
     */
    void updateGameState();
}