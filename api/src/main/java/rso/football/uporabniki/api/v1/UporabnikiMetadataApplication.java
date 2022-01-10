package rso.football.uporabniki.api.v1;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

//some change

//TODO: change url to not localhost
@OpenAPIDefinition(info = @Info(title = "Uporabniki API", version = "v1",
        contact = @Contact(email = "rb2600@student.uni-lj.si"),
        license = @License(name = "dev"), description = "API for managing uporabniki metadata."),
        servers = @Server(url = "https://20.85.248.6:8083/"))
@ApplicationPath("/v1")
public class UporabnikiMetadataApplication extends Application {

}
