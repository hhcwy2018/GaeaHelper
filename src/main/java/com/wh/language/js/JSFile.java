// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.wh.language.js;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class JSFile extends PsiFileBase {

  public JSFile(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, JSLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public FileType getFileType() {
    return JSFileType.INSTANCE;
  }

  @Override
  public String toString() {
    return "JS File";
  }

}
