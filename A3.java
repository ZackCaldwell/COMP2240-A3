import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
///-----------------------------------------------------------------
///   Class:          A3.java
///   Author:         Zack Caldwell (C3356085)
///   Description:    Main driver class of the program used to read
///                   from file and supply necessary parameters for
///                   algorithms.
///-----------------------------------------------------------------

public class A3 {
    public static void main(String[] args) {

        int framesCount;
        int quantum;
        LinkedList<Process> processes = new LinkedList<>();
        LinkedList<Page> secondaryMem = new LinkedList<>();

        //read process data, frame count and quantum from file
        try {
            framesCount = Integer.parseInt(args[0]);
            quantum = Integer.parseInt(args[1]);

            for (int i = 2; i < args.length; i++){
                Process newProcess = new Process(args[i]);

                File dataFile = new File(args[i]);
                Scanner fileReader = new Scanner(dataFile);
                String text = "";

                while(fileReader.hasNext()){
                    text += fileReader.nextLine() + " ";
                }

                String[] pageSequence = text.split(" ");
                pageSequence = Arrays.copyOfRange(pageSequence, 1, pageSequence.length - 1);
                if(pageSequence.length > 50){
                    throw new Exception();
                }

                for (String s : pageSequence) {
                    Page newPage = new Page(newProcess.getId() + "(" + s + ")");
                    newProcess.addPage(newPage.getId());
                }

                String[] uniquePages = new HashSet<String>(Arrays.asList(pageSequence)).toArray(new String[0]);
                for (String uniquePage : uniquePages) {
                    Page newPage = new Page(newProcess.getId() + "(" + uniquePage + ")");
                    secondaryMem.add(newPage);
                }

                processes.add(newProcess);
            }
        }
        catch (Exception e) {
            System.out.println("Invalid Input");
            return;
        }

        //instantiate necessary objects
        IOController ioController = new IOController(secondaryMem);
        FixedLocal scheduler1 = new FixedLocal(processes, ioController, framesCount, quantum);
        GlobalVariable scheduler2 = new GlobalVariable(processes, ioController, framesCount, quantum);

        //run algorithms
        scheduler1.run();
        scheduler2.run();

        //print results
        System.out.println("GCLOCK - Fixed-Local Replacement:\n" + scheduler1.toString());
        System.out.println("GCLOCK - Variable-Global Replacement:\n" + scheduler2.toString());
    }
}
