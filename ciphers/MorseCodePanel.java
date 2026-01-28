package ciphers;

import components.*;

import javax.swing.*;
import java.awt.*;

public class MorseCodePanel extends BaseCipherPanel {
    String[] alpha = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", " " };
    String[] morse = { ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..", "--",
            "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", ".----",
            "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.", "-----", "/" };

    public MorseCodePanel() {
        super("Morse Code");
    }

    @Override
    protected void addControls(JPanel p) {
        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));
        enc.addActionListener(e -> measureAndRun(() -> process(true), true));
        dec.addActionListener(e -> measureAndRun(() -> process(false), false));
        p.add(enc);
        p.add(dec);
    }

    private void process(boolean encrypt) {
        try {
            String text = getInputText().toUpperCase();
            StringBuilder sb = new StringBuilder();
            if (encrypt) {
                for (char c : text.toCharArray()) {
                    for (int i = 0; i < alpha.length; i++) {
                        if (alpha[i].equals("" + c)) {
                            sb.append(morse[i]).append(" ");
                            break;
                        }
                    }
                }
            } else {
                String[] parts = text.split("\\s+");
                for (String p : parts) {
                    for (int i = 0; i < morse.length; i++) {
                        if (morse[i].equals(p)) {
                            sb.append(alpha[i]);
                            break;
                        }
                    }
                }
            }
            outputArea.setText(sb.toString());
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
