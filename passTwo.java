import java.util.ArrayList;

public class passTwo extends passOne{
    ArrayList<String> objCode = new ArrayList();
    String base;
    // function to get object code
    public void getObjectCode(){
        // opcode
        converter.initialize();
        for (int i = 0; i < instructions.size(); i++) {
            if (!"End".equals(labels.get(i))) {
                // handling word , byte , resw , resb
                if (instructions.get(i).equalsIgnoreCase("resw") || instructions.get(i).equalsIgnoreCase("resb") || instructions.get(i).equalsIgnoreCase("start") || instructions.get(i).equalsIgnoreCase("end") || instructions.get(i).equalsIgnoreCase("base")) {
                    objCode.add("---");
                }
                else if (instructions.get(i).equalsIgnoreCase("word")) {
                    if(references.get(i).contains(",")){
                        String[] arr;
                        String code="";
                        arr = references.get(i).trim().split(",");
                        for(int l=0;l<arr.length;l++){
                            String data = Integer.toHexString(Integer.parseInt(arr[l]));
                            for (int x = data.length(); x < 6; x++) {
                                data = "0" + data;
                            }
                            code=code+" "+data;
                        }
                        objCode.add(code);
                    }
                    else {
                        String data = Integer.toHexString(Integer.parseInt(references.get(i)));
                        for (int x = data.length(); x < 6; x++) {
                            data = "0" + data;
                        }
                        objCode.add(data);
                    }
                }
                else if (instructions.get(i).equalsIgnoreCase("byte")) {
                    if (references.get(i).startsWith("X") || references.get(i).startsWith("x")) {
                        String objectCode = references.get(i).substring(2, references.get(i).length() - 1);
                        objCode.add(objectCode);
                    }
                    else {
                        String objectCode = "";
                        // Initialize final String
                        String hex = "";

                        // Make a loop to iterate through
                        // every character of ascii string
                        for (int j = 2; j <  references.get(i).length()-1; j++) {

                            // take a char from
                            // position j of string
                            char ch =  references.get(i).charAt(j);

                            // cast char to integer and
                            // find its ascii value
                            int in = (int)ch;

                            // change this ascii value
                            // integer to hexadecimal value
                            String part = Integer.toHexString(in);

                            // add this hexadecimal value
                            // to final string.
                            hex += part;
                        }
                        objectCode=hex;
                        objCode.add(objectCode);
                    }

                }
                // handling different addressing
                // format 1
                // format 2
                // format 4
                // format 3
                else {
                    String inst = "";
                    // getting instruction
                    for (int j = 0; j < instructions.size(); j++) {
                        if (j == i) {
                            inst = instructions.get(j);
                        }
                    }
                    // getting opcode from converter class
                    String opcode = "";
                    if(inst.startsWith("+")){ // handling + in instruction
                        opcode = converter.getOpCode(inst.replace("+",""));
                    }
                    else{
                        opcode = converter.getOpCode(inst);
                    }

                    // getting format of instruction
                    String format = converter.getFormat(inst);

                    // format 1 => opcode
                    if(format.equalsIgnoreCase("1")){
                        String objectCode = opcode;
                        objCode.add(objectCode);
                    }

                    // format 2
                    else if(format.equalsIgnoreCase("2")){
                        // getting reference
                        String ref = "";
                        for (int k = 0; k < references.size(); k++) {
                            if (k == i) {
                                ref = references.get(k);
                            }
                        }
                        String firstRegister="";
                        String secondRegister="";

                        String firstRegisterCode="";
                        String secondRegisterCode="";

                        String objectCode = "";
                        // in case of 2 registers
                        if (ref.toLowerCase().contains(",".toLowerCase())) {
                            // get registers
                            firstRegister = ref.substring( 0, ref.indexOf(","));
                            secondRegister = ref.substring(ref.indexOf(",")+1, ref.length());
                            // get number of registers
                            firstRegisterCode = getRegister(firstRegister);
                            secondRegisterCode = getRegister(secondRegister);
                            // object code
                            objectCode=opcode+firstRegisterCode+secondRegisterCode;
                        }
                        // in case of 1 register
                        else{
                            firstRegister = ref;
                            firstRegisterCode = getRegister(firstRegister);
                            objectCode=opcode+firstRegisterCode+"0";
                        }
                        objCode.add(objectCode);
                    }
                    // format 3,4
                    else {
                        // get value of base
                        getBase();

                        // getting reference , pc
                        String ref = ""; // reference
                        // rsub
                        if (inst.equalsIgnoreCase("rsub")) {
                            objCode.add("4F0000");
                        }
                        else {
                            String pc = "";
                            // refrence
                            for (int j = 0; j < references.size(); j++) {
                                if (j == i) {
                                    ref = references.get(j);
                                }
                            }
                            // pc
                            for (int j = 0; j < locctr.size(); j++) {
                                if (j == i) {
                                    pc = locctr.get(j+1);
                                }
                            }
                            // getting opcode in binary
                            String binaryOpCode = convertOpcodeToBinary(opcode);
                            // getting objectCode
                            objCode obj = new objCode(inst, ref, pc, base, binaryOpCode);
                            String code = obj.getObjCode();
                            objCode.add(code);
                        }
                    }
                }

            }
        }
    }

