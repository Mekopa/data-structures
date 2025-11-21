package demo;

import utils.Ks;
import utils.Parsable;

import java.time.LocalDate;
import java.util.*;


public final class Car implements Parsable<Car> {

    // common data for all cars (class-wide)
    private static final int minYear = 2000;
    private static final int currentYear = LocalDate.now().getYear();
    private static final double minPrice = 100.0;
    private static final double maxPrice = 333000.0;
    private static final String idCode = "TA";
    private static int serNr = 100;

    private final String carRegNo;

    private String make = "";
    private String model = "";
    private int year = -1;
    private int mileage = -1;
    private double price = -1.0;

    public Car() {
        carRegNo = idCode + (serNr++);    // the original carRegNo is issued
    }

    public Car(String make, String model, int year, int mileage, double price) {
        carRegNo = idCode + (serNr++);    // the original carRegNo is issued
        this.make = make;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.price = price;
        validate();
    }

    public Car(String dataString) {
        carRegNo = idCode + (serNr++);    // the original carRegNo is issued
        this.parse(dataString);
        validate();
    }

    public Car(Builder builder) {
        carRegNo = idCode + (serNr++);    // the original carRegNo is issued
        this.make = builder.make;
        this.model = builder.model;
        this.year = builder.year;
        this.mileage = builder.mileage;
        this.price = builder.price;
        validate();
    }

    public Car create(String dataString) {
        return new Car(dataString);
    }

    private void validate() {
        String errorType = "";
        if (year < minYear || year > currentYear) {
            errorType = "Incorrect year of manufacture, must be ["
                    + minYear + ":" + currentYear + "]";
        }
        if (price < minPrice || price > maxPrice) {
            errorType += " Price outside limits [" + minPrice
                    + ":" + maxPrice + "]";
        }

        if (!errorType.isEmpty()) {
            Ks.ern("The car is generated incorrectly: " + errorType + ". " + this);
        }
    }

    @Override
    public void parse(String dataString) {
        try {   // data delimited by spaces
            Scanner scanner = new Scanner(dataString);
            make = scanner.next();
            model = scanner.next();
            year = scanner.nextInt();
            setMileage(scanner.nextInt());
            setPrice(scanner.nextDouble());
        } catch (InputMismatchException e) {
            Ks.ern("Incorrect data format -> " + dataString);
        } catch (NoSuchElementException e) {
            Ks.ern("Missing data -> " + dataString);
        }
    }

    @Override
    public String toString() {  // papildyta su carRegNr
        return getCarRegNo() + "=" + make + "_" + model + ":" + year + " " + getMileage() + " " + String.format("%4.1f", price);
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCarRegNo() {  //** nauja.
        return carRegNo;
    }

    @Override
    public int compareTo(Car car) {
        return getCarRegNo().compareTo(car.getCarRegNo());
    }

    public static Comparator<Car> byMake = (Car c1, Car c2) -> c1.make.compareTo(c2.make); // first by make and then by model

    public static Comparator<Car> byPrice = (Car c1, Car c2) -> {
        // in ascending order
        if (c1.price < c2.price) {
            return -1;
        }
        if (c1.price > c2.price) {
            return +1;
        }
        return 0;
    };

    public static Comparator<Car> byYearPrice = (Car c1, Car c2) -> {
        // years in descending order, if years are equal, then by price
        if (c1.year > c2.year) {
            return +1;
        }
        if (c1.year < c2.year) {
            return -1;
        }
        if (c1.price > c2.price) {
            return +1;
        }
        if (c1.price < c2.price) {
            return -1;
        }
        return 0;
    };

    // Car class object builder
    public static class Builder {

        private final static Random RANDOM = new Random(1949);  // random generator
        private final static String[][] MODELS = { // an array of available car makes and models
                {"Mazda", "3", "6", "CX-3", "MX-5"},
                {"Ford", "Fiesta", "Kuga", "Focus", "Galaxy", "Mondeo"},
                {"VW", "Golf", "Jetta", "Passat", "Tiguan"},
                {"Honda", "HR-V", "CR-V", "Civic", "Jazz"},
                {"Renault", "Clio", "Megane", "Twingo", "Scenic"},
                {"Peugeot", "208", "308", "508", "Partner"},
                {"Audi", "A3", "A4", "A6", "A8", "Q3", "Q5"}
        };

        private String make = "";
        private String model = "";
        private int year = -1;
        private int mileage = -1;
        private double price = -1.0;

        public Car build() {
            return new Car(this);
        }

        public Car buildRandom() {
            int ma = RANDOM.nextInt(MODELS.length);        // make index  0..
            int mo = RANDOM.nextInt(MODELS[ma].length - 1) + 1;// model index 1..
            return new Car(MODELS[ma][0],
                    MODELS[ma][mo],
                    2000 + RANDOM.nextInt(22),// years between 2000 and 2021
                    6000 + RANDOM.nextInt(222000),// mileage between 6000 and 228000
                    800 + RANDOM.nextDouble() * 88000);// Price between 800 and 88800
        }

        public Builder year(int year) {
            this.year = year;
            return this;
        }

        public Builder make(String make) {
            this.make = make;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder mileage(int mileage) {
            this.mileage = mileage;
            return this;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }
    }
}
