package panda.host.model.data;

import panda.host.model.models.filters.Filter;

import java.util.ArrayList;

/**
 * Set of methods used to interact with a data of type T
 * @param <T> Handling type of data
 * @param <I> Handling type of Id the data uses
 */
public interface Data<T, I> {

    /**
     * Creates a table of T elements in a database
     */
    void init();

    /**
     * Finds every T element stored in a database
     * @return A list of T elements
     */
    ArrayList<T> getAll();

    /**
     * Finds every T element stored in a database applying a particular filter
     * @param filter represents a filter
     * @return A list of T elements
     */
    ArrayList<T> getMatchingData(Filter filter);

    /**
     * Finds every T element stored in a database applying a particular filter
     * @param filter represents a filter
     * @return A list of T elements, converted to Json format
     */
    String getMatchingDataToJson(Filter filter);

    /**
     * Finds the first T object that matches a filter in a T list
     * @param filter represents a filter
     * @return An object T
     */
    T getMatchingItem(Filter filter);

    /**
     * Finds the first T object that matches a filter in a T list
     * @param filter represents a filter
     * @return An object T, converted to Json format
     */
    String getMatchingItemToJson(Filter filter);

    /**
     * Finds a specific T element stored in a database
     * @param id represents the ID of the requested T element
     * @return the T element as an object of T class
     */
    T get(I id);

    /**
     * Adds a T element in a database
     * @param t is the T element as an object
     */
    boolean add(T t);

    /**
     * Drop a T element from a database
     * @param id is identifier of the element in the database
     */
    boolean remove(I id);

    /**
     * Edit a T element in a database
     * @param t is the T element as an object
     */
    boolean update(T t);

    /**
     * Get values that are filtered using a filter contained in a marshalled code
     * @param filterToJson is a filter that will be applied
     * @return values filtered using the filterToJson object
     */
    String getJsonMatchingDataFromJsonFilter(String filterToJson);

}
