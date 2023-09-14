import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

    public static int inversionCount(int[] arr) {
        int count = 0;
        for(int i=1; i< arr.length; i++) {
            for(int j=i+1; j< arr.length; j++) {
                if(arr[i]!=-1 && arr[j]!=-1 && arr[i]>arr[j]) count++;
            }
        }
        return count;
    }

    public static boolean isSolvable(int[][] arr, int[] arr1D, int k) {
        int count = inversionCount(arr1D)%2;
        System.out.println(arr.length);
        if(k%2!=0) {
            return count == 0;
        }
        else {
            int blankRow = -1;
            for(int i= arr.length-1; i>=1; i--) {
                 for(int j=arr.length-1; j>=1; j--) {
                    if(arr[i][j] == -1) blankRow = k-i+1;
                 }
            }
            if(blankRow%2==0 && count%2!=0) return true;
            return blankRow % 2 != 0 && count % 2 == 0;
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int k = input.nextInt();
        input.nextLine();

        int[][] arr = new int[k+1][k+1];
        int[][] goalArr = new int[k+1][k+1];
        int[] arr1D = new int[k*k+1];
        String line = input.nextLine();
        String[] words = line.split(" ");
        int count = 0;
        for(int i=1; i<arr.length; i++) {
            for(int j=1; j< arr.length; j++) {
                String s = words[count++];
                if(s.equalsIgnoreCase("*")) arr[i][j] = -1;
                else arr[i][j] = Integer.parseInt(s);
                if(count == k*k)    goalArr[i][j] = -1;
                else goalArr[i][j] = count;
                arr1D[count] = arr[i][j];
            }
        }

        Board initialBoard = new Board(k, arr);
        Board finalBoard = new Board(k, goalArr);
        Node initialNode = new Node(initialBoard,  finalBoard, null, true);

        // initialBoard.printBoard();
        // finalBoard.printBoard();
        boolean solvable = isSolvable(arr, arr1D, k*k);
        if(!solvable) System.out.println("Given puzzle is not solvable.");
        else {
            System.out.println("Given puzzle is solvable.");
            System.out.println("===========================================================");


            int running = 0;
            int hamCost = 0, manCost = 0;
            int exploredNodesHam = 0, expandedNodesHam = 0, exploredNodesMan = 0, expandedNodesMan = 0;
            while(running <= 1) {
                if(running == 0) {
                    // Hamming
                    System.out.println("--------------Using Hamming Priority Function-----------------");
                    initialNode = new Node(initialBoard,  finalBoard, null, true);
                    //System.out.println(initialNode.getF());

                    PriorityQueue<Node> pq = new PriorityQueue<>();
                    ArrayList<Node> closedList = new ArrayList<>();
                    pq.add(initialNode);
                    //pq.poll().getBoard().printBoard();

                    expandedNodesHam = 1; // entered the queue
                    exploredNodesHam = 0; // exited the queue

                    while(true) {
                        Node node = pq.poll();
                        exploredNodesHam++;
                        closedList.add(node);
                        node.getBoard().printBoard();
                        if(node.getBoard().isSameBoard(finalBoard)) {
                            hamCost = node.getG();
                            break;
                        }
                        ArrayList<Node> neighbors = node.getNeighbors(true);
                        for(Node neighNode:neighbors) {
                            boolean inClosedList = false;
                            for(Node tempNode:closedList) {
                                if(neighNode.getBoard().isSameBoard(tempNode.getBoard())) {
                                    inClosedList = true;
                                }
                            }
                            if(!inClosedList) {
                                pq.add(neighNode);
                                expandedNodesHam++;
                            }
                        }

                    }

                    System.out.println();

                }

                else {
                    // Manhattan
                    System.out.println("--------------Using Manhattan Priority Function-----------------");
                    initialNode = new Node(initialBoard, finalBoard, null, false);
                    //System.out.println(initialNode.getF());

                    PriorityQueue<Node> pq1 = new PriorityQueue<>();
                    ArrayList<Node> closedList = new ArrayList<>();
                    pq1.add(initialNode);

                    expandedNodesMan = 1; // entered the queue
                    exploredNodesMan = 0; // exited the queue

                    while(true) {
                        Node node = pq1.poll();
                        exploredNodesMan++;
                        closedList.add(node);
                        node.getBoard().printBoard();
                        if(node.getBoard().isSameBoard(finalBoard)) {
                            manCost = node.getG();
                            break;
                        }

                        ArrayList<Node> neighbors = node.getNeighbors(false);

                        for(Node neighNode:neighbors) {
                            boolean inClosedList = false;
                            for(Node tempNode:closedList) {
                                if(neighNode.getBoard().isSameBoard(tempNode.getBoard())) {
                                    inClosedList = true;
                                }
                            }
                            if(!inClosedList) {
                                pq1.add(neighNode);
                                expandedNodesMan++;
                            }
                        }


                    }


                }

                running++;
            }

            System.out.println("--------------------Using Hamming priority function-----------------");
            System.out.println("Cost: " + hamCost);
            System.out.println("Explored Nodes: " + exploredNodesHam);
            System.out.println("Expanded Nodes: " + expandedNodesHam);
            System.out.println();
            System.out.println("--------------------Using Manhattan priority function-----------------");
            System.out.println("Cost: " + manCost);
            System.out.println("Explored Nodes: " + exploredNodesMan);
            System.out.println("Expanded Nodes: " + expandedNodesMan);




        }






    }
}
