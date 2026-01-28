package ciphers;
import components.*;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class RailFenceCipherPanel extends BaseCipherPanel {
    JTextField depthField;

    public RailFenceCipherPanel() {
        super("Rail-Fence Cipher");
    }

    @Override
    protected void addControls(JPanel p) {
        p.add(new JLabel("<html><font color='white'>Depth:</font></html>"));
        depthField = createTextField("2");
        p.add(depthField);
        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));
        enc.addActionListener(e -> measureAndRun(() -> process(true), true));
        dec.addActionListener(e -> measureAndRun(() -> process(false), false));
        p.add(enc);
        p.add(dec);
    }

    @Override
    protected void resetFields() {
        depthField.setText("2");
    }

    private void process(boolean encrypt) {
        try {
            int depth = Integer.parseInt(depthField.getText());
            if (depth < 2)
                throw new RuntimeException("Depth >= 2");
            String text = getInputText();
            outputArea.setText(encrypt ? enc(text, depth) : dec(text, depth));
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }

    private String enc(String s, int k) {
        if (k <= 1)
            return s;
        StringBuilder[] rail = new StringBuilder[k];
        for (int i = 0; i < k; i++)
            rail[i] = new StringBuilder();
        boolean down = false;
        int row = 0;
        for (char c : s.toCharArray()) {
            rail[row].append(c);
            if (row == 0 || row == k - 1)
                down = !down;
            row += down ? 1 : -1;
        }
        StringBuilder res = new StringBuilder();
        for (StringBuilder sb : rail)
            res.append(sb);
        return res.toString();
    }

    private String dec(String s, int k) {
        if (k <= 1)
            return s;
        int len = s.length();
        char[][] rail = new char[k][len];
        for (int i = 0; i < k; i++)
            Arrays.fill(rail[i], '\n');
        boolean down = false;
        int row = 0;
        for (int i = 0; i < len; i++) {
            rail[row][i] = '*';
            if (row == 0 || row == k - 1)
                down = !down;
            row += down ? 1 : -1;
        }
        int idx = 0;
        for (int i = 0; i < k; i++)
            for (int j = 0; j < len; j++)
                if (rail[i][j] == '*' && idx < len)
                    rail[i][j] = s.charAt(idx++);
        StringBuilder res = new StringBuilder();
        down = false;
        row = 0;
        for (int i = 0; i < len; i++) {
            res.append(rail[row][i]);
            if (row == 0 || row == k - 1)
                down = !down;
            row += down ? 1 : -1;
        }
        return res.toString();
    }
}
