// This is a generated file. Not intended for manual editing.
package com.wh.language.js;

import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public interface JSProperty extends JSNamedElement {

  String getKey();

  String getValue();

  String getName();

  PsiElement setName(@NotNull String newName);

  PsiElement getNameIdentifier();

  ItemPresentation getPresentation();

}
