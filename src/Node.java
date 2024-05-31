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
        return right != null || left != null;
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

    public boolean checkRec() {
        if(!this.isFather())
            return true;

        boolean leftIsRec = false;
        boolean rightIsRec = false;

        if(data.relation == '|') {
            int leftH = left.getData().getHeight();
            int rightH = right.getData().getHeight();
            if(leftH == rightH){
                leftIsRec = left.checkRec();
                rightIsRec = right.checkRec();
            }
        }
        else if(data.relation == '-') {
            int leftW = left.getData().getWidth();
            int rightW = right.getData().getWidth();
            if(leftW == rightW){
                leftIsRec = left.checkRec();
                rightIsRec = right.checkRec();
            }
        }
        if(leftIsRec) {
            left.maxNumberOfRecs++;
            maxNumberOfRecs += left.maxNumberOfRecs;
        }
        if(rightIsRec){
            right.maxNumberOfRecs++;
            maxNumberOfRecs += right.maxNumberOfRecs;
        }
        if(leftIsRec && rightIsRec) {
            if(right.isFather()) {
                maxNumberOfRecs ++;
            }
            if(left.isFather()) {
                maxNumberOfRecs ++;
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

    @Override
    public String toString() {
        return "Node{" +
                "rel=" + data.getRelation() +
                '}';
    }
}