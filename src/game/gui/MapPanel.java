//package game.gui;
//import game.controller.GameController;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//
//public class MapPanel extends JPanel {
//    public MapPanel(GameController controller) {
//        this.controller = controller;
//        int rows = controller.getMapRows();
//        int cols = controller.getMapCols();
//
//        setLayout(new GridLayout(rows, cols));
//        cellButtons = new JButton[rows][cols];
//
//        setFocusable(true);
//        requestFocusInWindow();
//
//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++) {
//                JButton button = new JButton();
//                button.setFocusable(false);
//                button.setIcon(controller.getIconWithHealthBar(row, col));
//                int finalRow = row;
//                int finalCol = col;
//                button.addActionListener(e -> controller.handleLeftClick(finalRow, finalCol));
//                button.addMouseListener(new MouseAdapter() {
//                    public void mousePressed(MouseEvent e) {
//                        if (SwingUtilities.isRightMouseButton(e)) {
//                            controller.handleRightClick(finalRow, finalCol, button);
//                        }
//                    }
//                });
//                cellButtons[row][col] = button;
//                add(button);
//            }
//        }
//        setupKeyBindings();
//    }
//    private void setupKeyBindings() {
//        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
//        ActionMap actionMap = getActionMap();
//        inputMap.put(KeyStroke.getKeyStroke("UP"), "moveUp");
//        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
//        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
//        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
//
//        actionMap.put("moveUp", new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                controller.handleArrowKey("UP");
//            }
//        });
//        actionMap.put("moveDown", new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                controller.handleArrowKey("DOWN");
//            }
//        });
//        actionMap.put("moveLeft", new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                controller.handleArrowKey("LEFT");
//            }
//        });
//        actionMap.put("moveRight", new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                controller.handleArrowKey("RIGHT");
//            }
//        });
//    }
//    public void refresh() {
//        for (int row = 0; row < cellButtons.length; row++) {
//            for (int col = 0; col < cellButtons[0].length; col++) {
//                cellButtons[row][col].setIcon(controller.getIconWithHealthBar(row, col));
//            }
//        }
//    }
//    public void highlightCell(int row, int col, Color color) {
//        JButton cell = cellButtons[row][col];
//        Color original = cell.getBackground();
//        cell.setBackground(color);
//
//        new Timer(300, e -> cell.setBackground(original)).start();
//    }
//
//    private final JButton[][] cellButtons;
//    private final GameController controller;
//}
package game.gui;

import game.controller.GameController;
import game.observer.GameObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MapPanel extends JPanel implements GameObserver {
    public MapPanel(GameController controller) {
        this.controller = controller;
        int rows = controller.getMapRows();
        int cols = controller.getMapCols();

        setLayout(new GridLayout(rows, cols));
        cellButtons = new JButton[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                JButton button = new JButton();
                button.setIcon(controller.getIconWithHealthBar(row, col));
                int finalRow = row;
                int finalCol = col;
                button.addActionListener(e -> controller.handleLeftClick(finalRow, finalCol));
                button.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mousePressed(java.awt.event.MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            controller.handleRightClick(finalRow, finalCol, button);
                        }
                    }
                });
                cellButtons[row][col] = button;
                add(button);
            }
        }
        setupKeyBindings();
    }

    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        inputMap.put(KeyStroke.getKeyStroke("UP"), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");

        actionMap.put("moveUp", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                controller.handleArrowKey("UP");
            }
        });
        actionMap.put("moveDown", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                controller.handleArrowKey("DOWN");
            }
        });
        actionMap.put("moveLeft", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                controller.handleArrowKey("LEFT");
            }
        });
        actionMap.put("moveRight", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                controller.handleArrowKey("RIGHT");
            }
        });
    }

    public void refresh() {
        for (int row = 0; row < cellButtons.length; row++) {
            for (int col = 0; col < cellButtons[0].length; col++) {
                cellButtons[row][col].setIcon(controller.getIconWithHealthBar(row, col));
            }
        }
    }

    public void highlightCell(int row, int col, Color color) {
        JButton cell = cellButtons[row][col];
        Color original = cell.getBackground();
        cell.setBackground(color);

        new javax.swing.Timer(300, e -> cell.setBackground(original)).start();
    }

    @Override
    public void GameUpdated() {
        refresh();
    }

    private final JButton[][] cellButtons;
    private final GameController controller;
}
