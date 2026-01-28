package ciphers;

import components.*;
import javax.swing.*;
import java.awt.*;

public class VernamCipherPanel extends BaseCipherPanel {
    JTextField keyField;

    public VernamCipherPanel() {
        super("Vernam Cipher");
    }

    @Override
    protected void addControls(JPanel p) {
        p.add(new JLabel("<html><font color='white'>Key:</font></html>"));
        keyField = createTextField("");
        p.add(keyField);

        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));

        enc.addActionListener(e -> measureAndRun(this::process, true));
        dec.addActionListener(e -> measureAndRun(this::process, false));

        p.add(enc);
        p.add(dec);
    }

    @Override
    protected void resetFields() {
        keyField.setText("");
    }

    private void process() {
        try {
            String text = getInputText();
            String key = keyField.getText();
            if (text.length() != key.length())
                throw new RuntimeException("Key length must equal text length");
            StringBuilder res = new StringBuilder();
            for (int i = 0; i < text.length(); i++)
                res.append((char) (text.charAt(i) ^ key.charAt(i)));
            outputArea.setText(res.toString());
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
