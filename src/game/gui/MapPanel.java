package game.gui;


import game.controller.GameController;


import javax.swing.*;
import java.awt.*;


public class MapPanel extends JPanel {
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

    private final JButton[][] cellButtons;
    private final GameController controller;
}





