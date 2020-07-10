package physical;

import logical.TupleState;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import logical.Tuple;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import strategies.TupleProcessingStrategy;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

public class FogNode {

    private double totalRAM;

    private static Logger LOGGER = Logger.getLogger(FogNode.class);

    private double utilizedRam;


    public List<Double> utilizationHistory = new ArrayList<>();
    public List<Long> tupleTimeHistory = new ArrayList<Long>();
    private List<Tuple> tuple;
    private List<FogNode> connectedFogNodes;
    private List<Sensor> connectedSensorNodes;
    private Queue<Tuple> pendingTuplesQueue;
    private String name;
    private double MIPS;
    private int uplinkBW;
    private int downloadBW;
    private int Id;
    private TupleProcessingStrategy tupleProcessingStrategy;
    private double powerConsumption;
    private int level;

    public FogNode(int id, String name, List<Tuple> tuple, double MIPS, int uplinkBW, int downloadBW, TupleProcessingStrategy tupleProcessingStrategy) {
        this.tuple = tuple;
        this.name = name;
        this.MIPS = MIPS;
        Id = id;
        this.downloadBW = downloadBW;
        this.uplinkBW = uplinkBW;
        this.tupleProcessingStrategy = tupleProcessingStrategy;
        tuple = new ArrayList<>();
        connectedFogNodes = new ArrayList<>();
        connectedSensorNodes = new ArrayList<>();
        pendingTuplesQueue = new ConcurrentLinkedQueue<>();
        utilizedRam = 0;
    }

    public FogNode() {
        tuple = new ArrayList<>();
        connectedFogNodes = new ArrayList<>();
        connectedSensorNodes = new ArrayList<>();
        pendingTuplesQueue = new ConcurrentLinkedQueue<>();
    }

    /**
     * Sends tuple to another fognode .
     *
     * @param tuple           : Tuple to send .
     * @param destinationNode : The destination fognode to be sent to .
     */
    public void sendTuple(Tuple tuple, FogNode destinationNode,Random random) {
        if (this.connectedFogNodes.contains(destinationNode)) {
            // remove the tuple from 'this' fognode list since it has been sent ahead.
            this.tuple.remove(tuple);
            // free up RAM
            freeUpSpace(random);
            // set that tuple to the destination fognode list of tuples .
            LOGGER.log(Level.INFO, "[" + this.getName() + "]--Tuple Sent to [" + destinationNode.getName() + "] , DECREASING ram by 10 units . Total utilized RAM is [ " + utilizedRam + " ] and total tuples in list [ " + getTuple().size() + " ]");
            destinationNode.receiveTuple(tuple,random);
        } else {
            LOGGER.warn("[" + this.getName() + "] --Not connected to [" + destinationNode.getName() + "] ! Connect first then send the tuple !");
        }
    }

    /**
     * Receives a tuple and increases the utilized RAM
     * by a factor of 10 units.
     *
     * @param tupleToReceive
     */
    public void receiveTuple(Tuple tupleToReceive,Random random) {
        // increase the utilized RAM

        // if 'this' node's RAM is full
        // send it to upper level node.
        if (this.utilizedRam > this.totalRAM) {
            LOGGER.trace("[" + this.getName() + "]--Warning ! Overloaded RAM ! , Sending remaining tuples to a high level node !");
            for (FogNode fogNode : this.getConnectedFogNodes()) {
                // if that fognode has a level 1 and has its utilized RAM less than total RAM , then send it to
                // that one
                if (isHighLevelAndHasSpace(fogNode)) {
                    fogNode.receiveTuple(tupleToReceive,random);
                    tupleToReceive.setState(TupleState.FOGNODE_TO_HIGHNODE);
                    return;
                }
            }
            // if the tuple is still not sent to any upper level nodes because of memory consumptions
            // then we would have to send it to anyone of high level node Queues .
            if (tupleToReceive.getState().equals(TupleState.SENSOR_TO_FOGNODE.toString())) {
                LOGGER.info("[" + this.getName() + "]--Tuple is still at [" + TupleState.SENSOR_TO_FOGNODE.toString() + "] , " +
                        "sending it to High FogNode PENDING QUEUE , total Tuples in list [ "+ tuple.size() +"]");
                for (FogNode fogNode : this.getConnectedFogNodes()) {
                    if (isHighLevel(fogNode)) {
                        fogNode.getPendingTuplesQueue().add(tupleToReceive);
                        tupleToReceive.setState(TupleState.FOGNODE_TO_HIGHNODE);
                        LOGGER.info("[" + this.getName() + "]--Tuple is sent to [" + fogNode.getName() + "] queue , freeing up space and list");
                        this.tuple.remove(tupleToReceive);
                        freeUpSpace(random);
                        break;
                    }
                }
            }
        } else {
            this.utilizedRam = this.utilizedRam + 10;
            this.tuple.add(tupleToReceive);
            LOGGER.log(Level.INFO, "[" + this.getName() + "]--Tuple received , INCREASING ram by 10 units . Total utilized RAM is [ " + utilizedRam + " ] and total tuples in list [ " + tuple.size() + " ]");
            utilizationHistory.add(utilizedRam);
            Instant start = Instant.now();
            processTuple(tupleToReceive,random);
            Instant end = Instant.now();
            long time = Duration.between(start,end).toMillis();
            tupleTimeHistory.add(time);
        }
    }


