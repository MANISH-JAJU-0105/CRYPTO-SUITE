package ciphers;

import components.*;
import javax.swing.*;
import java.awt.*;

public class ScytaleCipherPanel extends BaseCipherPanel {
    private JTextField diameterField;

    public ScytaleCipherPanel() {
        super("Scytale Cipher");
    }

    @Override
    protected void addControls(JPanel p) {
        p.add(new JLabel("<html><font color='white'>Diameter:</font></html>"));
        diameterField = createTextField("4");
        p.add(diameterField);

        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));

        enc.addActionListener(e -> measureAndRun(() -> process(true), true));
        dec.addActionListener(e -> measureAndRun(() -> process(false), false));

        p.add(enc);
        p.add(dec);
    }

    @Override
    protected void resetFields() {
        diameterField.setText("4");
    }

    private void process(boolean encrypt) {
        try {
            String text = getInputText();
            int diameter = Integer.parseInt(diameterField.getText());
            if (diameter <= 0)
                throw new IllegalArgumentException("Diameter must be positive");

            outputArea.setText(encrypt ? encryptScytale(text, diameter) : decryptScytale(text, diameter));
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }

    private String encryptScytale(String text, int diameter) {
        StringBuilder result = new StringBuilder();
        int len = text.length();
        for (int i = 0; i < diameter; i++) {
            for (int j = i; j < len; j += diameter) {
                result.append(text.charAt(j));
            }
        }
        return result.toString();
    }

    private String decryptScytale(String text, int diameter) {
        int len = text.length();
        // Inverse of encryption logic
        char[] decrypted = new char[len];
        int idx = 0;
        for (int i = 0; i < diameter; i++) {
            for (int j = i; j < len; j += diameter) {
                decrypted[j] = text.charAt(idx++);
            }
        }
        return new String(decrypted);

    }
}
