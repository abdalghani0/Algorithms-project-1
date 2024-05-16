import java.io.FileWriter;
import java.io.IOException;
public class TXTDraw {


    public static void drawBinaryTree(Node node, String filePath) {
        try {
            FileWriter writer = new FileWriter(filePath);
            drawBinaryTreeUtil(node, writer, "", false);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void drawBinaryTreeUtil(Node node, FileWriter writer, String prefix, boolean isLeft) throws IOException {
        if (node != null) {
            writer.write(prefix);
            writer.write(isLeft ? "|-- " : "|+- ");
            //display data
            writer.write( "" + "\n");

            drawBinaryTreeUtil(node.left, writer, prefix + (isLeft ? "|   " : "    "), true);
            drawBinaryTreeUtil(node.right, writer, prefix + (isLeft ? "|   " : "    "), false);
        }
    }

}
