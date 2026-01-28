package ciphers;

import components.*;
import javax.swing.*;
import java.awt.*;

public class TwofishPanel extends BaseCipherPanel {
    public TwofishPanel() {
        super("Twofish (Simulated)");
    }

    @Override
    protected void addControls(JPanel p) {
        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));

        enc.addActionListener(e -> measureAndRun(
                () -> outputArea.setText("Twofish Encrypt (Simulated)"), true));

        dec.addActionListener(e -> measureAndRun(
                () -> outputArea.setText("Twofish Decrypt (Simulated)"), false));

        p.add(enc);
        p.add(dec);
    }
}
