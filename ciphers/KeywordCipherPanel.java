package ciphers;
import components.*;

import javax.swing.*;
import java.awt.*;

public class KeywordCipherPanel extends BaseCipherPanel {
    JTextField keyField;

    public KeywordCipherPanel() {
        super("Keyword Cipher");
    }

    @Override
    protected void addControls(JPanel p) {
        p.add(new JLabel("<html><font color='white'>Keyword:</font></html>"));
        keyField = createTextField("KEYWORD");
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
        keyField.setText("KEYWORD");
    }

    private void process(boolean encrypt) {
        try {
            String text = getInputText().toUpperCase();
            String key = keyField.getText().toUpperCase().replaceAll("[^A-Z]", "");
            String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String cipherAlpha = "";
            for (char c : key.toCharArray())
                if (!cipherAlpha.contains("" + c))
                    cipherAlpha += c;
            for (char c : alphabet.toCharArray())
                if (!cipherAlpha.contains("" + c))
                    cipherAlpha += c;

            StringBuilder sb = new StringBuilder();
            for (char c : text.toCharArray()) {
                if (Character.isLetter(c)) {
                    int idx = encrypt ? alphabet.indexOf(c) : cipherAlpha.indexOf(c);
                    sb.append(encrypt ? cipherAlpha.charAt(idx) : alphabet.charAt(idx));
                } else
                    sb.append(c);
            }
            outputArea.setText(sb.toString());
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
