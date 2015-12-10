package fr.pinguet62.jsfring.ws;

import static org.springframework.core.convert.TypeDescriptor.collection;
import static org.springframework.core.convert.TypeDescriptor.valueOf;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.core.convert.ConversionService;

import fr.pinguet62.jsfring.model.User;
import fr.pinguet62.jsfring.service.UserService;
import fr.pinguet62.jsfring.ws.dto.UserDto;

@Path("/user")
public final class UserWebservice {

    @Inject
    private ConversionService conversionService;

    @Inject
    private UserService userService;

    @GET
    @Path("/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserDto get(@PathParam("login") String login) {
        User user = userService.get(login);
        if (user == null)
            return null;
        return conversionService.convert(user, UserDto.class);
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserDto> list() {
        List<User> pojos = userService.getAll();
        return (List<UserDto>) conversionService.convert(pojos, collection(List.class, valueOf(User.class)),
                collection(List.class, valueOf(UserDto.class)));
    }

}