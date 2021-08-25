package com.annakhuseinova;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class ClaimManagement {

    public static void main(String[] args) throws Exception{
        InitialContext initialContext = new InitialContext();
        Queue claimQueue = (Queue) initialContext.lookup("queue/claimQueue");
        try(ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            JMSContext jmsContext = connectionFactory.createContext()){
            JMSProducer producer = jmsContext.createProducer();
            JMSConsumer consumer = jmsContext.createConsumer(claimQueue, "hospitalId=1");
            ObjectMessage objectMessage = jmsContext.createObjectMessage();
            objectMessage.setIntProperty("hospitalId", 1);
            Claim claim = new Claim();
            claim.setHospitalId(1);
            claim.setClaimAmount(1000);
            claim.setDoctorName("John");
            claim.setDoctorType("gyna");
            claim.setInsuranceProvider("blue cross");
            objectMessage.setObject(claim);
            producer.send(claimQueue, objectMessage);
            Claim receivedClaim = consumer.receiveBody(Claim.class);
            System.out.println(receivedClaim);

        }
    }
}
