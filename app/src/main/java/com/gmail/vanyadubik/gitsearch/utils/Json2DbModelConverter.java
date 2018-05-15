package com.gmail.vanyadubik.gitsearch.utils;

import com.gmail.vanyadubik.gitsearch.model.db.Owner;
import com.gmail.vanyadubik.gitsearch.model.db.Repository;
import com.gmail.vanyadubik.gitsearch.model.json.OwnerDTO;
import com.gmail.vanyadubik.gitsearch.model.json.ResultItemDTO;


public class Json2DbModelConverter {

    public static Owner convertOwner(OwnerDTO ownerDTO) {
        return new Owner(
                ownerDTO.getId(),
                ownerDTO.getLogin(),
                ownerDTO.getAvatar(),
                ownerDTO.getUrl(),
                ownerDTO.getBlog(),
                ownerDTO.getLocation());
    }

    public static Repository convertRepository(ResultItemDTO resultItemDTO) {
        return new Repository(
                    resultItemDTO.getId(),
                    resultItemDTO.getName(),
                    resultItemDTO.getFull_name(),
                    resultItemDTO.isPriv(),
                    resultItemDTO.getUrl(),
                    resultItemDTO.getDescription(),
                    resultItemDTO.getDateCreate(),
                    resultItemDTO.getDateUpdate(),
                    resultItemDTO.getSize(),
                    resultItemDTO.getWatchers(),
                    resultItemDTO.getScore(),
                    resultItemDTO.getDefBranch(),
                    resultItemDTO.getOwner() != null? resultItemDTO.getOwner().getId() : null);
    }
}
