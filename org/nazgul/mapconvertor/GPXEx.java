package org.nazgul.mapconvertor;


import org.alternativevision.gpx.beans.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;


class WaypointEx extends Waypoint {
    int index = -1;
    public int getIndex () {
        return index;
    }
    public void setIndex (int value) {
        index = value;
    }

    public WaypointEx(Waypoint source, int index) {
        super();
        setLatitude(source.getLatitude());
        setLongitude(source.getLongitude());
        setElevation(source.getElevation());
        setName(source.getName());
        setIndex(index);
    }

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Waypoint or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Waypoint)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Waypoint w = (Waypoint) o;

        // Compare the data members and return accordingly
        return equals(this, w);
    }

    public static boolean equals(Waypoint wp1, Waypoint wp2) {
        // TODO: compare other members
        return Double.compare(wp1.getLatitude(), wp2.getLatitude()) == 0
                && Double.compare(wp1.getLongitude(), wp2.getLongitude()) == 0;

    }
}

public class GPXEx extends GPX {
    public static final double earthRadius = 6378.137;

    Track getTrack()
    {
        HashSet<Track> tracks = getTracks();
        Iterator<Track> it = tracks.iterator();
        if (it.hasNext()) return it.next();
        else return null;
    }

    GPXEx(GPX gpxSource) {
        super();
        if (gpxSource == null) return;
        HashSet<Track> tracksSource = gpxSource.getTracks();
        if (tracksSource == null || tracksSource.size() == 0) return;
        Track trackSource = tracksSource.iterator().next();
        ArrayList<Waypoint> trackPointsSource = trackSource.getTrackPoints();

        HashSet<Track> tracks = new HashSet<Track>();
        Track track = new Track();
        ArrayList<Waypoint> trackPoints = new ArrayList<Waypoint>();
        int index = 0;
        for (Waypoint trackPointSource : trackPointsSource)
        {
            WaypointEx trackPoint = new WaypointEx(trackPointSource, index++);
            trackPoints.add(trackPoint);
        }

        track.setTrackPoints(trackPoints);
        tracks.add(track);
        setTracks(tracks);
    }

    int findWaypoint(Waypoint point)
    {
        Track track = getTrack();
        if (track == null)
            return -1;

        ArrayList<Waypoint> trackPoints = track.getTrackPoints();
        int index = 0;
        for (Waypoint trackpoint : trackPoints) {
            if (WaypointEx.equals(trackpoint, point))
                return index;
            index++;
        }
        return -1;
    }

    // bearing is in radians, distance is in meters
    public static Waypoint movePoint(Waypoint point, double bearing, double distance)
    {
        double lat = Math.toRadians(point.getLatitude());
        double lon = Math.toRadians(point.getLongitude());

        double distRadians = distance / (earthRadius * 1000);

        double newLat = Math.asin(Math.sin(lat) * Math.cos(distRadians) + Math.cos(lat) * Math.sin(distRadians) * Math.cos(bearing));
        double newLon = lon + Math.atan2(Math.sin(bearing) * Math.sin(distRadians) * Math.cos(lat), Math.cos(distRadians) - Math.sin(lat) * Math.sin(newLat));

        Waypoint waypoint = new Waypoint();
        waypoint.setLatitude(Math.toDegrees(newLat));
        waypoint.setLongitude(Math.toDegrees(newLon));
        return waypoint;
    }

    void addPoint(ArrayList<Waypoint> waypoints, Waypoint sourcePoint, double bearing, double radius, int index) {
        Waypoint newPoint = movePoint(sourcePoint, Math.PI * bearing, radius);
        newPoint.setName("Added manually");
        waypoints.add(index, newPoint);
    }

    public boolean addCheckpoint(Waypoint nearestPoint, double radius) {
        int index = findWaypoint(nearestPoint);

        if (index < 0)
            return false;

        ArrayList<Waypoint> waypoints  = getTrack().getTrackPoints();

        addPoint(waypoints, nearestPoint, 0.0, radius, ++index);
        addPoint(waypoints, nearestPoint, 0.5, radius, ++index);
        addPoint(waypoints, nearestPoint, 1.0, radius, ++index);
        addPoint(waypoints, nearestPoint, 1.5, radius, ++index);
        addPoint(waypoints, nearestPoint, 0.0, radius, ++index);

        WaypointEx originalPoint = new WaypointEx(nearestPoint, ++index);
        waypoints.add(index, originalPoint);

        return true;
    }

}
