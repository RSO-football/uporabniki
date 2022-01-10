package rso.football.uporabniki.api.v1.resources;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import org.antlr.v4.runtime.misc.Pair;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import rso.football.uporabniki.dtos.PostavkaRequest;
import rso.football.uporabniki.lib.UporabnikiMetadata;
import rso.football.uporabniki.services.beans.UporabnikiMetadataBean;
import rso.football.uporabniki.services.clients.PostavkaCreateApi;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

@Log
@ApplicationScoped
@Path("/uporabniki")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, DELETE, PUT, HEAD, OPTIONS")
public class UporabnikiMetadataResource {

    private Logger log = Logger.getLogger(UporabnikiMetadataResource.class.getName());

    @Inject
    private UporabnikiMetadataBean uporabnikiMetadataBean;

    @Inject
    @RestClient
    private PostavkaCreateApi postavkaCreateApi;

    @Context
    protected UriInfo uriInfo;

    @Operation(description = "Get all uporabniki metadata.", summary = "Get all metadata")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of uporabniki metadata",
                    content = @Content(schema = @Schema(implementation = UporabnikiMetadata.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    public Response getUporabnikiMetadata() {

        List<UporabnikiMetadata> uporabnikiMetadata = uporabnikiMetadataBean.getUporabnikiMetadataFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(uporabnikiMetadata).build();
    }

    @Operation(description = "Get all trenerji metadata.", summary = "Get all trenerji metadata")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of trenerji metadata",
                    content = @Content(schema = @Schema(implementation = UporabnikiMetadata.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    @Path("/trenerji")
    public Response getTrenerjiMetadata() {

        List<UporabnikiMetadata> trenerjiMetadata = uporabnikiMetadataBean.getTrenerjiMetadata();

        return Response.status(Response.Status.OK).entity(trenerjiMetadata).build();
    }

    @Operation(description = "Get trenerji id.", summary = "Get trenerji id")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "String trenerji id",
                    content = @Content(
                            schema = @Schema(implementation = UporabnikiMetadata.class))
            )})
    @GET
    @Path("/trenerjiId")
    public Response getTrenerjiIdMetadata() {

        String trenerjiId = uporabnikiMetadataBean.getTrenerjiIdMetadata();

        return Response.status(Response.Status.OK).entity(trenerjiId).build();
    }

    @Operation(description = "Get metadata for one uporabnik.", summary = "Get metadata for one uporabnik")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Igrisce metadata",
                    content = @Content(
                            schema = @Schema(implementation = UporabnikiMetadata.class))
            )})
    @GET
    @Path("/{uporabnikiMetadataId}")
    public Response getUporabnikiMetadata(@Parameter(description = "Metadata ID.", required = true)
                                              @PathParam("uporabnikiMetadataId") Integer uporabnikiMetadataId) {

        UporabnikiMetadata uporabnikiMetadata = uporabnikiMetadataBean.getUporabnikiMetadata(uporabnikiMetadataId);

        if (uporabnikiMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(uporabnikiMetadata).build();
    }

    @Operation(description = "Add uporabnik metadata.", summary = "Add metadata")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Metadata successfully added."
            ),
            @APIResponse(responseCode = "400", description = "Bad request.")
    })
    @POST
    public Response createUporabnikiMetadata(@RequestBody(
            description = "DTO object with uporabniki metadata.",
            required = true, content = @Content(
            schema = @Schema(implementation = UporabnikiMetadata.class))) UporabnikiMetadata uporabnikiMetadata) {

        if ((uporabnikiMetadata.getUporabnikID() == null || uporabnikiMetadata.getRole() == null)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            uporabnikiMetadata = uporabnikiMetadataBean.createUporabnikiMetadata(uporabnikiMetadata);
        }

        if (uporabnikiMetadata.getRole().equals("trener") || uporabnikiMetadata.getRole().equals("Trener")){
            System.out.println("Asinhrono delanje postavke");
            CompletionStage<String> stringCompletionStage = postavkaCreateApi.createPostavkaTrenerju(new PostavkaRequest(uporabnikiMetadata.getUporabnikId(), 90.0F));

            stringCompletionStage.whenComplete((s, throwable) -> System.out.println(s));
            stringCompletionStage.exceptionally(throwable -> {
               log.severe(throwable.getMessage());
               return throwable.getMessage();
            });
        }

        return Response.status(Response.Status.CREATED).entity(uporabnikiMetadata).build();

    }

    @Operation(description = "Update metadata for on uporabnik.", summary = "Update metadata")
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "Metadata successfully updated."
            ),
            @APIResponse(responseCode = "404", description = "Not found.")
    })
    @PUT
    @Path("{uporabnikiMetadataId}")
    public Response putUporabnikiMetadata(@Parameter(description = "Metadata ID.", required = true)
                                              @PathParam("uporabnikiMetadataId") Integer uporabnikiMetadataId,
                                          @RequestBody(
                                                  description = "DTO object with uporabnik metadata.",
                                                  required = true, content = @Content(
                                                  schema = @Schema(implementation = UporabnikiMetadata.class)))
                                                  UporabnikiMetadata uporabnikiMetadata) {

        Pair<UporabnikiMetadata, Boolean> returnValue = uporabnikiMetadataBean.putUporabnikiMetadata(uporabnikiMetadataId, uporabnikiMetadata);
        System.out.println(returnValue);
        if (returnValue == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        System.out.println(returnValue.a);
        System.out.println(returnValue.b);
        uporabnikiMetadata = returnValue.a;

        if (returnValue.b) {
            if (uporabnikiMetadata.getRole().equals("trener") || uporabnikiMetadata.getRole().equals("Trener")) {
                System.out.println("Asinhrono delanje postavke, ko spreminjas vlogo uporabnika");
                CompletionStage<String> stringCompletionStage = postavkaCreateApi.createPostavkaTrenerju(new PostavkaRequest(uporabnikiMetadata.getUporabnikId(), 90.0F));

                stringCompletionStage.whenComplete((s, throwable) -> System.out.println(s));
                stringCompletionStage.exceptionally(throwable -> {
                    log.severe(throwable.getMessage());
                    return throwable.getMessage();
                });
            }
        }

        return Response.status(Response.Status.NO_CONTENT).build();

    }

    @Operation(description = "Delete metadata for one uporabnik.", summary = "Delete metadata")
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "Metadata successfully deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Not found."
            )
    })
    @DELETE
    @Path("{uporabnikiMetadataId}")
    public Response deleteUporabnikiMetadata(@Parameter(description = "Metadata ID.", required = true)
                                                 @PathParam("uporabnikiMetadataId") Integer uporabnikiMetadataId) {

        boolean deleted = uporabnikiMetadataBean.deleteUporabnikiMetadata(uporabnikiMetadataId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}