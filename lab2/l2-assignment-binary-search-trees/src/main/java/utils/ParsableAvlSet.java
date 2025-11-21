package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

public class ParsableAvlSet<E extends Parsable<E>> extends AvlSet<E> implements ParsableSortedSet<E> {

    private final Function<String, E> createFunction; // function for creation of set element object

    /**
     * Constructor with parameters createFucntion
     *
     * @param createFunction function used for element creation
     */
    public ParsableAvlSet(Function<String, E> createFunction) {
        super();
        this.createFunction = createFunction;
    }

    /**
     * Constructor with parameters createFucntion and comparator
     *
     * @param createFunction function used for element creation
     * @param c comparator
     */
    public ParsableAvlSet(Function<String, E> createFunction, Comparator<? super E> c) {
        super(c);
        this.createFunction = createFunction;
    }

    /**
     * Creates an element form string and adds it to a set
     *
     * @param dataString
     */
    @Override
    public void add(String dataString) {
        add(createElement(dataString));
    }

    /**
     * Adds data from file filePath to a set
     *
     * @param filePath
     */
    @Override
    public void load(String filePath) {
        if (filePath == null || filePath.length() == 0) {
            return;
        }

        clear();
        try (BufferedReader fReader = Files.newBufferedReader(Paths.get(filePath), Charset.defaultCharset())) {
            fReader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .forEach(this::add);
        } catch (FileNotFoundException e) {
            Ks.ern("Data file " + filePath + " not found");
        } catch (IOException e) {
            Ks.ern("File " + filePath + " reading error");
        }
    }

    protected E createElement(String data) {
        return Optional.ofNullable(createFunction)
                .map(f -> f.apply(data))
                .orElseThrow(() -> new IllegalStateException("Set element creation function is missing"));
    }
}
