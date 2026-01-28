package ciphers;

import components.*;
import javax.swing.*;
import java.awt.*;

public class PolybiusSquarePanel extends BaseCipherPanel {
    public PolybiusSquarePanel() {
        super("Polybius Square");
    }

    @Override
    protected void addControls(JPanel p) {
        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));

        enc.addActionListener(e -> measureAndRun(() -> process(true), true));
        dec.addActionListener(e -> measureAndRun(() -> process(false), false));

        p.add(enc);
        p.add(dec);
    }

    private void process(boolean encrypt) {
        try {
            String text = getInputText().toUpperCase();
            StringBuilder sb = new StringBuilder();

            if (encrypt) {
                for (char c : text.toCharArray()) {
                    if (c == 'J')
                        c = 'I';
                    if (c >= 'A' && c <= 'Z') {
                        int row = (c - 'A') / 5 + 1;
                        int col = (c - 'A') % 5 + 1;
                        if (c > 'I') {
                            row = (c - 'A' - 1) / 5 + 1;
                            col = (c - 'A' - 1) % 5 + 1;
                        }
                        sb.append(row).append(col).append(" ");
                    }
                }
            } else {
                String[] parts = text.trim().split("\\s+");
                char[][] grid = {
                        { 'A', 'B', 'C', 'D', 'E' },
                        { 'F', 'G', 'H', 'I', 'K' },
                        { 'L', 'M', 'N', 'O', 'P' },
                        { 'Q', 'R', 'S', 'T', 'U' },
                        { 'V', 'W', 'X', 'Y', 'Z' }
                };
                for (String part : parts) {
                    if (part.length() == 2) {
                        int r = Character.getNumericValue(part.charAt(0)) - 1;
                        int c = Character.getNumericValue(part.charAt(1)) - 1;
                        if (r >= 0 && r < 5 && c >= 0 && c < 5) {
                            sb.append(grid[r][c]);
                        }
                    }
                }
            }
            outputArea.setText(sb.toString());
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
