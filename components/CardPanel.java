package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CardPanel extends JPanel {
    public CardPanel() {
        setBackground(Theme.CARD_BG);
        setOpaque(false); // Enable transparency for rounded corners
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        // Subtle Border
        g2.setColor(new Color(255, 255, 255, 20));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

        g2.dispose();
        super.paintComponent(g);
    }
}
