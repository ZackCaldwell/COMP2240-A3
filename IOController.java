import java.util.LinkedList;
import java.util.Objects;

///-----------------------------------------------------------------
///   Class:          IOController
///   Author:         Zack Caldwell (C3356085)
///   Description:    Class to store and control access to the simulated
///                   secondary memory of a computer system. Contains
///                   a list of pages which are read by the OS and moved
///                   to main memory as required.
///-----------------------------------------------------------------

public class IOController {
    private LinkedList<Page> secondaryMemory;

    public IOController(LinkedList<Page> inputMem){
        secondaryMemory = inputMem;
    }
    public Page getPage(String inputPage) {
        for(Page page : secondaryMemory){
            if(Objects.equals(page.getId(), inputPage)){
                return page;
            }
        }
        return null;
    }
}
