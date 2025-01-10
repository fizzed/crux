import com.fizzed.blaze.Config;
import com.fizzed.blaze.Contexts;
import com.fizzed.blaze.Task;
import com.fizzed.buildx.Buildx;
import com.fizzed.buildx.ContainerBuilder;
import com.fizzed.buildx.Target;
import com.fizzed.jne.*;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.fizzed.blaze.Contexts.withBaseDir;
import static com.fizzed.blaze.Systems.*;
import static com.fizzed.blaze.util.Globber.globber;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;

public class blaze {
    private final Logger log = Contexts.logger();
    private final Config config = Contexts.config();
    private final Path projectDir = withBaseDir("../").toAbsolutePath();

    private final List<Target> crossTestTargets = asList(
        new Target("linux", "x64").setTags("test").setHost("bmh-build-x64-linux-latest"),
        new Target("linux", "arm64").setTags("test").setHost("bmh-build-arm64-linux-latest"),
        new Target("linux", "riscv64").setTags("test").setHost("bmh-build-riscv64-linux-latest"),
        new Target("macos", "x64").setTags("test").setHost("bmh-build-x64-macos-latest"),
        new Target("macos", "arm64").setTags("test").setHost("bmh-build-arm64-macos-latest"),
        new Target("windows", "x64").setTags("test").setHost("bmh-build-x64-windows-latest"),
        new Target("windows", "arm64").setTags("test").setHost("bmh-build-arm64-windows-latest"),
        new Target("freebsd", "x64").setTags("test").setHost("bmh-build-x64-freebsd-latest"),
        new Target("openbsd", "x64").setTags("test").setHost("bmh-build-x64-openbsd-latest")
    );

    @Task(order = 1)
    public void cross_tests() throws Exception {
        new Buildx(crossTestTargets)
            .tags("test")
            .execute((target, project) -> {
                project.action("mvn", "clean", "test")
                    .run();
            });
    }

}
