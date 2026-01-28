package ciphers;

import components.*;
import javax.swing.*;
import java.awt.*;

public class ColumnarTranspositionPanel extends BaseCipherPanel {
    JTextField keyField;

    public ColumnarTranspositionPanel() {
        super("Columnar Transposition");
    }

    @Override
    protected void addControls(JPanel p) {
        p.add(new JLabel("<html><font color='white'>Key:</font></html>"));
        keyField = createTextField("KEY");
        p.add(keyField);

        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));

        enc.addActionListener(e -> measureAndRun(() -> process(true), true));
        dec.addActionListener(e -> measureAndRun(() -> process(false), false));

        p.add(enc);
        p.add(dec);
    }

    @Override
    protected void resetFields() {
        keyField.setText("KEY");
    }

    private void process(boolean encrypt) {
        try {
            String text = getInputText().replaceAll("\\s+", "");
            String key = keyField.getText();
            int cols = key.length();

            if (encrypt) {
                int rows = (int) Math.ceil((double) text.length() / cols);
                char[][] grid = new char[rows][cols];
                int idx = 0;
                for (int r = 0; r < rows; r++)
                    for (int c = 0; c < cols; c++) {
                        if (idx < text.length())
                            grid[r][c] = text.charAt(idx++);
                        else
                            grid[r][c] = 'X'; // Padding
                    }
                StringBuilder sb = new StringBuilder();
                for (int c = 0; c < cols; c++)
                    for (int r = 0; r < rows; r++)
                        sb.append(grid[r][c]);
                outputArea.setText(sb.toString());
            } else {
                // Simplified Decryption (Assumes rectangular grid for simplicity in this demo)
                int rows = text.length() / cols;
                char[][] grid = new char[rows][cols];
                int idx = 0;
                for (int c = 0; c < cols; c++)
                    for (int r = 0; r < rows; r++)
                        if (idx < text.length())
                            grid[r][c] = text.charAt(idx++);

                StringBuilder sb = new StringBuilder();
                for (int r = 0; r < rows; r++)
                    for (int c = 0; c < cols; c++)
                        sb.append(grid[r][c]);
                outputArea.setText(sb.toString());
            }
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
