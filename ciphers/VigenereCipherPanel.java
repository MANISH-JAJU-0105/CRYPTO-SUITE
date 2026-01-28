package ciphers;

import components.*;

import javax.swing.*;
import java.awt.*;

public class VigenereCipherPanel extends BaseCipherPanel {
    JTextField keyField;

    public VigenereCipherPanel() {
        super("Vigenere Cipher");
    }

    @Override
    protected void addControls(JPanel p) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        // Input
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        inputPanel.setOpaque(false);
        inputPanel.add(new JLabel("<html><font color='white'>Key:</font></html>"));
        keyField = createTextField("KEY");
        keyField.setPreferredSize(new Dimension(150, 30));
        inputPanel.add(keyField);
        container.add(inputPanel);

        container.add(Box.createVerticalStrut(10));

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        btnPanel.setOpaque(false);
        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));

        enc.addActionListener(e -> measureAndRun(() -> process(true), true));
        dec.addActionListener(e -> measureAndRun(() -> process(false), false));

        btnPanel.add(enc);
        btnPanel.add(dec);
        container.add(btnPanel);

        p.add(container);
    }

    @Override
    protected void resetFields() {
        keyField.setText("KEY");
    }

    private void process(boolean encrypt) {
        try {
            String text = getInputText().toUpperCase();
            String key = keyField.getText().toUpperCase().replaceAll("[^A-Z]", "");
            StringBuilder res = new StringBuilder();
            for (int i = 0, j = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c < 'A' || c > 'Z') {
                    res.append(c);
                    continue;
                }
                int m = c - 'A';
                int k = key.charAt(j % key.length()) - 'A';
                int val = encrypt ? (m + k) % 26 : (m - k + 26) % 26;
                res.append((char) (val + 'A'));
                j++;
            }
            outputArea.setText(res.toString());
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
