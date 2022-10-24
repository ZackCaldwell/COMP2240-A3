import java.util.LinkedList;

///-----------------------------------------------------------------
///   Class:          Variable Allocation with Global Replacement Scope
///   Author:         Zack Caldwell (C3356085)
///   Description:    Child of base algorithm with alterations to reflect
///                   expected behavior of memory management scheme
///                   'Fixed Allocation with Local Replacement Scope'
///-----------------------------------------------------------------

public class FixedLocal extends BaseAlgorithm {

    //parameterized constructor
    public FixedLocal(LinkedList<Process> inputReadyQueue, IOController ioController, int frameCount, int quantum) {
        super(inputReadyQueue, ioController, frameCount, quantum);
        assignPartition();
    }

    //method to divide main memory into equal partitions and assign them to a process
    public void assignPartition(){
        int framePartitionSize = frameCount / ready.size();
        int counter = 0;
        for (Process process : ready){
            int i = counter;
            process.setClockPointer(counter);
            while(i < counter + framePartitionSize){
                process.getFrameAllocationRange().add(i);
                i++;
            }
            counter += framePartitionSize;
        }
    }

    //method to check if a process's designated partition of main memory is full
    public boolean checkPartitionFull(Process process){
        for (int index : process.getFrameAllocationRange()){
            if(!mainMemory.get(index).isFull()){
                return false;
            }
        }
        return true;
    }

    //method to perform replacement procedure of a page inline with 'Fixed Allocation with Local Replacement Scope'
    //Uses GLOCK replacement policy
    @Override
    public void replacementProcedure(Page newPage, Process process) {
        if(checkPartitionFull(process)){
            int start = process.getFrameAllocationRange().get(0);
            int end = process.getFrameAllocationRange().get(process.getFrameAllocationRange().size() - 1) + 1;
            //iterate over process's partition of main memory
            while(true){
                //loop if reach end of partition
                if(process.getClockPointer() == end){
                    process.setClockPointer(start);
                }
                //first frame to reduce reference counter to 0 is replaced
                if(mainMemory.get(process.getClockPointer()).getReferenceCounter() == 0){
                    mainMemory.set(process.getClockPointer(), new Frame(newPage));
                    process.incrementClockPointer();
                    break;
                }
                //replace frame
                mainMemory.get(process.getClockPointer()).decrementReferenceCount();
                process.incrementClockPointer();
            }
        }
        //if frame is not full, add new frame to next available index
        else {
            mainMemory.set(process.getClockPointer(), new Frame(newPage));
            process.incrementClockPointer();
        }
    }
}
