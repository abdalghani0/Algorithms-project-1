public class Main {

    public static void main(String[] args){
        //(D[40,25]-H[40,25])-(E[40,30]-F[40,20])
        String input = "(A[20,10]|(B[20,10]|C[30,10]))-(D[30,50]|(E[40,30]-F[40,20]))";
        Node root = AssetFunctions.ImportTreeFromString(input);
        root.printTree(0, "root");

        System.out.println();
        System.out.println("Tree: " + AssetFunctions.exportStringFromTree(root));
        System.out.println();

        if(root.checkRec()){
            System.out.println("Is a rectangle");
            System.out.println("Max number of recs: " + (root.maxNumberOfRecs + 1));

            //draw rectangle
            AssetFunctions.drawTreeToFile("rectangle.txt", root);

            //draw rotated rectangle
            AssetFunctions.rotateRectangle(root);
            AssetFunctions.drawTreeToFile("rotatedRectangle.txt", root);
        }
        else {
            System.out.println("Not a rectangle");
            System.out.println("Max number of recs: " + root.maxNumberOfRecs);
        }
    }
}
