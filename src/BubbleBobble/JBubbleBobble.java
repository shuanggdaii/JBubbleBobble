package BubbleBobble;

import BubbleBobble.controller.GameController;
import BubbleBobble.view.GameView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

import java.awt.image.BufferedImage;

public class JBubbleBobble {


	public static void main(String[] args) {

		LevelComponent component = new LevelComponent();
		GameView view = new GameView(component);
		GameController controller = new GameController(view, component);
		controller.startGame();

		// Create a panel with an input box and a button
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(300, 200));  // Set the preferred size of the panel to 300x200 pixels
		JTextField nameField = new JTextField(20);  // Create a text input field
		JButton uploadButton = new JButton("Upload Avatar");  // Create a button to upload an avatar
		JLabel avatarLabel = new JLabel();  // Create a JLabel to display the avatar

		uploadButton.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();  // Create a file chooser
			fileChooser.setDialogTitle("Choose your avatar");  // Set the dialog title
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);  // Only allow file selection

			int userSelection = fileChooser.showOpenDialog(view.getMainFrame());
			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File fileToUpload = fileChooser.getSelectedFile();  // Get the selected file
				try {
					BufferedImage avatar = ImageIO.read(fileToUpload);
					ImageIcon avatarIcon = new ImageIcon(avatar.getScaledInstance(100, 100, Image.SCALE_SMOOTH));  // Resize the avatar
					avatarLabel.setIcon(avatarIcon);  // Set the JLabel icon to the selected avatar
					File avatarsDir = new File("avatars");
					if (!avatarsDir.exists()) {
						avatarsDir.mkdir();
					}
					String avatarFilePath = avatarsDir.getPath() + File.separator + "hero1.png";  // Get the full path
					ImageIO.write(avatar, "png", new File(avatarFilePath));  // Save the avatar, format can be adjusted if needed
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(view.getMainFrame(), "Failed to upload avatar.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		// Add components to the panel
		panel.add(new JLabel("Input player name:"));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));  // Set vertical layout
		panel.add(nameField);
		panel.add(new JLabel("<html>  <br> </html>"));
		panel.add(uploadButton);
		panel.add(new JLabel("<html> <br> </html>"));
		panel.add(avatarLabel);  // Add the JLabel to the panel

		// Display the dialog box with the input field and upload button
		int result = JOptionPane.showConfirmDialog(view.getMainFrame(), panel, "Player Setup", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			component.name = nameField.getText();
		}
	}
}
