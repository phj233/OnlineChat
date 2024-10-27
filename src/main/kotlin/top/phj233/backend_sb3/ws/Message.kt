package top.phj233.backend_sb3.ws

import org.babyfish.jimmer.sql.*
import top.phj233.backend_sb3.model.User

/**
 *
 * @author phj233
 * @since 2024/10/9 22:39
 * @version
 */
@Entity
interface Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long
    @ManyToOne
    @OnDissociate(DissociateAction.DELETE)
    val sender: User
    val content: String
    val time: String
}
