/*
*
* Worker will accept key/value pair by the master
* Perform the operation on the key/value pair
* Create key2/value2 pair
* Send it to Reducer
*
*/

import java.util.Map;

public interface Worker {

    public Map<Integer, String> data = null;

    public void initialize();
    public void sendToReducer(Map<Integer, String> data);

}
