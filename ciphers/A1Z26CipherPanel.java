package ciphers;

import components.*;
import javax.swing.*;
import java.awt.*;

public class A1Z26CipherPanel extends BaseCipherPanel {
    public A1Z26CipherPanel() {
        super("A1Z26 Cipher");
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
            String text = getInputText().trim();
            if (!encrypt) { // Decrypt
                String[] parts = text.split("[^0-9]+");
                StringBuilder sb = new StringBuilder();
                for (String p : parts) {
                    if (!p.isEmpty()) {
                        int val = Integer.parseInt(p);
                        if (val >= 1 && val <= 26)
                            sb.append((char) ('A' + val - 1));
                    }
                }
                outputArea.setText(sb.toString());
            } else { // Encrypt
                StringBuilder sb = new StringBuilder();
                for (char c : text.toUpperCase().toCharArray()) {
                    if (Character.isLetter(c))
                        sb.append((int) (c - 'A' + 1)).append("-");
                }
                if (sb.length() > 0)
                    sb.setLength(sb.length() - 1);
                outputArea.setText(sb.toString());
            }
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
