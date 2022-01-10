package rso.football.uporabniki.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.antlr.v4.runtime.misc.Pair;
import rso.football.uporabniki.lib.UporabnikiMetadata;
import rso.football.uporabniki.models.converters.UporabnikiMetadataConverter;
import rso.football.uporabniki.models.entities.UporabnikiMetadataEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@RequestScoped
public class UporabnikiMetadataBean {

    private Logger log = Logger.getLogger(UporabnikiMetadataBean.class.getName());

    @Inject
    private EntityManager em;

    public List<UporabnikiMetadata> getUporabnikiMetadata() {

        TypedQuery<UporabnikiMetadataEntity> query = em.createNamedQuery(
                "UporabnikiMetadataEntity.getAll", UporabnikiMetadataEntity.class);

        List<UporabnikiMetadataEntity> resultList = query.getResultList();

        return resultList.stream().map(UporabnikiMetadataConverter::toDto).collect(Collectors.toList());

    }


    public List<UporabnikiMetadata> getTrenerjiMetadata() {
        TypedQuery<UporabnikiMetadataEntity> query = em.createNamedQuery(
                "UporabnikiMetadataEntity.getAllTrenerji", UporabnikiMetadataEntity.class);

        List<UporabnikiMetadataEntity> resultList = query.getResultList();

        return resultList.stream().map(UporabnikiMetadataConverter::toDto).collect(Collectors.toList());
    }


    public String getTrenerjiIdMetadata() {
        TypedQuery<UporabnikiMetadataEntity> query = em.createNamedQuery(
                "UporabnikiMetadataEntity.getAllTrenerji", UporabnikiMetadataEntity.class);

        List<UporabnikiMetadataEntity> resultList = query.getResultList();

        String result = "";
        for (UporabnikiMetadataEntity u : resultList){
            result += u.getId()+",";
        }
        if (result.length() > 0){
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    public List<UporabnikiMetadata> getUporabnikiMetadataFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, UporabnikiMetadataEntity.class, queryParameters).stream()
                .map(UporabnikiMetadataConverter::toDto).collect(Collectors.toList());
    }

    public UporabnikiMetadata getUporabnikiMetadata(Integer id) {

        UporabnikiMetadataEntity uporabnikiMetadataEntity = em.find(UporabnikiMetadataEntity.class, id);

        if (uporabnikiMetadataEntity == null) {
            throw new NotFoundException();
        }

        UporabnikiMetadata uporabnikiMetadata = UporabnikiMetadataConverter.toDto(uporabnikiMetadataEntity);

        return uporabnikiMetadata;
    }

    public UporabnikiMetadata createUporabnikiMetadata(UporabnikiMetadata uporabnikiMetadata) {

        UporabnikiMetadataEntity uporabnikiMetadataEntity = UporabnikiMetadataConverter.toEntity(uporabnikiMetadata);

        try {
            beginTx();
            em.persist(uporabnikiMetadataEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (uporabnikiMetadataEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return UporabnikiMetadataConverter.toDto(uporabnikiMetadataEntity);
    }

    public Pair<UporabnikiMetadata, Boolean> putUporabnikiMetadata(Integer id, UporabnikiMetadata uporabnikiMetadata) {

        UporabnikiMetadataEntity c = em.find(UporabnikiMetadataEntity.class, id);
        Boolean isNewTrener = false;

        if (c == null) {
            return null;
        }

        UporabnikiMetadataEntity updatedUporabnikiMetadataEntity = UporabnikiMetadataConverter.toEntity(uporabnikiMetadata);

        if (c.getRole().equals("igralec") || c.getRole().equals("Igralec")){
            isNewTrener = true;
        }

        try {
            beginTx();
            updatedUporabnikiMetadataEntity.setId(c.getId());
            updatedUporabnikiMetadataEntity = em.merge(updatedUporabnikiMetadataEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return new Pair<>(UporabnikiMetadataConverter.toDto(updatedUporabnikiMetadataEntity), isNewTrener);
    }

    public boolean deleteUporabnikiMetadata(Integer id) {

        UporabnikiMetadataEntity uporabnikiMetadata = em.find(UporabnikiMetadataEntity.class, id);

        if (uporabnikiMetadata != null) {
            try {
                beginTx();
                em.remove(uporabnikiMetadata);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}