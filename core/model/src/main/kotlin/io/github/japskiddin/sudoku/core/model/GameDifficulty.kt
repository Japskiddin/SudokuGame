package io.github.japskiddin.sudoku.core.model

// @formatter:off
/*
 * qqwing - Sudoku solver and generator
 * Copyright (C) 2014 Stephen Ostermiller
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
// @formatter:on
public enum class GameDifficulty(public val mistakesLimit: Int) {
    UNSPECIFIED(mistakesLimit = 0),
    EASY(mistakesLimit = 12),
    INTERMEDIATE(mistakesLimit = 9),
    HARD(mistakesLimit = 6),
    EXPERT(mistakesLimit = 3)
}
