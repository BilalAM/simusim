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
package physical;

import logical.Tuple;
import org.apache.log4j.Logger;

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
