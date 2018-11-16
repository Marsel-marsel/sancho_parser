package com.jnetx.marsels.sanchoparser;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class DiameterMessageParserUnitTests {
    DiameterMessageParser clientLogTestingParser;
    DiameterMessageParser serverLogTestingParser;
    String clientLogFilePath = "etc/logs/client/system.log";
    String serverLogFilePath = "etc/logs/PCRF1/system.log";

    @Before
    public void setUp(){
        try {
            clientLogTestingParser = new DiameterMessageParser(clientLogFilePath);
            serverLogTestingParser = new DiameterMessageParser(serverLogFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown(){
        clientLogTestingParser.close();
        serverLogTestingParser.close();
    }

    @Test
    public void PARSER_SHOULD_RETURN_NOT_EMPTY_ARRAY(){
        int size;
        size = clientLogTestingParser.parse().size();
        Assert.assertTrue("Result array size: " + size,size != 0);

        size = serverLogTestingParser.parse().size();
        Assert.assertTrue("Result array size: " + size,size != 0);
    }

    @Test
    public void PARSER_SHOULD_FILL_FOOTCLOTHES_WITH_CONTENT(){
        ArrayList<Footcloth> result = clientLogTestingParser.parse();
        System.out.println(result.size());
        for (Footcloth curFC : result){
            Assert.assertTrue(curFC.getContentSize()>1);
        }
    }

    @Test
    public void ITERATOR_HAS_NEXT_METHOD_SHOULD_NOT_ROLL_READER() throws FileNotFoundException{
        AbstractSanchoParser parser1 = new DiameterMessageParser(clientLogFilePath);
        AbstractSanchoParser parser2 = new DiameterMessageParser(clientLogFilePath);

        parser1.hasNext();
        Footcloth fc1 = parser1.next();
        Footcloth fc2 = parser2.next();

        parser1.close();
        parser2.close();

        Assert.assertTrue(fc1.compareTo(fc2) == 0 && fc1.getHeader().equals(fc2.getHeader()));
    }
}
