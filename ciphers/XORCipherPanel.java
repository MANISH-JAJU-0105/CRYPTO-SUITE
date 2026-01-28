package ciphers;

import components.*;
import javax.swing.*;
import java.awt.*;

public class XORCipherPanel extends BaseCipherPanel {
    JTextField keyField;

    public XORCipherPanel() {
        super("XOR Cipher");
    }

    @Override
    protected void addControls(JPanel p) {
        p.add(new JLabel("<html><font color='white'>Key (Int):</font></html>"));
        keyField = createTextField("123");
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
        keyField.setText("123");
    }

    private void process() {
        try {
            String text = getInputText();
            int key = Integer.parseInt(keyField.getText());
            StringBuilder sb = new StringBuilder();
            for (char c : text.toCharArray())
                sb.append((char) (c ^ key));
            outputArea.setText(sb.toString());
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
