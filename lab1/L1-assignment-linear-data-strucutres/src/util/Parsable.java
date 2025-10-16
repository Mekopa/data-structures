/** @author Eimutis Karčiauskas, KTU IF Department of Software Engineering, 23 09 2014
 *
 * This is the interface that the model classes developed by KTU students have to implement.
 * The methods provide a convenient way of forming data from strings.
 ***************************************************************************** */
package util;

public interface Parsable<T> extends Comparable<T> {
    void parse(String dataString); // forms an object from a string
}
