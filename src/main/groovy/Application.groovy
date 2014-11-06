import org.glassfish.jersey.media.multipart.MultiPartFeature
import org.glassfish.jersey.server.ResourceConfig

import javax.ws.rs.ApplicationPath

@ApplicationPath("ws")
public class Application extends ResourceConfig {
    Application() {
//        packages("org.foo.rest;org.bar.rest")
        packages('ws')
        register(MultiPartFeature.class)
    }
}