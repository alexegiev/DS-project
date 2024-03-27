import java.util.Map;

public class WorkerImpl implements Worker{

    public void initialize(){
        System.out.println("Worker initialized");
    }

    @Override
    public void sendToReducer(Map<Integer, String> data) {

    }

}
