package com.lemonqwest.app.domain.user

import com.lemonqwest.app.domain.auth.PIN
import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Core domain entity representing a user in the LemonQwest family task management system.
 *
 * This entity encapsulates user identity, role-based permissions, and token economy participation.
 * Users can be either children who complete tasks to earn tokens, or caregivers who manage
 * tasks and oversee the family's progress.
 *
 * @property id Unique identifier for the user, automatically generated if not provided
 * @property name Display name for the user, used throughout the application interface
 * @property role User's role determining permissions and available features
 * @property tokenBalance Current token balance for participation in the reward economy
 * @property pin Optional PIN for role switching and authentication, null for child users
 * @property displayName Custom display name that can be different from the login name
 * @property avatarType Type of avatar being used (PREDEFINED or CUSTOM)
 * @property avatarData Avatar data - either predefined avatar ID or custom image data
 * @property favoriteColor User's preferred color for personalization
 * @property isAdmin Admin status for caregivers (true for Family Admin, false for Non-Admin)
 *
 * @sample
 * ```kotlin
 * // Create a child user
 * val child = User(
 *     name = "Lemmy",
 *     role = UserRole.CHILD,
 *     tokenBalance = TokenBalance.create(50)
 * )
 *
 * // Create a caregiver with PIN access
 * val caregiver = User(
 *     name = "Parent",
 *     role = UserRole.CAREGIVER,
 *     pin = PIN.create("1234")
 * )
 *
 * // Create a family admin caregiver
 * val admin = User(
 *     name = "Admin",
 *     role = UserRole.CAREGIVER,
 *     pin = PIN.create("1234"),
 *     isAdmin = true
 * )
 * ```
 */
@Serializable
data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val role: UserRole = UserRole.CHILD,
    val tokenBalance: TokenBalance = TokenBalance.zero(),
    val pin: PIN? = null,
    val displayName: String? = null,
    val avatarType: AvatarType = AvatarType.PREDEFINED,
    val avatarData: String = DEFAULT_AVATAR_ID,
    val favoriteColor: String? = null,
    val isAdmin: Boolean = false,
) {
    companion object {
        const val DEFAULT_AVATAR_ID = "default_child"
    }
}

/**
 * Defines the type of avatar a user can have.
 *
 * - [PREDEFINED]: Uses one of the built-in avatar options
 * - [CUSTOM]: Uses a custom uploaded image
 */
@Serializable
enum class AvatarType {
    /**
     * Predefined avatar from the app's built-in collection.
     * These avatars are theme-appropriate and optimized for performance.
     */
    PREDEFINED,

    /**
     * Custom avatar uploaded by the user.
     * Stored as base64 encoded image data.
     */
    CUSTOM,
}

/**
 * Defines the role-based access control system for LemonQwest application.
 *
 * Each role determines what actions a user can perform and which features are accessible.
 * This enum implements the principle of least privilege, ensuring users only have access
 * to functionality appropriate for their role.
 *
 * - [CHILD]: Can complete tasks, earn tokens, and spend tokens on rewards
 * - [CAREGIVER]: Can create/manage tasks, view family progress, and configure rewards
 */
@Serializable
enum class UserRole {
    /**
     * Child role with task completion and token earning capabilities.
     *
     * Children can:
     * - View and complete assigned tasks
     * - Earn tokens through task completion
     * - Spend tokens on available rewards
     * - Track their progress and achievements
     *
     * Children cannot:
     * - Create or modify tasks
     * - Access other users' data
     * - Change system settings
     * - View caregiver administrative features
     */
    CHILD,

    /**
     * Caregiver role with family management and oversight capabilities.
     *
     * Caregivers can:
     * - Create, modify, and assign tasks
     * - View family progress and statistics
     * - Configure reward catalog and pricing
     * - Manage user accounts and settings
     * - Switch to child role for demonstration
     *
     * Caregivers cannot:
     * - Complete tasks assigned to children
     * - Directly modify child token balances (must use proper award/spend flows)
     */
    CAREGIVER,
}
