import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class AssetFunctions {
    public static void drawTreeToFile(String path, Node tree) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            BufferedReader reader = new BufferedReader(new FileReader(path));

            drawRootToFile(tree, writer);
            writer.close();
            List<String> fileLines = reader.lines().collect(Collectors.toList());
            drawChildren(tree, path, fileLines, false);

            System.out.println("Rectangles have been drawn to the file: ");
            System.out.println();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private static void drawRootToFile(Node node, BufferedWriter writer) throws IOException {
        if (node == null) {
            return;
        }

        if (node.checkRec()) {
            int width = node.getData().getWidth();
            int height = node.getData().getHeight();

            // Draw the top side of the rectangle
            for (int i = 0; i < width; i++) {
                writer.write("-");
            }
            writer.write("\n");

            // Draw the sides of the rectangle
            for (int i = 0; i < height - 2; i++) {
                writer.write("|");
                for (int j = 0; j < width-2; j++) {
                    writer.write(" ");
                }
                writer.write("|\n");
            }

            // Draw the bottom side of the rectangle
            for (int i = 0; i < width; i++) {
                writer.write("-");
            }
            writer.write("\n\n");
        }
    }

    public static void drawChildren(Node root, String path, List<String> fileLines, boolean isRight) throws IOException {
        //stop condition
        if(root == null || root.getLeft() == null || root.getRight() == null )
            return;

        System.out.println(root);
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));

        //initializing all used variables
        int leftWidth = root.getLeft().getData().getWidth();
        int leftHeight = root.getLeft().getData().getHeight();
        char relation = root.getData().getRelation();
        int[] xOffAndYOff = calculateXOffAndYOff(root, isRight, relation);
        int xOffset = xOffAndYOff[0], yOffset = xOffAndYOff[1];

        if(relation == '-') {
            //drawing the horizontal line
            String newLine = "|" + " ".repeat(Math.max(0, xOffset - 2)) +
                    "-".repeat(Math.max(0, xOffset + leftWidth - xOffset)) +
                    " ".repeat(Math.max(0, fileLines.get(0).length() - 2 - (xOffset + leftWidth))) +
                    "|";
            fileLines.set(yOffset + leftHeight, newLine);

            //placing the names of rectangles
            if(!root.getLeft().isFather()) {
                StringBuilder nameLine = new StringBuilder(fileLines.get(yOffset + 1));
                nameLine.setCharAt(xOffset + 1, root.getLeft().getData().getName());
                fileLines.set(yOffset + 1, nameLine.toString());
            }
            if(!root.getRight().isFather()) {
                StringBuilder nameLine = new StringBuilder(fileLines.get(yOffset + leftHeight + 1));
                nameLine.setCharAt(xOffset + 1, root.getRight().getData().getName());
                fileLines.set(yOffset + leftHeight + 1, nameLine.toString());
            }
        }

        else if(relation == '|') {
            //drawing the vertical line using a for loop
            for (int i = yOffset; i <= yOffset + leftHeight ; i++) {
                String line = fileLines.get(i);
                if(line.length() > 0 ) {
                    String leftSub = line.substring(0, leftWidth + xOffset - 1);
                    String rightSub = line.substring(leftWidth + xOffset);
                    line = leftSub + "|" + rightSub;
                    fileLines.set(i, line);
                }
            }

            //placing the names of rectangles
            StringBuilder nameLine = new StringBuilder(fileLines.get(yOffset + 1));
            if(!root.getLeft().isFather()) {
                nameLine.setCharAt(xOffset + 1, root.getLeft().getData().getName());
            }
            if(!root.getRight().isFather()) {
                nameLine.setCharAt(xOffset + leftWidth + 1, root.getRight().getData().getName());
            }
            fileLines.set(yOffset + 1, nameLine.toString());
        }

        //writing the new file lines in the file
        for (int i = 0; i < fileLines.size() - 1; i++) {
            writer.write(fileLines.get(i));
            writer.newLine();
        }
        writer.close();

        //recursively draw all rectangles
        drawChildren(root.getLeft(), path, fileLines, false);
        drawChildren(root.getRight(), path, fileLines, true);
    }

    public static int[] calculateXOffAndYOff(Node root, boolean isRight, char relation) {
        int xOffset = 0, yOffset = 0, brotherWidth = 0, brotherHeight = 0, uncleWidth = 0, uncleHeight = 0;
        if(root.father != null) {
            brotherWidth = isRight ? root.father.getLeft().getData().getWidth() : 0;
            brotherHeight = root.father.getLeft().getData().getHeight();
        }
        if(root.father != null && root.father.father != null) {
            uncleWidth = root.father.father.getLeft().getData().getWidth();
            uncleHeight = root.father.father.getLeft().getData().getHeight();
        }

        if(relation == '-') {
            //calculating xOffset and yOffset to know where to draw the horizontal line
            if (root.father != null) {
                if (root.father.getData().getRelation() == '|') {
                    xOffset = brotherWidth;
                }
                if (isRight && root.father.getData().getRelation() == '-') {
                    yOffset = brotherHeight;
                    if (root.father.father != null && root.father.father.getData().getRelation() == '|') {
                        xOffset = uncleWidth;
                    }
                } else {
                    yOffset = uncleHeight;
                }
            }
        }
        else if(relation == '|') {
            //calculating xOffset and yOffset to know where to draw the horizontal line
            if (root.father != null) {
                if (root.father.getData().getRelation() == '-' && isRight) {
                    yOffset = brotherHeight;
                }
                xOffset = root.father.getData().getRelation() == '-' ? 0 : brotherWidth;
            }
        }

        return new int[]{xOffset, yOffset};
    }

    public static Node ImportTreeFromString(String input) {
        int count = 0;
        Node root = new Node();
        Node rightChild = new Node();
        Node leftChild = new Node();
        for (int i = 0; i < input.length(); i++) {
            if(input.charAt(i) == '(') {
                count++;
            }
            else if(input.charAt(i) == ')') {
                count--;
            }
            if(count == 0 && (input.charAt(i) == '|' || input.charAt(i) == '-')){
                root = new Node(new Data(input.charAt(i)));
                if(input.charAt(i-1) == ']') {
                    String data = input.substring(0, i);
                    int commaIndex = data.indexOf(",");
                    int openIndex = data.indexOf("[");
                    int closeIndex = data.indexOf("]");
                    int width = Integer.parseInt(data.substring(openIndex + 1, commaIndex));
                    int height = Integer.parseInt(data.substring(commaIndex + 1, closeIndex));
                    char name = data.charAt(0);
                    Data recData = new Data(width, height, name);
                    leftChild = new Node(recData);
                }
                else {
                    if(input.charAt(0) == '(') {
                        leftChild = ImportTreeFromString(input.substring(1, i));
                    }
                    else {
                        leftChild = ImportTreeFromString(input.substring(0, i));
                    }
                }
                String rightSubstring = input.substring(i + 1);
                if(input.charAt(i+2) == '['){
                    int commaIndex = rightSubstring.indexOf(",");
                    int openIndex = rightSubstring.indexOf("[");
                    int closeIndex = rightSubstring.indexOf("]");
                    int width = Integer.parseInt(rightSubstring.substring(openIndex + 1, commaIndex));
                    int height = Integer.parseInt(rightSubstring.substring(commaIndex + 1, closeIndex));
                    char name = rightSubstring.charAt(0);
                    Data recData = new Data(width, height, name);
                    rightChild = new Node(recData);
                }
                else {
                    if(input.charAt(i + 1) == '(') {
                        rightChild = ImportTreeFromString(input.substring(i + 2));
                    }
                    else {
                        rightChild = ImportTreeFromString(rightSubstring);
                    }
                }
            }
        }
        int leftW = leftChild.getData().getWidth();
        int rightW = rightChild.getData().getWidth();
        int leftH = leftChild.getData().getHeight();
        int rightH = rightChild.getData().getHeight();
        if(root.getData().getRelation() == '-') {
            int width = leftW == rightW ? leftW : 0;
            root.getData().setWidth(width);
            root.getData().setHeight(leftH + rightH);
        }
        else if(root.getData().getRelation() == '|') {
            int height = leftH == rightH ? leftH : 0;
            root.getData().setWidth(leftW + rightW);
            root.getData().setHeight(height);
        }
        Node father = root;
        leftChild.father = father;
        rightChild.father = father;
        root.setLeft(leftChild);
        root.setRight(rightChild);
        return root;
    }

    public static String exportStringFromTree(Node tree) {
        if(tree == null || !tree.isFather())
            return "";
        String stringTree;
        char relation = tree.getData().getRelation();
        Node leftChild = tree.getLeft();
        Node rightChild = tree.getRight();
        String leftChildString;
        String rightChildString;
        if(tree.getLeft().isFather()) {
            leftChildString = "(" + exportStringFromTree(tree.getLeft()) + ")";
        }
        else {
            leftChildString =
                    leftChild.getData().getName()
                            + "[" + leftChild.getData().getWidth()
                            + "," + leftChild.getData().getHeight()
                            + "]";
        }
        if(tree.getRight().isFather()) {
            rightChildString = "(" + exportStringFromTree(tree.getRight()) + ")";
        }
        else {
            rightChildString =
                    rightChild.getData().getName()
                            + "[" + rightChild.getData().getWidth()
                            + "," + rightChild.getData().getHeight()
                            + "]";
        }
        stringTree = leftChildString + relation +  rightChildString;
        return  stringTree;
    }

    public static void rotateRectangle(Node tree) {
        if(tree == null)
            return;
        char relation = tree.getData().getRelation();
        int width = tree.getData().getWidth();
        int height = tree.getData().getHeight();
        if(tree.isFather()) {
            Node left = tree.getLeft();
            Node right = tree.getRight();
            if(relation == '|') {
                tree.getData().setRelation('-');
            }
            else {
                tree.getData().setRelation('|');
                tree.setRight(left);
                tree.setLeft(right);
            }
        }
        tree.getData().setWidth(height);
        tree.getData().setHeight(width);
        rotateRectangle(tree.getLeft());
        rotateRectangle(tree.getRight());
    }
}
