package com.mobium.reference.fragments.support;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.mobium.reference.R;
import com.mobium.reference.fragments.BasicContentFragment;
import com.mobium.config.common.Config;



/**
 *   04.02.15.
 * http://mobiumapps.com/
 */
public class DevSettings extends BasicContentFragment {
    private EditText processorTextEdit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dev_settings, container, false);
        Button procesorButton = (Button) view.findViewById(R.id.ProcessorButton);
        processorTextEdit = (EditText) view.findViewById(R.id.processorEdit);
        procesorButton.setOnClickListener(v -> setNewProcessor(processorTextEdit.getText().toString()));
        update();
        return view;
    }

    private void update() {
        String processor = Config.get().getStaticData().mobiumApiUrl();
        processorTextEdit.setText(processor);
    }
    private void setNewProcessor(String processor) {

        getApplication().getCategories().clear();
        getApplication().getCart().clear();

        update();
    }
}