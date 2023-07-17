package com.wh.tools;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class WHFileEditorManagerListener implements FileEditorManagerListener {
    @Override
    public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
//        GaeaSwitchIdea.lock(file);
//        System.out.println(file.getName() + " is open!");
    }

    @Override
    public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
//        System.out.println(file.getName() + " is closed!");
        GaeaSwitchIdea.unlock(file, true);
    }
}
