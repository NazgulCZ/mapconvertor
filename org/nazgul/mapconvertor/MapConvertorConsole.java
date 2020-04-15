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
    private HashMap<String, String> args = new HashMap<String, String>();
    private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("org.nazgul.mapconvertor");

    String promptForPath(String fileType) {
        System.out.print("Enter " + fileType + " path: ");
        System.out.flush();

        Scanner in = new Scanner(System.in);
        String pathS = in.nextLine();
        return pathS;
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


    Path readPath(String argKey, String fileType, boolean checkFileExists) {
        return readPath(argKey, fileType, checkFileExists, null);
    }

    Path readPath(String argKey, String fileType, boolean checkFileExists, Path defaultPath) {
        Path path = null;
        Boolean correctPath = false;
        Boolean fileExists = false;
        Boolean argChecked = false;

        while (!(correctPath && (fileExists || !checkFileExists))) {
            String pathS;
            if (!argChecked) {
                String pathFromArg = args.getOrDefault(argKey, "");
                if (pathFromArg == "" && defaultPath != null)
                    pathFromArg = defaultPath.toString();
                if (pathFromArg != "") {
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
            if (correctPath && checkFileExists) {
                fileExists = path.toFile().exists();
                if (!fileExists)
                    System.out.println("File " + path.toString() + " does not exist.");
            }
            // TODO if checkFileExists == false, then check if file exists and let user confirm overwriting
        }

        // call getFileName() and get FileName path object
        Path fileName = path.getFileName();

        // print FileName
        return path;
    }

    private static Path getDefaultOutputFile() {
        return FileSystems.getDefault().getPath(System.getProperty("user.dir"), "output.gpx");
    }

    public void run() throws Exception {

        Path routeFilePath = readPath("routefile", "route file", true);
        Path checkpointFilePath = readPath("checkpointfile", "checkpoint file", true);
        Path outputFilePath = readPath("outputfile", "output file", false, getDefaultOutputFile());
        try {
            double newRadius = Double.parseDouble(args.getOrDefault("radius", "50"));
            if (newRadius > 0)
                radius = newRadius;
        } catch (NumberFormatException e) {

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
