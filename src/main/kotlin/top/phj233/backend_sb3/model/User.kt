package top.phj233.backend_sb3.model

import org.babyfish.jimmer.sql.*
import top.phj233.backend_sb3.ws.Message


@Entity
interface User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long
    val username: String
    val password: String
    val email: String
    @Column
    val phone: String?
    @Column
    val avatar: String?
    @Column
    val age: Int?
    @ManyToMany
    val roles: List<Role>
    @OneToMany(mappedBy = "sender")
    val messages: List<Message>
}


