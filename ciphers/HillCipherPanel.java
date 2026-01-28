package ciphers;

import components.*;

import javax.swing.*;
import java.awt.*;

public class HillCipherPanel extends BaseCipherPanel {
    JTextField keyField;

    public HillCipherPanel() {
        super("Hill Cipher (2x2)");
    }

    @Override
    protected void addControls(JPanel p) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        // Input
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        inputPanel.setOpaque(false);
        inputPanel.add(new JLabel("<html><font color='white'>Key (4):</font></html>"));
        keyField = createTextField("GYBN");
        keyField.setPreferredSize(new Dimension(100, 30));
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
        keyField.setText("GYBN");
    }

    private void process(boolean encrypt) {
        try {
            String text = getInputText().toUpperCase().replaceAll("[^A-Z]", "");
            String key = keyField.getText().toUpperCase().replaceAll("[^A-Z]", "");
            if (key.length() != 4)
                throw new RuntimeException("Key must be 4 letters");
            if (text.length() % 2 != 0)
                text += "X";

            int[][] K = { { key.charAt(0) - 'A', key.charAt(1) - 'A' },
                    { key.charAt(2) - 'A', key.charAt(3) - 'A' } };
            int det = (K[0][0] * K[1][1] - K[0][1] * K[1][0]) % 26;
            if (det < 0)
                det += 26;

            if (!encrypt) {
                int detInv = -1;
                for (int i = 0; i < 26; i++)
                    if ((det * i) % 26 == 1) {
                        detInv = i;
                        break;
                    }
                if (detInv == -1)
                    throw new RuntimeException("Key not invertible");

                int temp = K[0][0];
                K[0][0] = K[1][1];
                K[1][1] = temp;
                K[0][1] = -K[0][1];
                K[1][0] = -K[1][0];

                for (int i = 0; i < 2; i++)
                    for (int j = 0; j < 2; j++) {
                        K[i][j] = (K[i][j] * detInv) % 26;
                        if (K[i][j] < 0)
                            K[i][j] += 26;
                    }
            }

            StringBuilder res = new StringBuilder();
            for (int i = 0; i < text.length(); i += 2) {
                int r1 = text.charAt(i) - 'A';
                int r2 = text.charAt(i + 1) - 'A';
                int c1 = (K[0][0] * r1 + K[0][1] * r2) % 26;
                int c2 = (K[1][0] * r1 + K[1][1] * r2) % 26;
                res.append((char) (c1 + 'A')).append((char) (c2 + 'A'));
            }
            outputArea.setText(res.toString());
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
