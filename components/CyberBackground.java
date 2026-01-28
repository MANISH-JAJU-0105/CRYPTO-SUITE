package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CyberBackground extends JPanel {
    private List<Particle> particles = new ArrayList<>();
    private Timer animTimer;
    private Random random = new Random();

    public CyberBackground() {
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0)); // Transparent

        // Initialize particles
        for (int i = 0; i < 50; i++) {
            particles.add(new Particle());
        }

        animTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Particle p : particles) {
                    p.update();
                }
                repaint();
            }
        });
        animTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Draw connections
        g2.setColor(new Color(Theme.ACCENT_COLOR.getRed(), Theme.ACCENT_COLOR.getGreen(), Theme.ACCENT_COLOR.getBlue(),
                30));
        g2.setStroke(new BasicStroke(1f));

        for (int i = 0; i < particles.size(); i++) {
            Particle p1 = particles.get(i);
            for (int j = i + 1; j < particles.size(); j++) {
                Particle p2 = particles.get(j);
                double dist = Math.hypot(p1.x - p2.x, p1.y - p2.y);
                if (dist < 150) {
                    int alpha = (int) ((150 - dist) / 150.0 * 40);
                    g2.setColor(new Color(Theme.ACCENT_COLOR.getRed(), Theme.ACCENT_COLOR.getGreen(),
                            Theme.ACCENT_COLOR.getBlue(), alpha));
                    g2.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
                }
            }
        }

        // Draw particles
        for (Particle p : particles) {
            g2.setColor(new Color(Theme.ACCENT_COLOR.getRed(), Theme.ACCENT_COLOR.getGreen(),
                    Theme.ACCENT_COLOR.getBlue(), (int) (p.opacity * 255)));
            g2.fillOval((int) p.x - 2, (int) p.y - 2, 4, 4);
        }
    }

    private class Particle {
        double x, y;
        double vx, vy;
        float opacity;

        public Particle() {
            reset(true);
        }

        void reset(boolean randomY) {
            x = random.nextInt(Math.max(1, getWidth() > 0 ? getWidth() : 800));
            y = randomY ? random.nextInt(Math.max(1, getHeight() > 0 ? getHeight() : 600)) : getHeight() + 10;
            vx = (random.nextDouble() - 0.5) * 1.5;
            vy = -(random.nextDouble() * 1.0 + 0.2); // Move upwards
            opacity = random.nextFloat() * 0.5f + 0.1f;
        }

        void update() {
            x += vx;
            y += vy;

            if (y < -10 || x < -10 || x > getWidth() + 10) {
                reset(false);
                y = getHeight() + 10;
            }
        }
    }
}
