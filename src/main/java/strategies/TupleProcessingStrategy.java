/**
 * MIT License
 * <p>
 * Copyright (c) 2019 Bilal Asif Mirza
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
