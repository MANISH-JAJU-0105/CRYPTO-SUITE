package ciphers;

import components.*;
import javax.swing.*;
import java.awt.*;

public class GronsfeldCipherPanel extends BaseCipherPanel {
    private JTextField keyField;

    public GronsfeldCipherPanel() {
        super("Gronsfeld Cipher");
    }

    @Override
    protected void addControls(JPanel p) {
        p.add(new JLabel("<html><font color='white'>Key (Numbers):</font></html>"));
        keyField = createTextField("1234");
        p.add(keyField);

        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));

        enc.addActionListener(e -> measureAndRun(() -> process(true), true));
        dec.addActionListener(e -> measureAndRun(() -> process(false), false));

        p.add(enc);
        p.add(dec);
    }

    @Override
    protected void resetFields() {
        keyField.setText("1234");
    }

    private void process(boolean encrypt) {
        try {
            String text = getInputText().toUpperCase();
            String key = keyField.getText().replaceAll("[^0-9]", "");
            if (key.isEmpty())
                throw new IllegalArgumentException("Key must contain numbers");

            StringBuilder result = new StringBuilder();
            int keyIdx = 0;

            for (char c : text.toCharArray()) {
                if (c < 'A' || c > 'Z') {
                    result.append(c);
                    continue;
                }

                int shift = key.charAt(keyIdx % key.length()) - '0';
                int val = c - 'A';

                if (encrypt) {
                    val = (val + shift) % 26;
                } else {
                    val = (val - shift + 26) % 26;
                }

                result.append((char) (val + 'A'));
                keyIdx++;
            }
            outputArea.setText(result.toString());
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
