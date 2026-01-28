package ciphers;

import components.*;
import javax.swing.*;
import java.awt.*;

public class TrithemeCipherPanel extends BaseCipherPanel {
    public TrithemeCipherPanel() {
        super("Tritheme Cipher");
    }

    @Override
    protected void addControls(JPanel p) {
        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));

        enc.addActionListener(e -> measureAndRun(() -> {
            String text = inputArea.getText().toUpperCase();
            StringBuilder sb = new StringBuilder();
            int shift = 0;
            for (char c : text.toCharArray()) {
                if (Character.isLetter(c)) {
                    sb.append((char) ((c - 'A' + shift) % 26 + 'A'));
                    shift++;
                } else
                    sb.append(c);
            }
            outputArea.setText(sb.toString());
        }, true));

        dec.addActionListener(e -> measureAndRun(() -> outputArea.setText("Tritheme Decrypt (Simulated)"), false));

        p.add(enc);
        p.add(dec);
    }
}
