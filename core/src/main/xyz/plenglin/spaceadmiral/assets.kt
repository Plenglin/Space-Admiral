package xyz.plenglin.spaceadmiral

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin

val ASSET_SKIN = AssetDescriptor<Skin>("skin/default/skin/uiskin.skin", Skin::class.java)
val ASSET_UI_ATLAS = AssetDescriptor<TextureAtlas>("ui.atlas", TextureAtlas::class.java)
val ASSET_GAMESPRITE_ATLAS = AssetDescriptor<TextureAtlas>("gamesprite.atlas", TextureAtlas::class.java)
