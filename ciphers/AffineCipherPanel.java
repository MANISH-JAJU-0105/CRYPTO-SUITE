package ciphers;

import components.*;

import javax.swing.*;
import java.awt.*;

public class AffineCipherPanel extends BaseCipherPanel {
    JTextField aField, bField;

    public AffineCipherPanel() {
        super("Affine Cipher");
    }

    @Override
    protected void addControls(JPanel p) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        // Group inputs
        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        inputRow.setOpaque(false);

        JPanel aPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        aPanel.setOpaque(false);
        aPanel.add(new JLabel("<html><font color='white'>a:</font></html>"));
        aField = createTextField("5");
        aPanel.add(aField);
        inputRow.add(aPanel);

        JPanel bPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        bPanel.setOpaque(false);
        bPanel.add(new JLabel("<html><font color='white'>b:</font></html>"));
        bField = createTextField("8");
        bPanel.add(bField);
        inputRow.add(bPanel);

        container.add(inputRow);

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
        aField.setText("5");
        bField.setText("8");
    }

    private void process(boolean encrypt) {
        try {
            int a = Integer.parseInt(aField.getText());
            int b = Integer.parseInt(bField.getText());
            String text = getInputText().toUpperCase();
            StringBuilder sb = new StringBuilder();
            int aInv = 0;
            if (!encrypt) {
                for (int i = 0; i < 26; i++)
                    if ((a * i) % 26 == 1)
                        aInv = i;
                if (aInv == 0)
                    throw new Exception("'a' must be coprime to 26");
            }
            for (char c : text.toCharArray()) {
                if (Character.isLetter(c)) {
                    int x = c - 'A';
                    if (encrypt)
                        sb.append((char) (((a * x + b) % 26) + 'A'));
                    else {
                        int val = (aInv * (x - b)) % 26;
                        if (val < 0)
                            val += 26;
                        sb.append((char) (val + 'A'));
                    }
                } else
                    sb.append(c);
            }
            outputArea.setText(sb.toString());
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
