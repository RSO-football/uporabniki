package rso.football.uporabniki.services.clients;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import rso.football.uporabniki.dtos.PostavkaRequest;

import javax.enterprise.context.Dependent;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.concurrent.CompletionStage;

@Path("/postavke")
@RegisterRestClient(configKey = "postavke-api")
@Dependent
public interface PostavkaCreateApi {

    @POST
    CompletionStage<String> createPostavkaTrenerju(PostavkaRequest postavkaRequest);
}
