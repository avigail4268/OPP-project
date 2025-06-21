package game.gui;

import game.Main;
import game.combat.MagicElement;
import game.controller.GameController;
import game.engine.GameWorld;
import game.log.LogManager;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


/**
 * The GameSetUp class is responsible for setting up the game environment,
 * including player selection, map size, and other configurations.
 * It provides methods to gather user input and initialize the game world.
 */
public class GameSetUp {

    /**
     * Constructs a GameSetUp instance.
     * This constructor can be used for any necessary initialization before starting the game.
     */
    public GameSetUp() {
        // Constructor can be used for any necessary initialization
    }

    /**
     * Starts the game setup process, including gathering player information and initializing the game world.
     */
    public void start() {
        LogManager.startLogger();
        SwingUtilities.invokeLater(() -> {

            int size = askMapSize();
            int playerType = askPlayerType() + 1;
            MagicElement element = null;

            if (playerType == 2) {
                element = askElementType();
            }

            boolean includeDefense = playerType == 3;
            Map<String, Integer> attributes = askPlayerStatChanges(includeDefense);
            String name = askPlayerName();
            List <String> decorators = askDecorators();


            GameWorld world = new GameWorld(size, playerType, name, attributes, element, decorators);

            int panelSize = 640;
            int tileSize = panelSize / world.getMap().getSize();

            GameController controller = new GameController(world);
            controller.setTileSize(tileSize);
            world.setController(controller);

            GameFrame frame = new GameFrame(controller);
            controller.setFrame(frame);

            showAutoClosingWelcome(world.getPlayer().getName());
            world.startEnemyTask();
            frame.setVisible(true);
        });
    }

