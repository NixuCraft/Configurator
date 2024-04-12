package me.nixuge.configurator;

import org.bukkit.Location;
import org.bukkit.World;

import me.nixuge.configurator.error.ConfigParseException;
import me.nixuge.nixutils.maths.Area;
import me.nixuge.nixutils.maths.XYZ;

public class ConfigStringParsing {
    public static Location getLocationFromString(String str, World world) {
        // Parses X Y Z alone
        if (!str.contains(", "))
            return getLocationFromStringNoYawPitch(str, world);
        
        // Parses X Y Z, yaw pitch
        String[] xyz_yp = str.split(", ");
        if (xyz_yp.length != 2) {
            throw new ConfigParseException("ERROR TO WRITE");
        } 

        Location loc = getLocationFromStringNoYawPitch(xyz_yp[0], world);
        int[] yawPitch = getYawPitch(xyz_yp[1]);
        loc.setYaw(yawPitch[0]);
        loc.setPitch(yawPitch[1]);
        return loc;
    }
    public static Location getLocationFromStringNoYawPitch(String str, World world) {
        // Parses X Y Z alone
        String[] parts = str.split(" ");
        if (parts.length < 3) {
            throw new ConfigParseException("ERROR TO WRITE");
        }

        double[] xyz = new double[3];

        for (int i = 0; i < 3; i++) {
            String part = parts[i];
            try {
                xyz[i] = Double.parseDouble(part);
            } catch (NumberFormatException e) {
                throw new ConfigParseException("ERROR TO WRITE");
            }
        }

        return new Location(world, xyz[0], xyz[1], xyz[2]);
    }
    public static int[] getYawPitch(String str) {
        String[] raw_yp = str.split(" ");

        int[] yp = new int[2];

        for (int i = 0; i < 2; i++) {
            String part = raw_yp[i];
            try {
                yp[i] = Integer.parseInt(part);
            } catch (NumberFormatException e) {
                throw new ConfigParseException("ERROR TO WRITE");
            }
        }
        return yp;
    }

    public static Area getAreaFromString(String str) {
        // Parses 2*X Y Z to make an area
        String[] corners = str.split(" \\| ");
        if (corners.length != 2) {
            throw new ConfigParseException(String.format("Area string %s doesn't have 2 parts when splitted at ' | '", str));
        }

        return new Area(getXYZFromString(corners[0]), getXYZFromString(corners[1]));
    }
    
    public static XYZ getXYZFromString(String str) {
        // Parses X Y Z alone
        String[] parts = str.split(" ");
        if (parts.length < 3) {
            throw new ConfigParseException(String.format("XYZ string %s has less than 3 parts when splitted every space.", str));
        }

        int[] xyz = new int[3];

        for (int i = 0; i < 3; i++) {
            String part = parts[i];
            try {
                xyz[i] = Integer.parseInt(part);
            } catch (NumberFormatException e) {
                throw new ConfigParseException(String.format("'%s' couldn't be parsed to an integer in XYZ string %s.", xyz[i], str));
            }
        }

        return new XYZ(xyz[0], xyz[1], xyz[2]);
    }
}
