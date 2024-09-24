package com.bms.messaging.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@Table(name = "bms_user")
@Entity
public class UserEntity {
    @Id
    String emailId;
    String userId;
    String username;
}
