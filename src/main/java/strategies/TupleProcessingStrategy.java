package strategies;

import logical.Tuple;
import org.apache.log4j.Logger;

import java.util.*;

public class TupleProcessingStrategy {
    private static final Logger LOGGER = Logger.getLogger(TupleProcessingStrategy.class);


    public void execute(List<Tuple> incomingTuples) {
        List<Tuple> waitingTuples = new ArrayList<Tuple>();
        List<Tuple> finishedTuples = new ArrayList<>();
        if (incomingTuples.size() == 1) {
            LOGGER.info("Incoming list has only 1 tuple");
            Tuple processedTuple = processTuple(incomingTuples.get(0));
            finishedTuples.add(processedTuple);
        } else if (incomingTuples.size() > 1) {
            LOGGER.info("Incoming list has [ " + incomingTuples.size() + " ] tuples");
            for (Tuple t : incomingTuples) {
                waitingTuples.add(t);
            }
        }
        // i have the sorted list over here accoding to the data length
        waitingTuples = sortByMIPS(waitingTuples);
        for (Tuple t : waitingTuples) {
            LOGGER.info("Selected shortest data lenth tuple from waiting list to process");
            t = processTuple(t);
            finishedTuples.add(t);
        }
        LOGGER.info("Finished");
    }


    public static List<Tuple> sortByMIPS(List<Tuple> listOfTuples) {
        Collections.sort(listOfTuples, new Comparator<Tuple>() {
            public int compare(Tuple t1, Tuple t2) {
                return Double.compare(t1.getDataLength(), t2.getDataLength());
            }
        });
        return listOfTuples;
    }

    public Tuple processTuple(Tuple tuple) {
        LOGGER.info("Processing tuple ID [ " + tuple.getTupleId() + " ] with data-length [ " + tuple.getDataLength() + " ]");
        LOGGER.info("Processing completed , setting state to TRUE");
        return tuple;
    }

    public String isFirstTuple(String fogDevice) {
        return "hello";
    }
}
