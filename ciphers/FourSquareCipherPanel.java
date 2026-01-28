package ciphers;

import components.*;
import javax.swing.*;
import java.awt.*;

public class FourSquareCipherPanel extends BaseCipherPanel {
    public FourSquareCipherPanel() {
        super("Four-Square Cipher (Simulated)");
    }

    @Override
    protected void addControls(JPanel p) {
        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));

        enc.addActionListener(e -> measureAndRun(
                () -> outputArea.setText("Simulation Encrypt: " + new StringBuilder(inputArea.getText()).reverse()),
                true));

        dec.addActionListener(e -> measureAndRun(
                () -> outputArea.setText("Simulation Decrypt: " + new StringBuilder(inputArea.getText()).reverse()),
                false));

        p.add(enc);
        p.add(dec);
    }
}
