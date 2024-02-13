package com.example.ticketmaster.extermal;

/**
 * @author lintingxuan
 * @create 2024-02-13 10:28 AM
 */
public class GeoHash {
    private static final int MAX_PRECISION = 12; // Maximum precision for a geohash

    private static final String BASE_32 = "0123456789bcdefghjkmnpqrstuvwxyz";

    // Private constructor to prevent instantiation
    private GeoHash() {
    }

    public static String encode(double latitude, double longitude, int precision) {
        if (precision < 1 || precision > MAX_PRECISION) {
            throw new IllegalArgumentException("Precision must be between 1 and " + MAX_PRECISION);
        }

        double[] latRange = {-90.0, 90.0};
        double[] lonRange = {-180.0, 180.0};

        StringBuilder geohash = new StringBuilder();

        for (int i = 0; i < precision * 5; i++) {
            int combinedBits = 0;

            for (int j = 0; j < 5; j++) {
                combinedBits <<= 1;

                if (i % 2 == 0) {
                    double midLon = (lonRange[0] + lonRange[1]) / 2.0;
                    if (longitude > midLon) {
                        combinedBits |= 0x01;
                        lonRange[0] = midLon;
                    } else {
                        lonRange[1] = midLon;
                    }
                } else {
                    double midLat = (latRange[0] + latRange[1]) / 2.0;
                    if (latitude > midLat) {
                        combinedBits |= 0x01;
                        latRange[0] = midLat;
                    } else {
                        latRange[1] = midLat;
                    }
                }
            }

            geohash.append(BASE_32.charAt(combinedBits));
        }

        return geohash.toString();
    }

    public static void main(String[] args) {
        // Example usage
        double latitude = 37.7749; // San Francisco latitude
        double longitude = -122.4194; // San Francisco longitude
        int precision = 8; // Geohash precision

        String geohash = GeoHash.encode(latitude, longitude, precision);
        System.out.println("Geohash: " + geohash);
    }
}
