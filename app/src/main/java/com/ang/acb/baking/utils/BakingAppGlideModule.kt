package com.ang.acb.baking.utils

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule


/**
 * To use integration libraries and/or Glide’s API extensions we must
 * add exactly one AppGlideModule implementation and annotate it with
 * "@GlideModule" annotation.
 *
 * https://bumptech.github.io/glide/doc/configuration.html#applications
 */
@GlideModule
class BakingAppGlideModule : AppGlideModule()