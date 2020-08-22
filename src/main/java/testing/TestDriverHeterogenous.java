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
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;


/**
 * =============================================================================================
 * PLEASE READ THIS
 * =============================================================================================
 * <p>
 * Test Driver class to simulate a pre established static network
 * The network is composed of :
 * <p>
 * -  3 Fognodes , out of which 2 are low level and 1 are high level fognode .
 * -  2 Sensors , that are connected with the 3 low level fognodes
 * -  n Tuples , variable number of Tuples generated .
 * <p>
 * The network diagram is kind of like the below one :
 * <p>
 * High-Level-FogNode-1
 * /   \
 * /     \
 * /       \
 * /         \
 * Low-Level-FogNode-1      Low-Level-FogNode-2
 * |                        |
 * |                        |
 * |                        |
 * Sensor-1                      Sensor-2
 */
public class TestDriverHeterogenous {
    private static Logger LOGGER = Logger.getLogger(TestDriverHeterogenous.class);

    public static void main(String[] args) throws Exception {

        // FIRST PARAMETER = id
        // SECOND PARAMETER = name
        // THIRD PARAMETER = level ( you dont have to change that since its the topology )
        // FORUTH PARAMETER = RAM ( change this accordingly )
        // FIFTH PARAMETER = "downloadBW" ( change this accordingly )


        FogNode f1 = createHomogeneousFogNode(1, "f1", 0,100,50);
        FogNode f2 = createHomogeneousFogNode(2, "f2", 0,200,100);
        FogNode f3 = createHomogeneousFogNode(3, "f3", 1,400,100);
        FogNode f4 = createHomogeneousFogNode(4, "f4", 1,300,300);
        FogNode f5 = createHomogeneousFogNode(5, "f5", 1,50,200);

        Sensor s1 = TestDriverUtils.createSensor(1, "S1");
        Sensor s2 = TestDriverUtils.createSensor(2, "S2");
        Sensor s3 = TestDriverUtils.createSensor(3, "S3");

        //establish connections
        s1.connectTo(f1);
        s2.connectTo(f2);
        s3.connectTo(f3);


        f1.connectTo(f4);
        f1.connectTo(f5);

        f2.connectTo(f4);
        f2.connectTo(f5);

        f3.connectTo(f4);
        f3.connectTo(f5);

        /**
         * ===================================================================================
         *
         *
         *     CHANGE THE seed value FOR ALL THREE RANDOM OBJECTS INSIDE THE new Random() . ( Should not be same )
         *     CHANGE THE NUMBER OF TUPLES GENERATED BY A SENSOR BY CHANGING THE VALUE INSIDE THE createTuples()
         */
        Random randomizer = new Random(20);
        List<Tuple> testTuples = TestDriverUtils.createTuples(4, randomizer);
        randomizer = new Random(21);
        List<Tuple> testTuples2 = TestDriverUtils.createTuples(15, randomizer);
        randomizer = new Random(22);
        List<Tuple> testTuples3 = TestDriverUtils.createTuples(20, randomizer);
        /**
         * ===================================================================================
         */

        Instant start = Instant.now();

        Random finalRandomizer1 = randomizer;
        new Thread(() -> {
            for (int i = 0; i < testTuples.size(); i++) {
                Random finalRandomizer = finalRandomizer1;
                int finalI = i;
                s1.sendTuple(testTuples.get(finalI), f1, finalRandomizer);
            }
        },"custom").start();

        Random finalRandomizer2 = randomizer;
        new Thread(() -> {
            for (int i = 0; i < testTuples2.size(); i++) {
                Random finalRandomizer = finalRandomizer2;
                int finalI = i;
                s2.sendTuple(testTuples2.get(finalI), f2, finalRandomizer);
            }
        },"custom").start();


        for (int i = 0; i < testTuples3.size(); i++) {
            s3.sendTuple(testTuples3.get(i), f3, randomizer);
        }


        Instant end = Instant.now();
        long seconds = Duration.between(start, end).getSeconds();
        Set<Thread> allThreads = Thread.getAllStackTraces().keySet();
        for(Thread t : allThreads){
            if(t.getName().equalsIgnoreCase("custom")){
                t.join();
            }
        }
        LOGGER.log(Level.INFO, "Simulation took [ " + seconds + " ] to complete ");
        appendResultsToFile(f1, f2, f3, f4, f5);
    }


    /**
     * Appends all the result summary of the fognodes into their corresonding FTL files which are then
     * resolved into txt files created at the root folder of project .
     *
     * @param f1 : Fognode 1
     * @param f2 : Fognode 2
     * @param f3 : Fognode 3
     * @throws Exception
     */
    private static void appendResultsToFile(FogNode f1, FogNode f2, FogNode f3, FogNode f4, FogNode f5) throws Exception {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);

