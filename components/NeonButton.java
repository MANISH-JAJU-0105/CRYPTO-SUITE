package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NeonButton extends JButton {
    private Color baseColor;
    private boolean isHovered = false;

    public NeonButton(String text, Color color) {
        super(text);
        this.baseColor = color;
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setForeground(Color.WHITE);
        setBackground(color);
        setFocusPainted(false);
        setBorder(new EmptyBorder(10, 25, 10, 25));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setContentAreaFilled(false);
        setOpaque(false);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                isHovered = true;
                repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                isHovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isHovered) {
            g2.setColor(baseColor.brighter());
            // Glow effect
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2.setColor(baseColor);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        } else {
            g2.setColor(baseColor.darker());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        }

        // Text
        super.paintComponent(g2);
        g2.dispose();
    }
}
