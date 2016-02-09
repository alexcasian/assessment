package com.mtt.assessment.adapter.json;

import com.mtt.assessment.message.Message;

public interface JsonAdapter {

    <JSON, MSG extends Message> JSON toJson(MSG message);
}
