package com.base.api.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "states")
@AttributeOverride(name = "id", column = @Column(name = "state_id"))
public class State extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -4893178028645133168L;

    @Column(name = "status")
    private String status;

    @Column(name = "uuid", unique = true)
    @NotEmpty(message = "uuid must not be null or empty")
    private String uuid;

    @Column(name = "state_code")
    private String stateCode;

    @OneToMany()
    @JoinColumn(name = "state_id")
    private List<StateTranslation> states;

    @ManyToOne(fetch = FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.MERGE, 
            CascadeType.REFRESH})
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;
}
