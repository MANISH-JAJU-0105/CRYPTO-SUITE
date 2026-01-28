package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Timer;

public class AnimatedSidebarButton extends JButton {
    private Color targetColor;
    private Color currentColor;
    private Timer animationTimer;
    private float progress = 0f;
    private boolean hovering = false;
    private boolean isSelected = false;

    private static final Color NORMAL_COLOR = Theme.TEXT_SECONDARY;
    private static final Color HOVER_COLOR = Color.WHITE;
    private static final Color SELECTED_COLOR = Theme.ACCENT_COLOR;
    
    public AnimatedSidebarButton(String text) {
        super(text);
        this.currentColor = NORMAL_COLOR;
        this.targetColor = NORMAL_COLOR;
        
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(280, 45));
        setForeground(currentColor);
        setFont(Theme.FONT_NORMAL);
        setFocusPainted(false);
        setBorder(new EmptyBorder(10, 15, 10, 15));
        setHorizontalAlignment(SwingConstants.LEFT);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        animationTimer = new Timer(15, e -> {
            boolean needsRepaint = false;
            
            // Interpolate Color
            int r = currentColor.getRed();
            int g = currentColor.getGreen();
            int b = currentColor.getBlue();
            int tr = targetColor.getRed();
            int tg = targetColor.getGreen();
            int tb = targetColor.getBlue();

            if (r != tr || g != tg || b != tb) {
                int newR = moveTowards(r, tr);
                int newG = moveTowards(g, tg);
                int newB = moveTowards(b, tb);
                currentColor = new Color(newR, newG, newB);
                setForeground(currentColor);
                needsRepaint = true;
            }
            
            // Interpolate Progress (for background/indicator)
            float targetProgress = (isSelected || hovering) ? 1.0f : 0.0f;
            if (Math.abs(progress - targetProgress) > 0.01f) {
                progress += (targetProgress - progress) * 0.2f;
                needsRepaint = true;
            } else {
                progress = targetProgress;
            }

            if (needsRepaint) {
                repaint();
            } else {
                ((Timer)e.getSource()).stop();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovering = true;
                updateTargetState();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovering = false;
                updateTargetState();
            }
        });
    }

    private int moveTowards(int current, int target) {
        int step = 15; // Speed of color transition
        if (Math.abs(target - current) <= step) return target;
        return current + (target > current ? step : -step);
    }
    
    public void setSelected(boolean selected) {
        this.isSelected = selected;
        updateTargetState();
    }
    
    private void updateTargetState() {
        if (isSelected) {
            targetColor = SELECTED_COLOR;
            setFont(Theme.FONT_NORMAL.deriveFont(Font.BOLD)); // Make bold when selected
        } else if (hovering) {
            targetColor = HOVER_COLOR;
            setFont(Theme.FONT_NORMAL);
        } else {
            targetColor = NORMAL_COLOR;
            setFont(Theme.FONT_NORMAL);
        }
        
        if (!animationTimer.isRunning()) {
            animationTimer.start();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw subtle background on hover/select
        if (progress > 0.01f) {
            g2.setColor(new Color(Theme.ACCENT_COLOR.getRed(), Theme.ACCENT_COLOR.getGreen(), Theme.ACCENT_COLOR.getBlue(), (int)(30 * progress)));
            g2.fillRoundRect(5, 2, getWidth() - 10, getHeight() - 4, 10, 10);
            
            // Draw a small indicator bar on the left
            g2.setColor(new Color(Theme.ACCENT_COLOR.getRed(), Theme.ACCENT_COLOR.getGreen(), Theme.ACCENT_COLOR.getBlue(), (int)(255 * progress)));
            int barHeight = (int)(20 * progress);
            int barY = (getHeight() - barHeight) / 2;
            g2.fillRoundRect(5, barY, 3, barHeight, 3, 3);
        }

        super.paintComponent(g2);
        g2.dispose();
    }
}
