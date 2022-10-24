import java.util.*;

///-----------------------------------------------------------------
///   Class:          Base Algorithm
///   Author:         Zack Caldwell (C3356085)
///   Description:    Abstract parent class of which both scheme's scheduler
//                    would inherit from to add their unique behaviour
///                   without mass code rewrites.
///-----------------------------------------------------------------

public abstract class BaseAlgorithm {

    //member variables
    protected int totalTime = 0;
    protected int clockPointer = 0;
    protected int globalQuantum;
    protected int frameCount;
    protected IOController ioController;
    protected LinkedList<Process> blocked = new LinkedList<>();
    protected ArrayList<Frame> mainMemory;
    protected LinkedList<Process> ready = new LinkedList<>();
    protected Process runningProcess;
    protected LinkedList<Process> finished = new LinkedList<>();
    protected LinkedList<Process> processOrder = new LinkedList<>();

    //parameterized constructor
    public BaseAlgorithm(LinkedList<Process> inputReadyQueue, IOController ioController, int frameCount, int quantum){
        this.ioController = ioController;
        this.mainMemory = new ArrayList<>(frameCount);
        this.globalQuantum = quantum;
        this.frameCount = frameCount;
        initializeFrames();
        copyReadyList(inputReadyQueue);
    }

    //main driving class of scheduler
    public void run(){
        //While there are unfinished processes
        while(!blocked.isEmpty() || !ready.isEmpty() || runningProcess != null) {
            checkBlockedForReadyProcesses();
            //if these is a ready process available, run it until interrupt
            if(!ready.isEmpty()){
                runningProcess = ready.poll();
                executeProcess();
            }
            else {
                totalTime++;
            }
        }
    };

    //method to execute a process
    public void executeProcess(){
        int quantum = globalQuantum;
        //while the quantum time has not expired or the running process is the only ready process
        while((quantum != 0 && !runningProcess.getPageQueue().isEmpty()) ||
                (ready.isEmpty() && !runningProcess.getPageQueue().isEmpty())) {

            //if desired page is in main memory, run it
            if(checkFrames()){
                runningProcess.getPageQueue().poll();
                totalTime++;
                checkBlockedForReadyProcesses();
                quantum--;
            }
            //if it isn't, send the process to the blocked queue
            else {
                blocked.add(runningProcess);
                runningProcess.logFault(totalTime);
                runningProcess.setIOReturnTime(totalTime);
                runningProcess = null;
                return;
            }
        }
        //once process has be interrupted, if process is finished, add to finished list, if not, return it to ready queue
        if(runningProcess.getPageQueue().isEmpty()){
            runningProcess.setFinishTime(totalTime);
            finished.add(runningProcess);
            runningProcess = null;
        } else if (quantum == 0) {
            ready.add(runningProcess);
            runningProcess = null;
        }
    };

    //method to propogate list with empty frames
    private void initializeFrames() {
        for (int i = 0; i < frameCount; i++){
            mainMemory.add(new Frame());
        }
    };

    //method to perform deep copy of input ready queue upon construction
    private void copyReadyList(LinkedList<Process> inputReadyQueue) {
        for(Process inputProcess : inputReadyQueue){
            Process newProcess = new Process();
            newProcess.copy(inputProcess);
            this.ready.add(newProcess);
            this.processOrder.add(newProcess);
        }
    };

    //traverses main memory to check if a process's required page is present
    public boolean checkFrames(){
        for(Frame frame : mainMemory){
            if(frame.isFull()){
                if(Objects.equals(runningProcess.getPageQueue().peek(), frame.getPage().getId())){
                    frame.incrementReferenceCount();
                    return true;
                }
            }
        }
        return false;
    };

    //traverses the blocked queue and adds any ready processes to the ready queue
    public void checkBlockedForReadyProcesses(){
        Iterator<Process> iterator = blocked.iterator();
        while(iterator.hasNext()){
            Process process = iterator.next();
            if (process.getIOReturnTime() == totalTime){
                Page fetchedPage = ioController.getPage(process.getPageQueue().peek());
                replacementProcedure(fetchedPage, process);
                ready.add(process);
                iterator.remove();
            }
        }
    };

    //abstract method for the replacing an existing page in main memory with a new required page
    public abstract void replacementProcedure(Page newPage, Process process);

    //method to output algorithm results
    public String toString(){
        String output = "PID\tProcess Name\t\tTurnaround Time\t# Faults\tFault Times\n";
        for(int i = 0; i < processOrder.size(); i++){
            output += (i + 1) +"\t" +
                    processOrder.get(i).getId() + "\t\t" +
                    processOrder.get(i).getFinishTime() + "\t\t\t\t" +
                    processOrder.get(i).getFaultTimes().size() + "\t\t\t" +
                    processOrder.get(i).getFaultTimes() + "\n";
        }
        return output;
    }
}
