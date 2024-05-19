import java.io.*;
import java.util.Stack;

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
            writer.write(isLeft ? ")->> " : ")+>> ");
            //display data
            writer.write( node.isFather()? PrintData("father",node) +"\n" :PrintData("",node)+ "\n");
//node.isFather()? PrintData("father",node) :PrintData("",node)
            drawBinaryTreeUtil(node.left, writer, prefix + (isLeft ? ")     " : "      "), true);
            drawBinaryTreeUtil(node.right, writer,prefix + (isLeft ? ")     " : "      "), false);
        }
    }


    public static String PrintData(String status, Node node) {
        if(status == "father") {
            return "{" +
                     node.data.relation +
                    "}";
        }
        return "{" +
                "h=" + node.data.height +
                ", w=" + node.data.width +
                ", " + node.data.name +
                '}';
    }
    public static Node readBinaryTree(String filePath) {

        Node theRoot = null;
        try{
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            int i =0 ;
            while((line = bufferedReader.readLine())!= null){

                i++;
                System.out.println("loop"+ i);
                int level = line.lastIndexOf(")") + 1;
                String data = line.substring(level + 5).trim();
                System.out.println(data);

                Node newNode = null;
                if(data.charAt(0) == '-' || data.charAt(0) == '|'){
                    char relation = data.charAt(0);
                    // Create a new node with the extracted data
                    newNode = new Node(new Data(relation));
                    String line2;
                    while((line2 = bufferedReader.readLine())!= null){
                        Node newNodeL = null;
                        Node newNodeR = null;
                        int level2 = line.lastIndexOf(")") + 1;

                        if(level2 ==level+1){
                            String data2 = line.substring(level2 + 5).trim();

                            if((data2.charAt(0) == '-' || data2.charAt(0) == '|') && line.contains("-")){
                                char relation2 = data2.charAt(0);
                                // Create a new node with the extracted data
                                newNodeL = new Node(new Data(relation2));
                                newNode.left = newNodeL ;
                            }
                            else if((data2.charAt(0) == '-' || data2.charAt(0) == '|') && line.contains("+")){
                                char relation2 = data2.charAt(0);
                                // Create a new node with the extracted data
                                newNodeR = new Node(new Data(relation2));
                                newNode.right = newNodeR ;
                            }
                            else if(line.contains("-")){
                                String[] values = data2.split(", ");
                                int height = Integer.parseInt(values[0].substring(2));
                                int width = Integer.parseInt(values[1].substring(2));
                                char name = values[2].charAt(0);

                                // Create a new node with the extracted data
                                newNodeL = new Node(new Data(height, width, name));
                                newNode.left = newNodeL;
                            }else if(line.contains("+")){
                                String[] values = data2.split(", ");
                                int height = Integer.parseInt(values[0].substring(2));
                                int width = Integer.parseInt(values[1].substring(2));
                                char name = values[2].charAt(0);

                                // Create a new node with the extracted data
                                newNodeR = new Node(new Data(height, width, name));
                                newNode.right = newNodeR;
                            }
                        }
                    }
                }
            if(theRoot == null){
                theRoot = newNode ;
            }
            }

        }catch(IOException e){e.printStackTrace();}

        return theRoot;
    }
//    public static Node readBinaryTree(String filePath) {
//        Node rootNode = null;
//        Stack<Node> stack = new Stack<>();
//        try {
//            FileReader fileReader = new FileReader(filePath);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            String line;
//
//            while ((line = bufferedReader.readLine()) != null) {
//                int level = line.lastIndexOf("|") + 1;
//                String data = line.substring(level + 5).trim();
//                Node newNode;
//                if(data.charAt(0) == '-' || data.charAt(0) == '|'){
//
//                    char relation = data.charAt(0);
//                    // Create a new node with the extracted data
//                    newNode = new Node(new Data(relation));
//                } else{
//                    String[] values = data.split(", ");
//                    if (values.length >= 3) {
//                        int height = Integer.parseInt(values[0].substring(2));
//                        int width = Integer.parseInt(values[1].substring(2));
//                        char name = values[2].charAt(0);
//
//                        // Create a new node with the extracted data
//                        newNode = new Node(new Data(height, width, name));
//                    } else {
//                        // Handle the case where the data does not contain enough elements
//                        // You can choose to skip this line or handle it differently based on your requirements
//                        System.out.println("that error ");
//                    }
//                }
//
//                while (stack.size() >= level) {
//                    stack.pop();
//                }
//
//                if (!stack.isEmpty()) {
//                    Node parent = stack.peek();
//                    if (parent.left == null) {
//                        parent.left = newNode;
//                    } else {
//                        parent.right = newNode;
//                    }
//                } else {
//                    rootNode = newNode;
//                }
//
//                stack.push(newNode);
//            }
//
//            bufferedReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return rootNode;
//    }
//    public static Node readBinaryTree(String filePath) {
//        Node rootNode = null;
//        try {
//            FileReader fileReader = new FileReader(filePath);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            String line;
//
//            while ((line = bufferedReader.readLine()) != null) {
//                // Parse the line to extract node data
//                // Assuming the format of each line is "|--- {data}"
//                String[] parts = line.split("\\{");
//                char side = parts[0].charAt(1);
//                System.out.println( "the side is :"+side);
//                String data = parts[1].replace("}", "");
//
//                // Extract the individual values from the data
//                if(data.charAt(0) == '-' || data.charAt(0) == '|'){
//                    char relation = data.charAt(0);
//
//                    // Create a new node with the extracted data
//                    Node newNode = new Node(new Data(relation));
//                } else{
//                    String[] values = data.split(", ");
//                    int height = Integer.parseInt(values[0].substring(2));
//                    int width = Integer.parseInt(values[1].substring(2));
//                    char name = values[2].charAt(0);
//
//                    // Create a new node with the extracted data
//                    Node newNode = new Node(new Data(height, width, name));
//                }
//
////                if (rootNode == null) {
////                    rootNode = newNode;
////                }
//            }
//
//            bufferedReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // Fix the relations between tree nodes (example logic below)
////        if (rootNode != null) {
////            rootNode.left = new Node(new Data(0, 0, '-'));
////            rootNode.right = new Node(new Data(0, 0, '-'));
////        }
//
//        return rootNode;
//    }
}
