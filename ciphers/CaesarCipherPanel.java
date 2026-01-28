package ciphers;

import components.*;

import javax.swing.*;
import java.awt.*;

public class CaesarCipherPanel extends BaseCipherPanel {
    JTextField shiftField;

    public CaesarCipherPanel() {
        super("Caesar Cipher");
    }

    @Override
    protected void addControls(JPanel p) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        // Input
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        inputPanel.setOpaque(false);
        inputPanel.add(new JLabel("<html><font color='white'>Shift:</font></html>"));
        shiftField = createTextField("3");
        shiftField.setPreferredSize(new Dimension(100, 30));
        inputPanel.add(shiftField);
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
        shiftField.setText("3");
    }

    private void process(boolean encrypt) {
        try {
            String text = getInputText();
            int shift = Integer.parseInt(shiftField.getText());
            StringBuilder res = new StringBuilder();
            for (char c : text.toCharArray()) {
                if (Character.isLetter(c)) {
                    char base = Character.isUpperCase(c) ? 'A' : 'a';
                    int val = c - base;
                    int s = encrypt ? shift : -shift;
                    val = (val + s) % 26;
                    if (val < 0)
                        val += 26;
                    res.append((char) (base + val));
                } else
                    res.append(c);
            }
            outputArea.setText(res.toString());
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
