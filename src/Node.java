import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Node {
    Data data;
    Node left;
    Node right;
    Node father;
    int maxNumberOfRecs = 0;
    Node(Data data) {
        this.data = data;
    }

    public boolean isFather() {
        if(right != null || left != null)
            return true;
        return false;
    }
    Node () {}

    public void drawTree(String path) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            drawRootToFile(this, writer);
            writer.close();
            System.out.println("Rectangles have been drawn to the file: ");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private void drawRootToFile(Node node, BufferedWriter writer) throws IOException {
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

    public static void drawChildren(Node root, List<String> lines, boolean isRight) throws IOException {
        if(root == null || root.getLeft() == null || root.getRight() == null )
            return;
        BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt"));
        int leftWidth = root.getLeft().getData().getWidth();
        int leftHeight = root.getLeft().getData().getHeight();
        int brotherWidth = 0;
        int brotherHeight = 0;
        int uncleWidth = 0;
        int uncleHeight = 0;
        char relation = root.getData().getRelation();

        if(root.father != null) {
            brotherWidth = isRight ? root.father.getLeft().getData().getWidth() : 0;
            brotherHeight = root.father.getLeft().getData().getHeight();
        }
        if(root.father != null && root.father.father != null) {
            uncleWidth = root.father.father.getLeft().getData().getWidth();
            uncleHeight = root.father.father.getLeft().getData().getHeight();
        }
        int xOff, yOff;
        if(relation == '-') {
            String newLine = "|";
            for (int i = 0; i < brotherWidth - 1; i++) {
                newLine += " ";
            }
            for (int i = brotherWidth; i < brotherWidth + leftWidth - 1; i++) {
                newLine += "-";
            }
            for (int i =  brotherWidth + leftWidth - 1; i < lines.get(0).length() - 1; i++) {
                newLine += " ";
            }
            newLine += "|";
            lines.set(uncleHeight + leftHeight, newLine);
            for (int i = 0; i < lines.size() - 1; i++) {
                writer.write(lines.get(i));
                writer.newLine();
            }
        }
        else if(relation == '|') {
            yOff = 0;
            xOff = 0;
            if(root.father != null) {
                if(root.father.getData().getRelation() == '-' && isRight) {
                    yOff = brotherHeight;
                }
                else if(root.father.getData().getRelation() == '|' && isRight) {
                    yOff = 0;
                }
                xOff = root.father.getData().getRelation() == '-' ? 0 : brotherWidth;
            }
            for (int i = yOff; i <= yOff + leftHeight ; i++) {
                String line = lines.get(i);
                if(line.length() > 0 ) {
                    String leftSub = line.substring(0, leftWidth + xOff - 1);
                    String rightSub = line.substring(leftWidth + xOff);
                    line = leftSub + "|" + rightSub;
                    lines.set(i, line);
                }
            }
            for (int i = 0; i < lines.size() - 1; i++) {
                writer.write(lines.get(i));
                writer.newLine();
            }
        }
        writer.close();
        drawChildren(root.getLeft(), lines, false);
        drawChildren(root.getRight(), lines, true);
    }

    public void printTree(int level, String label) {
        if (this == null) {
            return;
        }

        for (int i = 0; i < level; i++) {
            System.out.print("\t");
        }

        if(this.isFather())
            System.out.println(label + " " + data.printData("father"));
        else
            System.out.println(label + " " + data.printData("leave"));

        if (right != null) {
            right.printTree(level+1, "right");
        }
        if (left != null) {
            left.printTree(level+1, "left");
        }
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
                System.out.println("Father: " + input.charAt(i));
                System.out.println("Left child: " + input.substring(0, i));
                System.out.println("right child: " + input.substring(i + 1, input.length()));
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
                    System.out.println("name: " + name + " width: " + width + " height: " + height);
                }
                else {
                    if(input.charAt(0) == '(') {
                        leftChild = ImportTreeFromString(input.substring(1, i));
                    }
                    else {
                        leftChild = ImportTreeFromString(input.substring(0, i));
                    }
                }
                if(input.charAt(i+2) == '['){
                    String data = input.substring(i+1, input.length());
                    int commaIndex = data.indexOf(",");
                    int openIndex = data.indexOf("[");
                    int closeIndex = data.indexOf("]");
                    int width = Integer.parseInt(data.substring(openIndex + 1, commaIndex));
                    int height = Integer.parseInt(data.substring(commaIndex + 1, closeIndex));
                    char name = data.charAt(0);
                    Data recData = new Data(width, height, name);
                    rightChild = new Node(recData);
                    System.out.println("name: " + name + " width: " + width + " height: " + height);
                }
                else {
                    if(input.charAt(i + 1) == '(') {
                        rightChild = ImportTreeFromString(input.substring(i + 2, input.length()));
                    }
                    else {
                        rightChild = ImportTreeFromString(input.substring(i + 1, input.length()));
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
        String stringTree = "";
        char relation = tree.getData().getRelation();
        Node leftChild = tree.getLeft();
        Node rightChild = tree.getRight();
        String leftChildString = "";
        String rightChildString = "";
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

    public boolean checkRec() {
        if(!this.isFather())
            return true;
        boolean leftIsRec = left.checkRec();
        boolean rightIsRec = right.checkRec();
        if(leftIsRec) {
            left.maxNumberOfRecs++;
            maxNumberOfRecs += left.maxNumberOfRecs;
        }

        if(rightIsRec){
            right.maxNumberOfRecs++;
            maxNumberOfRecs += right.maxNumberOfRecs;
        }
        if(data.relation == '|') {
            int leftH = left.getData().getHeight();
            int rightH = right.getData().getHeight();
            if(leftH == rightH){
                return true;
            }
            else {
                return false;
            }
        }
        else if(data.relation == '-') {
            int leftW = left.getData().getWidth();
            int rightW = right.getData().getWidth();
            if(leftW == rightW){
                return true;
            }
            else {
                return false;
            }
        }

        return leftIsRec && rightIsRec;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}                                                                                                                   