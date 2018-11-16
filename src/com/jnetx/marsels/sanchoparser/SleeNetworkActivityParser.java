package com.jnetx.marsels.sanchoparser;

import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SleeNetworkActivityParser extends AbstractSanchoParser {
    private static final Pattern NETWORK_ACTIVITY = Pattern.compile(".*Network activity.*");


    public SleeNetworkActivityParser(String filePath) throws FileNotFoundException {
        super(filePath);
    }

    @Override
    public boolean matches(Footcloth fc) {
        Matcher networkActivityMatcher = NETWORK_ACTIVITY.matcher(fc.getHeader());
        return networkActivityMatcher.matches();
    }

    @Override
    public void fillContent(Footcloth fc) {
        //empty because Footcloth doesn't need to have content. It consist one line only
    }
}
