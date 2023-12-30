/*
   This class handles obj code of format 3 , 4
 */
public class objCode extends passTwo{
    String n,i,x,b,p,e,inst,ref,pc,TA,disp,base,opcode,address;
    public objCode(String inst,String ref,String pc,String base,String opcode){
        this.inst=inst;
        this.ref=ref;
        this.pc=pc;
        this.base=base;
        this.opcode=opcode;
        getFlatBits();
    }

    // function to set flatbits & get displacement or address
    void getFlatBits(){
        // format 4
        if(inst.startsWith("+")){
            // n , i
            // immediate
            if(ref.startsWith("#")){
                this.n="0";
                this.i="1";
            }
            // indirect
            else if(ref.startsWith("@")){
                this.n="0";
                this.i="1";
            }
            // direct
            else{
                this.n="1";
                this.i="1";
            }

            // x
            if (ref.toLowerCase().contains(",x".toLowerCase())) {
                x="1";
            }
            else {
                x="0";
            }

            // b , p , e
            this.b="0";
            this.p="0";
            this.e="1";
        }
        // format 3
        else{
            // n , i
            // immediate
            if(ref.startsWith("#")){
                this.n="0";
                this.i="1";
            }
            // indirect
            else if(ref.startsWith("@")){
                this.n="1";
                this.i="0";
            }
            // direct
            else{
                this.n="1";
                this.i="1";
            }

            // x
            if (ref.toLowerCase().contains(",x".toLowerCase())) {
                x="1";
            }
            else {
                x="0";
            }

            // b , p
            if(ref.startsWith("#")){
                int flag=0;
                for(int i=0;i< labels.size();i++){
                    if(labels.get(i).equalsIgnoreCase(ref.replace("#",""))){
                        flag=1;
                    }
                }
                // variable
                if(flag==1){
                    if(Integer.parseInt(getTA(),16)-Integer.parseInt(pc,16) < Integer.parseInt("2047",16)){
                        this.p="1";
                        this.b="0";
                        this.disp=Integer.toHexString(Integer.parseInt(getTA(),16)-Integer.parseInt(pc,16));
                    }
                    else{
                        this.b="1";
                        this.p="0";
                        this.disp=Integer.toHexString(Integer.parseInt(getTA(),16)-Integer.parseInt(base));
                    }
                }
                // value
                else{
                    this.p="0";
                    this.b="0";
                    this.disp=getTA();
                }
            }
            else {
                if (Integer.parseInt(getTA(), 16) - Integer.parseInt(pc, 16)< 2047 && Integer.parseInt(getTA(), 16) - Integer.parseInt(pc, 16) > -2048) {
                    this.p = "1";
                    this.b = "0";
                    this.disp = Integer.toHexString(Integer.parseInt(getTA(), 16) - Integer.parseInt(pc, 16));
                }
                else {
                    this.b = "1";
                    this.p = "0";
                    this.disp = Integer.toHexString(Integer.parseInt(getTA(), 16) - Integer.parseInt(base,16));
                }
            }
            // e
            this.e="0";
        }
    }

    // function to get targert address
    public String getTA(){
        // indirect
        if(ref.startsWith("@")){
            // indexed
            if(ref.toLowerCase().contains(",x".toLowerCase())){
                ref = ref.substring(0,ref.length()-2);
            }
            // getting TA
            for(int i=0;i< labels.size();i++){
                if(labels.get(i).equalsIgnoreCase(ref.replace("@",""))){
                    this.TA=locctr.get(i);
                }
            }
            return TA;
        }

        // immediate
        else if(ref.startsWith("#") ){
            // indexed
            if(ref.toLowerCase().contains(",x".toLowerCase())){
                ref = ref.substring(0,ref.length()-2);
            }
            int flag=0;
            String str="";
            for(int i=0;i< labels.size();i++){
                if(labels.get(i).equalsIgnoreCase(ref.replace("#",""))){
                    str=locctr.get(i);
                    flag=1;
                }
            }
            // variable
            if(flag==1){
                this.TA=str;
            }
            // value
            else{
                String hex= Integer.toHexString(Integer.parseInt(ref.replace("#","")));
                this.TA=hex;
            }
            return TA;
        }
        // direct
        else{
            // indexed
            if(ref.toLowerCase().contains(",x".toLowerCase())){
                ref = ref.substring(0,ref.length()-2);
            }
            for(int i=0;i< labels.size();i++){
                if(labels.get(i).equalsIgnoreCase(ref)){
                    this.TA=locctr.get(i);
                }
            }
            return TA;
        }
    }

    // function to calculate object code
    public String getObjCode(){

        // setting flat bits
        String str="";
        str = this.opcode+this.n+this.i+this.x+this.b+this.p+this.e;

        // format 4
        if(inst.startsWith("+")){
            // indirect
            if(ref.startsWith("@")){
                // getting address
                for(int i=0;i< labels.size();i++){
                    if(labels.get(i).equalsIgnoreCase(ref.replace("@",""))){
                        this.address=locctr.get(i);
                    }
                }
            }

            // immediate
            else if(ref.startsWith("#")){
                // getting address
                String TA="";
                int flag=0;
                for(int i=0;i< labels.size();i++){
                    if(labels.get(i).equalsIgnoreCase(ref.replace("#",""))){
                        TA=locctr.get(i);
                        flag=1;
                    }
                }
                // variable
                if(flag==1){
                    this.address=TA;
                }
                // value
                else{
                    String hex= Integer.toHexString(Integer.parseInt(ref.replace("#","")));
                    this.address=hex;
                }

            }

            // direct
            else{
                // getting address
                for(int i=0;i< labels.size();i++){
                    if(labels.get(i).equalsIgnoreCase(ref)){
                        this.address=locctr.get(i);
                    }
                }
            }

            // returning object code
            // checking length of address
            if(this.address.length()<5){
                for (int x = this.address.length(); x < 5; x++) {
                    this.address = "0"+this.address;
                }
            }
            String objCode=Integer.toHexString(Integer.parseInt(str.substring(0,4),2))+Integer.toHexString(Integer.parseInt(str.substring(4,8),2))+Integer.toHexString(Integer.parseInt(str.substring(8,str.length()),2))+this.address;
            return objCode;
        }

        // format 3
        else{
            // returning object code
            // checking length of displacement
            // less than 3
            if(this.disp.length()<3){
                for (int x = this.disp.length(); x < 3; x++) {
                    this.disp = "0"+this.disp;
                }
            }
            // more than 3 (handling negative (ffffff))
            if(this.disp.length()>3){
                this.disp = this.disp.substring(this.disp.length()-3,this.disp.length());
            }

            String objCode=Integer.toHexString(Integer.parseInt(str.substring(0,4),2))+Integer.toHexString(Integer.parseInt(str.substring(4,8),2))+Integer.toHexString(Integer.parseInt(str.substring(8,str.length()),2))+this.disp;
            return objCode;
        }

    }

}