    // function to get and display HTE Record
    public void getHTERecord(){
        System.out.println("=======HTE RECORD=======");
        ArrayList<String> Header = new ArrayList();
        // header
        Header.add(labels.get(0));
        String data = references.get(0);
        for (int x = data.length(); x < 6; x++) {
            data = "0" + data;
        }
        Header.add(data);
        String str = Integer.toHexString(Integer.parseInt(locctr.get(locctr.size()-1), 16) - Integer.parseInt(locctr.get(1), 16));
        for (int x = str.length(); x < 6; x++) {
            str = "0" + str;
        }
        Header.add(str);
        System.out.print("H ");
        for (int i = 0; i < Header.size(); i++) {
            System.out.print(Header.get(i) + " ");
        }
        System.out.println("");



        // text
        String start=locctr.get(1);
        for (int x = start.length(); x < 6; x++) {
            start = "0" + start;
        }
        String end="";
        int counter=0;
        int num=0;
        ArrayList<String> text = new ArrayList();
        for(int i=1;i<instructions.size();i++){
            // resw , resb
            if(instructions.get(i).equalsIgnoreCase("resw")||instructions.get(i).equalsIgnoreCase("resb")) {
                if(num==0){
                    end = locctr.get(i-num);
                    tRecord t = new tRecord(start, end, text);
                    t.display();
                    text=new ArrayList<>();
                }
                num++;
                if(!(instructions.get(i+1).equalsIgnoreCase("resw") || instructions.get(i+1).equalsIgnoreCase("resb"))) {
                    start = locctr.get(i+1);
                    for (int x = start.length(); x < 6; x++) {
                        start = "0" + start;
                    }
                    counter = 0;
                }
            }
            // if didn,t reach 10 but reached end
            else if(instructions.get(i).equalsIgnoreCase("end") && counter<10){
                end=locctr.get(i);
                tRecord t = new tRecord(start,end,text);
                t.display();
            }
            // length of record didn't reach 10
            else if(counter < 10){
                if(!objCode.get(i).equalsIgnoreCase("---")) {
                    text.add(objCode.get(i));
                    counter++;
                }
            }
            // counter > 10
            else{
                end=locctr.get(i);
                tRecord t = new tRecord(start,end,text);
                t.display();
                text=new ArrayList<>();
                start=end;
                for (int x = start.length(); x < 6; x++) {
                    start = "0" + start;
                }
                text.add(objCode.get(i));
                counter=1;
            }
        }
        // modification record
        for(int k=0;k< instructions.size();k++){
            if(instructions.get(k).contains("+")&& !(references.get(k).contains("#"))){
                String var = Integer.toHexString(Integer.parseInt(locctr.get(k), 16) + Integer.parseInt("1", 16));
                for (int x = var.length(); x < 4; x++) {
                    var = "0" + var;
                }
                System.out.println("M "+var+" "+"05 "+labels.get(0));
            }
        }

        // end
        data =references.get(0);
        for (int x = data.length(); x < 6; x++) {
            data = "0" + data;
        }
        System.out.println("E"+" "+data);
    }

