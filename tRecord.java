import java.util.ArrayList;

public class tRecord {
    String start;
    String end;
    String length;
    ArrayList<String> opcodes;
    tRecord(String start , String end ,ArrayList<String> opcodes){
        this.start = start;
        this.end = end;
        this.opcodes = opcodes;
        getLength();
    }
    // function to get length of each T record
    public void getLength(){
        this.length = Integer.toHexString(Integer.parseInt(this.end,16)-Integer.parseInt(this.start,16));
        if(this.length.length()<2){
            this.length = "0"+this.length;
        }
    }

    // function to display T record
    public void display(){
        System.out.print("T"+" ");
        System.out.print(start+" ");
        System.out.print(length+" ");
        for(int i=0 ; i< opcodes.size() ; i++){
            System.out.print(opcodes.get(i)+" ");
        }
        System.out.println("");
    }
}



