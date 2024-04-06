package com.example.demo.editor;

import java.beans.PropertyEditorSupport;

public class LowercaseTextEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        super.setValue(text.toLowerCase());
    }

}
