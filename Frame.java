///-----------------------------------------------------------------
///   Class:          Frame
///   Author:         Zack Caldwell (C3356085)
///   Description:    Frame class used to represent an segment of
///                   main memory in a computer system. Stores a single
///                   page and meta info on the frame.
///-----------------------------------------------------------------

public class Frame {
    private boolean full;
    private Page page = null;
    private int referenceCounter;

    public Frame(){
        this.full = false;
    }

    public Frame(Page newPage){
        this.full = true;
        this.page = newPage;
        this.referenceCounter = 0;
    }

    public boolean isFull() {
        return full;
    }

    public Page getPage() {
        return page;
    }

    public int getReferenceCounter() {
        return referenceCounter;
    }
    public void incrementReferenceCount() {
        referenceCounter++;
    }
    public void decrementReferenceCount(){
        referenceCounter--;
    }
}