    // function to get register number
    public String getRegister(String register){
        if(register.equalsIgnoreCase("a")){
            return "0";
        }
        else if(register.equalsIgnoreCase("x")){
            return "1";
        }
        else if(register.equalsIgnoreCase("b")){
            return "3";
        }
        else if(register.equalsIgnoreCase("s")){
            return "4";
        }
        else if(register.equalsIgnoreCase("t")){
            return "5";
        }
        else if(register.equalsIgnoreCase("f")){
            return "6";
        }
        return "";
    }

    // function to convert opcode to binary
    public String convertOpcodeToBinary(String opcode){
        // 2d array with each hexa number and its binary representation
        String[][] binary = new String[16][2];
        binary[0] = new String[] {"0","0000"};
        binary[1] = new String[] {"1","0001"};
        binary[2] = new String[] {"2","0010"};
        binary[3] = new String[] {"3","0011"};
        binary[4] = new String[] {"4","0100"};
        binary[5] = new String[] {"5","0101"};
        binary[6] = new String[] {"6","0110"};
        binary[7] = new String[] {"7","0111"};
        binary[8] = new String[] {"8","1000"};
        binary[9] = new String[] {"9","1001"};
        binary[10] = new String[] {"A","1010"};
        binary[11] = new String[] {"B","1011"};
        binary[12] = new String[] {"C","1100"};
        binary[13] = new String[] {"D","1101"};
        binary[14] = new String[] {"E","1110"};
        binary[15] = new String[] {"F","1111"};
        // get first character in opcode
        String first=Character.toString(opcode.charAt(0));
        // convert first hexa to binary
        String firstBinary="";
        for(int i=0 ; i<16 ; i++){
            if(binary[i][0].equalsIgnoreCase(first)){
                firstBinary = binary[i][1];
            }
        }
        // get second character in opcode
        String second=Character.toString(opcode.charAt(1));
        // convert second hexa to binary
        String secondBinary="";
        for(int i=0 ; i<16 ; i++){
            if(binary[i][0].equalsIgnoreCase(second)){
                secondBinary = binary[i][1];
            }
        }
        // return opcode in binary discarding last 2 bits
        return firstBinary+secondBinary.substring(0,2);
    }

    // function to get base
    public void getBase(){
        for(int i=0;i< instructions.size();i++) {
            // handling if base or ldb
            if (instructions.get(i).equalsIgnoreCase("base") || instructions.get(i).equalsIgnoreCase("ldb")) {
                String ref = "";
                // getting reference
                for (int k = 0; k < references.size(); k++) {
                    if (k == i) {
                        ref = references.get(k);
                        // if ref indirect
                        if(ref.contains("@")){
                            ref.replace("@","");
                        }
                        // if reference immediate
                        if(ref.contains("#")){
                            ref.replace("#","");
                        }
                        // if reference indexed
                        if(ref.toLowerCase().contains(",x".toLowerCase())){
                            ref = ref.substring(0,ref.length()-2);
                        }
                        for (int m = 0; m < labels.size(); m++) {
                            // getting location counter
                            if (labels.get(m).equalsIgnoreCase(ref)) {
                                for (int n = 0; n < locctr.size(); n++) {
                                    if (n == m) {
                                        this.base = locctr.get(n);
                                    }
                                }
                            }
                        }
                    }
                }
                // handling immediate +value
                if(this.base==""){
                    this.base=Integer.toHexString(Integer.parseInt(ref));
                }
            }

        }
    }
}