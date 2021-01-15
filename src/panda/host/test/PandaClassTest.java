package panda.host.test;

import org.junit.jupiter.api.Test;
import panda.host.model.models.filters.PostFilter;
import panda.host.utils.Panda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static panda.host.utils.Panda.*;

class PandaClassTest {
    String pandaCode = "panda@getpost?rescode='404'&data='Nothing'";

    @Test
    void retrieveDataFromServerResponse() {
        // Getting request format
        String getPostRequestFormat = Panda.PANDA_ENCODING_PATTERNS.get(Panda.PandaOperation.PANDAOP_REQUEST_GET_POSTS);
        // I create a PostFilter to add filters in my request
        PostFilter filter = new PostFilter(true, "pdf", "3iac_tic_pam_2");
        // Creating the panda code to request the posts list
        pandaCode = String.format(getPostRequestFormat, filter.isAll(), filter.getFileType(), filter.getSchoolClassId());
        System.out.println(String.format("[Test] | Requesting posts using the panda code: '%s'.", pandaCode));

        assertNotNull(extractFiltersFromPandaCode(pandaCode));
    }

    @Test
    void getOperationFromPandaCode(){
        PandaOperation pandaOperation = getOperationTypeFromPandaCode(pandaCode);
        System.out.println(pandaOperation);
        assertEquals(PandaOperation.PANDAOP_RESPONSE_GET_POSTS, pandaOperation);
    }
}