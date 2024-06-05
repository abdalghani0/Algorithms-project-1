import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        //(D[40,25]-H[40,25])-(E[40,30]-F[40,20])
        //(A[20,10]|(B[20,10]|C[30,10]))-(D[30,50]|(E[40,30]-F[40,20]))
        //(A[10,20]-(B[10,20]-C[10,30]))|(D[50,30]-(E[30,40]|F[20,40]))
        //(B[40,10]|C[30,10])-(D[30,50]|(E[40,30]-F[40,20]))
        //((A[20,10]|(B[20,10]|C[30,10]))-G[70,10])-((D[30,50]|(E[40,30]-F[40,20]))-H[70,20])
        String input = "(D[40,25]-H[40,25])-(E[40,30]-F[40,20])";
        Node root = AssetFunctions.importTreeFromString(input);

        System.out.println();
        System.out.println("Tree: " + AssetFunctions.exportTreeToString(root));
        System.out.println();

        if(root.checkRec()){
            System.out.println("Is a rectangle");
            System.out.println("Max number of recs: " + (root.maxNumberOfRecs + 1));

            //draw rectangle to file
            AssetFunctions.exportTreeToFile("rectangle.txt", root);
            Node tree = AssetFunctions.importTreeFromFile("rectangle.txt");
            System.out.println(AssetFunctions.exportTreeToString(tree));

            //draw rotated rectangle to file
            AssetFunctions.rotateRectangle(root);
            AssetFunctions.exportTreeToFile("rotatedRectangle.txt", root);
            Node rotatedTree = AssetFunctions.importTreeFromFile("rotatedRectangle.txt");
            System.out.println(AssetFunctions.exportTreeToString(rotatedTree));

            ImportFrame frame = new ImportFrame(root);

        }
        else {
            System.out.println("Not a rectangle");
            System.out.println("Max number of recs: " + root.maxNumberOfRecs);
        }
    }
}
