import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.stream.Collectors;


class Main {

    public static void main(String[] args) throws ParseException {
        Options options = new Options();
        Option add = new Option("add", false, "add record");
        add.setArgs(2);
        options.addOption(add);
        Option rm = new Option("rm", false, "remove record");
        rm.setArgs(1);
        options.addOption(rm);
        Option show = new Option("show", false, "show records");
        options.addOption(show);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        Notebook notebook = new Notebook();

        if (cmd.hasOption("add")) {
            var arguments = cmd.getOptionValues("add");
            notebook.add(new NotebookRecord(arguments[0], arguments[1]));
        } else if (cmd.hasOption("rm")) {
            notebook.remove(cmd.getOptionValue("rm"));
        } else if (cmd.hasOption("show")) {
            String[] arguments = cmd.getArgs();
            if (arguments.length == 0) {
                notebook.show();
            } else {
                String[] keywords = Arrays.copyOfRange(arguments, 2, arguments.length);
                keywords = Arrays.stream(keywords).map(String::toLowerCase)
                        .collect(Collectors.toList())
                        .toArray(new String[0]);
                notebook.show(arguments[0], arguments[1], keywords);
            }
        }
    }
}
