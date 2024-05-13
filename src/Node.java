public class Node {
    Data data;
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
        Node rightChild = new Node();
        Node leftChild = new Node();
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
                    leftChild = new Node(recData);
                    System.out.println("name: " + name + " width: " + width + " height: " + height);
                }
                else {
                    if(input.charAt(0) == '(') {
                        leftChild = buildTree(input.substring(1, i));

                    }
                    else {
                        leftChild = buildTree(input.substring(0, i));
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
                        rightChild = buildTree(input.substring(i + 2, input.length()));
                    }
                    else {
                        rightChild = buildTree(input.substring(i + 1, input.length()));
                    }
                }
            }
        }
        int leftW = leftChild.getData().getWidth();
        int rightW = rightChild.getData().getWidth();
        int leftH = leftChild.getData().getHeight();
        int rightH = rightChild.getData().getHeight();
        if(root.getData().getRelation() == '–') {
            int width = leftW == rightW ? leftW : 0;
            root.getData().setWidth(width);
            root.getData().setHeight(leftH + rightH);
        }
        else if(root.getData().getRelation() == '|') {
            int height = leftH == rightH ? leftH : 0;
            root.getData().setWidth(leftW + rightW);
            root.getData().setHeight(height);
        }
        root.setLeft(leftChild);
        root.setRight(rightChild);
        return root;
    }

    public boolean checkRec() {
        if(!this.isFather())
            return true;
        boolean leftIsRec = left.checkRec();
        boolean rightIsRec = right.checkRec();
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