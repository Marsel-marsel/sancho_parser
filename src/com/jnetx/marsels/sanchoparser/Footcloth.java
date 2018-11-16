package com.jnetx.marsels.sanchoparser;

import java.io.File;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Footcloth implements Comparable<Footcloth>{
    private Timestamp ts;
    private ArrayList<String> content;
    private File source;

    public Footcloth(Timestamp ts, File source) {
        this.ts = ts;
        this.source = source;
    }

    public Footcloth(Timestamp ts, File source, String content){
        this.ts = ts;
        this.source = source;
        this.content = new ArrayList<>();
        this.content.add(content);
    }

    public void addContent(String str){
        this.content.add(str);
    }

    public String getHeader(){
        return this.content.get(0);
    }

    public Timestamp getTs(){
        return this.ts;
    }

    public File getSource() {
        return source;
    }

    @Override
    public int compareTo(Footcloth fc) {
        return this.ts.compareTo(fc.getTs());
    }

    public void print(PrintStream ps){
        for (String line : content){
            ps.println(line);
        }
    }
}
