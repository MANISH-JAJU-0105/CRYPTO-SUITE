package ciphers;

import components.*;

import javax.swing.*;
import java.math.BigInteger;
import java.security.MessageDigest;

public class HashingPanel extends BaseCipherPanel {
    String algo;

    public HashingPanel(String algo) {
        super(algo);
        this.algo = algo;
    }

    public HashingPanel() {
        this("SHA-256");
    }

    @Override
    protected void addControls(JPanel p) {
        JButton btn = new NeonButton("Generate Hash", Theme.ACCENT_COLOR);
        btn.addActionListener(e -> measureAndRun(this::process, true));
        p.add(btn);
    }

    private void process() {
        try {
            String targetAlgo = algo.equals("SHA-128") ? "SHA-256" : algo;
            MessageDigest md = MessageDigest.getInstance(targetAlgo);
            byte[] digest = md.digest(getInputText().getBytes());
            if (algo.equals("SHA-128")) {
                byte[] truncated = new byte[16];
                System.arraycopy(digest, 0, truncated, 0, 16);
                digest = truncated;
            }
            BigInteger no = new BigInteger(1, digest);
            String hashtext = no.toString(16);
            while (hashtext.length() < (digest.length * 2))
                hashtext = "0" + hashtext;
            outputArea.setText(hashtext.toUpperCase());
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
