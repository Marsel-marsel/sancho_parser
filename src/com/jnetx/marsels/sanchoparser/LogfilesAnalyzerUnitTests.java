package com.jnetx.marsels.sanchoparser;

import com.jnetx.marsels.LogfilesAnalyzer;
import org.junit.Test;

public class LogfilesAnalyzerUnitTests {
    String clientLogPath = "etc/logs/client/system.log";
    String serverLogpath = "etc/logs/PCRF1/system.log";
    String sleeLogPath = "etc/logs/slee/system.log";

    @Test
    public void LOGFILEANALYZER_SHOULD_PRINT_BUNCH_OF_MESSAGES() throws Exception{
        AbstractSanchoParser serverLogParser = new DiameterMessageParser(serverLogpath);
        AbstractSanchoParser clientLogParser = new DiameterMessageParser(clientLogPath);
        AbstractSanchoParser sleeLogParser = new SleeNetworkActivityParser(sleeLogPath);

        LogfilesAnalyzer analyzer = new LogfilesAnalyzer();

        analyzer.addParser(serverLogParser);
        analyzer.addParser(clientLogParser);
        analyzer.addParser(sleeLogParser);

        analyzer.setDelta(6000);
//        analyzer.setTargetTimestamp("2018-11-14 15:42:41,313");
        analyzer.setTargetTimestamp("2018-11-17 12:56:54,632");
        analyzer.printResult();
    }
}
