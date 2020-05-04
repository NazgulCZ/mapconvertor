package org.nazgul.mapconvertor;

import org.alternativevision.gpx.GPXParser;
import org.alternativevision.gpx.beans.GPX;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class MapConvertorConsole {
    static double radius = 50; // in meters
    private final HashMap<String, String> args = new HashMap<>();
    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("org.nazgul.mapconvertor");

    String promptForPath(String fileType) {
        System.out.print("Enter " + fileType + " path: ");
        System.out.flush();

        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }

    Boolean canOverwriteFile(Path path) {
        System.out.print("File " + path.toString() + " already exists. Overwrite [y/N]? ");

        Scanner in = new Scanner(System.in);
        String choice = in.nextLine().toLowerCase();
        return choice.startsWith("y");
    }

    private void ParseArgs(String[] commandLineArgs)
    {
        for (String s: commandLineArgs) {
            String[] kv = s.split("=", 2);
            args.put(kv[0], kv.length<2 ? null : kv[1]);
        }
    }
    public MapConvertorConsole(String[] args) {
        String s = String.format("Mapconvertor version %s.", Main.version);
        logger.info(s);
        System.out.println(s);
        logger.debug("debug");

        ParseArgs(args);
    }

    Path readPath(String argKey, String fileType) {
        return readPath(argKey, fileType, true, null);
    }

    Path readPath(String argKey, String fileType, boolean inputFile, Path defaultPath) {
        Path path = null;
        boolean correctPath = false;
        boolean fileExists = false;
        boolean argChecked = false;

        while (!(correctPath && (fileExists || !inputFile))) {
            String pathS;
            if (!argChecked) {
                String pathFromArg = args.getOrDefault(argKey, "");
                if (pathFromArg.isBlank()  && defaultPath != null)
                    pathFromArg = defaultPath.toString();
                if (!pathFromArg.isBlank()) {
                    pathS = pathFromArg;
                    System.out.println(fileType + " is " + pathFromArg);
                }
                else
                    pathS = promptForPath(fileType);
                argChecked = true;
            } else
                pathS = promptForPath(fileType);

            // create object of Path
            try {
                correctPath = false;
                path = Paths.get(pathS);
                correctPath = true;
            }
            catch (Exception ex) {
                System.out.println("Invalid path");
            }
            if (correctPath) {
                fileExists = path.toFile().exists();
                if (inputFile && !fileExists)
                    System.out.println("File " + path.toString() + " does not exist.");
                if (!inputFile && fileExists && !canOverwriteFile(path))
                    correctPath = false;
            }
        }

        return path;
    }

    private static Path getDefaultOutputFile() {
        return FileSystems.getDefault().getPath(System.getProperty("user.dir"), "output.gpx");
    }

    public void run() throws Exception {

        Path routeFilePath = readPath("routefile", "route file");
        Path checkpointFilePath = readPath("checkpointfile", "checkpoint file");
        Path outputFilePath = readPath("outputfile", "output file", false, getDefaultOutputFile());
        String argRadius = args.getOrDefault("radius", "50");
        try {
            double newRadius = Double.parseDouble(argRadius);
            if (newRadius > 0)
                radius = newRadius;
        } catch (NumberFormatException e) {
            logger.warn("Error parsing radius value \"" + argRadius + "\", using default value " + radius + ".");
        }

        GPXParser p = new GPXParser();
        GPX gpxRoute = p.parseGPX(new FileInputStream(routeFilePath.toString()));
        GPX gpxCheckpoints = p.parseGPX(new FileInputStream(checkpointFilePath.toString()));

        MapConvertor mapConvertor = new MapConvertor();
        mapConvertor.setRadius(radius);

        GPXEx gpxOutput = mapConvertor.Run(gpxRoute, gpxCheckpoints);

        p.writeGPX(gpxOutput, new FileOutputStream(outputFilePath.toString()));
    }

}
