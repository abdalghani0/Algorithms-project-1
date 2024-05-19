
public class Main {
    public static void main(String[] args) {
        String input = "(A[20,10]|(B[20,10]|C[30,10]))–(D[30,50]|(E[40,30]–F[40,20]))";
        Node root = Node.buildTree(input);


        System.out.println();
        System.out.println("Tree: ");
        root.printTree(0, "root");
        TXTDraw.drawBinaryTree(root,"data.txt");
        Node ne = TXTDraw.readBinaryTree("data.txt");
        TXTDraw.drawBinaryTree(ne,"data2.txt");

    }
}
