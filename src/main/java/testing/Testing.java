package testing;

import logical.Tuple;
import physical.FogNode;
import physical.Sensor;

import java.util.List;

public class Testing {
    public static void main(String[] args) throws InterruptedException {
        Tuple t1 = new Tuple(1,"source1","mera1");
        Tuple t2 = new Tuple(2,"source1","mera2");
        Tuple t3 = new Tuple(3,"source1","mera3");
        Tuple t4 = new Tuple(4,"source1","mera4");
        Tuple t5 = new Tuple(5,"source1","mera5");
        Tuple t6 = new Tuple(6,"source1","mera1");
        Tuple t7 = new Tuple(7,"source1","mera2");
        Tuple t8 = new Tuple(8,"source1","mera3");
        Tuple t9 = new Tuple(9,"source1","mera4");
        Tuple t10 = new Tuple(10,"source1","mera5");
        Tuple t11 = new Tuple(11,"source1","mera1");
        Tuple t12 = new Tuple(12,"source1","mera2");
        Tuple t13 = new Tuple(13,"source1","mera3");
        Tuple t14 = new Tuple(14,"source1","mera4");
        Tuple t15 = new Tuple(15,"source1","mera5");
        List<Tuple> tuples = List.of(t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15);

        // low level fognode
        FogNode lowLevel1 = new FogNode();
        lowLevel1.setId(1);
        lowLevel1.setName("LOW-LEVEL-FOG-1");
        lowLevel1.setMIPS(1000);
        lowLevel1.setDownloadBW(10);
        lowLevel1.setUplinkBW(10);
        lowLevel1.setLevel(0);
        lowLevel1.setTotalRAM(100);

        FogNode lowLevel2 = new FogNode();
        lowLevel2.setId(1);
        lowLevel2.setName("LOW-LEVEL-FOG-2");
        lowLevel2.setMIPS(1000);
        lowLevel2.setDownloadBW(10);
        lowLevel2.setUplinkBW(10);
        lowLevel2.setLevel(0);
        lowLevel2.setTotalRAM(100);

        // high level fognode

        FogNode highLevel1 = new FogNode();
        highLevel1.setId(1);
        highLevel1.setName("HIGH-LEVEL-FOG-1");
        highLevel1.setMIPS(1000);
        highLevel1.setDownloadBW(10);
        highLevel1.setUplinkBW(10);
        highLevel1.setLevel(1);
        highLevel1.setTotalRAM(1000);

        FogNode highLevel2 = new FogNode();
        highLevel2.setId(2);
        highLevel2.setName("HIGH-LEVEL-FOG-2");
        highLevel2.setMIPS(7000);
        highLevel2.setDownloadBW(10);
        highLevel2.setUplinkBW(10);
        highLevel2.setLevel(1);
        highLevel2.setTotalRAM(1000);


        Sensor saraSenseItPlease = new Sensor();
        saraSenseItPlease.sensorName = "Sensor A";
        saraSenseItPlease.tupleType = "howdy";
        saraSenseItPlease.connectTo(lowLevel1);
        lowLevel1.connectTo(highLevel1);
        for(Tuple tuple : tuples){
            saraSenseItPlease.sendTuple(tuple,lowLevel1);
        }

    }
}
