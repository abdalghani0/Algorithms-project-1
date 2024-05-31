import javax.swing.*;
import java.awt.*;


public class ImportPaint extends JPanel {
    Node node;

    //.....................paint details ....................................
    int panelWidth = 160 * 4 + 5725;
    int panelHeight = 125 * (100) + 45;

    int x = 2860;
    int y = 5;
    int weightCircle = 40;
    int heightCircle = 40;

    int circleCenterX = x + (weightCircle / 2);
    int circleBottomY = y + (heightCircle);

    //        int rightCircleX = circleCenterX + 0;
//        int leftCircleX = circleCenterX - 0;
//        int Y_Circles = circleBottomY + 0;
//
//
    int y2_Lines = circleBottomY + 85;
    int x2_rightline = circleCenterX + 150;
    int x2_leftline = circleCenterX - 155;


    public ImportPaint(Node node) {
        this.node = node;
        setPreferredSize(new Dimension(panelWidth, panelHeight));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int initialX = circleCenterX;
        int initialY = circleBottomY;
        int initialOffsetX = 300;
        int initialOffsetY = 125;
        paintComponent1(g, node, initialX, initialY, initialOffsetX, initialOffsetY);
    }

    protected void paintComponent1(Graphics g, Node node, int x, int y, int initialOffsetX, int initialOffsetY) {
        if (node != null) {
            // Draw the current node
            g.drawOval(x, y, weightCircle, heightCircle);

            // Draw the node's name or data inside the circle
            if (node.isFather())
                g.drawString(String.valueOf(node.getData().getRelation()), x + weightCircle / 2, y + heightCircle / 2);
            else
                g.drawString(String.valueOf(node.getData().getName()), x + weightCircle / 2, y + heightCircle / 2);
            // Coordinates for the left and right child nodes
            int leftX = x - initialOffsetX;
            int rightX = x + initialOffsetX;
            int childY = y + initialOffsetY;

            // Draw lines to the left and right children if they exist
            if (node.getLeft() != null) {
                g.drawLine(x + weightCircle / 2, y + heightCircle, leftX + weightCircle / 2, childY);
            }

            if (node.getRight() != null) {
                g.drawLine(x + weightCircle / 2, y + heightCircle, rightX + weightCircle / 2, childY);
            }
            paintComponent1(g, node.getLeft(), leftX, childY, initialOffsetX / 2, initialOffsetY); // recursive call for left child
            paintComponent1(g, node.getRight(), rightX, childY, initialOffsetX / 2, initialOffsetY); // recursive call for right child

        }
    }
}