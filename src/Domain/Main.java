package Domain;

import Presenting.TerminalInterfaceMain;

public class Main {

    //Requestor:
    // - put task in queues (MULTIPLE QUEUES?)
    // - result have to be consumed
    //Service:
    // - service get task from queue, execute, then put processing result in queue
    //General:
    // - each task has prio low, normal, high
    // - tasks are retrieved based on priority

    public static final Integer TIMESCALE = 1000;
}


