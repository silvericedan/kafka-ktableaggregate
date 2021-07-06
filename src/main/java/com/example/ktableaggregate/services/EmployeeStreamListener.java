package com.example.ktableaggregate.services;


import com.example.ktableaggregate.bindings.EmployeeListenerBinding;
import com.example.ktableaggregate.model.Employee;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@EnableBinding(EmployeeListenerBinding.class)
public class EmployeeStreamListener {

    @Autowired
    RecordBuilder recordBuilder;

    //We take the data as a KStream and convert it to a KTable because
    //the input data does NOT come with a key, and a KTable must have a key
    @StreamListener("employee-input-channel")
    public void process(KStream<String, Employee> input) {

        //1. we use map to make a new KStream, that has employee id as a key
        //2. use the peek method to print it to the log
        //3. transform to KTable
        //4. we group by department name, because we want to calculate average salary by department
        //5. the aggregate() method takes 3 lambda arguments, one initializer and two aggregators
        input.map((k, v) -> KeyValue.pair(v.getId(), v))
                .peek((k, v) -> log.info("Key = " + k + " Value = " + v))
                .toTable()
                .groupBy((k, v) -> KeyValue.pair(v.getDepartment(), v))
                .aggregate(
                        () -> recordBuilder.init(),
                        (k, v, aggV) -> recordBuilder.adder(v, aggV),
                        (k, v, aggV) -> recordBuilder.subtractor(v, aggV)
                ).toStream()
                .foreach((k, v) -> log.info("Key = " + k + " Value = " + v));
    }
}
