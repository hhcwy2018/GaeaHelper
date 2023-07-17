package com.wh.language.js;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.wh.tools.FileHelp;
import com.wh.tools.JsonHelp;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JSGaeaCompletionContributor extends CompletionContributor {
    public static final String PREFIX = "global_";
    ObjectHintInfos objectHintInfos = new ObjectHintInfos();

    public JSGaeaCompletionContributor() {
        super();
        try {
            init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static final <T> T getJsonData(JSONObject data, String key) {
        if (data.has(key)) {
            return (T) data.get(key);
        } else
            return null;
    }

    protected static void fillLookupKeys(LookupElementBuilder builder, String prex, String name) {
        builder.withLookupString(prex + name);
        //        if (prex != null)
//            name = name.substring(prex.length());
//        String key = prex;
//        for (int i = 0; i < name.length(); i++) {
//            key += name.charAt(i);
//            builder.withLookupString(key);
//        }
    }

    protected static void fillObjResult(ObjectHintInfo info, CompletionResultSet result, String prex) {
        LookupElementBuilder builder = LookupElementBuilder.create(info.name);
        fillLookupKeys(builder, prex, info.name);
        builder
                .withPresentableText(info.prototype);
        if (info.params != null && info.params.size() > 0)
            builder.appendTailText(info.params.toString(), false);
        try{
            result.addElement(
                    builder.withCaseSensitivity(true)//大小写不敏感
                            .withItemTextForeground(Color.white)//一级提示文本颜色
                            .withStrikeoutness(false)//添加表示废弃的删除线
                            .withTypeText(info.title)
//                            .withInsertHandler(new InsertHandler<LookupElement>() {
//                                @Override
//                                public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
//                                    Document document = context.getDocument();
//                                    int insertPosition = context.getSelectionEndOffset();
//                                    context.getEditor().getCaretModel().getCurrentCaret().moveToOffset(insertPosition);
//                                }
//                            })
                            .bold());
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    protected static void fillMethodResult(MethodHintInfo info, CompletionResultSet result, String prex) {
        LookupElementBuilder builder = LookupElementBuilder.create(info.replace);
        fillLookupKeys(builder, prex, prex+"."+info.name);
        builder
                .withPresentableText(info.prototype);
        if (info.params != null && info.params.size() > 0)
            builder.appendTailText(info.params.toString(), false);
        result.addElement(
                builder
                        .withPresentableText(info.prototype)
                        .withCaseSensitivity(true)//大小写不敏感
                        .withItemTextForeground(Color.white)//一级提示文本颜色
                        .withStrikeoutness(false)//添加表示废弃的删除线
                        .withTypeText(info.title)
                        .bold());
    }

    protected void init() throws Exception {
        objectHintInfos.init(FileHelp.getFile("configure", "commands"));
    }

    protected void fillResult(String key, CompletionResultSet result) {
        String[] keys = key.split("\\.");
        ObjectHintInfo objectHintInfo = objectHintInfos.get(keys[0]);
        if (objectHintInfo != null) {
            if (keys.length > 1) {

            }
            objectHintInfo.fill(result);
        } else {

        }
    }

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        int offset = parameters.getOffset();
        Document document = parameters.getEditor().getDocument();
        int lineStartOffset = document.getLineStartOffset(document.getLineNumber(offset));
        String text = document.getText(TextRange.create(lineStartOffset, offset));
        final String KEY = "global_";
        int index = text.lastIndexOf(KEY);
        if (index != -1) {
            text = text.substring(index);
            objectHintInfos.fill(text, result);
            result.stopHere();
        } else {
            super.fillCompletionVariants(parameters, result);
//            WordCompletionContributor.addWordCompletionVariants(result, parameters, Collections.emptySet());
        }
//        result.stopHere();
    }

    public static class ParamHintInfo {
        public String name;
        public String title;

        public ParamHintInfo() {

        }

        public ParamHintInfo(JSONObject data) {
            fromJson(data);
        }

        public void fromJson(JSONObject data) {
            name = getJsonData(data, "name");
            title = getJsonData(data, "title");
        }

        @Override
        public String toString() {
            return name + ":" + title;
        }
    }

    public static class ParamsHintInfo extends ArrayList<ParamHintInfo> {
        public void fromJson(JSONArray data) {
            clear();
            if (data == null || data.length() == 0)
                return;

            for (int i = 0; i < data.length(); i++) {
                add(new ParamHintInfo(data.getJSONObject(i)));
            }
        }

        @Override
        public String toString() {
            if (size() == 0)
                return "";

            String paramStr = null;
            for (ParamHintInfo info : this) {
                if (paramStr == null)
                    paramStr = info.toString();
                else
                    paramStr += "\n" + info.toString();
            }

            return paramStr;
        }
    }

    public static class MethodHintInfo {
        public String name;
        public String title;
        public String prototype;
        public String replace;
        public String result;
        public ParamsHintInfo params = new ParamsHintInfo();

        public MethodHintInfo() {
        }

        public MethodHintInfo(JSONObject data) {
            fromJson(data);
        }

        public void fromJson(JSONObject data) {
            params.clear();

            name = getJsonData(data, "name");
            title = getJsonData(data, "title");
            prototype = getJsonData(data, "proc");
            replace = getJsonData(data, "replace");
            result = getJsonData(data, "result");
            JSONArray paramDatas = getJsonData(data, "param");
            if (paramDatas != null && paramDatas.length() > 0) {
                params.fromJson(paramDatas);
            }
        }

        public void fill(CompletionResultSet result) {
            fillMethodResult(this, result, PREFIX);
        }

        public void fill(String methodKey, ObjectHintInfo objectHintInfo, CompletionResultSet result) {
            if (methodKey.equalsIgnoreCase(name))
                fillMethodResult(this, result, objectHintInfo.name + ".");
        }

    }

    public static class ObjectHintInfo extends MethodHintInfo {
        public Map<String, MethodHintInfo> methods = new ConcurrentHashMap<>();

        public ObjectHintInfo() {
            super();
        }

        public ObjectHintInfo(JSONObject data) {
            super();
            fromJson(data);
        }

        public void fromJson(JSONObject data) {
            super.fromJson(data);
            methods.clear();
            JSONArray methodDatas = data.getJSONArray("method");
            for (int i = 0; i < methodDatas.length(); i++) {
                MethodHintInfo methodHintInfo = new MethodHintInfo(methodDatas.getJSONObject(i));
                methods.put(methodHintInfo.name, methodHintInfo);
            }
        }

        public void fill(CompletionResultSet result) {
            fillObjResult(this, result, PREFIX);
            for (MethodHintInfo info : methods.values()) {
                info.fill(result);
            }
        }

        public void fill(String methodKey, CompletionResultSet result) {
            if (methodKey == null || methodKey.isEmpty())
                fillObjResult(this, result, PREFIX);
            else
                for (MethodHintInfo info : methods.values()) {
                    info.fill(methodKey, this, result);
                }
        }

    }

    public static class ObjectHintInfos extends ConcurrentHashMap<String, ObjectHintInfo> {

        public ObjectHintInfos() {
        }

        public ObjectHintInfos(File dir) throws Exception {
            init(dir);
        }

        protected String readResourceFileText(String path) throws IOException {
            try (BufferedInputStream stream = new BufferedInputStream(
                    ObjectHintInfo.class.getResourceAsStream(path));) {
                byte[] buffer = new byte[stream.available()];
                int start = 0;
                while (stream.available() > 0) {
                    int len = stream.read(buffer, start, stream.available());
                    start += len;
                }
                String text = new String(buffer, "utf8");
                return text;
            }
        }

        protected List<String> getResourceFileNames() throws IOException {
            return Arrays.asList(readResourceFileText("/configure/commands/index.dat"));
        }

        public void init(File dir) throws Exception {
            clear();
            List<String> commandFileNames = getResourceFileNames();
            if (commandFileNames == null || commandFileNames.size() == 0)
                return;

            for (String name : commandFileNames) {
                String text = readResourceFileText("/configure/commands/" + name);
                JSONObject data = (JSONObject) JsonHelp.parseJson(text);
                ObjectHintInfo obj = new ObjectHintInfo(data);
                put(obj.name, obj);
            }
        }

        public void fill(String key, CompletionResultSet result) {
            String[] keys = key == null ? new String[]{null} : key.split("\\.");
            ObjectHintInfo objectHintInfo = keys[0] == null ? null : get(key);
            if (keys[0] == null || objectHintInfo == null)
                for (ObjectHintInfo info : values()) {
                    info.fill(result);
                }
            else {
                if (keys.length == 1)
                    objectHintInfo.fill(result);
                else
                    objectHintInfo.fill(keys[1], result);
            }
        }

    }

//    public GaeaCompletionContributor() {
//        extend(CompletionType.BASIC, PlatformPatterns.psiElement(SimpleTypes.VALUE),
//                new CompletionProvider<>() {
//                    public void addCompletions(@NotNull CompletionParameters parameters,
//                                               @NotNull ProcessingContext context,
//                                               @NotNull CompletionResultSet resultSet) {
//                        resultSet.addElement(LookupElementBuilder.create("最终回车显示文本——也可以用来做匹配本文")
//                                .withLookupString("my额外的匹配文本1")
//                                .withLookupString("my额外的匹配文本2")
//                                .withPresentableText("一级提示文本")
//                                .withCaseSensitivity(true)//大小写不敏感
//                                .appendTailText("二级提示文本", true)
//                                //.withIcon()
//                                .withItemTextForeground(Color.BLUE)//一级提示文本颜色
//                                .withInsertHandler(new InsertHandler<LookupElement>() {
//                                    @Override
//                                    public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
//                                        Document document = context.getDocument();
//                                        int insertPosition = context.getSelectionEndOffset();
//                                        document.insertString(insertPosition, "  => ");
//                                        insertPosition += 5;
//                                        context.getEditor().getCaretModel().getCurrentCaret().moveToOffset(insertPosition);
//                                    }
//                                })
//                                .withStrikeoutness(true)//添加表示废弃的删除线
//                                .withTypeText("最右侧提示文本")
//                                .bold());
//                    }
//                }
//        );
//    }

}