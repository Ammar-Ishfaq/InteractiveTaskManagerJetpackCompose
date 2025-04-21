package com.m.ammar.itaskmanager.data.local.model

/**
 * Enum class representing the priority levels for tasks.
 *
 * @param weight The weight associated with each priority level. Lower values represent higher priority.
 * HIGH has the highest priority (weight = 1), followed by MEDIUM (weight = 2), and LOW (weight = 3).
 */
enum class Priority(val weight: Int) {
    HIGH(1),
    MEDIUM(2),
    LOW(3)
}
