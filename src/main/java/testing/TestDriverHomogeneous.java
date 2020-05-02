package testing;

import freemarker.template.Configuration;
import freemarker.template.Template;
import logical.Tuple;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import physical.FogNode;
import physical.Sensor;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    public static void main(String[] args) throws Exception {
            FogNode f1 = createHomogeneousFogNode(1,"f1",0);
            FogNode f2 = createHomogeneousFogNode(2,"f2",0);
            FogNode f3 = createHomogeneousFogNode(3,"f3",1);

            Sensor s1 = TestDriverUtils.createSensor(1,"S1");
            Sensor s2 = TestDriverUtils.createSensor(2,"S2");

            //establish connections
            s1.connectTo(f1);
            s2.connectTo(f2);
            f1.connectTo(f3);
            f2.connectTo(f3);

            List<Tuple> testTuples = TestDriverUtils.createTuples(10);
            Instant start = Instant.now();

               for (Tuple t : testTuples) {
                   new Thread(() -> s1.sendTuple(t, f1)).start();
                   s2.sendTuple(t, f2);
               }

            Instant end = Instant.now();
        long seconds = Duration.between(start,end).getSeconds();
        LOGGER.log(Level.INFO,"Simulation took [ " + seconds + " ] to complete ");
        appendResultsToFile(f1,f2,f3);
    }


    /**
     * Appends all the result summary of the fognodes into their corresonding FTL files which are then
     * resolved into txt files created at the root folder of project .
     * @param f1 : Fognode 1
     * @param f2 : Fognode 2
     * @param f3 : Fognode 3
     * @throws Exception
     */
    private static void appendResultsToFile(FogNode f1 , FogNode f2 , FogNode f3) throws Exception {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);

        cfg.setClassForTemplateLoading(TestDriverHomogeneous.class, "/");
        cfg.setDefaultEncoding("UTF-8");

        Template template = cfg.getTemplate("ftls/f1-result.ftl");

        Map<String, Object> templateData = new HashMap<>();
        templateData.put("totalTuples", f1.getTuple().size());
        templateData.put("dateTime", LocalDateTime.now().toString());
        templateData.put("connectedDevices",f1.getConnectedFogNodes().toString());
        templateData.put("rams",f1.utilizationHistory);
        templateData.put("tuples",f1.tupleTimeHistory);

        try (FileWriter out = new FileWriter(new File("f1-result-final.txt"),false)) {
            template.process(templateData, out);
            out.flush();
        }

        Template template2 = cfg.getTemplate("ftls/f2-result.ftl");

        Map<String, Object> templateData2 = new HashMap<>();
        templateData2.put("totalTuples", f2.getTuple().size());
        templateData2.put("dateTime", LocalDateTime.now().toString());
        templateData2.put("connectedDevices",f2.getConnectedFogNodes().toString());
        templateData2.put("rams",f2.utilizationHistory);
        templateData2.put("tuples",f2.tupleTimeHistory);

        try (FileWriter out = new FileWriter(new File("f2-result-final.txt"),false)) {
            template2.process(templateData2, out);
            out.flush();
        }

        Template template3 = cfg.getTemplate("ftls/f3-result.ftl");

        Map<String, Object> templateData3 = new HashMap<>();
        templateData3.put("totalTuples", f3.getTuple().size());
        templateData3.put("dateTime", LocalDateTime.now().toString());
        templateData3.put("connectedDevices",f3.getConnectedFogNodes().toString());
        templateData3.put("rams",f3.utilizationHistory);
        templateData3.put("tuples",f3.tupleTimeHistory);

        try (FileWriter out = new FileWriter(new File("f3-result-final.txt"),false)) {
            template3.process(templateData3, out);
            out.flush();
        }
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
