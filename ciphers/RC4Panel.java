package ciphers;

import components.*;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.util.Base64;

public class RC4Panel extends BaseCipherPanel {
    private JTextField keyField;

    public RC4Panel() {
        super("RC4 Cipher");
    }

    @Override
    protected void addControls(JPanel p) {
        p.add(new JLabel("Key (Base64):"));
        keyField = createTextField("");
        keyField.setPreferredSize(new Dimension(200, 30));
        p.add(keyField);

        JButton gen = new NeonButton("Gen Key", new Color(100, 100, 100));
        gen.addActionListener(e -> generateKey());
        p.add(gen);

        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        enc.addActionListener(e -> measureAndRun(() -> process(true), true));
        p.add(enc);

        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));
        dec.addActionListener(e -> measureAndRun(() -> process(false), false));
        p.add(dec);

        generateKey();
    }

    private void generateKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("RC4");
            kg.init(128);
            SecretKey sk = kg.generateKey();
            keyField.setText(Base64.getEncoder().encodeToString(sk.getEncoded()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process(boolean encrypt) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(keyField.getText());
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "RC4");

            Cipher cipher = Cipher.getInstance("RC4");
            cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec);

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