    /**
     * Helper method to free up space of this fognode by reducing
     * utilized RAM by 10 units .
     */
    private void freeUpSpace(Random randomizer) {
        if (this.utilizedRam == 0) {

        } else if (this.utilizedRam >= 10) {
            this.utilizedRam = this.utilizedRam - randomizer.nextInt(3) ;
        }
    }

    public int calculateRAMFromTuple(Tuple tuple) {
        double dataLength = tuple.getDataLength();
        if (dataLength >= 0 && dataLength <= 250) {
            return 200;
        } else if (dataLength >= 200 && dataLength <= 300) {
            return ThreadLocalRandom.current().nextInt(200, 300);
        } else {
            return 0;
        }
    }

    /**
     * Connects to another FogNode .
     * It works by :
     * - First check if the new fognode already is inside the connectedFogNode list or not .
     * - If it exists , do nothing and log it
     * - If it does not , then add the connectTo fognode inside 'this' fognode list
     * - And also add 'this' fognode inside the list of the connectTo fognode .
     *
     * @param connectTo
     */
    public void connectTo(FogNode connectTo) {
        if (connectedFogNodes.contains(connectTo) && connectTo.getConnectedFogNodes().contains(this)) {
            LOGGER.warn("[" + this.getName() + "]--Already connected to [" + connectTo.getName() + "] ! Skipping this ");
        } else {
            connectedFogNodes.add(connectTo);
            connectTo.getConnectedFogNodes().add(this);
            LOGGER.info("[" + this.getName() + "]--Connection established to [" + connectTo.getName() + "] ");

        }
    }


    /**
     * 'Processes' the tuple and returns the time it took in double .
     *
     * @return
     */
    public void processTuple(Tuple tuple,Random random) {
        double dataLength = tuple.getDataLength();
        try {
            if (dataLength >= 100 && dataLength <= 350) {
                Thread.sleep(ThreadLocalRandom.current().nextInt(500,1000));
            } else if (dataLength > 350 && dataLength <= 500) {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000,1500));
            } else if (dataLength > 500 && dataLength <= 1000) {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1500,2000));
            }
            freeUpSpace(random);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

    private boolean isHighLevel(FogNode fogNode) {
        return fogNode.getLevel() == 1;
    }

    private boolean isHighLevelAndHasSpace(FogNode fogNode) {
        return fogNode.getLevel() == 1 && fogNode.getUtilizedRam() <= fogNode.totalRAM;
    }

    /**
     * =================================================================================================================================================
     * GETTERS AND SETTERS BELOW
     * =================================================================================================================================================
     */
    public double getUtilizedRam() {
        return utilizedRam;
    }

    public void setUtilizedRam(double utilizedRam) {
        this.utilizedRam = utilizedRam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMIPS() {
        return MIPS;
    }

    public void setMIPS(double MIPS) {
        this.MIPS = MIPS;
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getUplinkBW() {
        return uplinkBW;
    }

    public void setUplinkBW(int uplinkBW) {
        this.uplinkBW = uplinkBW;
    }

    public int getDownloadBW() {
        return downloadBW;
    }

    public void setDownloadBW(int downloadBW) {
        this.downloadBW = downloadBW;
    }


    public TupleProcessingStrategy getTupleProcessingStrategy() {
        return tupleProcessingStrategy;
    }

    public void setTupleProcessingStrategy(TupleProcessingStrategy tupleProcessingStrategy) {
        this.tupleProcessingStrategy = tupleProcessingStrategy;
    }

    public List<Tuple> getTuple() {
        return tuple;
    }

    public void setTuple(Tuple... tuples) {
        for (Tuple tuple : tuples) {
            getTuple().add(tuple);
        }
    }

    public List<FogNode> getConnectedFogNodes() {
        return connectedFogNodes;
    }

    public void setConnectedFogNodes(List<FogNode> connectedFogNodes) {
        this.connectedFogNodes = connectedFogNodes;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isHighLevel() {
        return (utilizedRam >= 4000 && MIPS >= 5600);
    }

    public boolean isLowelLevel() {
        return (utilizedRam >= 1024 && MIPS >= 800);
    }

    public double getTotalRAM() {
        return totalRAM;
    }

    public void setTotalRAM(double totalRAM) {
        this.totalRAM = totalRAM;
    }


    public List<Sensor> getConnectedSensorNodes() {
        return connectedSensorNodes;
    }

    public void setConnectedSensorNodes(List<Sensor> connectedSensorNodes) {
        this.connectedSensorNodes = connectedSensorNodes;
    }

    public Queue<Tuple> getPendingTuplesQueue() {
        return pendingTuplesQueue;
    }

    public void setPendingTuplesQueue(Queue<Tuple> pendingTuplesQueue) {
        this.pendingTuplesQueue = pendingTuplesQueue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", Id)
                .append("name", name)
                .append("tuples", tuple)
                .append("MIPS", MIPS)
                .append("Utilized RAM", utilizedRam)
                .append("Downlink BW", downloadBW)
                .append("Uplink BW", uplinkBW)
                .toString();
    }
}
