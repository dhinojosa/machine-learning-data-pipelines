package com.xyzcorp;


import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.Optional;

@Path("/send")
public class KafkaSender {
    private static final Logger logger =
        LoggerFactory.getLogger(KafkaSender.class);

    @Inject
    @Channel("newsfeed")
    public Emitter<String> emitter;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String send(Map<String, String> entry) {
        logger.debug("Received {}", entry.toString());
        Optional.ofNullable(
            entry.get("key")).flatMap(x ->
            Optional.ofNullable(entry.get("value"))
                    .map(y -> {
                        emitter.send(y);
                        return "Success";
                    }));
        return "Failure";
    }
}
