package org.openskope.timeseries;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import joptsimple.BuiltinHelpFormatter;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.PrintStream;
import java.util.Arrays;

@SpringBootApplication
@ComponentScan
public class TimeseriesApp {

    public static VersionInfo versionInfo;

    private OptionSet options = null;
    private static PrintStream errStream;
    private static PrintStream outStream;    

    public static void main(String[] args) {

        ExitCode exitCode;
        
        try {
            exitCode = startServiceForArgs(args);
        } catch (Exception e) {
            e.printStackTrace();
            exitCode = ExitCode.UNCAUGHT_ERROR;
        }

        if (exitCode != ExitCode.SUCCESS) {
            System.exit(exitCode.value());
        }
    }

    public static ExitCode startServiceForArgs(String [] args) throws Exception {
        return startServiceForArgs(args, System.out, System.err);
    }
    
    public static ExitCode startServiceForArgs(String [] args, 
        PrintStream outStream, PrintStream errStream) throws Exception{

            TimeseriesApp.outStream = outStream;
            TimeseriesApp.errStream = errStream;

        versionInfo = VersionInfo.loadVersionInfoFromResource(
            "Timemseries Service", 
            "https://github.com/openskope/timeseries-service.git",
            "git.properties",
            "maven.properties");        

        try {

            OptionParser parser = createOptionsParser();
            OptionSet options;
            
            // parse the command line arguments and options
            try {
                options = parser.parse(args);
            } catch (OptionException exception) {
                throw new Exception(exception.getMessage());
            }

            // print detailed software version info and exit if requested
            if (options.has("v")) {
                errStream.print(versionInfo.versionBanner());
                errStream.print(versionInfo.versionDetails());
                return ExitCode.SUCCESS;
            }

            // print help and exit if requested
            if (options.has("h")) {
                errStream.print(versionInfo.versionBanner());
                // errStream.println(CLI_USAGE_HELP);
                // errStream.println(CLI_COMMAND_HELP);
                parser.printHelpOn(errStream);
                // errStream.println();
                // errStream.println(CLI_CONFIG_HELP);
                // errStream.println(CLI_EXAMPLES_HELP);
                return ExitCode.SUCCESS;
            }

        SpringApplication.run(TimeseriesApp.class, args);
        
        } catch (CliUsageException e) {
//            printToolUsageErrors(e.getMessage());
            return ExitCode.CLI_USAGE_ERROR;
        }

        return ExitCode.SUCCESS;
    }

    private static OptionParser createOptionsParser() throws Exception {
        
        OptionParser parser = new OptionParser() {{
            acceptsAll(Arrays.asList("v", "version"), "Shows version, git, and build details.");
            acceptsAll(Arrays.asList("h", "help"), "Displays this help.");
            acceptsAll(Arrays.asList("server.port"), "Sets PaloeCAR Browser web application service port.");
            acceptsAll(Arrays.asList("raster-data-service.url"), "Sets URL of raster data query service.");
            acceptsAll(Arrays.asList("raster-data-service.data-dir"), "Sets path to directory containing queryable raster data files.");
        }};

        parser.formatHelpWith(new BuiltinHelpFormatter(128, 2));

        return parser;
    }


    public enum ExitCode {
        
        SUCCESS         ( 0),
        UNCAUGHT_ERROR  (-1),
        CLI_USAGE_ERROR (-2);
        
        private int value;
        private ExitCode(int value) { this.value = value; }
        public int value() { return value;}
    }

    public class CliUsageException extends Exception {};
}
