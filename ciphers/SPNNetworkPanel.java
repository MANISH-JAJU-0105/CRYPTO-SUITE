package ciphers;
import components.*;
import javax.swing.*;
import java.awt.*;

public class SPNNetworkPanel extends BaseCipherPanel {
    public SPNNetworkPanel() {
        super("SPN Network (Simulated)");
    }

    @Override
    protected void addControls(JPanel p) {
        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));
        enc.addActionListener(e -> measureAndRun(() -> runSimulation(true), true));
        dec.addActionListener(e -> measureAndRun(() -> runSimulation(false), false));
        p.add(enc);
        p.add(dec);
    }
}
