package org.nazgul.mapconvertor;

import org.alternativevision.gpx.beans.*;
import java.util.HashSet;
import java.util.Iterator;

public class MapConvertor {

    private double radius = 50; // in meters
    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("org.nazgul.mapconvertor");

    public void setRadius(double radius) {
        this.radius = radius;
    }

//    double calcDistance(Waypoint wp1, Waypoint wp2) {
//        double deltaLat = Math.toRadians(wp2.getLatitude() - wp1.getLatitude());
//        double deltaLong = Math.toRadians(wp2.getLongitude() - wp1.getLongitude());
//        double a = Math.pow(Math.sin(deltaLat / 2), 2) + Math.cos(Math.toRadians(wp1.getLatitude())) * Math.cos(Math.toRadians(wp1.getLatitude())) * Math.pow(Math.sin(deltaLong / 2), 2);
//        double greatCircleDistance = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        return GPXEx.earthRadius * greatCircleDistance;
//    }

    Waypoint findNearestWaypoint (Waypoint checkpoint, Track track) {
        Double minDistance = null;
        Waypoint nearest = null;
        for (Waypoint waypoint : track.getTrackPoints()) {
            double distance = waypoint.distanceTo(checkpoint);
            if (nearest == null || minDistance > distance) {
                nearest = waypoint;
                minDistance = distance;
            }
        }

        return nearest;
    }

    GPXEx getGpxOutput(GPX gpxRoute) {
        return new GPXEx(gpxRoute);
    }

    void addCheckpoints(GPX gpxRoute, GPX gpxCheckpoints, GPXEx gpxOutput) {
        HashSet<Track> tracks = gpxRoute.getTracks();
        if (tracks.size() == 0) {
            logger.info("No tracks found, exiting.");
            return;
        }
        if (tracks.size() > 1) {
            logger.info("More than one track found, using the first one.");
            return;
        }
        Iterator<Track> it = tracks.iterator();
        Track track = it.next();

        HashSet<Waypoint> checkpoints = gpxCheckpoints.getWaypoints();
        for (Waypoint checkpoint : checkpoints) {
            System.out.println("Checkpoint " + checkpoint.getName());
            Waypoint nearest = findNearestWaypoint(checkpoint, track);
            if (nearest != null) {
                double distance;
                distance = checkpoint.distanceTo(nearest) * 1000;
                String s;
                s = String.format("Nearest point %s, distance %.2f m.", nearest.getName(), distance);
                logger.info(s);

                gpxOutput.addCheckpoint(nearest, radius);

            } else {
                logger.warn("Nearest point not found.");
            }
        }
    }

    public GPXEx Run(GPX gpxRoute, GPX gpxCheckpoints) /*throws IOException, FileNotFoundException, ParserConfigurationException, TransformerException, SAXException*/ {
        GPXEx gpxOutput = getGpxOutput(gpxRoute);

        addCheckpoints(gpxRoute, gpxCheckpoints, gpxOutput);

        return gpxOutput;
    }

}
