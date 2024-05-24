import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        String input = "(D[40,25]-H[40,25])-(E[40,30]-F[40,20])";
        Node root = Node.ImportTreeFromString(input);
        root.printTree(0, "root");

        System.out.println();
        System.out.println("Tree: " + Node.exportStringFromTree(root));
        System.out.println();

        if(root.checkRec()){
            System.out.println("Is a rectangle");
            System.out.println("Max number of recs: " + (root.maxNumberOfRecs + 1));
        }
        else {
            System.out.println("Not a rectangle");
            System.out.println("Max number of recs: " + root.maxNumberOfRecs);
        }

        //draw rectangle
        BufferedReader recReader = new BufferedReader(new FileReader("rectangle.txt"));
        root.drawTree("rectangle.txt");
        //draw the root then take the file content to modify it and draw children afterwards
        List<String> recLines = recReader.lines().collect(Collectors.toList());
        Node.drawChildren(root, "rectangle.txt", recLines, false);

        //draw rotated rectangle
        Node.rotateRectangle(root);
        BufferedReader RotatedRecReader = new BufferedReader(new FileReader("rotatedRectangle.txt"));
        root.drawTree("rotatedRectangle.txt");
        //draw the root then take the file content to modify it and draw children afterwards
        List<String> rotatedRecLines = RotatedRecReader.lines().collect(Collectors.toList());
        Node.drawChildren(root, "rotatedRectangle.txt", rotatedRecLines, false);
    }
}
