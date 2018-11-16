package com.jnetx.marsels.sanchoparser;

import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractSanchoParser implements Iterator<Footcloth> {
    File file;
    BufferedReader logfileReader;
    long delta = 6000;
    String targetTimestamp = "";

    public AbstractSanchoParser(String filePath) throws FileNotFoundException {
        file = new File(filePath);
        logfileReader = new BufferedReader(new FileReader(file));
    }

    public static AbstractSanchoParser createParser(LogfileType type, String filePath){
        try {
            switch (type){
                case EMULATOR:
                    return new DiameterMessageParser(filePath);
                case SLEE:
                    return new SleeNetworkActivityParser(filePath);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Footcloth> parse(){
        rollToClosestTimestamp();
        ArrayList<Footcloth> result = new ArrayList<>();
        long timeBetween;
        while (this.hasNext()){
            Footcloth currentFootcloth = this.next();

            if (result.size() == 0)
                timeBetween = Long.MIN_VALUE;
            else
                timeBetween = currentFootcloth.getTime()-result.get(result.size()-1).getTime();

            if (this.matches(currentFootcloth) && timeBetween<=delta) {
                fillContent(currentFootcloth);
                result.add(currentFootcloth);
            }
        }
        return result;
    }

    public abstract boolean matches(Footcloth fc);

    public abstract void fillContent(Footcloth fc);


    @Override
    public boolean hasNext() {
        Footcloth next = null;
        try {
            next = null;
            logfileReader.mark(999999);
            next = readFootcloth();
            logfileReader.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (next == null)
            return false;
        else
            return true;
    }

    @Override
    public Footcloth next() {
    Footcloth nextFootcloth = null;
    try {
        nextFootcloth = readFootcloth();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return nextFootcloth;
    }

    @Override
    public void remove() {

    }

    protected static Timestamp extractTimestamp(String logline) {
        Pattern TIMESTAMP_PATTERN = Pattern.compile("^(?<timestamp>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}).*");
        DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("YYYY'-'MM'-'dd HH':'mm':'ss','SSS");

        Matcher timestampMatcher = TIMESTAMP_PATTERN.matcher(logline);
        if (timestampMatcher.matches()) {
            try {
                Date timestampDate = TIMESTAMP_FORMAT.parse(timestampMatcher.group("timestamp"));
                return new Timestamp(timestampDate.getTime());
            } catch (ParseException e) {
//                System.err.println("Can't extract timestamp from: " + logline);
//                e.printStackTrace();
            }
        }
        return null;
    }

    private void rollToClosestTimestamp (){
        Footcloth targetFootcloth = new Footcloth(extractTimestamp(targetTimestamp));
        Footcloth currentFootcloth;
        while(this.hasNext()){
            currentFootcloth = this.next();
            if (currentFootcloth.compareTo(targetFootcloth)!=-1)
                break;
        }
    }

    protected Footcloth readFootcloth(){
        String logline;
        while((logline = this.readLine()) != null){
            Timestamp currentLineTimestamp = extractTimestamp(logline);
            if(currentLineTimestamp != null){
                return new Footcloth(currentLineTimestamp, this.file, logline);
            }
        }
        return null;
    }

    protected String readLine(){
        try {
            return logfileReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close(){
        try {
            logfileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDelta(long delta) {
        this.delta = delta;
    }

    public void setTargetTimestamp(String targetTimestamp) {
        this.targetTimestamp = targetTimestamp;
    }

    public String getFilepath(){
        return file.getAbsolutePath();
    }
}


