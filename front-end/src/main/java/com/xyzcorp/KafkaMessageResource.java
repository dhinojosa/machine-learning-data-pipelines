package com.xyzcorp;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/kafka")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class KafkaMessageResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    public KafkaMessageResource() {
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Outgoing("newsfeed")
    public String add(String message) {
        return "Success";
    }
}
