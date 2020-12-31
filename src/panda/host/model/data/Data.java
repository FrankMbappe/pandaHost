package panda.host.model.data;

import java.util.List;

/**
 * Set of methods used to interact with a data of type T
 * @param <T> Handling type of data
 */
public interface Data<T> {

    /**
     * Creates a table of T elements in a database
     */
    void init();

    /**
     * Finds every T element stored in a database
     * @return A list of T elements
     */
    List<T> getAll();

    /**
     * Finds a specific T element stored in a database
     * @param id represents the ID of the requested T element
     * @return the T element as an object of T class
     */
    T get(String id);

    /**
     * Adds a T element in a database
     * @param t is the T element as an object
     */
    void add(T t);
}
