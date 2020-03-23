package utilities;

import interfaces.*;
import models.*;

public class JerksonParse {

    private static final Printer print;
    private static final Parser parse;
    static {
        print = System.out::println;
        parse = new JerksonHandler()::getOutput;
    }

    public static void p(String msg) {
        print.printFunction(parse.parseFunction(msg));
    }


}
