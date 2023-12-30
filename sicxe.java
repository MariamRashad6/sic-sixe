public class sicxe extends passTwo{
    public sicxe(){
        startPassOne();
        startPassTwo();
    }
    public void startPassOne(){
        processFile();
        getLocctr();
        displaySymbolTable();
    }
    // displaying
    public void displayTable(){
        System.out.println("===============Table===============");
        for (int i = 0; i < labels.size(); i++) {
            for (int j = 0; j < locctr.size(); j++) {
                if (j == i) {
                    System.out.print(locctr.get(i) + " ");
                }
            }
            System.out.print(labels.get(i) + " ");
            for (int j = 0; j < instructions.size(); j++) {
                if (j == i) {
                    System.out.print(instructions.get(j) + "  ");
                }
            }
            for (int j = 0; j < references.size(); j++) {
                if (j == i) {
                    System.out.print(references.get(j) + " ");
                }
            }
            for (int j = 0; j < objCode.size(); j++) {
                if (j == i) {
                    System.out.print(objCode.get(j) + " ");
                }
            }
            System.out.println(" ");
        }
    }
    public void startPassTwo(){
        getObjectCode();
        displayTable();
        getHTERecord();
    }
}