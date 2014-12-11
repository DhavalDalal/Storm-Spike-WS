package ws

import groovy.util.logging.Slf4j
import org.glassfish.jersey.media.multipart.FormDataContentDisposition
import org.glassfish.jersey.media.multipart.FormDataParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import utils.MessageSender

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType

@Path("/rate")
@Slf4j
class RateResource {

    @Autowired
    @Qualifier(value = 'wsJmsMessageSender')
    private MessageSender sender

    @Context
    private HttpHeaders headers

    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public String ping() {
        """
         |{
         |  "greet": "Hello from ${getClass().simpleName}"
         |}
        """.stripMargin()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String newRate(String rateJson) {
        sendToQueue(rateJson)
        """
         |{
         |  "success": { "Accepted" : $rateJson }
         |}
        """.stripMargin()
    }

    private void sendToQueue(String message) {
        log.info("Sending to Queue...$message")
        sender.send(message)
    }
}
