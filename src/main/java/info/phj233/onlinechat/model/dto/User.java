package info.phj233.onlinechat.model.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户表
 * @TableName user
 */
@Entity
@Table(name="user")
@Data
public class User implements Serializable {
    /**
     * 用户uid
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private Integer uid;

    /**
     * 用户名
     */
    @Column(name = "username")
    private String username;

    /**
     * 用户密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 用户邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 用户组
     */
    @Column(name = "role")
    private String role;

    /**
     * 用户是否启用
     */
    @Column(name = "enabled", columnDefinition = "bit(1) default 1")
    private Boolean enabled;

    @Serial
    private static final long serialVersionUID = 1L;
}