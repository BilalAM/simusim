package physical;

import logical.Tuple;
import org.apache.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sensor {
    public String sensorName;
    public String tupleType;


    private List<FogNode> connectedFogNodes;
    private static Logger LOGGER = Logger.getLogger(Sensor.class);

    public void connectTo(FogNode fogNode){
        if(connectedFogNodes.contains(fogNode)){
            LOGGER.warn("["+this.sensorName+"]--- Already connected to FogNode [" +fogNode.getName()+"] ! Skipping this !");
        }else{
            this.connectedFogNodes.add(fogNode);
            fogNode.getConnectedSensorNodes().add(this);
            LOGGER.info("["+this.sensorName+"]--Connection established to ["+fogNode.getName()+"] ");
        }
    }

    public Sensor() {

        this.connectedFogNodes = new ArrayList<>();
    }

    public void sendTuple(Tuple tuple , FogNode toFogNode, Random random){
        if(connectedFogNodes.contains(toFogNode) && toFogNode.getConnectedSensorNodes().contains(this)){
            this.connectedFogNodes.remove(tuple);
            toFogNode.receiveTuple(tuple,random);

        }else{
            LOGGER.warn("["+sensorName+"] --Not connected to ["+toFogNode.getName()+"] ! Connect first then send the tuple !");

        }
    }


    public List<FogNode> getConnectedFogNodes() {
        return connectedFogNodes;
    }

    public void setConnectedFogNodes(List<FogNode> connectedFogNodes) {
        this.connectedFogNodes = connectedFogNodes;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getTupleType() {
        return tupleType;
    }

    public void setTupleType(String tupleType) {
        this.tupleType = tupleType;
    }
}
