package com.app.pixstory.ui.navigation_view.faqs;

import com.app.pixstory.base.BaseNavigator;
import com.app.pixstory.data.model.api.FAQResponse;
import java.util.List;

public interface FAQNavigator extends BaseNavigator {

    void faqFetched(List<FAQResponse.Data> data);
}
