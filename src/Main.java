import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String input = "(A[20,10]|(B[20,10]|C[30,10]))–(D[30,50]|(E[40,30]–F[40,20]))";
        Node root = Node.buildTree(input);
        root.printTree(0);
    }
}

class Node {
    data data;
    Node left;
    Node right;
    Node (data data, Node left, Node right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }
    Node (data data, Node node, char dir) {
        this.data = data;
        if(dir == 'l')
            this.left = node;
        else if(dir == 'r')
            this.right = node;
    }
    Node(data data) {
        this.data = data;
    }

    Node () {

    }

    public void printTree(int level) {
        if (this == null) {
            return;
        }

        if (right != null) {
            right.printTree(level + 1);
        }

        for (int i = 0; i < level; i++) {
            System.out.print("\t");
        }
        System.out.println(data.name);

        if (left != null) {
            left.printTree(level + 1);
        }
    }
    public static Node buildTree(String input) {
        int count = 0;
        Node root = new Node();
        for (int i = 0; i < input.length(); i++) {
            if(input.charAt(i) == '(') {
                count++;
            }
            else if(input.charAt(i) == ')') {
                count--;
            }
            if(count == 0 && (input.charAt(i) == '|' || input.charAt(i) == '–')){
                root = new Node(new data(input.charAt(i)));
                System.out.println("Father: " + input.charAt(i));
                if(input.charAt(0) == '(') {
                    System.out.println("Left child: " + input.substring(1, i));
                }
                else {
                    System.out.println("Left child: " + input.substring(0, i));
                }
                if(input.charAt(i + 1) == '(') {
                    System.out.println("right child: " + input.substring(i + 2, input.length()));
                }
                else {
                    System.out.println("right child: " + input.substring(i + 1, input.length()));
                }
                if(input.charAt(0) == '(') {
                    root.setLeft(buildTree(input.substring(1, i)));
                }
                else {
                    root.setRight(buildTree(input.substring(0, i)));
                }
                if(input.charAt(i + 1) == '(') {
                    root.setRight(buildTree(input.substring(i + 2, input.length())));
                }
                else {
                    root.setRight(buildTree(input.substring(i + 1, input.length())));
                }
            }
            else {

            }
        }
        return root;
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

    public data getData() {
        return data;
    }

    public void setData(data data) {
        this.data = data;
    }

    public static boolean checkRec(Node root) {
        if(root.data.relation == '|') {
            if(root.left.data.height == root.right.data.height) {
                int recH = root.left.data.height;
                int recW = root.left.data.width + root.right.data.width;
                root.data.setHeight(recH);
                root.data.setWidth(recW);
                return(true);
            }
            return (false);
        }
        else if(root.data.relation == '-') {
            if(root.left.data.width == root.right.data.width) {
                int recW = root.left.data.width;
                int recH = root.left.data.height + root.right.data.height;
                root.data.setWidth(recW);
                root.data.setHeight(recH);
                return(true);
            }
            return false;
        }
        return false;
    }
}
class data {
    int height;
    int width;
    String name;
    char relation;
    data(int height, int width, String name) {
        this.height = height;
        this.width = width;
        this.name = name;
    }
    data(char relation) {
        this.relation = relation;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getRelation() {
        return relation;
    }

    public void setRelation(char relation) {
        this.relation = relation;
    }
}