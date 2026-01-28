package ciphers;

import components.*;
import javax.swing.*;
import java.awt.*;

public class ROT13Panel extends BaseCipherPanel {
    public ROT13Panel() {
        super("ROT13");
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
            String text = getInputText();
            StringBuilder sb = new StringBuilder();
            for (char c : text.toCharArray()) {
                if (c >= 'a' && c <= 'z')
                    sb.append((char) ((c - 'a' + 13) % 26 + 'a'));
                else if (c >= 'A' && c <= 'Z')
                    sb.append((char) ((c - 'A' + 13) % 26 + 'A'));
                else
                    sb.append(c);
            }
            outputArea.setText(sb.toString());
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
