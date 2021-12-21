package rso.football.uporabniki.models.converters;

import rso.football.uporabniki.lib.UporabnikiMetadata;
import rso.football.uporabniki.models.entities.UporabnikiMetadataEntity;

public class UporabnikiMetadataConverter {

    public static UporabnikiMetadata toDto(UporabnikiMetadataEntity entity) {

        UporabnikiMetadata dto = new UporabnikiMetadata();
        dto.setUporabnikId(entity.getId());
        dto.setUporabnikID(entity.getUporabnikID());
        dto.setRole(entity.getRole());

        return dto;
    }

    public static UporabnikiMetadataEntity toEntity(UporabnikiMetadata dto) {

        UporabnikiMetadataEntity entity = new UporabnikiMetadataEntity();
        entity.setUporabnikID(dto.getUporabnikID());
        entity.setRole(dto.getRole());

        return entity;
    }

}
