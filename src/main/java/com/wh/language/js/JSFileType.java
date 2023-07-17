// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.wh.language.js;

import com.intellij.openapi.fileTypes.LanguageFileType;
import icons.MyIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class JSFileType extends LanguageFileType {

  public static final JSFileType INSTANCE = new JSFileType();

  private JSFileType() {
    super(JSLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public String getName() {
    return "JS File";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "JS language file";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return "js";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return MyIcons.FILE;
  }

}
