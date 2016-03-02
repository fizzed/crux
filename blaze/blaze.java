
import com.fizzed.blaze.Contexts;
import static com.fizzed.blaze.Systems.exec;
import org.slf4j.Logger;

public class blaze {
    private final Logger log = Contexts.logger();
    
    public void demo() {
        exec("mvn", "-Pninja-lite-demo-exec", "test").run();
    }
}
