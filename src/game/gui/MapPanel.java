package game.gui;
import game.controller.GameController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


class MapPanel extends JPanel {
    private static final int TILE_SIZE = 64;
    private GameController controller;
    private JButton[][] grid;
    private int rows;
    private int cols;

    public MapPanel(GameController controller) {
        this.controller = controller;
        this.rows = controller.getMapRows();
        this.cols = controller.getMapCols();
        setLayout(new GridLayout(rows, cols));
        grid = new JButton[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                JButton cell = new JButton();
                cell.setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
                cell.setFocusPainted(false);
                final int r = row;
                final int c = col;
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            controller.handleLeftClick(r, c);
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            controller.handleRightClick(r, c, cell);
                        }
                    }
                });

                grid[row][col] = cell;
                add(cell);
            }
        }
    }

    public void refresh() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                ImageIcon icon = controller.getIconForTile(row, col);
                grid[row][col].setIcon(icon);
            }
        }
        revalidate();
        repaint();
    }
}