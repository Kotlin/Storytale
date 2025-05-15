@file:Suppress("ktlint:standard:backing-property-naming")

package org.jetbrains.compose.storytale.gallery.material3

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal val ContentCopyImageVector: ImageVector
    get() {
        if (_Content_copy != null) {
            return _Content_copy!!
        }
        _Content_copy = ImageVector.Builder(
            name = "Content_copy",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero,
            ) {
                moveTo(360f, 720f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(280f, 640f)
                verticalLineToRelative(-480f)
                quadToRelative(0f, -33f, 23.5f, -56.5f)
                reflectiveQuadTo(360f, 80f)
                horizontalLineToRelative(360f)
                quadToRelative(33f, 0f, 56.5f, 23.5f)
                reflectiveQuadTo(800f, 160f)
                verticalLineToRelative(480f)
                quadToRelative(0f, 33f, -23.5f, 56.5f)
                reflectiveQuadTo(720f, 720f)
                close()
                moveToRelative(0f, -80f)
                horizontalLineToRelative(360f)
                verticalLineToRelative(-480f)
                horizontalLineTo(360f)
                close()
                moveTo(200f, 880f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(120f, 800f)
                verticalLineToRelative(-560f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(560f)
                horizontalLineToRelative(440f)
                verticalLineToRelative(80f)
                close()
                moveToRelative(160f, -240f)
                verticalLineToRelative(-480f)
                close()
            }
        }.build()
        return _Content_copy!!
    }

private var _Content_copy: ImageVector? = null

internal val MenuOpenImageVector: ImageVector
    get() {
        if (_Menu_open != null) {
            return _Menu_open!!
        }
        _Menu_open = ImageVector.Builder(
            name = "Menu_open",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero,
            ) {
                moveTo(120f, 720f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(520f)
                verticalLineToRelative(80f)
                close()
                moveToRelative(664f, -40f)
                lineTo(584f, 480f)
                lineToRelative(200f, -200f)
                lineToRelative(56f, 56f)
                lineToRelative(-144f, 144f)
                lineToRelative(144f, 144f)
                close()
                moveTo(120f, 520f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(400f)
                verticalLineToRelative(80f)
                close()
                moveToRelative(0f, -200f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(520f)
                verticalLineToRelative(80f)
                close()
            }
        }.build()
        return _Menu_open!!
    }

private var _Menu_open: ImageVector? = null

internal val Dark_mode: ImageVector
    get() {
        if (_Dark_mode != null) {
            return _Dark_mode!!
        }
        _Dark_mode = ImageVector.Builder(
            name = "Dark_mode",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero,
            ) {
                moveTo(480f, 840f)
                quadToRelative(-150f, 0f, -255f, -105f)
                reflectiveQuadTo(120f, 480f)
                reflectiveQuadToRelative(105f, -255f)
                reflectiveQuadToRelative(255f, -105f)
                quadToRelative(14f, 0f, 27.5f, 1f)
                reflectiveQuadToRelative(26.5f, 3f)
                quadToRelative(-41f, 29f, -65.5f, 75.5f)
                reflectiveQuadTo(444f, 300f)
                quadToRelative(0f, 90f, 63f, 153f)
                reflectiveQuadToRelative(153f, 63f)
                quadToRelative(55f, 0f, 101f, -24.5f)
                reflectiveQuadToRelative(75f, -65.5f)
                quadToRelative(2f, 13f, 3f, 26.5f)
                reflectiveQuadToRelative(1f, 27.5f)
                quadToRelative(0f, 150f, -105f, 255f)
                reflectiveQuadTo(480f, 840f)
                moveToRelative(0f, -80f)
                quadToRelative(88f, 0f, 158f, -48.5f)
                reflectiveQuadTo(740f, 585f)
                quadToRelative(-20f, 5f, -40f, 8f)
                reflectiveQuadToRelative(-40f, 3f)
                quadToRelative(-123f, 0f, -209.5f, -86.5f)
                reflectiveQuadTo(364f, 300f)
                quadToRelative(0f, -20f, 3f, -40f)
                reflectiveQuadToRelative(8f, -40f)
                quadToRelative(-78f, 32f, -126.5f, 102f)
                reflectiveQuadTo(200f, 480f)
                quadToRelative(0f, 116f, 82f, 198f)
                reflectiveQuadToRelative(198f, 82f)
                moveToRelative(-10f, -270f)
            }
        }.build()
        return _Dark_mode!!
    }

private var _Dark_mode: ImageVector? = null

internal val Light_mode: ImageVector
    get() {
        if (_Light_mode != null) {
            return _Light_mode!!
        }
        _Light_mode = ImageVector.Builder(
            name = "Light_mode",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero,
            ) {
                moveTo(480f, 600f)
                quadToRelative(50f, 0f, 85f, -35f)
                reflectiveQuadToRelative(35f, -85f)
                reflectiveQuadToRelative(-35f, -85f)
                reflectiveQuadToRelative(-85f, -35f)
                reflectiveQuadToRelative(-85f, 35f)
                reflectiveQuadToRelative(-35f, 85f)
                reflectiveQuadToRelative(35f, 85f)
                reflectiveQuadToRelative(85f, 35f)
                moveToRelative(0f, 80f)
                quadToRelative(-83f, 0f, -141.5f, -58.5f)
                reflectiveQuadTo(280f, 480f)
                reflectiveQuadToRelative(58.5f, -141.5f)
                reflectiveQuadTo(480f, 280f)
                reflectiveQuadToRelative(141.5f, 58.5f)
                reflectiveQuadTo(680f, 480f)
                reflectiveQuadToRelative(-58.5f, 141.5f)
                reflectiveQuadTo(480f, 680f)
                moveTo(200f, 520f)
                horizontalLineTo(40f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(160f)
                close()
                moveToRelative(720f, 0f)
                horizontalLineTo(760f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(160f)
                close()
                moveTo(440f, 200f)
                verticalLineToRelative(-160f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(160f)
                close()
                moveToRelative(0f, 720f)
                verticalLineToRelative(-160f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(160f)
                close()
                moveTo(256f, 310f)
                lineToRelative(-101f, -97f)
                lineToRelative(57f, -59f)
                lineToRelative(96f, 100f)
                close()
                moveToRelative(492f, 496f)
                lineToRelative(-97f, -101f)
                lineToRelative(53f, -55f)
                lineToRelative(101f, 97f)
                close()
                moveToRelative(-98f, -550f)
                lineToRelative(97f, -101f)
                lineToRelative(59f, 57f)
                lineToRelative(-100f, 96f)
                close()
                moveTo(154f, 748f)
                lineToRelative(101f, -97f)
                lineToRelative(55f, 53f)
                lineToRelative(-97f, 101f)
                close()
                moveToRelative(326f, -268f)
            }
        }.build()
        return _Light_mode!!
    }

private var _Light_mode: ImageVector? = null

public val OpenInFull: ImageVector
    get() {
        if (_openInFull != null) {
            return _openInFull!!
        }
        _openInFull = ImageVector.Builder(
            name = "Open_in_full",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero,
            ) {
                moveTo(120f, 840f)
                verticalLineToRelative(-320f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(184f)
                lineToRelative(504f, -504f)
                horizontalLineTo(520f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(320f)
                verticalLineToRelative(320f)
                horizontalLineToRelative(-80f)
                verticalLineToRelative(-184f)
                lineTo(256f, 760f)
                horizontalLineToRelative(184f)
                verticalLineToRelative(80f)
                close()
            }
        }.build()
        return _openInFull!!
    }

private var _openInFull: ImageVector? = null
