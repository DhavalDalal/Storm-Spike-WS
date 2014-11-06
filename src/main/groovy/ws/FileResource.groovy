package ws

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import utils.MessageSender

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType
import org.glassfish.jersey.media.multipart.FormDataParam
import org.glassfish.jersey.media.multipart.FormDataContentDisposition

import javax.ws.rs.core.Response

@Path("/file")
@Slf4j
class FileResource {

    @Autowired
    private MessageSender sender

    @Context
    private HttpHeaders headers

    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public String ping() {
        "{greet: 'hello from file resource'}"
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {
        sendToQueue(uploadedInputStream.text)
        String response = "Pushed to Queue... $fileDetail.fileName"
        return Response.status(200).entity(response).build()
    }

    private void sendToQueue(String message) {
        log.info("Sending to Queue...$message")
        sender.send(message)
    }
}
