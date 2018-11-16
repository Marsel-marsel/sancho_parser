package com.jnetx.marsels;

import com.jnetx.marsels.sanchoparser.AbstractSanchoParser;
import com.jnetx.marsels.sanchoparser.Footcloth;
import java.util.ArrayList;
import java.util.Collections;

public class LogfilesAnalyzer {
    ArrayList<AbstractSanchoParser> parsers = new ArrayList<>();

    public void addParser(AbstractSanchoParser parser){
        parsers.add(parser);
    }

    public void setTargetTimestamp(String targetTimestamp){
        for (AbstractSanchoParser parser : parsers) {
            parser.setTargetTimestamp(targetTimestamp);
        }
    }


    public void setDelta(long delta){
        for (AbstractSanchoParser parser: parsers ) {
            parser.setDelta(delta);
        }
    }

    public void printResult(){
        System.out.println("Statistics:");
        ArrayList<Footcloth> result = new ArrayList<>();
        for (AbstractSanchoParser parser: parsers) {
            ArrayList<Footcloth> subResult = parser.parse();
            System.out.println("\tFilename: " + parser.getFilepath());
            System.out.println("\tFound messages: " + subResult.size());
            result.addAll(subResult);
        }

        System.out.println();
        System.out.println("Messages in sequential order:");
        Collections.sort(result);
        for (Footcloth fc : result) {
            fc.print(System.out);
        }
    }
}
