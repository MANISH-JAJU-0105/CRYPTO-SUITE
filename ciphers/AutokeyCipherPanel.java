package ciphers;
import components.*;

import javax.swing.*;
import java.awt.*;

public class AutokeyCipherPanel extends BaseCipherPanel {
    JTextField keyField;

    public AutokeyCipherPanel() {
        super("Autokey Cipher");
    }

    @Override
    protected void addControls(JPanel p) {
        p.add(new JLabel("<html><font color='white'>Key:</font></html>"));
        keyField = createTextField("KEY");
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
        keyField.setText("KEY");
    }

    private void process(boolean encrypt) {
        try {
            String text = getInputText().toUpperCase().replaceAll("[^A-Z]", "");
            String key = keyField.getText().toUpperCase().replaceAll("[^A-Z]", "");
            StringBuilder sb = new StringBuilder();
            if (encrypt) {
                String currentKey = key + text;
                for (int i = 0; i < text.length(); i++) {
                    int p = text.charAt(i) - 'A';
                    int k = currentKey.charAt(i) - 'A';
                    sb.append((char) (((p + k) % 26) + 'A'));
                }
            } else {
                StringBuilder currentKey = new StringBuilder(key);
                for (int i = 0; i < text.length(); i++) {
                    int c = text.charAt(i) - 'A';
                    int k = currentKey.charAt(i) - 'A';
                    int p = (c - k + 26) % 26;
                    char pChar = (char) (p + 'A');
                    sb.append(pChar);
                    currentKey.append(pChar);
                }
            }
            outputArea.setText(sb.toString());
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
