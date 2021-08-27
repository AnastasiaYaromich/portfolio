public class Memory {
    private int memoriInt;

    public void addMemmory(long op){
        memoriInt+=op;
    }

    public boolean checkMemmory(){
        return memoriInt != 0;
    }

    public int getMemmory (){
        return memoriInt;
    }

    public void memmoryReset (){
        memoriInt=0;
    }
}
