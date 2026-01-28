package ciphers;
import components.*;

import javax.crypto.Cipher;
import javax.swing.*;
import java.awt.*;
import java.util.Base64;

public class AESPanel extends BaseCipherPanel {
    javax.crypto.SecretKey key;

    public AESPanel() {
        super("AES (Rijndael)");
        try {
            javax.crypto.KeyGenerator kg = javax.crypto.KeyGenerator.getInstance("AES");
            kg.init(128);
            key = kg.generateKey();
        } catch (Exception e) {
        }
    }

    @Override
    protected void addControls(JPanel p) {
        JButton gen = new NeonButton("New Key", new Color(100, 100, 100));
        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));
        gen.addActionListener(e -> {
            try {
                javax.crypto.KeyGenerator kg = javax.crypto.KeyGenerator.getInstance("AES");
                kg.init(128);
                key = kg.generateKey();
                outputArea.setText("New AES Key Generated!");
            } catch (Exception ex) {
            }
        });
        enc.addActionListener(e -> measureAndRun(() -> {
            try {
                Cipher c = Cipher.getInstance("AES");
                c.init(Cipher.ENCRYPT_MODE, key);
                byte[] b = c.doFinal(getInputText().getBytes());
                outputArea.setText(Base64.getEncoder().encodeToString(b));
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }, true));
        dec.addActionListener(e -> measureAndRun(() -> {
            try {
                Cipher c = Cipher.getInstance("AES");
                c.init(Cipher.DECRYPT_MODE, key);
                byte[] b = c.doFinal(Base64.getDecoder().decode(getInputText()));
                outputArea.setText(new String(b));
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }, false));
        p.add(gen);
        p.add(enc);
        p.add(dec);
    }
}
