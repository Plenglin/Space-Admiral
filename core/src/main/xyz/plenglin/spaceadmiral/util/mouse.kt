package xyz.plenglin.spaceadmiral.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

fun getCenterMousePos(): Vector2 = Vector2(Gdx.input.x - Gdx.graphics.width / 2f, Gdx.input.y - Gdx.graphics.height / 2f)
fun getCenterMousePos3(): Vector3 = Vector3(Gdx.input.x - Gdx.graphics.width / 2f, Gdx.input.y - Gdx.graphics.height / 2f, 0f)
fun getMousePos(): Vector2 = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
fun getMousePos3(): Vector3 = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
