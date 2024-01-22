package com.example.web;

import com.example.pojo.User;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static jakarta.ws.rs.core.Response.*;

@Path("/api")
public class ExampleResource {

    @GET
    @Path("/employee/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmployee(@PathParam("id") Long id) {
        Optional<User> userOptional = Optional.ofNullable(User.findById(id));
        var user = userOptional.orElseThrow(WebApplicationException::new);
        return ok(user).build();
    }

    @POST
    @Path("/employee")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveEmployee(User user) {
        user.persist();
        return status(Status.CREATED).entity(user).build();
    }

    @DELETE
    @Path("/employee/{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id) {
        Optional<User> userOptional = Optional.ofNullable(User.findById(id));
        userOptional.orElseThrow(WebApplicationException::new).delete();
        return noContent().build();
    }

    @PATCH
    @Path("/employee/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateUser(User userForUpdate, @PathParam("id") Long id) {
        Optional<User> userOptional = Optional.ofNullable(User.findById(id));
        var updatedUser = userOptional.map(user -> updateUser(userForUpdate, user))
                .orElseThrow(WebApplicationException::new);
        return ok(updatedUser).build();
    }

    private static User updateUser(User userForUpdate, User user) {
        user.setId(userForUpdate.getId());
        user.setName(userForUpdate.getName());
        user.setSurname(userForUpdate.getSurname());
        user.persist();
        return user;
    }

}
