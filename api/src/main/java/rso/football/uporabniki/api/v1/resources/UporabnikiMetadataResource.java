package rso.football.uporabniki.api.v1.resources;

import rso.football.uporabniki.lib.UporabnikiMetadata;
import rso.football.uporabniki.services.beans.UporabnikiMetadataBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/uporabniki")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UporabnikiMetadataResource {

    private Logger log = Logger.getLogger(UporabnikiMetadataResource.class.getName());

    @Inject
    private UporabnikiMetadataBean uporabnikiMetadataBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getUporabnikiMetadata() {

        List<UporabnikiMetadata> uporabnikiMetadata = uporabnikiMetadataBean.getUporabnikiMetadataFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(uporabnikiMetadata).build();
    }

    @GET
    @Path("/{uporabnikiMetadataId}")
    public Response getUporabnikiMetadata(@PathParam("uporabnikiMetadataId") Integer uporabnikiMetadataId) {

        UporabnikiMetadata uporabnikiMetadata = uporabnikiMetadataBean.getUporabnikiMetadata(uporabnikiMetadataId);

        if (uporabnikiMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(uporabnikiMetadata).build();
    }

    @POST
    public Response createUporabnikiMetadata(UporabnikiMetadata uporabnikiMetadata) {

        if ((uporabnikiMetadata.getUporabnikID() == null || uporabnikiMetadata.getRole() == null)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            uporabnikiMetadata = uporabnikiMetadataBean.createUporabnikiMetadata(uporabnikiMetadata);
        }

        return Response.status(Response.Status.CONFLICT).entity(uporabnikiMetadata).build();

    }

    @PUT
    @Path("{uporabnikiMetadataId}")
    public Response putUporabnikiMetadata(@PathParam("uporabnikiMetadataId") Integer uporabnikiMetadataId,
                                     UporabnikiMetadata uporabnikiMetadata) {

        uporabnikiMetadata = uporabnikiMetadataBean.putUporabnikiMetadata(uporabnikiMetadataId, uporabnikiMetadata);

        if (uporabnikiMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

    @DELETE
    @Path("{uporabnikiMetadataId}")
    public Response deleteUporabnikiMetadata(@PathParam("uporabnikiMetadataId") Integer uporabnikiMetadataId) {

        boolean deleted = uporabnikiMetadataBean.deleteUporabnikiMetadata(uporabnikiMetadataId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}