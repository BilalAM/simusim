package testing;

import logical.Tuple;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import physical.FogNode;
import physical.Sensor;
import physical.Topology;

import java.time.Duration;
import java.time.Instant;
import java.util.List;


/**
 *  =============================================================================================
 *                                      PLEASE READ THIS
 *  =============================================================================================
 *
 *  Test Driver class to simulate a pre established static network
 *  The network is composed of :
 *
 *   -  3 Fognodes , out of which 2 are low level and 1 are high level fognode .
 *   -  2 Sensors , that are connected with the 3 low level fognodes
 *   -  n Tuples , variable number of Tuples generated .
 *
 *   The network diagram is kind of like the below one :
 *
 *                                      High-Level-FogNode-1
 *                                              /   \
 *                                             /     \
 *                                            /       \
 *                                           /         \
 *                           Low-Level-FogNode-1      Low-Level-FogNode-2
 *                                     |                        |
 *                                     |                        |
 *                                     |                        |
 *                              Sensor-1                      Sensor-2
 *
 *
 */
public class TestDriverHomogeneous {
    private static Logger LOGGER = Logger.getLogger(TestDriverHomogeneous.class);
    public static void main(String[] args) {
            FogNode f1 = createHomogeneousFogNode(1,"f1",0);
            FogNode f2 = createHomogeneousFogNode(2,"f2",0);
            FogNode f3 = createHomogeneousFogNode(3,"f3",1);

            Sensor s1 = TestDriverUtils.createSensor(1,"S1");
            Sensor s2 = TestDriverUtils.createSensor(2,"S2");

            //establish connections
            s1.connectTo(f1);
            s2.connectTo(f1);
            f1.connectTo(f3);
            f2.connectTo(f3);




            List<Tuple> testTuples = TestDriverUtils.createTuples(10);
            List<Tuple> testTuples2 = TestDriverUtils.createTuples(50);
            Instant start = Instant.now();



               for (Tuple t : testTuples) {
                   new Thread(() -> s1.sendTuple(t, f1)).start();
                   s2.sendTuple(t, f1);
               }

            Instant end = Instant.now();
        long seconds = Duration.between(start,end).getSeconds();
        LOGGER.log(Level.INFO,"Simulation took [ " + seconds + " ] to complete ");

    }




    public static FogNode createHomogeneousFogNode(int id, String name, int level) {
        FogNode fogNode = new FogNode();
        fogNode.setId(id);
        fogNode.setName(name);
        fogNode.setLevel(level);
        fogNode.setMIPS(1000);
        fogNode.setDownloadBW(100);
        fogNode.setUplinkBW(100);
        fogNode.setTotalRAM(100);
        return fogNode;
    }

}
