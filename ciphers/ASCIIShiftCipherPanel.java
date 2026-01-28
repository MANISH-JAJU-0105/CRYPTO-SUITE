package ciphers;

import components.*;
import javax.swing.*;
import java.awt.*;

public class ASCIIShiftCipherPanel extends BaseCipherPanel {
    JTextField shiftField;

    public ASCIIShiftCipherPanel() {
        super("ASCII Shift Cipher");
    }

    @Override
    protected void addControls(JPanel p) {
        p.add(new JLabel("<html><font color='white'>Shift:</font></html>"));
        shiftField = createTextField("1");
        p.add(shiftField);
        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));
        enc.addActionListener(e -> measureAndRun(() -> process(true), true));
        dec.addActionListener(e -> measureAndRun(() -> process(false), false));
        p.add(enc);
        p.add(dec);
    }

    @Override
    protected void resetFields() {
        shiftField.setText("1");
    }

    private void process(boolean encrypt) {
        try {
            String text = getInputText();
            int shift = Integer.parseInt(shiftField.getText());
            StringBuilder sb = new StringBuilder();
            for (char c : text.toCharArray()) {
                sb.append((char) (c + (encrypt ? shift : -shift)));
            }
            outputArea.setText(sb.toString());
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }
}
