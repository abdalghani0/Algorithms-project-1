import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        String input = "(A[20,10]|(B[20,10]|C[30,10]))-(D[30,50]|(E[40,30]-F[40,20]))";
        Node root = Node.ImportTreeFromString(input);
        System.out.println();
        System.out.println("Tree: ");
        root.printTree(0, "root");
        System.out.println("export string from tree: " + Node.exportStringFromTree(root));
        System.out.println();
        if(root.checkRec()){
            System.out.println("Is a rectangle");
            System.out.println("Max number of recs: " + (root.maxNumberOfRecs + 1));
        }
        else {
            System.out.println("Not a rectangle");
            System.out.println("Max number of recs: " + root.maxNumberOfRecs);
        }
        BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
        PrintWriter pWriter = new PrintWriter(new FileWriter("data.txt"));
        root.drawTree("data.txt");
        List<String> lines = reader.lines().collect(Collectors.toList());
        System.out.println(input);
        Node.drawChildren(root, lines, false);
    }
}
