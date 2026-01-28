package ciphers;

import components.*;
import javax.swing.*;
import java.awt.*;

public class AtbashCipherPanel extends BaseCipherPanel {
    public AtbashCipherPanel() {
        super("Atbash Cipher");
    }

    @Override
    protected void addControls(JPanel p) {
        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));

        enc.addActionListener(e -> measureAndRun(this::process, true));
        dec.addActionListener(e -> measureAndRun(this::process, false));

        p.add(enc);
        p.add(dec);
    }

    private void process() {
        try {
            String text = getInputText().toUpperCase();
            StringBuilder sb = new StringBuilder();
            for (char c : text.toCharArray()) {
                if (Character.isLetter(c))
                    sb.append((char) ('Z' - (c - 'A')));
                else
                    sb.append(c);
            }
            outputArea.setText(sb.toString());
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
