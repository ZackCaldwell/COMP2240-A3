import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

///-----------------------------------------------------------------
///   Class:          Process
///   Author:         Zack Caldwell (C3356085)
///   Description:    Container for data ran through scheduling
///                   algorithms, emulating that of a real process.
///-----------------------------------------------------------------

public class Process {

    //member variables
    private String id;
    private int ioReturnTime;
    private int finishTime;
    private int clockPointer = 0;
    private ArrayList<Integer> frameAllocationRange = new ArrayList<>();
    private ArrayList<Integer> faultTimes = new ArrayList<>();
    private LinkedList<String> pageSequence = new LinkedList<>();

    //default constructor
    public Process(){
        this.id = "";
    }

    //parameterized constructor
    public Process(String id){
        this.id = id;
    }



    //mutator methods
    public String getId() {
        return id;
    }

    public void copy(Process newProcess) {
        this.id = newProcess.getId();
        for(String pageId : newProcess.getPageQueue()){
            addPage(pageId);
        }
    };

    public int getFinishTime() {
        return finishTime;
    }
    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public Queue<String> getPageQueue(){
        return pageSequence;
    }
    public void addPage(String pageId){
        pageSequence.add(pageId);
    }

    public int getIOReturnTime() {
        return ioReturnTime;
    }
    public void setIOReturnTime(int totalTime){
        this.ioReturnTime = totalTime + 6;
    }

    public ArrayList<Integer> getFrameAllocationRange() {
        return frameAllocationRange;
    }

    public int getClockPointer() {
        return clockPointer;
    }
    public void setClockPointer(int clockPointer) {
        this.clockPointer = clockPointer;
    }
    public void incrementClockPointer(){
        clockPointer++;
    }

    public void logFault(int faultTime){
        this.faultTimes.add(faultTime);
    }
    public ArrayList<Integer> getFaultTimes() {
        return faultTimes;
    }
}
