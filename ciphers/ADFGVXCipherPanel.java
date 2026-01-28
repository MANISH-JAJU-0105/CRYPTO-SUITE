package ciphers;

import components.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class ADFGVXCipherPanel extends BaseCipherPanel {
    private JTextField polybiusKeyField;
    private JTextField transKeyField;
    private static final char[] ADFGVX = { 'A', 'D', 'F', 'G', 'V', 'X' };
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public ADFGVXCipherPanel() {
        super("ADFGVX Cipher");
    }

    @Override
    protected void addControls(JPanel p) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        // Row 1: Polybius Key
        JPanel pKeyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pKeyPanel.setOpaque(false);
        pKeyPanel.add(new JLabel("<html><font color='white'>Polybius Key:</font></html>"));
        polybiusKeyField = createTextField("SECRET");
        pKeyPanel.add(polybiusKeyField);
        container.add(pKeyPanel);

        container.add(Box.createVerticalStrut(5));

        // Row 2: Transposition Key
        JPanel tKeyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        tKeyPanel.setOpaque(false);
        tKeyPanel.add(new JLabel("<html><font color='white'>Transposition Key:</font></html>"));
        transKeyField = createTextField("CRYPTO");
        tKeyPanel.add(transKeyField);
        container.add(tKeyPanel);

        container.add(Box.createVerticalStrut(10));

        // Row 3: Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        btnPanel.setOpaque(false);

        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));

        enc.addActionListener(e -> measureAndRun(() -> process(true), true));
        dec.addActionListener(e -> measureAndRun(() -> process(false), false));

        btnPanel.add(enc);
        btnPanel.add(dec);

        container.add(btnPanel);

        p.add(container);
    }

    @Override
    protected void resetFields() {
        polybiusKeyField.setText("SECRET");
        transKeyField.setText("CRYPTO");
    }

    private void process(boolean encrypt) {
        try {
            String input = getInputText();
            String pKey = polybiusKeyField.getText().toUpperCase().replaceAll("[^A-Z0-9]", "");
            String tKey = transKeyField.getText().toUpperCase().replaceAll("[^A-Z]", "");

            if (tKey.isEmpty())
                throw new Exception("Transposition Key cannot be empty");

            char[][] square = generatePolybiusSquare(pKey);

            if (encrypt) {
                String substituted = substitute(input, square);
                String result = transpose(substituted, tKey);
                outputArea.setText(result);
            } else {
                String untransposed = untranspose(input, tKey);
                String result = unsubstitute(untransposed, square);
                outputArea.setText(result);
            }
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }

    private char[][] generatePolybiusSquare(String key) {
        String uniqueKey = "";
        for (char c : (key + ALPHABET).toCharArray()) {
            if (uniqueKey.indexOf(c) == -1) {
                uniqueKey += c;
            }
        }

        char[][] square = new char[6][6];
        for (int i = 0; i < 36; i++) {
            square[i / 6][i % 6] = uniqueKey.charAt(i);
        }
        return square;
    }

    private String substitute(String text, char[][] square) {
        StringBuilder sb = new StringBuilder();
        text = text.toUpperCase().replaceAll("[^A-Z0-9]", "");

        for (char c : text.toCharArray()) {
            boolean found = false;
            for (int r = 0; r < 6; r++) {
                for (int cIdx = 0; cIdx < 6; cIdx++) {
                    if (square[r][cIdx] == c) {
                        sb.append(ADFGVX[r]).append(ADFGVX[cIdx]);
                        found = true;
                        break;
                    }
                }
                if (found)
                    break;
            }
        }
        return sb.toString();
    }

    private String unsubstitute(String text, char[][] square) {
        StringBuilder sb = new StringBuilder();
        text = text.replaceAll("[^ADFGVX]", "");

        for (int i = 0; i < text.length(); i += 2) {
            if (i + 1 >= text.length())
                break;
            char rChar = text.charAt(i);
            char cChar = text.charAt(i + 1);

            int r = -1, c = -1;
            for (int k = 0; k < 6; k++) {
                if (ADFGVX[k] == rChar)
                    r = k;
                if (ADFGVX[k] == cChar)
                    c = k;
            }

            if (r != -1 && c != -1) {
                sb.append(square[r][c]);
            }
        }
        return sb.toString();
    }

    private String transpose(String text, String key) {
        int cols = key.length();
        int rows = (int) Math.ceil((double) text.length() / cols);

        // Create grid
        char[][] grid = new char[rows][cols];
        int idx = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (idx < text.length()) {
                    grid[r][c] = text.charAt(idx++);
                } else {
                    grid[r][c] = ' '; // Padding
                }
            }
        }

        // Sort key to determine column order
        Integer[] colOrder = getColumnOrder(key);

        StringBuilder sb = new StringBuilder();
        for (int c : colOrder) {
            for (int r = 0; r < rows; r++) {
                if (grid[r][c] != ' ') {
                    sb.append(grid[r][c]);
                }
            }
            sb.append(" "); // Space between columns for readability
        }
        return sb.toString().trim();
    }

    private String untranspose(String text, String key) {
        text = text.replaceAll(" ", ""); // Remove spaces
        int cols = key.length();
        int len = text.length();
        int rows = (int) Math.ceil((double) len / cols);
        int shortCols = (rows * cols) - len; // Number of empty cells in last row

        Integer[] colOrder = getColumnOrder(key);
        char[][] grid = new char[rows][cols];

        int currentIdx = 0;
        for (int k = 0; k < cols; k++) {
            int colIdx = colOrder[k];
            int colLen = rows;
            // If this column is one of the last 'shortCols' columns (reading right to left
            // in the grid), it has one less char
            // The empty cells are at the end of the grid, so the last 'shortCols' columns
            // have length rows-1
            // Wait, the padding is at the end.
            // Example: 8 chars, 3 cols. Rows=3.
            // Grid:
            // X X X
            // X X X
            // X X
            // Col 0 len 3, Col 1 len 3, Col 2 len 2.
            // So columns >= (cols - shortCols) have length rows-1? No.
            // The empty cells are at grid[rows-1][cols-shortCols] to grid[rows-1][cols-1].

            if (colIdx >= (cols - shortCols)) {
                colLen = rows - 1;
            }

            for (int r = 0; r < colLen; r++) {
                if (currentIdx < text.length()) {
                    grid[r][colIdx] = text.charAt(currentIdx++);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] != 0) {
                    sb.append(grid[r][c]);
                }
            }
        }
        return sb.toString();
    }

    private Integer[] getColumnOrder(String key) {
        Integer[] indexes = new Integer[key.length()];
        for (int i = 0; i < key.length(); i++)
            indexes[i] = i;

        Arrays.sort(indexes, (a, b) -> Character.compare(key.charAt(a), key.charAt(b)));
        return indexes;
    }
}
