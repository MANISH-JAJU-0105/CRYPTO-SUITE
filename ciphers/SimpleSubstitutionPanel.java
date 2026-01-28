package ciphers;

import components.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleSubstitutionPanel extends BaseCipherPanel {
    private JTextField keyField;
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public SimpleSubstitutionPanel() {
        super("Simple Substitution");
    }

    @Override
    protected void addControls(JPanel p) {
        p.add(new JLabel("<html><font color='white'>Key (26 chars):</font></html>"));
        keyField = createTextField("");
        keyField.setPreferredSize(new Dimension(250, 30));
        p.add(keyField);

        JButton gen = new NeonButton("Random Key", new Color(100, 100, 100));
        gen.addActionListener(e -> generateKey());
        p.add(gen);

        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));

        enc.addActionListener(e -> measureAndRun(() -> process(true), true));
        dec.addActionListener(e -> measureAndRun(() -> process(false), false));

        p.add(enc);
        p.add(dec);

        generateKey();
    }

    private void generateKey() {
        List<Character> chars = new ArrayList<>();
        for (char c : ALPHABET.toCharArray()) {
            chars.add(c);
        }
        Collections.shuffle(chars);
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            sb.append(c);
        }
        keyField.setText(sb.toString());
    }

    @Override
    protected void resetFields() {
        generateKey();
    }

    private void process(boolean encrypt) {
        try {
            String key = keyField.getText().toUpperCase().replaceAll("[^A-Z]", "");
            if (key.length() != 26) {
                // Check if it has duplicates or missing chars?
                // For now just warn length
                if (key.length() < 26)
                    throw new IllegalArgumentException("Key must be 26 unique letters.");
            }

            String text = getInputText().toUpperCase();
            StringBuilder result = new StringBuilder();

            String source = encrypt ? ALPHABET : key;
            String target = encrypt ? key : ALPHABET;

            for (char c : text.toCharArray()) {
                int index = source.indexOf(c);
                if (index != -1) {
                    result.append(target.charAt(index));
                } else {
                    result.append(c);
                }
            }
            outputArea.setText(result.toString());
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
