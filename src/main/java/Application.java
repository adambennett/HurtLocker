import org.apache.commons.io.IOUtils;
import utilities.*;

import java.util.*;

public class Application {

    public String readRawDataToString() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        return IOUtils.toString(Objects.requireNonNull(classLoader.getResourceAsStream("RawData.txt")));
    }

    public void initialize() throws Exception {
        JerksonParse.p(readRawDataToString());
    }

    public static void main(String[] args) throws Exception {
        Application application = new Application();
        application.initialize();
    }
}
