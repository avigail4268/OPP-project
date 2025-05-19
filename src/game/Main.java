package game;

import game.engine.GameWorld;
import game.controller.GameController;
import game.gui.CharacterOption;
import game.gui.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * The Main class serves as the entry point to the game application.
 * It initializes the game world, controller, and GUI frame,
 * and starts the game using user input for configuration.
 */
public class Main {

    /**
     * The main method that launches the game.
     *
     * @param args Command-line arguments
     */

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            // Ask the user for map size (min 10)
            int size = askMapSize();

            // Ask the user for the type of player (Warrior, Mage, Archer)
            int playerType = askPlayerType() + 1;

            // Ask the user to enter a character name
            String name = askPlayerName();

            // Create the game world with the provided parameters
            GameWorld world = new GameWorld(size, playerType, name);

            // Define the fixed panel size in pixels
            int panelSize = 640;

            // Calculate tile size based on number of tiles (map size)
            int tileSize = panelSize / size;

            // Initialize the game controller with the world
            GameController controller = new GameController(world);
            controller.setTileSize(tileSize);
            world.setController(controller);// Set size of each tile

            // Create the GUI frame and link it to the controller
            GameFrame frame = new GameFrame(controller);
            controller.setFrame(frame);
            showAutoClosingWelcome(name);
            world.startEnemyTask();
            // Make the GUI visible
            frame.setVisible(true);
        });
    }

    /**
     * Prompts the user to input a map size via a dialog box.
     * Ensures that the returned size is at least 10.
     *
     * @return The validated map size (minimum 10)
     */

    public static int askMapSize() {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 10, 30, 10);
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
            @Override
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
            return 15;
        }
    }

    /**
     * Prompts the user to input a name for the player character.
     *
     * @return The name entered by the user
     */
    public static int askPlayerType() {
        String[] names = {"Archer", "Mage", "Warrior"};

        Image background = new ImageIcon(Main.class.getResource("/images/character_selection_bg.jpg")).getImage();


        JPanel panel = new JPanel() {
            @Override
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
            panel.add(new JLabel());
        }

        for (JRadioButton button : buttons) {
            panel.add(button);
        }

//        buttons[1].setSelected(true);

        int result = JOptionPane.showConfirmDialog(
                null, panel, "Choose Player Type", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i].isSelected()) {
                    return i;
                }
            }
        }
        return 1;
    }
    public static String askPlayerName() {

        Image background = new ImageIcon(Main.class.getResource("/images/map.jpg")).getImage();

        JPanel panel = new JPanel() {
            @Override
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
        label.setFont(new Font("Serif", Font.BOLD, 20));

        JTextField nameField = new JTextField();
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setFont(new Font("Serif", Font.PLAIN, 18));
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
            return "Player";
        }
    }
    public static void showAutoClosingWelcome(String name) {
        JWindow window = new JWindow();

        JLabel message = new JLabel("Welcome, " + name + "!", SwingConstants.CENTER);
        message.setFont(new Font("Serif", Font.BOLD, 28));
        message.setForeground(Color.BLACK);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 215, 0)); // זהב
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panel.add(message, BorderLayout.CENTER);

        window.getContentPane().add(panel);
        window.setSize(350, 100);
        window.setLocationRelativeTo(null);
        window.setAlwaysOnTop(true);
        window.setOpacity(0f);
        window.setVisible(true);

        Timer fadeIn = new Timer(10, null);
        fadeIn.addActionListener(new ActionListener() {
            float opacity = 0f;
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                window.setOpacity(Math.min(opacity, 1f));
                if (opacity >= 1f) {
                    fadeIn.stop();


                    Timer delay = new Timer(1500, null);
                    delay.setRepeats(false);
                    delay.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            Timer fadeOut = new Timer(30, null);
                            fadeOut.addActionListener(new ActionListener() {
                                float op = 1f;
                                public void actionPerformed(ActionEvent e) {
                                    op -= 0.05f;
                                    window.setOpacity(Math.max(op, 0f));
                                    if (op <= 0f) {
                                        ((Timer) e.getSource()).stop();
                                        window.dispose();
                                    }
                                }
                            });
                            fadeOut.start();
                        }
                    });
                    delay.start();
                }
            }
        });
        fadeIn.start();
    }
}
