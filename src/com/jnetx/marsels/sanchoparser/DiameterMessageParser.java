package com.jnetx.marsels.sanchoparser;

import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiameterMessageParser extends AbstractSanchoParser{
    private static final Pattern DIAMETER_MESSAGE_PROCESSED_PATTERN = Pattern.compile(".*(Received .+? DiameterMessage|Creating message ...|INFO  MessageSender).*");

    public DiameterMessageParser(String filePath) throws FileNotFoundException {
        super(filePath);
    }

    @Override
    public boolean matches(Footcloth fc) {
        Matcher diameterMessageMatcher = DIAMETER_MESSAGE_PROCESSED_PATTERN.matcher(fc.getHeader());
        return diameterMessageMatcher.matches();
    }

    @Override
    public void fillContent(Footcloth fc) {
        fc.addContent(this.readLine());
        String content;
        while (extractTimestamp( content = this.readLine()) == null)
            fc.addContent(content);
    }
}
