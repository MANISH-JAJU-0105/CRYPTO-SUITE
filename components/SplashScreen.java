package components;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {

    public SplashScreen() {
        setSize(500, 300);
        setLocationRelativeTo(null);
        setBackground(new Color(0, 0, 0, 0)); // Translucent window support

        JPanel content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background with rounded corners
                g2.setColor(Theme.MAIN_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Border
                g2.setColor(Theme.ACCENT_COLOR);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30);
            }
        };
        content.setLayout(null);
        content.setOpaque(false);
        setContentPane(content);

        // Logo / Text
        JLabel title = new JLabel("CRYPTO SUITE");
        title.setFont(Theme.FONT_TITLE.deriveFont(32f));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 80, 500, 40);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        content.add(title);

        JLabel subtitle = new JLabel("Initializing Secure Modules...");
        subtitle.setFont(Theme.FONT_NORMAL);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        subtitle.setBounds(0, 130, 500, 20);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        content.add(subtitle);

        // Custom Progress Bar
        JProgressBar bar = new JProgressBar();
        bar.setBounds(100, 180, 300, 6);
        bar.setBackground(new Color(40, 40, 40));
        bar.setForeground(Theme.ACCENT_COLOR);
        bar.setBorderPainted(false);
        content.add(bar);

        // Simulate Loading in a thread
        new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i++) {
                    bar.setValue(i);
                    Thread.sleep(25); // 2.5 seconds load
                }
                SwingUtilities.invokeLater(() -> {
                    dispose();
                    // Callback to main would be here, but we'll handle this in main() logic
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void showSplash() {
        setVisible(true);
        try {
            // Block partially to simulate wait, or just let the internal thread handle it?
            // Better: Blocking method for main to call
            Thread.sleep(2800);
        } catch (Exception e) {
        }
    }
}
