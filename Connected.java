import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Connected {
    private static final HashMap<String, HashSet<String>> cityMap = new HashMap<String, HashSet<String>>();

    private static void printResult(Boolean success) {
        System.out.println(success ? "yes" : "no");
        System.exit(0);
    }

    private static void addToCityMap(String key, String value) {
        if (cityMap.containsKey(key)) {
            cityMap.get(key).add(value);
        } else {
            cityMap.put(key, new HashSet<String>(Collections.singletonList(value)));
        }
    }

    private static Boolean findCity(String currentCity, String targetCity, HashSet<String> cityPath) {
        if (currentCity.equals(targetCity)) {
            // Connected
            return true;
        }
        if (cityMap.containsKey(currentCity)) {
            cityPath.add(currentCity);
            for (String nextCity : cityMap.get(currentCity)) {
                if (cityPath.contains(nextCity)) {
                    // Already exists in passed cities
                    continue;
                }
                if (findCity(nextCity, targetCity, cityPath)) {
                    // Connected
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        if (!(args.length == 3 && !args[0].isEmpty() && !args[1].isEmpty() && !args[2].isEmpty())) {
            System.out.println("java Connected <filename> <cityname1> <cityname2>");
            System.exit(-1);
        }

        String fileName = args[0].trim().toLowerCase();
        String cityName1 = args[1].trim().toLowerCase();
        String cityName2 = args[2].trim().toLowerCase();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            for(String line; (line = br.readLine()) != null; ) {
                String[] cities = line.split("\\s*,\\s*");
                if (cities.length < 2) {
                    // Skip if no 2 cities on a line
                    continue;
                }

                cities[0] = cities[0].trim().toLowerCase();
                cities[1] = cities[1].trim().toLowerCase();
                if (cities[0].equals(cities[1])) {
                    // Skip if same cities on a line
                    continue;
                }

                if ((cities[0].equals(cityName1) && cities[1].equals(cityName2))
                        || (cities[1].equals(cityName1) && cities[0].equals(cityName2))) {
                    // Quick check if cities exist on same line
                    printResult(true);
                }
                addToCityMap(cities[0], cities[1]);
                addToCityMap(cities[1], cities[0]);
            }

            if (cityMap.containsKey(cityName1) && cityMap.containsKey(cityName2)) {
                // Recursive find only if both cities exist in cityMap
                printResult(findCity(cityName1, cityName2, new HashSet<String>()));
            }

            printResult(false);
        } catch (FileNotFoundException e) {
            System.out.println("File is not found");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("Failed to read file");
            System.exit(-1);
        }
    }
}
