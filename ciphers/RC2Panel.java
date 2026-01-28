package ciphers;

import components.*;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.security.SecureRandom;
import java.util.Base64;

public class RC2Panel extends BaseCipherPanel {
    private JTextField keyField;
    private JTextField ivField;

    public RC2Panel() {
        super("RC2 Cipher");
    }

    @Override
    protected void addControls(JPanel p) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        // Row 1: Key
        JPanel keyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        keyPanel.setOpaque(false);
        keyPanel.add(new JLabel("<html><font color='white'>Key (Base64):</font></html>"));
        keyField = createTextField("");
        keyField.setPreferredSize(new Dimension(200, 30));
        keyPanel.add(keyField);
        container.add(keyPanel);

        container.add(Box.createVerticalStrut(5));

        // Row 2: IV
        JPanel ivPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        ivPanel.setOpaque(false);
        ivPanel.add(new JLabel("<html><font color='white'>IV (Base64):</font></html>"));
        ivField = createTextField("");
        ivField.setPreferredSize(new Dimension(200, 30));
        ivPanel.add(ivField);
        container.add(ivPanel);

        container.add(Box.createVerticalStrut(10));

        // Row 3: Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        btnPanel.setOpaque(false);

        JButton gen = new NeonButton("Gen Key/IV", new Color(100, 100, 100));
        gen.addActionListener(e -> generateKeyAndIV());
        btnPanel.add(gen);

        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        enc.addActionListener(e -> measureAndRun(() -> process(true), true));
        btnPanel.add(enc);

        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));
        dec.addActionListener(e -> measureAndRun(() -> process(false), false));
        btnPanel.add(dec);

        container.add(btnPanel);

        p.add(container);

        generateKeyAndIV();
    }

    private void generateKeyAndIV() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("RC2");
            kg.init(128);
            SecretKey sk = kg.generateKey();
            keyField.setText(Base64.getEncoder().encodeToString(sk.getEncoded()));

            byte[] iv = new byte[8]; // RC2 block size 8 bytes
            new SecureRandom().nextBytes(iv);
            ivField.setText(Base64.getEncoder().encodeToString(iv));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process(boolean encrypt) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(keyField.getText());
            byte[] ivBytes = Base64.getDecoder().decode(ivField.getText());
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "RC2");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance("RC2/CBC/PKCS5Padding");
            cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] input;
            if (encrypt) {
                input = getInputText().getBytes("UTF-8");
                byte[] output = cipher.doFinal(input);
                outputArea.setText(Base64.getEncoder().encodeToString(output));
            } else {
                input = Base64.getDecoder().decode(getInputText());
                byte[] output = cipher.doFinal(input);
                String result = new String(output, "UTF-8");
                outputArea.setText(result);
                System.out.println("Decrypted Text: " + result);
            }
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
