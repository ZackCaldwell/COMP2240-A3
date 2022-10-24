import java.util.LinkedList;

///-----------------------------------------------------------------
///   Class:          GlobalVariable.java
///   Author:         Zack Caldwell (C3356085)
///   Description:    Child of base algorithm with alterations to reflect
///                   expected behavior of memory management scheme
///                   'Variable Allocation with Global Replacement Scope'
///-----------------------------------------------------------------

public class GlobalVariable extends BaseAlgorithm {

    //parameterized constructor
    public GlobalVariable(LinkedList<Process> inputReadyQueue, IOController ioController, int frameCount, int quantum) {
        super(inputReadyQueue, ioController, frameCount, quantum);
    }

    //method to check if main memory is full
    public boolean checkFramesFull(){
        for (Frame frame : mainMemory){
            if(!frame.isFull()){
                return false;
            }
        }
        return true;
    }

    //method to perform replacement procedure of a page inline with 'Variable Allocation with Global Replacement Scope'
    //Uses GLOCK replacement policy
    @Override
    public void replacementProcedure(Page newPage, Process process) {
        if(checkFramesFull()){
            //loop over main memory
            while(true){
                if(clockPointer == frameCount){
                    clockPointer = 0;
                }
                //first frame to reduce reference counter to 0 is replaced
                if(mainMemory.get(clockPointer).getReferenceCounter() == 0){
                    mainMemory.set(clockPointer, new Frame(newPage));
                    clockPointer++;
                    break;
                }
                //replace frame
                mainMemory.get(clockPointer).decrementReferenceCount();
                clockPointer++;
            }
        }
        //if frame is not full, add new frame to next available index
        else {
            mainMemory.set(clockPointer, new Frame(newPage));
            clockPointer ++;
        }
    }
}
