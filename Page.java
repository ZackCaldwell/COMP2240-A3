///-----------------------------------------------------------------
///   Class:          Page
///   Author:         Zack Caldwell (C3356085)
///   Description:    Container for page data consisting of a page id,
///                   representing a small chunk of a process.
///-----------------------------------------------------------------

public class Page {
    //member variables
    private String id;

    //default constructor
    public Page(String id){
        this.id = id;
    }

    //mutator methods
    public String getId() {
        return id;
    }
}
