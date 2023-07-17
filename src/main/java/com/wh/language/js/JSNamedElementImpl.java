// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.wh.language.js;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class JSNamedElementImpl extends ASTWrapperPsiElement implements JSNamedElement {

  public JSNamedElementImpl(@NotNull ASTNode node) {
    super(node);
  }

}
