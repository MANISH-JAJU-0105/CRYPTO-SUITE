package ciphers;

import components.*;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.util.Base64;

public class DoubleDESPanel extends BaseCipherPanel {
    private JTextField key1Field;
    private JTextField key2Field;

    public DoubleDESPanel() {
        super("2DES");
    }

    @Override
    protected void addControls(JPanel p) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        // Row 1: Key 1
        JPanel k1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        k1Panel.setOpaque(false);
        k1Panel.add(new JLabel("<html><font color='white'>Key 1 (Base64):</font></html>"));
        key1Field = createTextField("");
        key1Field.setPreferredSize(new Dimension(150, 30));
        k1Panel.add(key1Field);
        container.add(k1Panel);

        container.add(Box.createVerticalStrut(5));

        // Row 2: Key 2
        JPanel k2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        k2Panel.setOpaque(false);
        k2Panel.add(new JLabel("<html><font color='white'>Key 2 (Base64):</font></html>"));
        key2Field = createTextField("");
        key2Field.setPreferredSize(new Dimension(150, 30));
        k2Panel.add(key2Field);
        container.add(k2Panel);

        container.add(Box.createVerticalStrut(10));

        // Row 3: Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        btnPanel.setOpaque(false);

        JButton gen = new NeonButton("Gen Keys", new Color(100, 100, 100));
        gen.addActionListener(e -> generateKeys());
        btnPanel.add(gen);

        JButton enc = new NeonButton("Encrypt", Theme.ACCENT_COLOR);
        enc.addActionListener(e -> measureAndRun(() -> process(true), true));
        btnPanel.add(enc);

        JButton dec = new NeonButton("Decrypt", new Color(200, 50, 50));
        dec.addActionListener(e -> measureAndRun(() -> process(false), false));
        btnPanel.add(dec);

        container.add(btnPanel);

        p.add(container);

        generateKeys();
    }

    private void generateKeys() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("DES");
            SecretKey sk1 = kg.generateKey();
            SecretKey sk2 = kg.generateKey();
            key1Field.setText(Base64.getEncoder().encodeToString(sk1.getEncoded()));
            key2Field.setText(Base64.getEncoder().encodeToString(sk2.getEncoded()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process(boolean encrypt) {
        try {
            byte[] k1 = Base64.getDecoder().decode(key1Field.getText());
            byte[] k2 = Base64.getDecoder().decode(key2Field.getText());
            SecretKeySpec sk1 = new SecretKeySpec(k1, "DES");
            SecretKeySpec sk2 = new SecretKeySpec(k2, "DES");

            Cipher c1 = Cipher.getInstance("DES/ECB/PKCS5Padding");
            // issues if possible, but PKCS5 is safer for text.
            // Actually, for Double DES: C = E2(E1(P)).
            // Intermediate result of E1(P) will be padded. E2 will pad again?
            // Standard practice: E1 pads, E2 treats as data.
            // Decrypt: P = D1(D2(C)).

            // Let's use PKCS5 for the outer layer and NoPadding for inner?
            // No, E1(P) produces blocks. E2 encrypts those blocks.
            // If we use ECB, we don't need IVs for simplicity here, though insecure.

            byte[] input;
            byte[] result;

            if (encrypt) {
                input = getInputText().getBytes("UTF-8");

                // Stage 1: Encrypt with Key 1
                c1.init(Cipher.ENCRYPT_MODE, sk1);
                byte[] stage1 = c1.doFinal(input);

                // Stage 2: Encrypt with Key 2 (Treat stage1 as raw bytes, maybe need padding if
                // not block aligned?
                // PKCS5Padding ensures block alignment).
                // So stage1 is multiple of 8 bytes.
                // We can use NoPadding for Stage 2 if we want, or PKCS5Padding again (double
                // padding).
                // Let's use PKCS5Padding for both to be safe and simple, though it adds size.

                Cipher c2Enc = Cipher.getInstance("DES/ECB/PKCS5Padding");
                c2Enc.init(Cipher.ENCRYPT_MODE, sk2);
                result = c2Enc.doFinal(stage1);

                outputArea.setText(Base64.getEncoder().encodeToString(result));
            } else {
                input = Base64.getDecoder().decode(getInputText());

                // Stage 1: Decrypt with Key 2
                Cipher c2Dec = Cipher.getInstance("DES/ECB/PKCS5Padding");
                c2Dec.init(Cipher.DECRYPT_MODE, sk2);
                byte[] stage1 = c2Dec.doFinal(input);

                // Stage 2: Decrypt with Key 1
                c1.init(Cipher.DECRYPT_MODE, sk1);
                result = c1.doFinal(stage1);

                String resStr = new String(result, "UTF-8");
                outputArea.setText(resStr);
            }
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
