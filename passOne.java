import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
public class passOne {
    // arrays
    public static ArrayList<String> labels = new ArrayList();
    public static ArrayList<String> instructions = new ArrayList();
    public static ArrayList<String> references = new ArrayList();
    public static ArrayList<String> locctr = new ArrayList();

    // function to read input file and add data to suitable array
    public void processFile(){
        // reading file
        File f = new File("C:\\Users\\mariam\\Desktop\\systems programming\\sicxe\\inSICXE.txt");
        Scanner reader;
        try {
            reader = new Scanner(f);
            String[] arr;
            String data;
            // checking for available next line
            while (reader.hasNext()) {
                // reading line of data from file
                data = reader.nextLine();
                // spliting string and adding it to array
                arr = data.trim().split("\\s+");

                // adding each string to suitable arraylist
                if ((arr.length) == 3) {
                    labels.add(arr[0]);
                    instructions.add(arr[1]);
                    references.add(arr[2]);
                }
                else if (arr[0].equalsIgnoreCase("rsub")) {
                    labels.add("###");
                    instructions.add(arr[0]);
                    references.add("###");
                }
                else {
                    labels.add("###");
                    instructions.add(arr[0]);
                    references.add(arr[1]);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception");
        }
    }

    // function to calculate location counter
    public void getLocctr(){
        // location counter
        String firstLoc=references.get(0).trim();
        for (int x = firstLoc.length(); x < 4; x++) {
            firstLoc = "0" + firstLoc;
        }
        locctr.add(firstLoc);
        locctr.add(firstLoc);
        for (int i = 1; i < instructions.size(); i++) {
            if (!instructions.get(i).equalsIgnoreCase("end")) {

                // handling base
                if(instructions.get(i).equalsIgnoreCase("base")){
                    String prevLoc = locctr.get(i);
                    String loc = prevLoc;
                    for (int x = loc.length(); x < 4; x++) {
                        loc = "0" + loc;
                    }
                    locctr.add(loc);
                }
                //handling format 1,2,4
                // format 1
                else if(converter.getFormat(instructions.get(i)).equalsIgnoreCase("1")){
                    String prevLoc = locctr.get(i);
                    String loc = Integer.toHexString(Integer.parseInt(prevLoc, 16) + Integer.parseInt("1", 16));
                    for (int x = loc.length(); x < 4; x++) {
                        loc = "0" + loc;
                    }
                    locctr.add(loc);
                }
                // format 2
                else if(converter.getFormat(instructions.get(i)).equalsIgnoreCase("2")){
                    String prevLoc = locctr.get(i);
                    String loc = Integer.toHexString(Integer.parseInt(prevLoc, 16) + Integer.parseInt("2", 16));
                    for (int x = loc.length(); x < 4; x++) {
                        loc = "0" + loc;
                    }
                    locctr.add(loc);
                }
                // format 4
                else if(instructions.get(i).startsWith("+")){
                    String prevLoc = locctr.get(i);
                    String loc = Integer.toHexString(Integer.parseInt(prevLoc, 16) + Integer.parseInt("4", 16));
                    for (int x = loc.length(); x < 4; x++) {
                        loc = "0" + loc;
                    }
                    locctr.add(loc);
                }
                // handling format 3
                // resw case
                else if (instructions.get(i).equalsIgnoreCase("resw")) {
                    int prevData = Integer.parseInt(locctr.get(i), 16);
                    String data = Integer.toHexString((Integer.parseInt(references.get(i)) * 3) + prevData);
                    for (int x = data.length(); x < 4; x++) {
                        data = "0" + data;
                    }
                    locctr.add(data);
                }
                // resb case
                else if (instructions.get(i).equalsIgnoreCase("resb")) {
                    int prevData = Integer.parseInt(locctr.get(i), 16);
                    String data = Integer.toHexString((Integer.parseInt(references.get(i))) + prevData);
                    for (int x = data.length(); x < 4; x++) {
                        data = "0" + data;
                    }
                    locctr.add(data);
                }
                // byte case
                else if (instructions.get(i).equalsIgnoreCase("byte")) {
                    // x
                    if (references.get(i).startsWith("X") || references.get(i).startsWith("x")) {
                        int prevData = Integer.parseInt(locctr.get(i), 16);
                        int lengthData = references.get(i).length();
                        String data = Integer.toHexString(((lengthData - 3) / 2) + prevData);
                        for (int x = data.length(); x < 4; x++) {
                            data = "0" + data;
                        }
                        locctr.add(data);
                    }
                    // c
                    else {
                        int prevData = Integer.parseInt(locctr.get(i), 16);
                        int lengthData = references.get(i).length();
                        String data = Integer.toHexString((lengthData - 3) + prevData);
                        for (int x = data.length(); x < 4; x++) {
                            data = "0" + data;
                        }
                        locctr.add(data);
                    }

                }
                // general case (word - instruction)
                else {
                    // , in word
                    if(instructions.get(i).equalsIgnoreCase("word") && references.get(i).contains(",")) {
                        int prevData = Integer.parseInt(locctr.get(i), 16);
                        String[] arr = references.get(i).trim().split(",");
                        int lengthData = arr.length;
                        String data = Integer.toHexString(((lengthData*3) + prevData));
                        for (int x = data.length(); x < 4; x++) {
                            data = "0" + data;
                        }
                        locctr.add(data);
                    }
                    else {
                        String prevLoc = locctr.get(i);
                        String loc = Integer.toHexString(Integer.parseInt(prevLoc, 16) + Integer.parseInt("3", 16));
                        for (int x = loc.length(); x < 4; x++) {
                            loc = "0" + loc;
                        }
                        locctr.add(loc);
                    }
                }

            }
        }
    }

    // getting and displaying symbol table
    public void displaySymbolTable(){
        System.out.println("=======Symbol Table=======");
        for(int i=0 ; i<labels.size() ; i++){
            if(!(labels.get(i).equalsIgnoreCase("###"))){
                for(int j =0 ; j<locctr.size() ; j++){
                    if(i==j){
                        System.out.println(labels.get(i)+" "+locctr.get(j));
                    }
                }
            }
        }
    }
}