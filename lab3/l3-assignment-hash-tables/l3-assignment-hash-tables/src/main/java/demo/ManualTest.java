package demo;

import utils.*;

import java.util.Locale;

import static utils.HashMap.DEFAULT_INITIAL_CAPACITY;
import static utils.HashMap.DEFAULT_LOAD_FACTOR;

public class ManualTest {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US); // Unify number formats
        executeTest();
    }

    public static void executeTest() {
        Car car1 = new Car("Renault", "Laguna", 1997, 50000, 1700);
        Car car2 = new Car("Renault", "Megane", 2001, 20000, 3500);
        Car car3 = new Car("Toyota", "Corolla", 2001, 20000, 8500.8);
        Car car4 = new Car("Renault Laguna 2001 115900 7500");
        Car car5 = new Car.Builder().buildRandom();
        Car car6 = new Car("Honda   Civic  2007  36400 8500.3");
        Car car7 = new Car("Renault Laguna 2001 115900 7500");

        // array of hash map keys
        String[] carsIds = {"TA156", "TA102", "TA178", "TA126", "TA105", "TA106", "TA107", "TA108"};
        // array of hash map values
        Car[] cars = {car1, car2, car3, car4, car5, car6, car7};

        executeCarMapTests(carsIds, cars);
        executeCarMapOaTests(carsIds, cars);
    }

    private static void executeCarMapTests(String[] carsIds, Car[] cars) {
        ParsableMap<String, Car> carsMap = new ParsableHashMap<>(
                String::new,
                Car::new,
                DEFAULT_INITIAL_CAPACITY,
                DEFAULT_LOAD_FACTOR,
                HashManager.HashType.DIVISION
        );

        for (int id = 0; id < cars.length; id++) {
            carsMap.put(carsIds[id], cars[id]);
        }

        Ks.oun("=== HashMap (Separate Chaining) Tests ===");
        Ks.oun("");

        Ks.oun("Distribution of key-value pairs in the hash map:");
        carsMap.println("");
        Ks.oun("Does key exists in the hash map?");
        Ks.oun(carsMap.contains(carsIds[6]));
        Ks.oun(carsMap.contains(carsIds[7]));
        Ks.oun("Distribution of key-value pairs in the hash map. Only keys displayed:");
        carsMap.println("=");

        Ks.oun("Lookup by key:");
        Ks.oun(carsMap.get(carsIds[2]));
        Ks.oun(carsMap.get(carsIds[7]));
        Ks.oun("Print the string of key-value pairs:");
        Ks.ounn(carsMap);

        // ========== TEST: containsValue() ==========
        Ks.oun("--- Testing containsValue() ---");
        Ks.oun("containsValue(car1 - exists): " + carsMap.containsValue(cars[0]));
        Ks.oun("containsValue(car2 - exists): " + carsMap.containsValue(cars[1]));
        Car nonExistentCar = new Car("Fake", "Car", 2020, 1000, 500);
        Ks.oun("containsValue(fake car - not exists): " + carsMap.containsValue(nonExistentCar));
        Ks.oun("");

        // ========== TEST: replace() ==========
        Ks.oun("--- Testing replace() ---");
        Ks.oun("Before replace - get(TA156): " + carsMap.get(carsIds[0]));
        Car newCar = new Car("BMW", "X5", 2022, 5000, 50000);
        boolean replaced = carsMap.replace(carsIds[0], cars[0], newCar);
        Ks.oun("replace(TA156, car1, newCar) - should be true: " + replaced);
        Ks.oun("After replace - get(TA156): " + carsMap.get(carsIds[0]));

        boolean replaceFail = carsMap.replace(carsIds[1], cars[0], newCar); // wrong oldValue
        Ks.oun("replace(TA102, car1, newCar) - wrong oldValue, should be false: " + replaceFail);

        boolean replaceNoKey = carsMap.replace("FAKE_KEY", cars[0], newCar);
        Ks.oun("replace(FAKE_KEY, ...) - key not exists, should be false: " + replaceNoKey);
        Ks.oun("");

        // ========== TEST: remove() ==========
        Ks.oun("--- Testing remove() ---");
        Ks.oun("Size before remove: " + carsMap.size());
        Ks.oun("contains(TA102) before remove: " + carsMap.contains(carsIds[1]));
        Car removed = carsMap.remove(carsIds[1]);
        Ks.oun("remove(TA102) returned: " + removed);
        Ks.oun("contains(TA102) after remove: " + carsMap.contains(carsIds[1]));
        Ks.oun("Size after remove: " + carsMap.size());

        Car removedAgain = carsMap.remove(carsIds[1]);
        Ks.oun("remove(TA102) again - should be null: " + removedAgain);

        Car removeNonExistent = carsMap.remove("FAKE_KEY");
        Ks.oun("remove(FAKE_KEY) - should be null: " + removeNonExistent);
        Ks.oun("");
    }

    private static void executeCarMapOaTests(String[] carsIds, Car[] cars) {
        ParsableMap<String, Car> carsMapOa = new ParsableHashMapOa<>(
                String::new,
                Car::new,
                DEFAULT_INITIAL_CAPACITY,
                DEFAULT_LOAD_FACTOR,
                HashManager.HashType.DIVISION,
                HashMapOa.OpenAddressingType.LINEAR
        );

        for (int id = 0; id < cars.length; id++) {
            carsMapOa.put(carsIds[id], cars[id]);
        }

        Ks.oun("=== HashMapOa (Open Addressing) Tests ===");
        Ks.oun("");

        Ks.oun("Distribution of key-value pairs in the hash map based on the open addressing:");
        carsMapOa.println("");
        Ks.oun("Does key exists in the hash map?");
        Ks.oun(carsMapOa.contains(carsIds[6]));
        Ks.oun(carsMapOa.contains(carsIds[7]));
        Ks.oun("Distribution of key-value pairs in the hash map based on the open addressing. Only keys displayed:");
        carsMapOa.println("=");

        Ks.oun("Lookup by key:");
        Ks.oun(carsMapOa.get(carsIds[2]));
        Ks.oun(carsMapOa.get(carsIds[7]));
        Ks.oun("Print the string of key-value pairs:");
        Ks.ounn(carsMapOa);

        // ========== TEST: containsValue() ==========
        Ks.oun("--- Testing containsValue() ---");
        Ks.oun("containsValue(car1 - exists): " + carsMapOa.containsValue(cars[0]));
        Ks.oun("containsValue(car2 - exists): " + carsMapOa.containsValue(cars[1]));
        Car nonExistentCar = new Car("Fake", "Car", 2020, 1000, 500);
        Ks.oun("containsValue(fake car - not exists): " + carsMapOa.containsValue(nonExistentCar));
        Ks.oun("");

        // ========== TEST: replace() ==========
        Ks.oun("--- Testing replace() ---");
        Ks.oun("Before replace - get(TA156): " + carsMapOa.get(carsIds[0]));
        Car newCar = new Car("BMW", "X5", 2022, 5000, 50000);
        boolean replaced = carsMapOa.replace(carsIds[0], cars[0], newCar);
        Ks.oun("replace(TA156, car1, newCar) - should be true: " + replaced);
        Ks.oun("After replace - get(TA156): " + carsMapOa.get(carsIds[0]));

        boolean replaceFail = carsMapOa.replace(carsIds[1], cars[0], newCar); // wrong oldValue
        Ks.oun("replace(TA102, car1, newCar) - wrong oldValue, should be false: " + replaceFail);

        boolean replaceNoKey = carsMapOa.replace("FAKE_KEY", cars[0], newCar);
        Ks.oun("replace(FAKE_KEY, ...) - key not exists, should be false: " + replaceNoKey);
        Ks.oun("");

        // ========== TEST: remove() ==========
        Ks.oun("--- Testing remove() ---");
        Ks.oun("Size before remove: " + carsMapOa.size());
        Ks.oun("contains(TA102) before remove: " + carsMapOa.contains(carsIds[1]));
        Car removed = carsMapOa.remove(carsIds[1]);
        Ks.oun("remove(TA102) returned: " + removed);
        Ks.oun("contains(TA102) after remove: " + carsMapOa.contains(carsIds[1]));
        Ks.oun("Size after remove: " + carsMapOa.size());

        Car removedAgain = carsMapOa.remove(carsIds[1]);
        Ks.oun("remove(TA102) again - should be null: " + removedAgain);

        Car removeNonExistent = carsMapOa.remove("FAKE_KEY");
        Ks.oun("remove(FAKE_KEY) - should be null: " + removeNonExistent);
        Ks.oun("");

        // ========== TEST: remove() doesn't break search (DELETED marker test) ==========
        Ks.oun("--- Testing DELETED marker (Open Addressing specific) ---");
        Ks.oun("After removing TA102, can we still find items that were inserted after it?");
        Ks.oun("contains(TA178): " + carsMapOa.contains(carsIds[2]));
        Ks.oun("get(TA178): " + carsMapOa.get(carsIds[2]));
        Ks.oun("");
    }
}
