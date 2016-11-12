package space.hideaway;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import space.hideaway.model.Data;
import space.hideaway.model.Device;
import space.hideaway.services.DataServiceImplementation;
import space.hideaway.services.UserServiceImplementation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by dough on 11/8/2016.
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CoCoTempApplication.class})
public class InsertTests {

    Logger logger = Logger.getLogger(getClass());

    @Autowired
    private DataServiceImplementation dataServiceImplementation;

    @Autowired
    private UserServiceImplementation userServiceImplementation;

    private CSVWriter csvWriter;

    @Test
    public void timeToInsert() throws UserNotFoundException {

        List<Data> dataList = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            for (int j = 1; j <= i; j++) {
                dataList.add(createSampleData());
            }
            long start = System.nanoTime();
            dataServiceImplementation.batchSave(dataList);
            long end = System.nanoTime();
            long total = end - start;
            logTime(i, total);
            dataList.clear();
        }
        try {
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logTime(int rows, long total) {
        logger.info("Rows: " + rows + " " + "Total: " + total);
        if (csvWriter == null) {
            try {
                csvWriter = new CSVWriter(new FileWriter("insert_test_50.csv"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        csvWriter.writeNext(new String[]{String.valueOf(rows), String.valueOf(total)});
    }

    private Data createSampleData() throws UserNotFoundException {
        UUID uuid = UUID.randomUUID();
        Date date = new Date(System.currentTimeMillis());
        Set<Device> deviceSet = userServiceImplementation.findByUsername("doughepi").getDeviceSet();
        List<Device> deviceList = new ArrayList<>(deviceSet);

        if (deviceList.isEmpty()) {
            Assert.fail("The test user has no devices!");
        }

        double temperature = new Random().nextDouble();
        return new Data(uuid, deviceList.get(0).getId(), date, temperature);
    }


}