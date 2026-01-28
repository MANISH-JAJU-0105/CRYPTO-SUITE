package ciphers;

import components.*;

import javax.swing.*;
import java.awt.*;

public class BinaryCipherPanel extends BaseCipherPanel {
    public BinaryCipherPanel() {
        super("Binary Cipher");
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
            String text = getInputText();
            if (encrypt) {
                StringBuilder sb = new StringBuilder();
                for (char c : text.toCharArray()) {
                    sb.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0')).append(" ");
                }
                outputArea.setText(sb.toString());
            } else {
                String[] parts = text.split("\\s+");
                StringBuilder sb = new StringBuilder();
                for (String p : parts) {
                    sb.append((char) Integer.parseInt(p, 2));
                }
                outputArea.setText(sb.toString());
            }
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
