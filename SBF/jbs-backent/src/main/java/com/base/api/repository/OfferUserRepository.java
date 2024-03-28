package com.base.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.api.entities.Offer;
import com.base.api.entities.OfferUser;
import com.base.api.entities.User;

@Repository
public interface OfferUserRepository extends JpaRepository<OfferUser, UUID> {

    List<OfferUser> findByOffer(Offer offer);

    List<OfferUser> findByUser(User user);

}
