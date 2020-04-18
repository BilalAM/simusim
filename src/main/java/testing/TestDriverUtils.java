package testing;

import logical.Tuple;
import physical.Sensor;

import java.util.ArrayList;
import java.util.List;

public class TestDriverUtils {

    public static List<Tuple> createTuples(int size) {
        List<Tuple> tuples = new ArrayList<>();
        for (int i = 0; i <= size; i++) {
            tuples.add(new Tuple(i, "source-A", "source-B"));
        }
        return tuples;
    }

    public static Sensor createSensor(int id, String name) {
        Sensor sensor = new Sensor();
        sensor.setSensorName(name);
        return sensor;
    }


}
