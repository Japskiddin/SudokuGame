package io.github.japskiddin.sudoku.feature.home.ui.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build

internal fun Context.getPackageInfo(): PackageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
} else {
    packageManager.getPackageInfo(packageName, 0)
}
