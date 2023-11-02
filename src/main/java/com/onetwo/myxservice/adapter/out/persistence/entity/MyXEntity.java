package com.onetwo.myxservice.adapter.out.persistence.entity;

import com.onetwo.myxservice.adapter.out.persistence.repository.converter.BooleanNumberConverter;
import com.onetwo.myxservice.domain.MyX;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter(AccessLevel.PRIVATE)
@Entity
@NoArgsConstructor
@Table(name = "my_x")
public class MyXEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String xsName;

    @Column(nullable = false)
    private Instant xsBirth;

    @Column(nullable = false, length = 1)
    @Convert(converter = BooleanNumberConverter.class)
    private Boolean isConnected;

    @Column
    private String xsUserId;

    @Column(nullable = false, length = 1)
    @Convert(converter = BooleanNumberConverter.class)
    private Boolean state;

    public MyXEntity(Long id, String userId, String xsName, Instant xsBirth, Boolean isConnected, String xsUserId, Boolean state) {
        this.id = id;
        this.userId = userId;
        this.xsName = xsName;
        this.xsBirth = xsBirth;
        this.isConnected = isConnected;
        this.xsUserId = xsUserId;
        this.state = state;
    }

    public static MyXEntity domainToEntity(MyX newMyX) {
        MyXEntity myXEntity = new MyXEntity(
                newMyX.getId(),
                newMyX.getUserId(),
                newMyX.getXsName(),
                newMyX.getXsBirth(),
                newMyX.isConnected(),
                newMyX.getXsUserId(),
                newMyX.isState()
        );

        myXEntity.setMetaDataByDomain(newMyX);
        return myXEntity;
    }
}
