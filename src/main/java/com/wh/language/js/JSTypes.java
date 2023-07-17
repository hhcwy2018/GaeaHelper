// This is a generated file. Not intended for manual editing.
package com.wh.language.js;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;

public interface JSTypes {

  IElementType PROPERTY = new JSElementType("PROPERTY");

  IElementType COMMENT = new JSTokenType("COMMENT");
  IElementType CRLF = new JSTokenType("CRLF");
  IElementType KEY = new JSTokenType("KEY");
  IElementType SEPARATOR = new JSTokenType("SEPARATOR");
  IElementType VALUE = new JSTokenType("VALUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == PROPERTY) {
        return new JSPropertyImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
