package ciphers;

import components.*;
import javax.swing.*;
import java.awt.*;

public class PortaCipherPanel extends BaseCipherPanel {
    private JTextField keyField;

    public PortaCipherPanel() {
        super("Porta Cipher");
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
            String text = getInputText().toUpperCase();
            String key = keyField.getText().toUpperCase().replaceAll("[^A-Z]", "");
            if (key.isEmpty())
                throw new IllegalArgumentException("Key cannot be empty");

            StringBuilder result = new StringBuilder();
            int keyIdx = 0;

            for (char c : text.toCharArray()) {
                if (c < 'A' || c > 'Z') {
                    result.append(c);
                    continue;
                }

                char k = key.charAt(keyIdx % key.length());
                // Porta Table Logic
                // Keys A,B use row 0; C,D use row 1; ...
                int row = (k - 'A') / 2;

                // Row 0 (A,B): A<->N, B<->O, ... (Shift 13)
                // Row 1 (C,D): A<->O, B<->P, ... (Shift ?)
                // Actually Porta is reciprocal.
                // Row 0: ABCDEFGHIJKLM NOPQRSTUVWXYZ
                // NOPQRSTUVWXYZ ABCDEFGHIJKLM
                // Row 1: ABCDEFGHIJKLM NOPQRSTUVWXYZ
                // OPQRSTUVWXYZN MABCDEFGHIJKL (Shifted?)

                // Standard Porta Table:
                // Key A,B: N O P Q R S T U V W X Y Z | A B C D E F G H I J K L M
                // Key C,D: O P Q R S T U V W X Y Z N | M A B C D E F G H I J K L
                // ...

                // First half (A-M) maps to second half (N-Z) and vice versa.
                // The second half is shifted.

                int val = c - 'A';
                int mapped;

                if (val < 13) { // A-M
                    // Maps to N-Z part
                    // Base N (13) + shift
                    mapped = (val + row) % 13 + 13;
                } else { // N-Z
                    // Maps to A-M part
                    // Base 0 + shift inverse?
                    // Let's look at the pattern.
                    // If row=0, N(13) -> A(0). (13-13-0 = 0)
                    // If row=1, O(14) -> A(0). (14-13-1 = 0)
                    mapped = (val - 13 - row + 13) % 13; // +13 to ensure positive
                    // Wait, (val - 13 - row) might be negative.
                    mapped = (val - 13 - row);
                    while (mapped < 0)
                        mapped += 13;
                    mapped %= 13;
                }

                result.append((char) (mapped + 'A'));
                keyIdx++;
            }
            outputArea.setText(result.toString());
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
