package components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ContentPanel extends JPanel {
    private JPanel mainContainer;
    private Component currentComponent;
    private BufferedImage transitionImage;
    private float alpha = 0.0f;
    private Timer transitionTimer;
    private boolean isAnimating = false;

    public ContentPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.MAIN_BG);

        mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(Theme.MAIN_BG);
        add(mainContainer, BorderLayout.CENTER);

        transitionTimer = new Timer(15, e -> {
            alpha -= 0.08f; // Speed of fade
            if (alpha <= 0) {
                alpha = 0;
                isAnimating = false;
                transitionImage = null; // Free memory
                ((Timer) e.getSource()).stop();
                repaint();
            } else {
                repaint();
            }
        });
    }

    public void showPanel(JComponent panel) {
        if (currentComponent == panel)
            return;

        // If we are currently showing something, capture it
        if (currentComponent != null && getWidth() > 0 && getHeight() > 0) {
            transitionImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = transitionImage.createGraphics();
            paint(g2); // Paint everything (including current animation state if any)
            g2.dispose();

            alpha = 1.0f;
            isAnimating = true;
            transitionTimer.restart();
        }

        mainContainer.removeAll();
        mainContainer.add(panel, BorderLayout.CENTER);
        currentComponent = panel;

        mainContainer.revalidate();
        mainContainer.repaint();
    }

    // Helper for CardLayout-style usage if needed, though simpler to use showPanel
    // We will just expose showPanel for direct usage.

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);

        // Draw the transition image on top
        if (isAnimating && transitionImage != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.drawImage(transitionImage, 0, 0, null);
            g2.dispose();
        }
    }
}
