package com.m.ammar.itaskmanager.utility

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

/**
 * A utility class to provide access to app resources (strings, dimensions, drawables, colors, etc.).
 *
 * @param context The context used to fetch resources.
 */
class ResourceProvider(private val context: Context) {

    /**
     * Retrieves a string resource.
     *
     * @param resId The resource ID of the string.
     * @param formatArgs Optional format arguments for the string.
     * @return The formatted string.
     */
    fun getString(resId: Int, vararg formatArgs: Any?): String {
        return context.getString(resId, *formatArgs)
    }

    /**
     * Retrieves a dimension resource.
     *
     * @param resId The resource ID of the dimension.
     * @return The dimension value in pixels.
     */
    fun getDimension(resId: Int): Float {
        return context.resources.getDimension(resId)
    }

    /**
     * Retrieves an integer resource.
     *
     * @param resId The resource ID of the integer.
     * @return The integer value.
     */
    fun getInteger(resId: Int): Int {
        return context.resources.getInteger(resId)
    }

    /**
     * Retrieves a drawable resource.
     *
     * @param resId The resource ID of the drawable.
     * @return The drawable, or null if not found.
     */
    fun getDrawable(resId: Int): Drawable? {
        return ContextCompat.getDrawable(context, resId)
    }

    /**
     * Retrieves a color resource.
     *
     * @param resId The resource ID of the color.
     * @return The color as an integer.
     */
    fun getColor(resId: Int): Int {
        return ContextCompat.getColor(context, resId)
    }
}
