package com.tonni.efolder;

import java.io.File;

public interface OnFileSelectedListener {
    void OnFileClicked(File file);

    void OnFileLongClicked(File file,int position_);
}
