package ciphers;
import components.*;

import javax.crypto.Cipher;
import javax.swing.*;
import java.awt.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

public class RSAPanel extends BaseCipherPanel {
    KeyPair kp;

    public RSAPanel() {
        super("RSA Encryption");
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            kp = kpg.generateKeyPair();
        } catch (Exception e) {
        }
    }

    @Override
    protected void addControls(JPanel p) {
        JButton gen = new NeonButton("New Keys", new Color(100, 100, 100));
        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));

        gen.addActionListener(e -> {
            try {
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(1024);
                kp = kpg.generateKeyPair();
                outputArea.setText("New KeyPair Generated!");
            } catch (Exception ex) {
            }
        });

        enc.addActionListener(e -> measureAndRun(() -> {
            try {
                Cipher c = Cipher.getInstance("RSA");
                c.init(Cipher.ENCRYPT_MODE, kp.getPublic());
                byte[] b = c.doFinal(getInputText().getBytes());
                outputArea.setText(Base64.getEncoder().encodeToString(b));
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }, true));

        dec.addActionListener(e -> measureAndRun(() -> {
            try {
                Cipher c = Cipher.getInstance("RSA");
                c.init(Cipher.DECRYPT_MODE, kp.getPrivate());
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
