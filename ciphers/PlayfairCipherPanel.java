package ciphers;

import components.*;
import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class PlayfairCipherPanel extends BaseCipherPanel {
    private JTextField keyField;
    private char[][] matrix = new char[5][5];

    public PlayfairCipherPanel() {
        super("Playfair Cipher");
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
        keyField = createTextField("PLAYFAIR");
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
        keyField.setText("PLAYFAIR");
    }

    private void process(boolean encrypt) {
        try {
            String key = keyField.getText().toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
            String text = getInputText().toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");

            if (key.isEmpty()) {
                outputArea.setText("Error: Key cannot be empty.");
                return;
            }

            generateMatrix(key);

            String result;
            if (encrypt) {
                String prepared = prepareText(text);
                result = transform(prepared, true);
            } else {
                result = transform(text, false);
            }

            outputArea.setText(result);

        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void generateMatrix(String key) {
        Set<Character> used = new HashSet<>();
        StringBuilder sb = new StringBuilder();

        for (char c : key.toCharArray()) {
            if (!used.contains(c)) {
                used.add(c);
                sb.append(c);
            }
        }

        for (char c = 'A'; c <= 'Z'; c++) {
            if (c == 'J')
                continue;
            if (!used.contains(c)) {
                used.add(c);
                sb.append(c);
            }
        }

        int k = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j] = sb.charAt(k++);
            }
        }
    }

    private String prepareText(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            sb.append(c);
            if (i + 1 < text.length()) {
                if (text.charAt(i + 1) == c) {
                    sb.append('X');
                }
            }
        }
        if (sb.length() % 2 != 0) {
            sb.append('X');
        }
        return sb.toString();
    }

    private String transform(String text, boolean encrypt) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i);
            char b = text.charAt(i + 1);
            int[] posA = findPos(a);
            int[] posB = findPos(b);

            int r1 = posA[0], c1 = posA[1];
            int r2 = posB[0], c2 = posB[1];

            if (r1 == r2) { // Same row
                c1 = (c1 + (encrypt ? 1 : 4)) % 5;
                c2 = (c2 + (encrypt ? 1 : 4)) % 5;
            } else if (c1 == c2) { // Same column
                r1 = (r1 + (encrypt ? 1 : 4)) % 5;
                r2 = (r2 + (encrypt ? 1 : 4)) % 5;
            } else { // Rectangle
                int temp = c1;
                c1 = c2;
                c2 = temp;
            }

            sb.append(matrix[r1][c1]);
            sb.append(matrix[r2][c2]);
        }
        return sb.toString();
    }

    private int[] findPos(char c) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (matrix[i][j] == c) {
                    return new int[] { i, j };
                }
            }
        }
        return new int[] { 0, 0 }; // Should not happen
    }
}