        cfg.setClassForTemplateLoading(TestDriverHeterogenous.class, "/");
        cfg.setDefaultEncoding("UTF-8");

        Template template = cfg.getTemplate("ftls/f1-result.ftl");

        Map<String, Object> templateData = new HashMap<>();
        templateData.put("totalTuples", f1.getTuple().size());
        templateData.put("dateTime", LocalDateTime.now().toString());
        templateData.put("connectedDevices", f1.getConnectedFogNodes().toString());
        templateData.put("rams", f1.utilizationHistory);
        templateData.put("tuples", f1.tupleTimeHistory);
        templateData.put("averageTime", TestDriverUtils.getAverageTime(f1.tupleTimeHistory));

        try (FileWriter out = new FileWriter(new File("f1-result-final.txt"), false)) {
            template.process(templateData, out);
            out.flush();
        }

        Template template2 = cfg.getTemplate("ftls/f2-result.ftl");

        Map<String, Object> templateData2 = new HashMap<>();
        templateData2.put("totalTuples", f2.getTuple().size());
        templateData2.put("dateTime", LocalDateTime.now().toString());
        templateData2.put("connectedDevices", f2.getConnectedFogNodes().toString());
        templateData2.put("rams", f2.utilizationHistory);
        templateData2.put("tuples", f2.tupleTimeHistory);
        templateData2.put("averageTime", TestDriverUtils.getAverageTime(f2.tupleTimeHistory));


        try (FileWriter out = new FileWriter(new File("f2-result-final.txt"), false)) {
            template2.process(templateData2, out);
            out.flush();
        }

        Template template3 = cfg.getTemplate("ftls/f3-result.ftl");

        Map<String, Object> templateData3 = new HashMap<>();
        templateData3.put("totalTuples", f3.getTuple().size());
        templateData3.put("dateTime", LocalDateTime.now().toString());
        templateData3.put("connectedDevices", f3.getConnectedFogNodes().toString());
        templateData3.put("rams", f3.utilizationHistory);
        templateData3.put("tuples", f3.tupleTimeHistory);
        templateData3.put("averageTime", TestDriverUtils.getAverageTime(f3.tupleTimeHistory));

        try (FileWriter out = new FileWriter(new File("f3-result-final.txt"), false)) {
            template3.process(templateData3, out);
            out.flush();
        }

        Template template4 = cfg.getTemplate("ftls/f4-result.ftl");

        Map<String, Object> templateData4 = new HashMap<>();
        templateData4.put("totalTuples", f4.getTuple().size());
        templateData4.put("dateTime", LocalDateTime.now().toString());
        templateData4.put("connectedDevices", f4.getConnectedFogNodes().toString());
        templateData4.put("rams", f4.utilizationHistory);
        templateData4.put("tuples", f4.tupleTimeHistory);
        templateData4.put("averageTime", TestDriverUtils.getAverageTime(f4.tupleTimeHistory));

        try (FileWriter out = new FileWriter(new File("f4-result-final.txt"), false)) {
            template4.process(templateData4, out);
            out.flush();
        }

        Template template5 = cfg.getTemplate("ftls/f5-result.ftl");

        Map<String, Object> templateData5 = new HashMap<>();
        templateData5.put("totalTuples", f5.getTuple().size());
        templateData5.put("dateTime", LocalDateTime.now().toString());
        templateData5.put("connectedDevices", f5.getConnectedFogNodes().toString());
        templateData5.put("rams", f5.utilizationHistory);
        templateData5.put("tuples", f5.tupleTimeHistory);
        templateData5.put("averageTime", TestDriverUtils.getAverageTime(f5.tupleTimeHistory));


        try (FileWriter out = new FileWriter(new File("f5-result-final.txt"), false)) {
            template5.process(templateData5, out);
            out.flush();
        }
    }


    public static FogNode createHomogeneousFogNode(int id, String name, int level,double RAM,int downloadBW) {
        FogNode fogNode = new FogNode();
        fogNode.setId(id);
        fogNode.setName(name);
        fogNode.setLevel(level);
        fogNode.setMIPS(1000);
        fogNode.setDownloadBW(downloadBW);
        fogNode.setUplinkBW(100);
        fogNode.setTotalRAM(RAM);

        // SET THIS TO TRUE FOR ENABLING QUANTUM , FALSE FOR DISABLING QUANTUM .
        fogNode.setQuantaEnabled(false);
        return fogNode;
    }

}
