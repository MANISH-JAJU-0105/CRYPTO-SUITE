package ciphers;

import components.*;
import javax.swing.*;
import java.awt.*;

public class DoubleColumnarTranspositionPanel extends BaseCipherPanel {
    private JTextField key1Field;
    private JTextField key2Field;

    public DoubleColumnarTranspositionPanel() {
        super("Double Columnar Transposition");
    }

    @Override
    protected void addControls(JPanel p) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        // Row 1: Key 1
        JPanel k1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        k1Panel.setOpaque(false);
        k1Panel.add(new JLabel("<html><font color='white'>Key 1:</font></html>"));
        key1Field = createTextField("KEYONE");
        key1Field.setPreferredSize(new Dimension(100, 30));
        k1Panel.add(key1Field);
        container.add(k1Panel);

        // Row 2: Key 2
        JPanel k2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        k2Panel.setOpaque(false);
        k2Panel.add(new JLabel("<html><font color='white'>Key 2:</font></html>"));
        key2Field = createTextField("KEYTWO");
        key2Field.setPreferredSize(new Dimension(100, 30));
        k2Panel.add(key2Field);
        container.add(k2Panel);

        container.add(Box.createVerticalStrut(10));

        // Buttons
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
        key1Field.setText("KEYONE");
        key2Field.setText("KEYTWO");
    }

    private void process(boolean encrypt) {
        try {
            String text = getInputText().replaceAll("\\s+", "");
            String key1 = key1Field.getText();
            String key2 = key2Field.getText();

            if (encrypt) {
                String pass1 = columnarEncrypt(text, key1);
                String pass2 = columnarEncrypt(pass1, key2);
                outputArea.setText(pass2);
            } else {
                String pass1 = columnarDecrypt(text, key2);
                String pass2 = columnarDecrypt(pass1, key1);
                outputArea.setText(pass2);
            }
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }

    private String columnarEncrypt(String text, String key) {
        int cols = key.length();
        int rows = (int) Math.ceil((double) text.length() / cols);
        char[][] grid = new char[rows][cols];
        int idx = 0;

        // Fill grid row by row
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (idx < text.length()) {
                    grid[r][c] = text.charAt(idx++);
                } else {
                    grid[r][c] = 'X'; // Padding
                }
            }
        }

        // Determine column order based on key
        Integer[] order = getColumnOrder(key);

        // Read grid column by column based on order
        StringBuilder sb = new StringBuilder();
        for (int c : order) {
            for (int r = 0; r < rows; r++) {
                sb.append(grid[r][c]);
            }
        }
        return sb.toString();
    }

    private String columnarDecrypt(String text, String key) {
        int cols = key.length();
        int rows = text.length() / cols;
        char[][] grid = new char[rows][cols];

        Integer[] order = getColumnOrder(key);

        int idx = 0;
        // Fill grid column by column based on order
        for (int c : order) {
            for (int r = 0; r < rows; r++) {
                if (idx < text.length()) {
                    grid[r][c] = text.charAt(idx++);
                }
            }
        }

        // Read grid row by row
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                sb.append(grid[r][c]);
            }
        }
        return sb.toString();
    }

    private Integer[] getColumnOrder(String key) {
        int len = key.length();
        Integer[] order = new Integer[len];
        Character[] chars = new Character[len];
        for (int i = 0; i < len; i++)
            chars[i] = key.charAt(i);

        // Simple sort to determine order
        // This is a naive implementation of getting indices.
        // A proper one handles duplicate letters by position.

        boolean[] used = new boolean[len];
        int orderIdx = 0;

        // Find alphabetical order
        for (char c = 0; c < 256; c++) { // Iterate through all chars
            for (int i = 0; i < len; i++) {
                if (!used[i] && key.charAt(i) == c) {
                    order[orderIdx++] = i;
                    used[i] = true;
                }
            }
        }

        // If key has chars not in 0-255 (unlikely for standard keys), fallback
        if (orderIdx < len) {
            for (int i = 0; i < len; i++) {
                if (!used[i])
                    order[orderIdx++] = i;
            }
        }

        // Wait, the order array should store the column index to read FIRST, SECOND,
        // etc.
        // So if key is "BAC", order is [1, 0, 2] (B is 2nd, A is 1st, C is 3rd) -> No.
        // "BAC": Sorted is ABC.
        // 1st col to read is 'A' (index 1).
        // 2nd col to read is 'B' (index 0).
        // 3rd col to read is 'C' (index 2).
        // So order = {1, 0, 2}.
        // My loop above does exactly this.

        return order;
    }
}