    /**
     * Asks the user to choose the size of the game map.
     * @return the size of the map as an integer (between 10 and 20)
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
     * Asks the user to choose a player type.
     * @return an integer representing the selected player type (0 for Archer, 1 for Mage, 2 for Warrior)
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
        return 1;
    }

    /**
     * Asks the player to enter their name.
     * @return the player's name as a String, or "Player" if no name is entered
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
     * Asks the player to choose how much to increase or decrease their stats.
     * @param includeDefense whether to include defense in the stat changes
     * @return a map containing the stat changes for health, power, and optionally defense
     */
    public static Map<String, Integer> askPlayerStatChanges(boolean includeDefense) {
        Map<String, Integer> result = new HashMap<>();

        JSpinner healthSpinner = new JSpinner(new SpinnerNumberModel(0, -3, 3, 1));
        JSpinner powerSpinner = new JSpinner(new SpinnerNumberModel(0, -3, 3, 1));
        JSpinner defenseSpinner = includeDefense ? new JSpinner(new SpinnerNumberModel(0, -3, 3, 1)) : null;

        JLabel statusLabel = new JLabel("Total must be 0");

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Health:"));
        panel.add(healthSpinner);
        panel.add(new JLabel("Power:"));
        panel.add(powerSpinner);

        if (includeDefense) {
            panel.add(new JLabel("Defense:"));
            panel.add(defenseSpinner);
        }

        panel.add(statusLabel);

        JButton okButton = new JButton("OK");
        okButton.setEnabled(true);
        panel.add(okButton);

        ChangeListener listener = e -> {
            int total = (int) healthSpinner.getValue() + (int) powerSpinner.getValue();
            if (includeDefense) {
                total += (int) defenseSpinner.getValue();
            }

            if (total == 0) {
                statusLabel.setText("Balance is 0");
                okButton.setEnabled(true);
            } else {
                statusLabel.setText("You need to subtract" + total + ")");
                okButton.setEnabled(false);
            }
        };

        healthSpinner.addChangeListener(listener);
        powerSpinner.addChangeListener(listener);
        if (includeDefense) {
            defenseSpinner.addChangeListener(listener);
        }

        JDialog dialog = new JDialog((Frame) null, "Choose how much to increase and decrease", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(panel);

        dialog.setSize(400, includeDefense ? 250 : 200);

        dialog.setLocationRelativeTo(null);

        okButton.addActionListener(e -> {
            result.put("Health", (int) healthSpinner.getValue());
            result.put("Power", (int) powerSpinner.getValue());
            if (includeDefense) {
                result.put("Defence", (int) defenseSpinner.getValue());
            }
            dialog.dispose();
        });

        dialog.setVisible(true);
        return result;
    }

    /**
     * Displays a welcome message in a JWindow that fades in and out automatically.
     * @param name the name of the player to display in the welcome message
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

    /**
     * Displays a victory panel with a congratulatory message and an image.
     * The panel fades in, stays visible for a short time, and then fades out.
     * @param onFinish a Runnable to execute after the panel fades out
     */
    public void showVictoryPanel(Runnable onFinish) {
        ImageIcon icon = new ImageIcon(Main.class.getResource("/images/winner.jpg"));
        JLabel label = new JLabel(icon);
        JWindow window = new JWindow();
        window.getContentPane().add(label);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setAlwaysOnTop(true);
        window.setOpacity(0f);
        window.setVisible(true);

        Timer fadeIn = new Timer(30, null);
        fadeIn.addActionListener(new ActionListener() {
            float opacity = 0f;

            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                window.setOpacity(Math.min(opacity, 1f));
                if (opacity >= 1f) {
                    ((Timer) e.getSource()).stop();
                    Timer delay = new Timer(1500, null);
                    delay.setRepeats(false);
                    delay.addActionListener(ev -> {
                        Timer fadeOut = new Timer(30, null);
                        fadeOut.addActionListener(new ActionListener() {
                            float op = 1f;

                            public void actionPerformed(ActionEvent e) {
                                op -= 0.05f;
                                window.setOpacity(Math.max(op, 0f));
                                if (op <= 0f) {
                                    ((Timer) e.getSource()).stop();
                                    window.dispose();
                                    if (onFinish != null) {
                                        onFinish.run();
                                    }
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

    /**
     * Displays a game over panel with an image and fades it in and out.
     * After fading out, it executes the provided Runnable.
     * @param onFinish a Runnable to execute after the panel fades out
     */
    public void gameOverPanel(Runnable onFinish) {
        Image background = new ImageIcon(Main.class.getResource("/images/gameOver.jpg")).getImage();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JPanel fullScreenPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, screenSize.width, screenSize.height, this);
            }
        };

        fullScreenPanel.setPreferredSize(screenSize);
        JWindow window = new JWindow();
        window.getContentPane().add(fullScreenPanel);
        window.setSize(screenSize);
        window.setLocation(0, 0);
        window.setAlwaysOnTop(true);
        window.setOpacity(0f);
        window.setVisible(true);

        Timer fadeIn = new Timer(30, null);
        fadeIn.addActionListener(new ActionListener() {
            float opacity = 0f;

            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                window.setOpacity(Math.min(opacity, 1f));
                if (opacity >= 1f) {
                    ((Timer) e.getSource()).stop();
                    Timer delay = new Timer(1500, null);
                    delay.setRepeats(false);
                    delay.addActionListener(ev -> {
                        Timer fadeOut = new Timer(30, null);
                        fadeOut.addActionListener(new ActionListener() {
                            float op = 1f;

                            public void actionPerformed(ActionEvent e) {
                                op -= 0.05f;
                                window.setOpacity(Math.max(op, 0f));
                                if (op <= 0f) {
                                    ((Timer) e.getSource()).stop();
                                    window.dispose();
                                    if (onFinish != null) {
                                        onFinish.run();
                                    }
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

    /**
     * Asks the user to choose up to 2 decorators for their character.
     * @return a list of selected decorator keys
     */
    public static List<String> askDecorators() {
        String[] decoratorNames = {"Boosted Attack", "Auto Heal", "Magic Amplifier *For Mages Only*"};
        String[] decoratorKeys = {"boost", "regen", "magicamplifier"};

        JCheckBox[] checkBoxes = new JCheckBox[decoratorNames.length];
        JPanel panel = new JPanel(new GridLayout(decoratorNames.length, 1, 5, 5));
        panel.setPreferredSize(new Dimension(300, 120));
        panel.setOpaque(false);

        // נעקב אחרי כמה תיבות סומנו
        final int[] selectedCount = {0};

        for (int i = 0; i < decoratorNames.length; i++) {
            JCheckBox checkBox = new JCheckBox(decoratorNames[i]);
            checkBox.setFont(new Font("Serif", Font.PLAIN, 14));

            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (checkBox.isSelected()) {
                        if (selectedCount[0] >= 2) {
                            checkBox.setSelected(false);
                            JOptionPane.showMessageDialog(null, "You can select up to 2 decorators only.", "Limit Reached", JOptionPane.WARNING_MESSAGE);
                        } else {
                            selectedCount[0]++;
                        }
                    } else {
                        selectedCount[0]--;
                    }
                }
            });

            checkBoxes[i] = checkBox;
            panel.add(checkBox);
        }

        int result = JOptionPane.showConfirmDialog(
                null, panel, "Choose up to 2 Decorators", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        List<String> chosen = new ArrayList<>();

        if (result == JOptionPane.OK_OPTION) {
            for (int i = 0; i < checkBoxes.length; i++) {
                if (checkBoxes[i].isSelected()) {
                    chosen.add(decoratorKeys[i]);
                }
            }
        } else {
            System.exit(0); // או return null
        }

        return chosen;
    }

    /**
     * Asks the user to choose a magic element type.
     * @return
     */
    public static MagicElement askElementType() {
        MagicElement[] elements = MagicElement.values();
        String[] imagePaths = {
                "/images/fire.jpg",
                "/images/ice.jpg",
                "/images/lightning.jpg",
                "/images/acid.jpg"
        };

        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 10));
        ButtonGroup group = new ButtonGroup();
        JToggleButton[] buttons = new JToggleButton[elements.length];

        for (int i = 0; i < elements.length; i++) {
            ImageIcon icon = new ImageIcon(GameSetUp.class.getResource(imagePaths[i]));
            Image scaledImage = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            buttons[i] = new JToggleButton(elements[i].name(), new ImageIcon(scaledImage));
            buttons[i].setVerticalTextPosition(SwingConstants.BOTTOM);
            buttons[i].setHorizontalTextPosition(SwingConstants.CENTER);
            group.add(buttons[i]);
            panel.add(buttons[i]);
        }

        int result = JOptionPane.showConfirmDialog(
                null, panel, "Choose Magic Element", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i].isSelected()) {
                    return elements[i];
                }
            }
        }
        return MagicElement.FIRE; // default
    }

    /**
     * Exits the game application.
     * @param game the current game world instance (not used in this method)
     */
    public void exitGame(GameWorld game) {
        System.exit(0);
    }
}