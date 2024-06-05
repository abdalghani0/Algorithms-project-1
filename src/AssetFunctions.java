import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssetFunctions {

    public static void exportTreeToFile(String path, Node tree) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            BufferedReader reader = new BufferedReader(new FileReader(path));
            drawRootToFile(path, tree);
            List<String> fileLines = reader.lines().collect(Collectors.toList());
            List<String> newFileLines = AssetFunctions.drawChildren(tree, fileLines);
            //writing the new file lines in the file
            for (String line : newFileLines) {
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }

    }

    public static void drawRootToFile(String path, Node tree) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            BufferedReader reader = new BufferedReader(new FileReader(path));

            drawRoot(tree, writer);
            writer.close();


            System.out.println("Rectangles have been drawn to the file: " + path);
            System.out.println();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private static void drawRoot(Node node, BufferedWriter writer) throws IOException {
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
    }

    public static List<String> drawChildren(Node root, List<String> fileLines) {
        char relation = root.getData().getRelation();
        List<String> leftSubList;
        List<String> rightSubList;
        List<String> newFileLines = new ArrayList<>();

        if(relation == '-') {
            int leftWidth = root.getLeft().getData().getWidth();
            int leftHeight = root.getLeft().getData().getHeight();
            String newLine = "|" + "-".repeat(leftWidth - 2) + "|";
            fileLines.set(leftHeight, newLine);
            leftSubList = drawChildren(root.getLeft(), fileLines.subList(0,leftHeight));
            rightSubList = drawChildren(root.getRight(), fileLines.subList(leftHeight,fileLines.size()));
            newFileLines.addAll(leftSubList);
            newFileLines.addAll(rightSubList);
        }
        else if(relation == '|') {
            int leftWidth = root.getLeft().getData().getWidth();
            int leftHeight = root.getLeft().getData().getHeight();
            for (int i = 1; i < fileLines.size() ; i++) {
                String line = fileLines.get(i);
                if(line.length() > 0 ) {
                    String leftSub = line.substring(0, leftWidth-1);
                    String rightSub = line.substring(leftWidth);
                    line = leftSub + "|" + rightSub;
                    fileLines.set(i, line);
                }
            }
            List<String> leftLines = new ArrayList<>();
            List<String> rightLines = new ArrayList<>();
            for (String line : fileLines) {
                if(line.length() > 0) {
                    leftLines.add(line.substring(0, leftWidth));
                    rightLines.add(line.substring(leftWidth));
                }
            }
            leftSubList = drawChildren(root.getLeft(), leftLines);
            rightSubList = drawChildren(root.getRight(), rightLines);
            for (int i = 0; i < fileLines.size(); i++) {
                newFileLines.add(leftSubList.get(i) + rightSubList.get(i));
            }
        }
        else {
            StringBuilder nameLine = new StringBuilder(fileLines.get(1));
            nameLine.setCharAt(1,root.getData().getName());
            fileLines.set(1, nameLine.toString());
            return fileLines;
        }
        return newFileLines;
    }

    public static Node importTreeFromFile(String path){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            List<String> fileLines = reader.lines().collect(Collectors.toList());
            return extractTreeFromFile(fileLines);
        }
        catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
        return new Node();
    }

    public static Node extractTreeFromFile(List<String> fileLines) {
        Node root = new Node();
        char relation = ' ';
        Node leftChild = new Node();
        Node rightChild = new Node();
        int treeHeight = fileLines.size();
        int treeWidth = fileLines.get(1).length();

        boolean isHorizontal = false;
        int indexOfHorizontalRel = 0;
        for (int j = 1; j < fileLines.size() - 1; j++) {
            String line = fileLines.get(j);
            int width = 0;
            for (int i = 1; i < line.length() - 1; i++) {
                if(line.charAt(i) == '-') {
                    width++;
                }
            }
            if(width == line.length() - 2){
                indexOfHorizontalRel = j;
                isHorizontal = true;
                break;
            }
        }

        String firstLine = fileLines.get(1);
        int indexOfVerticalRel = 0;
        for(int i = 1; i < firstLine.length() - 1; i++) {
            if(firstLine.charAt(i) == '|') {
                indexOfVerticalRel = i;
                for(int j = 1; j < fileLines.size() - 1; j++) {
                    if(fileLines.get(j).charAt(i) != '|') {
                        break;
                    }
                }
            }
        }

        if(isHorizontal) {
            relation = '-';
            leftChild = extractTreeFromFile(fileLines.subList(0, indexOfHorizontalRel));
            rightChild = extractTreeFromFile(fileLines.subList(indexOfHorizontalRel, fileLines.size()));
        }
        else if(indexOfVerticalRel != 0){
            relation = '|';
            List<String> leftLines = new ArrayList<>();
            List<String> rightLines = new ArrayList<>();
            for (String line : fileLines) {
                leftLines.add(line.substring(0, indexOfVerticalRel));
                rightLines.add(line.substring(indexOfVerticalRel));
            }
            leftChild = extractTreeFromFile(leftLines);
            rightChild = extractTreeFromFile(rightLines);
        }
        else {
            char name = fileLines.get(1).charAt(1) == ' ' ? fileLines.get(1).charAt(2) : fileLines.get(1).charAt(1);
            root.setData(new Data(treeWidth, treeHeight, name));
            return root;
        }

        root.setData(new Data(relation));
        root.getData().setHeight(treeHeight);
        root.getData().setWidth(treeWidth);
        root.setLeft(leftChild);
        root.setRight(rightChild);
        return root;
    }

    public static Node importTreeFromString(String input) {
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
                        leftChild = importTreeFromString(input.substring(1, i));
                    }
                    else {
                        leftChild = importTreeFromString(input.substring(0, i));
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
                        rightChild = importTreeFromString(input.substring(i + 2));
                    }
                    else {
                        rightChild = importTreeFromString(rightSubstring);
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

    public static String exportTreeToString(Node tree) {
        if(tree == null || !tree.isFather())
            return "";
        String stringTree;
        char relation = tree.getData().getRelation();
        Node leftChild = tree.getLeft();
        Node rightChild = tree.getRight();
        String leftChildString;
        String rightChildString;
        if(tree.getLeft().isFather()) {
            leftChildString = "(" + exportTreeToString(tree.getLeft()) + ")";
        }
        else {
            leftChildString =
                    leftChild.getData().getName()
                            + "[" + leftChild.getData().getWidth()
                            + "," + leftChild.getData().getHeight()
                            + "]";
        }
        if(tree.getRight().isFather()) {
            rightChildString = "(" + exportTreeToString(tree.getRight()) + ")";
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
