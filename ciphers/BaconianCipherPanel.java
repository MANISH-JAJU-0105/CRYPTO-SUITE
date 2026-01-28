package ciphers;
import components.*;

import javax.swing.*;
import java.awt.*;

public class BaconianCipherPanel extends BaseCipherPanel {
    public BaconianCipherPanel() {
        super("Baconian Cipher");
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
            String[] codes = { "AAAAA", "AAAAB", "AAABA", "AAABB", "AABAA", "AABAB", "AABBA", "AABBB", "ABAAA",
                    "ABAAB", "ABABA", "ABABB", "ABBAA", "ABBAB", "ABBBA", "ABBBB", "BAAAA", "BAAAB", "BAABA",
                    "BAABB", "BABAA", "BABAB", "BABBA", "BABBB", "BBAAA", "BBAAB" };
            if (encrypt) {
                StringBuilder sb = new StringBuilder();
                for (char c : text.toCharArray()) {
                    if (c >= 'A' && c <= 'Z')
                        sb.append(codes[c - 'A']).append(" ");
                }
                outputArea.setText(sb.toString());
            } else {
                StringBuilder sb = new StringBuilder();
                String[] parts = text.split("\\s+");
                for (String p : parts) {
                    for (int i = 0; i < codes.length; i++) {
                        if (codes[i].equals(p))
                            sb.append((char) ('A' + i));
                    }
                }
                outputArea.setText(sb.toString());
            }
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
