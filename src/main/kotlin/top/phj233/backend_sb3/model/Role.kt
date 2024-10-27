package top.phj233.backend_sb3.model

import org.babyfish.jimmer.sql.*


@Entity
interface Role{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long
    @Key
    val name: String
    @ManyToMany(mappedBy = "roles")
    val users: List<User>
}
