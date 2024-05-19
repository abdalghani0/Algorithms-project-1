public class Node {
    Data data;
    int fake;
    Node left;
    Node right;
    Node(Data data) {
        this.data = data;
    }

    public boolean isFather() {
        if(right != null || left != null)
            return true;
        return false;
    }
    Node () {}

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
                    root.setLeft(new Node(recData));
                    System.out.println("name: " + name + " width: " + width + " height: " + height);
                }
                else {
                    if(input.charAt(0) == '(') {
                        root.setLeft(buildTree(input.substring(1, i)));
                    }
                    else {
                        root.setRight(buildTree(input.substring(0, i)));
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
                    root.setRight(new Node(recData));
                    System.out.println("name: " + name + " width: " + width + " height: " + height);
                }
                else {
                    if(input.charAt(i + 1) == '(') {
                        root.setRight(buildTree(input.substring(i + 2, input.length())));
                    }
                    else {
                        root.setRight(buildTree(input.substring(i + 1, input.length())));
                    }
                }
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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