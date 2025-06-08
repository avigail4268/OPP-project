/**
 * avigail musai 322227711
 * rotem dino 209168442
 * chira borochov 345887046
 */

package game;
import game.engine.GameWorld;
import game.controller.GameController;
import game.gui.GameFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Entry point for the Dungeons & Dragons game.
 * Initializes user settings (map size, player type, name), game world, controller, and GUI.
 */
public class Main {

    /**
     * Launches the game application. Prompts the user for game configuration
     * and initializes the game world and GUI.
     * @param args Command-line arguments (unused)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int size = askMapSize();                      // Get map size from user
            int playerType = askPlayerType() + 1;         // Get player class (adjusted to internal format)
            String name = askPlayerName();                // Get player name

            GameWorld world = new GameWorld(size, playerType, name);
            int panelSize = 640;                          // Fixed size of display panel
            int tileSize = panelSize / size;              // Tile size determined dynamically

            GameController controller = new GameController(world);
            controller.setTileSize(tileSize);
            world.setController(controller);

            GameFrame frame = new GameFrame(controller);  // Set up GUI frame
            controller.setFrame(frame);

            showAutoClosingWelcome(name);                 // Show welcome message
            world.startEnemyTask();                       // Start enemy AI tasks
            frame.setVisible(true);                       // Display GUI
        });
    }

    /**
     * Displays a slider dialog allowing the user to select a map size.
     * The value is restricted to be between 10 and 20.
     * @return the selected map size
     */
    public static int askMapSize() {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 10, 20, 10);
        slider.setMajorTickSpacing(5);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setOpaque(false);

        JLabel label = new JLabel("Map size: min 10x10", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Serif", Font.BOLD, 16));

        slider.addChangeListener(e -> {
            int val = slider.getValue();
            label.setText("Map size: " + val + "x" + val);
        });

        Image background = new ImageIcon(Main.class.getResource("/images/map.jpg")).getImage();

        JLabel titleLabel = new JLabel("Choose the map size:", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panel.setLayout(new BorderLayout(10, 10));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(slider, BorderLayout.CENTER);
        panel.add(label, BorderLayout.SOUTH);
        panel.setPreferredSize(new Dimension(450, 220));
        panel.setOpaque(false);

        int result = JOptionPane.showConfirmDialog(
                null, panel, "Map Size", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            return slider.getValue();
        } else {
            System.exit(0);
            return -1; // Never reached
        }
    }

    /**
     * Prompts the user to select the type of player character.
     * @return the index of the selected character class (0 = Archer, 1 = Mage, 2 = Warrior)
     */
    public static int askPlayerType() {
        String[] names = {"Archer", "Mage", "Warrior"};
        Image background = new ImageIcon(Main.class.getResource("/images/character_selection_bg.jpg")).getImage();

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new GridLayout(2, 3, 10, 5));
        panel.setPreferredSize(new Dimension(640, 260));
        panel.setOpaque(false);

        ButtonGroup group = new ButtonGroup();
        JRadioButton[] buttons = new JRadioButton[names.length];

        for (int i = 0; i < names.length; i++) {
            buttons[i] = new JRadioButton(names[i]);
            buttons[i].setHorizontalAlignment(SwingConstants.CENTER);
            buttons[i].setOpaque(false);
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setFont(new Font("Serif", Font.BOLD, 14));
            group.add(buttons[i]);
            panel.add(new JLabel()); // Placeholder for spacing
        }

        for (JRadioButton button : buttons) {
            panel.add(button);
        }

        int result = JOptionPane.showConfirmDialog(
                null, panel, "Choose Player Type", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i].isSelected()) {
                    return i;
                }
            }
        } else {
            System.exit(0);
            return -1;
        }
        return 1; // default: Mage
    }

    /**
     * Prompts the user to enter a name for the player character.
     * @return the trimmed name, or "Player" if left empty
     */
    public static String askPlayerName() {
        Image background = new ImageIcon(Main.class.getResource("/images/map.jpg")).getImage();

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(450, 240));
        panel.setOpaque(false);

        JLabel label = new JLabel("Enter your name, brave hero:", SwingConstants.CENTER);
        label.setForeground(new Color(255, 255, 230));
        label.setFont(new Font("Serif", Font.BOLD, 28));

        JTextField nameField = new JTextField();
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setFont(new Font("Serif", Font.PLAIN, 26));
        nameField.setOpaque(false);
        nameField.setForeground(Color.WHITE);
        nameField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        panel.add(label, BorderLayout.NORTH);
        panel.add(nameField, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(
                null, panel, "Enter Name", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            return nameField.getText().trim().isEmpty() ? "Player" : nameField.getText().trim();
        } else {
            System.exit(0);
            return null; // Unreachable but required for compilation
        }

    }

    /**
     * Displays a welcome popup message with a fade-in and fade-out animation.
     * The message disappears automatically after a short delay.
     * @param name the name of the player to include in the message
     */
    public static void showAutoClosingWelcome(String name) {
        JWindow window = new JWindow();

        JLabel message = new JLabel("Welcome, " + name + "!", SwingConstants.CENTER);
        message.setFont(new Font("Serif", Font.BOLD, 28));
        message.setForeground(Color.BLACK);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(243, 164, 243));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panel.add(message, BorderLayout.CENTER);

        window.getContentPane().add(panel);
        window.setSize(350, 100);
        window.setLocationRelativeTo(null);
        window.setAlwaysOnTop(true);
        window.setOpacity(0f);
        window.setVisible(true);

        Timer fadeIn = new Timer(5, null);
        fadeIn.addActionListener(new ActionListener() {
            float opacity = 0f;
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                window.setOpacity(Math.min(opacity, 1f));
                if (opacity >= 1f) {
                    fadeIn.stop();

                    Timer delay = new Timer(500, null);
                    delay.setRepeats(false);
                    delay.addActionListener(e2 -> {
                        Timer fadeOut = new Timer(10, null);
                        fadeOut.addActionListener(new ActionListener() {
                            float op = 1f;
                            public void actionPerformed(ActionEvent e3) {
                                op -= 0.05f;
                                window.setOpacity(Math.max(op, 0f));
                                if (op <= 0f) {
                                    ((Timer) e3.getSource()).stop();
                                    window.dispose();
                                }
                            }
                        });
                        fadeOut.start();
                    });
                    delay.start();
                }
            }
        });
        fadeIn.start();
    }
}